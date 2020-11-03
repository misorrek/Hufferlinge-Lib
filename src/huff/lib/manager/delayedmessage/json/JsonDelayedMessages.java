package huff.lib.manager.delayedmessage.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JsonDelayedMessages 
{
	@SerializedName("player")
	public List<JsonPlayer> player = new ArrayList<>();
}
