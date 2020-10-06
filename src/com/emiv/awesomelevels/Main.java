package com.emiv.awesomelevels;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	
	SetupRankupPrices rankupPriceClass;
	public static Main mainClass;
	
	public void instanceClasses() {
		rankupPriceClass = new SetupRankupPrices();
		mainClass = this;
	}
 	
	//Level of each player
	private File lFile;
	private YamlConfiguration lYaml;
	
	//Messages
	private File mFile;
	private YamlConfiguration mYaml;
	
	private File rFile;
	private YamlConfiguration rYaml;

	@Override
	public void onEnable() {
		setupEconomy();
		instanceClasses();
		setupChat();
		
		this.saveDefaultConfig();
		
		Bukkit.getPluginManager().registerEvents(new onJoin(this), this);
		Bukkit.getPluginManager().registerEvents(new onChat(this), this);
		Bukkit.getPluginManager().registerEvents(new LevelListener(this), this);
		
		getCommand("level").setExecutor(new LevelCommand(this));
		getCommand("alreload").setExecutor(new ReloadCommand(this));
		
		try {
			initiateFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setMsg();
		rankupPriceClass.SetupPrices();
		setRewards();
		Save();
	}
	
	public void setMsg() {
		if (!mYaml.contains("NoPermission")) {
			mYaml.set("NoPermission", "&cYou don't have permission.");
		} if (!mYaml.contains("Reloaded")) {
			mYaml.set("Reloaded", "&aSuccessfully Reloaded.");
		} if (!mYaml.contains("LevelUpNoMoney")) {
			mYaml.set("LevelUpNoMoney", "&cInsufficient balance.");
		} if (!mYaml.contains("LevelUpMaxLevel")) {
			mYaml.set("LevelUpMaxLevel", "&eYou are max level.");
		}  if (!mYaml.contains("LevelUpSuccess")) {
			mYaml.set("LevelUpSuccess", "&eYou successfully ranked up to &9%level%");
		}
	}
	
	public void setRewards() {
		rYaml.set("Level1.Reward.Message", "&eYou recieved 5 diamonds for leveling up!");
		rYaml.set("Level1.Reward.Text", "5 Diamonds");
		rYaml.set("Level1.Commands.Console", "give %player% diamond 5");
		rYaml.set("Level10.Reward.Message", "&eYou unlocked kit king for leveling up!");
		rYaml.set("Level10.Reward.Text", "King Kit");
		rYaml.set("Level10.Commands.Console", "lp user %player% permission set kit.king true");
		rYaml.set("Level100.Reward.Message", "&eYou recieved god rank for being max level!");
		rYaml.set("Level100.Reward.Text", "God Rank");
		rYaml.set("Level100.Commands.Console", "lp user %player% parent set God");
		rYaml.set("Level100.Commands.Player", "say I am now max level!");
	}
	
	
	public void Save() {
		try {
			mYaml.save(mFile);
			rYaml.save(rFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Economy econ = null;
	
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public static Chat chat = null;
	
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

	public YamlConfiguration getLYaml() { return lYaml; }
	public File getLFile() { return lFile; }
	public YamlConfiguration getMYaml() { return mYaml; }
	public File getMFile() { return mFile; }
	public YamlConfiguration getRYaml() { return rYaml; }
	public File getRFile() { return rFile; }
	
	public void initiateFiles() throws IOException {
		lFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels").getDataFolder(), "levels.yml");
		if (!lFile.exists()) {
			lFile.createNewFile();
		}
		
		lYaml = YamlConfiguration.loadConfiguration(lFile);
		
		mFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels").getDataFolder(), "messages.yml");
		if (!mFile.exists()) {
			mFile.createNewFile();
		}
		
		mYaml = YamlConfiguration.loadConfiguration(mFile);
		
		rFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels").getDataFolder(), "rewards.yml");
		if (!rFile.exists()) {
			rFile.createNewFile();
		}
		
		rYaml = YamlConfiguration.loadConfiguration(rFile);
	}

}
