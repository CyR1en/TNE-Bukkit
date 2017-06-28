package com.github.tnerevival.core.configurations.impl;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.configurations.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

/**
 * Created by creatorfromhell on 6/25/2017.
 * All rights reserved.
 **/

public class WorldConfiguration extends Configuration {

  @Override
  public void load(FileConfiguration configurationFile) {
    Set<String> worldSpecifics = TNE.instance().worldConfigurations.getConfigurationSection("Worlds").getKeys(false);

    for(String world : worldSpecifics) {
      //TODO: Load world-specific configurations into their respective WorldManager.
    }
  }
}