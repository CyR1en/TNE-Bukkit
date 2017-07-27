package net.tnemc.core.common.version;

import com.github.tnerevival.core.SQLManager;
import com.github.tnerevival.core.db.FlatFile;
import com.github.tnerevival.core.db.H2;
import com.github.tnerevival.core.db.MySQL;
import com.github.tnerevival.core.db.flat.FlatFileConnection;
import com.github.tnerevival.core.db.flat.Section;
import com.github.tnerevival.core.version.Version;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.account.history.AccountHistory;
import net.tnemc.core.common.transaction.Transaction;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.*;

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
public class Alpha56 extends Version {

  private String prefix;

  public Alpha56(SQLManager sqlManager) {
    super(sqlManager);
    this.prefix = sqlManager.getPrefix();
  }

  @Override
  public boolean firstRun() {
    return false;
  }

  @Override
  public double getSaveVersion() {
    return 5.6;
  }

  @Override
  public double versionNumber() {
    return 5.6;
  }

  @Override
  public void update(double version, String type) {

  }

  public Map<String, Transaction> loadTransactions() {
    Map<String, Transaction> transactions = new HashMap<>();

    String table = prefix + "_TRANSACTIONS";
    try {
      int transactionIndex = sql().executeQuery("SELECT * FROM `" + table + "`;");
      while (sql().results(transactionIndex).next()) {
        //TODO: Data Handling
      }
      sql().close();
    } catch(Exception e) {
      TNE.debug(e);
    }
    return transactions;
  }

  public AccountHistory loadHistory(UUID id) {
    String table = prefix + "_TRANSACTIONS";
    try {
      AccountHistory history = new AccountHistory();
      int transactionIndex = sql().executePreparedQuery("SELECT * FROM `" + table + "` WHERE trans_initiator = ?", new Object[] {
          id
      });

      while(sql().results(transactionIndex).next()) {
        //TODO: Data Handling
      }
      sql().close();
      return history;
    } catch(Exception e) {
      TNE.debug(e);
    }
    return null;
  }

