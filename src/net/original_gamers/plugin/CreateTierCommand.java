package net.original_gamers.plugin;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateTierCommand implements CommandExecutor {
  private final KeyTierManager manager;
  
  public CreateTierCommand(KeyTierManager newManager) {
    manager = newManager;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (sender instanceof Player && sender.isOp()) {
      Player cmdSender = (Player) sender;
      
      if (args.length >= 1) {
        Block targetBlock = cmdSender.getTargetBlock((Set<Material>) null, 2);
        
        if (targetBlock.getType() != Material.AIR) {
          KeyTier newTier = new KeyTier(args[0], targetBlock.getLocation());
          
          if (!manager.tierAlreadyExists(newTier)) {
            manager.addKeyTier(newTier);
            cmdSender.sendMessage("Tier Created");
          }
          else {
            cmdSender.sendMessage("That tier already exists");
          }
        } 
        else {
          cmdSender.sendMessage("You have not selected a block!");
        }
      }
    }
    
    return false;
  }

}
