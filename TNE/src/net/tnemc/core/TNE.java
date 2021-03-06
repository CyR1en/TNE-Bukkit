package net.tnemc.core;

import com.github.tnerevival.Metrics;
import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.UpdateChecker;
import com.github.tnerevival.core.collection.EventList;
import com.github.tnerevival.core.collection.EventMap;
import net.milkbowl.vault.economy.Economy;
import net.tnemc.core.commands.admin.AdminCommand;
import net.tnemc.core.commands.config.ConfigCommand;
import net.tnemc.core.commands.currency.CurrencyCommand;
import net.tnemc.core.commands.module.ModuleCommand;
import net.tnemc.core.commands.money.MoneyCommand;
import net.tnemc.core.commands.transaction.TransactionCommand;
import net.tnemc.core.commands.yeti.YetiCommand;
import net.tnemc.core.common.EconomyManager;
import net.tnemc.core.common.TNEUUIDManager;
import net.tnemc.core.common.TransactionManager;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.api.Economy_TheNewEconomy;
import net.tnemc.core.common.api.ReserveEconomy;
import net.tnemc.core.common.api.TNEAPI;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.configurations.MessageConfigurations;
import net.tnemc.core.common.configurations.WorldConfigurations;
import net.tnemc.core.common.data.TNEDataManager;
import net.tnemc.core.common.data.TNESaveManager;
import net.tnemc.core.common.module.ModuleLoader;
import net.tnemc.core.event.module.TNEModuleLoadEvent;
import net.tnemc.core.event.module.TNEModuleUnloadEvent;
import net.tnemc.core.listeners.ConnectionListener;
import net.tnemc.core.listeners.PlayerListener;
import net.tnemc.core.menu.MenuManager;
import net.tnemc.core.worker.CacheWorker;
import net.tnemc.core.worker.SaveWorker;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;

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

  private EconomyManager manager;
  private MenuManager menuManager;

  private ModuleLoader loader;
  public UpdateChecker updater;
  private static boolean debugMode = false;

  //Economy APIs
  private Economy_TheNewEconomy vaultEconomy;
  private ReserveEconomy reserveEconomy;
  private TNEAPI api;

  // Files & Custom Configuration Files
  private File items;
  private File messagesFile;
  private File players;
  private File worlds;

  private FileConfiguration itemConfigurations;
  private FileConfiguration messageConfigurations;
  private FileConfiguration playerConfigurations;
  private FileConfiguration worldConfigurations;

  private MainConfigurations main;
  private MessageConfigurations messages;
  private WorldConfigurations world;

  //BukkitRunnable Workers
  private SaveWorker saveWorker;
  private CacheWorker cacheWorker;

  //Cache-related collections
  private List<EventList> cacheLists = new ArrayList<>();
  private List<EventMap> cacheMaps = new ArrayList<>();

  public void onLoad() {
    getLogger().info("Loading The New Economy with Java Version: " + System.getProperty("java.version"));
    instance = this;
    api = new TNEAPI(this);

    //Initialize Economy Classes
    if(getServer().getPluginManager().getPlugin("Vault") != null) {
      vaultEconomy = new Economy_TheNewEconomy(this);
      setupVault();
    }

    reserveEconomy = new ReserveEconomy(this);
  }

  public void onEnable() {
    super.onEnable();
    if(getServer().getPluginManager().getPlugin("Reserve") != null) {
      setupReserve();
    }

    //Create Debug Log
    try {
      new File(getDataFolder(), "debug.log").createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    currentSaveVersion = 10.0;

    setUuidManager(new TNEUUIDManager());

    updater = new UpdateChecker("https://creatorfromhell.com/tne/tnebuild.txt", getDescription().getVersion());

    //Run the ModuleLoader
    loader = new ModuleLoader();
    loader.load();

    //Load modules
    loader.getModules().forEach((key, value)->{
      TNEModuleLoadEvent event = new TNEModuleLoadEvent(key, value.getInfo().version());
      Bukkit.getServer().getPluginManager().callEvent(event);
      if(!event.isCancelled()) {
        value.getModule().load(this, loader.getLastVersion(value.getInfo().name()));
      }
    });

    getServer().getWorlds().forEach(world->{
      worldManagers.put(world.getName(), new WorldManager(world.getName()));
    });

    //Configurations
    initializeConfigurations();
    loadConfigurations();
    main = new MainConfigurations();
    messages = new MessageConfigurations();
    world = new WorldConfigurations();
    loader.getModules().forEach((key, value)->{
      value.getModule().getMainConfigurations().forEach((node, defaultValue)->{
        main.configurations.put(node, defaultValue);
      });
    });
    loader.getModules().forEach((key, value)->{
      value.getModule().getMessages().forEach((message, defaultValue)->{
        messages.configurations.put(message, defaultValue);
      });
    });
    loader.getModules().forEach((key, value)->{
      value.getModule().getConfigurations().forEach((configuration, identifier)->{
        configurations().add(configuration, identifier);
      });
    });
    configurations().add(main, "main");
    configurations().add(messages, "messages");
    configurations().add(world, "world");
    configurations().loadAll();

    int size = 1;
    boolean payShort = configurations().getBoolean("Core.Commands.PayShort");
    boolean balShort = configurations().getBoolean("Core.Commands.BalanceShort");
    boolean topShort = configurations().getBoolean("Core.Commands.TopShort");

    if(payShort) size += 1;
    if(balShort) size += 2;
    if(topShort) size += 1;

    int index = 0;

    String[] moneyArgs = new String[size];
    moneyArgs[index] = "money";
    index++;

    if(payShort) {
      moneyArgs[index] = "pay";
      index++;
    }

    if(balShort) {
      moneyArgs[index] = "bal";
      index++;
      moneyArgs[index] = "balance";
      index++;
    }

    if(topShort) {
      moneyArgs[index] = "baltop";
    }

    //Commands
    registerCommand(new String[] { "tne" }, new AdminCommand(this));
    registerCommand(new String[] { "tneconfig", "tnec" }, new ConfigCommand(this));
    registerCommand(new String[] { "currency", "cur" }, new CurrencyCommand(this));
    registerCommand(new String[] { "tnemodule", "tnem" }, new ModuleCommand(this));
    registerCommand(moneyArgs, new MoneyCommand(this));
    registerCommand(new String[] { "transaction", "trans" }, new TransactionCommand(this));
    registerCommand(new String[] { "yediot" }, new YetiCommand(this));
    loader.getModules().forEach((key, value)->{
      value.getModule().getCommands().forEach((command)->{
        List<String> accessors = new ArrayList<>();
        for(String string : command.getAliases()) {
          accessors.add(string);
        }
        accessors.add(command.getName());
        TNE.debug("Command Manager Null?: " + (commandManager == null));
        TNE.debug("Accessors?: " + accessors.size());
        TNE.debug("Command Null?: " + (command == null));
        registerCommand(accessors.toArray(new String[accessors.size()]), command);
      });
    });

    //Initialize our plugin's managers.
    manager = new EconomyManager();
    menuManager = new MenuManager();

    //General Variables based on configuration values
    consoleName = configurations().getString("Core.Server.Account.Name");
    debugMode = configurations().getBoolean("Core.Debug");
    useUUID = configurations().getBoolean("Core.UUID");

    TNESaveManager sManager = new TNESaveManager(new TNEDataManager(
        configurations().getString("Core.Database.Type").toLowerCase(),
        configurations().getString("Core.Database.MySQL.Host"),
        configurations().getInt("Core.Database.MySQL.Port"),
        configurations().getString("Core.Database.MySQL.Database"),
        configurations().getString("Core.Database.MySQL.User"),
        configurations().getString("Core.Database.MySQL.Password"),
        configurations().getString("Core.Database.Prefix"),
        new File(getDataFolder(), configurations().getString("Core.Database.File")).getAbsolutePath(),
        configurations().getBoolean("Core.Database.Transactions.Use"),
        configurations().getBoolean("Core.Database.Transactions.Cache"),
        configurations().getInt("Core.Database.Transactions.Update"),
        true
    ));
    setSaveManager(sManager);

    saveManager().getTNEManager().loadProviders();
    TNE.debug("Finished loading providers");

    TNE.debug("Setting format: " + configurations().getString("Core.Database.Type").toLowerCase());

    TNE.debug("Adding version files.");
    saveManager().addVersion(10.0, true);

    TNE.debug("Initializing Save Manager.");
    saveManager().initialize();

    TNE.debug("Calling Modules.enableSave");
    loader.getModules().forEach((key, value)->{
      value.getModule().enableSave(saveManager());
    });

    TNE.debug("Loading data.");
    saveManager().load();

    //Bukkit Runnables & Workers
    if(configurations().getBoolean("Core.AutoSaver.Enabled")) {
      saveWorker = new SaveWorker(this);
      saveWorker.runTaskTimer(this, configurations().getLong("Core.AutoSaver.Interval") * 20, configurations().getLong("Core.AutoSaver.Interval") * 20);
    }

    if(saveManager().getTNEManager().getTNEProvider().supportUpdate() && saveManager().getTNEManager().isCacheData()) {
      cacheWorker = new CacheWorker(this, cacheLists, cacheMaps);
      cacheWorker.runTaskTimer(this, saveManager().getTNEManager().getUpdate() * 20,
                               saveManager().getTNEManager().getUpdate() * 20);
    }
    getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    loader.getModules().forEach((key, value)->{
      value.getModule().getListeners(this).forEach(listener->{
        getServer().getPluginManager().registerEvents(listener, this);
        TNE.debug("Registering Listener");
      });
    });


    //Metrics
    if(configurations().getBoolean("Core.Metrics")) {
      new Metrics(this);
      getLogger().info("Sending plugin statistics.");
    }

    getLogger().info("The New Economy has been enabled!");
  }

  public void onDisable() {
    saveManager().save();
    loader.getModules().forEach((key, value)->{
      value.getModule().disableSave(saveManager());
    });
    loader.getModules().forEach((key, value)->{
      TNEModuleUnloadEvent event = new TNEModuleUnloadEvent(key, value.getInfo().version());
      Bukkit.getServer().getPluginManager().callEvent(event);
      value.getModule().unload(this);
    });
    super.onDisable();
    getLogger().info("The New Economy has been disabled!");
  }

  public static TNE instance() {
    return (TNE)instance;
  }

  @Override
  public TNEAPI api() {
    return (TNEAPI)api;
  }

  public Economy_TheNewEconomy vault() {
    return vaultEconomy;
  }

  public ReserveEconomy reserve() {
    return reserveEconomy;
  }

  public static ModuleLoader loader() { return instance().loader; }

  public static EconomyManager manager() {
    return instance().manager;
  }

  public static MenuManager menuManager() {
    return instance().menuManager;
  }

  public static TransactionManager transactionManager() {
    return instance().manager.transactionManager();
  }

  public static TNESaveManager saveManager() {
    return (TNESaveManager)instance().getSaveManager();
  }

  public static Logger logger() {
    return instance().getServer().getLogger();
  }

  public static TNEUUIDManager uuidManager() {
    return (TNEUUIDManager)instance().getUuidManager();
  }



  public void setUUIDS(Map<String, UUID> ids) {
    uuidCache.putAll(ids);
  }

  public MainConfigurations main() {
    return main;
  }

  public MessageConfigurations messages() {
    return messages;
  }

  public FileConfiguration messageConfiguration() {
    return messageConfigurations;
  }

  public FileConfiguration itemConfiguration() {
    return itemConfigurations;
  }

  public FileConfiguration playerConfiguration() {
    return playerConfigurations;
  }

  public FileConfiguration worldConfiguration() {
    return worldConfigurations;
  }

  private void initializeConfigurations() {
    items = new File(getDataFolder(), "items.yml");
    messagesFile = new File(getDataFolder(), "messages.yml");
    players = new File(getDataFolder(), "players.yml");
    worlds = new File(getDataFolder(), "worlds.yml");
    itemConfigurations = YamlConfiguration.loadConfiguration(items);
    messageConfigurations = YamlConfiguration.loadConfiguration(messagesFile);
    playerConfigurations = YamlConfiguration.loadConfiguration(players);
    worldConfigurations = YamlConfiguration.loadConfiguration(worlds);
    loader.getModules().forEach((key, value)->{
      value.getModule().initializeConfigurations();
    });
    try {
      setConfigurationDefaults();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  public static void debug(String message) {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(new File(TNE.instance().getDataFolder(), "debug.log"), true));
      writer.write(message + System.getProperty("line.separator"));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadConfigurations() {
    loader.getModules().forEach((key, value)->{
      value.getModule().loadConfigurations();
    });
    this.saveDefaultConfig();
    if(!new File(getDataFolder(), "config.yml").exists()) {
      getConfig().options().copyDefaults(true);
    }
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
      if(!check || !messagesFile.exists() || configurations().changed.contains(messageConfigurations.getName())) {
        messageConfigurations.save(messagesFile);
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
    Reader messagesStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
    Reader playersStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
    Reader worldsStream = new InputStreamReader(this.getResource("worlds.yml"), "UTF8");
    if (itemsStream != null && !items.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(itemsStream);
      itemConfigurations.setDefaults(config);
    }

    if (messagesStream != null && !messagesFile.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(messagesStream);
      messageConfigurations.setDefaults(config);
    }

    if (playersStream != null && !players.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(playersStream);
      playerConfigurations.setDefaults(config);
    }

    if (worldsStream != null && !worlds.exists()) {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(worldsStream);
      worldConfigurations.setDefaults(config);
    }
  }

  private void setupVault() {
    getServer().getServicesManager().register(Economy.class, vaultEconomy, this, ServicePriority.Highest);
    debug("Hooked into Vault");
  }

  private void setupReserve() {
    Reserve.instance().registerProvider(reserveEconomy);
    debug("Hooked into Reserve");
  }

  public void addWorldManager(WorldManager manager) {
    worldManagers.put(manager.getWorld(), manager);
  }

  public WorldManager getWorldManager(String world) {
    for(WorldManager manager : this.worldManagers.values()) {
      if(manager.getWorld().equalsIgnoreCase(world)) {
        debug("Return World Manager for world " + world);
        return manager;
      }
    }
    return null;
  }

  public Collection<WorldManager> getWorldManagers() {
    return worldManagers.values();
  }
}