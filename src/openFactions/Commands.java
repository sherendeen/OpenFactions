package openFactions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import openFactions.CustomNations;

public class Commands implements CommandExecutor{
	
	CustomNations plugin;
	public Commands(CustomNations plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] extraArguments) {
		//Player player = (Player) sender;
		if(command.getName().equalsIgnoreCase("of")) {
			if(extraArguments.length < 1) {
				//TODO: Add list of commands, kinda like a /help for our plugin
				return false;
			}
			else if(extraArguments[0].equalsIgnoreCase("create")) {
				//TODO: create faction
				//player.sendMessage(ChatColor.RED + "Attempting to create faction " + extraArguments[1]);
				System.out.println("Attempting to create faction");
				
				
				Faction faction = new Faction(extraArguments[1], sender.getName());
				Faction.serialize(faction, "faction_"+faction.getSerialUUID()+".fbin");
				CustomNations.factions.add(faction);
				
				return true;
			}
			//player.sendMessage("oof my dude");
			return true;
		}
//		maybe make use of switch block for commands in the future
//		switch(command.getName().toLowerCase())
//		{
//			
//		}
		
		
		return false;

	}

}
