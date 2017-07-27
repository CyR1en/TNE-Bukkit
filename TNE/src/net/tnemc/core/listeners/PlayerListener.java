package net.tnemc.core.listeners;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.WorldFinder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

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
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    if(event.getInventory().getTitle() != null &&
       event.getInventory().getTitle().equalsIgnoreCase("[TNE]Coming Soon")) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    Player player = (Player)event.getPlayer();
    UUID id = IDFinder.getID(player);
    String world = WorldFinder.getWorld(player);
  }

  @EventHandler
  public void onInteractEntityEvent(PlayerInteractEntityEvent event) {
    Player player = event.getPlayer();

    ItemStack take = new ItemStack(Material.WOOL);
    Wool takeData = (Wool)take.getData();
    takeData.setColor(DyeColor.RED);
    take.setData(takeData);
    ItemMeta takeMeta = take.getItemMeta();
    takeMeta.setDisplayName(ChatColor.WHITE + "Take Funds");
    take.setItemMeta(takeMeta);

    ItemStack pay = new ItemStack(Material.WOOL);
    Wool payData = (Wool)pay.getData();
    payData.setColor(DyeColor.YELLOW);
    pay.setData(payData);
    ItemMeta payMeta = pay.getItemMeta();
    payMeta.setDisplayName(ChatColor.WHITE + "Pay Funds");
    pay.setItemMeta(payMeta);

    ItemStack set = new ItemStack(Material.WOOL);
    Wool setData = (Wool)set.getData();
    setData.setColor(DyeColor.GRAY);
    set.setData(setData);
    ItemMeta setMeta = set.getItemMeta();
    setMeta.setDisplayName(ChatColor.WHITE + "Set Funds");
    set.setItemMeta(setMeta);

    ItemStack loan = new ItemStack(Material.WOOL);
    Wool loanData = (Wool)loan.getData();
    loanData.setColor(DyeColor.WHITE);
    loan.setData(loanData);
    ItemMeta loanMeta = loan.getItemMeta();
    loanMeta.setDisplayName(ChatColor.WHITE + "Loan Funds");
    loan.setItemMeta(loanMeta);

    ItemStack balance = new ItemStack(Material.WOOL);
    Wool balanceData = (Wool)balance.getData();
    balanceData.setColor(DyeColor.GREEN);
    balance.setData(balanceData);
    ItemMeta balanceMeta = balance.getItemMeta();
    balanceMeta.setDisplayName(ChatColor.WHITE + "Display Funds");
    balance.setItemMeta(balanceMeta);


    Inventory inventory = Bukkit.createInventory(null, 9, "[TNE]Coming Soon");
    inventory.setItem(2, take);
    inventory.setItem(3, pay);
    inventory.setItem(4, set);
    inventory.setItem(5, loan);
    inventory.setItem(6, balance);
    player.openInventory(inventory);
  }
}