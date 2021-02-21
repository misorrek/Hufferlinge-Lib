package huff.lib.wrapper;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class WrapperPlayServerOpenSignEditor extends AbstractPacket 
{
	public static final PacketType TYPE = PacketType.Play.Server.OPEN_SIGN_EDITOR;

	public WrapperPlayServerOpenSignEditor(@NotNull BlockPosition blockPosition) 
	{
		this();
		
		setLocation(blockPosition);
	}
	
	public WrapperPlayServerOpenSignEditor() 
	{
		super(new PacketContainer(TYPE), TYPE);
		
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerOpenSignEditor(PacketContainer packet) 
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
	 * @param blockPosition - the block position.
	 */
	public void setLocation(@NotNull BlockPosition blockPosition) 
	{
		Validate.notNull((Object) blockPosition, "The block position cannot be null.");
		
		handle.getBlockPositionModifier().write(0, blockPosition);
	}
}
