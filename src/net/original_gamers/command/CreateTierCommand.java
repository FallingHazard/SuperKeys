package net.original_gamers.command;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.RequiredArgsConstructor;
import net.original_gamers.managers.KeyTierManager;
import net.original_gamers.plugin.KeyTier;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class CreateTierCommand implements CommandExecutor {
  private final KeyTierManager manager;
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label,
      String[] args) {
    if (sender instanceof Player && sender.isOp()) {
      Player cmdSender = (Player) sender;
      
      if (args.length >= 1) {
        Block targetBlock = cmdSender.getTargetBlock((Set<Material>) null, 2);
        
        if (targetBlock.getType() != Material.AIR) {
          if (!targetBlock.hasMetadata("crateId")) {
            int keySpawnChance = args.length >= 2 
                ? Integer.parseInt(args[1]) : 2000000;
                
            KeyTier newTier = new KeyTier(args[0], targetBlock.getLocation(), keySpawnChance);
            
            if (!manager.tierAlreadyExists(newTier)) {                  
              manager.addKeyTier(newTier);
              
              String newTierMsg 
                = String.format("New tier %s added! Key spawn Chance: 1/%s", 
                                args[0], keySpawnChance);
              cmdSender.sendMessage(newTierMsg);
            }
            else {
              cmdSender.sendMessage("That tier already exists");
            }
          }
          else {
            cmdSender.sendMessage("That Block is already a crate!");
          }
        } 
        else {
          cmdSender.sendMessage("You have not selected a block!");
        }
      }
      else {
        cmdSender.sendMessage("Invalid Arguments!");;
      }
    }
    
    return false;
  }

}
