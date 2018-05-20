package Vista;


import Utils.AssetManager;
import Utils.Baralla;

import javax.swing.*;
import java.awt.*;

public class PanelWithBackGround extends JPanel {

    private String backGroundName;
    private boolean expandToPanel;

    public PanelWithBackGround(String backGroundName){
        super(new BorderLayout());
        this.backGroundName = backGroundName;
    }

    public PanelWithBackGround(String backGroundName,boolean expandToPanel){
        this(backGroundName);
        this.expandToPanel = expandToPanel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(expandToPanel){
            g.drawImage(Baralla.getRandomCard(),0,0,null);
        }else{
            g.drawImage(AssetManager.getImage(backGroundName),0,0,null);
        }
    }
}
