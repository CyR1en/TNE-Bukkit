package net.tnemc.core.common.utils;

import org.bukkit.Material;

/*
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
 * Created by creatorfromhell on 07/01/2017.
 */
public class MaterialUtils {
  public static String formatMaterialName(Material material) {
    String[] wordsSplit = material.name().split("_");
    String sReturn = "";
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
    }
    return sReturn;
  }

  public static String deformatMaterialName(String name) {
    String[] split = name.split("(?=[A-Z])");
    String sReturn = "";
    int count = 1;
    for(String s : split) {
      sReturn += s.toUpperCase();
      if(count < split.length && count > 1) {
        sReturn += "_";
      }
      count++;
    }
    return sReturn;
  }

  public static String formatMaterialNameWithSpace(Material material) {
    String[] wordsSplit = material.name().split("_");
    String sReturn = "";
    int count = 1;
    for(String w: wordsSplit) {
      String word = w.toUpperCase().replace(w.substring(1), w.substring(1).toLowerCase());
      sReturn += word;
      if(count < wordsSplit.length) {
        sReturn += " ";
      }
    }
    return sReturn;
  }

  public static String deformatMaterialNameWithSpace(String name) {
    String upperCase = name.toUpperCase();
    return upperCase.replace(" ", "_");
  }
}