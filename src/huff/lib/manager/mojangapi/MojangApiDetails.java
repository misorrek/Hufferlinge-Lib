package huff.lib.manager.mojangapi;

import com.google.gson.JsonElement;

public class MojangApiDetails 
{
	private MojangApiDetails() { }
	
	public static final int USERNAME_EXPIRE = 3600;
	public static final int UUID_EXPIRE = 3600;	
	public static final String UUID_GETTER = "https://api.mojang.com/users/profiles/minecraft/%s";
	public static final String USERNAME_GETTER = "https://api.mojang.com/user/profiles/%s/names";
	
	public static String formatApi(String api, String value) 
	{
		return String.format(api, value);
	}		
	
	public static boolean isRequestLimit(JsonElement json) 
	{		
		return json.getAsJsonObject().get("error") != null;
	}
}
