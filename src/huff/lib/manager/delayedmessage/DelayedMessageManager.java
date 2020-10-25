package huff.lib.manager.delayedmessage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import huff.lib.helper.FileHelper;
import huff.lib.helper.MessageHelper;
import huff.lib.helper.NMSHelper;

public class DelayedMessageManager
{
	/*
	{
		"player": [
		  	{
		  		"uuid": "",
		  		"messages": [
		  			{
		  				"delay_type": "",
		  				"message_type": "",
		  				"message": ""
		  			}
		  		]
		  	}	
		]
	} 
	*/
	
	private static final String JSONARRAY_PLAYER = "player";
	private static final String JSONFIELD_UUID = "uuid";
	
	private static final String JSONARRAY_MESSAGES = "messages";
	private static final String JSONFIELD_DELAYTYPE = "delay_type";
	private static final String JSONFIELD_MESSAGETYPE = "message_type";
	private static final String JSONFIELD_MESSAGE = "message";
	
	public DelayedMessageManager(@NotNull JavaPlugin plugin, @NotNull String pluginFolderPath)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		Validate.notNull((Object) pluginFolderPath, "The plugin-folder-path cannot be null.");
		
		this.plugin = plugin;
		this.jsonFilePath = Paths.get(pluginFolderPath, "delayedmessages.json").toString();
		this.rootObject = FileHelper.loadJsonObjectFromFile(jsonFilePath);
		
		checkJson();
	}
	
	private final JavaPlugin plugin;
	private final String jsonFilePath;
	private final JSONObject rootObject;
	
	public void addDelayedMessage(@NotNull UUID uuid, DelayType delayType, MessageType messageType, @NotNull String message)
	{		
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		Validate.notNull((Object) message, "The delayed-message cannot be null.");
		
		final JSONArray jsonMessages = getMessagesArray(uuid, true);
		
		if (jsonMessages != null) 
		{
			Map<Object, Object> messageMap = new LinkedHashMap<>(3); 
			messageMap.put(JSONFIELD_DELAYTYPE, delayType.toString());
			messageMap.put(JSONFIELD_MESSAGETYPE, messageType.toString());
			messageMap.put(JSONFIELD_MESSAGE, message);
			
			jsonMessages.add(messageMap);
			
			saveJsonObjectToFile();
		}
	}
	
	public void sendDelayedMessages(@NotNull Player player, DelayType delayType, MessageType messageType)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final ArrayList<String> resultMessages = new ArrayList<>();		
		final JSONArray jsonMessages = getMessagesArray(player.getUniqueId(), false);
		
		if (jsonMessages != null) 
		{
			Iterator<Map<Object, Object>> iterator = jsonMessages.iterator();
			
			while (iterator.hasNext())
			{
				Map<Object, Object> messageMap = iterator.next();
				
				if (messageMap.get(JSONFIELD_DELAYTYPE).equals(delayType) && messageMap.get(JSONFIELD_MESSAGETYPE).equals(messageType))
				{
					resultMessages.add((String) messageMap.get(JSONFIELD_MESSAGE));
					messageMap.remove(JSONFIELD_MESSAGE);
				}
			}
		}
		saveJsonObjectToFile();
		MessageHelper.sendMessagesDelayed(plugin, player, resultMessages, NMSHelper.getSecondsInTicks(5));
	}
	
	private void saveJsonObjectToFile()
	{
		FileHelper.saveJsonObjectToFile(jsonFilePath, rootObject);
	}
	
	private void checkJson()
	{
		if (!rootObject.containsKey(JSONARRAY_PLAYER))
		{
			rootObject.put(JSONARRAY_PLAYER, new JSONArray());
			saveJsonObjectToFile();
		}
	}
	
	private @Nullable JSONArray getMessagesArray(@NotNull UUID uuid, boolean createNewEntry)
	{
		final JSONArray playerArray = (JSONArray) rootObject.get(JSONARRAY_PLAYER);
		
		for (Object playerObject : playerArray)
		{
			if (playerObject instanceof JSONObject)
			{
				final JSONObject playerJsonObject = (JSONObject) playerObject;
				final UUID currentUUID = UUID.fromString((String) playerJsonObject.get(JSONFIELD_UUID));
				
				if (currentUUID != null && currentUUID.equals(uuid))
				{
					return (JSONArray) playerJsonObject.get(JSONARRAY_MESSAGES);
				}
			}
		}
		
		if (createNewEntry)
		{
			JSONObject playerObject = new JSONObject();
			JSONArray messagesArray = new JSONArray();
			
			playerObject.put(JSONFIELD_UUID, uuid);
			playerObject.put(JSONARRAY_MESSAGES, messagesArray);
			playerArray.add(playerObject);
			
			return messagesArray;
		}
		return null;
	}
}
