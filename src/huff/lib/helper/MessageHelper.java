package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageHelper 
{
	private MessageHelper() { }
	
	public static final String PLACEHOLDER_PLAYER = "%player%";
	public static final String PLACEHOLDER_TEXT = "%text%";
	
	public static final String PREFIX_HUFF = "§8☰ §aHufferlinge §8☷§7 ";
	public static final String PREFIX_HUFF_CONSOLE = "§7Hufferlinge §8☷ §7 ";
	
    public static final String NOPERMISSION = "Du hast keine Berechtigung für diesen Befehl.";
    public static final String PLAYERNOTFOUND = "Der Spieler" + PLACEHOLDER_PLAYER + "konnte nicht zugeordnet werden.";
    public static final String WRONGINPUT = "Die Eingabe ist ungültig." + PLACEHOLDER_TEXT;
    public static final String NORUNINCONSOLE = "Der Befehl kann nicht in der Konsole ausgeführt werden";
    
    public static String getNoPermission() { return PREFIX_HUFF + NOPERMISSION; }
    
    public static String getPlayerNotFound() { return PREFIX_HUFF + PLAYERNOTFOUND.replace(PLACEHOLDER_PLAYER, " "); }
    public static String getPlayerNotFound(String player) { return PREFIX_HUFF + PLAYERNOTFOUND.replace(PLACEHOLDER_PLAYER, getHighlighted(player)); }
    
    public static String getWrongInput() { return PREFIX_HUFF + WRONGINPUT.replace(PLACEHOLDER_TEXT, ""); }
    public static String getWrongInput(String text) { return PREFIX_HUFF + WRONGINPUT.replace(PLACEHOLDER_TEXT, " " + text); }
    
    @NotNull
    public static String getHighlighted(@NotNull String content)
    {
    	return getHighlighted(content, true, true);
    }
    
    @NotNull
    public static String getHighlighted(@NotNull String content, boolean spaceLeft, boolean spaceRight)
    {
    	Validate.notNull((Object) content, "The content cannot be null.");
    	
    	return (spaceLeft ? " §9" : "§9") + content + (spaceRight ? " §7" : "§7");
    }
    
    @NotNull
    public static String getQuoted(@NotNull String content)
    {
    	return getQuoted(content, true, true);
    }
    
    @NotNull
    public static String getQuoted(@NotNull String content, boolean spaceLeft, boolean spaceRight)
    {
    	Validate.notNull((Object) content, "The content cannot be null.");
    	
    	return (spaceLeft ? " §9\"" : "§9\"") + content + (spaceRight ? "\"§7 " : "\"§7");
    }
    
    public static void sendConsoleMessage(@NotNull String message)
    {
    	Validate.notNull((Object) message, "The message cannot be null.");
    	
    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('§', PREFIX_HUFF_CONSOLE + message));
    }
    
    public static void sendPermssionMessage(@NotNull String permission, @NotNull String message) 
	{
    	sendPermssionMessage(permission, message, null);
	}
    
    public static void sendPermssionMessage(@NotNull String permission, @NotNull String message, @Nullable String excludedPlayer) 
	{
    	Validate.notNull((Object) message, "The permission cannot be null.");
    	Validate.notNull((Object) message, "The message cannot be null.");
    	
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			if ((StringHelper.isNullOrEmpty(excludedPlayer) || !player.getName().equals(excludedPlayer)) && PermissionHelper.hasPlayerPermission(player, permission)) 
			{
				player.sendMessage(message);
			}
		}
	}
}
