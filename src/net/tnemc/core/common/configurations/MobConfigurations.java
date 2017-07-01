package net.tnemc.core.common.configurations;

import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;

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
 * Created by creatorfromhell on 06/30/2017.
 */
public class MobConfigurations extends Configuration {
  @Override
  public FileConfiguration getConfiguration() {
    return TNE.instance().mobConfigurations;
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Mobs");
    return nodes;
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Mobs.Enabled", true);
    configurations.put("Mobs.EnableAge", true);
    configurations.put("Mobs.Message", true);
    configurations.put("Mobs.Multiplier", 1.0);
    configurations.put("Mobs.Default.Enabled", true);
    configurations.put("Mobs.Default.Reward", 10.00);
    configurations.put("Mobs.Custom.Enabled", true);
    configurations.put("Mobs.Custom.Reward", 10.00);
    configurations.put("Mobs.Player.Enabled", true);
    configurations.put("Mobs.Player.Reward", 10.00);

    for(EntityType type : EntityType.values()) {
      if(type.isAlive() && !type.equals(EntityType.PLAYER)) {
        System.out.println("Adding Mob " + type.getEntityClass().getSimpleName());
        String formatted = formatName(type.getName());
        configurations.put("Mobs." + formatted + ".Enabled", true);
        configurations.put("Mobs." + formatted + ".Reward", 10.00);
        if(type.getEntityClass().isAssignableFrom(Ageable.class)) {
          configurations.put("Mobs." + formatted + ".Baby.Enabled", true);
          configurations.put("Mobs." + formatted + ".Baby.Reward", 5.00);
        }
      }
    }

    for(String s : configurationFile.getConfigurationSection("Mobs").getKeys(false)) {
      addChance(configurationFile, "Mobs." + s);
      addChance(configurationFile, "Mobs." + s + ".Baby");
    }

    loadExtras(configurationFile);
    super.load(configurationFile);
  }

  public static String formatName(String name) {
    String[] wordsSplit = name.split("_");
    String sReturn = "";
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
    }
    return sReturn;
  }

  private void loadExtras(FileConfiguration configurationFile) {
    String base = "Mobs.Player.Individual";
    Set<String> identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

    for(String s : identifiers) {
      String temp = base + "." + s;
      MISCUtils.debug(temp);
      Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
      Double reward = (!configurationFile.contains(temp + ".Reward"))? 10.0 : configurationFile.getDouble(temp + ".Reward");
      addChance(configurationFile, temp);
      configurations.put(temp + ".Enabled", enabled);
      configurations.put(temp + ".Reward", reward);
    }

    base = "Mobs.Custom.Entries";
    if(configurationFile.contains(base)) {
      identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

      for (String s : identifiers) {
        String temp = base + "." + s;
        MISCUtils.debug(temp);
        addChance(configurationFile, temp);
        Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
        Double reward = (!configurationFile.contains(temp + ".Reward")) ? 10.0 : configurationFile.getDouble(temp + ".Reward");
        configurations.put(temp + ".Enabled", enabled);
        configurations.put(temp + ".Reward", reward);

        if (configurationFile.contains(temp + ".Baby")) {
          addChance(configurationFile, temp + ".Baby");
          Boolean babyEnabled = !configurationFile.contains(temp + ".Baby.Enabled") || configurationFile.getBoolean(temp + ".Baby.Enabled");
          Double babyReward = (!configurationFile.contains(temp + ".Baby.Reward")) ? 10.0 : configurationFile.getDouble(temp + ".Baby.Reward");
          configurations.put(temp + ".Baby.Enabled", babyEnabled);
          configurations.put(temp + ".Baby.Reward", babyReward);
        }
      }
    }

    base = "Mobs.Slime";
    identifiers = configurationFile.getConfigurationSection(base).getKeys(false);

    for(String s : identifiers) {
      if(s.equalsIgnoreCase("Enabled") || s.equalsIgnoreCase("Reward")) continue;
      String temp = base + "." + s;
      MISCUtils.debug(temp);
      addChance(configurationFile, temp);
      Boolean enabled = !configurationFile.contains(temp + ".Enabled") || configurationFile.getBoolean(temp + ".Enabled");
      Double reward = (!configurationFile.contains(temp + ".Reward"))? 10.0 : configurationFile.getDouble(temp + ".Reward");
      MISCUtils.debug("configurations.put(" + temp + ".Enabled, " + enabled + ")");
      MISCUtils.debug("configurations.put(" + temp + ".Reward, " + reward + ")");
      configurations.put(temp + ".Enabled", enabled);
      configurations.put(temp + ".Reward", reward);
    }
  }

  private void addChance(FileConfiguration configurationFile, String base) {
    MISCUtils.debug("MobConfiguration.addChance(mobs.yml, " + base + ")");
    if(configurationFile.contains(base + ".Multiplier")) {
      configurations.put(base + ".Multiplier", configurationFile.getDouble(base + ".Multiplier"));
    }

    if(configurationFile.contains(base + ".Chance.Min") || configurationFile.contains(base + ".Chance.Max")) {
      Double min = (!configurationFile.contains(base + ".Chance.Min"))? configurationFile.getDouble(base + ".Chance.Max") - 5.0 : configurationFile.getDouble(base + ".Chance.Min");
      Double max = (!configurationFile.contains(base + ".Chance.Max"))? configurationFile.getDouble(base + ".Chance.Min") + 5.0 : configurationFile.getDouble(base + ".Chance.Max");
      configurations.put(base + ".Chance.Min", min);
      configurations.put(base + ".Chance.Max", max);
    }
  }
}