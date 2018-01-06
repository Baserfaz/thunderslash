package com.thunderslash.particles;

import java.util.ArrayList;
import java.util.List;

public class EmitterManager {

    private List<Emitter> emitters;
    
    public EmitterManager() {
        this.emitters = new ArrayList<Emitter>();
    }

    public Emitter createEmitter() {
        Emitter e = new Emitter();
        this.emitters.add(e);
        return e;
    }
    
    public List<Emitter> getEmitters() { return emitters; }
    public void setEmitters(List<Emitter> emitters) { this.emitters = emitters; }
}
