package Model;

import Controlador.Sounds;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AssetManager {

    private static Map<String, BufferedImage> imatges;
    private final static String PATH_IMATGES = "./Assets/Images";

    public static void loadData() {
        System.out.println("[LOADER]: " + Baralla.loadContent() + " images.");
        /*System.out.println("50% data loaded");*///TODO:millorar amb splash screen
        System.out.println("[LOADER]: " + Sounds.loadAllSounds() + " sound clips");
        System.out.println("[LOADER]: " + loadImatges() + " normal imgs");
    }

    private static int loadImatges(){

        File carpetaAssetsCartes = new File(PATH_IMATGES);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        imatges = new HashMap<>();

        if (listOfFiles != null) {
            for (File foto : listOfFiles) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(foto);
                } catch (IOException e) {
                    System.out.println("Error llegint " + foto.getName());
                }
                if (img != null) {
                    imatges.put(foto.getName(), img);
                }
            }
        }
        return imatges.size();
    }
    public static Image getImage(String nom){
        return imatges.get(nom);
    }
}
