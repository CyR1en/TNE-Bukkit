package net.tnemc.core.listeners;

import com.github.tnerevival.menu.MenuViewer;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.Currency;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.Optional;
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
 * Created by Daniel on 7/12/2017.
 */
public class PlayerListener implements Listener {

  TNE plugin;

  public PlayerListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPickup(PlayerPickupItemEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      Account account = Account.getAccount(id.toString());
      Optional<Currency> currency = TNE.manager().currencyManager().currencyFromItem(world, event.getItem().getItemStack());
      currency.ifPresent((cur)->{
        account.recalculateCurrencyHoldings(world, cur.getSingle());
      });
      TNE.manager().addAccount(account);
    }
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      Account account = Account.getAccount(id.toString());
      Optional<Currency> currency = TNE.manager().currencyManager().currencyFromItem(world, event.getItemDrop().getItemStack());
      currency.ifPresent((cur)->{
        account.recalculateCurrencyHoldings(world, cur.getSingle());
      });
      TNE.manager().addAccount(account);
    }
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    UUID id = IDFinder.getID(player);
    TNE.debug("Menu Viewers Length: " + TNE.instance().menuManager().getViewers().size());
    if(TNE.instance().menuManager().getViewer(id) != null) {
      TNE.debug("Player clicked in menu from menu manager. Slot: " + event.getRawSlot() + " Non Raw: " + event.getSlot());
      event.setCancelled(true);
      TNE.instance().menuManager().getHolder(id).onClick(event.getRawSlot(), player);
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    Player player = (Player)event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy) {
      if(TNE.instance().menuManager().getViewer(id) != null) {
        TNE.instance().menuManager().getHolder(id).onClose(player);
      } else {
        Account account = Account.getAccount(id.toString());
        account.recalculateItemHoldings(world);
        TNE.manager().addAccount(account);
      }
    }
  }

  @EventHandler
  public void onInteractEntityEvent(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
    boolean noEconomy = TNE.instance().getWorldManager(world).isEconomyDisabled();

    if(!noEconomy && event.getRightClicked() instanceof Player) {
      UUID actionPlayer = IDFinder.getID((Player)event.getRightClicked());
      MenuViewer viewer = new MenuViewer(id, "action", TNE.instance().menuManager().getHolder("action").getMainMenu());
      viewer.setData("action_player", actionPlayer);
      viewer.setData("action_world", world);
      TNE.instance().menuManager().getHolder("action").onOpen(player);

      plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
        TNE.instance().menuManager().addViewer(viewer);
      }, 20 * 5);
    }
  }
}