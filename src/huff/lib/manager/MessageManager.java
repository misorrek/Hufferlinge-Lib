package huff.lib.manager;

import java.nio.file.Paths;

import org.jetbrains.annotations.NotNull;

import huff.lib.helper.FileHelper;
import huff.lib.various.Constants;
import huff.lib.various.HuffConfiguration;

/**
 * A manager class that creates, loads and hold the persistent message file.
 */
public class MessageManager
{
	private static final String HEADER = 
			  "######################################## #\n"
			+ "+--------------------------------------+ #\n"
			+ "|                                      | #\n"
			+ "|      // H U F F E R L I N G E //     | #\n"
			+ "|         // M E S S A G E //          | #\n"
			+ "|                                      | #\n"
			+ "|--------------------------------------| #\n"
			+ "|                                      | #\n"
			+ "|  &  = Color Code                     | #\n"
			+ "| { } = Content Link                   | #\n"
			+ "| % % = Context Link                   | #\n"
			+ "|                                      | #\n"
			+ "+--------------------------------------+ #\n"
			+ "######################################## #";
	
	private static final String PATH = Paths.get(Constants.LIB_FOLDER, "message.yml").toString();
	
	public static final HuffConfiguration MESSAGE = (HuffConfiguration) FileHelper.loadYamlConfigurationFromFile(PATH, HEADER, null);
	
	private MessageManager() { }
	
	/**
	 * Adds the values from the given configuration as default values to the loaded message file.
	 * 
	 * @param   configuration   the configuration containing the default values
	 */
	public static void addDefaults(@NotNull HuffConfiguration configuration)
	{
		MESSAGE.addDefaults(configuration, PATH);
	}
}
