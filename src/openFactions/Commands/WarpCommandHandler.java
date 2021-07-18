package openFactions.Commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.PlayerInfo;
import openFactions.objects.Warp;
import openFactions.objects.enums.Can;
import openFactions.util.Helper;
import openFactions.util.constants.MsgHeader;
import openFactions.util.constants.MsgPrefix;
import openFactions.util.constants.MsgSeparator;

public class WarpCommandHandler implements CommandExecutor {

	private CustomNations plugin;

	// for teleportation
	private HashMap<String, Long> coolDowns = new HashMap<String, Long>();
	private final int COOLDOWN_TIME = 90;// TODO: Make Configurable

	public WarpCommandHandler(CustomNations plugin) {
		this.plugin = plugin;
	}

	/**
	 * command structure should consist of
	 * \ofw [args]
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] extraArguments) {
		
		//DEBUG: enter
		System.out.println(MsgPrefix.DEBUG + "Entering onCommand in WarpCommandHandler");
		
		Player player = Helper.validateCommandSender(sender);

		switch (extraArguments[0].toLowerCase()) {

		case "set":
		case "sethome":
			
			// inspecific
			return setHome(sender, extraArguments, player);
			
		case "setwarp":
			// DEBUG: show setwarp
			//System.out.println(this.getClass().getName() + MsgSeparator.COLON_TRIPLE + "``/of warp set`` executed");
			return setWarp(sender, extraArguments, player);
			
		case "list":
			
			//list warp
			return listWarps(sender,extraArguments, player);
			
		case "del":
			
			return deleteWarp(sender,extraArguments,player);
		case "warp":

			return teleportToWarp(sender, extraArguments, player);

		}

		return false;
	}

	private boolean setHome(CommandSender sender, String[] extraArguments, 
			Player player) {
		
		if (player == null) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot set homes as you are a console...");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot add faction homes since you are not in a faction.");
			return false;
		}
		
		if (!pi.getPlayerGroup().hasPermission(Can.SET_FACTION_WARP)) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot add faction homes since you are not in a group that has the permission set_faction_warp.");
			return false;
		}
		
		if (extraArguments[1] == null || extraArguments[1].equalsIgnoreCase("")) {
			sender.sendMessage(MsgPrefix.ERR + "You must specify a name for the home.");
			return false;
		}
		
		Faction playerFaction = pi.getPlayerFaction();
		
		int x = player.getLocation().getBlockX();
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ();
		
		Warp warp = new Warp(extraArguments[1],x, y, z, 
				plugin.getWorld().getName());
		
		playerFaction.addWarp(warp);
		
		sender.sendMessage(MsgPrefix.OK + "New warp `" +warp.getWarpName()+ 
				"` created at " +ChatColor.AQUA + "["+x+","+y+","+z+"]." );
		
		return true;
	}

	/**
	 * delete faction warp
	 * @param sender
	 * @param extraArguments
	 * @param player
	 * @return
	 */
	private boolean deleteWarp(CommandSender sender, String[] extraArguments, Player player) {
		if ( player  == null) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot delete faction warps since the console is not permitted to join a faction.");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot delete faction warps since you are not in a faction.");
			return false;
		}
		
		if (!pi.getPlayerGroup().hasPermission(Can.SET_FACTION_WARP)) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot delete warps since your faction group lacks the permission ``set_faction_warp.``");
			return false;
		}
		
		if (!Helper.warpExists(extraArguments[1], pi.getPlayerFaction())) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot delete this specified warp since it does not actually exist.");
			sender.sendMessage(MsgPrefix.INFO + "You can use /ofw list to see a list of warps.");
			return false;
		}
		
		// remove faction warp
		pi.getPlayerFaction().removeWarp(Helper.getWarpByName(extraArguments[1], pi.getPlayerFaction()));
		sender.sendMessage(MsgPrefix.INFO + "Removed warp `" + extraArguments[1] + ".`");
		
		return true;
	}

	/**
	 * List warps
	 * @param sender
	 * @param extraArguments
	 * @return
	 */
	private boolean listWarps(CommandSender sender, String[] extraArguments, Player player) {
		
		if (player == null) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot list faction warps since the console is not permitted to join a faction.");
			return false;
		}
		
		PlayerInfo pi = new PlayerInfo(player);
		
		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot view faction warps since you are not in a faction.");
			return false;
		}
		
		if (!(pi.getPlayerFaction().getWarps().size() >= 1)) {
			sender.sendMessage(MsgPrefix.INFO + "Your faction has no warps.");
			return true;
		}
		
		sender.sendMessage(MsgHeader.factionWarps);
		sender.sendMessage(MsgHeader.factionAttribute + "Faction" + MsgHeader.factionAttributeClose + pi.getPlayerFaction().getName());
		for(Warp w : pi.getPlayerFaction().getWarps()) {
			sender.sendMessage(MsgHeader.factionAttribute + w.getWarpName() + MsgHeader.factionAttributeClose);
			
			if (w.getAssociatedGroup() != null) {
				sender.sendMessage(MsgPrefix.LIST + "["+ w.getWarpName() + "] is associated with the group: " + w.getAssociatedGroup().getName());
			} else {
				sender.sendMessage(MsgPrefix.LIST + "[" + w.getWarpName() + "] is not associated with any particular group.");
			}
			
			
		}
		return true;
	}

	private long getSecondsLeft(Player player) {
		return ((coolDowns.get(player.getName()) / 1000) + COOLDOWN_TIME) - (System.currentTimeMillis() / 1000);
	}

	private boolean teleportToWarp(CommandSender sender, String[] extraArguments, Player player) {

		if (player == null) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}

		PlayerInfo pi = new PlayerInfo(player);

		if (!pi.isPlayerInAFaction()) {
			sender.sendMessage(MsgPrefix.ERR + "You cannot use faction warps when you are not in a faction.");
			return false;
		}

		if (!Helper.warpExists(extraArguments[1], pi.getPlayerFaction())) {
			sender.sendMessage(MsgPrefix.ERR + "No such warp exists.");
			return false;
		}
		Warp warp = Helper.getWarpByName(extraArguments[1], pi.getPlayerFaction());

		if (!pi.getPlayerGroup().hasPermission(Can.USE_FACTION_WARP)
				&& !warp.getAssociatedGroup().equals(pi.getPlayerGroup())) {

			sender.sendMessage(MsgPrefix.ERR + "Your group is not allowed to use faction warps.");
			sender.sendMessage(MsgPrefix.INFO + "Groups in your faction that can: "
					+ Helper.getGroupsByPermission(pi.getPlayerFaction(), Can.USE_FACTION_WARP));
			return false;
		}

		//
		// initiate teleport
		//
		//
		if (coolDowns.containsKey(player.getName())) {

			System.out.println(MsgPrefix.DEBUG + "Cooldowns contains key");

			long secondsLeft = getSecondsLeft(player);

			if (secondsLeft > 0) {
				sender.sendMessage(MsgPrefix.ERR + "You cannot teleport right now. " + secondsLeft + " seconds left.");
				return true;
			}

			coolDowns.put(player.getName(), System.currentTimeMillis());
		} else {
			coolDowns.put(player.getName(), System.currentTimeMillis());
		}

		player.teleport(new Location(plugin.getServer().getWorld(warp.getWorldName()), warp.getWarpX(), warp.getWarpY(),
				warp.getWarpZ()));

		return true;
	}

	/**
	 * Sets a new faction exclusive warp if possible
	 * 
	 * @param sender
	 * @param extraArguments
	 * @param player
	 * @return
	 */
	private boolean setWarp(CommandSender sender, String[] extraArguments, Player player) {

		if (player == null) {
			sender.sendMessage(MsgPrefix.ERR + "Console cannot set warps.");
			return false;
		}

		PlayerInfo pi = new PlayerInfo(player);

		if (!pi.isPlayerInAFaction() || !pi.isPlayerInAGroup()) {
			sender.sendMessage(
					MsgPrefix.ERR + "You cannot set faction warps when you are not in a faction or a faction group.");
			return false;
		}

		Faction playerFaction = pi.getPlayerFaction();
		Group playerGroup = pi.getPlayerGroup();

		// if the player is not in a group that can set faction warps...
		if (!Helper.doesGroupHavePermission(Can.SET_FACTION_WARP, playerGroup)) {
			sender.sendMessage(MsgPrefix.ERR + "Your faction does not allow your group (" + playerGroup.getName()
					+ ") to create new faction warps.");

			sender.sendMessage(MsgPrefix.INFO + "Groups your faction allows to set faction warps: ");

			ArrayList<Group> groups = Helper.getGroupsByPermission(playerFaction, Can.SET_FACTION_WARP);
			// list groups that have this permission/ability
			if (groups != null && groups.size() >= 1) {
				for (int i = 0; i < groups.size(); i++) {
					sender.sendMessage(ChatColor.AQUA + MsgPrefix.INFO + "" + i + 1 + ". " + groups.get(i).getName());
				}
			} else {
				// rare, highly unlikely, but it is theoretically possible
				// for a faction to remove from all groups that permission
				sender.sendMessage(MsgPrefix.OK + "No one, apparently. Is this supposed to happen?");
			}
			return false;
		}

		// by this point the player should have permission
		// first we'll check to see if there is a group specified
		System.out.println("extraArguments[0]:" + extraArguments[0]);
		
		// we need two arguments in order for this to work
		if ((extraArguments[1] != null // extraArguments[0] ==== warpName
				&& extraArguments[1] != "") && (extraArguments[2] != null && // === player group as string
						extraArguments[2] != "")) {

			if (Helper.doesGroupExist(extraArguments[1], playerFaction)) {

				int x = player.getLocation().getBlockX();
				int y = player.getLocation().getBlockY();
				int z = player.getLocation().getBlockZ();

				Warp warp = new Warp(extraArguments[2], x, y, z, plugin.getWorld().getName(),
						Helper.getGroupFromFactionByName(extraArguments[1], playerFaction));

				playerFaction.addWarp(warp);

				sender.sendMessage(MsgPrefix.OK + "New warp `" + warp.getWarpName() + "` created at [" + x + "," + y
						+ "," + z + "] for group " + Helper.getGroupFromFactionByName(extraArguments[1], playerFaction)
						+ ".");

				return true;
			} else {
				sender.sendMessage(
						MsgPrefix.ERR + "Can't assign group specific warp because specified group does not exist.");
				sender.sendMessage(MsgPrefix.INFO + "Try /of info to see a list of groups");

				return false;
			}

		}


		return false;
	}

}
