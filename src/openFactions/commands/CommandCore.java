package openFactions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import openFactions.CustomNations;
import openFactions.util.Helper;

public class CommandCore implements CommandExecutor {

	private CmdAddPermission cmdAddPermission = new CmdAddPermission();
	private CmdAssignToGroup cmdAssignToGroup = new CmdAssignToGroup();
	private CmdClaimLand cmdClaimLand = new CmdClaimLand();
	private CmdCreateFaction cmdCreateFaction = new CmdCreateFaction();
	private CmdCreateGroup cmdCreateGroup = new CmdCreateGroup();
	private CmdChangeFactionDescription cmdChangeFactionDescription = new CmdChangeFactionDescription();
	private CmdJoinFaction cmdJoinFaction = new CmdJoinFaction();
	private CmdListFactions cmdListFactions = new CmdListFactions();
	private CmdListClaims cmdListClaims = new CmdListClaims();
	private CmdLeaveFaction cmdLeaveFaction = new CmdLeaveFaction();
	private CmdChangeFactionName cmdChangeFactionName = new CmdChangeFactionName();
	private CmdRemovePermission cmdRemovePermission = new CmdRemovePermission();
	private CmdWhoOwns cmdWhoOwns = new CmdWhoOwns();
	private CmdUnclaimLand cmdUnclaimLand = new CmdUnclaimLand();
	private CmdUnclaimAll cmdUnclaimAll = new CmdUnclaimAll();
	private CmdWhoIs cmdWhoIs = new CmdWhoIs();
	private CmdSetPermission cmdSetPermission = new CmdSetPermission();
	private CmdShowInfo cmdShowInfo = new CmdShowInfo();
	private CmdShowFactionInfo cmdShowFactionInfo = new CmdShowFactionInfo();
	private CmdShowGroup cmdShowGroup = new CmdShowGroup();
	private CmdSetRelation cmdSetRelation = new CmdSetRelation();
	private CmdShowRelations cmdShowRelations = new CmdShowRelations();
	private CmdGrantVisa cmdGrantVisa = new CmdGrantVisa();
	private CmdRevokeVisa cmdRevokeVisa = new CmdRevokeVisa();
	private CmdCheckVisa cmdCheckVisa = new CmdCheckVisa();
	private CmdVote cmdVote = new CmdVote();
	private CmdPropose cmdPropose = new CmdPropose();
	private CmdHelp cmdHelp = new CmdHelp();
	private CmdPillage cmdPillage = new CmdPillage();
	private CmdCede cmdCede = new CmdCede();
	private CmdSetGroup cmdSetGroup = new CmdSetGroup();
	
	private OFCmdDiplomacy ofCmdDiplomacy = new OFCmdDiplomacy();
	
	// plugin reference
	private static CustomNations main;
	
	
	public static CustomNations getMain() {
		return main;
	}

	public CommandCore(CustomNations main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] extraArguments) {
		Player player = Helper.validateCommandSender(sender);
		
		if (!command.getName().equalsIgnoreCase("of")) {
			return false;
		}
		
		if (extraArguments.length < 1) {
			return false;
		}
	
		Helper.dateFormat.setLenient(false);
		// check subcommands
		switch (extraArguments[0].toLowerCase()) {
		case "addpermission":
		case "ap":
			
			return cmdAddPermission.handle(sender, command, extraArguments);
		case "assign":
			
			return cmdAssignToGroup.handle(sender, extraArguments);
			
		case "claim":
			//Keeping things neat by putting the bulk of the code outside this case block
			return cmdClaimLand.handle(sender,command,extraArguments);
			
		case "create":
			
			return cmdCreateFaction.handle(sender, extraArguments);
		
		case "pillage" :
			return cmdPillage.handle(sender, command);
		
		case "cede" :
			return cmdCede.handle(sender, command, extraArguments);
			
		case "creategroup":
		case "cg":
			return cmdCreateGroup.handle(sender, extraArguments);
			
		case "desc":
			return cmdChangeFactionDescription.handle(sender,extraArguments);
			
		case "join":
			
			return cmdJoinFaction.handle(sender, extraArguments);
			
		case "list":
			
			return cmdListFactions.handle(sender);
			
		case "listclaims":
			
			return cmdListClaims.handle(sender,player);
			
		case "leave":
		
			return cmdLeaveFaction.handle(sender);
		
		case "name":
			
			return cmdChangeFactionName.handle(sender,extraArguments);
			
		case "removepermission":
		case "rp":
			
			return cmdRemovePermission.handle(sender, command, extraArguments);
		
		case "owns":
			
			return cmdWhoOwns.handle(sender);
			
		case "unclaim":
			
			return cmdUnclaimLand.handle(sender,command,extraArguments);
			
		case "unclaimall":
			
			return cmdUnclaimAll.handle(sender);
			
		case "whois":
			
			return cmdWhoIs.handle(sender,extraArguments);

		case "setgroup":
			
			return cmdSetGroup.handle(sender,extraArguments);
			
		case "setpermission":
		case "sp":
			// does not do anything
			return cmdSetPermission.handle(sender,command,extraArguments);
			
		case "info":
			
			return cmdShowInfo.handle(sender);
			
		case "show":
			return cmdShowFactionInfo.handle(sender, extraArguments);
			
		case "showgroup":
			
			return cmdShowGroup.handle(sender, extraArguments);
			
		case "setrelation":
			//true = command will fully execute
			return cmdSetRelation.handle(sender,extraArguments,true); 
			
		case "showrelations":
			
			return cmdShowRelations.handle(sender, extraArguments);

		case "grantvisa":
			
			return cmdGrantVisa.handle(sender, extraArguments);
		case "revokevisa":
			
			return cmdRevokeVisa.handle(sender, extraArguments);
		case "checkvisa":
			
			return cmdCheckVisa.handle(sender, extraArguments);
			
		case "vote":
			return cmdVote.handle(sender,extraArguments,player);
			
		case "propose":
			return cmdPropose.handle(sender,extraArguments,player);

		case "help":
		default:
			return cmdHelp.handle(sender,extraArguments);
		}	
	}	
}
