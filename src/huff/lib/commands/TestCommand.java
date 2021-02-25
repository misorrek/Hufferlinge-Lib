package huff.lib.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import huff.lib.helper.SignHelper;
import huff.lib.various.HuffCommand;
import huff.lib.various.LibMessage;

/**
 * A command class to test various implementations.
 * Contains the command and tab completion.
 */
public class TestCommand extends HuffCommand
{
	public TestCommand(@NotNull JavaPlugin plugin)
	{
		super(plugin, "hufftest");
		
		super.setDescription("Der Hufferlinge Test Befehl.");
		addTabCompletion();
		super.registerCommand();
	}
	
	// C O M M A N D

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{		
		if (!(sender instanceof Player))
		{
			sender.sendMessage(LibMessage.NOTINCONSOLE.getValue());		
			return false;
		}
		SignHelper.openSignInput((Player) sender, SignHelper.getInputLines("Deine Eingabe", "---"));
		return true;
	}
	
	// T A B C O M P L E T I O N

	private void addTabCompletion()
	{
		//For future tab completion.
	}
}