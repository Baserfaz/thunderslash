package com.thunderslash.engine;

import java.net.URL;

import com.thunderslash.enumerations.SoundEffect;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundManager {

        Sound hover = null;
        Sound select= null;
    
      public SoundManager() {
          TinySound.init();
          TinySound.setGlobalVolume(0.1f);
          this.loadSounds();
      }
    
    
      private void loadSounds() {
          for(SoundEffect effect : SoundEffect.values()) {
              
              String path = this.getPath(effect);
              URL soundURL = this.getClass().getResource(path);
              
            switch(effect) {
            case HOVER:
                this.hover = TinySound.loadSound(soundURL);
                break;
            case SELECT:
                this.select = TinySound.loadSound(soundURL);
                break;
            default:
                System.out.println("SoundManager::loadSounds: soundeffect not supported: " + effect);
                break;
              }
          }
      }
      
      public void playSound(SoundEffect effect) {
          
          switch(effect) {
        case HOVER:
            this.hover.play();
            break;
        case SELECT:
            this.select.play();
            break;
        default:
            System.out.println("SoundManager::playSound: soundeffect not supported: " + effect);
            break;
          }
          
      }
      
    private String getPath(SoundEffect effect) {
        
        String path = "";
        
        switch(effect) {
        case SELECT:
            path = "/sounds/select.wav";
            break;
        case HOVER:
            path = "/sounds/hover.wav";
            break;
        default:
            System.out.println("SoundManager:: play: unsupported sound effect!");
            break;
        }
        
        return path;
    }
}
