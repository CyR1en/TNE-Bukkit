package com.github.tnerevival.commands.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import org.bukkit.command.CommandSender;

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
 * Created by creatorfromhell on 10/17/2016.
 */
public class AuctionStartCommand extends TNECommand {


  public AuctionStartCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "start";
  }

  @Override
  public String[] getAliases() {
    return new String[]{ "s" };
  }

  @Override
  public String getNode() {
    return "tne.auction.start";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
      "/auction start [configurations] - Start a new auction.",
      "[item:[data value]] - The name of the item to auction off",
      "[slot:#] - The slot of the item to auction off",
      "[amount:#] - The amount of <item> to auction off",
      "[start:#] - The starting bid for this item",
      "[cost:#] - The cost of this item.",
      "[increment:#] - The increment in which bids will be increased.",
      "[admin:true/false] - Whether or not this is an administrator auction.",
      "[time:#] - The length(in seconds) this auction will go on for.",
      "[global:true/false] - Whether or not this auction is global or world-based.",
      "[permission:node] - The permission needed to partake in this auction."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    String world = getWorld(sender);
    Boolean silent = command.equalsIgnoreCase("sauction");


    return false;
  }
}
