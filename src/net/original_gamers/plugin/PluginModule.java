package net.original_gamers.plugin;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects= {SuperKeysCore.class} )
public class PluginModule {
  private final SuperKeysPlugin plugin;
  
  public PluginModule(SuperKeysPlugin thePlugin) {
    plugin = thePlugin;
  }
  
  @Provides public SuperKeysPlugin providesPlugin() {
    return plugin;
  }

}
