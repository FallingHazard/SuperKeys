package net.original_gamers.plugin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.original_gamers.command.CreateTierCommand;
import net.original_gamers.command.ListKeyTiersCmd;
import net.original_gamers.command.RemoveKeyTierCmd;
import net.original_gamers.command.SpawnKeyCmd;
import net.original_gamers.command.TierWorldCmd;
import net.original_gamers.managers.KeyTierManager;
import net.original_gamers.managers.SuperEventHandler;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class SuperKeysCore {
  private final CreateTierCommand createTier;
  private final ListKeyTiersCmd listTiers;
  private final RemoveKeyTierCmd removeTiers;
  private final SpawnKeyCmd spawnKey;
  private final TierWorldCmd tierWorldCmd;
  
  private final KeyTierManager tierManager;
  private final SuperEventHandler eventHandler;
  private final AllowedWorldsData allowedWorldData;
  
  private final SuperKeysPlugin plugin;
  
  public void enable() {
    plugin.registerEventListener(eventHandler);
    
    plugin.registerCommand("tier_add", createTier);
    plugin.registerCommand("key_spawn", spawnKey);
    plugin.registerCommand("tier_list", listTiers);
    plugin.registerCommand("remove_tier", removeTiers);
    plugin.registerCommand("tierworlds", tierWorldCmd);
    
    tierManager.loadCrates();
    allowedWorldData.loadAllowedWorlds();
  }

  public static ItemStack createKeyItem(String name, int amount) {
    ItemStack keyItem = new ItemStack(Material.LEVER);
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
