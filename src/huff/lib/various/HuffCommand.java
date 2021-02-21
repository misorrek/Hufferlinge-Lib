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
    			sender.sendMessage(LibMessage.WRONGINPUT.getMessage(new StringPair("text", usage)));
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
    
    protected void setAliases(String... aliases) 
    {
        if (aliases != null && (register || aliases.length > 0))
        {
        	setAliases(Arrays.stream(aliases).collect(Collectors.toList()));
        }    
    }
    
    protected void addTabCompletion(int index, @NotNull String... arg) 
    {
        addTabCompletion(index, null, new String[0], arg);
    }

    protected void addTabCompletion(int index, @Nullable String permission, @Nullable String[] beforeText, @NotNull String... arg) 
    {
        addTabCompletion(index, permission, beforeText == null || beforeText.length == 0 ? null : Collections.singletonMap(index -1, Arrays.asList(beforeText)), arg);
    }
    
    protected void addTabCompletion(int index, @Nullable String permission, @Nullable Map<Integer, List<String>> beforeText, @NotNull String... arg) 
    {
    	Validate.notNull(((Object) arg), "The completion arguments cannot be null.");
    	
        if (arg.length > 0 && index >= 0) 
        {
            if (tabCompletion.containsKey(index))  
            {
                tabCompletion.get(index).addAll(Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCompletion(s, permission, beforeText)),
                        ArrayList::addAll));
            }
            else 
            {
                tabCompletion.put(index, Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCompletion(s, permission, beforeText)),
                        ArrayList::addAll)
                );
            }
        }
    }

    // T A B C O M P L E T I O N
    
    private static class TabCompletion 
    {
        private final String text;
        private final String permission;
        private final Map<Integer, List<String>> textBefore;

        private TabCompletion(String text, String permission, Map<Integer, List<String>> textBefore) 
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

        public String getText() 
        {
            return text;
        }

        public String getPermission() 
        {
            return permission;
        }
        
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