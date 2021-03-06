package net.tnemc.core.common.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MaterialUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.math.BigInteger;
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
 * Created by Daniel on 12/16/2017.
 */
public class ItemCalculations {

  public static void clearItems(TNEAccount account, TNECurrency currency) {
    TNE.debug("===== START Account.clearItems =====");
    Player player = account.getPlayer();

    TNE.debug("UUID: " + account.identifier().toString());
    if(player == null) TNE.debug("Player is null");

    for(TNETier tier : currency.getTNEMajorTiers().values()) {
      MaterialUtils.removeItem(player, tier.getItemInfo());
    }

    for(TNETier tier : currency.getTNEMinorTiers().values()) {
      MaterialUtils.removeItem(player, tier.getItemInfo());
    }
    TNE.debug("===== END Account.clearItems =====");
  }

  public static void setItems(TNEAccount account, TNECurrency currency, BigDecimal amount) {
    TNE.debug("=====START Account.setItems =====");
    TNE.debug("Holdings: " + amount.toPlainString());
    if(currency.isItem()) {
      BigDecimal old = getCurrencyItems(account, currency);
      BigDecimal difference = (amount.compareTo(old) >= 0)? amount.subtract(old) : old.subtract(amount);
      String differenceString = difference.toPlainString();
      String[] split = (differenceString + (differenceString.contains(".")? "" : ".00")).split("\\.");
      boolean consolidate = TNE.instance().api().getBoolean("Core.Server.Consolidate", WorldFinder.getWorld(account.identifier()), account.identifier());
      boolean add = (consolidate) || amount.compareTo(old) >= 0;

      if(consolidate) split = (amount.toPlainString() + (amount.toPlainString().contains(".")? "" : ".00")).split("\\.");

      if(consolidate) clearItems(account, currency);
      BigInteger majorChange = (consolidate)? setMajorConsolidate(account, currency, new BigInteger(split[0])) :
                                              setMajor(account, currency, new BigInteger(split[0]), add);
      BigInteger minorChange = (consolidate)? setMinorConsolidate(account, currency, new BigInteger(split[1])) :
                                              setMinor(account, currency, new BigInteger(split[1]), add);

      TNE.debug("MajorChange: " + majorChange.toString());
      TNE.debug("MinorChange: " + minorChange.toString());
      if(!consolidate && !add) {
        if(majorChange.compareTo(new BigInteger("0")) > 0) {
          setMajor(account, currency, majorChange, true);
        }

        if(minorChange.compareTo(new BigInteger("0")) > 0) {
          setMajor(account, currency, minorChange, true);
        }
      }
    }
    TNE.debug("=====END Account.setItems =====");
  }

