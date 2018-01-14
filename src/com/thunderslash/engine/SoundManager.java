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
    private Clip loopClip;
    
    private float baseVolumeLevel = -20f;
    
    public SoundManager() {
        
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixInfos[0]);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
        
        try { clip = (Clip)mixer.getLine(dataInfo); }
        catch(LineUnavailableException e) { e.printStackTrace(); }
        
        try { loopClip = (Clip)mixer.getLine(dataInfo); }
        catch(LineUnavailableException e) { e.printStackTrace(); }
        
        System.out.println("Mixer: " + mixer.getMixerInfo() + " succesfully loaded!");
        
    }

    public void playLoop(SoundEffect effect) {

        if(loopClip.isOpen()) loopClip.close();
        
        String path = this.getPath(effect);
        
        if(path != null && path.length() > 0) {
            
            // load file
            try {
                URL soundURL = this.getClass().getResource(path);
                AudioInputStream stream = AudioSystem.getAudioInputStream(soundURL);
                loopClip.open(stream);
            } 
            catch(LineUnavailableException e) { e.printStackTrace(); }
            catch(UnsupportedAudioFileException e) { e.printStackTrace(); }
            catch(IOException e) { e.printStackTrace(); }
            
            // set volume
            FloatControl volumeControl = (FloatControl) loopClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(this.baseVolumeLevel);
            
            // set to loop
            loopClip.loop(Clip.LOOP_CONTINUOUSLY);
            
            // play file
            loopClip.start();
            
        } else {
            System.out.println("SoundManager::play: soundfile\'s path is invalid!");
        }
        
    }
    
    public void stopLoop() {
        this.loopClip.stop();
        this.loopClip.close();
    }
    
    public void play(SoundEffect effect) {
        
        if(clip.isOpen()) clip.close();
        
        String path = this.getPath(effect);
        
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
            
        } else {
            System.out.println("SoundManager::play: soundfile\'s path is invalid!");
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
    
    public Mixer getMixer() { return mixer; }
    public void setMixer(Mixer mixer) { this.mixer = mixer; }
    public Clip getCurrentClip() { return clip; }
    public void setCurrentClip(Clip currentClip) { this.clip = currentClip; }
}