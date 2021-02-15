package huff.lib.manager;

import java.nio.file.Paths;

import huff.lib.helper.FileHelper;
import huff.lib.various.Constants;
import huff.lib.various.HuffConfiguration;

public class ConfigManager
{
	private static final String HEADER = 
			  "######################################## #\n"
			+ "+--------------------------------------+ #\n"
			+ "|                                      | #\n"
			+ "|      // H U F F E R L I N G E //     | #\n"
			+ "|          // C O N F I G //           | #\n"
			+ "|                                      | #\n"
			+ "|--------------------------------------| #\n"
			+ "|                                      | #\n"
			+ "|  &  = Color Code                     | #\n"
			+ "| { } = Content Link                   | #\n"
			+ "|                                      | #\n"
			+ "+--------------------------------------+ #\n"
			+ "######################################## #";
	
	public static final HuffConfiguration CONFIG = (HuffConfiguration) FileHelper.loadYamlConfigurationFromFile(
			Paths.get(Constants.LIB_FOLDER, "config.yml").toString(), 
			HEADER, 
			null);
	
	private ConfigManager() { }
}
