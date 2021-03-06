package net.tnemc.core.commands.money;

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
public class MoneyCommand extends TNECommand {

  public MoneyCommand(TNE plugin) {
    super(plugin);
    subCommands.add(new MoneyBalanceCommand(plugin));
    subCommands.add(new MoneyConvertCommand(plugin));
    subCommands.add(new MoneyGiveCommand(plugin));
    subCommands.add(new MoneyNoteCommand(plugin));
    subCommands.add(new MoneyPayCommand(plugin));
    subCommands.add(new MoneySetCommand(plugin));
    subCommands.add(new MoneyTakeCommand(plugin));
    subCommands.add(new MoneyTopCommand(plugin));
  }

  @Override
  public String getName() {
    return "money";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "bal", "balance", "pay", "baltop"
    };
  }

  @Override
  public String getNode() {
    return "tne.money";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(command.equalsIgnoreCase("balance") ||
        command.equalsIgnoreCase("bal") ||
        arguments.length == 0) {
      arguments = new String[1];
      arguments[0] = "balance";
    }

    if(command.equalsIgnoreCase("baltop")) {
      String[] args = new String[arguments.length + 1];
      args[0] = "top";
      System.arraycopy(arguments, 0, args, 1, arguments.length);
      arguments = args;
    }

    if(command.equalsIgnoreCase("pay")) {
      String[] args = new String[arguments.length + 1];
      args[0] = "pay";
      System.arraycopy(arguments, 0, args, 1, arguments.length);
      arguments = args;
    }
    return super.execute(sender, command, arguments);
  }
}