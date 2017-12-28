package com.thunderslash.data;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.utilities.Coordinate;
import com.thunderslash.utilities.LevelCreator;

public class World {

    private List<Block> currentRoomBlocks;
    private List<Room> rooms;
    
    public World() {
        this.rooms = new ArrayList<Room>();
        this.currentRoomBlocks = new ArrayList<Block>();
        
        for(int i = 0; i < Game.WORLD_ROOM_COUNT; i++) {
            
            LevelData data = LevelCreator.createLevel("testlevel.png");
            
            Room room = new Room(i, data.getWidth(), data.getHeight(), data.getBlocks());
            room.setBackGroundTiles(LevelCreator.createBackground(data.getWidth(), data.getHeight()));
            
            this.rooms.add(room);
            
            System.out.println("created room \'" + room.toString() +
                    "\', block count: " + room.getBlocks().size());
        }
    }

    public void initRoom(int index) {
        try { this.setCurrentRoomBlocks(LevelCreator.calculateSprites(this.rooms.get(index).getBlocks())); }
        catch (IndexOutOfBoundsException e) { System.out.println(e); }
    }

    public List<Block> getCurrentRoomBlocks() {
        return currentRoomBlocks;
    }

    public void setCurrentRoomBlocks(List<Block> roomBlocks) {
        this.currentRoomBlocks = roomBlocks;
    }
    
    public Room getCurrentRoom() {
        return rooms.get(Game.instance.getCurrentRoomIndex());
    }
    
    public Room getRoom(int index) {
        return rooms.get(index);
    }
    
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    
    public NeighborData getNeighboringPlayArea(Block block) {
        
        NeighborData data = new NeighborData(block);
        Coordinate p1 = block.getGridPosition();
        
        for(Block b : this.getRoom(Game.instance.getCurrentRoomIndex()).getBlocks()) {
            
            if(b.getIsEnabled() == false) continue;
            
            if(b.getBlocktype() == BlockType.PLAY_AREA || 
                    b.getBlocktype() == BlockType.PLAYER_SPAWN ||
                    b.getBlocktype() == BlockType.PLATFORM) {
                
                Coordinate p2 = b.getGridPosition();
                
                if(p1.x == p2.x && (p1.y - 1) == p2.y) {
                    data.setNeighbor(Direction.NORTH, b);
                } else if(p1.x == p2.x && (p1.y + 1) == p2.y) {
                    data.setNeighbor(Direction.SOUTH, b);
                } else if((p1.x - 1) == p2.x && p1.y == p2.y) {
                    data.setNeighbor(Direction.WEST, b);
                } else if((p1.x + 1) == p2.x && p1.y == p2.y) {
                    data.setNeighbor(Direction.EAST, b);
                } else if((p1.x - 1) == p2.x && (p1.y - 1) == p2.y) {
                    data.setNeighbor(Direction.NORTH_WEST, b);
                } else if((p1.x + 1) == p2.x && (p1.y - 1) == p2.y) {
                    data.setNeighbor(Direction.NORTH_EAST, b);
                } else if((p1.x - 1) == p2.x && (p1.y + 1) == p2.y) {
                    data.setNeighbor(Direction.SOUTH_WEST, b);
                } else if((p1.x + 1) == p2.x && (p1.y + 1) == p2.y) {
                    data.setNeighbor(Direction.SOUTH_EAST, b);
                }
            }
        }
        return data;
    }

}
