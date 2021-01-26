package com.emiv.awesomelevels;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	
	public static Main mainClass;
	
	public void instanceClasses() {
		mainClass = this;
	}
 	
	//Level of each player
	private File lFile;
	private YamlConfiguration lYaml;
	
	//Messages
	private File mFile;
	private YamlConfiguration mYaml;
	
	//Rewards
	private File rFile;
	private YamlConfiguration rYaml;

	//Potion Effects
	private File pFile;
	private YamlConfiguration pYaml;
	
	//LevelRequirements
	private File reqFile;
	private YamlConfiguration reqYaml;
	
	//PlayerStats
	private File sFile;
	private YamlConfiguration sYaml;
	
	@Override
	public void onEnable() {
		setupEconomy();
		instanceClasses();
		setupChat();
		
		this.saveDefaultConfig();
		
		try {
			initiateFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setMsg();
		setReq();
		setRewards();
		Save();
		
		Bukkit.getPluginManager().registerEvents(new onJoin(this), this);
		Bukkit.getPluginManager().registerEvents(new onChat(this), this);
		Bukkit.getPluginManager().registerEvents(new LevelListener(this), this);
		Bukkit.getPluginManager().registerEvents(new onKill(this), this);
		Bukkit.getPluginManager().registerEvents(new onMine(this), this);
		
		getCommand("level").setExecutor(new LevelCommand(this));
		getCommand("alreload").setExecutor(new ReloadCommand(this));
		

		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("AwesomeLevels"), new Runnable() {
		    @Override
		    public void run() {
		        SetPotionEffects();
		    }
		}, 0L, 200L);
	}
	
	private void setReq() {
		if (!reqYaml.contains("Level1") && this.getConfig().getInt("numberOfLevels") >= 1) {
			reqYaml.set("Level1.Coin", 1000);
			reqYaml.set("Level1.Kill.Type", "CHICKEN");
			reqYaml.set("Level1.Kill.Amount", 25);
		}
		if (!reqYaml.contains("Level2") && this.getConfig().getInt("numberOfLevels") >= 2) {
			reqYaml.set("Level2.Coin", 20000);
		}
		if (!reqYaml.contains("Level3") && this.getConfig().getInt("numberOfLevels") >= 3) {
			reqYaml.set("Level3.Coin", 50000);
			reqYaml.set("Level3.Mine.Type", "STONE");
			reqYaml.set("Level3.Mine.Amount", 640);
		}
		if (!reqYaml.contains("Level4") && this.getConfig().getInt("numberOfLevels") >= 4) {
			reqYaml.set("Level4.Coin", 100000);
		}
		if (!reqYaml.contains("Level5") && this.getConfig().getInt("numberOfLevels") >= 5) {
			reqYaml.set("Level5.Coin", 150000);
		}
		if (!reqYaml.contains("Level6") && this.getConfig().getInt("numberOfLevels") >= 6) {
			reqYaml.set("Level6.Coin", 250000);
		}
		if (!reqYaml.contains("Level7") && this.getConfig().getInt("numberOfLevels") >= 7) {
			reqYaml.set("Level7.Coin", 500000);
		}
		if (!reqYaml.contains("Level8") && this.getConfig().getInt("numberOfLevels") >= 8) {
			reqYaml.set("Level8.Coin", 1000000);
		}
		if (!reqYaml.contains("Level9") && this.getConfig().getInt("numberOfLevels") >= 9) {
			reqYaml.set("Level9.Coin", 2000000);
		}
		if (!reqYaml.contains("Level10") && this.getConfig().getInt("numberOfLevels") >= 10) {
			reqYaml.set("Level10.Coin", 5000000);
			reqYaml.set("Level10.Kill.Type", "ZOMBIE");
			reqYaml.set("Level10.Kill.Amount", 200);
			reqYaml.set("Level10.Mine.Type", "DIAMOND_ORE");
			reqYaml.set("Level10.Mine.Amount", 64);
		}
	}

	void SetPotionEffects() {
		String[] effects = {"FAST_DIGGING", "FIRE_RESISTANCE", "INCREASE_DAMAGE", "JUMP", "NIGHT_VISION", "REGENERATION", "SPEED", "WATER_BREATHING"};
		for (Player p : Bukkit.getOnlinePlayers()) {
			for (String s : effects) {
				if (pYaml.getInt(p.getName() + "." + s) != 0) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s), 200, pYaml.getInt(p.getName() + "." + s)));
				}
			}
		}
	}

	public void setMsg() {
		if (!mYaml.contains("NoPermission")) {
			mYaml.set("NoPermission", "&cYou don't have permission.");
		} if (!mYaml.contains("Reloaded")) {
			mYaml.set("Reloaded", "&aSuccessfully Reloaded.");
		} if (!mYaml.contains("LevelUpNotMet")) {
			mYaml.set("LevelUpNotMet", "&cRequirements are not met.");
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
		rYaml.set("Level5.Reward.Message", "&eYou unlocked kit king and permanent Haste I for leveling up!");
		rYaml.set("Level5.Reward.Text", "King Kit\nHaste I");
		rYaml.set("Level5.Effect.Type", "FAST_DIGGING");
		rYaml.set("Level5.Effect.Tier", 1);
		rYaml.set("Level10.Reward.Message", "&eYou recieved VIP Rank and permanent Strength II for being max level!");
		rYaml.set("Level10.Reward.Text", "VIP Rank\nStrength II");
		rYaml.set("Level10.Commands.Console", "lp user %player% parent set vip");
		rYaml.set("Level10.Commands.Player", "say I am now max level!");
		rYaml.set("Level10.Effect.Type", "INCREASE_DAMAGE");
		rYaml.set("Level10.Effect.Tier", 2);
	}
	
	
	public void Save() {
		try {
			mYaml.save(mFile);
			rYaml.save(rFile);
			reqYaml.save(reqFile);
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
	public YamlConfiguration getPYaml() { return pYaml; }
	public File getPFile() { return pFile; }
	public YamlConfiguration getReqYaml() { return reqYaml; }
	public File getReqFile() { return reqFile; }
	public YamlConfiguration getSYaml() { return sYaml; }
	public File getSFile() { return sFile; }
	
	public void initiateFiles() throws IOException {
		File subdir = new File(Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels").getDataFolder().getPath() + System.getProperty("file.separator") + "PlayerData");
		subdir.mkdir();
		
		lFile = new File(subdir.getPath(), "levels.yml");
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
		
		pFile = new File(subdir.getPath(), "potioneffects.yml");
		if (!pFile.exists()) {
			pFile.createNewFile();
		}
		
		pYaml = YamlConfiguration.loadConfiguration(pFile);
		
		reqFile = new File(Bukkit.getServer().getPluginManager().getPlugin("AwesomeLevels").getDataFolder(), "requirements.yml");
		if (!reqFile.exists()) {
			reqFile.createNewFile();
		}
		
		reqYaml = YamlConfiguration.loadConfiguration(reqFile);
		
		sFile = new File(subdir.getPath(), "stats.yml");
		if (!sFile.exists()) {
			sFile.createNewFile();
		}
		
		sYaml = YamlConfiguration.loadConfiguration(sFile);
	}

}
