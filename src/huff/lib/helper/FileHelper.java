package huff.lib.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FileHelper
{
	private FileHelper() { }
	
	public static @Nullable YamlConfiguration loadYamlConfigurationFromFile(@NotNull String path, @Nullable String header, @Nullable Map<String, Object> defaults)
	{
		Validate.notNull((Object) path, "The yaml-file-path cannot be null.");
		
		final File configFile = loadFile(path);
		
		if (configFile == null)
		{
			return null;
		}		
		final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
		
		if (configuration.options().header() == null || configuration.options().header().isEmpty())
		{
			configuration.options().header(StringHelper.isNotNullOrWhitespace(header) ? header : "Created by Hufferlinge Config Manager");
		}		
		
		if (configuration.getDefaults() == null && defaults != null)
		{
			configuration.addDefaults(defaults);
		}	
		
		try
		{
			configuration.options().copyHeader(true);
			configuration.options().copyDefaults(true);
			configuration.save(configFile);
		} 
		catch (IOException exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("The config-file \"%s\" could not be saved.", configFile.getAbsolutePath()), exception);
		}
		return configuration;
	}
	
	public static @Nullable Object readConfigValue(@NotNull YamlConfiguration config, @NotNull String path)
	{
		Validate.notNull((Object) config, "The yaml-configuration cannot be null.");
		Validate.notNull((Object) path, "The config-value-path cannot be null.");
		
		Object configValue = config.get(path);
		
		if (configValue == null && config.getDefaults() != null)
		{
			configValue = config.getDefaults().get(path);
		}
		Validate.notNull(configValue, "The config-value at \"" + path + "\" was null.");
		return configValue;
	}
	
	public static @Nullable JSONObject loadJsonObjectFromFile(@NotNull String path)
	{
		Validate.notNull((Object) path, "The json-file-path cannot be null.");
		
		final File jsonFile = loadFile(path);
		
		if (jsonFile == null)
		{
			return null;
		}
		
		try
		{
			return (JSONObject) new JSONParser().parse(new FileReader(jsonFile));
		}
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot create json-object from file \"%s\".", jsonFile.getAbsolutePath()), exception);
		}
		return null;
	}
	
	public static void saveJsonObjectToFile(@NotNull String path, @NotNull JSONObject jsonObject)
	{
		Validate.notNull((Object) path, "The json-file-path cannot be null.");
		Validate.notNull((Object) path, "The json-object cannot be null.");
		
		final File jsonFile = loadFile(path);
		
		if (jsonFile == null)
		{
			return;
		}		
			
		try
		{
			PrintWriter fileWriter = null;
			
			fileWriter = new PrintWriter(jsonFile);
			
			fileWriter.write(jsonObject.toJSONString());
			fileWriter.flush();
			fileWriter.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static @Nullable File loadFile(@NotNull String path)
	{
		Validate.notNull((Object) path, "The file-path cannot be null.");
		
		final File file = new File(path);
		
		if (!createFileAndParents(file))
		{
			Bukkit.getLogger().log(Level.SEVERE, "File \"{0}\" cannot be loaded.", path);
			return null;
		}
		return file;
	}
	
	public static boolean createFileAndParents(@NotNull File file)
	{
		Validate.notNull((Object) file, "The file cannot be null.");
		
		if(!file.exists())
		{
			final File parentFile = file.getParentFile();
			
			if (parentFile != null)
			{
				parentFile.mkdirs();
			}
			
			try
			{
				return file.createNewFile();
			} 
			catch (IOException exception)
			{
				Bukkit.getLogger().log(Level.SEVERE, String.format("The file \"%s\" could not be created.", file.getAbsolutePath()), exception);
			}
		}
		return true;
	}
}
