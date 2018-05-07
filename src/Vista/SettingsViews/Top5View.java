package Vista.SettingsViews;


import javax.swing.*;
import java.awt.*;

public class Top5View extends JPanel {

    public Top5View(){
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.fillRect(0,0,150,150);
    }
}
