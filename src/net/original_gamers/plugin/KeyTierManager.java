package net.original_gamers.plugin;

import java.util.HashMap;
import java.util.Map;

public class KeyTierManager {
  private final Map<String, KeyTier> keyTiers = new HashMap<String, KeyTier>();
  private final FileSystem fileSystem;
  
  public KeyTierManager(FileSystem theSystem) {
    fileSystem = theSystem;
  }
  
  public KeyTier getKeyTier(String tierName) {
    return keyTiers.get(tierName);
  }
  
  // Returns true if added to empty slot
  public boolean addKeyTier(KeyTier newKeyTier) {
    return keyTiers.put(newKeyTier.getName(), newKeyTier) == null;
  }
  
  // Returns true if something was removed
  public boolean removeKeyTier(String tierName) {
    return keyTiers.remove(tierName) != null;
  }
  
  public boolean tierAlreadyExists(KeyTier tierToTest) {
    return keyTiers.containsValue(tierToTest);
  }
  
  public void saveKeyTiers() {
    for (KeyTier tier : keyTiers.values()) {
      
    }
  }
}
