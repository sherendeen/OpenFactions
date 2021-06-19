package openFactions.util;

import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import openFactions.CustomNations;
import openFactions.objects.Faction;
import openFactions.objects.Group;
import openFactions.objects.LandClaim;
import openFactions.objects.Visa;
import openFactions.objects.enums.Can;
import openFactions.objects.enums.RelationshipType;

public class Helper {
	public static boolean isSpecifiedLandClaimInsideAnyFaction(LandClaim lc) {
        for (Faction fac : CustomNations.factions) {
            for (LandClaim landClaim : fac.getClaims()) {
                if (lc.equals(landClaim)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isSpecifiedChunkInsideAnyFaction(Chunk chunk) {
        for (Faction fac : CustomNations.factions) {
            for (LandClaim landClaim : fac.getClaims()) {
                if (landClaim.getClaimedChunk().equals(chunk)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static ArrayList<Faction> returnFactionObjectsWhereChunkIsFoundIn(final Chunk chunk) {
        ArrayList<Faction> result = new ArrayList<Faction>();
        for (Faction fac : CustomNations.factions) {
            for (LandClaim landClaim : fac.getClaims()) {
                if (landClaim.getClaimedChunk().equals(chunk)) {
                    result.add(fac);
                }
            }
        }
        return result;
    }
    
    public static LandClaim returnLandClaimContainingSpecifiedChunk(Chunk chunk) {
        for (Faction fac : CustomNations.factions) {
            for (LandClaim landClaim : fac.getClaims()) {
                if (landClaim.getClaimedChunk().equals(chunk)) {
                    return landClaim;
                }
            }
        }
        return null;
    }
    
    public static ArrayList<Faction> returnFactionObjectsWhereClaimIsFoundIn(LandClaim lc) {
        ArrayList<Faction> result = new ArrayList<Faction>();
        for (Faction fac : CustomNations.factions) {
            for (LandClaim landClaim : fac.getClaims()) {
                if (lc.equals(landClaim)) {
                    result.add(fac);
                }
            }
        }
        return result;
    }
    
    public static Group createAdminGroup() {
        return new Group("admin", false, Period.ZERO, false, 1, Can.ASSIGN_GROUPS, 
        		Can.CHANGE_FACTION_DESC,
        		Can.CHANGE_FACTION_NAME,
        		Can.CLAIM, Can.DISBAND, 
        		Can.EDIT_CLAIM,
        		Can.EDIT_CLAIM_SETTINGS, 
        		Can.EDIT_GROUPS,
        		Can.OVERRIDE_CLAIM_SETTINGS,
        		Can.KICK, Can.MAKE_OR_REMOVE_GROUPS, 
        		Can.SET_RELATION, 
        		Can.SET_VISA, 
        		Can.UNCLAIM,
        		Can.UNCLAIM_ALL );
    }
    
    public static Group createCommonGroup() {
        return new Group("common", false, Period.ZERO, false, 1,  
        		Can.CHANGE_FACTION_DESC,
        		Can.EDIT_CLAIM 
        );
    }
    
    public static boolean isPlayerInGroup(UUID uuid, Group group) {
        for (UUID uniqueId : group.getMembers()) {
            if (uniqueId.equals(uuid)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean doesGroupHavePermission(Can can, Group group) {
        for (int i = 0; i < group.getGroupPermissions().size(); ++i) {
            if (group.getGroupPermissions().get(i) == can) {
                return true;
            }
        }
        return false;
    }
    
    public static Group removeAllMembersFromGroup(Group group) {
        for (int i = 0; i < group.getMembers().size(); ++i) {
            group.removeMember(group.getMembers().get(i));
        }
        Group result = group;
        return result;
    }
    
    public static boolean doesStringMatchAValidPermission(String string) {
        for (int i = 0; i < Can.values().length; ++i) {
            if (Can.values()[i].toString().equalsIgnoreCase(string)) {
                return true;
            }
            if (Can.values()[i].toString().replace("_", "").equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
    
    public static ArrayList<Visa> getVisasOfPlayer(final UUID uuid) {
        ArrayList<Visa> list = new ArrayList<Visa>();
        for (Faction fac : CustomNations.factions) {
            for (Visa visa : fac.getVisas()) {
                if (visa.getVisaHolder().equals(uuid)) {
                    list.add(visa);
                }
            }
        }
        return list;
    }
    
    /**
     * Returns a nicely formated visa report.
     * 
     * TODO: make more readable
     * Unfortunately when the jar was decompiled, this
     * function was made totally unreadable. 
     * @param visa the player visa
     * @return an array of strings containing a line by line representation of a visa report
     */
    public static String[] getVisaReport(Visa visa) {
        String[] fields = { "Visa Holder: " + getPlayerNameFromUuid(visa.getVisaHolder()) + ".", 
        		"Issuer: " + visa.getIssuer() + ".", 
        		"Status: " + (visa.checkStatus(new Date(), visa.getExpirationDate()) ? "EXPIRED." : "ACTIVE."),
        		"Class: " + visa.getClass() + ".", 
        		"Issue Date: " + visa.getIssueDate() + ".", 
        		"Expiry Date: " + visa.getExpirationDate() + "." };
        return fields;
    }
    
    public static int getFactionFromUUID(final ArrayList<Faction> factionsList, final UUID uuid) {
        for (int i = 0; i < factionsList.size(); ++i) {
            if (factionsList.get(i).getSerialUUID() == uuid) {
                return i;
            }
        }
        return -1;
    }
    
    public static Faction returnFactionThatPlayerIsIn(final UUID playerUUID) {
        for (final Faction fac : CustomNations.factions) {
            for (final UUID member : fac.getMembers()) {
                if (member.equals(playerUUID)) {
                    return fac;
                }
            }
        }
        return null;
    }
    
    public static ArrayList<String> returnListOfNames(final ArrayList<UUID> uuids) {
        final ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < uuids.size(); ++i) {
            names.add(getPlayerNameFromUuid(uuids.get(i)));
        }
        return names;
    }
    
    public static boolean isPlayerInAnyFaction(final String name) {
        final UUID uuid = getUuidFromPlayerName(name);
        boolean result = false;
        for (final Faction fac : CustomNations.factions) {
            result = isPlayerInSpecifiedFaction(uuid, fac);
        }
        return result;
    }
    
    public static boolean isPlayerInSpecifiedFaction(final UUID uuid, final Faction faction) {
        if (faction != null) {
            for (int i = 0; i < faction.getMembers().size(); ++i) {
                if (faction.getMembers().get(i).equals(uuid)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Faction getFactionByFactionName(String name) {
        for (final Faction faction1 : CustomNations.factions) {
            if (faction1.getName().equalsIgnoreCase(name)) {
                return faction1;
            }
        }
        return null;
    }
    
    /**
     * Returns true if a faction's name equals the specified string
     * @param name name of the faction
     * @return whether the faction exists or not
     */
    public static boolean doesFactionExist(String name) {
    	
    	if ( CustomNations.factions.size() < 1) {
    		return false;
    	}
    	
        for (Faction fac : CustomNations.factions) {
            if (fac.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public static ArrayList<String> getListOfGroupNames(final ArrayList<Group> groups) {
        final ArrayList<String> results = new ArrayList<String>();
        for (final Group group : groups) {
            results.add(group.getName());
        }
        return results;
    }
    
    public static String[] getArrayOfCoordinates(final ArrayList<LandClaim> claims) {
        String[] array = new String[claims.size()];
        for (int i = 0; i < array.length; ++i) {
        	//if the current index is zero, 
        	//then we make the string start with an opening
        	//square bracket, otherwise we start with a comma
            array[i] = ((i == 0) ? "[" : (",["));
            //then we add the relevant information
            array[i] += claims.get(i).getChunkX() + ", " + claims.get(i).getChunkZ() + "]";
        }
        return array;
    }
    
    public static void HandlePlayerEvent(BlockBreakEvent event) {
    	Player player = event.getPlayer();
        Faction fac = null;
        // check to see if the player is even in a faction
        if (isPlayerInAnyFaction(player.getDisplayName())) {
            fac = returnFactionThatPlayerIsIn(player.getUniqueId());
        }
        
        LandClaim lc = null;
        // if the specified chunk is not inside a faction, stop checking
        //and let the event continue
        if (!isSpecifiedChunkInsideAnyFaction(event.getBlock().getChunk())) {
            return;
        }
        
        lc = returnLandClaimContainingSpecifiedChunk(event.getBlock().getChunk());
        // if the player is not in a faction but the landclaim is owned
        if (fac == null && lc != null) {
        	//cancel event
            event.setCancelled(true);
            return;
        }
        
        Group playerGroup = getGroupPlayerIsIn(fac, player.getUniqueId());
        //if this faction does not own the landclaim, cancel event
        if (!returnFactionObjectsWhereClaimIsFoundIn(lc).contains(fac)) {
            event.setCancelled(true);
            return;
        }
        
        // if the landclaim's exclusive group is NOT NULL
        // and it does not equal that of the player group
        // and the player group does not have permission to override claim settings
        if (lc.getExclusiveGroup() != null && 
        		!lc.getExclusiveGroup().equals(playerGroup) && 
        		!doesGroupHavePermission(Can.OVERRIDE_CLAIM_SETTINGS, playerGroup)) {
            event.setCancelled(true);
            return;
        }
        // if the player group DOES NOT have the edit_claim permission
        if (!doesGroupHavePermission(Can.EDIT_CLAIM, playerGroup)) {
        	//cancel the event
            event.setCancelled(true);
        }
    	
    }
    
    public static void HandlePlayerEvent(BlockPlaceEvent event) {
    	Player player = event.getPlayer();
        Faction fac = null;
        //if the player is in a faction.. get their faction
        if (isPlayerInAnyFaction(player.getDisplayName())) {
            fac = returnFactionThatPlayerIsIn(player.getUniqueId());
        }
        
        LandClaim lc = null;
        //if the specified chunk is NOT inside a faction
        //then we stop any further checks and
        //the event is not cancelled!
        if (!isSpecifiedChunkInsideAnyFaction(event.getBlock().getChunk())) {
            return;
        }
        
        lc = returnLandClaimContainingSpecifiedChunk(event.getBlock().getChunk());
        // if the faction is null and the landclaim is not null
        if (fac == null && lc != null) {
        	//cancel the event because we know that
        	//the land is owned by someone but the player is not
        	//in a faction that could possibly interact with it
            event.setCancelled(true);
            return;
        }
        
        Group playerGroup = getGroupPlayerIsIn(fac, player.getUniqueId());
        //if this faction is not among the list of factions which possess
        //this given claim
        if (!returnFactionObjectsWhereClaimIsFoundIn(lc).contains(fac)) {
        	//then cancel the event
            event.setCancelled(true);
            return;
        }
        
        // if the landclaim's exclusive group is NOT NULL
        // and it does not equal that of the player group
        // and the player group does not have permission to override claim settings
        if (lc.getExclusiveGroup() != null && 
        		!lc.getExclusiveGroup().equals(playerGroup) && 
        		!doesGroupHavePermission(Can.OVERRIDE_CLAIM_SETTINGS, playerGroup)) {
            // then we cancel the event
            event.setCancelled(true);
            return;
        }
        
        // if the player group DOES NOT have the edit_claim permission
        if (!doesGroupHavePermission(Can.EDIT_CLAIM, playerGroup)) {
        	//then we cancel the event
            event.setCancelled(true);
        }
    }
    
    public static ArrayList<String> getArrayListOfCoordinates(final ArrayList<LandClaim> claims) {
        ArrayList<String> coords = new ArrayList<String>();
        for (LandClaim lc : claims) {
            String result = lc.getClaimDescriptor().isEmpty() ? "] " : ("] - ``" + lc.getClaimDescriptor() + "`` ");
            coords.add(" [" + lc.getChunkX() + ", " + lc.getChunkZ() + result);
        }
        return coords;
    }
    
    public static UUID getUuidFromPlayerName(String name) {
        final Player userPlayer = Bukkit.getPlayer(name);
        return userPlayer.getUniqueId();
    }
    
    public static String getPlayerNameFromUuid(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        return player.getDisplayName();
    }
    
    public static Group transferMembersFromGroup(Group oldGroup, Group newGroup) {
        newGroup.setMembers(oldGroup.getMembers());
        removeAllMembersFromGroup(oldGroup);
        return newGroup;
    }
    
    public static Group getGroupPlayerIsIn(Faction fac, UUID uniqueId) {
        Group result = null;
        for (final Group group : fac.getGroups()) {
            for (final UUID uuid : group.getMembers()) {
                if (uuid.equals(uniqueId)) {
                    result = group;
                }
            }
        }
        return result;
    }
    
    public static boolean doesGroupExist(String name, Faction fac) {
        for (final Group g : fac.getGroups()) {
            if (g.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getRelationshipString(Faction faction) {
        return faction.getRelationships().toString();
    }
    
    public static String getRelationshipTypeString(Faction faction) {
        return faction.getRelationships().get(faction.getName()).toString();
    }
    
    public static RelationshipType getRelationshipType(Faction faction) {
        return faction.getRelationships().get(faction.getName());
    }
    
    public static Group getGroupFromFactionByName(String name, Faction fac) {
        for (final Group g : fac.getGroups()) {
            if (g.getName().equalsIgnoreCase(name)) {
                return g;
            }
        }
        return null;
    }
}
