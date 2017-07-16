package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.Currency;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.transaction.Transaction;
import net.tnemc.core.common.transaction.TransactionCost;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.type.TransactionSet;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
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
public class MoneySetCommand extends TNECommand {

  public MoneySetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.set";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Money.Set";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 2) {
      String world = (arguments.length == 3) ? arguments[2] : WorldFinder.getWorld(sender);
      String currencyName = (arguments.length >= 4) ? arguments[3] : TNE.instance().manager().currencyManager().get(world).getSingle();
      UUID id = IDFinder.getID(arguments[0]);

      Currency currency = TNE.instance().manager().currencyManager().get(world, currencyName);
      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
      if (parsed.contains("Messages")) {
        Message max = new Message(parsed);
        max.addVariable("$currency", currency.getSingle());
        max.addVariable("$world", world);
        max.addVariable("$player", getPlayer(sender).getDisplayName());
        max.translate(WorldFinder.getWorld(sender), sender);
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);

      Transaction transaction = new Transaction(IDFinder.getID(sender), id, world, new TransactionCost(value, currency), new TransactionSet());
      TransactionResult result = transaction.handle();

      if(result.proceed() && transaction.getRecipient() != null && Bukkit.getPlayer(id) != null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(id))) {
        Message message = new Message(result.recipientMessage());
        message.addVariable("$player", arguments[0]);
        message.addVariable("$world", world);
        message.addVariable("$currency", currencyName);
        message.addVariable("$amount", CurrencyFormatter.format(transaction.getCost().getCurrency(), world, transaction.getType().recipientBalance()));
        message.translate(world, id);
      }

      Message message = new Message(result.initiatorMessage());
      message.addVariable("$player", arguments[0]);
      message.addVariable("$world", world);
      message.addVariable("$currency", currencyName);
      message.addVariable("$amount", CurrencyFormatter.format(transaction.getCost().getCurrency(), world, transaction.getType().recipientBalance()));
      message.translate(world, IDFinder.getID(sender));
      return result.proceed();
    }
    help(sender);
    return false;
  }
}