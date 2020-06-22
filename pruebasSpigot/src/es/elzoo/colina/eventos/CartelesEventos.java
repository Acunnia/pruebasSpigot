package es.elzoo.colina.eventos;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CartelesEventos implements Listener {
	
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

			default:
				giveEspada(event.getPlayer());
				break;
			}
		}
	}
	
	private static void giveCasco(Player player) {
		player.getInventory().addItem(new ItemStack(Material.LEATHER_HELMET, 1));
	}
	
	private static void giveEspada(Player player) {
		player.getInventory().addItem(new ItemStack(Material.WOODEN_SWORD, 1));
	}
	
}
