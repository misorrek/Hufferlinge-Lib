package huff.lib.various;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import huff.lib.helper.JavaHelper;
import huff.lib.various.structures.KeyDefaultValuePair;
import huff.lib.various.structures.StringPair;

/**
 * 
 * 
 */
public class HuffConfiguration extends YamlConfiguration //TODO NEWLINE CASE
{
	private static final double LAYER_PER_WHITESPACE = 0.5;
	
	private final Map<String, List<CustomLine>> customLines = new HashMap<>();
	
	private int headerLines = 0;

	// I O
	
	public static @NotNull HuffConfiguration loadConfiguration(@NotNull File file)
	{
		Validate.notNull((Object) file, "The file cannot be null.");
		
		final HuffConfiguration config = new HuffConfiguration();
		
		try
		{
			config.load(file);
		} 
		catch (IOException | InvalidConfigurationException exception)
		{
			Bukkit.getLogger().log(Level.SEVERE, exception, () -> "Cannot load configuration from file \"" + file + "\".");
		}
		return config;
	}
	
    @Override
    public @NotNull String saveToString() 
    {
        final List<String> contents = Stream.of(super.saveToString().split("\n")).collect(Collectors.toList());
        final StringBuilder currentPathBuilder = new StringBuilder();
        final StringBuilder contentBuilder = new StringBuilder();        
        int currentLayer = 0;
       
        for (String line : contents) 
        {
            if(!line.isEmpty() && line.contains(":")) 
            {
                final int layerFromLine = getLayerFromLine(line);
                final String key = getKeyFromLine(line);
                
                if(layerFromLine <= currentLayer) 
                {
                	regressPathTo(layerFromLine, currentPathBuilder);
                }
                currentLayer = layerFromLine;

                if(currentLayer == 0) 
                {
                	currentPathBuilder.setLength(0);
                	currentPathBuilder.append(key);
                }
                else 
                {
                	currentPathBuilder.append(".");
                	currentPathBuilder.append(key);
                }
                final String path = currentPathBuilder.toString();
                
                if(customLines.containsKey(path)) 
                {
                    customLines.get(path).forEach(customLine -> contentBuilder.append(customLine.getLine(layerFromLine)));
                }
            }
            contentBuilder.append(line);
            contentBuilder.append('\n');
        }
        return contentBuilder.toString();
    }

    @Override
    public void loadFromString(String contents) throws InvalidConfigurationException 
    {
        super.loadFromString(contents);

        final List<String> collectedContents = Stream.of(contents.split("\n")).collect(Collectors.toList());
        final List<CustomLine> currentCustomLines = new ArrayList<>();
        final StringBuilder currentPathBuilder = new StringBuilder();     
        int currentLayer = 0;
        
        for (int i = headerLines; i < collectedContents.size(); i++) 
        {
        	final String line = collectedContents.get(i);
        	
        	if(!isCustomLine(line, currentCustomLines) && line.contains(":")) 
            {
                final int layerFromLine = getLayerFromLine(line);
                final String key = getKeyFromLine(line);
                
                if(layerFromLine <= currentLayer) 
                {
                	regressPathTo(layerFromLine, currentPathBuilder);
                }
                currentLayer = layerFromLine;

                if(currentLayer == 0) 
                {
                	currentPathBuilder.setLength(0);
                	currentPathBuilder.append(key);
                }
                else
                {
                	currentPathBuilder.append(".");
                	currentPathBuilder.append(key);
                }
                currentCustomLines.forEach(customLine -> addCustomLine(currentPathBuilder.toString(), customLine));
                currentCustomLines.clear();
            }
        }
    }
    
    public void set(@NotNull KeyDefaultValuePair<?> pair)
    {
    	Object defaultValue = pair.getDefaultValue();
    	
    	if (defaultValue.getClass().isEnum())
    	{
    		super.set(pair.getKey(), defaultValue.toString());
    	}
    	else
    	{
    		super.set(pair.getKey(), defaultValue);
    	}
    }
    
    public void addDefaults(@NotNull HuffConfiguration defaults) 
    {
        super.addDefaults(defaults);
        
        this.customLines.putAll(defaults.customLines);
    }
    
    /**
     * 
     * 
     * @param   path
     * @return      
     */
    @Override
    public String getString(@NotNull String path)
    {
    	return getString(path, new StringPair[0]);
    }
    
    public @Nullable String getString(@NotNull String path, @NotNull StringPair... contextParameters) //TODO Caching
    {
    	Validate.notNull((Object) path, "The path cannot be null.");
    	
    	final String content = super.getString(path);
    	
    	if (content == null)
    	{
    		return null;
    	}
    	final StringBuffer contentBuffer = new StringBuffer(content);
    	
    	handleContentLinks(contentBuffer);
    	handleContextParameter(contentBuffer, contextParameters);
    	
    	return contentBuffer.toString();
    }
    
