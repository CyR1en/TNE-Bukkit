package net.tnemc.core.common.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell on 7/2/2017.
 * All rights reserved.
 **/
public class WorldHoldings {

  private Map<String, BigDecimal> holdings = new HashMap<>();
  private String world;

  public WorldHoldings(String world) {
    this.world = world;
  }

  public BigDecimal getHoldings(String currency) {
    if(holdings.containsKey(currency)) {
      return holdings.get(currency);
    }
    //TODO: Initialize currency holdings with default holdings.
    return new BigDecimal(0.0);
  }

  public void setHoldings(String currency, BigDecimal newHoldings) {
    holdings.put(currency, newHoldings);
  }

  public boolean hasHoldings(String currency) {
    return holdings.containsKey(currency);
  }
}