package huff.lib.various.structures;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

/**
 * A data class that stores a point from a three dimensional coordinate system.
 * Also (de-)serializable by configuration.
 */
@SerializableAs(value = "Point")
public class Point implements ConfigurationSerializable
{
	public Point(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	private final double x;
	private final double y;
	private final double z;

	@NotNull
	public double getX()
	{
		return x;
	}
	
	@NotNull
	public double getY()
	{
		return y;
	}
	
	@NotNull
	public double getZ()
	{
		return z;
	}

	@Override
	@NotNull
	public Map<String, Object> serialize()
	{
		LinkedHashMap<String, Object> result = new LinkedHashMap<>();
		
		result.put("x", this.x);
		result.put("y", this.y);
		result.put("z", this.z);
		
		return result;
	}
	
	@NotNull
	public static Point deserialize(@NotNull Map<String, Object> args) {
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		
		if (args.containsKey("x")) {
			x = ((Number) args.get("x")).doubleValue();
		}
		if (args.containsKey("y")) {
			y = ((Number) args.get("y")).doubleValue();
		}
		if (args.containsKey("z")) {
			z = ((Number) args.get("z")).doubleValue();
		}
		return new Point(x, y, z);
	}
}
