package huff.lib.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.various.AlphanumericComparator;

/**
 * A command class to vanish its own or another player.
 * Contains the command, tab completion and a listener for the player join event.
 */
public class VanishCommand implements CommandExecutor, TabCompleter, Listener
{
	private static final String PERM_VANISH =  PermissionHelper.PERM_ROOT_HUFF + "vanish";
	
	public VanishCommand(@NotNull JavaPlugin plugin)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		
		this.plugin = plugin;
	}
	
	private final JavaPlugin plugin;
	private final HashSet<UUID> vanishedPlayer = new HashSet<>();
	
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
		
		if (PermissionHelper.hasPlayerPermissionFeedbacked(player, PERM_VANISH)) 
		{
			if (args.length == 1)
			{
				final Player targetPlayer = Bukkit.getPlayer(args[0]); 
				
				if (targetPlayer == null || !targetPlayer.isOnline())
				{
					MessageHelper.getPlayerNotFound(args[0]);
					return false;
				}
				
				if (vanishedPlayer.contains(player.getUniqueId()))
				{
					removeVanish(targetPlayer);
					player.sendMessage(MessageHelper.PREFIX_HUFF + "Du hast den Spieler" + MessageHelper.getQuoted(args[0]) + "wieder sichtbar gemacht.");
					return true;
				}
				else
				{
					addVanish(targetPlayer);
					player.sendMessage(MessageHelper.PREFIX_HUFF + "Du hast den Spieler" + MessageHelper.getQuoted(args[0]) + "verschollen.");
					return true;
				}
			}
			else
			{
				if (vanishedPlayer.contains(player.getUniqueId()))
				{
					removeVanish(player);
					return true;
				}
				else
				{
					addVanish(player);
					return true;
				}
			}
		}	
		return false;
	}
	
	private void removeVanish(Player player)
	{
		vanishedPlayer.remove(player.getUniqueId());
		
		for (Player publicPlayer : Bukkit.getOnlinePlayers())
		{
			publicPlayer.showPlayer(plugin, player);
		}
		player.sendMessage(MessageHelper.PREFIX_HUFF + "Du bist nun wieder sichtbar.");
	}
	
	private void addVanish(Player player)
	{
		vanishedPlayer.add(player.getUniqueId());
		
		for (Player publicPlayer : Bukkit.getOnlinePlayers())
		{
			publicPlayer.hidePlayer(plugin, player);
		}
		player.sendMessage(MessageHelper.PREFIX_HUFF + "Du bist nun verschollen.");
	}
	
	// T A B C O M P L E T I O N
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) 
	{
		final List<String> paramSuggestions = new ArrayList<>();
		
		if (!(sender instanceof Player) || !PermissionHelper.hasPlayerPermission((Player) sender, PERM_VANISH)) 
		{
			return paramSuggestions;
		}	

		if (args.length == 1)
		{
			for (Player publicPlayer : Bukkit.getOnlinePlayers())
			{
				paramSuggestions.add(publicPlayer.getName());
			}
		}		
		paramSuggestions.sort(new AlphanumericComparator());
		return paramSuggestions;
	}
	
	// L I S T E N E R
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		
		for (UUID uuid : vanishedPlayer) 
		{
			if(player.getUniqueId().equals(uuid)) 
			{
				for(Player publicPlayer : Bukkit.getOnlinePlayers()) 
				{
					publicPlayer.hidePlayer(plugin, player);
				}
				player.sendMessage(MessageHelper.PREFIX_HUFF + "§cDu bist noch verschollen.");
			} 
			else 
			{
				final Player someVanishedPlayer = Bukkit.getPlayer(uuid);
				
				if(someVanishedPlayer != null && someVanishedPlayer.isOnline()) 
				{
					player.hidePlayer(plugin, someVanishedPlayer);
				}	
			}
		}
	}
}
