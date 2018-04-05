package Vista.GameViews;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Baralla {

    private final static String PATH = "./Assets/Cartes_BlackJack/";
    private ArrayList<BufferedImage> cartes;

    public Baralla(){
        cartes = new ArrayList<>();

        File carpetaAssetsCartes = new File(PATH);
        File[] listOfFiles = carpetaAssetsCartes.listFiles();

        if(listOfFiles != null){
            for (File carta : listOfFiles) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read(carta);
                    System.out.println(carta.getName() + ":" + img.getWidth() + " " + img.getHeight());
                } catch (IOException e) {

                }
                if (img == null){
                    System.out.println("La carta " + carta.getName() + " no s'ha carregat.");
                }else {
                    cartes.add(img);
                }
            }
            System.out.println("Num cartes: " + cartes.size());
        }else{
            System.out.println("NO HI HAN CARTES PER CARREGAR");
        }
    }
    public void paintCard(String carta, Graphics g){

    }
}
