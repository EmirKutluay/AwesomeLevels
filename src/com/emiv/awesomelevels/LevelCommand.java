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
		String nextLevel = String.valueOf(playerLevel + 1);
		boolean everyThingChecks = true;
		if (Main.econ.getBalance(p) >= plugin.getReqYaml().getInt("Level" + nextLevel + ".Coin")) {
			if (plugin.getReqYaml().contains("Level" + nextLevel + ".Mine.Type") && plugin.getReqYaml().contains("Level" + nextLevel + ".Mine.Amount")) {
				if (plugin.getSYaml().contains(p.getName() + ".Mine." + plugin.getReqYaml().getString("Level" + nextLevel + ".Mine.Type"))) {
					if (plugin.getSYaml().getInt(p.getName() + ".Mine." + plugin.getReqYaml().getString("Level" + nextLevel + ".Mine.Type")) == plugin.getReqYaml().getInt("Level" + nextLevel + ".Mine.Amount")) {
					} else {
						everyThingChecks = false;
					}
				} else {
					everyThingChecks = false;
				}
			}
			if (plugin.getReqYaml().contains("Level" + nextLevel + ".Kill.Type") && plugin.getReqYaml().contains("Level" + nextLevel + ".Kill.Amount")) {
				if (plugin.getSYaml().contains(p.getName() + ".Kill." + plugin.getReqYaml().getString("Level" + nextLevel + ".Kill.Type"))) {
					if (plugin.getSYaml().getInt(p.getName() + ".Kill." + plugin.getReqYaml().getString("Level" + nextLevel + ".Kill.Type")) == plugin.getReqYaml().getInt("Level" + nextLevel + ".Kill.Amount")) {
						//
					} else {
						everyThingChecks = false;
					}
				} else {
					everyThingChecks = false;
				}
			}
		} else {
			everyThingChecks = false;
		}
		//LevelUpButton
		if (everyThingChecks && playerLevel != plugin.getConfig().getInt("numberOfLevels")) {

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
			greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&eRequirements:"));
			greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &7$&a" + String.valueOf(plugin.getReqYaml().getInt("Level" + nextLevel + ".Coin"))));
			if (plugin.getReqYaml().contains("Level" + nextLevel + ".Kill.Type") && plugin.getReqYaml().contains("Level" + nextLevel + ".Kill.Amount")) {
				String totalKills = String.valueOf(plugin.getReqYaml().get("Level" + nextLevel + ".Kill.Amount"));
				String killType = plugin.getReqYaml().getString("Level" + nextLevel + ".Kill.Type");
				if (plugin.getSYaml().contains(p.getName() + ".Kill." + plugin.getReqYaml().getString("Level" + nextLevel + ".Kill.Type"))) {
					String pKills = String.valueOf(plugin.getSYaml().get(p.getName() + ".Kill." + killType));
					greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &7" + pKills + "&8/&7" + totalKills + " &d&l" + killType + " &7Kills"));
				} else {
					greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &70" + "&8/&7" + totalKills + " &d&l" + killType + " &7Kills"));
				}
			}
			if (plugin.getReqYaml().contains("Level" + nextLevel + ".Mine.Type") && plugin.getReqYaml().contains("Level" + nextLevel + ".Mine.Amount")) {
				String totalBlocks = String.valueOf(plugin.getReqYaml().get("Level" + nextLevel + ".Mine.Amount"));
				String mineType = plugin.getReqYaml().getString("Level" + nextLevel + ".Mine.Type");
				if (plugin.getSYaml().contains(p.getName() + ".Mine." + plugin.getReqYaml().getString("Level" + nextLevel + ".Mine.Type"))) {
					String pBroken = String.valueOf(plugin.getSYaml().get(p.getName() + ".Mine." + mineType));
					greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &7" + pBroken + "&8/&7" + totalBlocks + " &d&l" + mineType + " &7Broken"));
				} else {
					greenPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &70" + "&8/&7" + totalBlocks + " &d&l" + mineType + " &7Broken"));
				}
			}
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
			redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&eRequirements:"));
			redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &7$&a" + String.valueOf(plugin.getReqYaml().getInt("Level" + nextLevel + ".Coin"))));
			if (plugin.getReqYaml().contains("Level" + nextLevel + ".Kill.Type") && plugin.getReqYaml().contains("Level" + nextLevel + ".Kill.Amount")) {
				String totalKills = String.valueOf(plugin.getReqYaml().get("Level" + nextLevel + ".Kill.Amount"));
				String killType = plugin.getReqYaml().getString("Level" + nextLevel + ".Kill.Type");
				if (plugin.getSYaml().contains(p.getName() + ".Kill." + plugin.getReqYaml().getString("Level" + nextLevel + ".Kill.Type"))) {
					String pKills = String.valueOf(plugin.getSYaml().get(p.getName() + ".Kill." + killType));
					redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &7" + pKills + "&8/&7" + totalKills + " &d&l" + killType + " &7Kills"));
				} else {
					redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &70" + "&8/&7" + totalKills + " &d&l" + killType + " &7Kills"));
				}
			}
			if (plugin.getReqYaml().contains("Level" + nextLevel + ".Mine.Type") && plugin.getReqYaml().contains("Level" + nextLevel + ".Mine.Amount")) {
				String totalBlocks = String.valueOf(plugin.getReqYaml().get("Level" + nextLevel + ".Mine.Amount"));
				String mineType = plugin.getReqYaml().getString("Level" + nextLevel + ".Mine.Type");
				if (plugin.getSYaml().contains(p.getName() + ".Mine." + plugin.getReqYaml().getString("Level" + nextLevel + ".Mine.Type"))) {
					String pBroken = String.valueOf(plugin.getSYaml().get(p.getName() + ".Mine." + mineType));
					redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &7" + pBroken + "&8/&7" + totalBlocks + " &d&l" + mineType + " &7Broken"));
				} else {
					redPaneLore.add(ChatColor.translateAlternateColorCodes('&', "&8- &70" + "&8/&7" + totalBlocks + " &d&l" + mineType + " &7Broken"));
				}
			}
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
