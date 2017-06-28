package com.github.tnerevival.commands.note;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.Currency;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by creatorfromhell on 6/25/2017.
 * All rights reserved.
 **/
public class NoteCommand extends TNECommand {

  public NoteCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "note";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.note";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/note <amount> [currency]",
        "<amount> - Amount of money to note.",
        "[currency] - The currency balance you wish to use."
    };
  }


  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(super.execute(sender, command, arguments)) {
      if(MISCUtils.ecoDisabled(IDFinder.findRealWorld(getPlayer(sender)))) {
        Message disabled = new Message("Messages.General.Disabled");
        disabled.translate(IDFinder.findRealWorld(getPlayer(sender)), sender);
        return false;
      }
      String world = IDFinder.findRealWorld(getPlayer(sender));
      String currencyName = (arguments.length >= 2)? arguments[1] : TNE.instance().manager.currencyManager.get(world).getName();
      Currency currency = getCurrency(world, currencyName);
    }
    Player player = getPlayer(sender);
    if(!Vault.enabled(IDFinder.findRealWorld(getPlayer(sender)), IDFinder.getID(player).toString())) {
      new Message("Messages.Vault.Disabled").translate(IDFinder.findRealWorld(player), player);
      return false;
    }
    help(sender);
    return false;
  }
}