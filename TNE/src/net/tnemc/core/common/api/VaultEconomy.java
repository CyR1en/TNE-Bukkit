package net.tnemc.core.common.api;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.tnemc.core.TNE;
import org.bukkit.OfflinePlayer;

import java.util.List;

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
 * Created by Daniel on 8/3/2017.
 */
public class VaultEconomy implements Economy {

  private TNE plugin;

  public VaultEconomy(TNE plugin) {
    this.plugin = plugin;
  }

  //TODO: Vault support
  @Override
  public boolean isEnabled() {
    return false;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public boolean hasBankSupport() {
    return false;
  }

  @Override
  public int fractionalDigits() {
    return 0;
  }

  @Override
  public String format(double amount) {
    return null;
  }

  @Override
  public String currencyNamePlural() {
    return null;
  }

  @Override
  public String currencyNameSingular() {
    return null;
  }

  @Override
  public boolean hasAccount(String s) {
    return false;
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer) {
    return false;
  }

  @Override
  public boolean hasAccount(String s, String s1) {
    return false;
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
    return false;
  }

  @Override
  public double getBalance(String s) {
    return 0;
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer) {
    return 0;
  }

  @Override
  public double getBalance(String s, String s1) {
    return 0;
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer, String s) {
    return 0;
  }

  @Override
  public boolean has(String s, double v) {
    return false;
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, double v) {
    return false;
  }

  @Override
  public boolean has(String s, String s1, double v) {
    return false;
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
    return false;
  }

  @Override
  public EconomyResponse withdrawPlayer(String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
    return null;
  }

  @Override
  public EconomyResponse withdrawPlayer(String s, String s1, double v) {
    return null;
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(String s, String s1, double v) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse createBank(String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
    return null;
  }

  @Override
  public EconomyResponse deleteBank(String s) {
    return null;
  }

  @Override
  public EconomyResponse bankBalance(String s) {
    return null;
  }

  @Override
  public EconomyResponse bankHas(String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse bankWithdraw(String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse bankDeposit(String s, double v) {
    return null;
  }

  @Override
  public EconomyResponse isBankOwner(String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
    return null;
  }

  @Override
  public EconomyResponse isBankMember(String s, String s1) {
    return null;
  }

  @Override
  public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
    return null;
  }

  @Override
  public List<String> getBanks() {
    return null;
  }

  @Override
  public boolean createPlayerAccount(String s) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(String s, String s1) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
    return false;
  }
}
