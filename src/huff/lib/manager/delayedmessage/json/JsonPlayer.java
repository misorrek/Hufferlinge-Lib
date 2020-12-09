package huff.lib.manager.delayedmessage.json;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public class JsonPlayer 
{
	@SerializedName("uuid")
	public UUID uuid = null;
	
	@SerializedName("messages")
	public List<JsonPlayerMessage> messages = new ArrayList<>();
}
