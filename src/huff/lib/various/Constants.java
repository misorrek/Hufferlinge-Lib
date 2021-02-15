package huff.lib.various;

import java.nio.file.Paths;

public class Constants
{
	private Constants() { }
	
	public static final String PLUGIN_FOLDER = "plugins";
	public static final String LIB_FOLDER = Paths.get(PLUGIN_FOLDER, "Hufferlinge-Lib").toString();
	
}
