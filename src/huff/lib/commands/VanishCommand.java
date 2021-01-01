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
		this.setDescription("Versteck einen Spieler");
		this.setUsage("/vanish (<Spieler>)");
		this.setPermission(PermissionHelper.PERM_ROOT_HUFF + "vanish");
		this.setAliases("hide", "v");
		addTabCompletion();
		this.registerCommand();
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
				player.sendMessage(MessageHelper.PREFIX_HUFF + "Du hast den Spieler" + MessageHelper.getQuoted(args[0]) + "wieder sichtbar gemacht.");
				
			}
			else
			{
				addVanish(targetPlayer);
				player.sendMessage(MessageHelper.PREFIX_HUFF + "Du hast den Spieler" + MessageHelper.getQuoted(args[0]) + "verschollen.");
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

	private void addTabCompletion()
	{
		this.addTabCompletion(0, Bukkit.getOnlinePlayers().stream()
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
