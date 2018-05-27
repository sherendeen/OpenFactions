package openFactions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;

public class Commands implements CommandExecutor{
	
	CustomNations plugin;
	public Commands(CustomNations plugin) {
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] extraArguments) {
		Player player = (Player)sender;
		if(command.getName().equalsIgnoreCase("of")) {
			player.sendMessage("oof my dude");
			return true;
		}
		// TODO Add commands
		return false;
	}

}
