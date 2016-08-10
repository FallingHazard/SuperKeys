package net.original_gamers.plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class FileSystem {
  private final SuperKeysPlugin plugin;
  private final File pluginFolder;
  private final File configFile;
  private final FileConfiguration config;
  private final Map<String, File> configFileCache;
  
  @Inject
  public FileSystem(SuperKeysPlugin somePlugin) {
    plugin = somePlugin;
    
    configFileCache = new HashMap<String, File>();
    
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
    File cachedFile = configFileCache.get(path);
    
    if (cachedFile != null)
      return cachedFile;
    
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
