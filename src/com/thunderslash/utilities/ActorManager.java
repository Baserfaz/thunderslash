package com.thunderslash.utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.World;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.Enemy;
import com.thunderslash.gameobjects.Player;

public class ActorManager {

    private List<Actor> actorInstances;
    private Player playerInstance;
    
    public ActorManager() {
        actorInstances = new ArrayList<Actor>();
        setPlayerInstance(null);
    }

    public Enemy createEnemyInstance(String name, Point pos, SpriteType spriteType, int health) {
        Enemy enemy = new Enemy(name, pos, spriteType, health);
        this.actorInstances.add(enemy);
        return enemy;
    }
    
    public void movePlayerToSpawnPosition() {
        
        if(this.playerInstance == null) {
            System.out.println("Player not yet instantiated!");
            return;
        }
        
        World world = Game.instance.getWorld();
        
        if(world.getCurrentRoomBlocks() == null) {
            System.out.println("Room not yet loaded!");
            return;
        }
        
        // get the current room's spawnpoint coordinates.
        Point spawnpos = new Point(0, 0);
        for(Block block : world.getCurrentRoomBlocks()) {
            if(block.getBlocktype() == BlockType.PLAYER_SPAWN) {
                spawnpos = block.getWorldPosition();
                break;
            }
        }
        
        this.playerInstance.setWorldPosition(spawnpos);
        
    }
    
    public Player createPlayerInstance(String actorName, SpriteType spriteType, int health) {
        
        if(this.playerInstance != null) {
            System.out.println("player already exists!");
            return null;
        }
        
        World world = Game.instance.getWorld();
        
        if(world.getCurrentRoomBlocks() == null) {
            System.out.println("room not yet loaded!");
            return null;
        }
        
        // get the current room's spawnpoint coordinates.
        Point spawnpos = new Point(0, 0);
        for(Block block : world.getCurrentRoomBlocks()) {
            if(block.getBlocktype() == BlockType.PLAYER_SPAWN) {
                spawnpos = block.getWorldPosition();
                break;
            }
        }
        
        // create player object
        Player player = new Player(actorName, spawnpos, spriteType, health);
        
        // set variables
        this.playerInstance = player;
        this.actorInstances.add(player);
        
        Game.instance.getCamera().setFollowTarget(player);
        
        return player;
    }

    public void removeActor(Actor go) {
        for(Actor actor : actorInstances) {
            if(actor.equals(go)) {
                actorInstances.remove(go);
                break;
            }
        }
    }

    public List<Actor> getActorInstances() {
        return actorInstances;
    }

    public void setActorInstances(List<Actor> actorInstances) {
        actorInstances.addAll(actorInstances);
    }

    public Player getPlayerInstance() {
        return playerInstance;
    }

    public void setPlayerInstance(Player playerInstance) {
        this.playerInstance = playerInstance;
    }
}
