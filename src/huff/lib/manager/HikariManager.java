package huff.lib.manager;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import huff.lib.helper.FileHelper;

/**
 * A manager class that creates and holds a connection to a hikari data source (local sql database).
 */
public class HikariManager
{
	private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource hikari;
    
    private HikariManager() { }
    
    /**
     * Gets whether the hikara data source or creates a new one.
     * 
     * @param   sqlFilePath      the path to create or load the local sql database
     * @param   initStatements   the statement to run at connection creation   
     * @return                   The hikari data source.
     */
    @NotNull
    public static HikariDataSource getHikari(@NotNull String sqlFilePath, @NotNull List<String> initStatements)
    {
    	if (hikari == null) 
    	{
    		init(sqlFilePath, initStatements);
    	}
        return hikari;
    }
    
    private static void init(@NotNull String sqlFilePath, @NotNull List<String> initStatements) 
    {
    	checkSqlFile(sqlFilePath);
    	
        config.setJdbcUrl("jdbc:sqlite:" + sqlFilePath);
        config.setUsername("hikarimanager");
        config.setPassword("hikaripassword");
        config.addDataSourceProperty("cachePrepStmts" , "true");
        config.addDataSourceProperty("prepStmtCacheSize" , "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        hikari = new HikariDataSource(config);                      
        
        checkSqlContent(hikari, initStatements);
    }

	private static void checkSqlFile(String sqlFilePath) 
	{	
		FileHelper.createFileAndParents(new File(sqlFilePath));
	}
	
    private static void checkSqlContent(@NotNull HikariDataSource hikari, @NotNull List<String> initStatements) 
    {
    	try (Connection connection = hikari.getConnection()) 
    	{
    		for (String initStatement : initStatements)
    		{
    			try (Statement statement = connection.createStatement())
    			{
    				statement.executeUpdate(initStatement);
    			}
    		}
    	} 
    	catch (SQLException exception) 
    	{
			Bukkit.getLogger().log(Level.SEVERE, "Cannot execute hikari init statements.", exception);
		}
	}
}
