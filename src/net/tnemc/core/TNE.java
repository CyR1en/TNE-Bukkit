package net.tnemc.core;

import com.github.tnerevival.Metrics;
import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.common.EconomyManager;
import net.tnemc.core.common.TNESQLManager;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.module.Module;
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

  private Map<String, Module> modules = new HashMap<>();
  private Map<String, WorldManager> worldManagers = new HashMap<>();

  public List<CommandSender> debuggers = new ArrayList<>();

  public EconomyManager manager;

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
    //Run the ModuleLoader
    modules = new ModuleLoader().load();

    instance = this;
    super.onEnable();

    getServer().getWorlds().forEach(world->{
      MISCUtils.debug("Adding world manager for world: " + world.getName());
      worldManagers.put(world.getName(), new WorldManager(world.getName()));
    });

    //Load modules
    modules.forEach((key, value)->{
      value.load(this);
    });

    //Commands
    modules.forEach((key, value)->{
      value.registerCommands(getCommandManager());
    });

    //Configurations
    loadConfigurations();
    configurations().loadAll();
    MainConfigurations main = new MainConfigurations();
    modules.forEach((key, value)->{
      value.registerMainConfigurations(main);
    });
    modules.forEach((key, value)->{
      value.registerConfigurations(configurations());
    });
    configurations().add(main, "main");
    configurations().updateLoad();

    //SQL-related variables
    cache = true;
    saveFormat = "mysql";
    update = configurations().getInt("Core.SQL.Transactions.Update");

    sqlManager = new TNESQLManager(configurations().getString("Core.SQL.Host"), configurations().getInt("Core.SQL.Port"),
        configurations().getString("Core.SQL.Database"), configurations().getString("Core.SQL.User"),
        configurations().getString("Core.SQL.Password"), configurations().getString("Core.SQL.Prefix"),
        "", "", ""
    );
    saveManager = new SaveManager(sqlManager);
    saveManager.initialize();
    modules.forEach((key, value)->{
      value.enableSave(saveManager);
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
    modules.forEach((key, value)->{
      value.disableSave(saveManager);
    });
    modules.forEach((key, value)->{
      value.unload(this);
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
    modules.forEach((key, value)->{
      value.initializeConfigurations();
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
    modules.forEach((key, value)->{
      value.loadConfigurations();
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
      modules.forEach((key, value)->{
        value.saveConfigurations();
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