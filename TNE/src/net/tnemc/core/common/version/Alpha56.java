package net.tnemc.core.common.version;

import com.github.tnerevival.core.SQLManager;
import com.github.tnerevival.core.version.Version;

import java.io.File;

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
 * Created by Daniel on 7/12/2017.
 */
public class Alpha56 extends Version {

  public Alpha56(SQLManager sqlManager) {
    super(sqlManager);
  }

  @Override
  public boolean firstRun() {
    return false;
  }

  @Override
  public double getSaveVersion() {
    return 5.6;
  }

  @Override
  public double versionNumber() {
    return 5.6;
  }

  @Override
  public void update(double version, String type) {

  }

  @Override
  public void loadFlat(File file) {

  }

  @Override
  public void saveFlat(File file) {

  }

  @Override
  public void loadMySQL() {

  }

  @Override
  public void saveMySQL() {

  }

  @Override
  public void loadSQLite() {
    loadH2();
  }

  @Override
  public void saveSQLite() {
    saveH2();
  }

  @Override
  public void loadH2() {

  }

  @Override
  public void saveH2() {

  }

  @Override
  public void createTables(String type) {

  }
}