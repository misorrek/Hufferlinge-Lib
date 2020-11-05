package huff.lib.manager.delayedmessage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.FileHelper;
import huff.lib.helper.MessageHelper;
import huff.lib.helper.IndependencyHelper;
import huff.lib.manager.delayedmessage.json.JsonDelayedMessages;
import huff.lib.manager.delayedmessage.json.JsonPlayer;
import huff.lib.manager.delayedmessage.json.JsonPlayerMessages;

public class DelayedMessagesManager
{	
	public DelayedMessagesManager(@NotNull JavaPlugin plugin, @NotNull String pluginFolderPath)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		Validate.notNull((Object) pluginFolderPath, "The plugin-folder-path cannot be null.");
		
		this.plugin = plugin;
		this.jsonFilePath = Paths.get(pluginFolderPath, "delayedmessages.json").toString();
				
		final Object jsonObject = FileHelper.loadJsonObjectFromFile(jsonFilePath, JsonDelayedMessages.class);
		
		if (jsonObject == null)
		{
			this.jsonDelayedMessages = new JsonDelayedMessages();
		}
		else
		{
			Validate.isTrue(jsonObject instanceof JsonDelayedMessages, String.format("Json object \"%s\" do not fit as JsonDelayedMessages.", jsonObject.toString()));
			
			this.jsonDelayedMessages = (JsonDelayedMessages) jsonObject;
		}
	}
	
	private final JavaPlugin plugin;
	private final String jsonFilePath;
	private final JsonDelayedMessages jsonDelayedMessages;
	
	public void addDelayedMessage(@NotNull UUID uuid, DelayType delayType, MessageType messageType, @NotNull String message)
	{		
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		Validate.notNull((Object) message, "The delayed-message cannot be null.");
		
		final List<JsonPlayerMessages> messagesArray = getPlayerMessages(uuid, true);
		final JsonPlayerMessages jsonPlayerMessages = new JsonPlayerMessages();
		
		jsonPlayerMessages.delayType = delayType;
		jsonPlayerMessages.messageType = messageType;
		jsonPlayerMessages.message = message;
		
		messagesArray.add(jsonPlayerMessages);
		saveJsonObjectToFile();
	}
	
	public void sendDelayedMessages(@NotNull Player player, DelayType delayType, MessageType messageType)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final ArrayList<String> resultMessages = new ArrayList<>();		
		final List<JsonPlayerMessages> jsonPlayerMessage = getPlayerMessages(player.getUniqueId(), false);
		
		if (!jsonPlayerMessage.isEmpty()) 
		{		
			final Iterator<JsonPlayerMessages> iterator = jsonPlayerMessage.iterator(); 
			
			while(iterator.hasNext())
			{
				final JsonPlayerMessages playerMessage = iterator.next();
				
				if (playerMessage.delayType == delayType && playerMessage.messageType == messageType)
				{
					resultMessages.add(playerMessage.message);
					jsonPlayerMessage.remove(playerMessage);
				}
			}
		}
		
		if (!resultMessages.isEmpty())
		{
			saveJsonObjectToFile();
			MessageHelper.sendMessagesDelayed(plugin, player, resultMessages, IndependencyHelper.getSecondsInTicks(5));
		}
	}
	
	private void saveJsonObjectToFile()
	{
		FileHelper.saveJsonObjectToFile(jsonFilePath, jsonDelayedMessages, jsonDelayedMessages.getClass());
	}
	
	private @Nullable List<JsonPlayerMessages> getPlayerMessages(@NotNull UUID uuid, boolean createNewEntry)
	{
		for (JsonPlayer player : jsonDelayedMessages.player)
		{
			if (player.uuid.equals(uuid))
			{
				return player.messages;
			}
		} 
		
		if (createNewEntry)
		{
			final JsonPlayer jsonPlayer = new JsonPlayer();
			
			jsonPlayer.uuid = uuid;
			jsonDelayedMessages.player.add(jsonPlayer);
			
			return jsonPlayer.messages;
		}
		return new ArrayList<>();
	}
}
