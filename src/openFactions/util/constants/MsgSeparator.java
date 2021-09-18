package openFactions.util.constants;

import org.bukkit.ChatColor;

public class MsgSeparator {

	/** : */
	public final static String COLON_SINGLE = ":";
	/** :: */
	public final static String COLON_DOUBLE = "::";
	/** ::: */
	public final static String COLON_TRIPLE = ":::";
	
	public final static String SPACE_QUAD = "     ";
	
	public final static String VALUE_FIELD_OPEN = ChatColor.RESET + "["+ ChatColor.AQUA +  "";
	public final static String VALUE_FIELD_CLOSE = "" +ChatColor.RESET + "]";
	
	public final static String COMMAND_DESC = ChatColor.RESET + " - ";
	public final static String COMMAND_INFO_FIELD_OPEN = "" +ChatColor.RED + ChatColor.BOLD +"[";
	public final static String COMMAND_INFO_FIELD_CLOSE = "" +ChatColor.RED + ChatColor.BOLD +"]" + ChatColor.RESET;
	public final static String COMMAND_USAGE_TEXT = ChatColor.AQUA + " usage: "+ChatColor.YELLOW;
	
}
