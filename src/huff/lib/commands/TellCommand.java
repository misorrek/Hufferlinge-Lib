package huff.lib.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.MessageHelper;
import huff.lib.helper.PermissionHelper;
import huff.lib.helper.StringHelper;
import huff.lib.various.AlphanumericComparator;

public class TellCommand implements CommandExecutor, TabCompleter
{
	private static final String PERM_TELL =  PermissionHelper.PERM_ROOT_HUFF + "tell";
	
	public TellCommand(@Nullable String consolePrefix)
	{
		if (StringHelper.isNotNullOrEmpty(consolePrefix))
		{
			this.consolePrefix = consolePrefix;
		}
	}
	
	private String consolePrefix = "§lKonsole";
	
	// C O M M A N D
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player && !PermissionHelper.hasPlayerPermissionFeedbacked((Player) sender, PERM_TELL))		
		{
			return false;
		}
		
		if (args.length >= 2)
		{
			Player target = Bukkit.getPlayer(args[0]);

			if (target != null)
			{
				StringBuilder builder = new StringBuilder();

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
		}
		sender.sendMessage(MessageHelper.getWrongInput("/" + cmd.getName() + " <Name> <Nachricht>"));
		return false;
	}
	
	private String getSenderName(CommandSender sender)
	{
		return (sender instanceof Player) ? sender.getName() : consolePrefix;
	}
	
	// T A B C O M P L E T I O N
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) 
	{
		List<String> paramSuggestions = new ArrayList<>();
		
		if (sender instanceof Player && !PermissionHelper.hasPlayerPermissionFeedbacked((Player) sender, PERM_TELL))		
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
}
