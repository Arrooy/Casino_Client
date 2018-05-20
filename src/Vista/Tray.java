package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;


/** Gestor de la system Tray i de l'icono que apareix en ella*/
public class Tray {

    /** JButton per a sortir del casino*/
    private static MenuItem sortir;
    /** TrayIcon que apareix a la systemTray*/
    private static TrayIcon trayIcon;

    /** Si es possible crear la tray, es genera amb l'icono icon.png i es genera el boto per a sortir.
     * Finalment s'afegeix el boto a la Tray
     */
    public static void init(){
        if (!SystemTray.isSupported()) {
            JOptionPane.showMessageDialog(null,"Error adding tray","SystemTray is not supported",JOptionPane.ERROR_MESSAGE);
        }else{

            PopupMenu popup = new PopupMenu();

            trayIcon = new TrayIcon(new ImageIcon("Assets/Images/icon.png").getImage());

            trayIcon.setImageAutoSize(true);

            SystemTray tray = SystemTray.getSystemTray();

            sortir = new MenuItem("Sortir del client");

            popup.add(sortir);

            trayIcon.setPopupMenu(popup);
            trayIcon.setToolTip("Casino game - LaSalle 2018");

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                JOptionPane.showMessageDialog(null,"TrayIcon could not be added.","SystemTray error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Realitza l'enllaç Vista amb Controlador
     * @param c controlador que gestiona el boto de la tray
     */
    public static void addController(Controller c) {
        sortir.addActionListener(c);
        sortir.setActionCommand("exitProgram");
    }

    /**
     * Borra l'icono de la tray
     */
    public static void exit(){
        SystemTray tray = SystemTray.getSystemTray();
        tray.remove(trayIcon);
    }
}