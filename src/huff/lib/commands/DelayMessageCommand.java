package huff.lib.commands;

import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.PermissionHelper;
import huff.lib.helper.StringHelper;
import huff.lib.helper.UserHelper;
import huff.lib.manager.delaymessage.DelayMessageManager;
import huff.lib.manager.delaymessage.DelayType;
import huff.lib.various.HuffCommand;
import huff.lib.various.LibMessage;
import huff.lib.various.structures.StringPair;

/**
 * A command class for sending a delayed message to a player via DelayMessageManager.
 * Contains the command.
 */
public class DelayMessageCommand extends HuffCommand 
{
	public DelayMessageCommand(@NotNull JavaPlugin plugin, @NotNull DelayMessageManager delayMessageManager)
	{
		super(plugin, "delaymessage");
		
		Validate.notNull((Object) delayMessageManager, "The delay message manager cannot be null.");
		
		this.delayMessageManager = delayMessageManager;
		super.setDescription("Sendet verzögerte Nachricht");
		super.setUsage("/delaymessage <Benachrichtigungs-Art> <Spieler> <Nachricht>");
		super.setPermission(PermissionHelper.PERM_ROOT_HUFF + "delaymessage");
		addTabCompletion();
		super.registerCommand();
	}
	
	private final DelayMessageManager delayMessageManager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		if (args.length >= 3)
		{
			final UUID targetPlayer = UserHelper.getUniqueId(args[1]);
			
			if (targetPlayer == null)
			{
				sender.sendMessage(LibMessage.NOTFOUND.getMessage(new StringPair("user", args[1])));
				return true;
			}
			
			try
			{
				final DelayType delayType = DelayType.valueOf(args[0]);					
				final StringBuilder builder = new StringBuilder();

				for (int i = 2; i < args.length; i++)
				{
					builder.append(args[i] + " ");
				}
				delayMessageManager.addDelayMessage(targetPlayer, delayType, builder.toString().trim());
				sender.sendMessage(LibMessage.DELAYMESSAGE_SAVED.getMessage());
				return true;
			}
			catch (IllegalArgumentException exception)
			{
				sender.sendMessage(LibMessage.DELAYMESSAGE_INVALIDTYPE.getMessage(new StringPair("types", StringHelper.toValueList(DelayType.class))));
				return true;
			}
		}
		return false;
	}
	
	// T A B C O M P L E T I O N
	
	private void addTabCompletion()
	{
		super.addTabCompletion(0, Stream.of(DelayType.values())
				.map(DelayType::toString)
				.toArray(String[]::new));
		super.addTabCompletion(1, Stream.of(Bukkit.getOfflinePlayers())
				.map(OfflinePlayer::getName)
				.toArray(String[]::new));
		super.addTabCompletion(2, "<Nachricht>");
	}
}
