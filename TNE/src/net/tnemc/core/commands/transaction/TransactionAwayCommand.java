package net.tnemc.core.commands.transaction;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.collection.paginate.Page;
import com.github.tnerevival.core.collection.paginate.Paginator;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
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
 * Created by Daniel on 7/12/2017.
 */
public class TransactionAwayCommand extends TNECommand {

  public TransactionAwayCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "away";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.transaction.away";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Transaction.Away";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = WorldFinder.getWorld(sender);
    Player player = getPlayer(sender);
    int page = 1;

    if(arguments.length >= 1) {
      if(MISCUtils.isInteger(arguments[0])) {
        page = Integer.parseInt(arguments[0]);
      }
    }

    List<UUID> away = TNE.manager().getAccount(IDFinder.getID(sender)).getHistory().getAway();

    if(away.size() > 0) {
      Paginator paginator = new Paginator(new ArrayList<>(away), 5);

      if (page > paginator.getMaxPages()) page = paginator.getMaxPages();
      Page p = paginator.getPage(page);

      Message transactions = new Message("Messages.Transaction.Away");
      transactions.addVariable("$page", page + "");
      transactions.addVariable("$page_top", paginator.getMaxPages() + "");
      transactions.translate(WorldFinder.getWorld(sender), sender);

      for(Object obj : p.getElements()) {
        if(obj != null && obj instanceof UUID) {
          TNETransaction transaction = TNE.transactionManager().get((UUID)obj);

          Message entry = new Message("Messages.Transaction.AwayEntry");
          entry.addVariable("$id", transaction.transactionID().toString());
          entry.addVariable("$type", transaction.type().name());
          entry.translate(WorldFinder.getWorld(sender), sender);
        }
      }
      return true;
    }
    new Message("Messages.Transaction.AwayNone").translate(world, player);
    return false;
  }
}