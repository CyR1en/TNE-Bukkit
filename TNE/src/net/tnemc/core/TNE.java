package net.tnemc.core;

import com.github.tnerevival.Metrics;
import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.common.EconomyManager;
import net.tnemc.core.common.TNESQLManager;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.module.ModuleLoader;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNE extends TNELib {
  private Map<String, WorldManager> worldManagers = new HashMap<>();

  public List<CommandSender> debuggers = new ArrayList<>();

  public EconomyManager manager;

  private ModuleLoader loader;

  public TNESQLManager sqlManager;
  public SaveManager saveManager;
  public static boolean debugMode = false;

  // Files & Custom Configuration Files
  public File items;
  public File messages;
  public File players;
  public File worlds;

  public FileConfiguration itemConfigurations;
  public FileConfiguration messageConfigurations;
  public FileConfiguration playerConfigurations;
  public FileConfiguration worldConfigurations;

  public void onEnable() {
    instance = this;
    super.onEnable();

    //Run the ModuleLoader
    loader = new ModuleLoader();
    loader.load();

    getServer().getWorlds().forEach(world->{
      MISCUtils.debug("Adding world manager for world: " + world.getName());
      worldManagers.put(world.getName(), new WorldManager(world.getName()));
    });

    //Load modules
    loader.getModules().forEach((key, value)->{
      value.getModule().load(this);
    });

    //Commands
    loader.getModules().forEach((key, value)->{
      value.getModule().registerCommands(getCommandManager());
    });

    //Configurations
    initializeConfigurations();
    loadConfigurations();
    configurations().loadAll();
    MainConfigurations main = new MainConfigurations();
    loader.getModules().forEach((key, value)->{
      value.getModule().registerMainConfigurations(main);
    });
    loader.getModules().forEach((key, value)->{
      value.getModule().registerConfigurations(configurations());
    });
    configurations().add(main, "main");
    configurations().updateLoad();

    //SQL-related variables
    cache = configurations().getBoolean("Core.Database.Transactions.Cache");
    saveFormat = configurations().getString("Core.Database.Type");
    update = configurations().getInt("Core.Database.Transactions.Update");

    sqlManager = new TNESQLManager(configurations().getString("Core.Database.MySQL.Host"), configurations().getInt("Core.Database.MySQL.Port"),
        configurations().getString("Core.Database.MySQL.Database"), configurations().getString("Core.Database.MySQL.User"),
        configurations().getString("Core.Database.MySQL.Password"), configurations().getString("Core.Database.Prefix"),
        "", "", ""
    );
    saveManager = new SaveManager(sqlManager);
    saveManager.initialize();
    loader.getModules().forEach((key, value)->{
      value.getModule().enableSave(saveManager);
    });

    //Initialize our plugin's managers.
    manager = new EconomyManager();

    //Version Checking

    //Metrics
    if(configurations().getBoolean("Core.Metrics")) {
      new Metrics(this);
      getLogger().info("Sending plugin statistics.");
    }

    getLogger().info("The New Economy has been enabled!");
  }

  public void onDisable() {
    loader.getModules().forEach((key, value)->{
      value.getModule().disableSave(saveManager);
    });
    loader.getModules().forEach((key, value)->{
      value.getModule().unload(this);
    });
    super.onDisable();
    getLogger().info("The New Economy has been disabled!");
  }

  public static TNE instance() {
    return (TNE)instance;
  }

  public Logger logger() {
    return getServer().getLogger();
  }

  private void initializeConfigurations() {
    loader.getModules().forEach((key, value)->{
      value.getModule().initializeConfigurations();
    });
    items = new File(getDataFolder(), "items.yml");
    messages = new File(getDataFolder(), "messages.yml");
    players = new File(getDataFolder(), "players.yml");
    worlds = new File(getDataFolder(), "worlds.yml");
    itemConfigurations = YamlConfiguration.loadConfiguration(items);
    messageConfigurations = YamlConfiguration.loadConfiguration(messages);
    playerConfigurations = YamlConfiguration.loadConfiguration(players);
    worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
    try {
      setConfigurationDefaults();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadConfigurations() {
    loader.getModules().forEach((key, value)->{
      value.getModule().loadConfigurations();
    });
    this.saveDefaultConfig();
    getConfig().options().copyDefaults(true);
    super.loadConfigurations();
    itemConfigurations.options().copyDefaults(true);
    messageConfigurations.options().copyDefaults(true);
    playerConfigurations.options().copyDefaults(true);
    worldConfigurations.options().copyDefaults(true);
    saveConfigurations(false);
  }

  private void saveConfigurations(boolean check) {
    if(!check || !new File(getDataFolder(), "config.yml").exists() || configurations().changed.contains("config.yml")) {
      saveConfig();
    }
    try {
      loader.getModules().forEach((key, value)->{
        value.getModule().saveConfigurations();
      });
      if(!check || !items.exists() || configurations().changed.contains(itemConfigurations.getName())) {
        itemConfigurations.save(items);
      }
      if(!check || !messages.exists() || configurations().changed.contains(messageConfigurations.getName())) {
        messageConfigurations.save(messages);
      }
      if(!check || !players.exists() || configurations().changed.contains(playerConfigurations.getName())) {
        playerConfigurations.save(players);
      }
      if(!check || !worlds.exists() || configurations().changed.contains(worldConfigurations.getName())) {
        worldConfigurations.save(worlds);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setConfigurationDefaults() throws UnsupportedEncodingException {
    Reader itemsStream = new InputStreamReader(this.getResource("items.yml"), "UTF8");
    Reader mobsStream = new InputStreamReader(this.getResource("mobs.yml"), "UTF8");
    Reader messagesStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
    Reader objectsStream = new InputStreamReader(this.getResource("objects.yml"), "UTF8");
    Reader materialsStream = new InputStreamReader(this.getResource("materials.yml"), "UTF8");
    Reader playersStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
    Reader worldsStream = new InputStreamReader(this.getResource("worlds.yml"), "UTF8");
    if (itemsStream != null) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(itemsStream);
      itemConfigurations.setDefaults(config);
    }

    if (messagesStream != null) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesStream);
      messageConfigurations.setDefaults(config);
    }

    if (playersStream != null) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(playersStream);
      playerConfigurations.setDefaults(config);
    }

    if (worldsStream != null) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(worldsStream);
      worldConfigurations.setDefaults(config);
    }
  }

  public void addWorldManager(WorldManager manager) {
    worldManagers.put(manager.getWorld(), manager);
  }

  public WorldManager getWorldManager(String world) {
    for(WorldManager manager : this.worldManagers.values()) {
      if(manager.getWorld().equalsIgnoreCase(world)) {
        return manager;
      }
    }
    return null;
  }

  public Collection<WorldManager> getWorldManagers() {
    return worldManagers.values();
  }
}