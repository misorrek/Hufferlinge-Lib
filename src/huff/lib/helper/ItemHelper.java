package huff.lib.helper;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A helper class containing static item methods.
 * Covers everything around "org.bukkit.inventory.ItemStack".
 */
public class ItemHelper
{
	private ItemHelper() { }
	
	/**
	 * Gets an "org.bukkit.inventory.ItemStack" which has the given meta details set.
	 * 
	 * @param   material      the material that defines the type of the returned item
	 * @param   displayName   a string that is displayed as name on the returned item
	 * @return                A "org.bukkit.inventory.ItemStack" with the given meta details.
	 */
	public static @NotNull ItemStack getItemWithMeta(Material material, @Nullable String displayName)
	{
		return getItemWithMeta(material, displayName, null);
	}
	
	/**
	 * Gets an "org.bukkit.inventory.ItemStack" which has the given meta details set.
	 * 
	 * @param   material      the material that defines the type of the returned item
	 * @param   displayName   a string that is displayed as name on the returned item
	 * @param   lore          a list of strings that is displayed as description on the returned item
	 * @return                A "org.bukkit.inventory.ItemStack" with the given meta details.
	 */
	public static @NotNull ItemStack getItemWithMeta(Material material, @Nullable String displayName, @Nullable List<String> lore)
	{
		return getItemWithMeta(material, displayName, lore, new ItemFlag[0]);
	}
	
	/**
	 * Gets an "org.bukkit.inventory.ItemStack" which has the given meta details set.
	 * 
	 * @param   material      the material that defines the type of the returned item
	 * @param   displayName   a string that is displayed as name on the returned item
	 * @param   lore          a list of strings that is displayed as description on the returned item 
	 * @param   itemFlags     a optional list of flags that set specific options for the returned item
	 * @return                A "org.bukkit.inventory.ItemStack" with the given meta details.
	 */
	public static @NotNull ItemStack getItemWithMeta(Material material, @Nullable String displayName, @Nullable List<String> lore, ItemFlag... itemFlags)
	{
		final ItemStack resultItem = new ItemStack(material);
		final ItemMeta resultMeta = resultItem.getItemMeta();
		
		if (StringHelper.isNotNullOrEmpty(displayName)) resultMeta.setDisplayName(displayName);	
		if (lore != null) resultMeta.setLore(lore);
		if (itemFlags != null) resultMeta.addItemFlags(itemFlags);		
		
		resultItem.setItemMeta(resultMeta);
		return resultItem;
	}
	
	/**
	 * Gets an "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" which has the given meta details set.
	 * 
	 * @param   owner         the owner which the skull is from
	 * @param   displayName   a string that is displayed as name on the returned skull item
	 * @return                A "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" with the given meta details.
	 */
	public static @NotNull ItemStack getSkullWithMeta(@NotNull OfflinePlayer owner, @Nullable String displayName)
	{
		return getSkullWithMeta(owner, displayName, null);
	}
	
	/**
	 * Gets an "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" which has the given meta details set.
	 * 
	 * @param   owner         the owner which the skull is from
	 * @param   displayName   a string that is displayed as name on the returned skull item
	 * @param   lore          a list of strings that is displayed as description on the returned skull item 
	 * @return                A "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" with the given meta details.
	 */
	public static @NotNull ItemStack getSkullWithMeta(@NotNull OfflinePlayer owner, @Nullable String displayName, @Nullable List<String> lore)
	{
		Validate.notNull((Object) owner, "The skull-owner cannot be null.");
		
		final ItemStack resultItem = getItemWithMeta(Material.PLAYER_HEAD, displayName, lore);
		final SkullMeta resultMeta = (SkullMeta) resultItem.getItemMeta();
		
		resultMeta.setOwningPlayer(owner);
		resultItem.setItemMeta(resultMeta);
		return resultItem;
	}

