package net.tnemc.core.listeners;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.version.ReleaseType;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    Account account = Account.getAccount(id.toString());
    if(TNE.instance().api().getBoolean("Core.Update.Notify") && player.hasPermission("tne.admin") && !TNE.instance().updater.getRelease().equals(ReleaseType.LATEST)) {
      String message = ChatColor.RED + "[TNE] Outdated! The current build is " + TNE.instance().updater.getBuild();
      if(TNE.instance().updater.getRelease().equals(ReleaseType.PRERELEASE)) {
        message = ChatColor.GREEN + "[TNE] Prerelease! Thank you for testing TNE Build: " + TNE.instance().updater.getCurrentBuild() + ".";
      }
      player.sendMessage(message);
    }

    if (plugin.getWorldManager(world) != null && !plugin.getWorldManager(world).isEconomyDisabled() || plugin.getWorldManager(world) == null) {
      if (!account.confirmed(WorldFinder.getWorld(player))) {
        String node = "Messages.Account.Confirm";
        if (account.getPin().equalsIgnoreCase("TNENOSTRINGVALUE")) {
          node = "Messages.Account.Set";
        }
        new Message(node).translate(WorldFinder.getWorld(player), player);
      }
    }

    TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value->{
      account.setCurrencyItems(TNE.instance().manager().currencyManager().get(world, value).get(),
                               account.getHoldings(world, value));
    });
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    if(TNE.instance().manager().exists(id)) {
      Account account = Account.getAccount(id.toString());
      account.setLastOnline(new Date().getTime());
      TNE.instance().manager().addAccount(account);
    }
  }
}