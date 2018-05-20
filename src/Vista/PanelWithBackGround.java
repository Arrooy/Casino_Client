package Vista;


import Utils.AssetManager;
import javax.swing.*;
import java.awt.*;

/**
 * Genera un panell que té background
 */

public class PanelWithBackGround extends JPanel {

    /** Nom de l'imatge de background*/
    private String backGroundName;

    /** Crea el panell definint l'imatge de fons*/
    public PanelWithBackGround(String backGroundName){
        super(new BorderLayout());
        this.backGroundName = backGroundName;
    }

    /** Es pinta l'imatge i despres el contingut del panell*/
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(AssetManager.getImage(backGroundName),0,0,null);
        super.paintComponent(g);
    }
}
