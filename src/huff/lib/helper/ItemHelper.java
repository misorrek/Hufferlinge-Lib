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

public class ItemHelper
{
	private ItemHelper() { }
	
	public static @NotNull ItemStack getItemWithMeta(@NotNull Material material, @Nullable String displayName)
	{
		return getItemWithMeta(material, displayName, null);
	}
	
	public static @NotNull ItemStack getItemWithMeta(@NotNull Material material, @Nullable String displayName, @Nullable List<String> lore)
	{
		return getItemWithMeta(material, displayName, lore, new ItemFlag[0]);
	}
	
	public static @NotNull ItemStack getItemWithMeta(@NotNull Material material, @Nullable String displayName, @Nullable List<String> lore, ItemFlag... itemFlags)
	{
		Validate.notNull((Object) material, "The material cannot be null.");
		
		final ItemStack resultItem = new ItemStack(material);
		final ItemMeta resultMeta = resultItem.getItemMeta();
		
		if (StringHelper.isNotNullOrEmpty(displayName)) resultMeta.setDisplayName(displayName);	
		if (lore != null) resultMeta.setLore(lore);
		if (itemFlags != null) resultMeta.addItemFlags(itemFlags);
			
		resultItem.setItemMeta(resultMeta);
		return resultItem;
	}
	
	public static @NotNull ItemStack getSkullWithMeta(@NotNull OfflinePlayer owner, @Nullable String displayName)
	{
		return getSkullWithMeta(owner, displayName, null);
	}
	
	public static @NotNull ItemStack getSkullWithMeta(@NotNull OfflinePlayer owner, @Nullable String displayName, @Nullable List<String> lore)
	{
		Validate.notNull((Object) owner, "The skull-owner cannot be null.");
		
		final ItemStack resultItem = getItemWithMeta(Material.PLAYER_HEAD, displayName, lore);
		final SkullMeta resultMeta = (SkullMeta) resultItem.getItemMeta();
		
		resultMeta.setOwningPlayer(owner);
		resultItem.setItemMeta(resultMeta);
		return resultItem;
	}
	
	public static @NotNull ItemStack getSkullWithMeta(@NotNull UUID ownerUUID, @Nullable String displayName)
	{
		return getSkullWithMeta(Bukkit.getOfflinePlayer(ownerUUID), displayName, null);
	}
	
	public static @NotNull ItemStack getSkullWithMeta(@NotNull UUID ownerUUID, @Nullable String displayName, @Nullable List<String> lore)
	{
		return getSkullWithMeta(Bukkit.getOfflinePlayer(ownerUUID), displayName, lore);
	}
	
	public static void applyLore(@NotNull ItemStack itemStack, @NotNull List<String> lore) 
	{
		ItemMeta loreMeta = itemStack.getItemMeta();
		loreMeta.setLore(lore);
		itemStack.setItemMeta(loreMeta);
	}
}
