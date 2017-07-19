package net.tnemc.core.common.transaction;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public abstract class TransactionType {

  protected BigDecimal initiatorBalance;
  protected BigDecimal initiatorOldBalance;
  protected BigDecimal recipientBalance;
  protected BigDecimal recipientOldBalance;
  protected TransactionResult result = null;

  /**
   * @return The name of this transaction type.
   */
  public abstract String getName();

  /**
   * @return True if the recipient, or initiator may be null, which is identified as the console.
   */
  public abstract boolean console();

  /**
   * Handles the transaction.
   * @return The {@TransactionResult result} of this transaction.
   * @param initiator The transaction initiator's identifier.
   * @param recipient The transaction recipient's identifier.
   * @param world The world the transaction occurred in.
   * @param cost The money and/or items this transaction consists of.
   */
  public abstract TransactionResult handle(String initiator, String recipient, String world, TransactionCost cost);

  /**
   * Handles the voiding of a transaction.
   * @return True if this transaction may be voided.
   */
  public abstract boolean voidTransaction();

  /**
   * Handles the initiator side of the transaction.
   */
  public abstract void handleInitiator();

  /**
   * @return The initiator's balance if the transaction proceeds.
   */
  public BigDecimal initiatorBalance() {
    return initiatorBalance;
  }

  /**
   * @return The initiator's balance before the transaction occurred, if it did.
   */
  public BigDecimal initiatorOldBalance() {
    return initiatorOldBalance;
  }

  /**
   * Handles the recipient side of the transaction.
   */
  public abstract void handleRecipient();

  /**
   * @return The recipient's balance if the transaction proceeds.
   */
  public BigDecimal recipientBalance() {
    return recipientBalance;
  }

  /**
   * @return The recipient's balance before the transaction occurred, if it did.
   */
  public BigDecimal recipientOldBalance() {
    return recipientOldBalance;
  }
}