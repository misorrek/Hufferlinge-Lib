package huff.lib.helper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	@Nullable
	public YamlConfiguration loadYamlConfigurationFromFile(@NotNull String path, @Nullable String header, @Nullable Map<String, Object> defaults)
	{
		Validate.notNull((Object) path, "The yaml-file-path cannot be null.");
		File configFile = loadFile(path);
		
		if (configFile == null)
		{
			return null;
		}
		
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
		
		if (configuration.options().header().isEmpty())
		{
			configuration.options().header(StringHelper.isNotNullOrWhitespace(header) ? header : "# Created by Hufferlinge Config Manager");
		}		
		
		if (configuration.getDefaults() == null && defaults != null)
		{
			configuration.addDefaults(defaults);
		}	
		return configuration;
	}
	
	@Nullable
	public JSONObject loadJsonObject(@NotNull File jsonFile)
	{
		Validate.notNull((Object) jsonFile, "The json-file cannot be null.");
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
	
	@Nullable
	public File loadFile(@NotNull String path)
	{
		Validate.notNull((Object) path, "The file-path cannot be null.");
		File file = new File(path);
		
		if (!createFileAndParents(file))
		{
			Bukkit.getLogger().log(Level.SEVERE, "File \"{0}\" cannot be loaded.", path);
			return null;
		}
		return file;
	}
	
	public boolean createFileAndParents(@NotNull File file)
	{
		Validate.notNull((Object) file, "The file cannot be null.");
		
		if(!file.exists())
		{
			File parentFile = file.getParentFile();
			
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
