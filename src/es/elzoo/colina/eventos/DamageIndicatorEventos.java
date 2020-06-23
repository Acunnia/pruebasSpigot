package es.elzoo.colina.eventos;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import es.elzoo.colina.Main;

public class DamageIndicatorEventos implements Listener {
	private final Main plugin;
	private final Map<ArmorStand, Long> armorStands = new LinkedHashMap<>();
	private final FixedMetadataValue armorStandMeta;
	
	public DamageIndicatorEventos(Main plugin) {
		this.plugin = plugin;
		armorStandMeta = new FixedMetadataValue(plugin, 0);
	}
	
	@EventHandler
    public void onEntityDamageEvent(EntityDamageEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }
        handleArmorStand((LivingEntity) e.getEntity(), damageFormat(e.getFinalDamage()), e.getCause(), e.getFinalDamage());
    }
	
	private String damageFormat(double damage) {
        DecimalFormat df;
        df = new DecimalFormat("#.##");
        return df.format(damage);
    }
	
	private void handleArmorStand(LivingEntity entity, String format, EntityDamageEvent.DamageCause damageCause, double damage) {
        if (isSpawnArmorStand(entity, damageCause, damage)) {
            spawnArmorStand(entity.getLocation(), format);
        }
    }
	
	public ArmorStand spawnArmorStand(Location loc, String name) {
        ArmorStand armorStand = buildArmorStand(loc, plugin.getConfig().getDouble("Damage Indicator.Distance"), armorStandMeta, name);
//        if (hider != null) {
//            Bukkit.getOnlinePlayers().stream().filter(op -> !plugin.getStorageProvider().showArmorStand(op)).forEach(op -> hider.hideEntity(op, armorStand));
//        }
        armorStands.put(armorStand, System.currentTimeMillis());
        return armorStand;
    }

	private boolean isSpawnArmorStand(Entity entity, EntityDamageEvent.DamageCause damageCause, double damage) {
		Boolean res = true;
		if (damage <= 0) {
			res = false;
		}
		if (!(entity instanceof LivingEntity)) {
            return false;
        }
		if (entity instanceof ArmorStand) {
            return false;
        }
		if (entity.hasMetadata("NPC")) {
            return false;
        }
        return res;
    }
	
	private static ArmorStand buildArmorStand(Location location, double distance, FixedMetadataValue fixedMetadataValue, String name) {
        ArmorStand armorStand;
        armorStand = location.getWorld().spawn(location.clone().add(0, location.getWorld().getMaxHeight() - location.getY(), 0), ArmorStand.class, stand -> setStandProperties(stand, location, distance, fixedMetadataValue));
        armorStand.setCustomName(name);
        armorStand.setCustomNameVisible(true);
        return armorStand;
    }
	
	private static void setStandProperties(ArmorStand armorStand, Location location, double distance, FixedMetadataValue fixedMetadataValue) {
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(false);
        armorStand.setMetadata("Mastercode-DamageIndicator", fixedMetadataValue);
        armorStand.setCollidable(false);
        armorStand.setInvulnerable(true);
        armorStand.teleport(location.clone().add(0, distance, 0));
        armorStand.setRemoveWhenFarAway(true);
    }
	
	public Map<ArmorStand, Long> getArmorStands() {
        return armorStands;
    }
	
}
