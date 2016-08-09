package net.original_gamers.plugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnKeyCmd implements CommandExecutor {
  private final KeyTierManager tierManager;
  
  public SpawnKeyCmd(KeyTierManager manager) {
    tierManager = manager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (sender instanceof Player && sender.isOp()) {
      if (args.length >= 1) {
        String tierDisplayName = ChatColor.translateAlternateColorCodes('&', args[0]);
        String tierName = ChatColor.stripColor(tierDisplayName);
        KeyTier tierToSpawnKeyFor = tierManager.getKeyTier(tierName);
        
        if (tierToSpawnKeyFor != null) {
          int amtToSpawn = args.length >= 2 ? Integer.parseInt(args[1]) : 1;
          
          ItemStack keyItem = SuperKeysPlugin.createKeyItem(tierDisplayName, amtToSpawn);
          Player cmdSender = (Player) sender;
          cmdSender.setItemInHand(keyItem);
        }
        else {
          sender.sendMessage("No Tier with that name!");
        }
      }
    }
    return false;
  }

}
