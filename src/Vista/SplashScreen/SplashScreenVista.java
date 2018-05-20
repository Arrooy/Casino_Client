package Vista.SplashScreen;

import javax.swing.*;
import java.awt.*;

/** JFrame de la loadingScreen*/
public class SplashScreenVista extends JFrame {
    /**
     * Configura el JFrame amb la mida especificada
     * @param width amplada de la loading screen
     * @param height altura de la loading screen
     */
    public SplashScreenVista(int width, int height){
        //Es configura el JFrame per apareixer com una sola animacio al centre de la screen
        setUndecorated(true);
        setSize(width,height);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
        setVisible(true);
        requestFocus();
        setAlwaysOnTop(true);
    }
}
