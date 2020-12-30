package huff.lib.manager.delaymessage.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class JsonDelayMessages 
{
	@SerializedName("player")
	public List<JsonPlayer> player = new ArrayList<>();
}
