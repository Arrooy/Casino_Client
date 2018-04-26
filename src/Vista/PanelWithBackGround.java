package Vista;


import Model.AssetManager;

import javax.swing.*;
import java.awt.*;

public class PanelWithBackGround extends JPanel {

    public PanelWithBackGround(BorderLayout borderLayout) {
        super(borderLayout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(AssetManager.getImage("background.jpg"),0,0,null);
    }
}
