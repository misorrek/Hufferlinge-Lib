package huff.lib.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A data class that extends a HashMap.
 * Stores the data for the support command.
 */
class SupportMap extends HashMap<UUID, HashSet<UUID>>
{
	private static final long serialVersionUID = 5598594219303972185L;

	public void add(@NotNull UUID user)
	{
		Validate.notNull((Object) user, "The user cannot be null.");
		this.put(user, new HashSet<>());
	}
	
	public void addSupporter(@NotNull UUID user, @NotNull UUID supporter)
	{
		Validate.notNull((Object) user, "The user cannot be null.");
		Validate.notNull((Object) supporter, "The supporter cannot be null.");
		this.get(user).add(supporter);
	}
	
	public void removeSupporter(@NotNull UUID user, @NotNull UUID supporter)
	{
		Validate.notNull((Object) user, "The user cannot be null.");
		Validate.notNull((Object) supporter, "The supporter cannot be null.");
		this.get(user).remove(supporter);
	}	
	
	public @Nullable UUID getCurrentSupportChat(@NotNull UUID supporter)
	{
		Validate.notNull((Object) supporter, "The supporter cannot be null.");
		
		for (Entry<UUID, HashSet<UUID>> chatEntry : this.entrySet())
		{
			for (UUID currentSupporter : chatEntry.getValue())
			{
				if (currentSupporter.equals(supporter))
				{
					return chatEntry.getKey();
				}
			}
		}
		return null;
	}
}
