package openFactions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import openFactions.objects.enums.Cmd;
import openFactions.util.constants.MsgPrefix;

public class CmdHelp {

	public boolean handle(CommandSender sender, String[] extraArguments) {
		sender.sendMessage(MsgPrefix.INFO + "--- OpenFactions Commands ---");
		for (int i = 0; i <  Cmd.values().length; i++) {
			sender.sendMessage(ChatColor.AQUA + Cmd.values()[i].toString());
		}
		return true;
	}

}
