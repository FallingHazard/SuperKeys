package net.original_gamers.command;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lombok.RequiredArgsConstructor;
import net.original_gamers.managers.KeyTierManager;
import net.original_gamers.plugin.KeyTier;
import net.original_gamers.plugin.SuperKeysCore;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class SpawnKeyCmd implements CommandExecutor {
  private final KeyTierManager tierManager;
  
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
          
          ItemStack keyItem = SuperKeysCore.createKeyItem(tierDisplayName, amtToSpawn);
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
