package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class Tray {

    private static MenuItem sortir;
    private static TrayIcon trayIcon;

    public static void init(){
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null,"Error adding tray","SystemTray is not supported",JOptionPane.ERROR_MESSAGE);
        }else{

            PopupMenu popup = new PopupMenu();

            trayIcon = new TrayIcon(new ImageIcon("data/PitchTrayIcon.gif").getImage());

            trayIcon.setImageAutoSize(true);

            SystemTray tray = SystemTray.getSystemTray();

            sortir = new MenuItem("Sortir del client");

            popup.add(sortir);

            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }
        }
    }

    public static void addController(Controller c) {
        sortir.addActionListener(c);
        sortir.setActionCommand("trayButtonExit");
    }

    public static void exit(){
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }
}