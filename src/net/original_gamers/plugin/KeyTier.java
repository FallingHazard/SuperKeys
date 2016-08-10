package net.original_gamers.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.avaje.ebeaninternal.server.subclass.MethodWriteReplace;

import lombok.Getter;

public class KeyTier implements ConfigurationSerializable {
  @Getter private final Location tierChestLocation;
  private final String tierName;
  private final Inventory rewardInv;
  
  private String displayName;
  
  public static Plugin plugin = Bukkit.getPluginManager().getPlugin("SuperKeys");
  
  private int keySpawnChance;
  
  public static final int DEFAULT_SPAWN_CHANCE = 2000000000;
  
  public KeyTier(String newDisplayName, Location newTierChestLocation) {
    this(newDisplayName, newTierChestLocation, DEFAULT_SPAWN_CHANCE);
  }
  
  public KeyTier(String newDisplayName, Location newTierChestLocation, 
                 int newKeySpawnChance) {
    this(newDisplayName, newTierChestLocation, newKeySpawnChance, new ItemStack[54]);
  }
  
  public KeyTier(String newDisplayName, Location newTierChestLocation,
                 int newKeySpawnChance, ItemStack[] rewards) {
    displayName = ChatColor.translateAlternateColorCodes('&', newDisplayName);
    tierName = ChatColor.stripColor(displayName);
    tierChestLocation = newTierChestLocation;
    
    String invRewardTitle = String.format("The Special Crates:%s", tierName);
    rewardInv = Bukkit.createInventory(null, 54, invRewardTitle);
    
    keySpawnChance = newKeySpawnChance;
    
    tierChestLocation
      .getBlock()
        .setMetadata("crateId", new FixedMetadataValue(plugin, tierName));
  }
  
  public KeyTier(Map<String, Object> tierData) {
    tierName = (String) tierData.get("TierName");
    keySpawnChance = (int) tierData.get("SpawnChance");
    displayName = (String) tierData.get("DisplayName");
 
    String invRewardTitle = String.format("The Special Crates:%s", tierName);
    rewardInv = Bukkit.createInventory(null, 54, invRewardTitle);
    
    List<Map<String, Object>> contents 
      = (List<Map<String, Object>>) tierData.get("Contents");
    
    List<ItemStack> invItems = new ArrayList<ItemStack>();
    for (Map<String, Object> itemMap : contents) {
      invItems.add(ItemStack.deserialize(itemMap));
    }
    ItemStack[] invArray = invItems.toArray(new ItemStack[invItems.size()]);
    rewardInv.setContents(invArray);
    
    Map<String, Object> serialChestLoc = ((MemorySection) tierData.get("ChestLoc")).getValues(false);
    tierChestLocation = Location.deserialize(serialChestLoc);
    
    tierChestLocation
      .getBlock().setMetadata("crateId", new FixedMetadataValue(plugin, tierName));
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof KeyTier) {
      return tierName.equalsIgnoreCase(((KeyTier) obj).getName());
    }
    
    return super.equals(obj);
  }
  
  public String getName() {
    return tierName;
  }
  
  public void setSpawnChance(int newChance) {
    keySpawnChance = newChance;
  }
  
  public ItemStack getRandomReward() {
    // Bad optimisation whatever..
    Random random = new Random();
    
    return rewardInv.getContents()[random.nextInt(54)].clone();
  }
  
  public int getKeyChance() {
    return keySpawnChance;
  }
  
  public Inventory getRewardInv() {
    return rewardInv;
  }
  
  @Override
  public Map<String, Object> serialize() {
    Map<String, Object> serialTier = new TreeMap<String, Object>();
    serialTier.put("TierName", tierName);
    serialTier.put("SpawnChance", keySpawnChance);
    serialTier.put("DisplayName", displayName);
    
    List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
    for (ItemStack someItem : rewardInv.getContents()) {
      if (someItem != null)
        contents.add(someItem.serialize());
      else
        contents.add(new ItemStack(Material.AIR).serialize());
    }
    serialTier.put("Contents", contents);
    
    serialTier.put("ChestLoc", tierChestLocation.serialize());
    
    return serialTier;
  }

  public String getDisplayName() {
    return displayName;
  }
  
}
