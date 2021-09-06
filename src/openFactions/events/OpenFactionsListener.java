package openFactions.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import openFactions.CustomNations;
import openFactions.util.Helper;

public class OpenFactionsListener implements Listener {
	private CustomNations plugin;
    
    public OpenFactionsListener(CustomNations plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void LandPillageEvent(LandPillageEvent event) {
    	event.pillageLand();
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Helper.HandlePlayerEvent(event);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Helper.HandlePlayerEvent(event);
    }
}
