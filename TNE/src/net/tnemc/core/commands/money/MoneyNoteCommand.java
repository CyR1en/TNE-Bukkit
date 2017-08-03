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
import net.tnemc.core.common.transaction.type.TransactionTake;
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
 * Created by Daniel on 7/31/2017.
 */
public class MoneyNoteCommand extends TNECommand {

  public MoneyNoteCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "note";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.note";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Note";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length >= 1) {
      String world = WorldFinder.getWorld(sender);
      String currencyName = (arguments.length >= 2) ? arguments[1] : TNE.manager().currencyManager().get(world).getSingle();
      Currency currency = TNE.manager().currencyManager().get(world, currencyName);
      UUID id = IDFinder.getID(arguments[0]);

      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[0]);
      if(parsed.contains("Messages")) {
        Message max = new Message(parsed);
        max.addVariable("$currency", currency.getSingle());
        max.addVariable("$world", world);
        max.addVariable("$player", getPlayer(sender).getDisplayName());
        max.translate(WorldFinder.getWorld(sender), sender);
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);

      Transaction transaction = new Transaction(IDFinder.getID(sender), id, world, new TransactionTake(new TransactionCost(value, currency)));
      TransactionResult result = TNE.transactionManager().perform(transaction);


      if(result.proceed()) {
        Message message = new Message(result.recipientMessage());
        message.addVariable("$player", arguments[0]);
        message.addVariable("$world", world);
        message.addVariable("$currency", currencyName);
        message.addVariable("$amount", CurrencyFormatter.format(transaction.getCost().getCurrency(), world, transaction.getType().recipientBalance()));
        message.translate(world, id);
      }
    }
    help(sender);
    return false;
  }
}