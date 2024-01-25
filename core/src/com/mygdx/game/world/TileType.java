package com.mygdx.game.world;

import java.util.HashMap;

public enum TileType {
	
	COAL(1, true, "Coal"),
	STONE(2, true, "Stone"),
	SKY(3, false, "Sky"),
	GRASS(4, true, "Grass"),
	LEAVES(5, false, "Leaves"),
	WOOD(6, true, "Wood");
	
	
	public static final int TILE_SIZE = 32;
	
	private int id;
	private boolean collidable;
	private String name;
	private float damage;
	
	private TileType (int id, boolean collidable, String name) {
		this(id, collidable, name, 0);
	}
	
	private TileType (int id, boolean collidable, String name, float damage) {
		this.id=id;
		this.collidable=collidable;
		this.name=name;
		this.damage=damage;
	}

	public int getId() {
		return id;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public String getName() {
		return name;
	}

	public float getDamage() {
		return damage;
	}
	
	private static HashMap<Integer, TileType> tileMap;
	
	static {
		tileMap = new HashMap<Integer, TileType>();
		for (TileType tileType : TileType.values()) {
			tileMap.put(tileType.getId(), tileType);
		}
	}
	
	public static TileType getTileTypeById (int id) {
		return tileMap.get(id);
	}
}
