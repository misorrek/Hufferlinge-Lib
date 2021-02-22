package huff.lib.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.PermissionHelper;
import huff.lib.various.HuffCommand;
import huff.lib.various.LibMessage;
import huff.lib.various.structures.StringPair;

/**
 * A command class to send messages from the console or a player to another player.
 * Contains the command and tab completion.
 */
public class TellCommand extends HuffCommand
{
	public TellCommand(@NotNull JavaPlugin plugin)
	{
		super(plugin, "tell");
		
		super.setDescription("Sendet eine Nachricht.");
		super.setUsage("/tell <Name> <Nachricht>");
		super.setAliases("message", "msg", "pm", "dm");
		super.setPermission(PermissionHelper.PERM_ROOT_HUFF + "tell");
		addTabCompletion();
		super.registerCommand();
	}
	
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
				
				sender.sendMessage(LibMessage.TELL_TOPREFIX.getMessage(new StringPair("user", target.getName()), new StringPair("text", builder.toString())));
				target.sendMessage(LibMessage.TELL_FROMPREFIX.getMessage(new StringPair("user", getSenderName(sender)), new StringPair("text", builder.toString())));
			} 
			else
			{
				sender.sendMessage(LibMessage.NOTFOUND.getMessage(new StringPair("user", args[0])));
			} 
			return true;
		}
		return false;
	}
	
	@NotNull
	private String getSenderName(CommandSender sender)
	{
		return (sender instanceof Player) ? sender.getName() : LibMessage.TITLE_CONSOLE.getMessage();
	}
	
	// T A B C O M P L E T I O N

	private void addTabCompletion()
	{
		super.addTabCompletion(0, Bukkit.getOnlinePlayers().stream()
				.map(Player::getName)
				.toArray(String[]::new));
		super.addTabCompletion(1, "<Nachricht>");
	}
}
