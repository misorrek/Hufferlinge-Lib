package huff.lib.manager.delayedmessage.json;

import com.google.gson.annotations.SerializedName;

import huff.lib.manager.delayedmessage.DelayType;

public class JsonPlayerMessage 
{
	@SerializedName("delay_type")
	public DelayType delayType = DelayType.NEXTJOIN;
	
	@SerializedName("prefix")
	public String prefix = "";
	
	@SerializedName("message")
	public String message = "";
}
