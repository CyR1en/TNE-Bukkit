package net.tnemc.core.common.transaction;

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
public interface TransactionResult {

  /**
   * @return The configuration node that corresponds to the message the initiator sees, or "" for no message.
   */
  String initiatorMessage();

  /**
   * @return The configuration node that corresponds to the message the recipient sees, or "" for no message.
   */
  String recipientMessage();

  /**
   * @return True if the transaction may proceed, otherwise false.
   */
  boolean proceed();
}