    private void handleContentLinks(@NotNull StringBuffer content)
    {
    	final Pattern pattern = Pattern.compile("(\\{[^}]*\\})");
		final Matcher matcher = pattern.matcher(content.toString());
		content.setLength(0);
    	
    	while (matcher.find())
    	{
    		final Object linkedObject = super.get(matcher.group().replaceAll("[{}]", ""));
    		final String linkedContent = linkedObject != null ? linkedObject.toString() : "";
    		
    		matcher.appendReplacement(content, linkedContent);
    	}
    	matcher.appendTail(content);
    }
    
    private void handleContextParameter(@NotNull StringBuffer content, @NotNull StringPair... contextParameters)
    {
    	final Pattern pattern = Pattern.compile("(\\%[^%]*\\%)");
		final Matcher matcher = pattern.matcher(content.toString());
		content.setLength(0);
		
    	while (matcher.find())
    	{
    		final String foundParameter = matcher.group().replaceAll("[%%]", "");
    		String contextParameter = "";
    				
    		for (StringPair pair : contextParameters)
    		{
    			if (pair.value1.equals(foundParameter))
    			{
    				contextParameter = pair.value2;
    				break;
    			}
    		}
    		matcher.appendReplacement(content, contextParameter);
    	}
    	matcher.appendTail(content);
    }
    
    // A D D I T A T I O N
    
    /**
     * 
     * @param path
     */
    public void addEmptyLine(@NotNull String path) 
    {
    	addCustomLine(path, new CustomLine());
    }
    
    /**
     * 
     * @param path
     * @param content
     * @param alignedWithKey
     */
    public void addCommentLine(@NotNull String path, @NotNull String content, boolean alignedWithKey) 
    {
    	Validate.notNull((Object) content, "The custom line content cannot be null");
    	
    	addCustomLine(path, new CustomLine(null, content, alignedWithKey));
    }
    
    /**
     * 
     * @param path
     * @param prefix
     * @param content
     * @param alignedWithKey
     */
    public void addCommentLine(@NotNull String path, @NotNull String prefix, @NotNull String content, boolean alignedWithKey) 
    {
    	Validate.notNull((Object) prefix, "The custom line prefix cannot be null");
    	Validate.notNull((Object) content, "The custom line content cannot be null");
    	
        addCustomLine(path, new CustomLine(prefix, content, alignedWithKey));
    }
    
    /**
     * 
     * @param path
     * @param contextParameters
     */
    public void addContextLine(@NotNull String path, @NotNull String... contextParameters)
    {
    	Validate.notNull((Object) contextParameters, "The context parameters cannot be null");
    	
    	addCustomLine(path, new CustomLine(contextParameters));
    }
    
    // U T I L
    
    @Override
    protected @NotNull String parseHeader(@NotNull String input) 
    {
        final String[] lines = input.split("\r?\n", -1);
        final StringBuilder result = new StringBuilder();
        final int commentPrefixLength = YamlConfiguration.COMMENT_PREFIX.length();
        boolean readingHeader = true;
        boolean foundHeader = false;

        for (int i = 0; (i < lines.length) && (readingHeader); i++)
        {
            final String line = lines[i];

            if (line.startsWith(YamlConfiguration.COMMENT_PREFIX)) 
            {      	
                if (i > 0) 
                {
                    result.append("\n");
                }

                if (line.length() > commentPrefixLength) 
                {
                    result.append(line.substring(commentPrefixLength));
                    headerLines++;
                }
                foundHeader = true;
            } 
            else if (foundHeader) 
            {
                readingHeader = false;
            }
        }     
        return result.toString();
    }
    
    private @Nullable String getKeyFromLine(String line) 
    {
        String key = null;

        for(int i = 0; i < line.length(); i++) 
        {
            if(line.charAt(i) == ':') 
            {
                key = line.substring(0, i);
                break;
            }
        }
        return key == null ? null : key.trim();
    }

    private int getLayerFromLine(String line) 
    {
        double d = 0;
        
        for (int i = 0; i < line.length(); i++) 
        {
            if (line.charAt(i) == ' ') 
            {
                d += LAYER_PER_WHITESPACE;
            }
            else 
            {
                break;
            }
        }
        return (int) d;
    }
    
