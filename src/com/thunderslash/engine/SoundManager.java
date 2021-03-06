package com.thunderslash.engine;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thunderslash.enumerations.SoundEffect;
import com.thunderslash.gameobjects.GameObject;
import com.thunderslash.utilities.Mathf;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

public class SoundManager {
      
      private Map<SoundEffect, Sound> sounds;
      
      private float soundVolume = 0.1f;
      
      public SoundManager() {
          TinySound.init();
          TinySound.setGlobalVolume(this.soundVolume);
          this.sounds = new HashMap<SoundEffect, Sound>();
          this.loadSounds();
      }
    
      private void loadSounds() {
          int soundCount = 0;
          int errorCount = 0;
          
          List<SoundEffect> errorEffects = new ArrayList<SoundEffect>();
          
          for(SoundEffect effect : SoundEffect.values()) {
              String path = this.getPath(effect);
              URL soundURL = this.getClass().getResource(path);
              Sound sound = TinySound.loadSound(soundURL);
              
              if(sound != null) this.sounds.put(effect, sound);
              else {
                  errorCount += 1;
                  errorEffects.add(effect);
              }
              
              soundCount += 1;
          }
          
          System.out.println("Loaded " + (soundCount - errorCount) + "/" + soundCount + " sound effects.");
          
          if(errorEffects.isEmpty() == false) {
              System.out.println("These sound effects failed to load:");
              for(SoundEffect e : errorEffects) { System.out.println(e.toString()); }
          } else {
              System.out.println("All sound effects succesfully loaded!");
          }
          
      }
      
      // player's sounds and GUI sounds should use playSound
      // and other sounds playSoundWithPan.
      public void playSound(SoundEffect effect) {
          this.sounds.get(effect).play();
      }
      
      public void playSoundWithPan(SoundEffect effect, GameObject target) {
          
          // 1. calculate if the target is in range.
          // 2. calculate the position of the target (right/left) from the player
          // 3. calculate the volume of the sound effect using distance.
          
          Point playerPos = Game.instance.getActorManager().getPlayerInstance().getHitboxCenter();
          Point targetPos = target.getHitboxCenter();
          
          double distance = playerPos.distance(targetPos.x, targetPos.y);
          double pan = 0.0;
          
          if(distance < Game.ENEMY_ACTIVATION_RANGE) {
          
              // right or left
              pan = (targetPos.x > playerPos.x) ? 1 : -1;
              
              // Convert 0.0 to enemy_activ_range -> 0.0 to 1.0
              // distance is a point on this range.
              // https://stackoverflow.com/questions/5731863/mapping-a-numeric-range-onto-another
              double output = 0.0 + ((1.0 - 0.0) / (Game.ENEMY_ACTIVATION_RANGE - 0.0)) * (distance - 0.0);
              output = Mathf.round(1.0 - output, 2);
              
              // the closer the target is to player
              // the higher the volume and nearer the sound.
              this.sounds.get(effect).play(1.0 * output, pan * output);
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
          case PLAYER_HURT:
              path = "/sounds/player_hurt.wav";
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
          case THUD:
              path = "/sounds/thud.wav";
              break;
          default:
              System.out.println("SoundManager:: play: unsupported sound effect!");
              break;
          }
          return path;
      }
}
