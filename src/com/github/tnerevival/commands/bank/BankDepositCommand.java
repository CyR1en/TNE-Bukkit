package com.github.tnerevival.commands.bank;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.transaction.TransactionType;
import com.github.tnerevival.utils.AccountUtils;
import com.github.tnerevival.utils.BankUtils;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankDepositCommand extends TNECommand {
	
	public BankDepositCommand(TNE plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "deposit";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public String getNode() {
		return "tne.bank.deposit";
	}

	@Override
	public boolean console() {
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] arguments) {
    String ownerName = (arguments.length >= 2)? arguments[1] : sender.getName();
    Player owner = MISCUtils.getPlayer(ownerName);
    Player player = MISCUtils.getPlayer(sender.getName());

    if(arguments.length == 1) {
      if(BankUtils.hasBank(MISCUtils.getID(owner))) {
				Double value = Double.valueOf(arguments[0].replace(TNE.instance.api.getString("Core.Currency.Decimal", MISCUtils.getWorld(getPlayer(sender)), MISCUtils.getID(getPlayer(sender)).toString()), "."));
        if (BankUtils.bankMember(MISCUtils.getID(owner), MISCUtils.getID(sender.getName()))) {
          if(AccountUtils.transaction(MISCUtils.getID(player).toString(), MISCUtils.getID(owner).toString(), value, TransactionType.BANK_DEPOSIT, MISCUtils.getWorld(player))) {
            Message deposit = new Message("Messages.Bank.Deposit");
            deposit.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), value));
            deposit.addVariable("$name",  ownerName);
            deposit.translate(MISCUtils.getWorld(player), player);
            return true;
          } else {
            Message insufficient = new Message("Messages.Money.Insufficient");
            insufficient.addVariable("$amount",  MISCUtils.formatBalance(player.getWorld().getName(), value));
            insufficient.addVariable("$name",  ownerName);
            insufficient.translate(MISCUtils.getWorld(player), player);
            return false;
          }
        }
        Message noAccess = new Message("Messages.Bank.Invalid");
        noAccess.addVariable("$name", ownerName);
        noAccess.translate(MISCUtils.getWorld(player), player);
        return false;
      }
      new Message("Messages.Bank.None").translate(MISCUtils.getWorld(player), player);
      return false;
		}
		help(sender);
		return false;
	}

	@Override
	public String getHelp() {
		return "/bank deposit <amount> [owner] - Put <amount> into [owner]'s bank. Defaults to your personal bank.";
	}
	
}