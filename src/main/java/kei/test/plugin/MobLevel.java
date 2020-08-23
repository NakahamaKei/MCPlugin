package kei.test.plugin;

import java.util.HashMap;

public class MobLevel {

	private static MobLevel singleton = new MobLevel();
	private HashMap<Integer, Double> hpMap = new HashMap<>();
	private HashMap<Integer, Double> attackMap = new HashMap<>();

	private MobLevel() {
		hpMap.put(1, 10.0);
		hpMap.put(2, 15.0);
		hpMap.put(3, 18.0);
		hpMap.put(4, 20.0);
		hpMap.put(5, 22.0);
		hpMap.put(6, 25.0);
		hpMap.put(7, 30.0);
		hpMap.put(8, 35.0);
		hpMap.put(9, 40.0);
		hpMap.put(10, 45.0);
		hpMap.put(11, 47.0);
		hpMap.put(12, 50.0);
		hpMap.put(13, 45.0);
		hpMap.put(14, 50.0);
		hpMap.put(15, 60.0);
		hpMap.put(16, 70.0);
		hpMap.put(17, 75.0);
		hpMap.put(18, 80.0);
		hpMap.put(19, 90.0);
		hpMap.put(20, 100.0);

		attackMap.put(1, 2.0);
		attackMap.put(2, 2.4);
		attackMap.put(3, 2.8);
		attackMap.put(4, 3.0);
		attackMap.put(5, 3.5);
		attackMap.put(6, 4.0);
		attackMap.put(7, 4.5);
		attackMap.put(8, 5.0);
		attackMap.put(9, 5.5);
		attackMap.put(10, 6.0);
		attackMap.put(11, 6.5);
		attackMap.put(12, 7.0);
		attackMap.put(13, 8.0);
		attackMap.put(14, 9.0);
		attackMap.put(15, 10.0);
		attackMap.put(16, 11.0);
		attackMap.put(17, 12.0);
		attackMap.put(18, 13.0);
		attackMap.put(19, 14.0);
		attackMap.put(20, 15.0);
	}

	public static MobLevel getInstance() {
		return singleton;
	}

	public double getMobHP(int level) {
		return hpMap.get(level);
	}

	public double getMobAttack(int level) {
		return attackMap.get(level);
	}

}
