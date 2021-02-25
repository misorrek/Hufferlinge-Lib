package huff.lib.wrapper;

import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

public class WrapperPlayServerTileEntityData extends AbstractPacket 
{
	public static final PacketType TYPE = PacketType.Play.Server.TILE_ENTITY_DATA;

	/**
	 * @param   blockPosition   the position of the block to change the tile entity data for 
	 * @param   action          the action type
	 * @param   nbtBase         the updated nbt data
	 */
	public WrapperPlayServerTileEntityData(@NotNull BlockPosition blockPosition, int action, @NotNull NbtBase<?> nbtBase) 
	{
		this();
		
		setLocation(blockPosition);
		setAction(action);
		setNbtData(nbtBase);
	}
	
	public WrapperPlayServerTileEntityData() 
	{
		super(new PacketContainer(TYPE), TYPE);
		
		handle.getModifier().writeDefaults();
	}

	/**
	 * @param   packet   packet from the type "Play.Server.TILE_ENTITY_DATA"
	 */
	public WrapperPlayServerTileEntityData(PacketContainer packet) 
	{
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location.
	 * 
	 * @return The current Location
	 */
	@NotNull
	public BlockPosition getLocation() 
	{
		return handle.getBlockPositionModifier().read(0);
	}

	/**
	 * Set Location.
	 * 
	 * @param value - new value.
	 */
	public void setLocation(BlockPosition value) 
	{
		handle.getBlockPositionModifier().write(0, value);
	}

	/**
	 * Retrieve Action.
	 * <p>
	 * Notes: the type of update to perform
	 * 
	 * @return The current Action
	 */
	public int getAction() 
	{
		return handle.getIntegers().read(0);
	}

	/**
	 * Set Action.
	 * 
	 * @param value - new value.
	 */
	public void setAction(int value) 
	{
		handle.getIntegers().write(0, value);
	}

	/**
	 * Retrieve NBT Data.
	 * <p>
	 * Notes: if not present then its TAG_END (0)
	 * 
	 * @return The current NBT Data
	 */
	@NotNull
	public NbtBase<?> getNbtData() 
	{
		return handle.getNbtModifier().read(0);
	}

	/**
	 * Set NBT Data.
	 * 
	 * @param value - new value.
	 */
	public void setNbtData(NbtBase<?> value) 
	{
		handle.getNbtModifier().write(0, value);
	}

}
