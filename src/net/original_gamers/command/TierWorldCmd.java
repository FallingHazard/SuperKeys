package net.original_gamers.command;

import javax.inject.Inject;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.RequiredArgsConstructor;
import net.original_gamers.plugin.AllowedWorldsData;

@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class TierWorldCmd  implements CommandExecutor {
  private final AllowedWorldsData allowedWorldData;
  

  @Override
  public boolean onCommand(CommandSender sender, Command arg1, String label,
      String[] args) {
    if (sender instanceof Player && sender.isOp()) {
      Player cmdSender = (Player) sender;
      
      if (args.length >= 1) {
        switch (args[0]) {
        case "addworld":
          World worldToAdd = cmdSender.getWorld();
          
          if (allowedWorldData.allowNewWorld(worldToAdd)) {
            String successMsg = String.format("World: %s now spawns keys!", worldToAdd.getName());
            cmdSender.sendMessage(successMsg);
          }
          else {
            cmdSender.sendMessage("That world already spawn keys!");
          }
          break;
          
        case "removeworld":
          World worldToRemove = cmdSender.getWorld();
          if (allowedWorldData.removeAllowedWorld(worldToRemove)) {
            String successMsg = String.format("World: %s no long spawns keys!", 
                                              worldToRemove.getName());
            cmdSender.sendMessage(successMsg);
          }
          else {
            cmdSender.sendMessage("That world already does not spawn keys!");
          }
          break;
          
        case "listworlds":
          cmdSender.sendMessage("Allowed Worlds: ");
          
          for (String allowedWorld : allowedWorldData.getWorldList()) {
            cmdSender.sendMessage(allowedWorld);
          }
          
          cmdSender.sendMessage("~.");
          break;
          
        default:
          cmdSender.sendMessage("Invalid Args");
          break;
        }
      }
    }
    return false;
  }

}
