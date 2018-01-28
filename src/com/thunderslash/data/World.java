package com.thunderslash.data;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.engine.Game;
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
        
        String lvl1 = "testlevel.png";
        String lvl0 = "testlevel2.png";
        
        for(int i = 0; i < Game.WORLD_ROOM_COUNT; i++) {
            
            String path = "";
            if(i == 0) path = lvl0;
            else if(i == 1) path = lvl1;
            
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
        this.setCurrentRoomBlocks(LevelCreator.calculateSprites(room));
        this.currentRoom = room;
    }
    
    // ----- GETTERS & SETTERS -------
    public List<Block> getCurrentRoomBlocks() { return currentRoomBlocks; }
    public void setCurrentRoomBlocks(List<Block> roomBlocks) { this.currentRoomBlocks = roomBlocks; }
    public Room getCurrentRoom() { return rooms.get(Game.instance.getCurrentRoomIndex()); }
    public Room getRoom(int index) { return rooms.get(index); }
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
