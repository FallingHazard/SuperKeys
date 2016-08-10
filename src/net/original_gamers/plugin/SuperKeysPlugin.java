package net.original_gamers.plugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import dagger.ObjectGraph;

public class SuperKeysPlugin extends JavaPlugin {

  @Override
  public void onLoad() {
    ConfigurationSerialization.registerClass(KeyTier.class);
  }

  @Override
  public void onEnable() {
    ObjectGraph.create(new PluginModule(this)).get(SuperKeysCore.class).enable();
  }
  
  
  public void registerEventListener(Listener listener) {
    getServer().getPluginManager().registerEvents(listener, this);
  }
  
  public void registerCommand(String label, CommandExecutor executor) {
    getCommand(label).setExecutor(executor);
  }
}
