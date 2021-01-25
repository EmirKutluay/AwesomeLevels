package com.emiv.awesomelevels;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import net.md_5.bungee.api.ChatColor;

public class LevelListener implements Listener {

	Main plugin;
	public LevelListener(Main instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (ChatColor.translateAlternateColorCodes('&', e.getView().getTitle()).equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("levelMenuTitle")))) {
			if (e.getCurrentItem() != null) {
				e.setCancelled(true);
				switch (e.getCurrentItem().getType()) {
				case LIME_STAINED_GLASS_PANE:
					rankupCmd(p);
					p.closeInventory();
					break;
				case RED_STAINED_GLASS_PANE:
					p.closeInventory();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("serverPrefix") + " " + plugin.getMYaml().getString("LevelUpNotMet")));
					break;
				case BLUE_STAINED_GLASS_PANE:
					p.closeInventory();
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("serverPrefix") + " " + plugin.getMYaml().getString("LevelUpMaxLevel")));
					break;
				default:
					return;
				}
			}
		}
	}
	
	public void Save() {
		try {
			plugin.getLYaml().save(plugin.getLFile());
			plugin.getPYaml().save(plugin.getPFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void rankupCmd(Player p) {
		int playerLevel = Integer.valueOf(plugin.getLYaml().getString(p.getName()));
		int nextLevel = playerLevel + 1;
		Double price = (double) plugin.getReqYaml().getInt("Level" + nextLevel + ".Coin");
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("serverPrefix") + " " + plugin.getMYaml().getString("LevelUpSuccess").replace("%level%", String.valueOf(playerLevel + 1))));
		Main.econ.withdrawPlayer(p, price);
		plugin.getLYaml().set(p.getName(), String.valueOf(playerLevel + 1));
		if (plugin.getConfig().getBoolean("levelUpSound")) {
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
		}
		if (plugin.getConfig().getBoolean("levelUpFirework")) {
			spawnFireworks(p.getLocation(), 1);
		}
		if (plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Reward.Message")) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getRYaml().getString("Level" + String.valueOf(playerLevel + 1) + ".Reward.Message")));
		}
		if (plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Commands.Console")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), plugin.getRYaml().getString("Level" + String.valueOf(playerLevel + 1) + ".Commands.Console").replace("%player%", p.getName()));
		}
		if (plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Commands.Player")) {
			p.performCommand(plugin.getRYaml().getString("Level" + String.valueOf(playerLevel + 1) + ".Commands.Player"));
		}
		if (plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Effect.Type") && plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Effect.Tier")) {
			plugin.getPYaml().set(p.getName() + "." + plugin.getRYaml().getString("Level" + String.valueOf(playerLevel + 1) + ".Effect.Type"), plugin.getRYaml().getInt("Level" + String.valueOf(playerLevel + 1) + ".Effect.Tier"));
		}
		Save();
	}
	
    public static void spawnFireworks(Location location, int amount){
        Location loc = location.add(0.0, 8.0, 0.0);
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
       
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
       
        fw.setFireworkMeta(fwm);
        fw.detonate();
       
        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }
}
