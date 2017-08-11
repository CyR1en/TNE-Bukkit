package net.tnemc.core.common.transaction;

import net.tnemc.core.common.currency.Currency;

import java.math.BigDecimal;

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
 * Created by Daniel on 8/9/2017.
 */
public class TransactionCharge {
  TransactionChargeType type = TransactionChargeType.LOSE;
  String world;
  Currency currency;
  BigDecimal amount;

  public TransactionCharge(String world, Currency currency, BigDecimal amount) {
    this.world = world;
    this.currency = currency;
    this.amount = amount;
  }

  public TransactionChargeType getType() {
    return type;
  }

  public void setType(TransactionChargeType type) {
    this.type = type;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}