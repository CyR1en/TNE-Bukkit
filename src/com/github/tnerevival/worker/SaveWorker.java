package com.github.tnerevival.worker;

import com.github.tnerevival.TNE;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveWorker extends BukkitRunnable {

  private TNE plugin;

  public SaveWorker(TNE plugin) {
    this.plugin = plugin;
  }

  @Override
  public void run() {
    plugin.saveManager.save();
  }
}