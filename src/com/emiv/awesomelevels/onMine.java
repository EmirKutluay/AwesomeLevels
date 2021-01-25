package com.emiv.awesomelevels;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onMine implements Listener {


	Main plugin;
	public onMine(Main instance) {
		plugin = instance;
	}
	
	void save() {
		try {
			plugin.getSYaml().save(plugin.getSFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerMine(BlockBreakEvent e) {
		if (e.getPlayer() instanceof Player) {
			String pName = e.getPlayer().getName();
			int pLevel = Integer.valueOf(plugin.getLYaml().getString(pName));
			String pNextLevel = String.valueOf(pLevel + 1);
			if (plugin.getReqYaml().contains("Level" + pNextLevel + ".Mine.Type") && plugin.getReqYaml().contains("Level" + pNextLevel + ".Mine.Amount")) {
				String type = plugin.getReqYaml().getString("Level" + pNextLevel + ".Mine.Type");
				if (e.getBlock().getType() == Material.getMaterial(type)) {
					if (!plugin.getSYaml().contains(pName + ".Mine." + type)) {
						plugin.getSYaml().set(pName + ".Mine." + type, 1);
					} else {
						if (plugin.getSYaml().getInt(pName + ".Mine." + type) < plugin.getReqYaml().getInt("Level" + pNextLevel + ".Mine.Amount")) {
							plugin.getSYaml().set(pName + ".Mine." + type, plugin.getSYaml().getInt(pName + ".Mine." + type) + 1);
						}
					}
				}
			}
		}

		save();
	}
}
