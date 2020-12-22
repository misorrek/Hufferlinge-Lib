package huff.lib.helper;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class containing static data processing methods.
 * Covers for example data converting or parsing.
 */
public class DataHelper
{
	private static final String PATTERN_STRINGLOC = "%s#%f#%f#%f#%f#%f";
	private static final String REGEX_STRINGLOC = "(.*)#([0-9.-]*)#([0-9.-]*)#([0-9.-]*)#([0-9.-]*)#([0-9.-]*)";
	
	private DataHelper() { }
	
	/**
	 * Converting a "org.bukkit.Location" to a String with the pattern "%s#%f#%f#%f#%f#%f".
	 * %s : world
	 * %f : x, y, z, yaw, pitch
	 * 
	 * @param   location   a not null "org.bukkit.Location"
	 * @return             the location as string with the pattern described
	 */
	public static @NotNull String convertLocationToString(@NotNull Location location)
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		Validate.notNull((Object) location.getWorld(), "The location-world cannot be null.");
		
		return String.format(PATTERN_STRINGLOC, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	/**
	 * Converting a string with the pattern "%s#%f#%f#%f#%f#%f" to a "org.bukkit.Location".
	 * %s : world
	 * %f : x, y, z, yaw, pitch
	 * 
	 * @param   locationString   a string with the pattern described
	 * @return                   the "org.bukkit.Location" contained in the string - returns null if cannot be parsed
	 */
	public static @Nullable Location convertStringtoLocation(@NotNull String locationString)
	{
		Validate.notNull((Object) locationString, "The location-string cannot be null.");
		
		final Pattern valuePattern = Pattern.compile(REGEX_STRINGLOC);
		final Matcher matcher = valuePattern.matcher(locationString);
		
		if (matcher.find())
		{
			final World locationWorld = Bukkit.getWorld(matcher.group(1));

			if (locationWorld != null)
			{
				return new Location(locationWorld, 
			            parseDouble(matcher.group(2), 0), 
			            parseDouble(matcher.group(3), 0),
			            parseDouble(matcher.group(4), 0),
			            (float) parseDouble(matcher.group(5), 0),
			            (float) parseDouble(matcher.group(6), 0));
			}							
		}
		return null;
	}

	/**
	 * Parsing a double value from a string with standard java method. 
	 * If it cannot be parsed, error gets directly logged and the given standard value returned
	 *
	 * @param   value   	    a string that represents a double value
	 * @param   standardValue   a standard value returned if a error occurs
	 * @return           	    the parsed double value - in case of error the standard value
	 */	
	public static double parseDouble(@Nullable String value, double standardValue)
	{
		if (value != null)
		{
			try
			{
				return Double.parseDouble(value);
			}
			catch (NumberFormatException exception)
			{
				Bukkit.getLogger().log(Level.SEVERE, "The double-value cannot be parsed.", exception);
			}
		}		
		return standardValue;
	}
}
