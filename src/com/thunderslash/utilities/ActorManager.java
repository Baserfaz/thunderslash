package com.thunderslash.utilities;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.data.World;
import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.ActorType;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.SpriteType;
import com.thunderslash.gameobjects.Actor;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.gameobjects.Player;

public class ActorManager {

    private List<Actor> actorInstances;
    private Actor playerInstance;
    
    public ActorManager() {
        actorInstances = new ArrayList<Actor>();
        setPlayerInstance(null);
    }

    public Actor createActorInstance(String actorName, ActorType actorType, 
            SpriteType spriteType, int spriteSize, int spriteSizeMult, int health) {

        Actor actor = null;
        
        if(actorType == ActorType.Player) {
            
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
            Coordinate spawnpos = new Coordinate(0, 0);
            for(Block block : world.getCurrentRoomBlocks()) {
                if(block.getBlocktype() == BlockType.PLAYER_SPAWN) {
                    spawnpos = block.getWorldPosition();
                    break;
                }
            }
            
            // create player object
            Player player = new Player(actorName, spawnpos, spriteType, health);
            
            // set variables
            actor = player;
            this.playerInstance = actor;
            
            Game.instance.getCamera().setFollowTarget(player);
            
        } else if (actorType == ActorType.Enemy) {}
        return actor;
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

    public Actor getPlayerInstance() {
        return playerInstance;
    }

    public void setPlayerInstance(Actor playerInstance) {
        this.playerInstance = playerInstance;
    }
}
