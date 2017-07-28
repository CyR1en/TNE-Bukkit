package net.tnemc.core.menu.screens;

import com.github.tnerevival.menu.MenuScreen;
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
public class TakeScreen extends MenuScreen {
  private Inventory inventory;

  public TakeScreen() {
    inventory = Bukkit.createInventory(null, 9, "[TNE]Action");
  }

  @Override
  public String getName() {
    return "take";
  }

  @Override
  public boolean isMain() {
    return false;
  }

  @Override
  public Inventory getInventory() {
    return inventory;
  }
}