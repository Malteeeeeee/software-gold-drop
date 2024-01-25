package com.mygdx.game.world;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.entities.*;
import com.mygdx.game.items.Gold;
import com.mygdx.game.items.Item;
import com.mygdx.game.items.ItemType;
//import com.mygdx.game.entities.EntityLoader;


public abstract class GameMap {
	
	public static Player player;
	public static Slime slime;

	public static ArrayList<Entity> entities;
	public static ArrayList<Item> items;

	private boolean slimenur1mal = true;
	private boolean enemy1nur1mal = true;

	
	public GameMap() {
		player = new Player(320,200,this,100,10);
		entities = new ArrayList<Entity>();
		entities.add(player);

		loadEntitiesFromJson("entities.json");
		entities.forEach(entity -> {
			System.out.println(entity);
			if(entity.getType() == EntityType.SLIME){
				slime = (Slime) entity;
            }
		});
		items = new ArrayList<Item>();
		items.add(new Gold(80,300,this));


	}
	
	public void render (OrthographicCamera camera, SpriteBatch batch) {
		for(Entity entity : entities) {
			entity.render(batch);
		}
		for(Item item : items) {
			item.render(batch);

		}

	}
	public void update(float delta) {
	    ArrayList<Entity> entitiesToRemove = new ArrayList<>();
		ArrayList<Item> itemsToRemove = new ArrayList<>();


	    for (Entity entity : entities) {
	        entity.update(delta, 9.81f);

	        // Überprüfe, ob die Gesundheit null ist und füge sie zur Liste der zu entfernenden Entitäten hinzu
	        if (entity.getHealth() == 0 && entity.getHealth() < entity.getMaxHealth()) {
	            entitiesToRemove.add(entity);
	        }
	    }

	    // Entferne die Entitäten aus der Liste nach der Iteration
	    entities.removeAll(entitiesToRemove);

		for(Item item : items) {
			item.update(delta, 9.81f);

		if(item.isInPlayerRange(player)) {
			itemsToRemove.add(item);
			player.addGold();
		}
		}
		items.removeAll(itemsToRemove);


		if(slime.isDead && slimenur1mal == true) {
			for(int i = 0; i<slime.GoldDrop ; i++) {
			items.add(new Gold(slime.getX(),slime.getY(),this));
			}

			slimenur1mal = false;
		}



	}
	public abstract void dispose ();
	
	public TileType getTileTypeByLocation(int layer, float x, float y) {
		return this.getTileTypeByCoordinate(layer,  (int) (x / TileType.TILE_SIZE), (int) (y / TileType.TILE_SIZE));
	}
	
	
	public abstract TileType getTileTypeByCoordinate(int layer, int col, int row);
	
	public boolean doesRectCollideWithMap(float x, float y, int width, int height) {
		if (x < 0 || y < 0 || x + width > getPixelWidth() || y + height > getPixelHeight()) 
			return true;
		
		for (int row = (int) (y/TileType.TILE_SIZE); row < Math.ceil((y + height) / TileType.TILE_SIZE); row++) {
			for (int col = (int) (x/TileType.TILE_SIZE); col < Math.ceil((x + width) / TileType.TILE_SIZE); col++) {
				for (int layer = 0; layer < getLayers(); layer++) {
					TileType type = getTileTypeByCoordinate(layer, col, row);
					if (type != null && type.isCollidable())
						return true;
				}
			}
		}
		
		return false;
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getLayers();
	
	public int getPixelWidth() {
		return this.getWidth() * TileType.TILE_SIZE;
	}
	public int getPixelHeight() {
		return this.getHeight() * TileType.TILE_SIZE;
	}
	 protected void loadEntitiesFromJson(String jsonFilePath) {
	        Json json = new Json();
	        ArrayList<EntityData> entityDataList = json.fromJson(ArrayList.class, EntityData.class, Gdx.files.internal(jsonFilePath));

	        for (EntityData entityData : entityDataList) {
	            entities.add(createEntityFromData(entityData));
	        }
	    }

	    private Entity createEntityFromData(EntityData entityData) {
	        if ("Player".equals(entityData.type)) {
	            return new Player(entityData.x, entityData.y, this, entityData.health, entityData.attackDamage);
	        } else if ("Slime".equals(entityData.type)) {
	            return new Slime(entityData.x, entityData.y, this, entityData.health, entityData.attackDamage);
	        }
	        return null;
	    }
}
	

