package com.emiv.awesomelevels;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class ReloadCommand implements CommandExecutor {

	Main main;
	public ReloadCommand(Main instance) {
		main = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("awesomelevels.reload")) {
				Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels");
				main.reloadConfig();
                plugin.getPluginLoader().disablePlugin(plugin);
                plugin.getPluginLoader().enablePlugin(plugin);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("serverPrefix") + " " + main.getMYaml().getString("Reloaded")));
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("serverPrefix") + " " +  main.getMYaml().getString("NoPermission")));
			}
		} else {
			Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels");
			main.reloadConfig();
            plugin.getPluginLoader().disablePlugin(plugin);
            plugin.getPluginLoader().enablePlugin(plugin);
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("serverPrefix") + " " + main.getMYaml().getString("Reloaded")));
		}
		return false;
	}

}
