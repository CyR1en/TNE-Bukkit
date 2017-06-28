package com.github.tnerevival.commands.vault;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VaultBuyCommand extends TNECommand {

  public VaultBuyCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "buy";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.vault.buy";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    Account account = AccountUtils.getAccount(IDFinder.getID(player));


    if(account.hasVault(IDFinder.findRealWorld(getPlayer(sender)))) {
      new Message("Messages.Vault.Already").translate(IDFinder.findRealWorld(player), player);
      return false;
    }


    MISCUtils.debug("Has bypass? " + player.hasPermission("tne.vault.bypass"));
    if(!player.hasPermission("tne.vault.bypass")) {
      if(AccountUtils.transaction(IDFinder.getID(player).toString(), null, Vault.cost(IDFinder.findRealWorld(player), IDFinder.getID(player).toString()), TransactionType.MONEY_INQUIRY, IDFinder.findRealWorld(player))) {
        AccountUtils.transaction(IDFinder.getID(player).toString(), null, Vault.cost(IDFinder.findRealWorld(player), IDFinder.getID(player).toString()), TransactionType.MONEY_REMOVE, IDFinder.findRealWorld(player));
      } else {
        Message insufficient = new Message("Messages.Money.Insufficient");
        insufficient.addVariable("$amount",  CurrencyFormatter.format(IDFinder.findRealWorld(player), Vault.cost(IDFinder.findRealWorld(getPlayer(sender)), IDFinder.getID(player).toString())));
        insufficient.translate(IDFinder.findRealWorld(player), player);
        return false;
      }
    }
    MISCUtils.debug(IDFinder.getID(player).toString());
    Vault vault = new Vault(IDFinder.getID(player), IDFinder.findRealWorld(getPlayer(sender)), Vault.size(IDFinder.findRealWorld(player), IDFinder.getID(player).toString()));
    account.getVaults().put(IDFinder.findRealWorld(getPlayer(sender)), vault);
    TNE.instance().manager.accounts.put(account.getUid(), account);
    new Message("Messages.Vault.Bought").translate(IDFinder.findRealWorld(player), player);
    return true;
  }

  @Override
  public String getHelp() {
    return "/vault buy - Used to purchase a vault.";
  }

}