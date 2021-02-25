package huff.lib.various;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.PermissionHelper;
import huff.lib.various.structures.StringPair;

/**
 * The base for all hufferlinge commands.
 * Contains the command registration, the command handling and the tab completion.
 * There is no need to register the commands manually in the plugin.yml - simply set all details like description or alias and run "registerCommand". 
 */
public abstract class HuffCommand extends Command implements CommandExecutor, PluginIdentifiableCommand 
{
    protected HuffCommand(@NotNull JavaPlugin plugin, @NotNull String name) 
    {
        super(name);

        Validate.notNull((Object) plugin, "The plugin instance cannot be null.");
        Validate.notNull((Object) name, "The command name cannot be null.");
        Validate.isTrue(StringUtils.isNotBlank(name), "The command name cannot be blank.");

        this.plugin = plugin;
        setLabel(name);
    }
    
    private final JavaPlugin plugin;
    private final Map<Integer, ArrayList<TabCompletion>> tabCompletion = new HashMap<>();
    private boolean register = false;

    @Override
    public JavaPlugin getPlugin() 
    {
        return this.plugin;
    }
    
    @Override
    public boolean execute(CommandSender sender, String command, String[] arg) 
    {
    	if (!PermissionHelper.hasPlayerPermissionFeedbacked(sender, getPermission()))
    	{
    		return false;
    	}
    	
    	if (!onCommand(sender, this, command, arg))
    	{
    		final String usage = getUsage();
    		
    		if (StringUtils.isNotBlank(usage))
    		{
    			sender.sendMessage(LibMessage.WRONGINPUT.getValue(new StringPair("text", usage)));
    		}
    		return false;
    	}
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) 
    {
    	final int index = args.length - 1;
    	
        if (!PermissionHelper.hasPlayerPermissionFeedbacked(sender, getPermission()) || tabCompletion.isEmpty() || !tabCompletion.containsKey(index))
        {
        	return new ArrayList<>();
        }
        return tabCompletion.get(index).stream()          
                .filter(completion -> completion.getPermission() == null || sender.hasPermission(completion.getPermission()))
                .filter(completion -> completion.containsTextBefore(args))
                .filter(completion -> completion.getText().startsWith(args[index]))
                .map(TabCompletion::getText)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }
    
    public Map<Integer, ArrayList<TabCompletion>> getTabComplete() 
    {
        return tabCompletion;
    }
    
    /**
     * Registers the command at the server.
     * Before run the registration set all details like description or alias. Details set after the registration have no effect.
     * 
     * @return   Whether the command were registered or not.
     */
    protected boolean registerCommand() 
    {
        if (!register) 
        {
        	try
            {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                
                f.setAccessible(true);
                
                final CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());
                
                register = commandMap.register(plugin.getName(), this);
            } 
            catch (IllegalAccessException | NoSuchFieldException exception) 
            {
                Bukkit.getLogger().log(Level.SEVERE, "Cannot get declared field \"commandMap\" from bukkit server class.", exception);
            }
        }
        return register;
    }
    
    /**
     * Sets all given strings as alias for the command.
     * 
     * @param   aliases   strings representing the aliases
     */
    protected void setAliases(String... aliases) 
    {
        if (aliases != null && (register || aliases.length > 0))
        {
        	setAliases(Arrays.stream(aliases).collect(Collectors.toList()));
        }    
    }
    
    /**
     * Adds the given parameter suggestions at the specified index.
     * 
     * @param   index         the index of the target parameter starting at zero
     * @param   suggestions   the suggestions for the target parameter     
     */
    protected void addTabCompletion(int index, @NotNull String... suggestions) 
    {
        addTabCompletion(index, null, new String[0], suggestions);
    }

    /**
     * Adds the given parameter suggestions at the specified index.
     * If the suggestions will be shown depends on the given permission and before texts.
     * 
     * @param   index         the index of the target parameter starting at zero
     * @param   permission    the permission that a player needs to show the parameter suggestions
     * @param   beforeText    parameters that must be entered one index before to show the parameter suggestions
     * @param   suggestions   the suggestions for the target parameter     
     */
    protected void addTabCompletion(int index, @Nullable String permission, @Nullable String[] beforeText, @NotNull String... suggestions) 
    {
        addTabCompletion(index, permission, beforeText == null || beforeText.length == 0 ? null : Collections.singletonMap(index -1, Arrays.asList(beforeText)), suggestions);
    }
    
    /**
     * Adds the given parameter suggestions at the specified index.
     * If the suggestions will be shown depends on the given permission and before texts.
     * 
     * @param   index         the index of the target parameter starting at zero
     * @param   permission    the permission that a player needs to show the parameter suggestions
     * @param   beforeText    parameters of specified indexes that must be entered before to show the parameter suggestions
     * @param   suggestions   the suggestions for the target parameter     
     */
    protected void addTabCompletion(int index, @Nullable String permission, @Nullable Map<Integer, List<String>> beforeText, @NotNull String... suggestions) 
    {
    	Validate.notNull(((Object) suggestions), "The suggestions cannot be null.");
    	
        if (suggestions.length > 0 && index >= 0) 
        {
            if (tabCompletion.containsKey(index))  
            {
                tabCompletion.get(index).addAll(Arrays.stream(suggestions).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCompletion(s, permission, beforeText)),
                        ArrayList::addAll));
            }
            else 
            {
                tabCompletion.put(index, Arrays.stream(suggestions).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCompletion(s, permission, beforeText)),
                        ArrayList::addAll)
                );
            }
        }
    }

    // T A B C O M P L E T I O N
    
    /**
     * Represents one suggestion for the command.
     */
    private static class TabCompletion 
    {
        private final String text;
        private final String permission;
        private final Map<Integer, List<String>> textBefore;

        private TabCompletion(@NotNull String text, @Nullable String permission, @Nullable Map<Integer, List<String>> textBefore) 
        {
            this.text = text;
            this.permission = permission;
            
            if (textBefore != null && !textBefore.isEmpty()) 
            {
                this.textBefore = textBefore;
            }
            else
            {
            	this.textBefore = null;
            }
        }

        /**
         * Gets the suggestion.
         * 
         * @return   The suggestion.
         */
        @NotNull
        public String getText() 
        {
            return text;
        }

        /**
         * Gets the permission needed to show the suggestion.
         * 
         * 
         * @return   The permission.
         */
        @Nullable
        public String getPermission() 
        {
            return permission;
        }
        
        /**
         * Checks if the current argument path fits to the needed to show the suggestion.
         * E.g. /command arg1 arg2 arg3 - Checks if arg1, arg2, arg3 fits to show the suggestion.
         * 
         * @param   args   the current arguments
         * @return         The check result.
         */
        public boolean containsTextBefore(@NotNull String[] args)
        {
        	if (textBefore != null)
        	{
        		for (Entry<Integer, List<String>> entry : textBefore.entrySet())
            	{
            		if (entry.getKey() >= args.length || !entry.getValue().contains(args[entry.getKey()]))
            		{
            			return false;
            		}
            	}
        	}
        	return true;
        }
    }
}