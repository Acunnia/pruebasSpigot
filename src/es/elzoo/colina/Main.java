package es.elzoo.colina;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import es.elzoo.colina.eventos.CartelesEventos;
import es.elzoo.colina.eventos.DamageIndicatorEventos;
import es.elzoo.colina.eventos.ParkourEventos;

public class Main extends JavaPlugin {
	private DamageIndicatorEventos diEventos;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CartelesEventos(), this);
		getServer().getPluginManager().registerEvents(new ParkourEventos(), this);
		Bukkit.getPluginManager().registerEvents(diEventos = new DamageIndicatorEventos(this), this);
		startTask();
	}
	
	
	private void startTask() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (diEventos != null) {
                Iterator<Map.Entry<ArmorStand, Long>> asit = diEventos.getArmorStands().entrySet().iterator();
                while (asit.hasNext()) {
                    Map.Entry<ArmorStand, Long> ent = asit.next();
                    if (ent.getValue() + 1500 <= System.currentTimeMillis()) {
                        ent.getKey().remove();
                        asit.remove();
                    } else {
                        ent.getKey().teleport(ent.getKey().getLocation().clone().add(0.0, 0.07, 0.0));
                    }
                }
            }
		}, 0, 1);
	}
}
