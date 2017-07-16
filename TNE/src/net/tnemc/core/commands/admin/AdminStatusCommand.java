package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.account.AccountStatus;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

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
public class AdminStatusCommand extends TNECommand {

  public AdminStatusCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "status";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.status";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Admin.Status";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      if(TNE.instance().manager().exists(IDFinder.getID(arguments[0]))) {
        UUID target = IDFinder.getID(arguments[0]);
        Account acc = TNE.instance().manager().getAccount(target);

        AccountStatus status = (arguments.length == 2)? AccountStatus.fromName(arguments[1]) : acc.getStatus();
        boolean changed = (status.getName().equals(acc.getStatus().getName()));

        if(changed) {
          acc.setStatus(status);
          TNE.instance().manager().addAccount(acc);
        }
        String message = (changed)? "Messages.Admin.StatusChange" : "Messages.Admin.Status";

        if(changed && Bukkit.getOnlinePlayers().contains(target)) {
          String world = WorldFinder.getWorld(target);
          Message m = new Message("Messages.Account.StatusChange");
          m.addVariable("$status", status.getName());
          m.translate(world, target);
        }

        Message m = new Message(message);
        m.addVariable("$player", arguments[0]);
        m.addVariable("$status", arguments[1]);
        m.translate("", sender);
        return true;
      }


      Message m = new Message("Messages.General.Player");
      m.addVariable("$player", arguments[0]);
      m.translate("", sender);
      return false;
    }
    help(sender);
    return false;
  }
}