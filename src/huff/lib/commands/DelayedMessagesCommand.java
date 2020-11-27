package huff.lib.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.manager.delayedmessage.DelayType;
import huff.lib.manager.delayedmessage.DelayedMessagesManager;
import huff.lib.manager.delayedmessage.MessageType;

public class DelayedMessagesCommand implements CommandExecutor 
{
	public DelayedMessagesCommand(@NotNull DelayedMessagesManager delayedMessageManager)
	{
		Validate.notNull((Object) delayedMessageManager, "The delayed-messages-manager cannot be null.");
		
		this.delayedMessageManager = delayedMessageManager;
	}
	
	private final DelayedMessagesManager delayedMessageManager;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		/*if (sender instanceof Player && !PermissionHelper.hasPlayerPermissionFeedbacked((Player) sender, PermissionHelper.PERM_ALL))
		{
			return false;
		}*/
		
		if (args.length >= 4)
		{
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			MessageType messageType = MessageType.valueOf(args[1]);
			DelayType delayType = DelayType.valueOf(args[2]);			
			StringBuilder builder = new StringBuilder();
			
			if (messageType == null)
			{
				MessageType[] messageTypeValue = MessageType.values();
				
				for (int i = 0; i < messageTypeValue.length; i++)
				{
					if (i != (messageTypeValue.length - 1))
					{
						builder.append(messageTypeValue[i].toString() + ", ");
					}
					else
					{
						builder.append(messageTypeValue[i].toString());
					}
				}	
				sender.sendMessage(MessageHelper.PREFIX_HUFF + "Die Nachrichten-Kategorie ist ungültig. Mögliche Werte §9\"" + builder.toString() + "\"§7.");
				return false;
			}
			
			if (delayType == null)
			{
				DelayType[] delayTypeValues = DelayType.values();
				
				for (int i = 0; i < delayTypeValues.length; i++)
				{
					if (i != (delayTypeValues.length - 1))
					{
						builder.append(delayTypeValues[i].toString() + ", ");
					}
					else
					{
						builder.append(delayTypeValues[i].toString());
					}
				}	
				sender.sendMessage(MessageHelper.PREFIX_HUFF + "Die Benachrichtigungs-Art ist ungültig. Mögliche Werte §9\"" + builder.toString() + "\"§7.");
				return false;
			}
			
			if (targetPlayer == null)
			{
				sender.sendMessage(MessageHelper.getPlayerNotFound(args[0]));
				return false;
			}

			for (int i = 3; i < args.length; i++)
			{
				builder.append(args[i] + " ");
			}
			
			if (builder.length() > 0)
			{
				builder.deleteCharAt(builder.length() -1);
			}
			delayedMessageManager.addDelayedMessage(targetPlayer.getUniqueId(), delayType, messageType, builder.toString());
			return true;
		}
		sender.sendMessage(MessageHelper.getWrongInput("/delayedmessage [Nachrichten-Kategorie] [Benachrichtigungs-Art] [Spieler] <Nachricht>"));
		return false;
	}
}
