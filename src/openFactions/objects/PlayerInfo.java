package openFactions.objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import openFactions.objects.enums.Can;
import openFactions.util.Helper;

public class PlayerInfo {

	private String playerName;
	private UUID playerUUID;
	private Faction playerFaction;
	private Group playerGroup;
	private ArrayList<Visa> playerVisas;
	private Player player;
	
	public PlayerInfo(final Player player) {
		
		this.playerUUID = player.getUniqueId();
		this.playerName = player.getName();
		
		this.playerFaction = Helper.getPlayerFaction(this.playerUUID);
		
		this.player = player;
		
		if(Helper.isPlayerInAnyFaction(Helper.getPlayerNameFromUuid(this.playerUUID))) {
			this.playerGroup = Helper.getGroupPlayerIsIn(playerFaction, this.playerUUID);
		}
		
		this.playerVisas = Helper.getVisasOfPlayer(this.playerUUID);
	}
	
	public ArrayList<Can> getPlayerPermissions() {
		return this.playerGroup.getGroupPermissions();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public UUID getPlayerUUID() {
		return playerUUID;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public Faction getPlayerFaction() {
		return playerFaction;
	}

	public void setPlayerFaction(Faction playerFaction) {
		this.playerFaction = playerFaction;
	}

	public Group getPlayerGroup() {
		return playerGroup;
	}

	public void setPlayerGroup(Group playerGroup) {
		this.playerGroup = playerGroup;
	}

	public ArrayList<Visa> getPlayerVisas() {
		return playerVisas;
	}

	public void setPlayerVisas(ArrayList<Visa> playerVisas) {
		this.playerVisas = playerVisas;
	}
	
	public boolean isPlayerInAFaction() {
		return (this.playerFaction != null) ? true : false;
	}
	
	public boolean isPlayerInAGroup() {
		return (this.playerGroup != null) ? true : false;
	}
	
	public boolean doesPlayerHaveAnyVisas() {
		return (this.playerVisas != null && this.playerVisas.size() > 0 ) ? true : false;
	}
	
}
