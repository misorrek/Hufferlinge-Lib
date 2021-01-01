package huff.lib.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.various.HuffCommand;

/**
 * A command class to send messages from the console or a player to another player.
 * Contains the command and tab completion.
 */
public class TellCommand extends HuffCommand
{
	public TellCommand(@NotNull JavaPlugin plugin, @Nullable String consolePrefix)
	{
		super(plugin, "tell");
		
		if (StringUtils.isNotBlank(consolePrefix))
		{
			this.consolePrefix = consolePrefix;
		}
		this.setDescription("Sendet eine Nachricht.");
		this.setUsage("/tell <Name> <Nachricht>");
		this.setAliases("message", "msg", "pm", "dm");
		this.setPermission(PermissionHelper.PERM_ROOT_HUFF + "tell");
		addTabCompletion();
		this.registerCommand();
	}

	private String consolePrefix = MessageHelper.NAME_HUFF_CONSOLE;
	
	// C O M M A N D
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{		
		if (args.length >= 2)
		{
			final Player target = Bukkit.getPlayer(args[0]);

			if (target != null)
			{
				final StringBuilder builder = new StringBuilder();

				for (int i = 1; i < args.length; i++)
				{
					builder.append(args[i] + " ");
				}
				sender.sendMessage("§8☰ §dTell §8| §7An §9" + target.getName() + " §8» §7" + builder.toString());
				target.sendMessage("§8☰ §dTell §8| §7Von §9" + getSenderName(sender) + " §8» §7" + builder.toString());
			} 
			else
			{
				sender.sendMessage(MessageHelper.getPlayerNotFound(args[0]));
			} 
			return true;
		}
		return false;
	}
	
	private String getSenderName(CommandSender sender)
	{
		return (sender instanceof Player) ? sender.getName() : consolePrefix;
	}
	
	// T A B C O M P L E T I O N

	private void addTabCompletion()
	{
		this.addTabCompletion(0, Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.toArray(String[]::new));
		this.addTabCompletion(1, "<Nachricht>");
	}
}
