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

    private List<Block> roomBlocks;
    private List<Room> rooms;
    
    public World() {
        this.rooms = new ArrayList<Room>();
        this.roomBlocks = new ArrayList<Block>();
        
        for(int i = 0; i < Game.WORLD_ROOM_COUNT; i++) {
            Room room = new Room(i, LevelCreator.createLevel("testlevel.png"));
            this.rooms.add(room);
            
            System.out.println("created room \'" + room.toString() +
                    "\', block count: " + room.getBlocks().size());
        }
    }

    public void initRoom(int index) {
        try { this.setRoomBlocks(LevelCreator.calculateSprites(this.rooms.get(index).getBlocks())); }
        catch (IndexOutOfBoundsException e) { System.out.println(e); }
    }

    public List<Block> getRoomBlocks() {
        return roomBlocks;
    }

    public void setRoomBlocks(List<Block> roomBlocks) {
        this.roomBlocks = roomBlocks;
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
    
    public NeighborData getNeighbors(Block block) {
        
        NeighborData data = new NeighborData(block);
        Coordinate p1 = block.getGridPosition();
        
        for(Block b : this.getRoom(Game.instance.getCurrentRoomIndex()).getBlocks()) {
            
            if(b.getBlocktype() == BlockType.WATER) continue;
            if(b.getIsEnabled() == false) continue;
            
            Coordinate p2 = b.getGridPosition();
            
            if(p1.x == p2.x && (p1.y - 1) == p2.y) {
                data.setNeighbor(Direction.NORTH, b);
            } else if(p1.x == p2.x && (p1.y + 1) == p2.y) {
                data.setNeighbor(Direction.SOUTH, b);
            } else if((p1.x - 1) == p2.x && p1.y == p2.y) {
                data.setNeighbor(Direction.WEST, b);
            } else if((p1.x + 1) == p2.x && p1.y == p2.y) {
                data.setNeighbor(Direction.EAST, b);
            }
        }
        return data;
    }

}
