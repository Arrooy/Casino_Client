package Vista.GameViews;

import java.awt.image.BufferedImage;

public class Card {

    private String name;
    private BufferedImage img;
    private int posX;
    private int posY;
    private boolean flipped;

    public Card(String name, BufferedImage img){
        this.name = name;
        this.img = img;
        flipped = false;
        posX = 0;
        posY = 0;
    }

    public Card(String name, BufferedImage img,int x,int y){
        this.name = name;
        this.img = img;
        posX = x;
        posY = y;
        flipped = false;
    }

    public boolean isFlipped() {
        return flipped;
    }
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }
    public BufferedImage getImg() {
        return img;
    }
    public void setImg(BufferedImage img) {
            this.img = img;
        }
    public int getPosX() {
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosY() {
        return posY;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }
}