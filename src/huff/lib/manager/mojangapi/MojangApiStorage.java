package huff.lib.manager.mojangapi;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import com.zaxxer.hikari.HikariDataSource;

import huff.lib.helper.JavaHelper;
import huff.lib.manager.HikariManager;
import huff.lib.various.Constants;

public class MojangApiStorage 
{
	private static final HikariDataSource hikari = HikariManager.getHikari(
			Paths.get(Constants.LIB_FOLDER, "user_cache.db").toString(),
			Stream.of("CREATE TABLE IF NOT EXISTS users(UUID varchar(32), username VARCHAR(17), time varchar(11))")
			      .collect(Collectors.toList()));
	
	private MojangApiStorage() { }
	
	public static void updateSQL(String statement, String first, String second, int expire) throws SQLException 
	{
		try (final Connection connection = hikari.getConnection()) 
		{
			try (final PreparedStatement update = connection.prepareStatement(statement))
			{
				update.setString(1, first);
				update.setString(2, getExpireTime(expire));
		     	update.setString(3, second);
		     	update.execute();  
			}
		} 
		catch (SQLException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, "SQL update cannot be executed.", exception);
		}     	
	}
	
	public static void insertSQL(String statement, String first, String second, int expire) throws SQLException 
	{
		try (final Connection connection = hikari.getConnection()) 
		{
			try (final PreparedStatement insert = connection.prepareStatement(statement))
			{
				insert.setString(1, first);
				insert.setString(2, second);
				insert.setString(3, getExpireTime(expire));
				insert.execute();
			}
			
		} 
		catch (SQLException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, "SQL insert cannot be executed.", exception);
		} 		
	}
	
	public static CachedRowSet getSQL(String statement, String value) throws SQLException 
	{
		try (Connection connection = hikari.getConnection()) 
		{
			try (final PreparedStatement select = connection.prepareStatement(statement))
			{
				select.setString(1, value);    
				
				final ResultSet result = select.executeQuery();	
				CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
				
				crs.populate(result);
		     	result.close();
		     	return crs;		
			}
		} 
		catch (SQLException exception) 
		{
			Bukkit.getLogger().log(Level.SEVERE, "SQL update cannot be executed.", exception);
			return null;
		} 	
	}
	
	public static boolean needsUpdating(CachedRowSet crs) throws SQLException 
	{		
		if (crs.size() == 1 && crs.next())
		{		
			return Long.parseLong(crs.getString("time")) < (System.currentTimeMillis() / JavaHelper.SECOND_IN_MILLIS);
		}
		return true;
	}
	
	private static @NotNull String getExpireTime(int expire)
	{
		 return Long.toString((System.currentTimeMillis() / JavaHelper.SECOND_IN_MILLIS) + expire);
	}
}
