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

    private List<Block> currentRoomBlocks;
    private List<Room> rooms;
    
    public World() {
        this.rooms = new ArrayList<Room>();
        this.currentRoomBlocks = new ArrayList<Block>();
        
        for(int i = 0; i < Game.WORLD_ROOM_COUNT; i++) {
            LevelData data = LevelCreator.createLevel("testlevel2.png");
            Room room = new Room(i, data.getWidth(), data.getHeight(), data.getBlocks());
            room.setBackground(LevelCreator.createBackground(room));
            this.rooms.add(room);
            
            System.out.println("Created room (lvl: " + i + ") \'" + room.toString() +
                    "\', block count: " + room.getBlocks().size() + ".");
        }
        
    }
    
    public void initializeRoom(int index) {
        try { this.setCurrentRoomBlocks(LevelCreator.calculateSprites(this.rooms.get(index).getBlocks())); }
        catch (IndexOutOfBoundsException e) { System.out.println(e); }
    }
    
    public NeighborData getNeighbors(Block block) {
        
        NeighborData data = new NeighborData(block);
        Point p1 = block.getGridPosition();
        
        List<BlockType> allowedTypes = new ArrayList<BlockType>(
                Arrays.asList(
                        BlockType.PLAY_AREA, BlockType.PLAYER_SPAWN,
                        BlockType.PLATFORM, BlockType.EXIT, BlockType.HURT,
                        BlockType.WATER));
        
        for(Block b : this.getCurrentRoom().getBlocks()) {
            
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
