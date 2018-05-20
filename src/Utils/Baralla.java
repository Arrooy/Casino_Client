package Utils;

import Controlador.SplashScreen;
import Model.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.*;

/** Guestiona les cartes del BlackJack i les seves imatges*/
public class Baralla {

    /** Localitzacio de les cartes a l'interior de la carpeta del projecte*/
    private final static String PATH = "./Assets/Cartes_BlackJack/";

    /** Diccionari de totes les cartes*/
    private static Map<String, BufferedImage> cartes;

    /** Nom de totes les cartes del joc*/
    private static Stack<String> nomCartes;

    /**
     * Carrega totes les imatges de les cartes
     * @param splashScreen splash screen per on s'indicaran errors i l'estat de la carrega
     */
    public static void loadContent(SplashScreen splashScreen) {

        nomCartes = new Stack<>();

        cartes = new HashMap<>();
        //Es llegeix totala carpeta indicada en PATH
        File carpetaAssetsCartes = new File(PATH);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        //Si la carpeta existeix
        if (listOfFiles != null) {
            //Per a tots els arxius de la carpeta es carrega la imatge
            for (File carta : listOfFiles) {
                BufferedImage img = null;
                try {
                    //Es llegeix la imatge
                    img = ImageIO.read(carta);
                } catch (IOException e) {
                    //En cas d'error, es para la carrega de Assets
                    System.out.println("Error llegint " + carta.getName());
                    splashScreen.stop();
                }
                if (img != null) {
                    //Si s'ha pogut carregar la carta, s'afegeix al diccionari
                    cartes.put(carta.getName(), img);
                    //I s'indica a la loadingScreen
                    splashScreen.infoMessage("Loaded " + cartes.size() + " card images.");
                }
                //S'afegeix el nom de la carta a la llista de noms
                nomCartes.push(carta.getName());
            }
        }
    }

    /**
     * Retorna el conjunt de noms de les cartes
     * @return una pila amb el nom de les cartes
     */
    public static Stack<String> getNomCartes() {
        //Enviem nomes les cartes que no son dorsals
        Stack<String>nomCartesNecesaries = new Stack<>();
        for(String carta : nomCartes){
            if(!carta.contains("back"))
                nomCartesNecesaries.push(carta);
        }

        return nomCartesNecesaries;
    }

    /**
     * Retorna la imatge de la carta solicitada
     * @param carta carta de la que es vol obtindre la imatge
     * @return imatge de la carta
     */
    public static Image findImage(Card carta) {
        //Modifiquem el nom de la carta si aquesta ha d'estar girada
        String nomCarta = carta.isGirada() ? carta.getReverseName() : carta.getCardName();
        return cartes.get(nomCarta);
    }

    /**
     * Retorna una carta aleatoria del Diccionari. Funcio que s'utilitza per a l'animacio del blackJack
     * @return La imatge de la carta
     */
    public static BufferedImage getRandomCard() {
        return (BufferedImage)cartes.values().toArray()[(int)(Math.random() *cartes.size())];
    }
}
