package Vista;

import javax.swing.*;
import java.awt.*;

public class Tray {

    private MenuItem sortir;
    private TrayIcon trayIcon;


    public Tray(){

        //AFEGIR ABANS DE CREAR UNA TRAY
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
        }

        PopupMenu popup = new PopupMenu();

        trayIcon = new TrayIcon(new ImageIcon("data/PitchTrayIcon.gif").getImage());

        trayIcon.setImageAutoSize(true);

        SystemTray tray = SystemTray.getSystemTray();

        sortir = new MenuItem("Sortir del programa");

        popup.add(sortir);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}