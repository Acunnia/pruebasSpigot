package es.elzoo.colina;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getLogger().log(Level.FINE, "Im alive");
	}
}
