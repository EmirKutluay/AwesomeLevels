package com.emiv.awesomelevels;

import java.io.IOException;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class onKill implements Listener {

	Main plugin;
	public onKill(Main instance) {
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
	public void onKillEvent(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			String pName = e.getEntity().getKiller().getName();
			int pLevel = Integer.valueOf(plugin.getLYaml().getString(pName));
			String pNextLevel = String.valueOf(pLevel + 1);
			if (plugin.getReqYaml().contains("Level" + pNextLevel + ".Kill.Type") && plugin.getReqYaml().contains("Level" + pNextLevel + ".Kill.Amount")) {
				String type = plugin.getReqYaml().getString("Level" + pNextLevel + ".Kill.Type");
				if (e.getEntityType() == getEntityByName(type)) {
					if (!plugin.getSYaml().contains(pName + ".Kill." + type)) {
						plugin.getSYaml().set(pName + ".Kill." + type, 1);
					} else {
						if (plugin.getSYaml().getInt(pName + ".Kill." + type) < plugin.getReqYaml().getInt("Level" + pNextLevel + ".Kill.Amount")) {
							plugin.getSYaml().set(pName + ".Kill." + type, plugin.getSYaml().getInt(pName + ".Kill." + type) + 1);
						}
					}
				}
			}
		}
		save();
	}
	
	
	public EntityType getEntityByName(String name) {
        for (EntityType type : EntityType.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}
