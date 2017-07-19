package net.tnemc.core.common.configurations;

import com.github.tnerevival.core.configurations.Configuration;
import net.tnemc.core.TNE;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
public class MessageConfigurations extends Configuration {
  @Override
  public FileConfiguration getConfiguration() {
    return TNE.instance().messageConfigurations;
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Messages");
    return nodes;
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Messages.General.NoPerm", "<red>I'm sorry, but you do not have permission to do that.");
    configurations.put("Messages.General.NoPlayer", "<red>Unable to locate player \"$player\"!");
    configurations.put("Messages.General.Saved", "<yellow>Successfully saved all TNE Data!");
    configurations.put("Messages.General.Disabled", "<red>Economy features are currently disabled in this world!");

    configurations.put("Messages.Admin.NoBalance", "<red>$player has no balance data for the world \"$world\"!");
    configurations.put("Messages.Admin.Balance", "<white>$player currently has <gold>$amount <white>for world \"$world\"!");
    configurations.put("Messages.Admin.NoTransactions", "<white>$player has no transactions to display.");
    configurations.put("Messages.Admin.Configuration", "<white>The value of $node is currently $value.");
    configurations.put("Messages.Admin.SetConfiguration", "<white>The value of $node has been set to $value.");
    configurations.put("Messages.Admin.ID", "<white>The UUID for $player is $id.");
    configurations.put("Messages.Admin.Exists", "<red>A player with that name already exists.");
    configurations.put("Messages.Admin.Created", "<white>Successfully created account for $player.");
    configurations.put("Messages.Admin.Deleted", "<white>Successfully deleted account for $player.");
    configurations.put("Messages.Admin.Purge", "<white>Successfully purged all economy accounts.");
    configurations.put("Messages.Admin.PurgeWorld", "<white>Successfully purged economy accounts in $world.");
    configurations.put("Messages.Admin.Status", "<white>Status for $player has been changed to <green>$status<white>.");
    configurations.put("Messages.Admin.Reset", "<white>Performed an economy reset using these parameters -  world = $world, currency = $currency, and player = $player.");

    configurations.put("Messages.Account.Locked", "<red>You can't do that with a locked account($player)!");
    configurations.put("Messages.Account.Set", "<yellow>You must use /pin set before accessing your money and/or bank.");
    configurations.put("Messages.Account.Confirm", "<yellow>You must use /pin confirm before accessing your money and/or bank.");
    configurations.put("Messages.Account.NoTransactions", "<white>You have no transactions to display at this time.");
    configurations.put("Messages.Account.Reset", "<white>Your pin has been reset to <green>$pin<white>.");
    configurations.put("Messages.Account.StatusChange", "<white>Your account's status has been changed to <green>$status<white>.");

    configurations.put("Messages.Money.Given", "<white>You were given <gold>$amount<white>.");
    configurations.put("Messages.Money.Received", "<white>You were paid <gold>$amount <white> by <white> $from.");
    configurations.put("Messages.Money.Taken", "<white>$from took <gold>$amount<white> from you.");
    configurations.put("Messages.Money.Insufficient", "<red>I'm sorry, but you do not have <gold>$amount<red>.");
    configurations.put("Messages.Money.Balance", "<white>You currently have <gold>$amount<white> on you.");
    configurations.put("Messages.Money.Gave", "<white>Successfully gave $player <gold>$amount<white>.");
    configurations.put("Messages.Money.Set", "<white>Successfully set $player\'s balance to <gold>$amount<white>.");
    configurations.put("Messages.Money.Paid", "<white>Successfully paid $player <gold>$amount<white>.");
    configurations.put("Messages.Money.Took", "<white>Successfully took <gold>$amount<white> from $player.");
    configurations.put("Messages.Money.Negative", "<red>Amount cannot be a negative value!");
    configurations.put("Messages.Money.SelfPay", "<red>You can't pay yourself!");
    configurations.put("Messages.Money.NoPins", "<red>Pins are disabled in this world!");
    configurations.put("Messages.Money.NoCurrency", "<red>The currency \"$currency\" could not be found in \"$world\".");
    configurations.put("Messages.Money.Converted", "<white>Successfully exchanged \"<gold>$from_amount<white>\" to \"<gold>$amount<white>\".");
    configurations.put("Messages.Money.Top", "<white>=========[<gold>Economy Top<white>]========= Page: $page/$page_top");
    configurations.put("Messages.Money.InvalidFormat", "<red>I'm sorry, but the monetary value you've entered is wrong.");
    configurations.put("Messages.Money.ExceedsCurrencyMaximum", "<red>I'm sorry, but the monetary value you've entered exceeds the maximum possible balance.");
    configurations.put("Messages.Money.ExceedsPlayerMaximum", "<red>I'm sorry, but performing this transaction will place your balance over the maximum allowed.");
    configurations.put("Messages.Money.ExceedsOtherPlayerMaximum", "<red>I'm sorry, but performing this transaction will place $player's balance over the maximum allowed.");

    configurations.put("Messages.World.Change", "<white>You have been charged <gold> $amount<white> for changing worlds.");
    configurations.put("Messages.World.ChangeFailed", "<red>I'm sorry, but you need at least <gold>$amount<red> to change worlds.");

    String base = "Messages.Mob.Custom";
    Set<String> keys = configurationFile.getConfigurationSection(base).getKeys(false);
    for(String s : keys) {
      configurations.put(base + "." + s, configurationFile.getString(base + "." + s));
    }

    super.load(configurationFile);
  }
}