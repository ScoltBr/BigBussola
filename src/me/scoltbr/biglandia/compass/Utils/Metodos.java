package me.scoltbr.biglandia.compass.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Metodos {
	private final static int CENTER_PX = 154;

	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void sendTitle(Player player, String title, String subtitle) {
		try {
			if (title != null) {
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("%player%", player.getDisplayName());
				Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE")
						.get(null);
				Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
				Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
						getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
						int.class, int.class, int.class);
				Object titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, 10, 10, 10);
				sendPacket(player, titlePacket);
			}

			if (subtitle != null) {
				subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
				subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
				Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE")
						.get(null);
				Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
				Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(
						getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"),
						int.class, int.class, int.class);
				Object subtitlePacket = subtitleConstructor.newInstance(enumSubtitle, chatSubtitle, 10, 10, 10);
				sendPacket(player, subtitlePacket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendTabTitle(Player player, String header, String footer) {
		if (header == null)
			header = "";
		header = ChatColor.translateAlternateColorCodes('&', header);

		if (footer == null)
			footer = "";
		footer = ChatColor.translateAlternateColorCodes('&', footer);

		header = header.replaceAll("%player%", player.getDisplayName());
		footer = footer.replaceAll("%player%", player.getDisplayName());

		try {
			Object tabHeader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\":\"" + header + "\"}");
			Object tabFooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class)
					.invoke(null, "{\"text\":\"" + footer + "\"}");
			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutPlayerListHeaderFooter")
					.getConstructor(getNMSClass("IChatBaseComponent"));
			Object packet = titleConstructor.newInstance(tabHeader);
			Field field = packet.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(packet, tabFooter);
			sendPacket(player, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendTitle(Player player, String msgTitle, String msgSubTitle, int ticks) {
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + msgTitle + "\"}");
		IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + msgSubTitle + "\"}");
		PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle p2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p2);
	}

	public static void sendActionBar(Player player, String message) {
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
	}
}