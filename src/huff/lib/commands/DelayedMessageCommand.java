package huff.lib.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.manager.delayedmessage.DelayType;
import huff.lib.manager.delayedmessage.MessageType;

public class DelayedMessageCommand implements CommandExecutor 
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{	
		if (sender instanceof Player && !PermissionHelper.hasPlayerPermissionFeedbacked((Player) sender, PermissionHelper.PERM_ALL))
		{
			return false;
		}
		
		if (args.length >= 4)
		{
			MessageType messageType = MessageType.valueOf(args[0]);
			DelayType delayType = DelayType.valueOf(args[1]);
			Player targetPlayer = Bukkit.getPlayer(args[0]);
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
				sender.sendMessage(MessageHelper.PREFIX_HUFF + "Die Nachrichten-Kategorie ist ung�ltig. M�gliche Werte �9\"" + builder.toString() + "\"�7.");
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
				sender.sendMessage(MessageHelper.PREFIX_HUFF + "Der Benachrichtigungs-Art ist ung�ltig. M�gliche Werte �9\"" + builder.toString() + "\"�7.");
				return false;
			}
			
			if (targetPlayer == null)
			{
				//sender.sendMessage(Methods.getPlayerNotFound(args[2]));
				return false;
			}

			for (int i = 3; i < args.length; i++)
			{
				builder.append(args[i] + " ");
			}
			//main.getDelayedMessages().addDelayedMessage(targetPlayer.getUniqueId(), delayType, messageType, builder.toString());
			return true;
		}
		//sender.sendMessage(Methods.getWrongInput("/delayedmessage [Nachrichten-Kategorie] [Benachrichtigungs-Art] [Spieler] <Nachricht>"));
		return false;
	}
}
