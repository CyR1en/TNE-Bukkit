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
import net.tnemc.core.common.transaction.type.TransactionPay;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class MoneyPayCommand extends TNECommand {

  public MoneyPayCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "pay";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.pay";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "Messages.Money.Pay";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    String world = WorldFinder.getWorld(sender);
    if(arguments.length >= 2) {
      String currencyName = (arguments.length >= 3) ? arguments[2] : TNE.manager().currencyManager().get(world).getSingle();
      Currency currency = TNE.manager().currencyManager().get(world, currencyName);
      UUID id = IDFinder.getID(arguments[0]);

      String parsed = CurrencyFormatter.parseAmount(currency, world, arguments[1]);
      if(parsed.contains("Messages")) {
        Message max = new Message(parsed);
        max.addVariable("$currency", currency.getSingle());
        max.addVariable("$world", world);
        max.addVariable("$player", getPlayer(sender).getDisplayName());
        max.translate(WorldFinder.getWorld(sender), sender);
        return false;
      }

      BigDecimal value = new BigDecimal(parsed);

      Transaction transaction = new Transaction(IDFinder.getID(sender), id, world, new TransactionPay(new TransactionCost(value, currency)));
      TransactionResult result = TNE.transactionManager().perform(transaction);

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