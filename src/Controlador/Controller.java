package Controlador;

import Controlador.Game_Controlers.BlackJackController;
import Model.AssetManager;
import Model.Baralla;
import Model.Card;
import Model.User;
import Vista.*;
import Network.*;
import Vista.GameViews.BlackJack.BlackJackView;

import javax.swing.*;
import java.awt.event.*;

/** Controlador del client*/

public class Controller implements ActionListener, WindowListener, MouseListener, ComponentListener{

    /** Finestra grafica del client*/
    private Finestra finestra;
    private MainViewClient mainView;
    private LogInView logInView;
    private SignInView signInView;
    private GameSelectorView gameSelectorView;
    private SettingsView settingsView;
    private BlackJackView blackJackView;

    private BlackJackController BJController;

    /** Usuari que controla el client*/
    private User user;

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
        return logInView.getRememberLogIn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "goToLogIn":
                finestra.setLogInView();
                break;
            case "backToMain":
                logInView.clearFields();
                signInView.clearFields();
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
                finestra.setMainView();
                break;
            case "settings":
                finestra.setSettingsView();
                break;
            case "signIn":
                finestra.setSignInView();
                break;
            case "guest":
                networkManager.enterAsGuest();
                break;
            case "roulette":
                if(user.isGuest()){
                    displayError("ETS UN GUEST PUTA","no pots pasar hehe");
                }else{
                    //Codi per a usuaris normals
                }
                break;
            case "horse":
                if(user.isGuest()){
                    displayError("ETS UN GUEST PUTA","no pots pasar hehe");
                }else{
                    //Codi per a usuaris normals
                }
                break;
            case "blackJack":
                networkManager.initBlackJack(Baralla.getNomCartes());
                break;
            case "exitProgram":
                exitProgram(0);
                break;
            case "goSignIn":
                finestra.setSignInView();
                break;
            case "acceptSignIn":
                if (signInView.passwordIsCorrect()) signUp();
                else ;//TODO: mostra error informatiu
                break;
        }
    }

    public void showFinestra() {
        finestra.setVisible(true);
        finestra.requestFocus();
    }

    public void setUser(User u){
        user = u;
    }

    public void newBJCard(Card cartaResposta) {
        BJController.newBJCard(cartaResposta);
    }

    private void signUp() {
        networkManager.requestSignUp(finestra.getSignUpUser());
    }

    public void showErrorLogIn(String s) {
        logInView.setError(s);
    }
    public void setMainView(MainViewClient mainView) {
        this.mainView = mainView;
    }
    public void setLogInView(LogInView logInView) {
        this.logInView = logInView;
    }
    public void setSignInView(SignInView signInView) {this.signInView = signInView;}
    public void setGameSelectorView(GameSelectorView gameSelectorView) {this.gameSelectorView = gameSelectorView;}
    public void setSettingsView(SettingsView settingsView) {this.settingsView = settingsView;}
    public void showGamesView() {
        logInView.clearFields();
        finestra.setGameSelector();
    }

    public void initBlackJack() {
        BJController = new BlackJackController(blackJackView,networkManager);
        finestra.setBlackJackView();
    }

    public void setBlackJackView(BlackJackView blackJackView) {
        this.blackJackView = blackJackView;
    }

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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void componentResized(ComponentEvent e) {
        if(BJController != null)
            BJController.updateSize(false);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if(BJController != null)
            BJController.updateSize(true);
    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
