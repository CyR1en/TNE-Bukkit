package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.event.TNETransactionEvent.TNEPreTransaction;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

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
   * Dictionary is a {@link Map} collection that contains {@link String Transaction Type Identifier} as
   * the key and {@link TransactionType Transaction Type} as the value.
   */
  private Map<String, TransactionType> types = new HashMap<>();

  /**
   * Dictionary is a {@link Map} collection that contains {@link String Transaction Result Identifier} as
   * the key and {@link TransactionResult Transaction Result} as the value.
   */
  private Map<String, TransactionResult> results = new HashMap<>();


  public TransactionManager() {
    initialize();
  }

  private void initialize() {
    //TODO: Initialize core Transaction Types & Results
  }

  public TransactionResult perform(Transaction transaction) {
    TNEPreTransaction event = new TNEPreTransaction(transaction);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      //TODO: Return generic failed result.
    }

    TransactionResult result = transaction.handle();
    if(result.proceed() || TNE.configurations().getBoolean("Core.Transactions.TrackFailed")) {

    }
    return transaction.handle();
  }

  void log(Transaction transaction) {
    if(transaction.getInitiator() != null) {
      Account.getAccount(transaction.getInitiator());
    }

    if(transaction.getRecipient() != null) {
      Account.getAccount(transaction.getRecipient());
    }
  }
}