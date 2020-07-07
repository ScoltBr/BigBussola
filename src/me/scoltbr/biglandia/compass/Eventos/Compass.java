package me.scoltbr.biglandia.compass.Eventos;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.scoltbr.biglandia.compass.Utils.Metodos;

public class Compass implements Listener {

	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& player.getItemInHand().getType() == Material.COMPASS) {

			ItemStack itemInHand = player.getItemInHand();
			ItemMeta im = itemInHand.getItemMeta();
			im.setDisplayName("§cEm direção ao jogador: §a" + getNearest(player, 250.0).getName());
			player.getItemInHand().setItemMeta(im);
			player.setCompassTarget(getNearest(player, 250.0).getLocation());
			Metodos.sendActionBar(player, "§cEm direção ao jogador: §a" + getNearest(player, 250.0).getName());
		}
	}

	public Player getNearest(Player player, Double range) {
		double distance = Double.POSITIVE_INFINITY;
		Player target = null;
		for (Entity entity : player.getNearbyEntities(500, 256, 500)) {
			if (entity == null) {
				target = player;
			}
			if (!(entity instanceof Player))
				continue;
			Player alvo = (Player) entity;
			if (alvo.getGameMode() != GameMode.SURVIVAL)
				continue;
			if (entity == player)
				continue;
			double distanceto = player.getLocation().distance(entity.getLocation());
			if (distanceto > distance)
				continue;
			distance = distanceto;
			target = (Player) entity;
		}
		return target;
	}

}
