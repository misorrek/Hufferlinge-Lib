package huff.lib.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;

import huff.lib.events.PlayerSignInputEvent;
import huff.lib.events.PlayerSignMenuClose;
import huff.lib.wrapper.WrapperPlayClientUpdateSign;
import huff.lib.wrapper.WrapperPlayServerBlockChange;
import huff.lib.wrapper.WrapperPlayServerOpenSignEditor;
import huff.lib.wrapper.WrapperPlayServerTileEntityData;

public class SignHelper
{
	private static final HashMap<UUID, BlockPosition> currentSignInputs = new HashMap<>();
	private static final int SIGN_Y = 1;
	private static final int TILEENTITYDATA_ACTION = 9;
	
	private SignHelper() { }
	
	public static String[] getInputLines(@NotNull String thirdLine, @NotNull String fourthLine)
	{
		Validate.notNull((Object) thirdLine, "The third line cannot be null.");
		Validate.notNull((Object) fourthLine, "The fourth line cannot be null.");
	
		final String[] lines = new String[4];
		
		lines[0] = "";
		lines[1] = "^^^";
		lines[2] = thirdLine;
		lines[3] = fourthLine;	
		
		return lines;
	}
	
	public static String[] getLines(@NotNull String firstLine, @NotNull String secondLine, @NotNull String thirdLine, @NotNull String fourthLine)
	{
		Validate.notNull((Object) firstLine, "The first line cannot be null.");
		Validate.notNull((Object) secondLine, "The second line cannot be null.");
		Validate.notNull((Object) thirdLine, "The third line cannot be null.");
		Validate.notNull((Object) fourthLine, "The fourth line cannot be null.");
	
		final String[] lines = new String[4];
		
		lines[0] = firstLine;
		lines[1] = secondLine;
		lines[2] = thirdLine;
		lines[3] = fourthLine;	
		
		return lines;
	}
	
	public static void openSignInput(@NotNull Player player, @NotNull String[] lines)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) lines, "The lines cannot be null.");	
		
		final BlockPosition blockPositon = new BlockPosition(player.getLocation().getBlockX(), SIGN_Y, player.getLocation().getBlockZ());
		final WrappedBlockData wrappedBlockData = WrappedBlockData.createData(Material.JUNGLE_WALL_SIGN);
		
		new WrapperPlayServerBlockChange(blockPositon, wrappedBlockData).sendPacket(player);
		new WrapperPlayServerTileEntityData(blockPositon, TILEENTITYDATA_ACTION, getLinesAsNbt(lines, blockPositon)).sendPacket(player);
		new WrapperPlayServerOpenSignEditor(blockPositon).sendPacket(player);
		
		currentSignInputs.put(player.getUniqueId(), blockPositon);
	}
	
	public static void registerSignUpdateListener(@NotNull JavaPlugin plugin)
    {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
 
        manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.UPDATE_SIGN)
        {
            @Override
            public void onPacketReceiving(PacketEvent event)
            {
                final Player player = event.getPlayer();
            	final BlockPosition storedBlockPosition = currentSignInputs.get(player.getUniqueId());
            	
            	if (storedBlockPosition == null)
            	{
            		return;
            	}    	    	
                final WrapperPlayClientUpdateSign packet = new WrapperPlayClientUpdateSign(event.getPacket());
            	final BlockPosition currentblockPosition = packet.getLocation();
                
                if (!storedBlockPosition.equals(currentblockPosition))
                {
                	return;
                }
                new WrapperPlayServerBlockChange(currentblockPosition, WrappedBlockData.createData(Material.AIR)).sendPacket(player);
                currentSignInputs.remove(player.getUniqueId());                           
                Bukkit.getScheduler().runTask(plugin, () ->
                {
                	 Bukkit.getPluginManager().callEvent(new PlayerSignInputEvent(player, packet.getLines()));
                	 Bukkit.getPluginManager().callEvent(new PlayerSignMenuClose(player));
                });
            }

            @Override
            public void onPacketSending(PacketEvent event)
            {
            	//Not needed because i sent the packet.
            }
        });
    }
	
	private static NbtBase<?> getLinesAsNbt(@NotNull String[] lines, @NotNull BlockPosition blockPosition)
	{
		List<NbtBase<?>> tags = new ArrayList<>();
		
		tags.add(NbtFactory.of("id", "jungle_wall_sign"));
        tags.add(NbtFactory.of("x", blockPosition.getX()));
        tags.add(NbtFactory.of("y", blockPosition.getY()));
        tags.add(NbtFactory.of("z", blockPosition.getZ()));
		tags.add(NbtFactory.of("Text1", IndependencyHelper.getJsonText(lines[0])));
		tags.add(NbtFactory.of("Text2", IndependencyHelper.getJsonText(lines[1])));
		tags.add(NbtFactory.of("Text3", IndependencyHelper.getJsonText(lines[2])));
		tags.add(NbtFactory.of("Text4", IndependencyHelper.getJsonText(lines[3])));
		
		return NbtFactory.ofCompound("", tags);
	}
}