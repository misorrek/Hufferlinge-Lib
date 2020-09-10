package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.query.QueryOptions;

public class PermissionHelper 
{
	private PermissionHelper() { }
	
	public static final String PERM_ROOT_HUFF = "huff.";
    public static final String PERM_ALL =  PERM_ROOT_HUFF + "*";
	
	public static boolean hasPlayerPermission(@NotNull Player player, @NotNull String permission) 
	{			
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) permission, "The permission cannot be null.");
		
		return player.isPermissionSet(permission) || player.isPermissionSet(PERM_ALL);
	}
	
	public static boolean hasPlayerPermissionFeedbacked(@NotNull Player player, @NotNull String permission) 
	{	
		if(!hasPlayerPermission(player, permission)) 
		{
			player.sendMessage(MessageHelper.getNoPermission());			
			return false;
		}		
		return true;
	}
	
	// L U C K P E R M S
		
	public static @Nullable LuckPerms getLuckPerms()
	{
		RegisteredServiceProvider<LuckPerms> luckyPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		 
		return luckyPermsProvider != null ? luckyPermsProvider.getProvider() : null;
	}
		
	public static @NotNull QueryOptions getPlayerQueryOptions(@NotNull LuckPerms luckPerms, @NotNull Player player)
	{
		Validate.notNull((Object) luckPerms, "The luckperms-api cannot be null.");
		Validate.notNull((Object) player, "The player cannot be null.");
		
		return luckPerms.getContextManager().getQueryOptions(player);
	}
		
	public static @Nullable String getPrimaryPlayerPrefix(@NotNull LuckPerms luckPerms, @NotNull Player player)
	{
		Validate.notNull((Object) luckPerms, "The luckperms-api cannot be null.");
		Validate.notNull((Object) player, "The player cannot be null.");
		
		return luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData()
			            .getMetaData(getPlayerQueryOptions(luckPerms, player)).getPrefix();
	}
} 
