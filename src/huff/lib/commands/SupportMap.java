package huff.lib.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class SupportMap extends HashMap<UUID, HashSet<UUID>>
{
	private static final long serialVersionUID = 5598594219303972185L;

	public void add(UUID userUUID)
	{
		this.put(userUUID, new HashSet<UUID>());
	}
	
	public void addSupporter(UUID userUUID, UUID supporterUUID)
	{
		this.get(userUUID).add(supporterUUID);
	}
	
	public void removeSupporter(UUID userUUID, UUID supporterUUID)
	{
		this.get(userUUID).remove(supporterUUID);
	}	
	
	public UUID getCurrentSupportChat(UUID targetUserUUID)
	{
		for (Entry<UUID, HashSet<UUID>> chatEntry : this.entrySet())
		{
			for (UUID supporterUUID : chatEntry.getValue())
			{
				if (supporterUUID.equals(targetUserUUID))
				{
					return chatEntry.getKey();
				}
			}
		}
		return null;
	}
}
