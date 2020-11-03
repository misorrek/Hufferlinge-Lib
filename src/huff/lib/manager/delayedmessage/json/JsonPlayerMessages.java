package huff.lib.manager.delayedmessage.json;

import com.google.gson.annotations.SerializedName;

import huff.lib.manager.delayedmessage.DelayType;
import huff.lib.manager.delayedmessage.MessageType;

public class JsonPlayerMessages 
{
	@SerializedName("delay_type")
	public DelayType delayType = DelayType.NEXTJOIN;
	
	@SerializedName("message_type")
	public MessageType messageType = MessageType.INFO;
	
	@SerializedName("message")
	public String message = "";
}
