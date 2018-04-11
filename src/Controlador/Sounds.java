package Controlador;


import Vista.SplashScreen;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class Sounds extends Thread {

    private static Map<String, Clip> audios;

    public static void loadAllSounds(SplashScreen splashScreen){
        audios = new HashMap<>();


        File carpetaAssetsCartes = new File("./Assets/Audio");
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        if (listOfFiles != null) {
            for (File soundFile : listOfFiles) {
                try {
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                    // Get a sound clip resource.
                    Clip clip = AudioSystem.getClip();
                    // Open audio clip and load samples from the audio input stream.
                    clip.open(audioIn);//TODO: INFORMARSE DE SI AIXO ESTA BE FERHO AQUI O ES MILLOR FERHO CADA COP QUE EES FA PLAY
                    audios.put(soundFile.getName(),clip);
                    splashScreen.infoMessage("Loaded " + audios.size() + " sound clips.");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void stopAllAudio(){
        //parem tots els clips de la llista d'audios
        audios.forEach((nom,clip)->{if (clip.isRunning()) clip.stop();});
    }

    public static void play(String fileName){

        System.out.println("[SOUND]: " + fileName);
        Clip clip = audios.get(fileName);
        if(clip.isRunning()) clip.stop();

        clip.setFramePosition(0);
        clip.start();
    }

    public static void songNoEnd(String fileName){
        audios.get(fileName).loop(Clip.LOOP_CONTINUOUSLY);
    }

    public static void waitFor(String nomFitxer) {
        Clip clip = audios.get(nomFitxer);
        while(clip.isRunning()){
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
