package huff.lib.manager.mojangapi;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import huff.lib.helper.WebHelper;

public class MojangApiManager 
{	
	private static final String INSERT = "INSERT INTO users VALUES (?, ?, ?)";	
	private static final String SELECT_UUID = "SELECT * FROM users WHERE username=?";
	private static final String UPDATE_UUID = "UPDATE users SET uuid=?, time=? WHERE username=?";
	private static final String SELECT_USERNAME = "SELECT * FROM users WHERE uuid=?";
	private static final String UPDATE_USERNAME = "UPDATE users SET username=?, time=? WHERE uuid=?";

	private MojangApiManager() { }
	
	@SuppressWarnings("deprecation")
	public static @Nullable UUID getUUID(@NotNull String username) 
	{
		Validate.notNull((Object) username, "The username cannot be null.");
		
		try 
		{
			final CachedRowSet crs = MojangApiStorage.getSQL(SELECT_UUID, username);
			
			if(MojangApiStorage.needsUpdating(crs)) 
			{			
				final String response = WebHelper.sendGet(MojangApiDetails.formatApi(MojangApiDetails.UUID_GETTER, username));
				
				if(response.isEmpty()) 
				{
					return (crs.size() == 1) ? UUID.fromString(parseUUID(crs.getString("uuid"))) : null; 				
				}
				JsonElement uuidJsonEle = new JsonParser().parse(response);
				
				if (MojangApiDetails.isRequestLimit(uuidJsonEle)) 
				{				
					return (crs.size() == 1) ?  UUID.fromString(parseUUID(crs.getString("uuid"))) : null;
				}
			    JsonObject  uuidJsonObj = uuidJsonEle.getAsJsonObject();
			    String uuid = uuidJsonObj.get("id").getAsString();
			    
			    if(crs.size() == 1)
			    {
			    	MojangApiStorage.updateSQL(UPDATE_UUID, uuid, username, MojangApiDetails.UUID_EXPIRE);
			    }
			    else
			    {
			    	MojangApiStorage.insertSQL(INSERT, uuid, username, MojangApiDetails.UUID_EXPIRE);
			    }
			    return UUID.fromString(parseUUID(uuid));
			}		
			return UUID.fromString(parseUUID(crs.getString("uuid")));
		}
		catch (SQLException | IllegalArgumentException exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot get uuid from username.", exception);
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static @Nullable String getUsername(@NotNull UUID uuid) 
	{   
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		
		final String shrinkedUUID = shrinkUUID(uuid);
		
		try 
		{
			final CachedRowSet crs = MojangApiStorage.getSQL(SELECT_USERNAME, shrinkedUUID);
			
			if(MojangApiStorage.needsUpdating(crs)) 
			{
				final String response = WebHelper.sendGet(MojangApiDetails.formatApi(MojangApiDetails.USERNAME_GETTER, shrinkedUUID));
				
				if(response.isEmpty()) 
				{
					return (crs.size() == 1) ? crs.getString("username") : null;
				}
				JsonElement usernameJsonEle = new JsonParser().parse(response);
				
				if(MojangApiDetails.isRequestLimit(usernameJsonEle)) 
				{
					return (crs.size() == 1) ? crs.getString("username") : null;
				}
			    JsonArray  usernameJsonArr = usernameJsonEle.getAsJsonArray();
			    JsonObject usernameJsonObj = usernameJsonArr.get(0).getAsJsonObject();
			    String username = usernameJsonObj.get("name").getAsString();
			    
			    if(crs.size() > 1)
			    {
			    	MojangApiStorage.updateSQL(UPDATE_USERNAME, username, shrinkedUUID, MojangApiDetails.USERNAME_EXPIRE);
			    }
			    else
			    {
			    	MojangApiStorage.insertSQL(INSERT, shrinkedUUID, username, MojangApiDetails.USERNAME_EXPIRE);
			    }
			    return username;
			}
			return crs.getString("username");
		}
		catch (SQLException exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot get username from uuid", exception);
			return null;
		}
	}
	
	public static void updateCache(@NotNull UUID uuid, @NotNull String username) 
	{
		Validate.notNull((Object) uuid, "The uuid cannot be null.");
		
		final String shrinkedUUID = shrinkUUID(uuid);
		
		try
		{
			final CachedRowSet crs = MojangApiStorage.getSQL(SELECT_USERNAME, shrinkedUUID);
			
			if(MojangApiStorage.needsUpdating(crs)) 
			{
			    if (crs.size() > 1)
			    {
			    	MojangApiStorage.updateSQL(UPDATE_USERNAME, username, shrinkedUUID, MojangApiDetails.UUID_EXPIRE);
			    }	
			    else
			    {
			    	MojangApiStorage.insertSQL(INSERT, shrinkedUUID, username, MojangApiDetails.UUID_EXPIRE);		
			    }    
			}	
		}
		catch (SQLException exception) 
		{ 
			Bukkit.getLogger().log(Level.SEVERE, "Cannot update mojang api cache.", exception);
		}
	}
	
	private static @NotNull String shrinkUUID(@NotNull UUID uuid)
	{
		return uuid.toString().replace("-", "");
	}
	
	private static @Nullable String parseUUID(@Nullable String uuid) 
	{
		if (uuid == null || uuid.length() < 32)
		{
			return null;
		}
		return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
	}
}
