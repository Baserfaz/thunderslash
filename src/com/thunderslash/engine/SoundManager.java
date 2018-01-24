package com.thunderslash.engine;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.thunderslash.enumerations.SoundEffect;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundManager {
      
      private Map<SoundEffect, Sound> sounds;
      
      public SoundManager() {
          TinySound.init();
          TinySound.setGlobalVolume(0.1f);
          this.sounds = new HashMap<SoundEffect, Sound>();
          this.loadSounds();
      }
    
      private void loadSounds() {
          int soundCount = 0;
          int errorCount = 0;
          
          for(SoundEffect effect : SoundEffect.values()) {
              String path = this.getPath(effect);
              URL soundURL = this.getClass().getResource(path);
              Sound sound = TinySound.loadSound(soundURL);
              
              if(sound != null) this.sounds.put(effect, sound);
              else errorCount += 1;
              
              soundCount += 1;
          }
          
          System.out.println("Loaded " + (soundCount - errorCount) + "/" + soundCount + " sound effects.");
      }
      
      public void playSound(SoundEffect effect) {
          this.sounds.get(effect).play();
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
        case PLAYER_JUMP:
            path = "/sounds/player_jump.wav";
            break;
        case LAND:
            path = "/sounds/land.wav";
            break;
        case SLIME_JUMP:
            path = "/sounds/slime_jump.wav";
            break;
        case PLAYER_ATTACK:
            path = "/sounds/player_attack.wav";
            break;
        case ATTACK_HIT:
            path = "/sounds/attack_hit.wav";
            break;
        default:
            System.out.println("SoundManager:: play: unsupported sound effect!");
            break;
        }
        
        return path;
    }
}
