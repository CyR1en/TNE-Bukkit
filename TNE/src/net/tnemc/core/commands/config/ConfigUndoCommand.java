package net.tnemc.core.commands.config;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import org.bukkit.command.CommandSender;

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
 * Created by Daniel on 7/10/2017.
 */
public class ConfigUndoCommand extends TNECommand {

  public ConfigUndoCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "undo";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "u"
    };
  }

  @Override
  public String getNode() {
    return "tne.config.undo";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Config.Undo";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    return true;
  }
}