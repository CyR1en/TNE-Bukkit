package net.tnemc.mobs;

import com.github.tnerevival.commands.CommandManager;
import com.github.tnerevival.core.configurations.ConfigurationManager;
import net.tnemc.core.TNE;
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
    name = "Mobs",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class MobsModule extends Module {

  @Override
  public void load(TNE tne) {
    tne.logger().info("Mobs Module loaded!");
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Mobs Module unloaded!");
  }

  @Override
  public void registerConfigurations(ConfigurationManager manager) {
    super.registerConfigurations(manager);
  }

  @Override
  public void registerCommands(CommandManager manager) {
    super.registerCommands(manager);
  }
}