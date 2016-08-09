package net.original_gamers.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class SuperKeysPlugin extends JavaPlugin {

  @Override
  public void onLoad() {
    ConfigurationSerialization.registerClass(KeyTier.class);
  }

  @Override
  public void onEnable() {
    FileSystem fileSystem = new FileSystem(this);
    KeyTierManager tierManager = new KeyTierManager(fileSystem, this);
    getServer().getPluginManager().registerEvents(new SuperEventHandler(tierManager, fileSystem), this);
    getCommand("tier_add").setExecutor(new CreateTierCommand(tierManager));
    getCommand("key_spawn").setExecutor(new SpawnKeyCmd(tierManager));
  }
  
  public static ItemStack createKeyItem(String name, int amount) {
    ItemStack keyItem = new ItemStack(Material.REDSTONE_TORCH_OFF);
    ItemMeta keyMeta = keyItem.getItemMeta();
    
    keyMeta.setDisplayName(name);
    
    List<String> lore = new ArrayList<String>();
    lore.add(ChatColor.MAGIC + (ChatColor.AQUA + "This is a super key"));
    lore.add(name);
    lore.add(ChatColor.RED + "Redeem at the Legends Plots");
    keyMeta.setLore(lore);
    
    keyItem.setItemMeta(keyMeta);
    
    keyItem.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
    keyItem.setAmount(amount);
    return keyItem;
  }
}
