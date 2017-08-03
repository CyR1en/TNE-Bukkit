package net.tnemc.core.menu.screens.display;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.menu.icon.impl.MessageIcon;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.Currency;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.transaction.Transaction;
import net.tnemc.core.common.transaction.TransactionCost;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.type.TransactionInquiry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
 * Created by Daniel on 7/30/2017.
 */
public class CurrencyIcon extends MessageIcon {

  private String world;
  private String currency;
  private String player;

  public CurrencyIcon(Integer slot, ItemStack stack, String screen, String message, String permission, String world, String currency, String player) {
    super(slot, stack, screen, message, permission);
    this.world = world;
    this.currency = currency;
    this.player = player;
  }

  @Override
  public boolean onClick(Player p) {
    UUID id = IDFinder.getID(player);
    UUID initiatorID = IDFinder.getID(p);
    Currency cur = TNE.manager().currencyManager().get(world, currency);

    Transaction transaction = new Transaction(initiatorID, id, world, new TransactionInquiry(new TransactionCost(new BigDecimal(0.0), cur)));
    TransactionResult result = transaction.handle();
    Message m = new Message(result.initiatorMessage());
    m.addVariable("$player", IDFinder.getUsername(id.toString()));
    m.addVariable("$world", world);
    m.addVariable("$amount", CurrencyFormatter.format(transaction.getCost().getCurrency(), world, transaction.getType().recipientBalance()));
    this.message = m.grab(world, p);

    p.closeInventory();
    super.onClick(p);
    return true;
  }
}