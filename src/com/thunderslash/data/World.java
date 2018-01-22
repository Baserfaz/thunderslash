package com.thunderslash.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thunderslash.engine.Game;
import com.thunderslash.enumerations.BlockType;
import com.thunderslash.enumerations.Direction;
import com.thunderslash.gameobjects.Block;
import com.thunderslash.utilities.LevelCreator;

public class World {

    private Room currentRoom;
    private List<Block> currentRoomBlocks;
    private List<Room> rooms;
    
    public World() {
        this.rooms = new ArrayList<Room>();
        this.currentRoomBlocks = new ArrayList<Block>();
        this.currentRoom = null;
        
        String lvl0 = "testlevel.png";
        String lvl1 = "testlevel2.png";
        
        for(int i = 0; i < Game.WORLD_ROOM_COUNT; i++) {
            
            String path = "";
            if(i == 0) path = lvl1;
            else if(i == 1) path = lvl0;
            
            LevelData data = LevelCreator.createLevel(path);
            Room room = new Room(i, data);
            room.getData().setBackground(LevelCreator.createBackground(room));
            this.rooms.add(room);
            
            System.out.println("Created room (lvl: " + i + ") \'" + room.toString() +
                    "\', block count: " + room.getData().getBlocks().size() + ".");
        }
        
    }
    
    public void initializeRoom(int index) {
        
        // deactivate previous room
        if(this.currentRoom != null) this.currentRoom.deactivateRoom();
        
        Room room = this.rooms.get(index);
        room.activateRoom();
        this.setCurrentRoomBlocks(LevelCreator.calculateSprites(room.getData().getBlocks()));
        this.currentRoom = room;
    }
    
    public NeighborData getNeighbors(Block block) {
        
        NeighborData data = new NeighborData(block);
        Point p1 = block.getGridPosition();
        
        List<BlockType> allowedTypes = new ArrayList<BlockType>(
                Arrays.asList(
                        BlockType.PLAY_AREA, BlockType.PLAYER_SPAWN,
                        BlockType.PLATFORM, BlockType.EXIT, BlockType.HURT,
                        BlockType.WATER));
        
        for(Block b : this.getCurrentRoom().getData().getBlocks()) {
            
            if(b.getIsEnabled() == false) continue;
            
            if(allowedTypes.contains(b.getBlocktype())) {
                
                Point p2 = b.getGridPosition();
                
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
    
    // ----- GETTERS & SETTERS -------
    public List<Block> getCurrentRoomBlocks() { return currentRoomBlocks; }
    public void setCurrentRoomBlocks(List<Block> roomBlocks) { this.currentRoomBlocks = roomBlocks; }
    public Room getCurrentRoom() { return rooms.get(Game.instance.getCurrentRoomIndex()); }
    public Room getRoom(int index) { return rooms.get(index); }
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
