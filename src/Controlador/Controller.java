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
                addMoneyView.noErrorPassword();
                addMoneyView.noErrorMoney();
                break;
            case"SETTINGS - walletEvolution":
                finestra.setSettingsView(e.getActionCommand());
                break;
            case "PASSWORD CHANGE  - CONFIRM PASSWORD":
                //setNewPassword(passwordChangeView.getNewPassword());
                //passwordChangeView.getPassword();
                //TODO update password


                break;
            case "ADD MONEY":
                long money = addMoneyView.getAmount();
                String password = addMoneyView.getPassword();
                //TODO ARREGLAR MERI
                //networkManager.getWallet(); //get wallet hauria de rebre els diners que te l'usuari

                boolean tOK = true;
                if(false){ //FALTA MIRAR SI L'USUARI TÉ MASSA DINERS (+100.000)
                    addMoneyView.showErrorMoney();
                    tOK = false;
                }else{
                    addMoneyView.noErrorMoney();
                }
                if(password.equals(user.getPassword())){
                    addMoneyView.noErrorPassword();
                } else {
                    addMoneyView.showErrorPassword();
                    tOK = false;
                }
                if(tOK){
                    //TODO add money MERI
                    networkManager.doTransaction(money);
                }
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
        int strength = 0;
        switch( ((JComponent)e.getSource()).getName()){
            case "PASSWORD FIELD CHANGE - NEW PASSWORD":
                strength = checkPassword(passwordChangeView.getNewPassword());
                if(passwordChangeView.getPasswordChangeRequest() && strength > 0){
                    passwordChangeView.canConfirm(true);
                }else{
                   // passwordChangeView.passwordKO("Passwords must match");
                }
                if(strength <= 0){
                    passwordChangeView.canConfirm(false);
                    passwordChangeView.setStrength(-strength);
                    passwordChangeView.manageError(true);
                }else{
                    passwordChangeView.setStrength(strength);
                    passwordChangeView.manageError(false);
                }

                break;
            case "PASSWORD FIELD CHANGE - CONFIRM PASSWORD":
                if(passwordChangeView.getPasswordChangeRequest() && checkPassword(passwordChangeView.getNewPassword()) > 0){
                    passwordChangeView.canConfirm(true);
                    passwordChangeView.manageError(false);
                }else{
                    passwordChangeView.canConfirm(false);
                    passwordChangeView.manageError(true);
                    passwordChangeView.passwordKO("Passwords must match");
                }
                break;
        }
    }

    /** Mostrem si el canvi de contrasenya s'ha pogut efectuar correctament
     * Retorn: < 0 : Menys de 8 caracters, retorn*-1 = strength
     *       : = 0 : Altre error
     *       : > 0 : No hi ha error, retorn = strength
     *
     * Tambe s'actualitza el missatge d'error si n'hi ha */
    public int checkPassword(String newPassword){
        int strength;
        try {
            if(newPassword.length() >= 8){
                strength = containsRequiredChars(newPassword);
                if(isValidUTF(newPassword)){
                    return strength;
                }else{
                    passwordChangeView.passwordKO("Must contain valid UTF-8 characters");
                    return 0;
                }
            }else{
                passwordChangeView.passwordKO("Must be at least 8 charcaters");
                return -(int)(newPassword.length()*(float)1.75);
            }
        }catch (Exception e) {
            passwordChangeView.passwordKO(e.getMessage());
            return 0;
        }
    }

    /** Comprovem que no hi hagi ningun caracter non UTF-8*/
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
    /** Mirem que solida es la contrasenya, en cas de que no s'hagin introduit un mínim d0un nombre, una Majuscula i una minuscula el llença
     * una excepcio amb el text d'error que cal mostrar*/
    private int containsRequiredChars (String string) throws Exception{
        int relativeStrength = 0;
        int length = string.length();
        int upperCase = 0;
        int lowerCase = 0;
        int specialChars = 0;
        int number = 0;
        char[] input = string.toCharArray();

        for(int i = 0; i < length; i++){
            if(Character.isUpperCase(input[i])){
                upperCase++;
            }else if(Character.isLowerCase(input[i])){
                lowerCase++;

            }else if(Character.isDigit(input[i])){
                number++;
            }else{
                specialChars++;
            }
        }
        if(number <= 0 || upperCase <= 0 || lowerCase <= 0 ){
            throw new Exception("Must contain at least: number, Upper and Lower case character.");
        }else{
            relativeStrength = (int)(((length - number)*(length - upperCase)*(length-lowerCase)*(length-specialChars))/(float)(length*length*length)*10);
            return relativeStrength;
        }


    }



}
