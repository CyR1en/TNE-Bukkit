package net.tnemc.conversion;

import com.github.tnerevival.commands.CommandManager;
import net.tnemc.core.TNE;
import net.tnemc.core.common.configurations.MainConfigurations;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;

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
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Conversion",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class ConversionModule extends Module {

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Conversion Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Conversion Module unloaded!");
  }

  @Override
  public void registerMainConfigurations(MainConfigurations configuration) {
    configuration.configurations.put("Core.Conversion.Convert", false);
    configuration.configurations.put("Core.Conversion.Name", "iConomy");
    configuration.configurations.put("Core.Conversion.Format", "MySQL");
    configuration.configurations.put("Core.Conversion.Options.Host", "localhost");
    configuration.configurations.put("Core.Conversion.Options.Port", 3306);
    configuration.configurations.put("Core.Conversion.Options.Database", "sql_eco");
    configuration.configurations.put("Core.Conversion.Options.User", "root");
    configuration.configurations.put("Core.Conversion.Options.Password", "Password");
  }

  @Override
  public void registerCommands(CommandManager manager) {
    super.registerCommands(manager);
  }
}