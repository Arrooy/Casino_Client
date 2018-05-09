package Model;

import Controlador.Sounds;
import Controlador.SplashScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Image.SCALE_DEFAULT;

/** Gestiona tots els assets del joc. Coordina la càrrega de contingut inicial i guarda les imatges orientades a la UI*/
public class AssetManager {

    /** Localitzacio de les imatges de la UI*/
    private final static String PATH_IMATGES = "./Assets/Images";

    /** Conjunt d'imatges de la UI*/
    private static Map<String, BufferedImage> imatges;

    /** Carrega tot el contingut del joc*/
    public static void loadData(SplashScreen splashScreen) {
        Baralla.loadContent(splashScreen);
        Chips.load(splashScreen);
        loadImatges(splashScreen);
        Sounds.loadAllSounds(splashScreen);
    }

    /** Carrega les imatges de la UI*/
    private static void loadImatges(SplashScreen splashScreen){
        //Es llegeix la carpeta indicada en PATH_IMATGES
        File carpetaAssetsCartes = new File(PATH_IMATGES);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        imatges = new HashMap<>();

        //Si la carpeta existeix
        if (listOfFiles != null) {
            //Per cada foto de la UI
            for (File foto : listOfFiles) {
                BufferedImage img = null;
                try {
                    //Es llegeix la foto
                    img = ImageIO.read(foto);
                } catch (IOException e) {
                    e.printStackTrace();
                    //En el cas de trobar un error en la carrega, es para el sistema
                    System.out.println("Error llegint " + foto.getName());
                    splashScreen.stop();
                }
                if (img != null) {
                    //Si la lectura ha sigut correcte, es guarda la imatge
                    imatges.put(foto.getName(), img);

                    //S'indica en la loadingScreen l'estat de la carrega
                    splashScreen.infoMessage("Loaded " + imatges.size() + " UI images.");
                }
            }
        }
    }

    public static Font getEFont() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("./Assets/Fonts/ELEPHNT.TTF")).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./Assets/Fonts/ELEPHNT.TTF")));
            return font;
        } catch (IOException|FontFormatException e) {
            System.out.println("No funciona la font");
            e.printStackTrace();
        }
        return new Font("Comic Sans", Font.PLAIN, 20);
    }

    /**
     * Retorna una imatge de la UI
     * @param nom nom de la imatge que es vol obtenir
     * @return imatge solicitada
     */

    public static Image getImage(String nom){
        return imatges.get(nom);
    }

    /**
     * Retorna una imatge de la UI escalada
     * @param nom nom de la imatge que es vol obtenir
     * @param width amplada de la imatge
     * @param height altura de la imatge
     * @return imatge amb el tamany personalitzat
     */
    public static Image getImage(String nom, int width, int height) {
        return imatges.get(nom).getScaledInstance(width,height,SCALE_DEFAULT);
    }
}
