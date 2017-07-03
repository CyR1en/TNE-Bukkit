package net.tnemc.core.common.account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by creatorfromhell on 7/2/2017.
 * All rights reserved.
 **/
public class WorldAccount {

  private Map<String, BigDecimal> balances = new HashMap<>();

  private String world;
  private Vault vault;
  private Bank bank;

  public WorldAccount(String world) {
    this.world = world;
  }
}