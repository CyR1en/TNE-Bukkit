package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.Currency;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public class TransactionCost {

  private List<ItemStack> items;
  private BigDecimal amount;
  private Currency currency;

  public TransactionCost(BigDecimal amount) {
    this(amount, TNE.instance().manager.currencyManager.get(TNE.instance().defaultWorld));
  }

  public TransactionCost(BigDecimal amount, Currency currency) {
    this(amount, currency, new ArrayList<ItemStack>());
  }

  public TransactionCost(BigDecimal amount, Currency currency, List<ItemStack> items) {
    this.amount = amount;
    this.currency = currency;
    this.items = items;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public List<ItemStack> getItems() {
    return items;
  }

  public void setItems(List<ItemStack> items) {
    this.items = items;
  }
}