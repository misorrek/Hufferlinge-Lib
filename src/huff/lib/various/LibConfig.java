package huff.lib.various;

import org.bukkit.util.BoundingBox;

import huff.lib.manager.ConfigManager;
import huff.lib.various.structures.ConfigPair;
import huff.lib.various.structures.Point;

public class LibConfig
{
	private LibConfig() { }
	
	public static final ConfigPair<String> DATABASE_HOST = new ConfigPair<>("lib.database.host", "", String.class);
	public static final ConfigPair<String> DATABASE_PORT = new ConfigPair<>("lib.database.port", "", String.class);
	public static final ConfigPair<String> DATABASE_NAME = new ConfigPair<>("lib.database.name", "", String.class);
	public static final ConfigPair<String> DATABASE_USERNAME = new ConfigPair<>("lib.database.username", "", String.class);
	public static final ConfigPair<String> DATABASE_PASSWORD = new ConfigPair<>("lib.database.password", "", String.class);
	
	public static final ConfigPair<String> REDIS_HOST = new ConfigPair<>("lib.redis.host", "127.0.0.1", String.class);
	public static final ConfigPair<Integer> REDIS_PORT = new ConfigPair<>("lib.redis.port", 6379, Integer.class);
	
	public static final ConfigPair<Boolean> CHAT_DISPLAYLUCKPERMS = new ConfigPair<>("lib.chat.display_luckperms", false, Boolean.class);
	public static final ConfigPair<Boolean> CHAT_DISPLAYWORLD = new ConfigPair<>("lib.chat.display_world", false, Boolean.class);
	
	public static final ConfigPair<Integer> AREACHAT_COOLDOWN = new ConfigPair<>("lib.areachat.global_cooldown", 5, Integer.class);
	public static final ConfigPair<BoundingBox> AREACHAT_RANGE = new ConfigPair<>("lib.areachat.range", new BoundingBox(20, 10, 20, -20, -10, -20), BoundingBox.class);
	
	public static final ConfigPair<Point> ENTITY_FOLLOWLOOKRADIUS = new ConfigPair<>("lib.entity.follow_look_radius", new Point(5, 2.5, 5), Point.class);
	
	public static void init()
	{
		final HuffConfiguration config = new HuffConfiguration();
		
		config.addEmptyLine("lib");
		config.addCommentLine("lib", "+--------------------------------------+ #", false);
		config.addCommentLine("lib", "          // L I B A R Y //              #", false);
		config.addCommentLine("lib", "+--------------------------------------+ #", false);
		config.addEmptyLine("lib");
		
		config.set(DATABASE_HOST);
		config.set(DATABASE_PORT);
		config.set(DATABASE_NAME);
		config.set(DATABASE_USERNAME);
		config.set(DATABASE_PASSWORD);
		
		config.set(REDIS_HOST);
		config.set(REDIS_PORT);
		
		config.set(CHAT_DISPLAYLUCKPERMS);
		config.set(CHAT_DISPLAYWORLD);
		
		config.addCommentLine(AREACHAT_COOLDOWN.getKey(), "Cooldown in seconds", true);
		config.set(AREACHAT_COOLDOWN);
		config.set(AREACHAT_RANGE);
		
		config.set(ENTITY_FOLLOWLOOKRADIUS);
		
		ConfigManager.addDefaults(config);
	}
}
