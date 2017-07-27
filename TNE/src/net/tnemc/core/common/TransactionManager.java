package net.tnemc.core.common;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.transaction.Transaction;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.TransactionType;
import net.tnemc.core.common.transaction.result.*;
import net.tnemc.core.common.transaction.type.*;
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
  private Map<String, TransactionResult> results = new HashMap<>();
  private Map<String, Class<? extends TransactionType>> types = new HashMap<>();

  public TransactionManager() {
    loadResults();
    //TODO: loadTypes();
  }

  public Transaction get(UUID id) {
    return transactions.get(id);
  }

  public void loadResults() {
    results.put("conversion", new TransactionResultConversion());
    results.put("failed", new TransactionResultFailed());
    results.put("gave", new TransactionResultGave());
    results.put("holdings", new TransactionResultHoldings());
    results.put("insufficient", new TransactionResultInsufficient());
    results.put("lost", new TransactionResultLost());
    results.put("paid", new TransactionResultPaid());
    results.put("selfpay", new TransactionResultSelfPay());
    results.put("set", new TransactionResultSet());

    TNE.instance().loader().getModules().forEach((key, value)->{
      value.getModule().registerResults().forEach((k, v)->{
        results.put(k, v);
      });
    });
  }

  public void loadTypes() {
    types.put("conversion", TransactionConversion.class);
    types.put("give", TransactionGive.class);
    types.put("inquiry", TransactionInquiry.class);
    types.put("pay", TransactionPay.class);
    types.put("set", TransactionSet.class);
    types.put("take", TransactionTake.class);

    TNE.instance().loader().getModules().forEach((key, value)->{
      value.getModule().registerTypes().forEach((k, v)->{
        types.put(k, v);
      });
    });
  }

  //TODO: Get type method

  public TransactionResult getResult(String name) {
    return results.get(name);
  }

  public void add(Transaction transaction) {
    if(!transactions.containsKey(transaction.getUuid())) {
      log(transaction);
      return;
    }
    transactions.put(transaction.getUuid(), transaction);
  }

  public TransactionResult perform(Transaction transaction) {
    TNEPreTransaction event = new TNEPreTransaction(transaction);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      return new TransactionResultFailed();
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

  public Map<UUID, Transaction> getTransactions() {
    return transactions;
  }

  public UUID generateTransactionID() {
    UUID id = UUID.randomUUID();

    while(transactions.containsKey(id)) {
      id = UUID.randomUUID();
    }
    return id;
  }

  public boolean isValid(UUID id) {
    return transactions.containsKey(id);
  }

  public boolean isVoided(UUID id) {
    return transactions.get(id).isVoided();
  }

  public boolean voidTransaction(UUID id) {
    return transactions.get(id).getType().voidTransaction();
  }
}