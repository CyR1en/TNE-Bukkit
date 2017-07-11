package net.tnemc.core.common;

import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.transaction.Transaction;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.result.TransactionFailed;
import net.tnemc.core.event.transaction.TNEPreTransaction;
import org.bukkit.Bukkit;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public class TransactionManager {

  /**
   * Dictionary is a {@link Map} collection that contains {@link UUID Transaction Identifier} as
   * the key and {@link Transaction Transaction} as the value.
   */
  private Map<UUID, Transaction> transactions = new HashMap<>();

  public TransactionResult perform(Transaction transaction) {
    TNEPreTransaction event = new TNEPreTransaction(transaction);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      return new TransactionFailed();
    }
    return transaction.handle();
  }

  void log(Transaction transaction) {
    transactions.put(transaction.getUuid(), transaction);
    if(transaction.getInitiator() != null) {
      Account.getAccount(transaction.getInitiator()).log(transaction);
    }

    if(transaction.getRecipient() != null) {
      Account.getAccount(transaction.getRecipient()).log(transaction);
    }
  }

  public UUID generateTransactionID() {
    UUID id = UUID.randomUUID();

    while(transactions.containsKey(id)) {
      id = UUID.randomUUID();
    }
    return id;
  }
}