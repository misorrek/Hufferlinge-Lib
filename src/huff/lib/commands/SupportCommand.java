package huff.lib.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.various.HuffCommand;
import huff.lib.various.LibMessage;
import huff.lib.various.structures.StringPair;

/**
 * A command class to create a support chat which can be joined by a supporter.
 * So one or more supporters can chat with the person that needs help.
 * A supporter cannot create a support chat.
 * Contains the command, tab completion and listener for the async player chat and player quit event.
 */
public class SupportCommand extends HuffCommand implements Listener
{
	private static final String PERM_SUPPORT =  PermissionHelper.PERM_ROOT_HUFF + "support";
	private static final String PREFIX_SUPPORT = "§8☰ §eSupport §8☷§7 ";	

	public SupportCommand(@NotNull JavaPlugin plugin)
	{
		super(plugin, "support");

		super.setDescription("Öffnet einen Support-Chat");
		super.setAliases("ticket");
		super.registerCommand();
	}

	private final SupportMap supportMap = new SupportMap();
	
	// C O M M A N D
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{ 
		if (!(sender instanceof Player)) 
		{
			sender.sendMessage(LibMessage.NOTINCONSOLE.getMessage());
			return false;
		}	
		final Player player = (Player) sender;

		if (args.length == 0)
		{
			return executeCreate(player);
		}
		else if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("list") && PermissionHelper.hasPlayerPermissionFeedbacked(player, PERM_SUPPORT))
			{
				return executeShowList(player);
			}
			else if (args[0].equalsIgnoreCase("leave"))
			{
				return executeLeave(player);
			}
		}
		else if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("enter") && PermissionHelper.hasPlayerPermissionFeedbacked(player, PERM_SUPPORT))
			{				
				return executeEnter(player, args[1]);
			}
			else if (args[0].equalsIgnoreCase("delete") && PermissionHelper.hasPlayerPermissionFeedbacked(player, PERM_SUPPORT))
			{
				return executeDelete(player, args[1]);
			}
		}
		
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			player.sendMessage(LibMessage.SUPPORT_TEAM_HELP.getMessage());
		}
		else
		{ 
			player.sendMessage(LibMessage.SUPPORT_USER_HELP.getMessage());
		}
		return false;
	}
	
	private boolean executeCreate(@NotNull Player player)
	{
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			player.sendMessage(LibMessage.SUPPORT_TEAM_CREATE.getMessage());
			return false;
		}
		
		if (supportMap.containsKey(player.getUniqueId()))
		{
			player.sendMessage(LibMessage.SUPPORT_USER_ALREADYOPEN.getMessage());
			return false;
		}
		
		supportMap.add(player.getUniqueId());
		MessageHelper.sendPermssionMessage(PERM_SUPPORT, LibMessage.SUPPORT_TEAM_USERCREATE.getMessage());
		player.sendMessage(LibMessage.SUPPORT_USER_CREATE.getMessage());
		return true;
	}
	
	private boolean executeShowList(@NotNull Player player)
	{
		final StringBuilder builder = new StringBuilder();
		
		builder.append(PREFIX_SUPPORT + "Liste offener Support-Kanäle §8☰");
		
		for (Entry<UUID, HashSet<UUID>> supportEntry : supportMap.entrySet())
		{
			Player targetPlayer = Bukkit.getPlayer(supportEntry.getKey());
			
			if (targetPlayer != null && targetPlayer.isOnline())
			{
				builder.append("\n§8☰ " + targetPlayer.getName() + " §8| §7Helfer: " + supportEntry.getValue().size());
			}
			else
			{									
				supportMap.remove(supportEntry.getKey());
			}
		}					
		player.sendMessage(builder.toString());
		return true;
	}
	
	private boolean executeLeave(@NotNull Player player)
	{
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			final UUID supportChat = supportMap.getCurrentSupportChat(player.getUniqueId());
			
			if (supportChat == null)
			{
				player.sendMessage(LibMessage.SUPPORT_TEAM_CANTLEAVE.getMessage());
				return false;
			}
			else
			{
				sendChannelMessage(supportChat, LibMessage.SUPPORT_CHANNEL_TEAMLEAVE.getMessage(new StringPair("team", player.getName())), true);
				player.sendMessage(LibMessage.SUPPORT_TEAM_LEAVE.getMessage(new StringPair("user", Bukkit.getPlayer(supportChat).getName())));
				supportMap.get(supportChat).remove(player.getUniqueId());
				return true;
			}							
		}
		else
		{
			if (!supportMap.containsKey(player.getUniqueId()))
			{
				player.sendMessage(LibMessage.SUPPORT_USER_CANTLEAVE.getMessage());
				return false;
			}
			else
			{
				sendChannelMessage(player.getUniqueId(), LibMessage.SUPPORT_CHANNEL_USERDELETE.getMessage(new StringPair("user", player.getName())), false);			
				player.sendMessage(LibMessage.SUPPORT_USER_USERDELETE.getMessage());				
				supportMap.remove(player.getUniqueId());
				return true;
			}
		}
	}
	
	private boolean executeEnter(@NotNull Player player, @NotNull String targetName)
	{
		final UUID currentSupportChat = supportMap.getCurrentSupportChat(player.getUniqueId());
		
		if (currentSupportChat != null)
		{
			
			player.sendMessage(LibMessage.SUPPORT_TEAM_ALREADYENTERED.getMessage(new StringPair("user", Bukkit.getOfflinePlayer(currentSupportChat).getName())));
			return false;
		}
		Validate.notNull((Object) targetName, "The player-target-name cannot be null.");
		
		final Player targetPlayer = Bukkit.getPlayer(targetName); 
		
		if (targetPlayer == null || !supportMap.containsKey(targetPlayer.getUniqueId()))
		{
			player.sendMessage(LibMessage.SUPPORT_TEAM_NOTFOUND.getMessage());
			return false;
		}
		supportMap.get(targetPlayer.getUniqueId()).add(player.getUniqueId());
		
		MessageHelper.sendPermssionMessage(PERM_SUPPORT, 
				LibMessage.SUPPORT_TEAM_ENTEROTHER.getMessage(new StringPair("team", player.getName()), new StringPair("user",targetName)), 
				player.getName());
		sendChannelMessage(targetPlayer.getUniqueId(), LibMessage.SUPPORT_CHANNEL_TEAMENTER.getMessage(new StringPair("team", player.getName())), true);
		player.sendMessage(LibMessage.SUPPORT_TEAM_ENTER.getMessage(new StringPair("user", targetName)));
		return true;	
	}
	
	private boolean executeDelete(@NotNull Player player, @NotNull String targetName)
	{
		Validate.notNull((Object) targetName, "The player-target-name cannot be null.");
		
		final Player targetPlayer = Bukkit.getPlayer(targetName); 
		
		if (targetPlayer == null || !supportMap.containsKey(targetPlayer.getUniqueId()))
		{
			player.sendMessage(LibMessage.SUPPORT_TEAM_NOTFOUND.getMessage());
			return false;
		}
		supportMap.remove(targetPlayer.getUniqueId());	
		
		MessageHelper.sendPermssionMessage(PERM_SUPPORT, 
				LibMessage.SUPPORT_CHANNEL_TEAMDELETE.getMessage(new StringPair("team", player.getName()), new StringPair("user", targetName)),
				player.getName());
		targetPlayer.sendMessage(LibMessage.SUPPORT_USER_TEAMDELETE.getMessage());
		player.sendMessage(LibMessage.SUPPORT_TEAM_TEAMDELETE.getMessage(new StringPair("user", targetName)));
		return true;
	}
	
	// T A B C O M P L E T I O N
	
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) 
    {
    	final boolean isPlayerInSupport = sender instanceof Player && supportMap.containsKey(((Player) sender).getUniqueId());
    	
    	if (PermissionHelper.hasPlayerPermission(sender, PERM_SUPPORT))
		{
    		super.addTabCompletion(0, "list", "delete");
			
			if (!isPlayerInSupport)
			{
				super.addTabCompletion(0, "enter");
				super.addTabCompletion(1, PERM_SUPPORT, Stream.of("enter", "delete").toArray(String[]::new), supportMap.keySet().stream()
						.map(uuid -> Bukkit.getOfflinePlayer(uuid).getName())
						.toArray(String[]::new));
			}
		}
		
		if (isPlayerInSupport)
		{
			super.addTabCompletion(0, "leave");
		}	
		return super.tabComplete(sender, alias, args);
    }
	
	// L I S T E N E R
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) 
	{
		final Player player = event.getPlayer();
		final String msg = event.getMessage();
		
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			final UUID targetChat = supportMap.getCurrentSupportChat(player.getUniqueId());
			
			if (targetChat != null)
			{
				sendChannelMessage(targetChat, LibMessage.SUPPORT_CHANNEL_TEAMPREFIX.getMessage(new StringPair("team", player.getName()), new StringPair("text", msg)), true);
			}
		}
		else if (supportMap.containsKey(player.getUniqueId()))
		{
			sendChannelMessage(player.getUniqueId(), LibMessage.SUPPORT_CHANNEL_USERPREFIX.getMessage(new StringPair("user", player.getName()), new StringPair("text", msg)), true);
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		final Player player = event.getPlayer();
		
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			final UUID targetChat = supportMap.getCurrentSupportChat(player.getUniqueId());
			
			if (targetChat != null)
			{
				sendChannelMessage(targetChat, LibMessage.SUPPORT_CHANNEL_TEAMLEAVE.getMessage(new StringPair("team", player.getName())), true);
				supportMap.get(targetChat).remove(player.getUniqueId());
			}
		}
		else if (supportMap.containsKey(player.getUniqueId()))
		{
			sendChannelMessage(player.getUniqueId(), LibMessage.SUPPORT_CHANNEL_USERDISCONNECT.getMessage(new StringPair("user", player.getName())), false);
			supportMap.remove(player.getUniqueId());
		}
	}
	
	// U T I L
	
	private void sendChannelMessage(@NotNull UUID targetChat, String message, boolean sendToUser)
	{
		Validate.notNull((Object) targetChat, "The target-chat cannot be null.");
		
		if (!supportMap.containsKey(targetChat))
		{
			return;
		}	
		Validate.notNull((Object) message, "The chat-message cannot be null.");
		
		if (sendToUser) 
		{
			final Player targetPlayer = Bukkit.getPlayer(targetChat);
			
			if (targetPlayer != null && targetPlayer.isOnline())
			{
				targetPlayer.sendMessage(message);
			}
		}	
		
		for (UUID uuid : supportMap.get(targetChat))
		{
			final Player helferPlayer = Bukkit.getPlayer(uuid);
			
			if (helferPlayer != null && helferPlayer.isOnline())
			{
				helferPlayer.sendMessage(message);
			}
		}	
	}
}
