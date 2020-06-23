package es.elzoo.colina;

import org.bukkit.plugin.java.JavaPlugin;

import es.elzoo.colina.eventos.CartelesEventos;
import es.elzoo.colina.eventos.ParkourEventos;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CartelesEventos(), this);
		getServer().getPluginManager().registerEvents(new ParkourEventos(), this);
	}
}
