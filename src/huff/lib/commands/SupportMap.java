package huff.lib.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SupportMap extends HashMap<UUID, HashSet<UUID>>
{
	private static final long serialVersionUID = 5598594219303972185L;

	public void add(@NotNull UUID userUUID)
	{
		Validate.notNull((Object) userUUID, "The user-uuid cannot be null.");
		this.put(userUUID, new HashSet<>());
	}
	
	public void addSupporter(@NotNull UUID userUUID, @NotNull UUID supporterUUID)
	{
		Validate.notNull((Object) userUUID, "The user-uuid cannot be null.");
		Validate.notNull((Object) supporterUUID, "The supporter-uuid cannot be null.");
		this.get(userUUID).add(supporterUUID);
	}
	
	public void removeSupporter(@NotNull UUID userUUID, @NotNull UUID supporterUUID)
	{
		Validate.notNull((Object) userUUID, "The user-uuid cannot be null.");
		Validate.notNull((Object) supporterUUID, "The supporter-uuid cannot be null.");
		this.get(userUUID).remove(supporterUUID);
	}	
	
	@Nullable
	public UUID getCurrentSupportChat(@NotNull UUID targetSupporterUUID)
	{
		Validate.notNull((Object) targetSupporterUUID, "The target supporter-uuid cannot be null.");
		
		for (Entry<UUID, HashSet<UUID>> chatEntry : this.entrySet())
		{
			for (UUID supporterUUID : chatEntry.getValue())
			{
				if (supporterUUID.equals(targetSupporterUUID))
				{
					return chatEntry.getKey();
				}
			}
		}
		return null;
	}
}
