package com.emiv.awesomelevels;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class LevelCommand implements CommandExecutor {

	Main plugin;
	public LevelCommand(Main instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("awesomelevels.menu")) {
				applyUI(p);
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("serverPrefix") + " " + plugin.getMYaml().getString("NoPermission")));
			}
		}
		
		return false;
	}
	
	public void applyUI(Player p) {
		String playerHeadDisplayName = "&e&lProgress of &a&l%player%";
		int playerLevel = Integer.valueOf(plugin.getLYaml().getString(p.getName()));
		
		
		Inventory gui = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("levelMenuTitle")));
		//Player Head
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
		playerHeadMeta.setOwningPlayer(p);
		playerHeadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', playerHeadDisplayName.replace("%player%", p.getName())));
		ArrayList<String> headLore = new ArrayList<>();
		headLore.add("");
		headLore.add(ChatColor.translateAlternateColorCodes('&', "&eCurrent Level: &9%level%".replace("%level%", String.valueOf(playerLevel))));
		headLore.add(ChatColor.translateAlternateColorCodes('&', "&eNext Level: &9%nextlevel%".replace("%nextlevel%", String.valueOf(playerLevel + 1))));
		headLore.add("");
		headLore.add("Unlocked Rewards:");
		for (int i = 1; i < playerLevel + 1; i++) {
			if (plugin.getRYaml().contains("Level" + String.valueOf(i) + ".Reward.Text"))
			{
				headLore.add(ChatColor.translateAlternateColorCodes('&', "&5- &f" + plugin.getRYaml().getString("Level" + String.valueOf(i) + ".Reward.Text")));
			}
		}
		playerHeadMeta.setLore(headLore);
		playerHead.setItemMeta(playerHeadMeta);
		gui.setItem(12, playerHead);
		//LevelUpButton
		if (Main.econ.getBalance(p) >= SetupRankupPrices.rankupPrices.get(playerLevel + 1) && playerLevel != plugin.getConfig().getInt("numberOfLevels")) {
			ItemStack greenPane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			ItemMeta greenPaneMeta = greenPane.getItemMeta();
			greenPaneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lLevel %level% -> %nextlevel%".replace("%level%", String.valueOf(playerLevel)).replace("%nextlevel%", String.valueOf(playerLevel + 1))));
			ArrayList<String> greenPaneLore = new ArrayList<>();
			if (plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Reward.Text")) {
				greenPaneLore.add("");
				greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&a&lRewards for next level:"));
				greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&a&l- &7" + plugin.getRYaml().getString("Level" + String.valueOf(playerLevel + 1) + ".Reward.Text")));
			}
			greenPaneLore.add("");
			greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&7Cost: $&a" + String.valueOf(SetupRankupPrices.rankupPrices.get(playerLevel + 1))));
			greenPaneMeta.setLore(greenPaneLore);
			greenPane.setItemMeta(greenPaneMeta);
			gui.setItem(14, greenPane);
		} else if (playerLevel != plugin.getConfig().getInt("numberOfLevels")){
			ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			ItemMeta redPaneMeta = redPane.getItemMeta();
			redPaneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lLevel %level% -> %nextlevel%".replace("%level%", String.valueOf(playerLevel)).replace("%nextlevel%", String.valueOf(playerLevel + 1))));
			ArrayList<String> redPaneLore = new ArrayList<>();
			if (plugin.getRYaml().contains("Level" + String.valueOf(playerLevel + 1) + ".Reward.Text"))  {
				redPaneLore.add("");
				redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&c&lRewards for next level:"));
				redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&c&l- &7" + plugin.getRYaml().getString("Level" + String.valueOf(playerLevel + 1) + ".Reward.Text")));
			}
			redPaneLore.add("");
			redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&7Cost: $&c" + String.valueOf(SetupRankupPrices.rankupPrices.get(playerLevel + 1))));
			redPaneMeta.setLore(redPaneLore);
			redPane.setItemMeta(redPaneMeta);
			gui.setItem(14, redPane);
		} else {
			ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
			ItemMeta bluePaneMeta = bluePane.getItemMeta();
			bluePaneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lMax Level"));
			bluePane.setItemMeta(bluePaneMeta);
			gui.setItem(14, bluePane);
		}
		//Glass Panes
		for (int i = 0; i < 12; i++) {
			ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta grayPaneMeta = grayPane.getItemMeta();
			grayPaneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8."));
			grayPane.setItemMeta(grayPaneMeta);
			gui.setItem(i, grayPane);
		}
		for (int i = 15; i < 27; i++) {
			ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta grayPaneMeta = grayPane.getItemMeta();
			grayPaneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8."));
			grayPane.setItemMeta(grayPaneMeta);
			gui.setItem(i, grayPane);
		}
		ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta grayPaneMeta = grayPane.getItemMeta();
		grayPaneMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8."));
		grayPane.setItemMeta(grayPaneMeta);
		gui.setItem(13, grayPane);
		
		p.openInventory(gui);
	}

}
