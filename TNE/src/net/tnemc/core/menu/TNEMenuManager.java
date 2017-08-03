package net.tnemc.core.menu;

import com.github.tnerevival.menu.MenuManager;
import com.github.tnerevival.menu.MenuScreen;
import com.github.tnerevival.menu.MenuViewer;
import com.github.tnerevival.menu.ScreenHolder;
import net.tnemc.core.TNE;

import java.util.HashMap;
import java.util.Map;
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
public class TNEMenuManager extends MenuManager {

  private Map<String, ScreenHolder> holders = new HashMap<>();
  private Map<UUID, MenuViewer> viewers = new HashMap<>();

  @Override
  public ScreenHolder getHolder(UUID uuid) {
    if(viewers.get(uuid) != null) {
      return holders.get(viewers.get(uuid).getScreenHolder());
    }
    return null;
  }

  @Override
  public MenuScreen getScreen(UUID uuid) {
    if(viewers.get(uuid) != null) {
      MenuViewer viewer = viewers.get(uuid);
      return holders.get(viewer.getScreenHolder()).getScreens().get(viewer.getCurrentMenu());
    }
    return null;
  }

  @Override
  public void addViewer(MenuViewer menuViewer) {
    viewers.put(menuViewer.getId(), menuViewer);
  }

  @Override
  public void removeViewer(UUID uuid) {
    viewers.remove(uuid);
    TNE.debug("Removing viewer " + uuid.toString());
  }

  @Override
  public MenuViewer getViewer(UUID uuid) {
    return viewers.get(uuid);
  }

  @Override
  public void addMenu(ScreenHolder screenHolder) {
    holders.put(screenHolder.getName(), screenHolder);
  }

  @Override
  public ScreenHolder getHolder(String name) {
    return holders.get(name);
  }

  @Override
  public Map<UUID, MenuViewer> getViewers() {
    return viewers;
  }
}