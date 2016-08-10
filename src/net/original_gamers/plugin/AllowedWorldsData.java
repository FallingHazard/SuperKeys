package net.original_gamers.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor=@__(@Inject))
public class AllowedWorldsData {
  private Set<UUID> allowedWorlds = new HashSet<UUID>();
  private final FileSystem fileSystem;
  
  public boolean allowNewWorld(World worldToAllow) {
    boolean completed = allowedWorlds.add(worldToAllow.getUID());
    if (completed) {
      saveAllowedWorlds();
    }
    
    return completed;
  }
  
  public boolean removeAllowedWorld(World toDisAllow) {
    boolean completed = allowedWorlds.remove(toDisAllow.getUID());
    if (completed) {
      saveAllowedWorlds();
    }
    
    return completed;
  }
  
  public void saveAllowedWorlds() {
    FileConfiguration config = fileSystem.getBaseConfig();
    List<String> toSave = new ArrayList<String>();
    
    for (UUID allowedWorld : allowedWorlds) {
      toSave.add(allowedWorld.toString());
    }
    
    config.set("Allowed-Worlds", toSave);
    
    fileSystem.saveBase();
  }

  public boolean worldIsAllowed(World world) {
    return allowedWorlds.contains(world.getUID());
  }
  
  public List<String> getWorldList() {
    List<String> worldList = new ArrayList<String>();
    
    for (UUID worldUID : allowedWorlds) {
      worldList.add(Bukkit.getWorld(worldUID).getName());
    }
    
    return worldList;
  }
  
  public void loadAllowedWorlds() {
    List<String> loadedAllowedWorlds 
      = (List<String>) fileSystem.getBaseConfig().getList("Allowed-Worlds");
    
    for (String allowedWorldName : loadedAllowedWorlds) {
      allowedWorlds.add(UUID.fromString(allowedWorldName));
    }
  }
}
