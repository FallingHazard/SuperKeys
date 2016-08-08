package net.original_gamers.plugin;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperKeysPlugin extends JavaPlugin {

  @Override
  public void onLoad() {
    ConfigurationSerialization.registerClass(KeyTier.class);
  }

  @Override
  public void onEnable() {
    KeyTierManager tierManager = new KeyTierManager(new FileSystem(this), this);
    getServer().getPluginManager().registerEvents(new SuperEventHandler(tierManager), this);
    getCommand("tier_add").setExecutor(new CreateTierCommand(tierManager));
  }
}
