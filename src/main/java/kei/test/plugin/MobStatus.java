package kei.test.plugin;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;

public class MobStatus {
	private UUID uuid;
	private String name;
	private int level;
	private double maxHp;
	private double hp;
	private double attack;
	private LivingEntity entity;

	public MobStatus(UUID uuid, String name, int level, double maxHp, double hp, double attack, LivingEntity entity) {
		this.uuid = uuid;
		this.name = name;
		this.level = level;
		this.maxHp = maxHp;
		this.hp = hp;
		this.attack = attack;
		this.entity = entity;
	}

	public boolean isValid() {
		return entity.isValid();
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(double maxHp) {
		this.maxHp = maxHp;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}

	public double getAttack() {
		return attack;
	}

	public void setAttack(double attack) {
		this.attack = attack;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

}
