package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.currency.TNECurrency;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.result.TransactionResult;
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
public class MoneyBalanceCommand extends TNECommand {

  public MoneyBalanceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "balance";
  }

  @Override
  public String[] getAliases() {
    return new String[] {
        "bal"
    };
  }

  @Override
  public String getNode() {
    return "tne.money.balance";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Balance";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = (arguments.length >= 1)? arguments[0] : WorldFinder.getWorld(sender);
    TNE.debug("MoneyBalanceCommand.execute, World: " + world);
    if(TNE.manager() == null) TNE.debug("Economy Manager is null");
    if(TNE.manager().currencyManager() == null) TNE.debug("TNECurrency Manager is null");
    if(TNE.manager().currencyManager().get(world) == null) TNE.debug("World TNECurrency is null");
    String currencyName = (arguments.length >= 2)? arguments[1] : TNE.manager().currencyManager().get(world).name();
    TNECurrency currency = TNE.manager().currencyManager().get(world, currencyName);
    UUID id = IDFinder.getID(sender);


    TNETransaction transaction = new TNETransaction(id, id, world, TNE.transactionManager().getType("inquiry"));
    transaction.setRecipientCharge(new TransactionCharge(world, currency, new BigDecimal(0.0)));
    TransactionResult result = TNE.transactionManager().perform(transaction);

    TNE.debug("Result: " + result.name());
    TNE.debug("Transaction Success: " + result.proceed());
    TNE.debug("Result Message: " + result.recipientMessage());
    Message message = new Message(result.recipientMessage());
    message.addVariable("$player", IDFinder.ecoToUsername(IDFinder.getID(transaction.recipient())));
    message.addVariable("$world", world);
    message.addVariable("$currency", currencyName);
    message.addVariable("$amount", CurrencyFormatter.format(transaction.recipientBalance().getCurrency(), world, transaction.recipientBalance().getAmount()));
    message.translate(world, sender);
    return result.proceed();
  }
}