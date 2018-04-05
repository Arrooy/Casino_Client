package Vista.GameViews;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.Collections;
import java.util.Stack;

public class Baralla {

    private final static String PATH = "./Assets/Cartes_BlackJack/";
    private Stack<Card> cartes;
    private int indexDemo;

    public Baralla(){
        cartes = new Stack<>();

        indexDemo = 0;

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
                    cartes.push(new Card(carta.getName(),img));
                }
            }
            System.out.println("Num cartes: " + cartes.size());
        }else{
            System.out.println("NO HI HAN CARTES PER CARREGAR");
        }
        shuffle();
    }

    private void shuffle() {
        Collections.shuffle(cartes);
    }


        


    public void paintCard(String nomCarta, Graphics g,int x,int y){
        g.drawImage(buscaCarta(nomCarta),x,y,null);
    }

    public void paintDemoCards(Graphics g,int x,int y){
        if(indexDemo >= 71)indexDemo=0;
        g.drawImage(cartes.get(indexDemo++).getImg(),x,y,null);
    }

    private Image buscaCarta(String nomCarta) {
        for(Card carta : cartes){
            if(carta.getName().equals(nomCarta))
                return carta.getImg();
        }
        return null;
        //TODO: RETURN DEFAULT IMAGE PER INDICAR QUE NO EXISTEIX L'IMATGE SOLICITADA
    }
}