    private void regressPathTo(int targetLayer, StringBuilder currentPathBuilder) 
    {
        if (targetLayer <= 0) 
        {
        	currentPathBuilder.setLength(0);
            return;
        }
        final String[] keys = currentPathBuilder.toString().split("\\.");
        
        currentPathBuilder.setLength(0);
        
        for (int i = 0; i < targetLayer; i++)
        {
        	currentPathBuilder.append(keys[i]);
        	
        	if (i < (targetLayer - 1))
        	{
        		currentPathBuilder.append(".");
        	}
        }
    }
    
    // C U S T O M L I N E
    
    private boolean isCustomLine(@NotNull String line, @NotNull List<CustomLine> currentCustomLines)
    {
    	
    	final String trimmedLine = line.trim();
    	
    	if (trimmedLine.isEmpty())
    	{
    		currentCustomLines.add(new CustomLine());
    		return true;
    	}
    	else if (trimmedLine.startsWith(CustomLine.CONTEXT_PREFIX))
    	{
    		final Pattern valuePattern = Pattern.compile("%([^%]*)>");
    		final Matcher matcher = valuePattern.matcher(trimmedLine.replace(CustomLine.CONTEXT_PREFIX, ""));
    		final List<String> contextParameters = new ArrayList<>();
    		
    		while (matcher.find())
    		{
    			final String parameter = matcher.group();
    			
    			if (StringUtils.isNotBlank(parameter))
    			{
        			contextParameters.add(parameter);
    			}   			
    		}   		
    		currentCustomLines.add(new CustomLine(contextParameters.stream().toArray(String[]::new)));
    	}
    	else if (trimmedLine.contains("#"))
    	{        
    		final Pattern valuePattern = Pattern.compile("([# ]*)(.*)");
    		final Matcher matcher = valuePattern.matcher(trimmedLine);
    		
    		if (matcher.find())
    		{
    			currentCustomLines.add(new CustomLine(matcher.group(1), matcher.group(2), line.startsWith(" ")));						
    		}
    		else
    		{
    			currentCustomLines.add(new CustomLine(null, trimmedLine, line.startsWith(" ")));
    		}    
    		return true;
    	}
    	return false;
    }
    
    private void addCustomLine(@NotNull String path, @NotNull CustomLine customLine) 
    {
    	Validate.notNull((Object) path, "The custom line path cannot be null");
    	Validate.notNull((Object) path, "The custom line cannot be null");
    	
        final List<CustomLine> pathEntries = JavaHelper.getValueOrDefault(customLines.get(path), new ArrayList<>());
        
    	pathEntries.add(customLine);
        customLines.put(path, pathEntries);
    }
    
    private enum CustomLineType
    {
    	EMPTY_LINE,
    	COMMENT_LINE,
    	CONTEXT_LINE;
    }
    
    private class CustomLine
    {
    	public static final String CONTEXT_PREFIX = "#%# ";
    	
    	public CustomLine()
    	{
    		this.type = CustomLineType.EMPTY_LINE;
    		this.prefix = "";
    		this.content = "";
    		this.alignedWithKey = false;
    	}
    	
    	public CustomLine(@Nullable String prefix, @Nullable String content, boolean alignedWithKey)
    	{
    		this.type = CustomLineType.COMMENT_LINE;
    		this.prefix = prefix != null ? prefix.replaceAll("[^# ]", "") : YamlConfiguration.COMMENT_PREFIX;
    		this.content = content != null ? content : "";
    		this.alignedWithKey = alignedWithKey;
    	}
    	
    	public CustomLine(@NotNull String... contextParameters)
    	{
    		this.type = CustomLineType.CONTEXT_LINE;
    		this.prefix = CONTEXT_PREFIX;
    		
    		final StringBuilder contentBuilder = new StringBuilder();
    		Stream.of(contextParameters).forEach(parameter -> contentBuilder.append("%" + parameter + "% # "));
    		
    		this.content = contentBuilder.toString();
    		this.alignedWithKey = true;
    	}
    	
    	private final CustomLineType type;
    	private final String prefix;
    	private final String content;
    	private final boolean alignedWithKey;
    	
    	public @NotNull String getLine(int currentLayer)
    	{
    		final String spaces = alignedWithKey ? getSpacesFromLayer(currentLayer) : "";
    		
    		return (type == CustomLineType.EMPTY_LINE ? "" : spaces +  prefix + content) + "\n";
    	}
    	
        private @NotNull String getSpacesFromLayer(int layer)
        {
        	final StringBuilder spaceBuilder = new StringBuilder();
        	int runs = (int) (layer / LAYER_PER_WHITESPACE); 
        	
        	while (runs > 0)
        	{
        		spaceBuilder.append(" ");
        		runs--;
        	}
        	return spaceBuilder.toString();
        }
    }
}