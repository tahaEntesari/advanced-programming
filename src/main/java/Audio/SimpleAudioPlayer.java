package Audio;
// Code Taken from GeeksForGeeks
// Java program to play an Audio
// file using Clip Object

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SimpleAudioPlayer {
    public enum Sounds {
        timerTickToc
    }

    private HashMap<Sounds, Clip> clip = new HashMap<>();
    private static SimpleAudioPlayer instance;

    public static SimpleAudioPlayer getInstance() {
        if (instance == null) instance = new SimpleAudioPlayer();
        return instance;
    }

    private SimpleAudioPlayer() {
        String baseLocation = Configurations.Main_config_file.getAudioBaseLocations();
        try {
            this.clip.put(Sounds.timerTickToc, AudioSystem.getClip());
            this.clip.get(Sounds.timerTickToc).open(AudioSystem.getAudioInputStream(
                    new File(baseLocation + "timerTickToc.wav").getAbsoluteFile()));

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void playSound(Sounds soundName) {
        switch (soundName) {
            case timerTickToc:
                clip.get(Sounds.timerTickToc).setMicrosecondPosition(0);
                clip.get(Sounds.timerTickToc).start();
                break;

        }
    }

    public void stopSound(Sounds soundName) {
        switch (soundName){
            case timerTickToc:
                clip.get(Sounds.timerTickToc).stop();
        }
    }

}



