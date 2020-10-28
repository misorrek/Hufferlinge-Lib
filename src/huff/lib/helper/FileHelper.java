package huff.lib.helper;

import java.io.BufferedReader;
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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileHelper
{
	private FileHelper() { }
	
	// Y A M L
	
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
	
	public static @NotNull Object readConfigValue(@NotNull YamlConfiguration config, @NotNull String path)
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
	
	// J S O N
	
	public static @Nullable JsonObject loadJsonObjectFromFile(@NotNull String path)
	{
		Validate.notNull((Object) path, "The json-file-path cannot be null.");
		
		final File jsonFile = loadFile(path);
		
		if (jsonFile == null)
		{
			return null;
		}		
		final String jsonFileContent = readFileContents(jsonFile);
		JsonObject jsonObject = null;
		
		if (StringHelper.isNotNullOrWhitespace(jsonFileContent))
		{
			try
			{
				jsonObject = JsonParser.parseString(jsonFileContent).getAsJsonObject();
			}
			catch (Exception exception)
			{
				Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot parse json-object from file \"%s\".", jsonFile.getAbsolutePath()), exception);
			}
		}
		
		if (jsonObject == null)
		{
			jsonObject = new JsonObject();
		}		
		return jsonObject;
	}
	
	public static void saveJsonObjectToFile(@NotNull String path, @NotNull JsonElement jsonElement)
	{
		Validate.notNull((Object) path, "The json-file-path cannot be null.");
		Validate.notNull((Object) path, "The json-object cannot be null.");
		
		final File jsonFile = loadFile(path);
		
		Validate.notNull((Object) jsonFile, String.format("Cannot save json-element into json-file at \"%s\".", path));	
			
		try
		{
			PrintWriter fileWriter = new PrintWriter(jsonFile);
			
			fileWriter.write(jsonElement.getAsString());
			fileWriter.flush();
			fileWriter.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	// G E N E R A L
	
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
	
	public static @NotNull String readFileContents(@NotNull File file)
	{
		Validate.notNull((Object) file, "The file cannot be null.");
		
		try (final BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			final StringBuilder stringBuilder = new StringBuilder();
			
			final String ls = System.getProperty("line.separator");
			String line = null;
			
			while ((line = reader.readLine()) != null) 
			{
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1); // delete the last new line separator
			
			return stringBuilder.toString();
		}
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot read from file \"%s\".", file.getAbsolutePath()), exception);
		}
		return "";
	}
}
