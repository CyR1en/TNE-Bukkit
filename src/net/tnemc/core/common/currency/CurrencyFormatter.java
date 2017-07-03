package net.tnemc.core.common.currency;

import com.github.tnerevival.core.Message;
import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CurrencyFormatter {

  /**
   * Formats an amount into a readable format, based on various parameters, and configurations.
   * @param world The world to use for configuration purposes.
   * @param amount The amount to format
   * @return The readable formatted amount.
   */
  public static String format(String world, BigDecimal amount) {
    MISCUtils.debug("CurrencyFormatter.format(" + world + ", " + amount.toPlainString() + ")");
    return format(TNE.instance().manager.getCurrencyManager().get(world), world, amount);
  }

  public static String format(String world, String name, BigDecimal amount) {
    MISCUtils.debug("CurrencyFormatter.format(" + name + ", " + world + ", " + amount.toPlainString() + ")");
    return format(TNE.instance().manager.getCurrencyManager().get(world, name), world, amount);
  }

  public static String format(Currency currency, String world, BigDecimal amount) {
    MISCUtils.debug("CurrencyFormatter.format(" + currency.getName() + ", " + world + ", " + amount.toPlainString() + ")");

    if(currency == null) currency = TNE.instance().manager.getCurrencyManager().get(TNE.instance().defaultWorld);

    amount = round(currency.getName(), world, amount);
    MISCUtils.debug(currency.getName() + " World: " + currency);

    String shortFormat = "<symbol><short.amount>";
    String format = currency.getFormat();

    String[] amountStr = (String.valueOf(amount) + (String.valueOf(amount).contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    BigInteger minor = new BigInteger(String.format("%1$-2s", Integer.valueOf(amountStr[1])).replace(' ', '0'));
    String majorName = (major.compareTo(BigInteger.ONE) == 0)? currency.getTier("Major").getSingle() : currency.getTier("Major").getPlural();
    String minorName = (minor.compareTo(BigInteger.ONE) == 0)? currency.getTier("Minor").getSingle() : currency.getTier("Minor").getPlural();
    String majorString = (currency.canSeparateMajor())? insert(major.toString(), currency.getMajorSeparator(), 3) : major.toString();

    Map<String, String> replacements = new HashMap<>();
    replacements.put("<symbol>", currency.getTier("Major").getSymbol());
    replacements.put("<decimal>", currency.getDecimal());
    replacements.put("<major>", majorString + " " + majorName);
    replacements.put("<minor>", minor + " " + minorName);
    replacements.put("<major.name>", majorName);
    replacements.put("<minor.name>", minorName);
    replacements.put("<major.amount>", majorString + "");
    replacements.put("<minor.amount>", minor + "");
    replacements.put("<short.amount>", shorten(currency, amount));
    replacements.putAll(Message.colours);

    String formatted = (currency.shorten())? shortFormat : format;

    for(Map.Entry<String, String> entry : replacements.entrySet()) {
      formatted = formatted.replace(entry.getKey(), entry.getValue());
    }
    return formatted;
  }

  private static String insert(String text, String insert, int period) {
    Pattern p = Pattern.compile("(.{" + period + "})", Pattern.DOTALL);
    text = new StringBuffer(text).reverse().toString();
    Matcher m = p.matcher(text);
    String result = m.replaceAll("$1" + insert);

    if(result.endsWith(insert)) {
      return new StringBuffer(result.substring(0, result.lastIndexOf(insert))).reverse().toString();
    }
    return new StringBuffer(result).reverse().toString();
  }

  public static String parseAmount(Currency currency, String world, String amount) {
    if(amount.length() > 40) return "Messages.Money.ExceedsCurrencyMaximum";
    if(isBigDecimal(amount, currency.getName(), world)) {
      BigDecimal translated = translateBigDecimal(amount, currency.getName(), world);
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

  public static BigDecimal round(String world, String currency, BigDecimal amount) {
    if(TNE.instance().manager.getCurrencyManager().contains(world, currency)) {
      return amount.setScale(TNE.instance().manager.getCurrencyManager().get(world, currency).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
    }

    if(TNE.instance().manager.getCurrencyManager().contains(world)) {
      return amount.setScale(TNE.instance().manager.getCurrencyManager().get(world).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
    }
    return amount.setScale(TNE.instance().manager.getCurrencyManager().get(TNE.instance().defaultWorld).getDecimalPlaces(), BigDecimal.ROUND_CEILING);
  }

  private static BigDecimal parseWeight(Currency currency, BigDecimal decimal) {
    String[] amountStr = (String.valueOf(decimal) + (decimal.toPlainString().contains(".")? "" : ".00")).split("\\.");
    BigInteger major = new BigInteger(amountStr[0]);
    BigInteger minor = new BigInteger(String.format("%1$-2s", Integer.valueOf(amountStr[1])).replace(' ', '0'));
    BigInteger majorConversion = minor;
    majorConversion = majorConversion.divide(new BigInteger(currency.getTier("Minor").getWeight() + ""));
    major = major.add(majorConversion);
    minor = minor.mod(new BigInteger(currency.getTier("Minor").getWeight() + ""));

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
    String major = TNE.instance().manager.getCurrencyManager().get(world).getMajor();
    return isBigDecimal(value, major, world);
  }

  private static boolean isBigDecimal(String value, String currency, String world) {
    String decimal = TNE.instance().manager.getCurrencyManager().get(world, currency).getDecimal();
    try {
      new BigDecimal(value.replace(decimal, "."));
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static BigDecimal translateBigDecimal(String value, String world) {
    String major = TNE.instance().manager.getCurrencyManager().get(world).getMajor();
    return translateBigDecimal(value, major, world);
  }

  public static BigDecimal translateBigDecimal(String value, String currency, String world) {
    String decimal = TNE.instance().manager.getCurrencyManager().get(world, currency).getDecimal();
    return new BigDecimal(value.replace(decimal, "."));
  }
}