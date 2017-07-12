package net.tnemc.core.listeners;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

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
public class PlayerListener {

  @EventHandler
  public void onPickup(PlayerPickupItemEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    Player player = (Player)event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
  }
}