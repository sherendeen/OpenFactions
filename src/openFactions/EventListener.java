package openFactions;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;

public class EventListener implements Listener {
private CustomNations plugin;
    
    public EventListener(CustomNations plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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
