package huff.lib.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import huff.lib.various.HuffConfiguration;

/**
 * A helper class containing static file IO methods.
 * Covers general files, yaml and json.
 */
public class FileHelper
{
	private FileHelper() { }
	
	// Y A M L
	
	/**
	 * Tries to load a YamlConfiguration from the file at the given path.
	 * Are the header and/or the default values of the YamlConfiguration empty, the given values are used.
	 * 
	 * @param   path       the path to the file including filename
	 * @param   header     the optional header for the case of an empty file
	 * @param   defaults   the optional default values for the case of an empty file
	 * @returns            The loaded YamlConfiguration.
	 */
	@Nullable
	public static YamlConfiguration loadYamlConfigurationFromFile(@NotNull String path, @Nullable String header, @Nullable Map<String, Object> defaults)
	{
		Validate.notNull((Object) path, "The yaml-file-path cannot be null.");
		
		final File configFile = loadFile(path);
		
		if (configFile == null)
		{
			return null;
		}		
		final HuffConfiguration configuration = HuffConfiguration.loadConfiguration(configFile);
		
		if (configuration.options().header() == null || configuration.options().header().isEmpty())
		{
			configuration.options().header(StringUtils.isNotBlank(header) ? header : "Created by Hufferlinge Config Manager");
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
	
	/**
	 * Tries to read the value at the given path in the given YamlConfiguration.
	 * 
	 * @param   config   the YamlConfiguration to read
	 * @param   path     the path of the target value
	 * @returns          The read value.
	 */
	@NotNull
	public static Object readConfigValue(@NotNull YamlConfiguration config, @NotNull String path)
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
	
	/**
	 * Tries to load a json object as the specified class from the file at the given path.
	 * 
	 * @param   path       	the path to the file including filename
	 * @param   jsonClass   the class of json object to be loaded
	 * @returns            	The loaded object representing a json structure.
	 */
	@Nullable
	public static Object loadJsonObjectFromFile(@NotNull String path, @NotNull Class<?> jsonClass)
	{
		Validate.notNull((Object) path, "The json-file-path cannot be null.");
		
		final File jsonFile = loadFile(path);
		
		if (jsonFile == null)
		{
			return null;
		}		
		final String jsonFileContent = readFileContents(jsonFile);
		
		if (StringUtils.isNotBlank(jsonFileContent))
		{
			try
			{
				return new Gson().fromJson(jsonFileContent, jsonClass);
			}
			catch (Exception exception)
			{
				Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot create json-objects from file \"%s\".", jsonFile.getAbsolutePath()), exception);
			}
		}
		return null;
	}
	
	/**
	 * Tries to save a json object as the specified class to the file at the given path.
	 * If the file doesn't exist it will tried to create a new one.
	 * 
	 * @param   path       	 the path to the file including filename
	 * @param   jsonObject   the json object to save
	 * @param   jsonClass    the class of json object to be loaded
	 * @returns            	 The loaded object representing a json structure.
	 */
	public static void saveJsonObjectToFile(@NotNull String path, @NotNull Object jsonObject, @NotNull Class<?> jsonClass)
	{
		Validate.notNull((Object) path, "The json-file-path cannot be null.");
		Validate.notNull((Object) path, "The json-object cannot be null.");
		
		final File jsonFile = loadFile(path);
		
		Validate.notNull((Object) jsonFile, String.format("Cannot save json-element into json-file at \"%s\".", path));	
			
		try (final PrintWriter fileWriter = new PrintWriter(jsonFile))
		{	
			fileWriter.write(new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject, jsonClass));
			fileWriter.flush();
		} 
		catch (FileNotFoundException exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot save json-object to file \"%s\".", jsonFile.getAbsolutePath()), exception);
		}
	}
	
	// G E N E R A L
	
	/**
	 * Tries to create a new file at the file's path.
	 * If the path contains parent elements that not exist, they will be created.
	 * 
	 * @param   file   the file object containing the target path
	 * @returns        A boolean if the creation was successful.
	 */
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
	
	/**
	 * Tries to load a file object from the given path.
	 * If the file doesn't exist it will tried to create a new one.
	 * 
	 * @param   path   the path to the target file
	 * @returns        The loaded file object.
	 */
	@Nullable
	public static File loadFile(@NotNull String path)
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
	
	/**
	 * Reads all contents from a given file.
	 * If the file doesn't exist a empty string will be returned.
	 * 
	 * @param   file   the file object to read from
	 * @returns        The read contents as a string.
	 */
	@NotNull
	public static String readFileContents(@NotNull File file)
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
			
			if (stringBuilder.length() > 0)
			{
				stringBuilder.deleteCharAt(stringBuilder.length() - 1); // delete the last new line separator
			}
			return stringBuilder.toString();
		}
		catch (Exception exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, String.format("Cannot read from file \"%s\".", file.getAbsolutePath()), exception);
		}
		return "";
	}
}
