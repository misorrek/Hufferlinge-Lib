package huff.lib.manager.delayedmessage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	private final JsonObject rootObject;
	
	public void addDelayedMessage(@NotNull UUID uuid, DelayType delayType, MessageType messageType, @NotNull String message)
	{		
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		Validate.notNull((Object) message, "The delayed-message cannot be null.");
		
		final JsonArray messagesArray = getMessagesArray(uuid, true);
		
		if (messagesArray != null) 
		{
			final JsonObject messageObject = new JsonObject();
			
			messageObject.addProperty(JSONFIELD_DELAYTYPE, delayType.toString());
			messageObject.addProperty(JSONFIELD_MESSAGETYPE, messageType.toString());
			messageObject.addProperty(JSONFIELD_MESSAGE, message);		
			messagesArray.add(messageObject);
			saveJsonObjectToFile();
		}
	}
	
	public void sendDelayedMessages(@NotNull Player player, DelayType delayType, MessageType messageType)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final ArrayList<String> resultMessages = new ArrayList<>();		
		final JsonArray messagesArray = getMessagesArray(player.getUniqueId(), false);
		
		if (messagesArray != null) 
		{		
			for (JsonElement messageElement : messagesArray)
			{				
				if (!messageElement.isJsonObject())
				{
					continue;
				}
				final JsonObject messageObject = messageElement.getAsJsonObject();
				
				if (messageObject.get(JSONFIELD_DELAYTYPE).getAsString().equals(delayType.toString()) &&
				    messageObject.get(JSONFIELD_MESSAGETYPE).getAsString().equals(messageType.toString()))
				{
					resultMessages.add(messageObject.get(JSONFIELD_MESSAGE).getAsString());
					messageObject.remove(JSONFIELD_MESSAGE);
				}
			}
		}
		
		if (!resultMessages.isEmpty())
		{
			saveJsonObjectToFile();
			MessageHelper.sendMessagesDelayed(plugin, player, resultMessages, NMSHelper.getSecondsInTicks(5));
		}
	}
	
	private void saveJsonObjectToFile()
	{
		FileHelper.saveJsonObjectToFile(jsonFilePath, rootObject);
	}
	
	private void checkJson()
	{
		if (!rootObject.keySet().contains(JSONARRAY_PLAYER))
		{
			rootObject.add(JSONARRAY_PLAYER, new JsonArray());
		}
	}
	
	private @Nullable JsonArray getMessagesArray(@NotNull UUID uuid, boolean createNewEntry)
	{
		final JsonArray playerArray = rootObject.get(JSONARRAY_PLAYER).getAsJsonArray();
		
		if (playerArray == null)
		{		
			return null;
		}
			
		for (JsonElement playerElement : playerArray)
		{
			if (playerElement.isJsonObject())
			{
				final JsonObject playerJsonObject = playerElement.getAsJsonObject();
				final UUID currentUUID = UUID.fromString(playerJsonObject.get(JSONFIELD_UUID).getAsString());
				
				if (currentUUID != null && currentUUID.equals(uuid))
				{
					return playerJsonObject.get(JSONARRAY_MESSAGES).getAsJsonArray();
				}
			}
		}
		
		if (createNewEntry)
		{
			JsonObject playerObject = new JsonObject();
			JsonArray messagesArray = new JsonArray();
			
			playerObject.addProperty(JSONFIELD_UUID, uuid.toString());
			playerObject.add(JSONARRAY_MESSAGES, messagesArray);
			playerArray.add(playerObject);
			
			return messagesArray;
		}
		return null;
	}
}
