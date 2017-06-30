package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.core.currency.Tier;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigDecimal;
import java.util.*;

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
 * Created by creatorfromhell on 10/22/2016.
 */
public class CurrencyManager {
  private static BigDecimal largestSupported;
  private Map<String, Currency> globalCurrencies = new HashMap<>();
  private Set<String> worlds = TNE.instance().worldConfigurations.getConfigurationSection("Worlds").getKeys(false);

  //Cache-related maps.
  private Map<String, List<Currency>> trackedCurrencies = new HashMap<>();
  private List<String> globalDisabled = new ArrayList<>();

  public CurrencyManager() {
    loadCurrencies();
  }

  public void loadCurrencies() {
    largestSupported = new BigDecimal("900000000000000000000000000000000000");
    trackedCurrencies = new HashMap<>();

    loadCurrency(TNE.instance().getConfig(), false, TNE.instance().defaultWorld);
    for(String s : worlds) {
      loadCurrency(TNE.instance().worldConfigurations, true, s);
    }
    largestSupported = null;
  }

  private void loadCurrency(FileConfiguration configuration, boolean world, String worldName) {
    String curBase = ((world)? "Worlds." + worldName : "Core") + ".Currency";
    if(configuration.contains(curBase)) {

      Set<String> currencies = configuration.getConfigurationSection(curBase).getKeys(false);
      MISCUtils.debug(currencies.toArray().toString());

      for(String cur : currencies) {
        if (configuration.contains("Core.Currency." + cur + ".Disabled") &&
            configuration.getBoolean("Core.Currency." + cur + ".Disabled")) {
              return;
        }

        MISCUtils.debug("[Loop]Loading Currency: " + cur + " for world: " + worldName);
        String base = curBase + "." + cur;
        BigDecimal balance = configuration.contains(base + ".Balance")?  new BigDecimal(configuration.getString(base + ".Balance")) : new BigDecimal(200.00);
        String decimal = configuration.contains(base + ".Decimal")? configuration.getString(base + ".Decimal") : ".";
        Integer decimalPlaces = configuration.contains(base + ".DecimalPlace")? ((configuration.getInt(base + ".DecimalPlace") > 4)? 4 : configuration.getInt(base + ".DecimalPlace")) : 2;
        BigDecimal maxBalance = configuration.contains(base + ".MaxBalance")? ((new BigDecimal(configuration.getString(base + ".MaxBalance")).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(configuration.getString(base + ".MaxBalance"))) : largestSupported;
        String format = configuration.contains(base + ".Format")? configuration.getString(base + ".Format").trim() : "<symbol><major.amount><decimal><minor.amount>";
        String prefixes = configuration.contains(base + ".Prefixes")? configuration.getString(base + ".Prefixes").trim() : "kMGTPEZYXWV";
        Boolean worldDefault = !configuration.contains(base + ".Default") || configuration.getBoolean(base + ".Default");
        Double rate = configuration.contains(base + ".Conversion")? configuration.getDouble(base + ".Conversion") : 1.0;
        Boolean item = configuration.contains(base + ".ItemCurrency") && configuration.getBoolean(base + ".ItemCurrency");
        Boolean vault = configuration.contains(base + ".Vault") && configuration.getBoolean(base + ".Vault");
        Boolean notable = configuration.contains(base + ".Notable") && configuration.getBoolean(base + ".Notable");
        Boolean bankChest = configuration.contains(base + ".BankChest") && configuration.getBoolean(base + ".BankChest");
        Boolean ender = configuration.contains(base + ".EnderChest") && configuration.getBoolean(base + ".EnderChest");
        Boolean track = configuration.contains(base + ".TrackChest") && configuration.getBoolean(base + ".TrackChest");
        Boolean separate = configuration.contains(base + ".Major.Separate") && configuration.getBoolean(base + ".Major.Separate");
        String separator = configuration.contains(base + ".Major.Separator")? configuration.getString(base + ".Major.Separator") : ",";
        String symbol = configuration.contains(base + ".Symbol")? configuration.getString(base + ".Symbol") : "$";
        String major = configuration.contains(base + ".Major.Single")? configuration.getString(base + ".Major.Single") : "dollar";
        String majorPlural = configuration.contains(base + ".Major.Plural")? configuration.getString(base + ".Major.Plural") : "dollars";
        String minor = configuration.contains(base + ".Minor.Single")? configuration.getString(base + ".Minor.Single") : "cent";
        String minorPlural = configuration.contains(base + ".Minor.Plural")? configuration.getString(base + ".Minor.Plural") : "cents";
        String majorItem = configuration.contains(base + ".Minor.Major")? configuration.getString(base + ".Minor.Major") : "GOLD_INGOT";
        String minorItem = configuration.contains(base + ".Minor.Item")? configuration.getString(base + ".Minor.Item") : "IRON_INGOT";
        Integer minorWeight = configuration.contains(base + ".Minor.Weight")? configuration.getInt(base + ".Minor.Weight") : 100;

        //Interest-related configurations
        Boolean interestEnabled = !configuration.contains(base + ".Interest.Enabled") || configuration.getBoolean(base + ".Interest.Enabled");
        Double interestRate = configuration.contains(base + ".Interest.Rate")? configuration.getDouble(base + ".Interest.Rate") : 0.2;
        Long interestInterval = configuration.contains(base + ".Interest.Interval")? configuration.getLong(base + ".Interest.Interval") * 1000 : 1800;


        Tier majorTier = new Tier();
        majorTier.setSymbol(symbol);
        majorTier.setMaterial(majorItem);
        majorTier.setSingle(major);
        majorTier.setPlural(majorPlural);

        Tier minorTier = new Tier();
        minorTier.setSymbol(symbol);
        minorTier.setMaterial(minorItem);
        minorTier.setSingle(minor);
        minorTier.setPlural(minorPlural);
        minorTier.setWeight(minorWeight);

        Currency currency = new Currency();
        currency.setMaxBalance(maxBalance);
        currency.setBalance(balance);
        currency.setDecimal(decimal);
        currency.setDecimalPlaces(decimalPlaces);
        currency.setFormat(format);
        currency.setPrefixes(prefixes);
        currency.setName(cur);
        currency.setWorldDefault(worldDefault);
        currency.setRate(rate);
        currency.setItem(item);
        currency.setVault(vault);
        currency.setNotable(notable);
        currency.setBankChest(bankChest);
        currency.setEnderChest(ender);
        currency.setTrackChest(track);
        currency.setSeparateMajor(separate);
        currency.setMajorSeparator(separator);
        currency.addTier("Major", majorTier);
        currency.addTier("Minor", minorTier);

        //Interest-related configurations
        currency.setInterestEnabled(interestEnabled);
        currency.setInterestRate(interestRate);
        currency.setInterestInterval(interestInterval);

        addCurrency(worldName, currency);
      }
    }
  }

  public void addCurrency(String world, Currency currency) {
    MISCUtils.debug("[Add]Loading Currency: " + currency.getName() + " for world: " + world);
    if(world.equalsIgnoreCase(TNE.instance().defaultWorld)) {
      globalCurrencies.put(world, currency);
    } else {
      if(TNE.instance().getWorldManager(world) != null) {
        TNE.instance().getWorldManager(world).addCurrency(currency);
      }
    }
  }

  public void disableAll(String currency) {
    globalDisabled.add(currency);
    for(WorldManager manager : TNE.instance().getWorldManagers()) {
      TNE.instance().getWorldManager(manager.getWorld()).disable(currency, true);
    }
  }

  public void initializeWorld(String world) {
    loadCurrency(TNE.instance().worldConfigurations, true, world);
    for(Currency currency : globalCurrencies.values()) {
      if(!globalDisabled.contains(currency.getName())) {
        TNE.instance().getWorldManager(world).addCurrency(currency);
      }
    }
  }

  public Currency get(String world) {
    for(Currency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      if(currency.isWorldDefault()) return currency;
    }
    return TNE.instance().getWorldManager(TNE.instance().defaultWorld).getCurrency("Default");
  }

  public Currency get(String world, String name) {
    if(TNE.instance().getWorldManager(world).containsCurrency(name)) {
      return TNE.instance().getWorldManager(world).getCurrency(name);
    }
    return get(world);
  }

  public BigDecimal convert(Currency from, Currency to, BigDecimal amount) {
    double fromRate = from.getRate();
    double toRate = to.getRate();

    return convert(fromRate, toRate, amount);
  }

  public BigDecimal convert(Currency from, double toRate, BigDecimal amount) {
    return convert(from.getRate(), toRate, amount);
  }

  public BigDecimal convert(double fromRate, double toRate, BigDecimal amount) {
    double rate = fromRate - toRate;
    BigDecimal difference = amount.multiply(new BigDecimal(rate));

    return amount.add(difference);
  }

  public boolean contains(String world) {
    return TNE.instance().getWorldManager(world) != null;
  }

  public Collection<Currency> getWorldCurrencies(String world) {
    return TNE.instance().getWorldManager(world).getCurrencies();
  }

  public List<Currency> getTrackedCurrencies(String world) {
    if(trackedCurrencies.containsKey(world)) return trackedCurrencies.get(world);

    List<Currency> values = new ArrayList<>();

    for(String s : globalCurrencies.keySet()) {
      if(s.contains(world + ":")) {

        Currency currency = globalCurrencies.get(s);
        if(currency.isItem() && currency.canTrackChest()) {
          values.add(currency);
        }
      }
    }

    if(values.size() == 0) {
      Currency defaultCur = get(TNE.instance().defaultWorld);
      if(defaultCur.isItem() && defaultCur.canTrackChest()) {
        values.add(defaultCur);
      }
    }

    trackedCurrencies.put(world, values);
    return values;
  }

  public boolean contains(String world, String name) {
    MISCUtils.debug("CurrencyManager.contains(" + world + ", " + name + ")");
    return TNE.instance().getWorldManager(world).containsCurrency(name);
  }
}