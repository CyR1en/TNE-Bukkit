package com.github.tnerevival.serializable;

import org.bukkit.enchantments.Enchantment;

import java.io.Serializable;


public class SerializableEnchantment implements Serializable {
  private static final long serialVersionUID = 1L;

  String name;

  public SerializableEnchantment(Enchantment e) {
    this.name = e.getName();
  }

  public SerializableEnchantment(String name) {
    this.name = name;
  }

  public Enchantment getEnchantment() {
    return Enchantment.getByName(name);
  }
}
