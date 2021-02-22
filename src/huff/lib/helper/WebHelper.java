package huff.lib.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class WebHelper 
{
	private WebHelper() { }
	
	@Nullable
	public static String sendGet(String url) 
	{	
		try 
		{
			final URL obj = new URL(url);
			final HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			
			connection.setRequestMethod("GET");
	
			final BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			final StringBuilder responseBuilder = new StringBuilder();
			String inputLine;
			
			while ((inputLine = inputReader.readLine()) != null) 
			{
				responseBuilder.append(inputLine);
			}
			inputReader.close();
	
			return responseBuilder.toString();
		} 
		catch(IOException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, exception, () -> "Cannot request GET from \"" + url + "\".");
			return null;
		}
	}
	
	@Nullable
	public static String sendPost(String url, String data) 
	{	
		try 
		{
			final URL obj = new URL(url);
			final HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
			
			connection.setRequestMethod("POST");		
			connection.setDoOutput(true);
			
			final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			
			outputStream.writeBytes(data);
			outputStream.flush();
			outputStream.close();
	
			final BufferedReader inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			final StringBuilder responseBuilder = new StringBuilder();
			String inputLine;
				
			while ((inputLine = inputReader.readLine()) != null) {
				responseBuilder.append(inputLine);
			}
			inputReader.close();
	
			return responseBuilder.toString();
		}
		catch(IOException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, exception, () -> "Cannot request POST to \"" + url + "\".");
			return null;
		}
	}
}
