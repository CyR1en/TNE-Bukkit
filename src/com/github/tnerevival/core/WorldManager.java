package com.github.tnerevival.core;

import com.github.tnerevival.core.currency.Currency;

import java.util.*;

/**
 * Created by creatorfromhell on 6/25/2017.
 * All rights reserved.
 **/
public class WorldManager {
  private Map<String, Currency> currencies = new HashMap<>();
  private Map<String, Object> configurations = new HashMap<>();
  private List<String> disabled = new ArrayList<>();

  private String world;

  public WorldManager(String world) {
    this.world = world;
  }

  public void addCurrency(Currency currency) {
    currencies.put(currency.getName(), currency);
  }

  public Currency getCurrency(String currency) {
    return currencies.get(currency);
  }

  public Collection<Currency> getCurrencies() {
    return currencies.values();
  }

  public void disable(String currency, boolean disable) {
    if(disable) disabled.add(currency);
    else disabled.remove(currency);
  }

  public boolean containsCurrency(String currency) {
    for(String s : currencies.keySet()) {
      if(s.equalsIgnoreCase(currency)) return true;
    }
    return false;
  }

  public boolean configExists(String node) {
    return configurations.containsKey(node);
  }

  public Object getConfiguration(String node) {
    return configurations.get(node);
  }
}