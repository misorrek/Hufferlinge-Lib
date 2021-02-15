package huff.lib.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityManager
{
	private static Map<String, List<UUID>> enities = new HashMap<>();
	
	private EntityManager( ) { }
	
	public boolean IsEntity(String key, UUID uuid)
	{
		final List<UUID> uuids = enities.get(key);
		
		if (uuids != null)
		{
			return uuids.contains(uuid);
		}
		return false;
	}
}
