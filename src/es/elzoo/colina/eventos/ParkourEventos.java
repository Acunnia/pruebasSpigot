package es.elzoo.colina.eventos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import es.elzoo.colina.Main;

public class ParkourEventos implements Listener {
	static private Map<UUID, Long> tiempos = new HashMap<>();
	static private Map<UUID, Integer> checkpoints = new HashMap<>();
	static private Map<UUID, BukkitTask> tareas = new HashMap<>();
	
	static private final Integer cpoints = 5;
	
	private final Main plugin;
	
	public ParkourEventos(Main plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onPressurePlate(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.PHYSICAL)) {
			Location loc = event.getClickedBlock().getLocation();
			if (event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE && (new Location(event.getPlayer().getWorld(), loc.getX(), loc.getBlockY()-1, loc.getZ()).getBlock()).getType() == Material.BEDROCK) {
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
			mostrarTiempo(player);
			player.sendMessage("Empieza el parkour del Lobby.");
		} else {
			tiempos.replace(player.getUniqueId(), System.currentTimeMillis());
			checkpoints.replace(player.getUniqueId(), 0);
			player.sendMessage("Tiempo reiniciado, vuelves a empezar");
		}
	}
	
	private void mostrarTiempo(Player player) {
		BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
            	Long tiempo = System.currentTimeMillis() - tiempos.get(player.getUniqueId());
            	player.sendActionBar(ChatColor.DARK_GRAY + "Tiempo: " + TimeUnit.MILLISECONDS.toMinutes(tiempo) +  ":" + TimeUnit.MILLISECONDS.toSeconds(tiempo) %60 + "." + (tiempo-TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(tiempo))));
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1);
        tareas.put(player.getUniqueId(), runnable);
	}
	
	private void siguienteCheckpoint(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (tiempos.get(player.getUniqueId()) == null) {
			return;
		} else {
			Integer actual = checkpoints.get(player.getUniqueId());
			Location loc = event.getClickedBlock().getLocation();
			Block debajo = new Location(event.getPlayer().getWorld(), loc.getX(), loc.getBlockY()-1, loc.getZ()).getBlock();
			switch (debajo.getType()) {
			case LIME_CONCRETE:
				if (actual < 1) {
					actual = 1;
				} else if (actual == 1) {
					return;
				} else {
					player.sendMessage("Te has saltado algo");
					return;
				}
				break;
			case GREEN_CONCRETE:
				if (actual < 2) {
					actual = 2;
				} else if (actual == 2) {
					return;
				} else {
					player.sendMessage("Te has saltado algo");
					return;
				}
				break;
			case YELLOW_CONCRETE:
				if (actual < 3) {
					actual = 3;
				} else if (actual == 3) {
					return;
				} else {
					player.sendMessage("Te has saltado algo");
					return;
				}
				break;
			case ORANGE_CONCRETE:
				if (actual < 4) {
					actual = 4;
				} else if (actual == 4) {
					return;
				} else {
					player.sendMessage("Te has saltado algo");
					return;
				}
				break;
			case RED_CONCRETE:
				if (actual < 5) {
					actual = 5;
				} else if (actual == 5) {
					return;
				} else {
					player.sendMessage("Te has saltado algo");
					return;
				}
				break;
			default:
				return;
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
		tareas.get(player.getUniqueId()).cancel();
		tareas.remove(player.getUniqueId());
		tiempos.remove(player.getUniqueId());
		checkpoints.remove(player.getUniqueId());
	}
	
	private void cancelarParkour(Player player) {
		if (tiempos.get(player.getUniqueId()) != null) {
			tareas.get(player.getUniqueId()).cancel();
			tareas.remove(player.getUniqueId());
			tiempos.remove(player.getUniqueId());
			checkpoints.remove(player.getUniqueId());
		}
	}
	
	@EventHandler
	public void cancelFly(PlayerToggleFlightEvent event) {
		
	}
}
