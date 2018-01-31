package com.thunderslash.utilities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.World;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.EnemyType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.Climber;
import com.thunderslash.gameobjects.Enemy;
import com.thunderslash.gameobjects.Player;
import com.thunderslash.gameobjects.Roller;
import com.thunderslash.gameobjects.Slime;

public class ActorManager {

    private List<Actor> actorInstances;
    private Player playerInstance;
    
    public ActorManager() {
        actorInstances = new ArrayList<Actor>();
        setPlayerInstance(null);
    }

    public Enemy createEnemyInstance(String name, Point pos, EnemyType enemyType, int health) {
        
        Enemy enemy = null;
        
        switch(enemyType) {
        case SLIME:
            enemy = new Slime(name, pos, health);
            break;
        case ROLLER:
            enemy = new Roller(name, pos, health);
            break;
        case CLIMBER:
            enemy = new Climber(name, pos, health);
            break;
        default:
            System.out.println("ActorManager::createEnemyInstance: unsupported enemy type: " + enemyType);
            break;
        }
        
        this.actorInstances.add(enemy);
        return enemy;
    }
    
    public void movePlayerToSpawnPosition() {
        
        if(this.playerInstance == null) {
            System.out.println("Player not yet instantiated: execute createPlayerInstance first!");
            return;
        }
        this.playerInstance.setWorldPosition(this.getSpawnPointPosition());
    }
    
    public Player createPlayerInstance(String actorName, SpriteType spriteType, int health) {
        
        if(this.playerInstance != null) {
            System.out.println("player already exists!");
            return null;
        }
        
        // create player object
        Player player = new Player(actorName, this.getSpawnPointPosition(), spriteType, health);
        
        // set variables
        this.playerInstance = player;
        this.actorInstances.add(player);
        
        Game.instance.getCamera().setFollowTarget(player);
        
        return player;
    }
    
    private Point getSpawnPointPosition() {
        World world = Game.instance.getWorld();
        
        if(world.getCurrentRoomBlocks() == null) {
            System.out.println("Room not yet loaded!");
            return null;
        }
        
        Point spawnpos = new Point(0, 0);
        for(Block block : world.getCurrentRoomBlocks()) {
            if(block.getBlocktype() == BlockType.PLAYER_SPAWN) {
                spawnpos = block.getWorldPosition();
                break;
            }
        }
        return spawnpos;
    }
    
    public void removeActor(Actor go) {
        for(Actor actor : actorInstances) {
            if(actor.equals(go)) {
                actorInstances.remove(go);
                break;
            }
        }
    }
    
    // ---- GETTERS & SETTERS ----
    public List<Actor> getActorInstances() { return actorInstances; }
    public void setActorInstances(List<Actor> actorInstances) { actorInstances.addAll(actorInstances); }
    public Player getPlayerInstance() { return playerInstance; }
    public void setPlayerInstance(Player playerInstance) { this.playerInstance = playerInstance; }
}
