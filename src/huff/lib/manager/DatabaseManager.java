package huff.lib.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.MessageHelper;
import huff.lib.various.LibConfig;

public class DatabaseManager 
{
	public DatabaseManager(@NotNull String host, @NotNull String port, @NotNull String name, 
			               @NotNull String username, @NotNull String password)
	{
		Validate.notNull((Object) host, "The database host cannot be null.");
		Validate.notNull((Object) port, "The database port cannot be null.");
		Validate.notNull((Object) name, "The database name cannot be null.");
		Validate.notNull((Object) username, "The database username cannot be null.");
		Validate.notNull((Object) password, "The database password cannot be null.");
		
		this.host = host;
		this.port = port;
		this.name = name;
		this.username = username;
		this.password = password;
	}
	
	public DatabaseManager()
	{
		this(LibConfig.DATABASE_HOST.getValue(),
			 LibConfig.DATABASE_PORT.getValue(),
			 LibConfig.DATABASE_NAME.getValue(),
			 LibConfig.DATABASE_USERNAME.getValue(),
			 LibConfig.DATABASE_PASSWORD.getValue());
	}
	
	private final String host;
	private final String port;
	private final String name;
	private final String username;
	private final String password;
	
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
				connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", host, port, name), 
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
