package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class Baralla {

    private final static String PATH = "./Assets/Cartes_BlackJack/";
    private static LinkedList<BufferedImage> cartes;
    private static Stack<String> nomCartes;

    public static void loadContent() {

        nomCartes = new Stack<>();

        cartes = new LinkedList<>();

        File carpetaAssetsCartes = new File(PATH);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        if (listOfFiles != null) {
            for (File carta : listOfFiles) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(carta);
                } catch (IOException e) {
                    System.out.println("Error llegint " + carta.getName());
                }
                if (img != null) {
                    cartes.add(img);
                }
                nomCartes.push(carta.getName());
            }
            System.out.println("Nombre de cartes carregades: " + cartes.size());
        }
    }

    public static Stack<String> getNomCartes() {
        Stack<String>nomCartesNecesaries = new Stack<>();
        for(String carta : nomCartes){
            if(!carta.contains("back"))
                nomCartesNecesaries.push(carta);
        }
        System.out.println("Sending:" + Arrays.toString(nomCartesNecesaries.toArray()));
        return nomCartesNecesaries;
    }

    public static Image findImage(Card carta) {

        String nomCarta = carta.isGirada() ? carta.getReverseName() : carta.getCardName();

        File[] listOfFiles = new File(PATH).listFiles();

        if (listOfFiles != null) {

            for (int i = 0; i < listOfFiles.length; i++) {
                if (nomCarta.equalsIgnoreCase(listOfFiles[i].getName())) {
                    return cartes.get(i);
                }
            }
        }
        //Per si no trobem res.
        return null;
    }
}
