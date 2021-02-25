package huff.lib.wrapper;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

public class WrapperPlayClientUpdateSign extends AbstractPacket 
{
	public static final PacketType TYPE = PacketType.Play.Client.UPDATE_SIGN;

	/**
	 * @param   blockPosition   the position of the updated sign block
	 * @param   lines           the updated lines of the sign
	 */
	public WrapperPlayClientUpdateSign(@NotNull BlockPosition blockPosition, @Nullable String[] lines) 
	{
		this();
		
		setLocation(blockPosition);
		
		if (lines != null)
		{
			setLines(lines);
		}
	}
	
	public WrapperPlayClientUpdateSign() 
	{
		super(new PacketContainer(TYPE), TYPE);
		
		handle.getModifier().writeDefaults();
	}
	
	/**
	 * @param   packet   packet from the type "Play.Client.UPDATE_SIGN"
	 */
	public WrapperPlayClientUpdateSign(PacketContainer packet) 
	{
		super(packet, TYPE);
	}

	/**
	 * Retrieve Location as block position.
	 * 
	 * @return The current location as block position.
	 */
	@NotNull
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

	/**
	 * Retrieve this sign's lines of text.
	 * 
	 * @return The current lines
	 */
	@NotNull
	public String[] getLines() 
	{
		return handle.getStringArrays().read(0);
	}

	/**
	 * Set this sign's lines of text.
	 * 
	 * @param value - Lines, must be 4 elements long
	 */
	public void setLines(@NotNull String[] lines) 
	{
		Validate.notNull((Object) lines, "The lines cannot be null.");
		Validate.isTrue(lines.length == 4, "The lines must have four elements.");

		handle.getStringArrays().write(0, lines);
	}
}
