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

public class DataHelper
{
	private static final String PATTERN_STRINGLOC = "%s#%f#%f#%f#%f#%f";
	private static final String REGEX_STRINGLOC = "(.*)#([0-9.-]*)#([0-9.-]*)#([0-9.-]*)#([0-9.-]*)#([0-9.-]*)";
	
	private DataHelper() { }
	
	public static @NotNull String convertLocationToString(@NotNull Location location)
	{
		Validate.notNull((Object) location, "The location cannot be null.");
		Validate.notNull((Object) location.getWorld(), "The location-world cannot be null.");
		
		return String.format(PATTERN_STRINGLOC, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	public static @Nullable Location convertStringtoLocation(@NotNull String locationString)
	{
		Validate.notNull((Object) locationString, "The location-string cannot be null.");
		
		final Pattern valuePattern = Pattern.compile(REGEX_STRINGLOC);
		final Matcher matcher = valuePattern.matcher(locationString);
		
		if (matcher.find())
		{
			final World locationWorld = Bukkit.getWorld(matcher.group(1));

			Bukkit.getConsoleSender().sendMessage(String.format("%s, %s, %s, %s, %s, %s", matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6)));
			
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