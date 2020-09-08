package huff.lib.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.MessageHelper;
import huff.lib.interfaces.DatabaseProperties;

public class DatabaseManager 
{
	public DatabaseManager(DatabaseProperties databaseProperties)
	{
		this.host = databaseProperties.getHost();
		this.port = databaseProperties.getPort();
		this.databasename = databaseProperties.getDatabasename();
		this.username = databaseProperties.getUsername();
		this.password = databaseProperties.getPassword();
	}
	
	public DatabaseManager(String host, String port, String databasename, String username, String password)
	{
		this.host = host;
		this.port = port;
		this.databasename = databasename;
		this.username = username;
		this.password = password;
	}
	
	private String host;
	private String port;
	private String databasename;
	private String username;
	private String password;
	
	private Connection connection;
	
	public boolean isConnected()
	{	
		return connection != null;
	}
	
	@Nullable
	public Connection getConnection()
	{
		return connection;
	}
	
	public void connect()
	{		
		if(!isConnected()) 
		{
			try 
			{
				connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", host, port, databasename), 
						                                 username, password);
				MessageHelper.sendConsoleMessage("SQL-Verbindung erfolgreich hergestellt.");
			} 
			catch (SQLException execption) 
			{
				Bukkit.getLogger().log(Level.SEVERE, "SQL-Verbindung fehlgeschlagen. Daten hinterlegt?");
			}
		}
	}
	
	public void disconnect() 
	{
		if(isConnected()) 
		{
			try 
			{
				connection.close();
				MessageHelper.sendConsoleMessage("SQL-Verbindung getrennt.");
			} 
			catch (SQLException execption) 
			{
				Bukkit.getLogger().log(Level.SEVERE, "Trennen der SQL-Verbindung fehlgeschlagen.", execption);
			}
		}
	}
	
	@NotNull
	public PreparedStatement prepareStatement(String statement, Object... objects) throws SQLException
	{
		if (!isConnected())
		{
			throw new SQLException("Keine SQL-Verbindung. Daten hinterlegt?");
		}		
		return getConnection().prepareStatement(String.format(statement, objects));
	}
}
