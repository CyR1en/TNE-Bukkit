package net.tnemc.core.common.module;

import com.github.tnerevival.commands.CommandManager;
import com.github.tnerevival.core.SaveManager;
import com.github.tnerevival.core.configurations.ConfigurationManager;
import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.MainConfigurations;

import java.util.ArrayList;
import java.util.List;

/*
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
 * Created by creatorfromhell on 07/01/2017.
 */

/**
 * The base class for all TNE Modules.
 */
public abstract class Module {

  public Module() {
  }

  /**
   * @return a list of the classes that contain {@link net.tnemc.core.common.module.injectors.ModuleInjector module injectors} for this module.
   */
  public List<Class> moduleInjectors() {
    return new ArrayList<>();
  }

  /**
   * Called when this @Module is loaded.
   * @param tne An instance of the main TNE class.
   * @param version The last version of this module used on this server.
   */
  public void load(TNE tne, String version) {

  }

  /**
   * Called when this @Module is unloaded.
   * @param tne An instance of the main TNE class.
   */
  public void unload(TNE tne) {

  }

  /**
   * Called when data is being backed up to the "backup/" directory.
   * @param saveManager An instance of TNE's Save Manager.
   */
  public void backup(SaveManager saveManager) {

  }

  /**
   * Used to perform any data loading, manipulation, layout updating, etc.
   * @param saveManager An instance of TNE's Save Manager
   */
  public void enableSave(SaveManager saveManager) {

  }

  /**
   * Used to save any remaining data to the correct database.
   * @param saveManager An instance of TNE's Save Manager
   */
  public void disableSave(SaveManager saveManager) {

  }

  /**
   * Used to initialize any configuration files this module may use.
   */
  public void initializeConfigurations() {

  }

  /**
   * Used to load any configuration files this module may use.
   * This step is for initializing. the File, and YamlConfigurations classes.
   */
  public void loadConfigurations() {

  }

  /**
   * Used to save any configuration files this module may use.
   */
  public void saveConfigurations() {

  }

  /**
   * Register any configuration node defaults for config.yml that this module may use.
   * @param configuration An instance of TNE's MainConfigurations class.
   */
  public void registerMainConfigurations(MainConfigurations configuration) {

  }

  /**
   * Registers any configuration files this module may use.
   * @param manager An instance of TNE's configurations manager.
   */
  public void registerConfigurations(ConfigurationManager manager) {

  }

  /**
   * Registers any commands that this module may add.
   * @param manager An instance of TNE's command manager.
   */
  public void registerCommands(CommandManager manager) {

  }
}