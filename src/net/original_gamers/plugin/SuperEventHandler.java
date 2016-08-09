package net.original_gamers.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SuperEventHandler implements Listener {
  private final KeyTierManager manager;
  private final FileSystem fileSystem;
  private final List<UUID> keyWorlds = new ArrayList<UUID>();
  
  public SuperEventHandler(KeyTierManager newManager, FileSystem system) {
    manager = newManager;
    fileSystem = system;
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onBreak(BlockBreakEvent event) {
    Block brokenBlock = event.getBlock();
    if (brokenBlock.hasMetadata("crateId")) {
      Player breaker = event.getPlayer();
      
      if (breaker.isOp()) {
        String crateName = brokenBlock.getMetadata("crateId").get(0).asString();
        
        if (manager.removeKeyTier(crateName)) {
          breaker.sendMessage("Tier deleted");
        }
        else {
          breaker.sendMessage("Weird error");;
        }
      }
      else {
        event.setCancelled(true);
        breaker.sendMessage(ChatColor.RED + "Don't break mikemys' crates!");
      }
    }
  }
  
  @EventHandler(priority=EventPriority.MONITOR)
  public void onKeyBlockBreak(BlockBreakEvent event) {
    Block brokenBlock = event.getBlock();
    if (keyWorlds.contains(brokenBlock.getLocation().getWorld().getUID())) {
      if (!event.isCancelled()) {
        List<ItemStack> spawnedKeys = manager.maybeTriggerKeySpawn();
        
        Player breaker = event.getPlayer();
        
        for (ItemStack key : spawnedKeys) {
          breaker.getInventory().addItem(key);
          String keyMsg = String.format("You got a %s key!", key.getItemMeta().getDisplayName());
          breaker.sendMessage(keyMsg);
        }
      }
    }
  }
  
  @EventHandler
  public void onInvClose(InventoryCloseEvent event) {
    String invTitle = event.getInventory().getTitle();
    if (invTitle.contains("The Special Crates")) {
      manager.saveOneKeyTier(invTitle.split(":")[1]);
    }
  }
  
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onInteract(PlayerInteractEvent event) {
    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Block clickedBlock = event.getClickedBlock();
      
      Player clicker = event.getPlayer();
      
      if (clickedBlock.hasMetadata("crateId")) {
        event.setCancelled(true);
        
        ItemStack heldItem = clicker.getItemInHand();
        
        String clickedCrateName = clickedBlock.getMetadata("crateId").get(0).asString();
        
        if (itemIsAKey(heldItem)) {
          String keyType 
            = ChatColor.stripColor(heldItem.getItemMeta().getLore().get(1));
            
          if (clickedCrateName.equals(keyType)) {
            KeyTier rewardTier = manager.getKeyTier(keyType);
            
            int newAmountInHand = heldItem.getAmount() - 1;
            if (newAmountInHand <= 0) {
              clicker.setItemInHand(new ItemStack(Material.AIR));
            }
            else {
              heldItem.setAmount(newAmountInHand);
              clicker.setItemInHand(heldItem);
            }
            
            ItemStack reward = rewardTier.getRandomReward();
          }
          else {
            clicker.sendMessage(String.format("That requires a %s key!", clickedCrateName));
          }
        }
        else if (clicker.isOp()) {
          KeyTier clickedCrateTier = manager.getKeyTier(clickedCrateName);
          
          clicker.openInventory(clickedCrateTier.getRewardInv());
          clicker.sendMessage("Admin edit mode engaged!");
          
        }
        else {
          clicker.sendMessage(String.format("That requires a %s key!", clickedCrateName));
        }
      }
      
    }
  }

  public static boolean itemIsAKey(ItemStack someItem) {
    return someItem.getType().equals(Material.LEVER) 
           && someItem.getEnchantmentLevel(Enchantment.DURABILITY) == 25
           && someItem.getItemMeta().getLore().get(0).equals(ChatColor.stripColor("Super Key"));
  }
  
}

