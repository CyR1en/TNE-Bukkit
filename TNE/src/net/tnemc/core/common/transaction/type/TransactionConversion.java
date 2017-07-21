package net.tnemc.core.common.transaction.type;

import net.tnemc.core.common.account.Account;
import net.tnemc.core.common.transaction.TransactionResult;
import net.tnemc.core.common.transaction.TransactionType;
import net.tnemc.core.common.transaction.result.TransactionResultConversion;
import net.tnemc.core.common.transaction.result.TransactionResultInsufficient;

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
 * Created by Daniel on 7/12/2017.
 */
public class TransactionConversion extends TransactionType {
  private String worldTo;
  private String currencyTo;

  public TransactionConversion(String worldTo, String currencyTo) {
    this.worldTo = worldTo;
    this.currencyTo = currencyTo;
  }

  @Override
  public String getName() {
    return "Conversion";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public TransactionResult success() {
    return new TransactionResultConversion();
  }

  @Override
  public void handleInitiator() {
    if(initiatorOldBalance.compareTo(cost.getAmount()) == -1 || !Account.getAccount(initiator).hasItems(cost.getItems())) {
      result = new TransactionResultInsufficient();
      return;
    }
    initiatorBalance = initiatorOldBalance.subtract(cost.getAmount());
  }

  @Override
  public void handleRecipient() {
    recipientBalance = recipientOldBalance.add(cost.getAmount());
  }

  @Override
  public void initializeBalances() {
    initiatorOldBalance = Account.getAccount(initiator).getHoldings(world, cost.getCurrency().getSingle());
    recipientOldBalance = Account.getAccount(recipient).getHoldings(worldTo, currencyTo);
  }

  @Override
  public void setBalances() {
    if(initiator != null) {
      if(recipient == null) {
        Account initiatorAccount = Account.getAccount(initiator);
        if (initiatorAccount != null) {
          initiatorAccount.setHoldings(world, cost.getCurrency().getSingle(), initiatorBalance);
          if(cost.getItems().size() > 0) {
            initiatorAccount.giveItems(cost.getItems());
          }
        }
      }
    }

    if(recipient != null) {
      Account recipientAccount = Account.getAccount(recipient);
      if(recipientAccount != null) {
        recipientAccount.setHoldings(worldTo, currencyTo, recipientBalance);
        if(cost.getItems().size() > 0) {
          recipientAccount.giveItems(cost.getItems());
        }
      }
    }
  }
}