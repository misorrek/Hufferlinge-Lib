package huff.lib.helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_16_R1.ChatMessageType;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import net.minecraft.server.v1_16_R1.PacketPlayOutChat;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R1.Vec3D;
import net.minecraft.server.v1_16_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R1.PacketPlayOutTitle.EnumTitleAction;

public class PackageHelper 
{
	private static final int TICK_SECOND = 20;
	
	private PackageHelper() { }
	
	// G E N E R A L
	
	public static void sendPacket(Player player, Object packet) 
	{
		try 
		{
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerCon = handle.getClass().getField("playerConnection").get(handle);
			playerCon.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerCon, packet);
		} 
		catch (Exception exeption) 
		{
			exeption.printStackTrace();
		}
	}
	
	public static void sendPacketDelayed(JavaPlugin plugin, Player player, Object packet, long ticks) 
	{
		new BukkitRunnable()
		{				
			@Override
			public void run()
			{
				sendPacket(player, packet);
			}
		}.runTaskLater(plugin, ticks);
	}
	
	public static void sendPacketDelayedAsync(JavaPlugin plugin, Player player, Object packet, int ticks) 
	{
		new BukkitRunnable()
		{				
			@Override
			public void run()
			{
				sendPacket(player, packet);
			}
		}.runTaskLaterAsynchronously(plugin, ticks);
	}
	
	// D I S P L A Y S
	
	private static void sendRawTitle(Player player, String title, String subtitle)
	{
		IChatBaseComponent chatTitle = ChatSerializer.a(getJsonMessage(title));
		IChatBaseComponent chatSubTitle = ChatSerializer.a(getJsonMessage(subtitle));
		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle packet2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);
	}
	
	public static void sendTitle(Player player, String title, String subtitle, int ticks) 
	{
		sendTitle(player, title, subtitle, ticks, TICK_SECOND, TICK_SECOND);
	}
	
	public static void sendTitle(Player player, String title, String subtitle, int ticks, int infade, int outfade) 
	{
		sendRawTitle(player, title, subtitle);
		sendTime(player, ticks, infade, outfade);
	}
	
	public static void sendGameInfo(Player player, String message) 
	{
		IChatBaseComponent cbc = ChatSerializer.a(getJsonMessage(message));
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO, player.getUniqueId());
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
	}
	
	// U T I L
	
	public static Vec3D getLocationToVec3D(Location loc) 
	{
		return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public static long getSecondsInTicks(int seconds)
	{
		return (long) TICK_SECOND * seconds;
	}
	
	public static String getJsonMessage(String message)
	{
		return "{\"text\": \"" + message + "\"}";
	}
	
	private static void sendTime(Player player, int ticks, int infade, int outfade)
	{
		PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, infade, ticks, outfade);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private static Class<?> getNMSClass(String name) 
	{
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try 
		{
			return Class.forName("net.minecraft.server." + version + "." + name);
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
}
