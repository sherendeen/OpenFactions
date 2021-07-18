package openFactions.util.constants;

import org.bukkit.ChatColor;

public class MsgHeader {
	
	public static String factionWarps = MsgSeparator.SPACE_QUAD+MsgSeparator.SPACE_QUAD+ ChatColor.BOLD + "" + ChatColor.BLACK + 
			MsgSeparator.COLON_TRIPLE + " " + ChatColor.GREEN + "Faction Warps" + " " +  
			ChatColor.BLACK +""+ ChatColor.BOLD + MsgSeparator.COLON_TRIPLE + ChatColor.RESET;
	public static String factionAttribute = "* "+ChatColor.BOLD + "" + ChatColor.AQUA + "";
	public static String factionAttributeClose = MsgSeparator.COLON_SINGLE + ChatColor.RESET + " ";
}
