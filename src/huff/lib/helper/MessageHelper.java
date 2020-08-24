package huff.lib.helper;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
    
    public static String getWrongInput() { return PREFIX_HUFF + PLAYERNOTFOUND.replace(PLACEHOLDER_TEXT, ""); }
    public static String getWrongInput(String text) { return PREFIX_HUFF + PLAYERNOTFOUND.replace(PLACEHOLDER_TEXT, " " + text); }
    
    public static String getHighlighted(String content)
    {
    	return " §9" + content + "§7 ";
    }
    
    public static String getHighlighted(String content, boolean spaceLeft, boolean spaceRight)
    {
    	return (spaceLeft ? " " : "") + getQuoted(content) + (spaceRight ? " " : "");
    }
    
    public static String getQuoted(String content)
    {
    	return " §9\"" + content + "\"§7 ";
    }
    
    public static String getQuoted(String content, boolean spaceLeft, boolean spaceRight)
    {
    	return (spaceLeft ? " " : "") + getQuoted(content) + (spaceRight ? " " : "");
    }
    
    public static void sendConsoleMessage(String mesaage)
    {
    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('§', PREFIX_HUFF_CONSOLE + mesaage));
    }
    
    public static void sendPermssionMessage(String permission, String message) 
	{
    	sendPermssionMessage(permission, message, "");
	}
    
    public static void sendPermssionMessage(String permission, String message, String excludedPlayer) 
	{
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			if ((excludedPlayer.isEmpty() || !player.getName().equals(excludedPlayer)) && PermissionHelper.hasPlayerPermission(player, permission)) 
			{
				player.sendMessage(message);
			}
		}
	}
}
