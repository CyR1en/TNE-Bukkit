package net.tnemc.core.common.transaction.type;

import net.tnemc.core.common.transaction.TransactionCost;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.TransactionType;

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
public class TransactionConversion extends TransactionType {
  private String worldTo;
  private String currencyTo;

  public TransactionConversion(String worldTo, String currencyTo) {
    this.worldTo = worldTo;
    this.currencyTo = currencyTo;
  }

  @Override
  public String getName() {
    return "Conversion";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public TransactionResult handle(String initiator, String recipient, String world, TransactionCost cost) {
    return null;
  }

  @Override
  public boolean voidTransaction() {
    return false;
  }

  @Override
  public void handleInitiator() {
  }

  @Override
  public void handleRecipient() {
  }
}