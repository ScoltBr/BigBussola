package me.scoltbr.biglandia.compass;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.scoltbr.biglandia.compass.Eventos.Compass;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new Compass(), this);
	}

}
