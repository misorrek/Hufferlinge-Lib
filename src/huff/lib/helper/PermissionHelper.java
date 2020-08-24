package huff.lib.helper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.query.QueryOptions;

public class PermissionHelper 
{
	private PermissionHelper() { }
	
	public static final String PERM_ROOT_HUFF = "huff.";
    public static final String PERM_ALL =  PERM_ROOT_HUFF + "*";
	
	public static boolean hasPlayerPermission(Player player, String permission) 
	{			
		return player.isPermissionSet(permission) || player.isPermissionSet(PERM_ALL);
	}
	
	public static boolean hasPlayerPermissionFeedbacked(Player player, String permission) 
	{	
		if(!hasPlayerPermission(player, permission)) 
		{
			player.sendMessage(MessageHelper.getNoPermission());			
			return false;
		}		
		return true;
	}
	
	// L U C K P E R M S
	
	public static LuckPerms getLuckPerms()
	{
		RegisteredServiceProvider<LuckPerms> luckyPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		 
		return luckyPermsProvider != null ? luckyPermsProvider.getProvider() : null;
	}
	
	public static QueryOptions getPlayerQueryOptions(LuckPerms luckPerms, Player player)
	{
		return luckPerms.getContextManager().getQueryOptions(player);
	}
	
	public static String getPrimaryPlayerPrefix(LuckPerms luckPerms, Player player)
	{
		return luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData()
			            .getMetaData(getPlayerQueryOptions(luckPerms, player)).getPrefix();
	}
} 
