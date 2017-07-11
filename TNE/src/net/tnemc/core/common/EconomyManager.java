package net.tnemc.core.common;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.event.account.TNEAccountCreationEvent;
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
public class EconomyManager {


  /**
   * Used to hold all accounts.
   * Dictionary is a {@link Map} collection that contains {@link UUID Player UUID} as
   * the key and {@link Account account} as the value.
   */
  private Map<UUID, Account> accounts = new HashMap<>();

  /**
   * The {@link CurrencyManager currency manager}, which manages all loaded currencies, and their respective tiers.
   */
  private CurrencyManager currencyManager;

  /**
   * The {@link TransactionManager transaction manager}, which manages all transactions performed on the server.
   */
  private TransactionManager transactionManager;

  public EconomyManager() {
    currencyManager = new CurrencyManager();
    transactionManager = new TransactionManager();
  }

  public CurrencyManager currencyManager() {
    return currencyManager;
  }

  public TransactionManager transactionManager() {
    return transactionManager;
  }

  public boolean exists(UUID id) {
    return accounts.containsKey(id);
  }

  public void addAccount(Account account) {
    accounts.put(account.getId(), account);
  }

  public Account getAccount(UUID id) {
    if(!exists(id)) {
      if(!createAccount(id)) {
        return null;
      }
    }
    return accounts.get(id);
  }

  public boolean createAccount(UUID id) {
    Account account = new Account(id);
    account.setSpecial(TNE.instance().special.contains(id));

    TNEAccountCreationEvent event = new TNEAccountCreationEvent(id, account);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.isCancelled()) {
      return false;
    }
    accounts.put(account.getId(), account);
    return true;
  }
}