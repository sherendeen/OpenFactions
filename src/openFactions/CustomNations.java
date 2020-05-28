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
package openFactions;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Seth G. R. Herendeen
 *
 */
public class CustomNations extends JavaPlugin{
	
	public static ArrayList<Faction> factions = new ArrayList<Faction>(); 
	
	
	
	private World w ;
	public World getWorld() {
		return this.w;
	}
	
	@Override
	public void onEnable() {
		
		
		this.getCommand("of").setExecutor(new Commands(this));

		ArrayList<String> paths = new ArrayList<String>();
		//TODO: improve this getWorld() so that it isn't hardcoded like this
		//perhaps make it so that it uses whatever it is configured to use
		this.w = getServer().getWorld("world");
		
		try {
			Files.list(new File(System.getProperty("user.dir")).toPath()).forEach(path ->{
				
				if (path.toAbsolutePath().toString().endsWith(".fbin")) {
					System.out.println("*" + path);
					paths.add(path.toAbsolutePath().toString());
				}
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Attempting to deserialize...");
		for (int i = 0; i < paths.size(); i++ ) {
			CustomNations.factions.add(Faction.deserialize(paths.get(i)));
		}
		
		System.out.println("Loading saved chunks...");
		for(Faction fac : CustomNations.factions) {
			int i = 0;
			for (LandClaim lc : fac.getClaims()) {
				i++;
				System.out.println(i +"# Setting claimed chunk @ [X " +lc.getChunkX() + ", Z " + lc.getChunkZ() +"]");
				lc.setClaimedChunkFromCoordinates(lc.getChunkX(), lc.getChunkZ(), this);
			}
		}
		System.out.println("Done with chunks.");
			
	}
	
	/**
	 * When the server shuts down, or whenever the plugin is disabled,
	 * serialize the entire array list of faction objects.
	 */
	@Override
	public void onDisable() {
		
		System.out.println("Saving all faction files...");
		for(int i = 0; i < factions.size(); i++) {
			//serialize each of the faction objects in the list
			Faction.serialize(factions.get(i), factions.get(i).getAutoFileName());
		}
	}


	/**
	 * Deletes the faction save file (*.fbin)
	 * @param autoFileName the preformated file name (recommended)
	 */
	public static void deleteFactionSave(String autoFileName) {
		File file = new File(autoFileName);
		if(file.exists()) {
			boolean b = file.delete();
			System.out.println(autoFileName + " deletion: " + b);
		}
	}
	
	//////////
	
	
}
