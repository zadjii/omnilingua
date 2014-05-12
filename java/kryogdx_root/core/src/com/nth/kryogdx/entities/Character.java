package com.nth.kryogdx.entities;

import com.badlogic.gdx.math.Matrix4;
import com.nth.kryogdx.game.Engine;
import com.nth.kryogdx.shaders.Shader;

/**
 * Created with IntelliJ IDEA.
 * User: zadjii
 * Date: 5/2/14
 * Time: 3:37 PM
 */
public abstract class Character extends Entity {

	private float hp;
	private float mp;

	public Character(){
		super();
		initStats();
	}


	public void tick(Engine e, float delta) {
		super.tick(delta);
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);
		throw new RuntimeException("The character shouldn't be tick(∆)'d" +
			" if they are in an Engine.\n" +
			"They should call tick(Engine, delta) instead");
	}

	/*
	I'm adding all of the old stats from Demigods to the new character class,
		to make it different from just an Entity
	 */

	/*
	 *  These constants represent each stat's location in the stat arrays.
	 */
	public static final int ATTACK = 0;
	public static final int ARMOR = 1;
	public static final int MAGIC_RESIST = 2;
	public static final int ABILITY = 3;
	public static final int FIRE_POWER = 4;
	public static final int WATER_POWER = 5;
	public static final int AIR_POWER = 6;
	public static final int EARTH_POWER = 7;
	public static final int PURPLE_POWER = 8;
	public static final int LIGHT_POWER = 9;
	public static final int DARK_POWER = 10;
	public static final int FIRE_RESIST = 11;
	public static final int WATER_RESIST = 12;
	public static final int AIR_RESIST = 13;
	public static final int EARTH_RESIST = 14;
	public static final int PURPLE_RESIST = 15;
	public static final int LIGHT_RESIST = 16;
	public static final int DARK_RESIST = 17;
	public static final int CRIT_CHANCE = 18;
	public static final int HP = 19;
	public static final int MP = 20;
	public static final int HPREGEN = 21;
	public static final int MPREGEN = 22;
	public static final int VAMPIRISM = 23;
	public static final int ATTACK_SPEED = 24;

	public static final int MAX_ARMOR = 250;

	private double[] baseStats = new double[25];
	private double[] flatStats = new double[25];
	private double[] pcntStats = new double[25];

	private void initStats(){
		baseStats[HP] = 100;
		baseStats[MP] = 40;
		baseStats[MPREGEN] = 0.025;
		baseStats[HPREGEN] = 0.0;
		baseStats[ATTACK_SPEED] = 30.0;
		hp = (int)baseStats[HP];
		mp = (int)baseStats[MP];
	}


	public double getStat(int stat){
		return (baseStats[stat] + flatStats[stat]) * (1 + pcntStats[stat]);
	}
	public void setBaseStat(int stat, double value){
		baseStats[stat] = value;
	}
	public void setFlatBonus(int stat, double value){
		flatStats[stat] = value;
	}

	public void modifyBaseStat(int stat, double value){
		baseStats[stat] += value;
	}
	public void modifyFlatBonus(int stat, double value){
		flatStats[stat] += value;
	}
	public void modifyPercentBonus(int stat, double value){
		pcntStats[stat] += value;
	}

	public float getHP() {
		return hp;
	}

	public void setHP(float hp) {
		this.hp = hp;
	}

	public float getMP() {
		return mp;
	}

	public void setMP(float mp) {
		this.mp = mp;
	}

	public abstract void render(Matrix4 modl, Shader shader);
}
