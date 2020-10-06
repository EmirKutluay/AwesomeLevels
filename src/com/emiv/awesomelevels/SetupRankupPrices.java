package com.emiv.awesomelevels;

import java.util.HashMap;

public class SetupRankupPrices {
	
	public static HashMap<Integer, Double> rankupPrices = new HashMap<>();
	
	public void SetupPrices() {
		int numberOfLevels = Main.mainClass.getConfig().getInt("numberOfLevels");
		Double firstLevel = Main.mainClass.getConfig().getDouble("firstLevelPrice");
		for(int i = 1; i < numberOfLevels + 1; i++) {
			if (!rankupPrices.containsKey(i)) {
				rankupPrices.put(i, firstLevel * i * i);
			}
		}
	}
}
