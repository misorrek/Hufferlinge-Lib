package huff.lib.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.helper.StringHelper;
import huff.lib.manager.delaymessage.DelayMessageManager;
import huff.lib.manager.delaymessage.DelayType;
import huff.lib.various.AlphanumericComparator;

/**
 * A command class for sending a delayed message to a player via DelayMessageManager.
 * Contains the command.
 */
public class DelayMessageCommand implements CommandExecutor, TabCompleter
{
	public static final String PERM_DELAYMESSAGE =  PermissionHelper.PERM_ROOT_HUFF + "delaymessage";
	
	public DelayMessageCommand(@NotNull DelayMessageManager delayMessageManager)
	{
		Validate.notNull((Object) delayMessageManager, "The delay message manager cannot be null.");
		
		this.delayMessageManager = delayMessageManager;
	}
	
	private final DelayMessageManager delayMessageManager;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		if (sender instanceof Player && !PermissionHelper.hasPlayerPermissionFeedbacked((Player) sender, PERM_DELAYMESSAGE))
		{
			return false;
		}
		
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
				return false;
			}
		}
		sender.sendMessage(MessageHelper.getWrongInput("/delaymessage <Benachrichtigungs-Art> <Spieler> <Nachricht>"));
		return false;
	}
	
	// T A B C O M P L E T I O N
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) 
	{
		final List<String> paramSuggestions = new ArrayList<>();
		
		if (!(sender instanceof Player) || !PermissionHelper.hasPlayerPermission((Player) sender, PERM_DELAYMESSAGE))		
		{
			return paramSuggestions;
		}
		
		switch (args.length)
		{
		case 1:
			for (DelayType delayType : DelayType.values())
			{
				paramSuggestions.add(delayType.toString());
			}	
			break;
		case 2:
			for (Player publicPlayer : Bukkit.getOnlinePlayers())
			{
				paramSuggestions.add(publicPlayer.getName());
			}
			break;
		case 3:
			paramSuggestions.add("<Nachricht>");
			break;
		}
		paramSuggestions.sort(new AlphanumericComparator());
		return paramSuggestions;
	}
}
