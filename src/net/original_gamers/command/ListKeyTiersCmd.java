package net.original_gamers.command;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lombok.RequiredArgsConstructor;
import net.original_gamers.managers.KeyTierManager;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class ListKeyTiersCmd implements CommandExecutor {
  private final KeyTierManager tierManager;

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (sender.isOp()) {
      sender.sendMessage("Key Tiers: ");
      for (String tierName : tierManager.getTierNames()) {
        sender.sendMessage(tierName);
      }
      
      sender.sendMessage("~.");
    }
    
    return false;
  }

}
