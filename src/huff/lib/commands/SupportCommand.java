package huff.lib.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.various.AlphanumericComparator;

public class SupportCommand implements CommandExecutor, TabCompleter, Listener
{
	private static final String PREFIX_SUPPORT = "§8☰ §eSupport §8☷§7 ";	
	private static final String PERM_SUPPORT =  PermissionHelper.PERM_ROOT_HUFF + "support";
	
	private SupportMap supportMap = new SupportMap();
	
	// C O M M A N D
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{ 
		if (!(sender instanceof Player)) 
		{
			MessageHelper.sendConsoleMessage(MessageHelper.NORUNINCONSOLE);
			return false;
		}	
		Player player = (Player) sender;

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
			player.sendMessage(MessageHelper.getWrongInput("/support [list|leave|enter <Name>|delete <Name>]"));
		}
		else
		{ 
			player.sendMessage(MessageHelper.getWrongInput("/support [leave] -" + MessageHelper.getQuoted("support") + "ohne Zusatz öffnet einen neuen Support-Kanal."));
		}
		return false;
	}
	
	private boolean executeCreate(@NotNull Player player)
	{
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			player.sendMessage(PREFIX_SUPPORT + "Du kannst als Teammitglied keinen Support-Kanal öffnen. Nutze" + MessageHelper.getQuoted("/tell <Name>") + "zur Teamkommunikation.");
			return false;
		}
		
		if (supportMap.containsKey(player.getUniqueId()))
		{
			player.sendMessage(PREFIX_SUPPORT + "Du hast schon einen Support-Kanal geöffnet. Um den Support-Kanal zu schließen nutze" + MessageHelper.getQuoted("/support leave", true, false) + ".");
			return false;
		}
		
		supportMap.add(player.getUniqueId());
		MessageHelper.sendPermssionMessage(PERM_SUPPORT, PREFIX_SUPPORT + MessageHelper.getHighlighted(player.getName())  + "hat einen Support-Kanal geöffnet.");
		player.sendMessage(PREFIX_SUPPORT + "Du hast einen Support-Kanal geöffnet. Alle Nachrichten werden, ohne erneute Befehlseingabe, hierher geschickt.");
		player.sendMessage(PREFIX_SUPPORT + "Um den Support-Kanal zu verlassen nutze" + MessageHelper.getQuoted("/support leave", true, false) + ".");
		return true;
	}
	
	private boolean executeShowList(@NotNull Player player)
	{
		StringBuilder builder = new StringBuilder();
		
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
				player.sendMessage(PREFIX_SUPPORT + "Du bist in keinem Support-Kanal. Um dir die Vorhandenen anzuschauen nutze" + MessageHelper.getQuoted("/support list", true, false) + ".");
				return false;
			}
			else
			{
				sendChannelMessage(supportChat, PREFIX_SUPPORT + "Das Teammitglied" +  MessageHelper.getHighlighted(player.getName()) + "hat den Support-Kanal verlassen.", true);
				player.sendMessage(PREFIX_SUPPORT + "Du hast den Support-Kanal von" + MessageHelper.getHighlighted(Bukkit.getPlayer(supportChat).getName()) + "verlassen.");
				supportMap.get(supportChat).remove(player.getUniqueId());
				return true;
			}							
		}
		else
		{
			if (!supportMap.containsKey(player.getUniqueId()))
			{
				player.sendMessage(PREFIX_SUPPORT + "Du bist in keinem Support-Kanal. Um dir einen zu eröffnen nutze" + MessageHelper.getQuoted("/support", true, false) + ".");
				return false;
			}
			else
			{
				sendChannelMessage(player.getUniqueId(), PREFIX_SUPPORT + MessageHelper.getHighlighted(player.getName()) + "hat den Support-Kanal geschlossen.", false);			
				player.sendMessage(PREFIX_SUPPORT + "Du hast den Support-Kanal geschlossen.");				
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
			try
			{
				player.sendMessage(PREFIX_SUPPORT + "Du bist noch im Support-Chat von §9" + Bukkit.getPlayer(currentSupportChat).getName());
				return false;
			}
			catch (Exception e) { }
		}
		Validate.notNull((Object) targetName, "The player-target-name cannot be null.");
		
		final Player targetPlayer = Bukkit.getPlayer(targetName); 
		
		if (targetPlayer == null || !supportMap.containsKey(targetPlayer.getUniqueId()))
		{
			player.sendMessage(PREFIX_SUPPORT + "Support-Kanal nicht gefunden. Um dir die Vorhandenen anzuschauen nutze" + MessageHelper.getQuoted("/support list", true, false) + ".");
			return false;
		}
		supportMap.get(targetPlayer.getUniqueId()).add(player.getUniqueId());
		
		MessageHelper.sendPermssionMessage(PERM_SUPPORT, PREFIX_SUPPORT + "Das Teammitglied" + MessageHelper.getHighlighted(player.getName()) + "hat den Support-Kanal von" + MessageHelper.getHighlighted(targetName) + "betreten.", player.getName());
		targetPlayer.sendMessage(PREFIX_SUPPORT+ "Das Teammitglied" +  MessageHelper.getHighlighted(player.getName()) + "hat den Support-Kanal betreten.");		
		player.sendMessage(PREFIX_SUPPORT + "Du hast den Support-Kanal von" +  MessageHelper.getHighlighted(targetPlayer.getName()) + "betreten.");
		return true;	
	}
	
	private boolean executeDelete(@NotNull Player player, @NotNull String targetName)
	{
		Validate.notNull((Object) targetName, "The player-target-name cannot be null.");
		
		final Player targetPlayer = Bukkit.getPlayer(targetName); 
		
		if (targetPlayer == null || !supportMap.containsKey(targetPlayer.getUniqueId()))
		{
			player.sendMessage(PREFIX_SUPPORT + "Support-Kanal nicht gefunden. Um dir die Vorhandenen anzuschauen nutze" + MessageHelper.getQuoted("/support list", true, false) + ".");
			return false;
		}
		supportMap.remove(targetPlayer.getUniqueId());	
		
		MessageHelper.sendPermssionMessage(PERM_SUPPORT, PREFIX_SUPPORT + "Das Teammitglied" + MessageHelper.getHighlighted(player.getName()) + "hat den Support-Kanal von" + MessageHelper.getHighlighted(targetName) + "entfernt.", player.getName());
		targetPlayer.sendMessage(PREFIX_SUPPORT + "Dein Support-Kanal wurde entfernt. Bei Rückfragen wende dich bitte über unseren Teamspeak" + MessageHelper.getHighlighted("hufferlinge.de") + "an ein Teammitglied.");				
		player.sendMessage(PREFIX_SUPPORT + "Du hast den Support-Kanal von" + MessageHelper.getHighlighted(targetPlayer.getName()) + "entfernt.");
		return true;
	}
	
	// T A B C O M P L E T I O N
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) 
	{
		List<String> paramSuggestions = new ArrayList<>();
		
		if (!(sender instanceof Player)) 
		{
			return paramSuggestions;
		}	
		Player player = (Player) sender;
	
		if (args.length == 0)
		{
			fillFirstSuggestion(player, paramSuggestions);
		}
		else if (args.length == 2 && (args[0].equalsIgnoreCase("enter") || args[0].equalsIgnoreCase("delete")) && PermissionHelper.hasPlayerPermissionFeedbacked(player, PERM_SUPPORT))
		{
			fillAvailableChatsSuggestion(paramSuggestions);
		}
		paramSuggestions.sort(new AlphanumericComparator());
		return paramSuggestions;
	}
	
	private void fillFirstSuggestion(@NotNull Player player, @NotNull List<String> paramSuggestions)
	{
		Validate.notNull((Object) paramSuggestions, "The param-suggestion-list cannot be null.");
		
		boolean isPlayerInSupport = supportMap.containsKey(player.getUniqueId());
		
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			paramSuggestions.add("list");
			paramSuggestions.add("delete");
			
			if (!isPlayerInSupport)
			{
				paramSuggestions.add("enter");
			}
		}
		
		if (isPlayerInSupport)
		{
			paramSuggestions.add("leave");
		}
	}
	
	private void fillAvailableChatsSuggestion(@NotNull List<String> paramSuggestions)
	{
		Validate.notNull((Object) paramSuggestions, "The param-suggestion-list cannot be null.");
		
		for (UUID availableChat : supportMap.keySet())
		{
			paramSuggestions.add(availableChat.toString());
		}
	}
	
	// L I S T E N E R
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) 
	{
		Player player = event.getPlayer();
		String msg = event.getMessage();
		
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			final UUID targetChat = supportMap.getCurrentSupportChat(player.getUniqueId());
			
			if (targetChat != null)
			{
				sendChannelMessage(targetChat, PREFIX_SUPPORT + "§9" + player.getName() + " §8» §7" + msg, true);
			}
		}
		else if (supportMap.containsKey(player.getUniqueId()))
		{
			sendChannelMessage(player.getUniqueId(), PREFIX_SUPPORT + "§7" + player.getName() + " §8» §7" + msg, true);	
		}
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		if (PermissionHelper.hasPlayerPermission(player, PERM_SUPPORT))
		{
			final UUID targetChat = supportMap.getCurrentSupportChat(player.getUniqueId());
			
			if (targetChat != null)
			{
				sendChannelMessage(targetChat, PREFIX_SUPPORT + "Das Teammitglied" +  MessageHelper.getHighlighted(player.getName()) + "hat den Support-Kanal verlassen.", true);
				supportMap.get(targetChat).remove(player.getUniqueId());
			}
		}
		else if (supportMap.containsKey(player.getUniqueId()))
		{
			sendChannelMessage(player.getUniqueId(), PREFIX_SUPPORT + MessageHelper.getHighlighted(player.getName()) + "hat den Server verlassen. Support-Kanal wurde geschlossen.", false);
			supportMap.remove(player.getUniqueId());
		}
	}
	
	// U T I L
	
	private void sendChannelMessage(@NotNull UUID targetChat, @NotNull String message, boolean sendToUser)
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
