package es.elzoo.colina.eventos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import net.md_5.bungee.api.ChatColor;

public class ParkourEventos implements Listener {
	static private Map<UUID, Long> tiempos = new HashMap<>();
	static private Map<UUID, Integer> checkpoints = new HashMap<>();
	
	static private final Integer cpoints = 4;
	
	@EventHandler
	public void onPressurePlate(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.PHYSICAL)) {
			if (event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
				empezarParkour(event.getPlayer());
			} else if (event.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
				siguienteCheckpoint(event);
			}
		}
	}
	
	@EventHandler
	private void desconectarParkour(PlayerQuitEvent event) {
		cancelarParkour(event.getPlayer());
	}
	
	private void empezarParkour(Player player) {
		if (tiempos.get(player.getUniqueId()) == null) {
			tiempos.put(player.getUniqueId(), System.currentTimeMillis());
			checkpoints.put(player.getUniqueId(), 0);
			player.sendMessage("Empieza el parkour del Lobby.");
		} else {
			tiempos.replace(player.getUniqueId(), System.currentTimeMillis());
			checkpoints.replace(player.getUniqueId(), 0);
			player.sendMessage("Tiempo reiniciado, vuelves a empezar");
		}
	}
	
	private void siguienteCheckpoint(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (tiempos.get(player.getUniqueId()) == null) {
			player.sendMessage(ChatColor.BOLD+"Aquí no se empieza el parkour! Vuelve atrás y busca el inicio.");
		} else {
			Integer actual = checkpoints.get(player.getUniqueId());
			Location loc = event.getClickedBlock().getLocation();
			Block debajo = new Location(event.getPlayer().getWorld(), loc.getX(), loc.getBlockY()-1, loc.getZ()).getBlock();
			switch (debajo.getType()) {
			case LIME_CONCRETE:
				actual = 1;
				break;
			case GREEN_CONCRETE:
				actual = 2;
				break;
			case YELLOW_CONCRETE:
				actual = 3;
				break;
			case ORANGE_CONCRETE:
				actual = 4;
				break;
			default:
				actual = 5;
				break;
			}
			
			if (actual == cpoints) {
				finParkour(player);
			} else {
				checkpoints.replace(player.getUniqueId(), actual);
				player.sendMessage("Has conseguido el checkpoint número: " + (actual));
			}
		}
	}
	
	private void finParkour(Player player) {
		Long tiempo = System.currentTimeMillis() - tiempos.get(player.getUniqueId());
		player.sendMessage("Has completado el parkour en " + TimeUnit.MILLISECONDS.toMinutes(tiempo) +  ":" + TimeUnit.MILLISECONDS.toSeconds(tiempo) %60 + "." + (tiempo-TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(tiempo))));
		tiempos.remove(player.getUniqueId());
		checkpoints.remove(player.getUniqueId());
	}
	
	private void cancelarParkour(Player player) {
		if (tiempos.get(player.getUniqueId()) != null) {
			tiempos.remove(player.getUniqueId());
			checkpoints.remove(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void cancelFly(PlayerToggleFlightEvent event) {
		
	}
}
