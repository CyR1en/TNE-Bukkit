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

  /**
   * @return The name of this transaction type.
   */
  public abstract String getName();

  /**
   * @return True if the recipient, or initiator may be null, which is identified as the console.
   */
  public abstract boolean console();

  /**
   * @return The result of this transaction.
   */
  public abstract TransactionResult getResult();

  /**
   * Handles the transaction.
   * @return True if everything is ready to proceed to the next step.
   */
  public abstract boolean handle();

  /**
   * Handles the initiator side of the transaction.
   * @return True if everything is ready to proceed on the initiator's side of the transaction.
   */
  public abstract boolean handleInitiator();

  /**
   * @return The initiator's balance if the transaction proceeds.
   */
  public abstract BigDecimal initiatorBalance();

  /**
   * Handles the recipient side of the transaction.
   * @return True if everything is ready to proceed on the recipient's side of the transaction.
   */
  public abstract boolean handleRecipient();

  /**
   * @return The recipient's balance if the transaction proceeds.
   */
  public abstract BigDecimal recipientBalance();
}