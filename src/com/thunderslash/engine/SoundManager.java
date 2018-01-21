package com.thunderslash.engine;

import java.net.URL;

import com.thunderslash.enumerations.SoundEffect;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundManager {

      private Sound hover  = null;
      private Sound select = null;
      private Sound jump   = null;
      private Sound land   = null;
      private Sound slime_jump = null;  
      private Sound player_attack = null;
      private Sound attack_hit = null;
      
      public SoundManager() {
          TinySound.init();
          TinySound.setGlobalVolume(0.1f);
          this.loadSounds();
      }
    
    
      private void loadSounds() {
          
          int soundCount = 0;
          int errorCount = 0;
          
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
              case PLAYER_JUMP:
                  this.jump = TinySound.loadSound(soundURL);
                  break;
              case LAND:
                  this.land = TinySound.loadSound(soundURL);
                  break;
              case SLIME_JUMP:
                  this.slime_jump = TinySound.loadSound(soundURL);
                  break;
              case PLAYER_ATTACK:
                  this.player_attack = TinySound.loadSound(soundURL);
                  break;
              case ATTACK_HIT:
                  this.attack_hit = TinySound.loadSound(soundURL);
                  break;
              default:
                  System.out.println("SoundManager::loadSounds: soundeffect not supported: " + effect);
                  errorCount += 1;
                  break;
              }
              soundCount += 1;
          }
          
          System.out.println("Loaded " + (soundCount - errorCount) + "/" + soundCount + " sound effects.");
      }
      
      public void playSound(SoundEffect effect) {
          
        switch(effect) {
        case HOVER:
            this.hover.play();
            break;
        case SELECT:
            this.select.play();
            break;
        case PLAYER_JUMP:
            this.jump.play();
            break;
        case LAND:
            this.land.play();
            break;
        case SLIME_JUMP:
            this.slime_jump.play();
            break;
        case PLAYER_ATTACK:
            this.player_attack.play();
            break;
        case ATTACK_HIT:
            this.attack_hit.play();
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
