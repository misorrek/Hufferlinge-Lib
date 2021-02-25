package huff.lib.manager.delaymessage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.FileHelper;
import huff.lib.helper.MessageHelper;
import huff.lib.manager.delaymessage.json.JsonDelayMessages;
import huff.lib.manager.delaymessage.json.JsonPlayer;
import huff.lib.manager.delaymessage.json.JsonPlayerMessage;
import huff.lib.various.Constants;
import huff.lib.various.LibMessage;
import huff.lib.helper.IndependencyHelper;

/**
 * A manager class for delayed messages e.g. for offline messages.
 */
public class DelayMessageManager
{	
	public DelayMessageManager(@NotNull JavaPlugin plugin)
	{
		Validate.notNull((Object) plugin, "The plugin-instance cannot be null.");
		
		this.plugin = plugin;
		this.jsonFilePath = Paths.get(Constants.LIB_FOLDER, "delaymessage.json").toString();
				
		final Object jsonObject = FileHelper.loadJsonObjectFromFile(jsonFilePath, JsonDelayMessages.class);
		
		if (jsonObject == null)
		{
			this.jsonDelayMessages = new JsonDelayMessages();
		}
		else
		{
			Validate.isTrue(jsonObject instanceof JsonDelayMessages, String.format("Json object \"%s\" do not fit as jsonDelayMessages.", jsonObject.toString()));
			
			this.jsonDelayMessages = (JsonDelayMessages) jsonObject;
		}
	}
	
	private final JavaPlugin plugin;
	private final String jsonFilePath;
	private final JsonDelayMessages jsonDelayMessages;
	
	/**
	 * Stores a new delayed message and sends it when the delay type triggers. 
	 * 
	 * @param   uuid        the target players uuid
	 * @param   delayType   the delay type that determines the trigger to send the message
	 * @param   message     the message to send
	 */
	public void addDelayMessage(@NotNull UUID uuid, DelayType delayType, @NotNull String message)
	{
		addDelayMessage(uuid, delayType, null, message);
	}
	
	/**
	 * Stores a new delayed message with custom prefix and sends it when the delay type triggers. 
	 * 
	 * @param   uuid        the target players uuid
	 * @param   delayType   the delay type that determines the trigger to send the message
	 * @param   prefix      the custom prefix
	 * @param   message     the message to send
	 */
	public void addDelayMessage(@NotNull UUID uuid, DelayType delayType, @Nullable String prefix, @NotNull String message)
	{		
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		Validate.notNull((Object) message, "The delay message cannot be null.");
		
		final List<JsonPlayerMessage> messagesArray = getPlayerMessages(uuid, true);
		final JsonPlayerMessage jsonPlayerMessages = new JsonPlayerMessage();
		
		jsonPlayerMessages.delayType = delayType;
		if (StringUtils.isNotEmpty(prefix)) jsonPlayerMessages.prefix = prefix;
		if (StringUtils.isNotEmpty(message)) jsonPlayerMessages.message = message;
		
		messagesArray.add(jsonPlayerMessages);
		saveJsonObjectToFile();
	}
	
	/**
	 * Gets all stored delayed messages from the specific type of the given player. 
	 * If no messages can be found the list will be returned empty.
	 * 
	 * @param   player      the target player
	 * @param   delayType   the target delay type
	 * @return              The stored delayed messages.
	 */
	@NotNull
	public List<String> getDelayMessages(@NotNull Player player, DelayType delayType)
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
				
				if (StringUtils.isNotEmpty(playerMessage.prefix))
				{
					resultMessages.add(playerMessage.prefix + " " + playerMessage.message);
				}
				else
				{
					resultMessages.add(LibMessage.PREFIX_DELAYMESSAGE.getValue() + playerMessage.message);
				}
			}
			
			if (jsonPlayerMessages.isEmpty())
			{
				jsonDelayMessages.player.remove(getPlayer(player.getUniqueId()));
			}
		}
		
		if (!resultMessages.isEmpty())
		{
			saveJsonObjectToFile();
		}
		return resultMessages;
	}
	
	/**
	 * Sends all stored delayed messages from the specific type of the given player to that player. 
	 * If no messages can be found nothing will be send.
	 * 
	 * @param   player      the target player
	 * @param   delayType   the target delay type
	 */
	public void sendDelayMessages(@NotNull Player player, DelayType delayType)
	{
		final List<String> delayMessages = getDelayMessages(player, delayType);
		
		if (!delayMessages.isEmpty())
		{
			MessageHelper.sendMessagesDelayed(plugin, player, delayMessages, IndependencyHelper.getSecondsInTicks(10));
		}
	}
	
	private void saveJsonObjectToFile()
	{
		FileHelper.saveJsonObjectToFile(jsonFilePath, jsonDelayMessages, jsonDelayMessages.getClass());
	}
	
	@Nullable
	private JsonPlayer getPlayer(@NotNull UUID uuid)
	{
		for (JsonPlayer player : jsonDelayMessages.player)
		{
			if (player.uuid.equals(uuid))
			{
				return player;
			}
		} 
		return null;
	}
	
	@NotNull
	private List<JsonPlayerMessage> getPlayerMessages(@NotNull UUID uuid, boolean createNewEntry)
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
			jsonDelayMessages.player.add(jsonPlayer);
			
			return jsonPlayer.messages;
		}
		return new ArrayList<>();
	}
}
