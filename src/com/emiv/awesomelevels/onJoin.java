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
		Save();
	}

}