  public static BigInteger setMajorConsolidate(TNEAccount account, TNECurrency currency, BigInteger amount) {
    TNE.debug("===== START setMinorItems =====");
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    for(Map.Entry<Integer, TNETier> entry : currency.getTNEMajorTiers().entrySet()) {
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      BigInteger itemAmount = workingAmount.divide(weight);

      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());
      items.put(entry.getKey(), stack);
    }
    giveItems(account, items.values());
    return new BigInteger("0");
  }

  public static BigInteger setMinorConsolidate(TNEAccount account, TNECurrency currency, BigInteger amount) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    for(Map.Entry<Integer, TNETier> entry : currency.getTNEMinorTiers().entrySet()) {
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      BigInteger itemAmount = workingAmount.divide(weight);

      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());
      items.put(entry.getKey(), stack);
    }
    giveItems(account, items.values());
    return new BigInteger("0");
  }

  public static BigInteger setMajor(TNEAccount account, TNECurrency currency, BigInteger amount, boolean add) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    BigInteger actualAmount = new BigInteger("0");
    NavigableMap<Integer, TNETier> values = (add)? currency.getTNEMajorTiers() :
                                                currency.getTNEMajorTiers().descendingMap();
    String additional = "0";
    for(Map.Entry<Integer, TNETier> entry : values.entrySet()) {
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      TNE.debug("Weight: " + weight.toString());
      TNE.debug("Addition: " + additional);

      BigInteger itemAmount = workingAmount.divide(weight).add(new BigInteger(additional));
      BigInteger itemActual = new BigInteger(MaterialUtils.getCount(account.getPlayer(), entry.getValue().getItemInfo()) + "");
      additional = "0";
      TNE.debug("ItemAmount: " + itemAmount.toString());
      TNE.debug("itemActual: " + itemActual.toString());

      if(!add && itemActual.compareTo(itemAmount) < 0) {
        additional = itemAmount.subtract(itemActual).toString();
        itemAmount = itemActual;
      }

      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());


      actualAmount = actualAmount.add(weight.multiply(itemAmount));
      workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
      items.put(entry.getKey(), stack);
    }
    if(add) giveItems(account, items.values());
    else takeItems(account, items.values());

    if(actualAmount.compareTo(amount) > 0) {
      TNE.debug("return actual sub: " + actualAmount.subtract(amount).toString());
      return actualAmount.subtract(amount);
    }
    return new BigInteger("0");
  }

  public static BigInteger setMinor(TNEAccount account, TNECurrency currency, BigInteger amount, boolean add) {
    Map<Integer, ItemStack> items = new HashMap<>();
    BigInteger workingAmount = new BigInteger(amount.toString());
    BigInteger actualAmount = new BigInteger("0");
    Set<Map.Entry<Integer, TNETier>> values = (add)? currency.getTNEMinorTiers().entrySet() :
                                                     currency.getTNEMinorTiers().descendingMap().entrySet();
    for(Map.Entry<Integer, TNETier> entry : values) {
      BigInteger weight = BigInteger.valueOf(entry.getKey());

      BigInteger itemAmount = workingAmount.divide(weight);

      ItemStack stack = entry.getValue().getItemInfo().toStack();
      stack.setAmount(itemAmount.intValue());

      if(add || hasItem(account, stack)) {
        actualAmount = actualAmount.add(weight.multiply(itemAmount));
        workingAmount = workingAmount.subtract(weight.multiply(itemAmount));
        items.put(entry.getKey(), stack);
      }
    }
    if(add) giveItems(account, items.values());
    else takeItems(account, items.values());

    if(actualAmount.compareTo(amount) > 0) {
      return actualAmount.subtract(amount);
    }
    return new BigInteger("0");
  }

  public static BigDecimal getCurrencyItems(TNEAccount account, TNECurrency currency) {
    TNE.debug("=====START Account.getCurrencyItems =====");
    BigDecimal value = new BigDecimal(0.0);
    if(currency.isItem()) {
      Player player = account.getPlayer();
      for(TNETier tier : currency.getTNEMajorTiers().values()) {
        value = value.add(new BigDecimal(MaterialUtils.getCount(player, tier.getItemInfo()) * tier.weight()));
      }

      for(TNETier tier : currency.getTNEMinorTiers().values()) {
        Integer parsed = MaterialUtils.getCount(player, tier.getItemInfo()) * tier.weight();
        String convert = "." + String.format(Locale.US, "%0" + currency.decimalPlaces() + "d", parsed);
        value = value.add(new BigDecimal(convert));
      }
    }
    TNE.debug("=====END Account.getCurrencyItems =====");
    return value;
  }

  public static void recalculateItemHoldings(TNEAccount account, String world) {
    TNE.debug("=====START Account.recalculateItemHoldings =====");
    for(TNECurrency currency : TNE.manager().currencyManager().getWorldCurrencies(world)) {
      if(currency.isItem()) {
        recalculateItemHoldings(account, world, currency);
      }
    }
    TNE.debug("=====END Account.recalculateItemHoldings =====");
  }

  public static void recalculateItemHoldings(TNEAccount account, String world, TNECurrency currency) {
    TNE.debug("=====START Account.recalculateCurrencyHoldings =====");
    boolean consolidate = TNE.instance().api().getBoolean("Core.Server.Consolidate", world, account.identifier());
    BigDecimal items = getCurrencyItems(account, currency);
    if(consolidate) clearItems(account, currency);
    account.setHoldings(world, currency.name(), items);
    TNE.manager().addAccount(account);
    TNE.debug("=====END Account.recalculateCurrencyHoldings =====");
  }

  public static boolean hasItem(TNEAccount account, ItemStack stack) {
    Player player = account.getPlayer();
    return player.getInventory().contains(stack, stack.getAmount());
  }

  public static void takeItems(TNEAccount account, Collection<ItemStack> items) {
    TNE.debug("=====START Account.takeItems =====");
    Player player = account.getPlayer();
    for(ItemStack stack : items) {
      player.getInventory().removeItem(stack);
    }
    TNE.debug("=====END Account.takeItems =====");
  }

  public static void giveItems(TNEAccount account, Collection<ItemStack> items) {
    TNE.debug("=====START Account.giveItems =====");
    Player player = account.getPlayer();
    List<ItemStack> leftOver = new ArrayList<>();
    for(ItemStack stack : items) {
      leftOver.addAll(player.getInventory().addItem(stack).values());
    }

    for(ItemStack stack : leftOver) {
      if(stack != null) {
        player.getWorld().dropItem(player.getLocation(), stack);
      }
    }
    TNE.debug("=====END Account.giveItems =====");
  }
}