package net.tnemc.core.common.module;

import net.tnemc.core.TNE;
import net.tnemc.core.common.utils.MISCUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
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
 * Created by creatorfromhell on 07/01/2017.
 */

/**
 * This class loads all modules from the modules directory, and loads the main class based on @Module.
 **/
public class ModuleLoader {

  public Map<String, ModuleEntry> modules = new HashMap<>();

  /**
   * Loads all modules into a map for later usage.
   * @return The map containing every module in format Name, ModuleInstance
   */
  public void load() {
    File directory = new File("plugins/TheNewEconomy/modules");
    File[] jars = directory.listFiles((dir, name) -> name.endsWith(".jar"));

    if(jars != null) {
      for (File jar : jars) {
        Module module = getModuleClass(jar.getAbsolutePath());
        if (!module.getClass().isAnnotationPresent(ModuleInfo.class)) {
          TNE.instance().getLogger().info("Invalid module format! Module File: " + jar.getName());
          continue;
        }
        ModuleInfo info = module.getClass().getAnnotation(ModuleInfo.class);
        ModuleEntry entry = new ModuleEntry(info, module);
        TNE.instance().getLogger().info("Found module: " + info.name() + " version: " + info.version());
        modules.put(entry.getInfo().name(), entry);
      }
    }
  }

  private Module getModuleClass(String modulePath) {
    Module module = null;

    File file = new File(modulePath);
    String moduleMain = getModuleMain(file);
    Class<? extends Module> moduleClass;
    Class<?> mainClass;

    try {
      mainClass = Class.forName(moduleMain, true, getClass().getClassLoader());
    } catch (ClassNotFoundException e) {
      MISCUtils.debug(e);
    }

    URLClassLoader urlClassLoader = null;
    try {
      urlClassLoader = new URLClassLoader(new URL[] { file.toURI().toURL()}, Module.class.getClassLoader());
      mainClass = urlClassLoader.loadClass(moduleMain);
      moduleClass = mainClass.asSubclass(Module.class);
      module = moduleClass.newInstance();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return module;
  }

  private String getModuleMain(File jarFile) {
    String main = "";
    JarFile jar = null;
    InputStream in = null;
    BufferedReader reader = null;

    try {
      jar = new JarFile(jarFile);
      JarEntry infoFile = jar.getJarEntry("module.tne");

      if(infoFile == null) {
        TNE.instance().getLogger().info("TNE encountered an error while loading a module! No module.tne file!");
        return "";
      }

      in = jar.getInputStream(infoFile);
      reader = new BufferedReader(new InputStreamReader(in));

      main = reader.readLine().split("=")[1].trim();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if(jar != null) {
        try {
          jar.close();
        } catch(IOException e) {
          MISCUtils.debug(e);
        }
      }

      if(in != null) {
        try {
          in.close();
        } catch(IOException e) {
          MISCUtils.debug(e);
        }
      }

      if(reader != null) {
        try {
          reader.close();
        } catch(IOException e) {
          MISCUtils.debug(e);
        }
      }
    }
    return main;
  }

  public Map<String, ModuleEntry> getModules() {
    return modules;
  }
}