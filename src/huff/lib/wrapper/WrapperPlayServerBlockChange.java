package huff.lib.wrapper;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;

public class WrapperPlayServerBlockChange extends AbstractPacket 
{
	public static final PacketType TYPE = PacketType.Play.Server.BLOCK_CHANGE;

	public WrapperPlayServerBlockChange(@NotNull BlockPosition blockPosition, @NotNull WrappedBlockData blockData) 
	{
		this();
		
		setLocation(blockPosition);
		setBlockData(blockData);
	}
	
	public WrapperPlayServerBlockChange() 
	{
		super(new PacketContainer(TYPE), TYPE);
		
		handle.getModifier().writeDefaults();
	}
	
	public WrapperPlayServerBlockChange(PacketContainer packet) 
	{
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location as block position.
	 * 
	 * @return The current location as block position.
	 */
	public BlockPosition getLocation() 
	{
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set location as block position.
	 * 
	 * @param blockPosition   the block position.
	 */
	public void setLocation(@NotNull BlockPosition blockPosition) 
	{
		Validate.notNull((Object) blockPosition, "The block position cannot be null.");
		
		handle.getBlockPositionModifier().write(0, blockPosition);
	}

	/**
	 * Retrieve the bukkit location.
	 * 
	 * @param   world    World for the location
	 * @return           The bukkit location.
	 */
	public Location getBukkitLocation(@NotNull World world) 
	{
		Validate.notNull((Object) world, "The world cannot be null.");
		
		return getLocation().toVector().toLocation(world);
	}

	/**
	 * Retrieve Block Data.
	 * 
	 * @return The current Block Data
	 */
	public WrappedBlockData getBlockData() 
	{
		return handle.getBlockData().read(0);
	}

	/**
	 * Set Block Data.
	 * 
	 * @param   value   new value.
	 */
	public void setBlockData(@NotNull WrappedBlockData blockData) 
	{
		Validate.notNull((Object) blockData, "The block data cannot be null.");
		
		handle.getBlockData().write(0, blockData);
	}
}
