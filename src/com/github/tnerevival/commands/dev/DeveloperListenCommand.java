package com.github.tnerevival.commands.dev;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by creatorfromhell on 6/17/2017.
 * All rights reserved.
 **/
public class DeveloperListenCommand extends TNECommand {

  public DeveloperListenCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "listen";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean developer() {
    return true;
  };

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    sender.sendMessage(ChatColor.BLUE + "Thank you for choosing the TNE Developer Commands.");
    String listening = (TNE.instance().manager.debuggers.contains(sender))? "You are no longer listening to this servers debug values." : "You are now listening to this server's debug values.";
    if(TNE.instance().manager.debuggers.contains(sender)) {
      TNE.instance().manager.debuggers.remove(sender);
    } else {
      TNE.instance().manager.debuggers.add(sender);
    }
    sender.sendMessage(ChatColor.BLUE + listening);
    return true;
  }

  @Override
  public String getHelp() {
    return "/tnedev listen - Helps me help you.";
  }
}