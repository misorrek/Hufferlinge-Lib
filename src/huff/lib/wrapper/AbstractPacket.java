package huff.lib.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;

/*
 * Represents a abstract minecraft client server connection packet.
 * Contains general handling like send and receive or the packet handle.
 */
public abstract class AbstractPacket 
{
	/**
	 * Constructs a new strongly typed wrapper for the given packet.
	 * 
	 * @param handle - handle to the raw packet data.
	 * @param type - the packet type.
	 */
	protected AbstractPacket(@NotNull PacketContainer handle, @NotNull PacketType type) 
	{
		Validate.notNull((Object) handle, "The handle (packet container) cannot be null.");
		Validate.isTrue(Objects.equal(handle.getType(), type), "The handle (packet container) is not a packet of type " + type);
		
		this.handle = handle;
	}
	
	protected final PacketContainer handle;

	/**
	 * Retrieve a handle to the raw packet data.
	 * 
	 * @return Raw packet data.
	 */
	@NotNull
	public PacketContainer getHandle() 
	{
		return handle;
	}
	
	/**
	 * Send the current packet to all online players.
	 */
	public void broadcastPacket() 
	{
		ProtocolLibrary.getProtocolManager().broadcastServerPacket(getHandle());
	}

	/**
	 * Send the current packet to the given receiver.
	 * 
	 * @param receiver - the receiver.
	 * @throws RuntimeException If the packet cannot be sent.
	 */
	public void sendPacket(Player receiver) 
	{
		try 
		{
			ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, getHandle());
		} 
		catch (InvocationTargetException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, exception, () -> "Cannot send packet (Type " + getHandle().getType().toString() + ") to receiver " + receiver.getName() + ".");
		}
	}

	/**
	 * Simulate receiving the current packet from the given sender.
	 * 
	 * @param sender - the sender.
	 * @throws RuntimeException if the packet cannot be received.
	 */
	public void receivePacket(Player sender) 
	{
		try 
		{
			ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, getHandle());
		} 
		catch (InvocationTargetException | IllegalAccessException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, exception, () -> "Cannot receive packet (Type " + getHandle().getType().toString() + ") from sender " + sender.getName() + ".");
		}
	}
}
