package Model;

import Controlador.SplashScreen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Chips {

    private static final String PATH = "./Assets/Chips/";
    private static Map<String,BufferedImage> chips;

    public static void load(SplashScreen splashScreen) {
        chips = new HashMap<>();

        File carpetaAssetsChips = new File(PATH);
        File[] listOfFiles = carpetaAssetsChips.listFiles();

        if (listOfFiles != null) {
            for (File chip : listOfFiles) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(chip);
                } catch (IOException e) {
                    System.out.println("Error llegint " + chip.getName());
                    splashScreen.stop();
                }
                if (img != null) {
                    chips.put(chip.getName(), img);
                    splashScreen.infoMessage("Loaded " + chips.size() + " token images.");
                }
            }
        }
    }

    public static BufferedImage getChip(String name){
        return chips.get(name);
    }
}
