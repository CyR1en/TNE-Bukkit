package net.tnemc.core.common.transaction;

import net.tnemc.core.TNE;
import net.tnemc.core.event.transaction.TNETransactionEvent;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

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
public class Transaction {

  private boolean voided = false;

  private UUID uuid;
  private String initiator;
  private String recipient;
  private String world;
  private TransactionType type;
  private long time;

  public Transaction(UUID initiator, UUID recipient, String world, TransactionType type) {
    this(TNE.transactionManager().generateTransactionID(), initiator.toString(), recipient.toString(), world, type, new Date().getTime());
  }

  public Transaction(String initiator, String recipient, String world, TransactionType type) {
    this(TNE.transactionManager().generateTransactionID(), initiator, recipient, world, type, new Date().getTime());
  }

  public Transaction(UUID id, UUID initiator, UUID recipient, String world, TransactionType type, long time) {
    this(id, initiator.toString(), recipient.toString(), world, type, time);
  }

  public Transaction(UUID id, String initiator, String recipient, String world, TransactionType type, long time) {
    this.uuid = id;
    this.initiator = initiator;
    this.recipient = recipient;
    this.world = world;
    this.type = type;
    this.time = time;
  }

  public TransactionResult handle() {
    time = new Date().getTime();
    TransactionResult result = type.handle(initiator, recipient, world);
    TNETransactionEvent event = new TNETransactionEvent(this, result);
    Bukkit.getServer().getPluginManager().callEvent(event);
    if(event.getResult().proceed()) {
      event.getTransaction().getType().setBalances();
    }
    return event.getResult();
  }

  public UUID getUuid() {
    return uuid;
  }

  public boolean voidTransaction() {
    voided = type.voidTransaction();
    return voided;
  }

  public boolean isVoided() {
    return voided;
  }

  public void setVoided(boolean voided) {
    this.voided = voided;
  }

  public String getInitiator() {
    return initiator;
  }

  public String getRecipient() {
    return recipient;
  }

  public String getWorld() {
    return world;
  }

  public TransactionCost getCost() {
    return type.cost;
  }

  public TransactionType getType() {
    return type;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }
}