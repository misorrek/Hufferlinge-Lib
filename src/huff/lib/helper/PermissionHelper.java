package huff.lib.helper;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.query.QueryOptions;

/**
 * A Helper class containing static permisson methods.
 * Covers gerneral permission and LuckPerms. 
 */
public class PermissionHelper 
{
	private PermissionHelper() { }
	
	public static final String PERM_ROOT_HUFF = "huff.";
        public static final String PERM_ALL =  PERM_ROOT_HUFF + "*";
	
	/**
	 * Checks if the player has the given permission.
	 * 
	 * Note: The player does not receive any feedback.
	 * 
	 * @param   player  	 the player to check
	 * @param   permission   the to checking permisson
	 * @return             	 A boolean that gives the check result.
	 */
	public static boolean hasPlayerPermission(@NotNull Player player, @NotNull String permission) 
	{			
		Validate.notNull((Object) player, "The player cannot be null.");
		Validate.notNull((Object) permission, "The permission cannot be null.");
		
		return player.isPermissionSet(permission) || player.isPermissionSet(PERM_ALL);
	}
	
	/**
	 * Checks if the player has the given permission.
	 * If not the player receives feedback. 
	 * 
	 * @param   player  	 the player to check
	 * @param   permission   the to checking permisson
	 * @return             	 A boolean that gives the check result.
	 */
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
	
	/**
	 * Gets the LuckPerms provider from the bukkit ServicesManager.
	 * 
	 * @return   The LuckPerms provider.
	 */
	public static @Nullable LuckPerms getLuckPerms()
	{
		RegisteredServiceProvider<LuckPerms> luckyPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		 
		return luckyPermsProvider != null ? luckyPermsProvider.getProvider() : null;
	}

	/**
	 * Gets the LuckPerms QueryOptions from the given player out of the LuckPerms provider.
	 * 
	 * @param   luckPerms   the LuckPerms provider 
	 * @param   player	the target player
	 * @return              The LuckPerms QueryOptions from the given player.
	 */
	public static @NotNull QueryOptions getPlayerQueryOptions(@NotNull LuckPerms luckPerms, @NotNull Player player)
	{
		Validate.notNull((Object) luckPerms, "The luckperms-provider cannot be null.");
		Validate.notNull((Object) player, "The player cannot be null.");
		
		return luckPerms.getContextManager().getQueryOptions(player);
	}
	
	/**
	 * Gets the LuckPerms primary prefix from the given player out of the LuckPerms provider.
	 * 
	 * @param   luckPerms   the LuckPerms provider 
	 * @param   player	the target player
	 * @return              The LuckPerms primary prefix from the given player.
	 */
	public static @Nullable String getPrimaryPlayerPrefix(@NotNull LuckPerms luckPerms, @NotNull Player player)
	{
		Validate.notNull((Object) luckPerms, "The luckperms-api cannot be null.");
		Validate.notNull((Object) player, "The player cannot be null.");
		
		return luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData()
			            .getMetaData(getPlayerQueryOptions(luckPerms, player)).getPrefix();
	}
} 
