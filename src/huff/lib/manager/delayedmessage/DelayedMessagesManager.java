package huff.lib.manager.delayedmessage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.FileHelper;
import huff.lib.helper.MessageHelper;
import huff.lib.helper.StringHelper;
import huff.lib.helper.IndependencyHelper;
import huff.lib.manager.delayedmessage.json.JsonDelayedMessages;
import huff.lib.manager.delayedmessage.json.JsonPlayer;
import huff.lib.manager.delayedmessage.json.JsonPlayerMessage;

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
	
	public void addDelayedMessage(@NotNull UUID uuid, DelayType delayType, @NotNull String message)
	{
		addDelayedMessage(uuid, delayType, null, message);
	}
	
	public void addDelayedMessage(@NotNull UUID uuid, DelayType delayType, @Nullable String prefix, @NotNull String message)
	{		
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		Validate.notNull((Object) message, "The delayed-message cannot be null.");
		
		final List<JsonPlayerMessage> messagesArray = getPlayerMessages(uuid, true);
		final JsonPlayerMessage jsonPlayerMessages = new JsonPlayerMessage();
		
		jsonPlayerMessages.delayType = delayType;
		if (StringHelper.isNotNullOrEmpty(prefix)) jsonPlayerMessages.prefix = prefix;
		if (StringHelper.isNotNullOrEmpty(message)) jsonPlayerMessages.message = message;
		
		messagesArray.add(jsonPlayerMessages);
		saveJsonObjectToFile();
	}
	
	public @NotNull List<String> getDelayedMessages(@NotNull Player player, DelayType delayType)
	{
		Validate.notNull((Object) player, "The player cannot be null.");
		
		final List<String> resultMessages = new ArrayList<>();		
		final List<JsonPlayerMessage> jsonPlayerMessages = getPlayerMessages(player.getUniqueId(), false);
		
		if (!jsonPlayerMessages.isEmpty()) 
		{		 
			final List<JsonPlayerMessage> playerMessagesToSend = new ArrayList<>();
			
			for (int i = 0; i < jsonPlayerMessages.size(); i++) 
			{
				final JsonPlayerMessage playerMessage = jsonPlayerMessages.get(i);
				
				if (playerMessage.delayType == delayType)
				{
					playerMessagesToSend.add(playerMessage);
				}
			}
			
			for (JsonPlayerMessage playerMessage : playerMessagesToSend)
			{	
				jsonPlayerMessages.remove(playerMessage);
				
				if (StringHelper.isNotNullOrEmpty(playerMessage.prefix))
				{
					resultMessages.add(playerMessage.prefix + " " + playerMessage.message);
				}
				else
				{
					resultMessages.add(MessageHelper.PREFIX_DELAYMESSAGE + playerMessage.message);
				}
			}
			
			if (jsonPlayerMessages.isEmpty())
			{
				jsonDelayedMessages.player.remove(getPlayer(player.getUniqueId()));
			}
		}
		
		if (!resultMessages.isEmpty())
		{
			saveJsonObjectToFile();
		}
		return resultMessages;
	}
	
	public void sendDelayedMessages(@NotNull Player player, DelayType delayType)
	{
		final List<String> delayedMessages = getDelayedMessages(player, delayType);
		
		if (!delayedMessages.isEmpty())
		{
			MessageHelper.sendMessagesDelayed(plugin, player, delayedMessages, IndependencyHelper.getSecondsInTicks(10));
		}
	}
	
	private void saveJsonObjectToFile()
	{
		FileHelper.saveJsonObjectToFile(jsonFilePath, jsonDelayedMessages, jsonDelayedMessages.getClass());
	}
	
	private @Nullable JsonPlayer getPlayer(@NotNull UUID uuid)
	{
		for (JsonPlayer player : jsonDelayedMessages.player)
		{
			if (player.uuid.equals(uuid))
			{
				return player;
			}
		} 
		return null;
	}
	
	private @NotNull List<JsonPlayerMessage> getPlayerMessages(@NotNull UUID uuid, boolean createNewEntry)
	{
		final JsonPlayer player = getPlayer(uuid);
		
		if (player != null)
		{
			return player.messages;
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
