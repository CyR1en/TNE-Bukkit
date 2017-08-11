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

  private TNE plugin = null;
  private TNEAPI api = null;

  public VaultEconomy(TNE plugin) {
    this.plugin = plugin;
    this.api = plugin.api();
  }

  //TODO: Vault support
  @Override
  public boolean isEnabled() {
    return api != null;
  }

  @Override
  public String getName() {
    return "TheNewEconomy";
  }

  @Override
  public boolean hasBankSupport() {
    return TNE.loader().hasModule("Banks");
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
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).getPlural();
  }

  @Override
  public String currencyNameSingular() {
    return TNE.manager().currencyManager().get(TNE.instance().defaultWorld).getSingle();
  }

  @Override
  public boolean hasAccount(String username) {
    return false;
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer) {
    return false;
  }

  @Override
  public boolean hasAccount(String username, String world) {
    return false;
  }

  @Override
  public boolean hasAccount(OfflinePlayer offlinePlayer, String world) {
    return false;
  }

  @Override
  public double getBalance(String username) {
    return 0;
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer) {
    return 0;
  }

  @Override
  public double getBalance(String username, String world) {
    return 0;
  }

  @Override
  public double getBalance(OfflinePlayer offlinePlayer, String world) {
    return 0;
  }

  @Override
  public boolean has(String username, double amount) {
    return false;
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, double amount) {
    return false;
  }

  @Override
  public boolean has(String username, String world, double amount) {
    return false;
  }

  @Override
  public boolean has(OfflinePlayer offlinePlayer, String world, double amount) {
    return false;
  }

  @Override
  public EconomyResponse withdrawPlayer(String username, double amount) {
    return null;
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
    return null;
  }

  @Override
  public EconomyResponse withdrawPlayer(String username, String world, double amount) {
    return null;
  }

  @Override
  public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(String username, double amount) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(String username, String world, double amount) {
    return null;
  }

  @Override
  public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String world, double amount) {
    return null;
  }

  @Override
  public EconomyResponse createBank(String name, String world) {
    return null;
  }

  @Override
  public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
    return null;
  }

  @Override
  public EconomyResponse deleteBank(String name) {
    return null;
  }

  @Override
  public EconomyResponse bankBalance(String name) {
    return null;
  }

  @Override
  public EconomyResponse bankHas(String name, double amount) {
    return null;
  }

  @Override
  public EconomyResponse bankWithdraw(String name, double amount) {
    return null;
  }

  @Override
  public EconomyResponse bankDeposit(String name, double amount) {
    return null;
  }

  @Override
  public EconomyResponse isBankOwner(String name, String username) {
    return null;
  }

  @Override
  public EconomyResponse isBankOwner(String name, OfflinePlayer offlinePlayer) {
    return null;
  }

  @Override
  public EconomyResponse isBankMember(String name, String username) {
    return null;
  }

  @Override
  public EconomyResponse isBankMember(String name, OfflinePlayer offlinePlayer) {
    return null;
  }

  @Override
  public List<String> getBanks() {
    return null;
  }

  @Override
  public boolean createPlayerAccount(String username) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(String username, String world) {
    return false;
  }

  @Override
  public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String world) {
    return false;
  }
}
