/** 
Copyright (C) 2018-2020 Seth Herendeen; Samuel Inciarte

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
package openFactions;
**/
package openFactions.objects.enums;

public enum Can {
	ASSIGN_GROUPS,	
	ADD_PLAYERS,// manually adding players
	CHANGE_FACTION_DESC,	
	CHANGE_FACTION_NAME,	
	CLAIM,	
	CEDE,
	DISBAND,
	EDIT_CLAIM,	
	EDIT_CLAIM_SETTINGS,
	EDIT_GROUPS,
	OVERRIDE_CLAIM_SETTINGS,
	KICK,	
	PROPOSE_RESOLUTION,
	OPEN_CONTAINERS,
	MAKE_OR_REMOVE_GROUPS,
	TOGGLE_JOINABLE,//so that joining may be streamlined
	SET_FACTION_WARP,
	SET_RELATION,
	SET_VISA,
	UNCLAIM,	
	UNCLAIM_ALL,USE_FACTION_WARP,
	VOTE
}
