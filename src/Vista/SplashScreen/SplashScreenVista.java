package Vista.SplashScreen;

import javax.swing.*;
import java.awt.*;

public class SplashScreenVista extends JFrame {

    public SplashScreenVista(int width, int height){
        //Es configura el JFrame per apareixer un una sola animacio al centre de la screen
        setUndecorated(true);
        setSize(width,height);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
        setVisible(true);
        requestFocus();
    }
}
