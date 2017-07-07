package net.tnemc.core.common.currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
 * Created by creatorfromhell on 10/21/2016.
 */
public class Currency {

  private Map<Integer, Tier> majorTiers = new TreeMap<>();

  private Map<Integer, Tier> minorTiers = new TreeMap<>();

  private boolean worldDefault = true;
  private BigDecimal balance;
  private BigDecimal maxBalance;
  private boolean item;
  private boolean vault;
  private boolean notable;
  private boolean bankChest;
  private boolean enderChest;
  private boolean trackChest;
  private boolean separateMajor;
  private String majorSeparator;
  private Integer minorWeight;
  private String single;
  private String plural;
  private String symbol;
  private String format;
  private String prefixes;
  private double rate;
  private String decimal;
  private int decimalPlaces;

  //Interest-related configurations
  private boolean interestEnabled = false;
  private double interestRate = 0.2;
  private long interestInterval = 1800;

  //Tier-related methods.
  public TreeMap<Integer, Tier> getMajorTiers() {
    return (TreeMap<Integer, Tier>)majorTiers;
  }

  public void setMajorTiers(TreeMap<Integer, Tier> tiers) {
    majorTiers = tiers;
  }

  public void addMajorTier(Tier tier) {
    majorTiers.put(tier.getWeight(), tier);
  }

  public Optional<Tier> getMajorTier(Integer weight) {
    return Optional.of(majorTiers.get(weight));
  }

  public Optional<Tier> getMajorTier(String name) {
    for(Tier tier : majorTiers.values()) {
      if(tier.getSingle().equalsIgnoreCase(name) || tier.getPlural().equalsIgnoreCase(name))
        return Optional.of(tier);
    }
    return Optional.empty();
  }

  public TreeMap<Integer, Tier> getMinorTiers() {
    return (TreeMap<Integer, Tier>)minorTiers;
  }

  public void setMinorTiers(TreeMap<Integer, Tier> tiers) {
    minorTiers = tiers;
  }

  public void addMinorTier(Tier tier) {
    minorTiers.put(tier.getWeight(), tier);
  }

  public Optional<Tier> getMinorTier(Integer weight) {
    return Optional.of(minorTiers.get(weight));
  }

  public Optional<Tier> getMinorTier(String name) {
    for(Tier tier : minorTiers.values()) {
      if(tier.getSingle().equalsIgnoreCase(name) || tier.getPlural().equalsIgnoreCase(name))
        return Optional.of(tier);
    }
    return Optional.empty();
  }

  public boolean shorten() {
    return format.contains("<shorten>");
  }

  public boolean isWorldDefault() {
    return worldDefault;
  }

  public void setWorldDefault(boolean worldDefault) {
    this.worldDefault = worldDefault;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getMaxBalance() {
    return maxBalance;
  }

  public void setMaxBalance(BigDecimal maxBalance) {
    this.maxBalance = maxBalance;
  }

  public boolean isItem() {
    return item;
  }

  public void setItem(boolean item) {
    this.item = item;
  }

  public boolean canVault() {
    return vault;
  }

  public void setVault(boolean vault) {
    this.vault = vault;
  }

  public boolean isNotable() {
    return notable;
  }

  public void setNotable(boolean notable) {
    this.notable = notable;
  }

  public boolean canBankChest() {
    return bankChest;
  }

  public void setBankChest(boolean bankChest) {
    this.bankChest = bankChest;
  }

  public boolean canEnderChest() {
    return enderChest;
  }

  public void setEnderChest(boolean enderChest) {
    this.enderChest = enderChest;
  }

  public boolean canTrackChest() {
    return trackChest;
  }

  public void setTrackChest(boolean trackChest) {
    this.trackChest = trackChest;
  }

  public boolean canSeparateMajor() {
    return separateMajor;
  }

  public void setSeparateMajor(boolean separateMajor) {
    this.separateMajor = separateMajor;
  }

  public String getMajorSeparator() {
    return majorSeparator;
  }

  public void setMajorSeparator(String majorSeparator) {
    this.majorSeparator = majorSeparator;
  }

  public Integer getMinorWeight() {
    return minorWeight;
  }

  public void setMinorWeight(Integer minorWeight) {
    this.minorWeight = minorWeight;
  }

  public String getSingle() {
    return single;
  }

  public void setSingle(String single) {
    this.single = single;
  }

  public String getPlural() {
    return plural;
  }

  public void setPlural(String plural) {
    this.plural = plural;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getPrefixes() {
    return prefixes;
  }

  public void setPrefixes(String prefixes) {
    this.prefixes = prefixes;
  }

  public double getRate() {
    return rate;
  }

  public void setRate(double rate) {
    this.rate = rate;
  }

  public String getDecimal() {
    return decimal;
  }

  public void setDecimal(String decimal) {
    this.decimal = decimal;
  }

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  public void setDecimalPlaces(int decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public boolean isInterestEnabled() {
    return interestEnabled;
  }

  public void setInterestEnabled(boolean interestEnabled) {
    this.interestEnabled = interestEnabled;
  }

  public double getInterestRate() {
    return interestRate;
  }

  public void setInterestRate(double interestRate) {
    this.interestRate = interestRate;
  }

  public long getInterestInterval() {
    return interestInterval;
  }

  public void setInterestInterval(long interestInterval) {
    this.interestInterval = interestInterval;
  }
}