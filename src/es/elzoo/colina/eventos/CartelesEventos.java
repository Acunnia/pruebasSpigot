package es.elzoo.colina.eventos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CartelesEventos implements Listener {
	static private Map<UUID, Long> cdCasco = new HashMap<>();
	static private Map<UUID, Long> cdNieve = new HashMap<>();
	
	private final static int cd = 30;
	
	@EventHandler
	public static void onPlayerInteract(PlayerInteractEvent event) {
		if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		if(!event.hasBlock()) {
			return;
		}
		if(!(event.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		String[] sign = ((Sign) event.getClickedBlock().getState()).getLines();
		if(sign[0].equals("[KIT]")) {
			switch (sign[1]) {
			case "Casco de cuero":
				giveCasco(event.getPlayer());
				break;
			case "Bola de nieve":
				giveNieve(event.getPlayer());
			default:
				giveEspada(event.getPlayer());
				break;
			}
		}
	}
	
	private static void giveCasco(Player player) {
		if (cdCasco.get(player.getUniqueId()) == null) {
			cdCasco.put(player.getUniqueId(), System.currentTimeMillis());
			player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET, 1));
			return;
		}
		if ((cdCasco.get(player.getUniqueId())/1000 + cd) <= (System.currentTimeMillis()/1000)) {
			cdCasco.put(player.getUniqueId(), System.currentTimeMillis());
			player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET, 1));
		} else {
			Long time = (cdCasco.get(player.getUniqueId())/1000 + cd) - (System.currentTimeMillis()/1000);
			player.sendMessage("Te quedan " + time + " segundos para poder usar ese cartel otra vez");
		}
	}
	
	private static void giveNieve(Player player) {
		if (cdNieve.get(player.getUniqueId()) == null) {
			cdNieve.put(player.getUniqueId(), System.currentTimeMillis());
			player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 1));
			return;
		}
		if ((cdNieve.get(player.getUniqueId())/1000 + cd) <= (System.currentTimeMillis()/1000)) {
			cdNieve.put(player.getUniqueId(), System.currentTimeMillis());
			player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 1));
		} else {
			Long time = (cdNieve.get(player.getUniqueId())/1000 + cd) - (System.currentTimeMillis()/1000);
			player.sendMessage("Te quedan " + time + " segundos para poder usar ese cartel otra vez");
		}
	}
	
	private static void giveEspada(Player player) {
		player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD, 1));
	}
	
}
