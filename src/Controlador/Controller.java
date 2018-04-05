package Controlador;

import Vista.*;
import Network.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/** Controlador del client*/

public class Controller implements ActionListener, WindowListener {

    /** Finestra grafica del client*/
    private Finestra finestra;
    private MainViewClient mainView;
    private LogInView logInView;
    private SignInView signInView;
    private GameSelector gameSelector;
    private SettingsView settingsView;

    /** Responsable de la connectivitat amb el servidor*/
    private NetworkManager networkManager;

    /** Inicialitza un nou controlador i realitza les relacions amb la vista i el gestor de la connectivitat*/
    public Controller(Finestra finestra, NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.finestra = finestra;
    }

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        mainView.displayError(title,errorText);
    }

    /** Metode per a tencar el client de forma segura.*/
    public void exitProgram(int status){
        networkManager.requestLogOut();
        Tray.exit();
        System.exit(status);
    }

    /** Retorna l'estat de la JCheckBox RememberLogIn inidcant doncs si s'ha de guardar localment el login del usuari*/
    public boolean rememberLogIn(){
        return (logInView != null) && logInView.getRememberLogIn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "goToLogIn":
                finestra.setLogInView();
                break;
            case "backToMain":
                logInView.clearFields();
                finestra.setMainView();
                break;
            case "logIn":
                //S'intenta fer logIn al servidor amb les credencials introduides
                networkManager.logIn(logInView.getUsername(), logInView.getPassword());
                logInView.clearFields();
                break;
            case "logOut":
                //S'intenta desconectar-se del servidor
                networkManager.requestLogOut();
                break;
            case "settings":
                finestra.setSettingsView();
                break;
            case "signIn":
                finestra.setSignInView();
                break;
            case "guest":
                displayError("YOW! ETS INVITADO","Molt guay :D");
                break;
            case "roulette":
                finestra.setBlackJackView();
                break;
            case "horse":
                break;
            case "blackJack":
                break;
            case "trayButtonExit":
                exitProgram(0);
                break;
        }
    }

    public void setMainView(MainViewClient mainView) {
        this.mainView = mainView;
    }
    public void setLogInView(LogInView logInView) {
        this.logInView = logInView;
    }
    public void setSignInView(SignInView signInView) {this.signInView = signInView;}
    public void setGameSelector(GameSelector gameSelector) {this.gameSelector = gameSelector;}
    public void setSettingsView(SettingsView settingsView) {this.settingsView = settingsView;}

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exitProgram(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public void showGamesView() {
        finestra.setGameSelector();
    }
}
