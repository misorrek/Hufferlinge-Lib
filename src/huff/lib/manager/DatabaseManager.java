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

/**
 * A manager class that creates and holds a connection to a sql database.
 */
public class DatabaseManager 
{
	/**
	 * @param   host       the sql users host address
	 * @param   port       the sql users port
	 * @param   name       the sql database name
	 * @param   username   the sql username
	 * @param   password   the sql users password
	 */
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
	
	/**
	 * Uses the connection details from the configuration.
	 */
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
	
	/**
	 * Checks if a connection to the sql database exists.
	 * 
	 * @return   The check result.
	 */
	public boolean isConnected()
	{	
		return connection != null;
	}
	
	/**
	 * Gets the connection to the sql database.
	 * If no connection exists null will be returned.
	 * 
	 * @return   The sql connection.
	 */
	@Nullable
	public Connection getConnection()
	{
		return connection;
	}
	
	/**
	 * Tries to connect to the sql database with the specified details.
	 */
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
	
	/**
	 * Disconnects the connection to the sql database if one exists.
	 */
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
	
	/**
	 * Gets a prepared sql statement in which the given objects are inserted.
	 * 
	 * @param   statement      the sql statement. 
	 * @param   objects        the objects to insert in the statement
	 * @return                 The prepared sql statement.
	 * 
	 * @throws  SQLException   if no connection exists
	 */
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
