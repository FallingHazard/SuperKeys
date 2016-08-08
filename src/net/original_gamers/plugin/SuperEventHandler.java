package net.original_gamers.plugin;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class SuperEventHandler implements Listener {
  private final KeyTierManager manager;
  
  public SuperEventHandler(KeyTierManager newManager) {
    manager = newManager;
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
      else {
        clicker.sendMessage("That is not a crate");
      }
      
    }
  }

  public static boolean itemIsAKey(ItemStack someItem) {
    return someItem.getType().equals(Material.LEVER) 
           && someItem.getEnchantmentLevel(Enchantment.DURABILITY) == 25
           && someItem.getItemMeta().getLore().get(0).equals(ChatColor.stripColor("Super Key"));
  }
  
}

