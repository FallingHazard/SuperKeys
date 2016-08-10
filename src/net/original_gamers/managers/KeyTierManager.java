package net.original_gamers.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.RequiredArgsConstructor;
import net.original_gamers.command.CreateTierCommand;
import net.original_gamers.command.ListKeyTiersCmd;
import net.original_gamers.command.RemoveKeyTierCmd;
import net.original_gamers.command.SpawnKeyCmd;
import net.original_gamers.plugin.FileSystem;
import net.original_gamers.plugin.KeyTier;
import net.original_gamers.plugin.SuperKeysCore;
import net.original_gamers.plugin.SuperKeysPlugin;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class KeyTierManager {
  private Map<String, KeyTier> keyTiers = new HashMap<String, KeyTier>();
  
  private final FileSystem fileSystem;
  private final SuperKeysPlugin plug;
  
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
    boolean success = keyTiers.put(newKeyTier.getName(), newKeyTier) == null;
    
    if (success) {
      saveOneKeyTier(newKeyTier);
    }
    
    return success;
  }
  
  // Returns true if something was removed
  public boolean deleteKeyTier(String tierName) {
    File tierConfigFile
      = fileSystem.getAConfigFile(String.format("crates/%s.yml", tierName));
    
    tierConfigFile.delete();
    KeyTier tierToRemove = keyTiers.remove(tierName);
    
    if (tierToRemove != null)
      tierToRemove.getTierChestLocation().getBlock().removeMetadata("crateId", plug);
    
    return tierToRemove != null;
  }
  
  public boolean tierAlreadyExists(KeyTier tierToTest) {
    return keyTiers.containsValue(tierToTest);
  }
  
  public void saveAllKeyTiers() {
    for (KeyTier tier : keyTiers.values()) {
      saveOneKeyTier(tier);
    }
  }
  
  public void saveOneKeyTier(String tierName) {
    saveOneKeyTier(keyTiers.get(tierName));
  }
  
  public List<ItemStack> maybeTriggerKeySpawn() {
    Random random = new Random();
    List<ItemStack> spawnedKeys = new LinkedList<ItemStack>();
    
    for (KeyTier tier : keyTiers.values()) {
      if (random.nextInt(tier.getKeyChance()) == 0) {
        spawnedKeys.add(SuperKeysCore.createKeyItem(tier.getDisplayName(), 1));
      }
    }
    
    return spawnedKeys;
  }
  
  public void saveOneKeyTier(KeyTier tier) {
    File tierConfigFile
    = fileSystem.getAConfigFile(String.format("crates/%s.yml", tier.getName()));
  
    FileConfiguration tierConfig 
      = YamlConfiguration.loadConfiguration(tierConfigFile);
    
    tierConfig.set("Key Tier", tier.serialize());
    
    plug.getServer().getScheduler().scheduleAsyncDelayedTask(plug, new Runnable() {
      
      @Override
      public void run() {
        try {
            tierConfig.save(tierConfigFile);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }, 1L);
  }

  public Set<String> getTierNames() {
    return keyTiers.keySet();
  }
}
