package net.tnemc.core.menu;

import com.github.tnerevival.menu.Menu;
import net.tnemc.core.TNE;
import net.tnemc.core.menu.screens.MainScreen;
import net.tnemc.core.menu.screens.TakeScreen;

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
public class TNEActionMenu extends Menu {

  public TNEActionMenu() {
    super("[TNE]Action", "action");
    registerScreens();
    registerIcons();
  }

  public void registerScreens() {
    screens.put("main", new MainScreen());
    screens.put("take", new TakeScreen());
    TNE.loader().getModules().forEach((key, value)->{
      screens.putAll(value.getModule().registerScreens());
    });
  }

  public void registerIcons() {
    TNE.loader().getModules().forEach((key, value)->{
      value.getModule().registerIcons().forEach((k, v)->{
        screens.get(v.getScreen()).addIcon(v);
        icons.put(k, v);
      });
    });

  }
}