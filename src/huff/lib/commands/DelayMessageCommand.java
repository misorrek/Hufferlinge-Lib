package huff.lib.commands;

import java.util.stream.Stream;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.helper.StringHelper;
import huff.lib.manager.delaymessage.DelayMessageManager;
import huff.lib.manager.delaymessage.DelayType;
import huff.lib.various.HuffCommand;

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
		this.setDescription("Sendet verzögerte Nachricht");
		this.setUsage("/delaymessage <Benachrichtigungs-Art> <Spieler> <Nachricht>");
		this.setPermission(PermissionHelper.PERM_ROOT_HUFF + "delaymessage");
		addTabCompletion();
		this.registerCommand();
	}
	
	private final DelayMessageManager delayMessageManager;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		if (args.length >= 3)
		{
			try
			{
				DelayType delayType = DelayType.valueOf(args[0]);	
				OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
				StringBuilder builder = new StringBuilder();
				
				for (int i = 2; i < args.length; i++)
				{
					builder.append(args[i] + " ");
				}
				delayMessageManager.addDelayMessage(targetPlayer.getUniqueId(), delayType, builder.toString().trim());
				sender.sendMessage(MessageHelper.PREFIX_HUFF + "Nachricht zum Versand gespeichert.");
				return true;
			}
			catch (IllegalArgumentException exception)
			{
				sender.sendMessage(MessageHelper.PREFIX_HUFF + "Die Benachrichtigungs-Art ist ungültig. Mögliche Werte §9\"" + StringHelper.toValueList(DelayType.class) + "\"§7.");
				return true;
			}
		}
		return false;
	}
	
	// T A B C O M P L E T I O N
	
	private void addTabCompletion()
	{
		this.addTabCompletion(0, Stream.of(DelayType.values())
				.map(DelayType::toString)
				.toArray(String[]::new));
		this.addTabCompletion(1, Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.toArray(String[]::new));
		this.addTabCompletion(2, "<Nachricht>");
	}
}
