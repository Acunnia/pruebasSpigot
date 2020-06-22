package es.elzoo.colina;

import org.bukkit.plugin.java.JavaPlugin;

import es.elzoo.colina.eventos.CartelesEventos;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CartelesEventos(), this);
	}
}
