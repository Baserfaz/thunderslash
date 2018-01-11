package com.thunderslash.particles;

import java.util.ArrayList;
import java.util.List;

import com.thunderslash.gameobjects.GameObject;

public class EmitterManager {

    private List<Emitter> emitters;
    
    public EmitterManager() {
        this.emitters = new ArrayList<Emitter>();
    }

    public Emitter createEmitter(GameObject parent) {
        Emitter e = new Emitter(parent);
        this.emitters.add(e);
        return e;
    }
    
    public List<Emitter> getEmitters() { return emitters; }
    public void setEmitters(List<Emitter> emitters) { this.emitters = emitters; }
}
