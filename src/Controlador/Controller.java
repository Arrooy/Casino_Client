package Controlador;

import Controlador.Game_Controlers.BlackJackController;
import Model.Baralla;
import Model.Card;
import Model.Model_BJ;
import Model.User;
import Vista.*;
import Network.*;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.SettingsViews.*;

import javax.swing.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/** Controlador del client*/

public class Controller implements ActionListener, WindowListener, MouseListener, ComponentListener, KeyListener{

    /** Finestra grafica del client*/
    private Finestra finestra;
    private MainViewClient mainView;
    private LogInView logInView;
    private SignInView signInView;
    private GameSelectorView gameSelectorView;
    private AddMoneyView addMoneyView;
    private Settings settings;
    private SettingsView settingsView;
    private PasswordChangeView passwordChangeView;
    private WalletEvolutionView walletEvolutionView;
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
                break;
            case "logOut":
                //S'intenta desconectar-se del servidor
                JsonManager.removeRemember();
                networkManager.requestLogOut();
                finestra.setMainView();

                break;
            case "signIn":
                finestra.setSignInView();
                break;
            case "guest":
                networkManager.enterAsGuest();
                break;
            case "roulette":
                    //Codi per a usuaris normals
                break;
            case "horse":
                    //Codi per a usuaris normals
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
            case "settings":
                finestra.setSettingsView("NOTHING");
                break;
            case "SETTINGS - backFromSettings":
                finestra.setGameSelector(user.isGuest());
                break;
            case"SETTINGS - changePass":
                finestra.setSettingsView(e.getActionCommand());
                break;
            case"SETTINGS - addMoneyButton":
                finestra.setSettingsView(e.getActionCommand());
                break;
            case"SETTINGS - walletEvolution":
                finestra.setSettingsView(e.getActionCommand());
                break;
            case "PASSWORD CHANGE  - CONFIRM PASSWORD":
                if(passwordChangeView.getStrength() > 0){
                    //passwordChangeView.getPassword();
                    //TODO update password
                    System.out.println("New password: " + passwordChangeView.getPassword());
                }
                break;
            case "ADD MONEY":
                if(addMoneyView.getAmount() == 0){
                    addMoneyView.showError();
                }else{
                    //TODO add money
                    addMoneyView.noError();
                }

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
        BJController.newBJCard(cartaResposta,this);
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

    public void setSettings(Settings settings) {
        this.settings = settings;
        this.passwordChangeView = settings.getPasswordChangeView();
        this.walletEvolutionView = settings.getWalletEvolutionView();
        this.addMoneyView = settings.getAddMoneyView();
        this.settingsView = settings.getSettingsView();
    }
    public void showGamesView() {
        logInView.clearFields();
        finestra.setGameSelector(user.isGuest());
    }

    public void initBlackJack() {
        //crea el controlador de la nova partida amb un nou model
        BJController = new BlackJackController(blackJackView,networkManager,new Model_BJ());
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
            BJController.updateSizeBJ();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if(BJController != null)
            BJController.updateSizeBJ();
    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch( ((JComponent)e.getSource()).getName()){
            case "PASSWORD FIELD CHANGE - NEW PASSWORD":
                checkPassword();
                break;
            case "PASSWORD FIELD CHANGE - CONFIRM PASSWORD":
                if(!passwordChangeView.getPasswordChangeRequest()){
                    passwordChangeView.passwordKO("Passwords must match");
                }else{
                    checkPassword();
                }
                break;
        }
    }

    /** Mostrem si el canvi de contrasenya s'ha pogut efectuar correctament*/
    public boolean checkPassword(){
        String newPassword = passwordChangeView.getPassword();
        //PasswordPing passwordping = new PasswordPing("", "");//YOUR_API_KEY, YOUR_API_SECRET);
        int strength = 0;
       // try {
            if(newPassword.length() >= 8){

                if(isValidUTF(newPassword)){
                    strength = containsRequiredChars(newPassword);
                    if(strength > 0) {
                        if (true/*passwordping.CheckPassword(newPassword)*/) {
                            strength += 1;
                            passwordChangeView.passwordOK(strength);
                        }
                        return true;
                    }else{
                        passwordChangeView.passwordKO("Must contain at least a number, lower case and upper case character");
                        return false;
                    }

                }else{
                    passwordChangeView.passwordKO("Must use UTF-8 characters");
                    return false;
                }


            }else{
                passwordChangeView.passwordKO("Password must contain at least 8 characters");
                return false;
            }
        /*}catch (IOException e){
            passwordChangeView.passwordKO("Password Error");
            return false;
        }*/
    }


    private boolean isValidUTF(String string){
        CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();

        try {
            cs.decode(ByteBuffer.wrap(string.getBytes()));
            return true;
        }
        catch(CharacterCodingException e){
            return false;
        }
    }

    private int containsRequiredChars (String string){
        int relativeStrength = 0;

        int upperCase = 0;
        int lowerCase = 0;
        int number = 0;
        char[] input = string.toCharArray();

        for(int i = 0; i < input.length; i++){
            if(Character.isUpperCase(input[i])){
                upperCase++;
            }else if(Character.isLowerCase(input[i])){
                lowerCase++;

            }else if(Character.isDigit(input[i])){
                number++;
            }
        }
        if(number < 0 || upperCase < 0 || lowerCase < 0 ){
            return -1;
        }else{
            relativeStrength = (int)(((string.length() - number/(float)string.length() - lowerCase/(float)string.length() - upperCase/(float)string.length())* 10)/(float) string.length());
            //TODO calculate strength
            return relativeStrength;
        }


    }


}
