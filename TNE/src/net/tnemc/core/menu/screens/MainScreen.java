package net.tnemc.core.menu.screens;

import com.github.tnerevival.menu.MenuScreen;
import com.github.tnerevival.menu.icon.MenuIcon;
import net.tnemc.core.menu.screens.main.DisplayIcon;
import net.tnemc.core.menu.screens.main.PayIcon;
import net.tnemc.core.menu.screens.main.SetIcon;
import net.tnemc.core.menu.screens.main.TakeIcon;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

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
 * Created by Daniel on 7/28/2017.
 */
public class MainScreen extends MenuScreen {
  private Inventory inventory;
  private int size;

  public MainScreen() {

    icons.put(3, new SetIcon());
    icons.put(4, new TakeIcon());
    icons.put(5, new PayIcon());
    icons.put(6, new DisplayIcon());

    size = icons.size() / 9;
    if((icons.size() % 9) > 0) size++;
    inventory = Bukkit.createInventory(null, size * 9, "[TNE]Action");

    for(MenuIcon icon : icons.values()) {
      inventory.setItem(icon.getSlot(), icon.getStack());
    }
  }

  @Override
  public String getName() {
    return "main";
  }

  @Override
  public boolean isMain() {
    return true;
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