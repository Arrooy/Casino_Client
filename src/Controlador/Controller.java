package Controlador;

import Vista.MainView;
import Network.*;
import Vista.Tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/** Controlador del client*/

public class Controller implements ActionListener, WindowListener {

    /** Finestra grafica del client*/
    private MainView view;

    /** Responsable de la connectivitat amb el servidor*/
    private NetworkManager networkManager;

    /** Inicialitza un nou controlador i realitza les relacions amb la vista i el gestor de la connectivitat*/
    public Controller(MainView view, NetworkManager networkManager) {
        this.view = view;
        this.networkManager = networkManager;
    }

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        view.displayError(title,errorText);
    }

    /** Metode per a tencar el client de forma segura.*/
    public void exitProgram(int status){
        networkManager.requestLogOut();
        Tray.exit();
        System.exit(status);
    }

    /** Retorna l'estat de la JCheckBox RememberLogIn inidcant doncs si s'ha de guardar localment el login del usuari*/
    public boolean rememberLogIn(){
        return view.getRememberLogIn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "logIn":
                //S'intenta fer logIn al servidor amb les credencials introduides
                networkManager.logIn(view.getUsername(),view.getPassword());
                break;
            case "logOut":
                //S'intenta desconectar-se del servidor
                networkManager.requestLogOut();
                break;
            case "trayButtonExit":
                exitProgram(0);
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exitProgram(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {


    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
