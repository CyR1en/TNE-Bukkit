package net.tnemc.core.common.module;

import net.tnemc.core.TNE;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

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
 * This class loads all modules from the modules directory, and loads the main class based on @Module.
 **/
public class ModuleLoader {

  /**
   * Loads all modules into a map for later usage.
   * @return The map containing every module in format Name, ModuleInstance
   */
  public Map<String, Module> load() {
    Map<String, Module> modules = new HashMap<>();

    File[] jars = new File(TNE.instance().getDataFolder(), "/modules/").listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".jar");
      }
    });

    for(File jar : jars) {
      
    }

    return modules;
  }
}