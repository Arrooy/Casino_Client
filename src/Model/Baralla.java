package Model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.*;

public class Baralla {

    private final static String PATH = "./Assets/Cartes_BlackJack/";
    //private static LinkedList<BufferedImage> cartes;
    private static Map<String, BufferedImage> cartes;
    private static Stack<String> nomCartes;

    public static int loadContent() {

        nomCartes = new Stack<>();

        //cartes = new LinkedList<>();
        cartes = new HashMap<>();

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
                    cartes.put(carta.getName(), img);
                }
                nomCartes.push(carta.getName());
            }
        }
        return cartes.size();
    }

    public static Stack<String> getNomCartes() {
        //Enviem nomes les cartes que no son dorsals
        Stack<String>nomCartesNecesaries = new Stack<>();
        for(String carta : nomCartes){
            if(!carta.contains("back"))
                nomCartesNecesaries.push(carta);
        }

        return nomCartesNecesaries;
    }

    public static Image findImage(Card carta) {
        //Modifiquem el nom de la carta si aquesta ha d'estar girada
        String nomCarta = carta.getCardName();//carta.isGirada() ? carta.getReverseName() : carta.getCardName();
        return cartes.get(nomCarta);
    }
}
