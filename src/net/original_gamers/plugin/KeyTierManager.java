package net.original_gamers.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class KeyTierManager {
  private final Map<String, KeyTier> keyTiers = new HashMap<String, KeyTier>();
  private final FileSystem fileSystem;
  private final JavaPlugin plug;
  
  public KeyTierManager(FileSystem theSystem, JavaPlugin plugin) {
    fileSystem = theSystem;
    plug = plugin;
    
    loadCrates();
    
    plug.getServer().getScheduler().scheduleSyncRepeatingTask(plug, new BukkitRunnable() {
      
      @Override
      public void run() {
        saveKeyTiers();
      }
    }, 0L, 20L);
  }
  
  public void loadCrates() {
    File[] crateFiles = new File(plug.getDataFolder(), "crates").listFiles();
    
    if (crateFiles != null)
      for (File crateFile : crateFiles) {
        
        Map<String, Object> tierData 
          = YamlConfiguration.loadConfiguration(crateFile).getConfigurationSection("Key Tier").getValues(false);
        
        KeyTier newTier = new KeyTier(tierData);
        keyTiers.put(newTier.getName(), newTier);
      }
  }
  
  public KeyTier getKeyTier(String tierName) {
    return keyTiers.get(tierName);
  }
  
  // Returns true if added to empty slot
  public boolean addKeyTier(KeyTier newKeyTier) {
    return keyTiers.put(newKeyTier.getName(), newKeyTier) == null;
  }
  
  // Returns true if something was removed
  public boolean removeKeyTier(String tierName) {
    return keyTiers.remove(tierName) != null;
  }
  
  public boolean tierAlreadyExists(KeyTier tierToTest) {
    return keyTiers.containsValue(tierToTest);
  }
  
  public void saveKeyTiers() {
    for (KeyTier tier : keyTiers.values()) {
      File tierConfigFile
        = fileSystem.getAConfigFile(String.format("crates/%s.yml", tier.getName()));
      
      FileConfiguration tierConfig 
        = YamlConfiguration.loadConfiguration(tierConfigFile);
      
      tierConfig.set("Key Tier", tier.serialize());
      
      // For now
      try {
        tierConfig.save(tierConfigFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
