package net.tnemc.core;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.SaveManager;
import net.tnemc.core.common.TNESQLManager;
import org.bukkit.Bukkit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

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

  public TNESQLManager sqlManager;
  public SaveManager saveManager;
  public String defaultWorld = "Default";
  public SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss.S");
  public static final Pattern uuidCreator = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
  public static boolean debugMode = false;

  public void onEnable() {
    instance = this;
    defaultWorld = Bukkit.getServer().getWorlds().get(0).getName();

    //SQL-related variables
    cache = true;
    saveFormat = "mysql";
    update = configurations.getInt("Core.SQL.Transactions.Update");

    sqlManager = new TNESQLManager(configurations.getString("Core.SQL.Host"), configurations.getInt("Core.SQL.Port"),
        configurations.getString("Core.SQL.Database"), configurations.getString("Core.SQL.User"),
        configurations.getString("Core.SQL.Password"), configurations.getString("Core.SQL.Prefix"),
        "", "", ""
    );

    super.onEnable();
    saveManager = new SaveManager(sqlManager);
    saveManager.initialize();

    getLogger().info("The New Economy has been enabled!");
  }

  public void onDisable() {
    getLogger().info("The New Economy has been disabled!");
  }

  public static TNE instance() {
    return (TNE)instance;
  }

  public void loadConfigurations() {
    getConfig().options().copyDefaults(true);
    super.loadConfigurations();
    saveConfigurations(false);
  }

  private void saveConfigurations(boolean check) {
    if (!check || !new File(getDataFolder(), "config.yml").exists() || configurations.changed.contains("config.yml")) {
      saveConfig();
    }
  }
}