package net.original_gamers.command;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lombok.RequiredArgsConstructor;
import net.original_gamers.managers.KeyTierManager;
import net.original_gamers.plugin.KeyTier;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class RemoveKeyTierCmd implements CommandExecutor {
  private final KeyTierManager tierManager;

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (sender.isOp()) {
      if (args.length >= 1) {
        KeyTier tierToDel = tierManager.getKeyTier(args[0]);
        
        if (tierToDel != null) {
          tierManager.deleteKeyTier(tierToDel.getName());
          String successMsg = String.format("Tier %s deleted!", tierToDel.getDisplayName());
        }
        else {
          sender.sendMessage("No such Tier");
        }
      }
      else {
        sender.sendMessage("Invalid Args");
      }
    }
    return false;
  }

}
