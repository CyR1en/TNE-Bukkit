package net.tnemc.core.common;

import com.github.tnerevival.core.SQLManager;


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
public class TNESQLManager extends SQLManager {
  public TNESQLManager(String mysqlHost, Integer mysqlPort, String mysqlDatabase, String mysqlUser, String mysqlPassword, String prefix, String h2File, String sqliteFile, String flatfile) {
    super(mysqlHost, mysqlPort, mysqlDatabase, mysqlUser, mysqlPassword, prefix, h2File, sqliteFile, flatfile, true);
  }

  public boolean backup() {
    //TODO: Data handling.
    return false;
  }

  public void recreate() {
    //TODO: Data handling.
  }
}