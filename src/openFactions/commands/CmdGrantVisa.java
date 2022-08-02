package openFactions.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.objects.Visa;
import openFactions.util.Helper;
import openFactions.util.constants.MsgPrefix;

public class CmdGrantVisa  {
	
	

	public boolean handle(CommandSender sender, String[] extraArguments) {

		UUID visaHolder = null;
		Player player = (Player) sender;
		Date currentDate = new Date();
		Date expirationDate = null;
		String expirationDateString = null;
		String visaClass = "0";
		int visaClassInteger = 0;

		// Sender has to specify a player, more specifically, a real one
		if (extraArguments.length < 2) {
			sender.sendMessage(MsgPrefix.ERR + "You must specify a player!");
			return true;
		}

		// If the player is not real, exit.
		try {
			visaHolder = Helper.getUuidFromPlayerName(extraArguments[1]);
		} catch (NullPointerException e) {
			sender.sendMessage(MsgPrefix.ERR + "You must specify a real player!");
			return true;
		}

		if (visaHolder != null) {

			if (Helper.isPlayerInAnyFaction(player.getName()) == false) {
				sender.sendMessage(MsgPrefix.ERR + "You are not in a faction!");
				return true;
			}

			Faction senderFaction = Helper.getPlayerFaction(player.getUniqueId());
			ArrayList<Visa> senderFactionVisaList = senderFaction.getVisas();

			// Check to see if the player already has a visa.
			for (int i = 0; i < senderFactionVisaList.size(); i++) {
				Visa v = senderFactionVisaList.get(i);
				if (v.getVisaHolder().equals(visaHolder)) {
					sender.sendMessage(MsgPrefix.OK + "That player already has a visa.");
					return true;
				}
			}
			switch (extraArguments.length) {
			// If there is only a player name in the arguments, grant a class 0 visa with no
			// expiration date.
			case (2):
				Visa visa = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder,
						visaClassInteger);
				senderFaction.addVisa(visa);
				sender.sendMessage(MsgPrefix.OK + "Granted " + extraArguments[1] + " a Class " + visaClass
						+ " visa for " + senderFaction.getName());
				return true;
			case (3):
				try {
					expirationDateString = extraArguments[2];
					expirationDate = Helper.dateFormat.parse(expirationDateString);
				} catch (ParseException e) {
					e.printStackTrace();
					/* This is fine, if it's not a date, then it is likely to be a visa class */}
				// If there is a second argument, but it is an expiration date, then the
				// expiration date will be the 2nd argument, and the class will be zero.
				if (expirationDate instanceof Date) {
					Visa visa2 = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder,
							visaClassInteger);
					senderFaction.addVisa(visa2);
					sender.sendMessage(MsgPrefix.OK + "Granted " + extraArguments[1] + " a Class " + visaClass
							+ " visa for " + senderFaction.getName() + " until " + expirationDateString);
					return true;
				}
				// If there is a second argument, but it is not an expiration date, then the
				// visa class will be the 2nd argument
				try {
					visaClass = extraArguments[2];
					visaClassInteger = Integer.valueOf(visaClass);
					if (visaClassInteger > 5 || visaClassInteger < 0) {
						sender.sendMessage(MsgPrefix.ERR + "Visa class must be a number from 0 to 4");
						return true;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(MsgPrefix.ERR + "Visa class must be a number");
					return true;
				}
				Visa visa2 = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder,
						visaClassInteger);
				senderFaction.addVisa(visa2);
				sender.sendMessage(MsgPrefix.OK + "Granted " + extraArguments[1] + " a Class " + visaClass
						+ " visa for " + senderFaction.getName());
				return true;
			// If there are three arguments, then they will represent Player, Expiration
			// Date and Class
			case (4):
				// Class has to be from 0-4
				// Expiration date must be valid
				try {
					expirationDateString = extraArguments[2];
					expirationDate = Helper.dateFormat.parse(expirationDateString);
					visaClass = extraArguments[3];
					visaClassInteger = Integer.valueOf(visaClass);
					if (visaClassInteger > 5 || visaClassInteger < 0) {
						sender.sendMessage(MsgPrefix.ERR + "Visa class must be a number from 0 to 4");
						return true;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(MsgPrefix.ERR + "Visa class must be a number from 0 to 4");
					return true;
				} catch (ParseException e) {
					sender.sendMessage(MsgPrefix.ERR + "Incorrect format, the correct date format is mm/dd/yyyy");
					return true;
				}
				Visa visa3 = new Visa(currentDate, expirationDate, senderFaction.getName(), visaHolder,
						visaClassInteger);
				senderFaction.addVisa(visa3);
				sender.sendMessage(MsgPrefix.OK + "Granted " + extraArguments[1] + " a Class " + visaClass
						+ " visa for " + senderFaction.getName() + " until " + expirationDateString);
				return true;
			}
		}
		return false;
	}
}
