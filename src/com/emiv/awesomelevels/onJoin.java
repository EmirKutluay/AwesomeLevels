package com.emiv.awesomelevels;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {

	Main plugin;
	public onJoin(Main instance) {
		plugin = instance;
	}
	
	public void Save() {
		try {
			plugin.getLYaml().save(plugin.getLFile());
			plugin.getPYaml().save(plugin.getPFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onJoinPlayer(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!plugin.getLYaml().contains(p.getName())) {
			plugin.getLYaml().set(p.getName(), String.valueOf(0));
		}
		String[] effects = {"FAST_DIGGING", "FIRE_RESISTANCE", "INCREASE_DAMAGE", "JUMP", "NIGHT_VISION", "REGENERATION", "SPEED", "WATER_BREATHING"};
		for (String s : effects) {
			if (!plugin.getPYaml().contains(p.getName() + "." + s)) {
				plugin.getPYaml().set(p.getName() + "." + s, 0);
			}
		}
		Save();
	}

}
