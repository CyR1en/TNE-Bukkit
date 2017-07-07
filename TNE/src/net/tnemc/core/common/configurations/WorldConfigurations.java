package net.tnemc.core.common.configurations;

import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
 * Created by Daniel on 7/7/2017.
 */
public class WorldConfigurations extends Configuration {
  @Override
  public FileConfiguration getConfiguration() {
    return TNE.instance().worldConfigurations;
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Worlds");
    return nodes;
  }

  @Override
  public void load(FileConfiguration configurationFile) {

    Set<String> worlds = configurationFile.getConfigurationSection("Worlds").getKeys(false);

    for(String world : worlds) {
      WorldManager manager = TNE.instance().getWorldManager(world);
      if(manager == null) {
        continue;
      }
      Set<String> configurations = configurationFile.getConfigurationSection("Worlds." + world).getKeys(true);

      for(String s : configurations) {
        String node = "Worlds." + world + "." + s;
        if(!configurationFile.isConfigurationSection(node) && !node.contains("Worlds." + world + ".Currency")) {
          manager.setConfiguration(node, configurationFile.get(node));
        }
      }
      TNE.instance().addWorldManager(manager);
    }

    super.load(configurationFile);
  }
}
