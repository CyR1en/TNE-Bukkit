package net.tnemc.core.common.currency;

import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

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
 * Created by Daniel on 7/16/2017.
 */
public class CurrencyFormatter {

  public static String format(String world, BigDecimal amount) {
    TNE.debug("CurrencyFormatter.format(" + world + ", " + amount.doubleValue() + ")");
    return format(TNE.manager().currencyManager().get(world), world, amount);
  }

  public static String format(String world, String name, BigDecimal amount) {
    TNE.debug("CurrencyFormatter.format(" + name + ", " + world + ", " + amount.doubleValue() + ")");
    return format(TNE.manager().currencyManager().get(world, name), world, amount);
  }

  public static String format(Currency currency, String world, BigDecimal amount) {
    TNE.debug("CurrencyFormatter.format(" + currency.getSingle() + ", " + world + ", " + amount.toPlainString() + ")");

    if(currency == null) currency = TNE.manager().currencyManager().get(TNE.instance().defaultWorld);

    amount = round(world, currency.getSingle(), amount);
    TNE.debug(currency.getSingle() + " World: " + currency);

    String shortFormat = "<symbol><short.amount>";
    String format = currency.getFormat();

    String[] amountStr = (String.valueOf(amount) + (String.valueOf(amount).contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    BigInteger minor = new BigInteger(String.format("%1$-2s", Integer.valueOf(amountStr[1])).replace(' ', '0'));
    String majorName = (major.compareTo(BigInteger.ONE) == 0)? currency.getSingle() : currency.getPlural();
    String minorName = (minor.compareTo(BigInteger.ONE) == 0)? currency.getSingleMinor() : currency.getPluralMinor();

    Map<String, String> replacements = new HashMap<>();
    replacements.put("<symbol>", currency.getSymbol());
    replacements.put("<decimal>", currency.getDecimal());
    replacements.put("<major>", major + " " + majorName);
    replacements.put("<minor>", minor + " " + minorName);
    replacements.put("<major.name>", majorName);
    replacements.put("<minor.name>", minorName);
    replacements.put("<major.amount>", major + "");
    replacements.put("<minor.amount>", minor + "");
    replacements.put("<short.amount>", shorten(currency, amount));
    replacements.putAll(Message.colours);

    String formatted = (currency.shorten())? shortFormat : format;

    for(Map.Entry<String, String> entry : replacements.entrySet()) {
      formatted = formatted.replace(entry.getKey(), entry.getValue());
    }
    return formatted;
  }

  public static BigDecimal round(String world, String currency, BigDecimal amount) {
    if(TNE.manager().currencyManager().contains(world, currency)) {
      return amount.setScale(TNE.manager().currencyManager().get(world, currency).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
    }

    if(TNE.manager().currencyManager().contains(world)) {
      return amount.setScale(TNE.manager().currencyManager().get(world).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
    }
    return amount.setScale(TNE.manager().currencyManager().get(TNE.instance().defaultWorld).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
  }

  public static String parseAmount(Currency currency, String world, String amount) {
    if(amount.length() > 40) return "Messages.Money.ExceedsCurrencyMaximum";
    if(isBigDecimal(amount, currency.getSingle(), world)) {
      BigDecimal translated = translateBigDecimal(amount, currency.getSingle(), world);
      if(translated.compareTo(currency.getMaxBalance()) > 0) {
        return "Messages.Money.ExceedsCurrencyMaximum";
      }
      translated = parseWeight(currency, translated);
      return translated.toPlainString();
    }
    String updated = amount.replaceAll(" ", "");
    if(!currency.getPrefixes().contains(updated.charAt(updated.length() - 1) + "")) {
      return "Messages.Money.InvalidFormat";
    }
    return fromShort(currency, updated);
  }

  private static BigDecimal parseWeight(Currency currency, BigDecimal decimal) {
    String[] amountStr = (String.valueOf(decimal) + (String.valueOf(decimal).contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    BigInteger minor = new BigInteger(String.format("%1$-2s", Integer.valueOf(amountStr[1])).replace(' ', '0'));
    BigInteger majorConversion = minor;
    majorConversion = majorConversion.divide(new BigInteger(currency.getMinorWeight() + ""));
    major = major.add(majorConversion);
    minor = minor.mod(new BigInteger(currency.getMinorWeight() + ""));

    return new BigDecimal(major.toString() + currency.getDecimal() + minor.toString());
  }

  private static String shorten(Currency currency, BigDecimal balance) {
    String prefixes = currency.getPrefixes();
    BigInteger wholeNum = balance.toBigInteger();
    if (wholeNum.compareTo(new BigInteger("1000")) < 0) {
      return "" + wholeNum.toString();
    }
    String whole = wholeNum.toString();
    int pos = ((whole.length() - 1) / 3) - 1;
    int posInclude = ((whole.length() % 3) == 0)? 3 : whole.length() % 3;
    return whole.substring(0, posInclude) + prefixes.charAt(pos);
  }
  private static String fromShort(Currency currency, String amount) {
    int charIndex = currency.getPrefixes().indexOf(amount.charAt(amount.length() - 1)) + 1;
    String sub = amount.substring(0, amount.length() - 1);
    String form = "%1$-" + ((charIndex * 3) + sub.length()) + "s";
    return String.format(form, Integer.valueOf(sub)).replace(' ', '0');
  }

  public static boolean isBigDecimal(String value, String world) {
    String major = TNE.manager().currencyManager().get(world).getSingle();
    return isBigDecimal(value, major, world);
  }

  private static boolean isBigDecimal(String value, String currency, String world) {
    String decimal = TNE.manager().currencyManager().get(world, currency).getDecimal();
    try {
      new BigDecimal(value.replace(decimal, "."));
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static BigDecimal translateBigDecimal(String value, String world) {
    String major = TNE.manager().currencyManager().get(world).getSingle();
    return translateBigDecimal(value, major, world);
  }

  public static BigDecimal translateBigDecimal(String value, String currency, String world) {
    String decimal = TNE.manager().currencyManager().get(world, currency).getDecimal();
    return new BigDecimal(value.replace(decimal, "."));
  }
}