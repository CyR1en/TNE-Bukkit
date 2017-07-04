package net.tnemc.core.common.inventory;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by creatorfromhell on 2/14/2017.
 * All rights reserved.
 **/
public abstract class InventoryType {

  public static List<InventoryType> types = new ArrayList<>();

  private String identifier;
  private String permission;

  public static InventoryType fromTitle(String title) {
    for(InventoryType type : types) {
      if(title.toLowerCase().contains(type.getIdentifier().toLowerCase())) return type;
    }
    return null;
  }

  public boolean canOpen(Player player) {
    return player.hasPermission(permission);
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }
}