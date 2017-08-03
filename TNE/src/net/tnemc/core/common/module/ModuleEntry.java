package net.tnemc.core.common.module;

/**
 * Created by creatorfromhell on 7/4/2017.
 * All rights reserved.
 **/
public class ModuleEntry {
  private ModuleInfo info;
  private Module module;

  public ModuleEntry(ModuleInfo info, Module module) {
    this.info = info;
    this.module = module;
  }

  public void test() {
  }

  public ModuleInfo getInfo() {
    return info;
  }

  public void setInfo(ModuleInfo info) {
    this.info = info;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }
}