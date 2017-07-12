package net.tnemc.core.common.account;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.history.AccountHistory;
import net.tnemc.core.common.transaction.Transaction;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;
import java.util.*;

/*
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
 * Created by creatorfromhell on 07/01/2017.
 */
public class Account {
  private Map<String, WorldHoldings> holdings = new HashMap<>();
  private Map<Location, TrackedItems> trackedItems = new HashMap<>();
  private List<String> confirmed = new ArrayList<>();

  private AccountHistory history;

  private int accountNumber = 0;
  private UUID id;
  private AccountStatus status;
  private String pin;
  private boolean special;
  private String joined;
  private long lastOnline;

  public Account(UUID id) {
    this.id = id;
  }

  public void log(Transaction transaction) {
    history.log(transaction);
  }

  public void setHoldings(String world, String currency, BigDecimal newHoldings) {
    setHoldings(world, currency, newHoldings, false);
  }

  public void setHoldings(String world, String currency, BigDecimal newHoldings, boolean skipInventory) {
    WorldHoldings worldHoldings = holdings.containsKey(world)? holdings.get(world) : new WorldHoldings(world);
    worldHoldings.setHoldings(currency, newHoldings);

    if(!skipInventory) {
      net.tnemc.core.common.currency.Currency cur = TNE.instance().manager().currencyManager().get(world, currency).get();
      if(cur.isItem()) {
        setCurrencyItems(cur, newHoldings);
      }
    }

    holdings.put(world, worldHoldings);
  }

  public BigDecimal getHoldings(String world, String currency) {
    WorldHoldings worldHoldings = holdings.containsKey(world)? holdings.get(world) : new WorldHoldings(world);
    return worldHoldings.getHoldings(currency);
  }

  public static Account getAccount(String identifier) {
    return TNE.instance().manager().getAccount(IDFinder.getID(identifier));
  }

  public boolean confirmed(String world) {
    //TODO: Pins
    return false;
  }

  public void setCurrencyItems(net.tnemc.core.common.currency.Currency currency, BigDecimal amount) {
    //TODO: Item Currencies
  }

  public BigDecimal getCurrencyItems(net.tnemc.core.common.currency.Currency currency, Inventory inventory) {
    //TODO: Item Currencies
    return new BigDecimal(0.0);
  }

  public void recalculateItemHoldings(String world, Inventory inventory) {
    //TODO: Item Currencies.
  }

  public void recalculateCurrencyHoldings(String world, Inventory inventory, String currency) {
    //TODO: Item Currencies.
  }

  public AccountHistory getHistory() {
    return history;
  }

  public void setHistory(AccountHistory history) {
    this.history = history;
  }

  public int getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(int accountNumber) {
    this.accountNumber = accountNumber;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public boolean isSpecial() {
    return special;
  }

  public void setSpecial(boolean special) {
    this.special = special;
  }

  public String getJoined() {
    return joined;
  }

  public void setJoined(String joined) {
    this.joined = joined;
  }

  public long getLastOnline() {
    return lastOnline;
  }

  public void setLastOnline(long lastOnline) {
    this.lastOnline = lastOnline;
  }
}