  public void saveTransaction(Transaction transaction) {
    if(!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_TRANSACTIONS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (trans_id, trans_initiator, trans_player, trans_world, trans_type, trans_cost, trans_oldBalance, trans_balance, trans_time) " +
              "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE trans_player = ?, trans_world = ?",
          new Object[] {
              //TODO: Data Handling
          }
      );
      sql().close();
    }
  }

  public void deleteTransaction(UUID id) {
    if(!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_TRANSACTIONS WHERE trans_id = ? ", new Object[] { id.toString() });
      sql().close();
    }
  }  
  
  public Map<String, UUID> loadIDS() {
    Map<String, UUID> ids = new HashMap<>();

    String table = prefix + "_ECOIDS";
    try {
      int idIndex = sql().executeQuery("SELECT * FROM " + table + ";");
      while (sql().results(idIndex).next()) {
        ids.put(sql().results(idIndex).getString("username"), UUID.fromString(sql().results(idIndex).getString("uuid")));
      }
      sql().close();
    } catch(Exception e) {
      TNE.debug(e);
    }
    return ids;
  }

  public UUID loadID(String username) {
    String table = prefix + "_ECOIDS";
    try {
      int idIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE username = ?", new Object[] {
          username
      });
      if(sql().results(idIndex).next()) {
        UUID id = UUID.fromString(mysql().results(idIndex).getString("uuid"));
        sql().close();
        return id;
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return null;
  }

  public void saveID(String username, UUID id) {
    if(!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_ECOIDS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (username, uuid) VALUES (?, ?) ON DUPLICATE KEY UPDATE username = ?",
          new Object[] {
              username,
              id.toString(),
              username
          });
      sql().close();
    }
  }

  public void removeID(String username) {
    if(!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_ECOIDS WHERE username = ?", new Object[] { username });
      sql().close();
    }
  }

  public void removeID(UUID id) {
    if(!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_ECOIDS WHERE uuid = ?", new Object[] { id.toString() });
      sql().close();
    }
  }

  public Collection<Account> loadAccounts() {
    List<Account> accounts = new ArrayList<>();

    String table = prefix + "_USERS";
    try {
      int accountIndex = sql().executeQuery("SELECT uuid FROM " + table + ";");
      while (sql().results(accountIndex).next()) {
        accounts.add(loadAccount(UUID.fromString(sql().results(accountIndex).getString("uuid"))));
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return accounts;
  }

  public Account loadAccount(UUID id) {
    String table = prefix + "_USERS";
    try {
      int accountIndex = sql().executePreparedQuery("SELECT * FROM " + table + " WHERE uuid = ?", new Object[]{
          id.toString()
      });
      if (sql().results(accountIndex).next()) {
        ResultSet results = sql().results(accountIndex);
        Account account = new Account(UUID.fromString(results.getString("uuid")));
        //TODO: Data Handling.

        //Load balances
        String balancesTable = prefix + "_BALANCES";
        int balancesIndex = sql().executePreparedQuery("SELECT * FROM " + balancesTable + " WHERE uuid = ?", new Object[]{account.getId().toString()});
        while (sql().results(balancesIndex).next()) {
          results = sql().results(balancesIndex);
          account.setHoldings(results.getString("world"), results.getString("currency"), new BigDecimal(results.getString("balance")));
        }
        sql().close();
        return account;
      }
    } catch(Exception e) {
      TNE.debug(e);
    }
    return null;
  }

  public void saveAccount(Account acc) {
    if (!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      String table = prefix + "_USERS";
      sql().executePreparedUpdate("INSERT INTO `" + table + "` (uuid, acc_pin, inventory_credits, command_credits, joinedDate, accountnumber, accountstatus, account_special) VALUES(?, ?, ?, ?, ?, ?, ?, ?)" +
              " ON DUPLICATE KEY UPDATE acc_pin = ?, inventory_credits = ?, command_credits = ?, joinedDate = ?, accountnumber = ?, accountstatus = ?, account_special = ?",
          new Object[]{
              //TODO: Data Handling
          }
      );

      final String balTable = prefix + "_BALANCES";
      acc.getHoldings().forEach((world, worldHoldings)->{
        worldHoldings.getHoldings().forEach((currency, balance)->{
          sql().executePreparedUpdate("INSERT INTO `" + balTable + "` (uuid, server_name, world, currency, balance) " +
                  "VALUES(?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE balance = ?",
              new Object[]{
                  acc.getId().toString(),
                  TNE.instance().getServer().getServerName(),
                  world,
                  currency,
                  balance.toPlainString(),
                  balance.toPlainString()
              }
          );
        });
      });
    }
  }

  public void deleteAccount(UUID id) {
    if(!TNE.instance().saveFormat.equalsIgnoreCase("flatfile")) {
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_USERS WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_BALANCES WHERE uuid = ? ", new Object[] { id.toString() });
      sql().executePreparedUpdate("DELETE FROM " + prefix + "_TRACKED WHERE uuid = ? ", new Object[] { id.toString() });
      sql().close();
    }
  }

  @Override
  public void loadFlat(File file) {

  }

  @Override
  public void saveFlat(File file) {
    Section accounts = new Section("accounts");
    Section ids = new Section("ids");
    Section transactions = new Section("transactions");
    try {

      db = new FlatFile(TNE.instance().getDataFolder() + File.separator + TNE.instance().api().getString("Core.Database.FlatFile.File"), true);
      FlatFileConnection connection = (FlatFileConnection)db.connection();
      connection.getOOS().writeDouble(versionNumber());
      connection.getOOS().writeObject(accounts);
      connection.getOOS().writeObject(ids);
      connection.getOOS().writeObject(transactions);
      connection.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void loadMySQL() {
    loadH2();
  }

  @Override
  public void saveMySQL() {
    createTables("mysql");
    String table = TNE.sqlManager().getPrefix() + "_INFO";
    db = new MySQL(TNE.sqlManager().getMysqlHost(),
                   TNE.sqlManager().getMysqlPort(),
                   TNE.sqlManager().getMysqlDatabase(),
                   TNE.sqlManager().getMysqlUser(),
                   TNE.sqlManager().getMysqlPassword());
    mysql().executePreparedUpdate("Update " + table + " SET version = ?, server_name = ? WHERE id = 1;",
                                   new Object[] { String.valueOf(versionNumber()),
                                                  TNE.instance().getServer().getServerName()
                                                });

    TNE.manager().getAccounts().forEach((id, account)->saveAccount(account));
    TNE.instance().offlineIDS.forEach(this::saveID);
    TNE.transactionManager().getTransactions().forEach((id, transaction)->saveTransaction(transaction));
  }

  @Override
  public void loadSQLite() {
    loadH2();
  }

  @Override
  public void saveSQLite() {
    saveH2();
  }

  @Override
  public void loadH2() {
    if(TNE.instance().cache) {
      Collection<Account> accounts = loadAccounts();

      accounts.forEach((acc)->TNE.manager().addAccount(acc));
      TNE.instance().offlineIDS.putAll(loadIDS());
      Map<String, Transaction> transactions = loadTransactions();
      transactions.forEach((key, value)->TNE.transactionManager().add(value));
    }
  }

  @Override
  public void saveH2() {
    createTables("h2");
    db = new H2(TNE.sqlManager().getH2File(), TNE.sqlManager().getMysqlUser(), TNE.sqlManager().getMysqlPassword());
    String table = TNE.sqlManager().getPrefix() + "_INFO";
    h2().executePreparedUpdate("Update " + table + " SET version = ? WHERE id = 1;", new Object[] { String.valueOf(versionNumber()) });

    TNE.manager().getAccounts().forEach((id, account)->saveAccount(account));
    TNE.instance().offlineIDS.forEach(this::saveID);
    TNE.transactionManager().getTransactions().forEach((id, transaction)->saveTransaction(transaction));
  }

  @Override
  public void createTables(String type) {
    String prefix = TNE.sqlManager().getPrefix();
    String table = prefix + "_INFO";
    if(type.equalsIgnoreCase("h2")) {
      File h2DB = new File(TNE.sqlManager().getH2File());
      if (!h2DB.exists()) {
        try {
          h2DB.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
        "`id` INTEGER NOT NULL UNIQUE," +
        "`version` VARCHAR(10)," +
        "`server_name` VARCHAR(250)" +
        ");");
    sql().executePreparedUpdate("INSERT INTO `" + table + "` (id, version, server_name) VALUES(1, ?, ?) ON DUPLICATE KEY UPDATE version = ?, server_name = ?",
        new Object[] {
            versionNumber(),
            TNE.instance().getServer().getServerName(),
            versionNumber(),
            TNE.instance().getServer().getServerName()
        });

    table = prefix + "_ECOIDS";
    sql().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (" +
        "`username` VARCHAR(56)," +
        "`uuid` VARCHAR(36) UNIQUE" +
        ");");

    table = prefix + "_USERS";
    sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
        "`account_uuid` VARCHAR(36) NOT NULL UNIQUE," +
        "`account_username` VARCHAR(16) NOT NULL," +
        "`account_inventory_credits` LONGTEXT," +
        "`account_command_credits` LONGTEXT," +
        "`account_joined` VARCHAR(60)," +
        "`account_number` INTEGER," +
        "`account_status` VARCHAR(60)," +
        "`account_special` BOOLEAN," +
        ");");

    table = prefix + "_BALANCES";
    sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
        "`balances_uuid` VARCHAR(36) NOT NULL," +
        "`balances_server` VARCHAR(250) NOT NULL," +
        "`balances_world` VARCHAR(50) NOT NULL," +
        "`balances_currency` VARCHAR(250) NOT NULL," +
        "`balances_balance` VARCHAR(41)," +
        "PRIMARY KEY(balances_uuid, balances_server, balances_world, balances_currency)" +
        ");");

    table = prefix + "_TRACKED";
    sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
        "`tracked_uuid` VARCHAR(36) NOT NULL," +
        "`tracked_material` LONGTEXT," +
        "`tracked_location` VARCHAR(250)," +
        "`tracked_slot` INT(60) NOT NULL," +
        "PRIMARY KEY(tracked_uuid, tracked_location, tracked_slot)" +
        ");");

    table = prefix + "_TRANSACTIONS";
    sql().executeUpdate("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
        "`trans_id` VARCHAR(36)," +
        "`trans_world` VARCHAR(36)," +
        "`trans_initiator` VARCHAR(36)," +
        "`trans_initiator_obalance` VARCHAR(41)," +
        "`trans_initiator_balance` VARCHAR(41)," +
        "`trans_recipient` VARCHAR(36)," +
        "`trans_recipient_obalance` VARCHAR(41)," +
        "`trans_recipient_balance` VARCHAR(41)," +
        "`trans_type` VARCHAR(60)," +
        "`trans_cost` VARCHAR(41)," +
        "`trans_time` BIGINT(60)," +
        "`trans_extra` LONGTEXT," +
        "PRIMARY KEY(trans_id)" +
        ");");
    sql().close();
  }
}