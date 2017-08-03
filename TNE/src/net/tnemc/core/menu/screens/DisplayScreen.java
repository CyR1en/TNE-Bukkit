package net.tnemc.core.menu.screens;

import com.github.tnerevival.menu.MenuScreen;
import com.github.tnerevival.menu.MenuViewer;
import com.github.tnerevival.menu.icon.MenuIcon;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.Currency;
import net.tnemc.core.menu.screens.display.CurrencyIcon;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
 * Created by Daniel on 7/30/2017.
 */
public class DisplayScreen extends MenuScreen {
  private Inventory inventory;
  private int size;

  public DisplayScreen() {
  }

  @Override
  public void open(Player player) {
    initializeCurrencies(IDFinder.getID(player));
    super.open(player);
  }

  private void initializeCurrencies(UUID viewer) {
    MenuViewer viewerInstance = TNE.instance().menuManager().getViewer(viewer);
    String world = (String)viewerInstance.getData("action_world");
    String player = (String)viewerInstance.getData("action_player");
    int i = 0;
    for(Currency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      ItemStack stack = new ItemStack(Material.PAPER, 1);
      ItemMeta meta = stack.getItemMeta();
      meta.setDisplayName(currency.getSingle());
      stack.setItemMeta(meta);
      icons.put(i, new CurrencyIcon(i, stack, "display", "", "", world, currency.getSingle(), player));
      i++;
    }


    size = icons.size() / 9;
    if((icons.size() % 9) > 0) size++;
    inventory = Bukkit.createInventory(null, size * 9, "[TNE]Action");

    for(MenuIcon icon : icons.values()) {
      inventory.setItem(icon.getSlot(), icon.getStack());
    }
  }

  @Override
  public String getName() {
    return "display";
  }

  @Override
  public boolean isMain() {
    return false;
  }

  @Override
  public void addIcon(MenuIcon icon) {
    size = icons.size() / 9;
    if((icons.size() % 9) > 0) size++;
    inventory.setItem(icon.getSlot(), icon.getStack());
    super.addIcon(icon);
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}