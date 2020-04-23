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
				
				
				Faction faction = new Faction(extraArguments[1], ((Player) sender).getUniqueId());
				
				CustomNations.factions.add(faction);
				Faction.serialize(faction, "faction_"+CustomNations.factions.lastIndexOf(faction)+"_"+faction.getSerialUUID()+".fbin");
				return true;
			} else if(extraArguments[0].equalsIgnoreCase("list")) {
				
				sender.sendMessage("List of factions - Output");
				
				for ( int i = 0 ; i < CustomNations.factions.size(); i++ ) {
					sender.sendMessage(CustomNations.factions.get(i).toString());
				}
				
			} else if(extraArguments[0].equalsIgnoreCase("show")) {
				
				String facStrQuery = extraArguments[1];
				
				for ( Faction fac : CustomNations.factions) {
					if (fac.getSerialUUID().toString() == facStrQuery) {
						sender.sendMessage(fac.toString());
						return true;
					}
				}
				
				return false;
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
