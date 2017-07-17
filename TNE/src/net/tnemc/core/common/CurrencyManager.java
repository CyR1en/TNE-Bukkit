package net.tnemc.core.common;

import net.tnemc.core.TNE;
import net.tnemc.core.common.currency.Currency;
import net.tnemc.core.common.currency.ItemTier;
import net.tnemc.core.common.currency.Tier;
import net.tnemc.core.event.currency.TNECurrencyLoadEvent;
import net.tnemc.core.event.currency.TNECurrencyTierLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

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
      TNE.debug(currencies.toArray().toString());

      for(String cur : currencies) {
        if (configuration.contains("Core.Currency." + cur + ".Disabled") &&
            configuration.getBoolean("Core.Currency." + cur + ".Disabled")) {
              return;
        }

        TNE.debug("[Loop]Loading Currency: " + cur + " for world: " + worldName);
        String base = curBase + "." + cur;
        String single = configuration.getString(base + ".Name.Major.Single", "Dollar");
        String plural = configuration.getString(base + ".Name.Major.Plural", "Dollars");
        String singleMinor = configuration.getString(base + ".Name.Minor.Single", "Cent");
        String pluralMinor = configuration.getString(base + ".Name.Minor.Plural", "Cents");
        BigDecimal balance = new BigDecimal(configuration.getString(base + ".Balance", "200.00"));
        String decimal = configuration.getString(base + ".Decimal", ".");
        Integer decimalPlaces = ((configuration.getInt(base + ".DecimalPlace", 2) > 4)? 4 : configuration.getInt(base + ".DecimalPlace", 2));
        BigDecimal maxBalance = ((new BigDecimal(configuration.getString(base + ".MaxBalance", largestSupported.toPlainString())).compareTo(largestSupported) > 0)? largestSupported : new BigDecimal(configuration.getString(base + ".MaxBalance", largestSupported.toPlainString())));
        String format = configuration.getString(base + ".Format", "<symbol><major.amount><decimal><minor.amount>").trim();
        String prefixes = configuration.getString(base + ".Prefixes", "kMGTPEZYXWV").trim();
        Boolean worldDefault = configuration.getBoolean(base + ".Default", true);
        Double rate = configuration.getDouble(base + ".Conversion", 1.0);
        Boolean item = configuration.getBoolean(base + ".ItemCurrency");
        Boolean vault = configuration.getBoolean(base + ".Vault", true);
        Boolean notable = configuration.getBoolean(base + ".Notable", false);
        Boolean bankChest = configuration.getBoolean(base + ".BankChest", true);
        Boolean ender = configuration.getBoolean(base + ".EnderChest", true);
        Boolean track = configuration.getBoolean(base + ".TrackChest", false);
        Boolean separate = configuration.getBoolean(base + ".Major.Separate", true);
        String separator = configuration.getString(base + ".Major.Separator", ",");
        String symbol = configuration.getString(base + ".Symbol", "$");
        Integer minorWeight = configuration.getInt(base + ".Minor.Weight", 100);

        //Interest-related configurations
        Boolean interestEnabled = configuration.getBoolean(base + ".Interest.Enabled", false);
        Double interestRate = configuration.getDouble(base + ".Interest.Rate", 0.2);
        Long interestInterval = configuration.getLong(base + ".Interest.Interval", 1800) * 1000;

        Currency currency = new Currency();
        currency.setMaxBalance(maxBalance);
        currency.setBalance(balance);
        currency.setDecimal(decimal);
        currency.setDecimalPlaces(decimalPlaces);
        currency.setFormat(format);
        currency.setPrefixes(prefixes);
        currency.setSingle(single);
        currency.setPlural(plural);
        currency.setSingleMinor(singleMinor);
        currency.setPluralMinor(pluralMinor);
        currency.setSymbol(symbol);
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
        currency.setMinorWeight(minorWeight);

        //Interest-related configurations
        currency.setInterestEnabled(interestEnabled);
        currency.setInterestRate(interestRate);
        currency.setInterestInterval(interestInterval);

        loadTiers(worldName, currency, configuration, base + ".Tiers");

        TNECurrencyLoadEvent event = new TNECurrencyLoadEvent(worldName, currency.getSingle());
        Bukkit.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
          addCurrency(worldName, currency);
        }
      }
    }
  }

  private void loadTiers(String world, Currency currency, FileConfiguration configuration, String baseNode) {
    Set<String> tiers = configuration.getConfigurationSection(baseNode).getKeys(false);
    for(String tierName : tiers) {
      String tierBase = baseNode + "." + tierName;

      //Normal tier variables
      String single = configuration.getString(tierBase + ".Name.Single", "Dollar");
      String plural = configuration.getString(tierBase + ".Name.Plural", "Dollars");
      String type = configuration.getString(tierBase + ".Type", "Major");
      Integer weight = configuration.getInt(tierBase + ".Weight", 1);

      //ItemTier variables
      String material = configuration.getString(tierBase + ".Item.Material", "PAPER");
      short damage = (short)configuration.getInt(tierBase + ".Item.Damage", 0);
      String customName = configuration.getString(tierBase + ".Item.Name", null);
      String lore = configuration.getString(tierBase + ".Item.Lore", null);

      ItemTier item = new ItemTier(material, damage);
      item.setName(customName);
      item.setLore(lore);

      Set<String> enchants = configuration.getConfigurationSection(tierBase + ".Item.Enchantments").getKeys(false);
      for(String enchant : enchants) {
        Enchantment parsed = Enchantment.getByName(enchant);
        if(parsed != null) {
          item.addEnchantment(parsed.getName(), configuration.getString(tierBase + ".Item.Enchantments." + enchant, "*"));
        }
      }

      Tier tier = new Tier();
      tier.setItemInfo(item);
      tier.setSingle(single);
      tier.setPlural(plural);
      tier.setWeight(weight);

      TNECurrencyTierLoadedEvent event = new TNECurrencyTierLoadedEvent(world, currency.getSingle(), tier.getSingle(), type);
      Bukkit.getServer().getPluginManager().callEvent(event);

      if(!event.isCancelled()) {
        if (type.equalsIgnoreCase("minor")) {
          currency.addMinorTier(tier);
          continue;
        }
        currency.addMajorTier(tier);
      }
    }
  }

  public void addCurrency(String world, Currency currency) {
    TNE.debug("[Add]Loading Currency: " + currency.getSingle() + " for world: " + world);
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
    globalCurrencies.forEach((key, value)->{
      if(!globalDisabled.contains(value.getSingle())) {
        TNE.instance().getWorldManager(world).addCurrency(value);
      }
    });
  }

  public Currency get(String world) {
    for(Currency currency : TNE.instance().getWorldManager(world).getCurrencies()) {
      if(currency.isWorldDefault()) return currency;
    }
    return null;
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

    globalCurrencies.keySet().forEach(str->{
      if(str.contains(world + ":")) {

        Currency currency = globalCurrencies.get(str);
        if(currency.isItem() && currency.canTrackChest()) {
          values.add(currency);
        }
      }
    });

    if(values.size() == 0) {
      Currency defaultCur = get(TNE.instance().defaultWorld);
      if(defaultCur.isItem() && defaultCur.canTrackChest()) {
        values.add(defaultCur);
      }
    }

    trackedCurrencies.put(world, values);
    return values;
  }

  public void rename(String world, String currency, String newName) {
    //TODO: Data Handling.
  }

  public Optional<String> currencyFromItem(ItemStack stack) {
    //TODO: Item Currencies.
    return Optional.empty();
  }

  public boolean contains(String world, String name) {
    TNE.debug("CurrencyManager.contains(" + world + ", " + name + ")");
    return TNE.instance().getWorldManager(world).containsCurrency(name);
  }
}