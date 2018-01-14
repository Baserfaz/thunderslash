package com.thunderslash.engine;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.thunderslash.enumerations.SoundEffect;

public class SoundManager {

    private Mixer mixer;
    private Clip clip;
    
    private float baseVolumeLevel = -20f;
    
    public SoundManager() {
        
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixInfos[0]);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
        
        try { clip = (Clip)mixer.getLine(dataInfo); }
        catch(LineUnavailableException e) { e.printStackTrace(); }
        
        System.out.println("Mixer: " + mixer.getMixerInfo() + " succesfully loaded!");
        
        //this.play(SoundEffect.SELECT);
        
    }

    public void playLoop(SoundEffect effect) {
        // TODO
    }
    
    public void play(SoundEffect effect) {
        
        String path = "";
        
        switch(effect) {
        case SELECT:
            path = "/sounds/select.wav";
            break;
        default:
            System.out.println("SoundManager:: play: unsupported sound effect!");
            break;
        }
        
        if(path != null && path.length() > 0) {
            
            // load file
            try {
                URL soundURL = this.getClass().getResource(path);
                AudioInputStream stream = AudioSystem.getAudioInputStream(soundURL);
                clip.open(stream);
            } 
            catch(LineUnavailableException e) { e.printStackTrace(); }
            catch(UnsupportedAudioFileException e) { e.printStackTrace(); }
            catch(IOException e) { e.printStackTrace(); }
            
            // set volume
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(this.baseVolumeLevel);
            
            // play file
            clip.start();
        }
        
    }
    
    public Mixer getMixer() { return mixer; }
    public void setMixer(Mixer mixer) { this.mixer = mixer; }
    public Clip getCurrentClip() { return clip; }
    public void setCurrentClip(Clip currentClip) { this.clip = currentClip; }
}
