package net.original_gamers.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SuperKeysPlugin extends JavaPlugin {

  @Override
  public void onEnable() {
    KeyTierManager tierManager = new KeyTierManager(new FileSystem(this));
    getServer().getPluginManager().registerEvents(new SuperEventHandler(tierManager), this);
    getCommand("tier_add").setExecutor(new CreateTierCommand(tierManager));
  }
}
