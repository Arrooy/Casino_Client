package Utils;


import Controlador.SplashScreen;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/** Sounds gestiona els sorolls i la musica del casino*/
public class Sounds extends Thread {

    /** Localitzacio dels sounds a l'interior de la carpeta d'assets*/
    private static final String PATH = "./Assets/Audio";

    /** Diccionari d'audios carregats*/
    private static Map<String, Clip> audios;

    /** Indica si els l'audio esta activat*/
    private static boolean muted;

    /**
     * Carrega tots els audios de la carpeta especificada en PATH
     * @param splashScreen pantalla on es mostren missatges de l'estat de carrega
     */
    public static void loadAllSounds(SplashScreen splashScreen){
        //Al iniciar el programa, s'activa la musica
        muted = false;

        audios = new HashMap<>();

        //Es llegeix la carpeta especificada en PATH
        File carpetaAssetsCartes = new File(PATH);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        //Si la carpeta no esta buida
        if (listOfFiles != null) {
            //Es carregan tots els audios
            for (File soundFile : listOfFiles) {
                try {
                    AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

                    // Agafem el resource del audio clip.
                    Clip clip = AudioSystem.getClip();

                    // S'obre el audio i es guarda
                    clip.open(audioIn);
                    audios.put(soundFile.getName(),clip);

                    //S'indica el progres de audios carregats
                    splashScreen.infoMessage("Loaded " + audios.size() + " sound clips.");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Para tots els audios del sistema
     */
    public static void stopAllAudio(){
        //parem tots els clips de la llista d'audios
        audios.forEach((nom,clip)->{if (clip.isRunning()) clip.stop();});
    }

    /**
     * Para el audio file
     * @param file nom del audio que es vol aturar
     */
    public static void stopOneAudioFile(String file){
        Clip audioToStop = audios.get(file);
        if(audioToStop.isRunning())
            audioToStop.stop();
    }

    /**
     * Reprodueix un audio guardat en el sistema
     * @param fileName nom del audio que es vol reproduir
     */
    public static void play(String fileName){
        if(!muted) {
            //S'agafa l'audio del diccionari
            Clip clip = audios.get(fileName);

            //En el cas d'estar reproduint-se, s'atura
            if (clip.isRunning()) clip.stop();

            //Es torna a la posicio inicial del clip
            clip.setFramePosition(0);

            //S'inicia l'audio
            clip.start();
        }
    }

    /**
     * Reprodueix un audio guardat en el sistema en el instant de temps indicat
     * @param fileName nom del audio que es vol reproduir
     * @param startTime temps inicial de reproducio en ms
     */
    public static void play(String fileName,long startTime){
        if(!muted) {
            //S'agafa l'audio del diccionari
            Clip clip = audios.get(fileName);

            //En el cas d'estar reproduint-se, s'atura
            if (clip.isRunning()) clip.stop();

            //Es torna a la posicio inicial del clip
            clip.setMicrosecondPosition(startTime * 1000);

            //S'inicia l'audio
            clip.start();
        }
    }

    /**
     * Reprodueix l'audio filename indefinidament
     * @param fileName nom del audio que es vol reproduir
     */
    public static void songNoEnd(String fileName){
        if(!muted)audios.get(fileName).loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Espera a que un audio acabi de reproduir-se
     * @param nomFitxer nom del fitxer al que es vol esperar
     */
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

    /**
     * Indica si l'audio esta silenciat o no
     * @return l'audio esta silenciat
     */
    public static boolean isMuted() {
        return muted;
    }

    /**
     * Actualitza l'estat de l'audio
     * @param muted nou estat de l'audio. Silenciat o no silenciat
     */
    public static void setMuted(boolean muted) {
        Sounds.muted = muted;
        if(muted){
            Sounds.stopAllAudio();
        }else{
            Sounds.songNoEnd("wii.wav");
        }
    }
}
