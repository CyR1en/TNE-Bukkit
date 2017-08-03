package net.tnemc.core.listeners;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.version.ReleaseType;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.transaction.Transaction;
import net.tnemc.core.common.transaction.TransactionCost;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.type.TransactionWorldChange;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.math.BigDecimal;
import java.util.Date;
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
 * Created by Daniel on 7/8/2017.
 */
public class ConnectionListener implements Listener {

  TNE plugin;

  public ConnectionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    TNE.debug(id + "");
    boolean first = !TNE.manager().exists(id);
    Account account = Account.getAccount(id.toString());
    if(first) account.initializeHoldings(world);
    if(TNE.instance().api().getBoolean("Core.Update.Notify") && player.hasPermission("tne.admin") && !TNE.instance().updater.getRelease().equals(ReleaseType.LATEST)) {
      String message = ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.instance().updater.getBuild();
      if(TNE.instance().updater.getRelease().equals(ReleaseType.PRERELEASE)) {
        message = ChatColor.GREEN + "[TNE] Prerelease! Thank you for testing TNE Build: " + TNE.instance().updater.getCurrentBuild() + ".";
      }
      player.sendMessage(message);
    }

    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();
    if(!noEconomy) {
      TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value -> {
        account.setCurrencyItems(TNE.manager().currencyManager().get(world, value),
            account.getHoldings(world, value));
      });
    }

    account.getHistory().populateAway(account.getLastOnline());
    TNE.manager().addAccount(account);
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    if(TNE.manager().exists(id)) {
      Account account = Account.getAccount(id.toString());
      account.setLastOnline(new Date().getTime());
      account.getHistory().clearAway();
      TNE.manager().addAccount(account);
    }
  }

  @EventHandler
  public void onWorldChange(final PlayerChangedWorldEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = player.getWorld().getName();

    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();
    if(!noEconomy && TNE.instance().api().getBoolean("Core.World.EnableChangeFee", world, IDFinder.getID(player).toString())) {
      if(!player.hasPermission("tne.bypass.world")) {
        Transaction transaction = new Transaction(id, id, world, new TransactionWorldChange(new TransactionCost(new BigDecimal(0.0), TNE.manager().currencyManager().get(world))));
        TransactionResult result = TNE.transactionManager().perform(transaction);
        if(!result.proceed()) {
          player.teleport(event.getFrom().getSpawnLocation());
        }
        Message message = new Message(result.recipientMessage());
        message.addVariable("$amount", CurrencyFormatter.format(WorldFinder.getWorld(player), new BigDecimal(0.0)));
        message.translate(world, player);
      }
      Account.getAccount(id.toString()).initializeHoldings(world);
    }

    if(!noEconomy && !TNE.instance().api().getBoolean("Core.World.EnableChangeFee", world, IDFinder.getID(player).toString())) {
      Account.getAccount(id.toString()).initializeHoldings(world);
    }
  }

  @EventHandler
  public void onWorldLoad(final WorldLoadEvent event) {
    String world = event.getWorld().getName();
    TNE.manager().currencyManager().initializeWorld(world);
  }
}