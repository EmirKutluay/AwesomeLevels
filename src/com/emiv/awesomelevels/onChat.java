package com.emiv.awesomelevels;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class onChat implements Listener {

	Main plugin;
	public onChat(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onChatPlayer(AsyncPlayerChatEvent e) {
		
		Player p = e.getPlayer();
		p.setDisplayName(p.getName());
		String format = e.getFormat();
		if (plugin.getConfig().contains("level" + plugin.getLYaml().getString(p.getName()) + "Prefix")) {
			format = format.replace("awesomeLevel", ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("level" + plugin.getLYaml().getString(p.getName()) + "Prefix").replace("%level%", plugin.getLYaml().getString(p.getName()))));
		} else {
			format = format.replace("awesomeLevel", ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("levelPrefix").replace("%level%", plugin.getLYaml().getString(p.getName()))));
		}		
		e.setFormat(format);
	}
	
}
