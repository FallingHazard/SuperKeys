package net.original_gamers.plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FileSystem {
  private final JavaPlugin plugin;
  private final File pluginFolder;
  private final File configFile;
  private final FileConfiguration config;
  
  public FileSystem(JavaPlugin somePlugin) {
    plugin = somePlugin;
    
    pluginFolder = somePlugin.getDataFolder();
    
    if (!pluginFolder.exists()) {
      pluginFolder.mkdir();
    }
    
    configFile = new File(pluginFolder, "config.yml");
    
    if (!configFile.exists()) {
      try {
        configFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    config = YamlConfiguration.loadConfiguration(configFile);
  }
  
  public FileConfiguration getBaseConfig() {
    return config;
  }
  
  public void saveBase() {
    try {
      config.save(configFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  // mines/bigmines/config.yml
  public File getAConfigFile(String path) {
    String[] fileParts = path.split("/");
    
    if (fileParts.length > 1) {
      File newFolder = new File(pluginFolder, fileParts[0]);
      if (!newFolder.exists())
        newFolder.mkdir();
      
      for (int index = 1; index < fileParts.length -1; index ++) {
        newFolder = new File(new File(fileParts[index - 1]), fileParts[index]);
        if (!newFolder.exists())
          newFolder.mkdir();
      }
      
      return new File(newFolder, fileParts[fileParts.length - 1]);
    }
    else {
      return new File(pluginFolder, path);
    }
  }
  
  
  
}
