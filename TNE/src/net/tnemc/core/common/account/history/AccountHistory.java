package net.tnemc.core.common.account.history;

import net.tnemc.core.common.transaction.Transaction;

import java.util.HashMap;
import java.util.Map;
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
 * Created by creatorfromhell on 07/05/2017.
 */
public class AccountHistory {


  /**
   * Dictionary is a {@link Map} collection that contains {@link String World Name} as
   * the key and {@link WorldHistory World Transaction History} as the value.
   */
  private Map<String, WorldHistory> worldHistory = new HashMap<>();

  /**
   * The {@link UUID} of the player this history is about.
   */
  private UUID id;

  public void log(Transaction transaction) {
    WorldHistory history = (worldHistory.containsKey(transaction.getWorld()))? worldHistory.get(transaction.getWorld())
                                                                             : new WorldHistory(transaction.getWorld());
    history.addTransaction(transaction);
    worldHistory.put(history.getWorld(), history);
  }
}