	/**
	 * Gets an "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" which has the given meta details set.
	 * 
	 * @param   ownerUUID     the owner's uuid which the skull is from
	 * @param   displayName   a string that is displayed as name on the returned skull item
	 * @return                A "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" with the given meta details.
	 */
	public static @NotNull ItemStack getSkullWithMeta(@NotNull UUID ownerUUID, @Nullable String displayName)
	{
		return getSkullWithMeta(Bukkit.getOfflinePlayer(ownerUUID), displayName, null);
	}
	
	/**
	 * Gets an "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" which has the given meta details set.
	 * 
	 * @param   ownerUUID     the owner's uuid which the skull is from
	 * @param   displayName   a string that is displayed as name on the returned skull item
	 * @param   lore          a list of strings that is displayed as description on the returned skull item 
	 * @return                A "org.bukkit.inventory.ItemStack" from type "PLAYER_HEAD" with the given meta details.
	 */
	public static @NotNull ItemStack getSkullWithMeta(@NotNull UUID ownerUUID, @Nullable String displayName, @Nullable List<String> lore)
	{
		return getSkullWithMeta(Bukkit.getOfflinePlayer(ownerUUID), displayName, lore);
	}
	
	/**
	 * Updates the meta details of the given item stack.
	 * 
	 * @param   itemStack     the item stack where are the meta details to update from
	 * @param   displayName   a string that is displayed as name on the returned item
	 */
	public static void updateItemWithMeta(@NotNull ItemStack itemStack, @Nullable String displayName)
	{
		updateItemWithMeta(itemStack, displayName, null, new ItemFlag[0]);
	}
	
	/**
	 * Updates the meta details of the given item stack.
	 * 
	 * @param   itemStack     the item stack where are the meta details to update from
	 * @param   displayName   a string that is displayed as name on the returned item
	 * @param   lore          a list of strings that is displayed as description on the returned item
	 */
	public static void updateItemWithMeta(@NotNull ItemStack itemStack, @Nullable String displayName, @Nullable List<String> lore)
	{
		updateItemWithMeta(itemStack, displayName, lore, new ItemFlag[0]);
	}
	
	/**
	 * Updates the meta details of the given item stack.
	 * 
	 * @param   itemStack     the item stack where are the meta details to update from
	 * @param   displayName   a string that is displayed as name on the returned item
	 * @param   lore          a list of strings that is displayed as description on the returned item 
	 * @param   itemFlags     a optional list of flags that set specific options for the returned item
	 */
	public static void updateItemWithMeta(@NotNull ItemStack itemStack, @Nullable String displayName, @Nullable List<String> lore, ItemFlag... itemFlags)
	{
		Validate.notNull((Object) itemStack, "The item stack cannot be null.");
		
		final ItemMeta itemMeta = itemStack.getItemMeta();
		
		if (StringHelper.isNotNullOrEmpty(displayName)) itemMeta.setDisplayName(displayName);	
		if (lore != null) itemMeta.setLore(lore);
		if (itemFlags != null) itemMeta.addItemFlags(itemFlags);
			
		itemStack.setItemMeta(itemMeta);
	}
	
	/**
	 * Applies a lore meta detail to the given "org.bukkit.inventory.ItemStack".
	 * 
	 * @param   itemStack     the item stack which the lore meta detail gets applied
	 * @param   lore          the list of strings that gets applied to the item stack
	 * @return                The "org.bukkit.inventory.ItemStack" with the lore meta detail applied.
	 */
	public static void applyLore(@NotNull ItemStack itemStack, @NotNull List<String> lore) 
	{
		ItemMeta loreMeta = itemStack.getItemMeta();
		loreMeta.setLore(lore);
		itemStack.setItemMeta(loreMeta);
	}
	
	
	/**
	 * Checks if a item stack is not null and has an item meta.
	 * 
	 * @param   itemStack   the item stack to check the meta from
	 * @return              The check result.
	 */
	public static boolean hasMeta(@Nullable ItemStack itemStack)
	{
		return itemStack != null && itemStack.getItemMeta() != null;
	}
}
