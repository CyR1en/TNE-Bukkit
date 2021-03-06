package net.tnemc.core.menu.icons.main;

import net.tnemc.core.menu.icons.Icon;
import org.bukkit.Material;

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
 * Created by Daniel on 11/5/2017.
 */
public class SetIcon extends Icon {

  public SetIcon() {
    super(3, Material.STAINED_GLASS_PANE, "Set Funds", (byte)11);

    data.put("action_type", "set");
    this.switchMenu = "cur_selection_set";
    this.node = "tne.menu.set";
  }
}