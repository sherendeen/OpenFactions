package openFactions.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import openFactions.CustomNations;
import openFactions.commands.CommandCore;
import openFactions.objects.Faction;
import openFactions.objects.LandClaim;
import openFactions.util.constants.MsgPrefix;

public class LandPillageEvent extends Event implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    boolean cancelled;
    boolean success; 
    CustomNations plugin = CommandCore.getMain();
    Faction pillager;
    Faction pillaged; 
    LandClaim pillagedClaim;

    public LandPillageEvent(Faction pillager, Faction pillaged, LandClaim pillagedClaim) {
    	this.pillager = pillager;
    	this.pillaged = pillaged;
    	this.pillagedClaim = pillagedClaim;
    }
    
    public boolean pillageLand() {
    	
    	
    	if(!(pillager.getClaims().contains(pillagedClaim))) {
        	  Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
        	  @Override
        	  public void run() {
//        		  if(cancelled == true) {
//            		  success = false;
//        		  }

        		  //debug
        		  System.out.println();
        		  System.out.println(MsgPrefix.DEBUG + pillagedClaim.toString());
        		  
//        		  
//        		  pillager.getClaims().add(pillagedClaim);
//        		  pillaged.getClaims().remove(pillagedClaim);
//        		  
        		  pillager.addClaim(pillagedClaim);
        		  pillaged.removeClaim(pillagedClaim);
        		  
        		  success = true;
        		  plugin.getServer().broadcastMessage("debug");
        	  }
        	}, 100L);
        	  return true;
    	}
    	
    	return false; 
    	
    }
    
    public boolean getResult() {
    	return success;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
