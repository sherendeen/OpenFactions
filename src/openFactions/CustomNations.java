package openFactions;

import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Seth G. R. Herendeen
 *
 */
public class CustomNations extends JavaPlugin{
	
	public static ArrayList<Faction> factions = new ArrayList<Faction>(); 
	
	//TODO: Add file return functions for configuration purposes
	
	@Override
	public void onEnable() {
		
		
		this.getCommand("of").setExecutor(new Commands(this));
		
		
		
	}
	
	//////////
	
	
}
