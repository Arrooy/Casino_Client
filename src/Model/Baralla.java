package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.LinkedList;

public class Baralla {

    private final static String PATH = "./Assets/Cartes_BlackJack/";
    private LinkedList<BufferedImage> cartes;
    private LinkedList<String> nomCartes;

    public Baralla() {

        nomCartes = new LinkedList<>();

        cartes = new LinkedList<>();

        File carpetaAssetsCartes = new File(PATH);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        if (listOfFiles != null) {
            for (File carta : listOfFiles) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(carta);
                    System.out.println(carta.getName() + ":" + img.getWidth() + " " + img.getHeight());
                } catch (IOException e) {
                    System.out.println("Error llegint " + carta.getName());
                }
                if (img != null) {
                    cartes.add(img);
                }
                nomCartes.add(carta.getName());
            }
        }

    }

    public LinkedList<String> getNomCartes() {
        return nomCartes;
    }

    /*
    public void paintCard(String nomCarta, Graphics g, int x, int y) {

        g.drawImage(buscaCarta(nomCarta), x, y, null);
    }
 */

    private Image buscaCarta(String nomCarta) {

        File[] listOfFiles = new File(PATH).listFiles();

        if (listOfFiles != null) {

            for (int i = 0; i < listOfFiles.length; i++) {
                if (nomCarta.equalsIgnoreCase(listOfFiles[i].getName())) {
                    return cartes.get(i);
                }
            }
        }
        //Si no trobem res.
        return null;
    }
}
