package Controlador;

import Model.Baralla;
import Model.BlackJack;
import Model.Card;
import Model.User;
import Vista.*;
import Network.*;

import java.awt.event.*;

/** Controlador del client*/

public class Controller implements ActionListener, WindowListener, MouseListener {

    /** Finestra grafica del client*/
    private Finestra finestra;
    private MainViewClient mainView;
    private LogInView logInView;
    private SignInView signInView;
    private GameSelectorView gameSelectorView;
    private SettingsView settingsView;

    /** Usuari que controla el client*/
    private User user;

    /** Responsable de la connectivitat amb el servidor*/
    private NetworkManager networkManager;

    /** Baralla de cartes del blackJack*/
    private BlackJack blackJack;

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
                displayError("YOW! ETS INVITADO","Molt guay :D");
                break;
            case "roulette":

                break;
            case "horse":
                break;
            case "blackJack":
                //Es crea el model del blackJack
                //TODO: REVISAR SI ESTA BE CREAR EL MODEL AQUI O ES MILLOR AL MAIN I NO FERLO SERVIR FINS ARA.
                blackJack = new BlackJack(user);
                networkManager.initBlackJack(blackJack.getNomCartes());
                finestra.setBlackJackView();
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

    public void setUser(User u){
        user = u;
    }

    public void newBJCard(Card cartaResposta) {
        blackJack.addCard(cartaResposta);
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
        finestra.setGameSelector();
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
        System.out.println("Entered in a element: " + e.getComponent().getClass().getName());
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
