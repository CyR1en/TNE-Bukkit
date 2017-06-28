package com.github.tnerevival.commands.market;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/12/2017.
 * All rights reserved.
 **/
public class MarketCommand extends TNECommand {

  public MarketCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "market";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.market.command";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public Boolean activated(String world, String player) {
    return TNE.instance().api().getBoolean("Core.Markets.Enabled", world, player);
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(MISCUtils.ecoDisabled(IDFinder.findRealWorld(getPlayer(sender)))) {
      Message disabled = new Message("Messages.General.Disabled");
      disabled.translate(IDFinder.findRealWorld(getPlayer(sender)), sender);
      return false;
    }
    return super.execute(sender, command, arguments);
  }
}