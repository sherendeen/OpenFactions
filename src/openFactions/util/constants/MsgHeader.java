package openFactions.util.constants;

import org.bukkit.ChatColor;

public class MsgHeader {
	
	/**
	 * header for faction warps
	 */
	public static String FACTION_WARPS = MsgSeparator.SPACE_QUAD+MsgSeparator.SPACE_QUAD+ ChatColor.BOLD + ChatColor.BLACK + 
			MsgSeparator.COLON_TRIPLE + " " + ChatColor.GREEN + "Faction Warps" + " " +  
			ChatColor.BLACK +""+ ChatColor.BOLD + MsgSeparator.COLON_TRIPLE + ChatColor.RESET;
	/**
	 * header for faction claims
	 */
	public static String FACTION_CLAIMS = MsgSeparator.SPACE_QUAD+MsgSeparator.SPACE_QUAD+ ChatColor.BOLD + ChatColor.AQUA + 
			MsgSeparator.COLON_TRIPLE + " " + ChatColor.LIGHT_PURPLE + "Faction Claims" + " " +  
			ChatColor.AQUA +""+ ChatColor.BOLD + MsgSeparator.COLON_TRIPLE + ChatColor.RESET;
	
	public static String factionAttribute = "* "+ChatColor.BOLD + "" + ChatColor.AQUA + "";
	public static String factionAttributeClose = MsgSeparator.COLON_SINGLE + ChatColor.RESET + " ";
}
