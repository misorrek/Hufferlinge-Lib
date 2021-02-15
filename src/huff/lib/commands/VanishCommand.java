package huff.lib.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.various.HuffCommand;
import huff.lib.various.LibMessage;
import huff.lib.various.structures.StringPair;

/**
 * A command class to vanish its own or another player.
 * Contains the command, tab completion and a listener for the player join event.
 */
public class VanishCommand extends HuffCommand implements Listener
{
	public VanishCommand(@NotNull JavaPlugin plugin)
	{
		super(plugin, "vanish");
		
		this.plugin = plugin;
		super.setDescription("Versteck einen Spieler");
		super.setUsage("/vanish (<Spieler>)");
		super.setPermission(PermissionHelper.PERM_ROOT_HUFF + "vanish");
		super.setAliases("hide", "v");
		addTabCompletion();
		super.registerCommand();
	}

	private final JavaPlugin plugin;
	private final HashSet<UUID> vanishedPlayer = new HashSet<>();
	
	// C O M M A N D
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{ 
		Player player = (Player) sender;
		
		if (args.length == 1)
		{
			final Player targetPlayer = Bukkit.getPlayer(args[0]); 
			
			if (targetPlayer == null || !targetPlayer.isOnline())
			{
				MessageHelper.getPlayerNotFound(args[0]);
				return true;
			}
			
			if (vanishedPlayer.contains(player.getUniqueId()))
			{
				removeVanish(targetPlayer);
				
				player.sendMessage(LibMessage.VANISH_OTHER_OFF.getMessage(new StringPair("user", args[0])));
				
			}
			else
			{
				addVanish(targetPlayer);
				player.sendMessage(LibMessage.VANISH_OTHER_ON.getMessage(new StringPair("user", args[0])));
			}
		}
		else
		{
			if (vanishedPlayer.contains(player.getUniqueId()))
			{
				removeVanish(player);
			}
			else
			{
				addVanish(player);
			}
		}
		return true;
	}
	
	private void removeVanish(Player player)
	{
		vanishedPlayer.remove(player.getUniqueId());
		
		for (Player publicPlayer : Bukkit.getOnlinePlayers())
		{
			publicPlayer.showPlayer(plugin, player);
		}
		player.sendMessage(LibMessage.VANISH_SELF_OFF.getMessage());
	}
	
	private void addVanish(Player player)
	{
		vanishedPlayer.add(player.getUniqueId());
		
		for (Player publicPlayer : Bukkit.getOnlinePlayers())
		{
			publicPlayer.hidePlayer(plugin, player);
		}
		player.sendMessage(LibMessage.VANISH_SELF_ON.getMessage());
	}
	
	// T A B C O M P L E T I O N

	private void addTabCompletion()
	{
		super.addTabCompletion(0, Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.toArray(String[]::new));
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
				player.sendMessage(LibMessage.VANISH_SELF_ONREMINDER.getMessage());
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
