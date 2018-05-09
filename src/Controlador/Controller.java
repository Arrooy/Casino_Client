package Controlador;

import Controlador.Game_Controlers.BlackJackController;
import Controlador.Game_Controlers.HorseRaceController;
import Model.*;
import Utils.JsonManager;
import Vista.*;
import Network.*;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.GameViews.HorseRaceView;
import Vista.MainFrame.Finestra;
import Vista.SettingsViews.*;
import Vista.SwingModifications.IconPasswordField;
import Vista.SwingModifications.IconTextField;

import javax.swing.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/** Controlador del client*/

public class Controller implements ActionListener, ComponentListener, KeyListener, FocusListener{

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
    private Top5View top5View;
    private BlackJackView blackJackView;

    private BlackJackController BJController;
    private HorseRaceView horseRaceView;
    private HorseRaceController horseRaceController;


    /** Usuari que controla el client*/
    private User user;

    /** Responsable de la connectivitat amb el servidor*/
    private NetworkManager networkManager;
    private DraggableWindow draggableWindow;

    /** Inicialitza un nou controlador i realitza les relacions amb la vista i el gestor de la connectivitat*/
    public Controller(Finestra finestra, NetworkManager networkManager,DraggableWindow draggableWindow, HorseRaceView horseRaceView) {
        this.networkManager = networkManager;
        this.finestra = finestra;
        this.draggableWindow = draggableWindow;
        this.horseRaceView = horseRaceView;
        this.horseRaceController = new HorseRaceController(this.horseRaceView, this.networkManager, this.finestra);

    }

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        mainView.displayError(title,errorText);
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
                //user.setUsername(logInView.getUsername());
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
                finestra.showUserconfig(false);
                break;
            case "horseRace":
                finestra.setHorseRaceView();
                networkManager.sendHorseRaceRequest();
                horseRaceController.play();
                finestra.showUserconfig(false);
                System.out.println("Play");
                break;
            case "blackJack":
                System.out.println("INIT BLACKJACK REQUESTED");
                networkManager.initBlackJack(Baralla.getNomCartes(),manageBJBet());
                finestra.showUserconfig(false);
                break;
            case "goSignIn":
                finestra.setSignInView();
                break;
            case "acceptSignIn":
                if(signInView.getUsername().length() > 0 && signInView.getEmail().length() > 0){
                    signUp();
                }else{
                    signInView.passwordKO("Must fill in all fields");
                    signInView.manageError(true);
                }

                break;
            case "settings":
                if(user != null && !user.isGuest())
                    finestra.setSettingsView("NOTHING");
                break;
            case "SETTINGS - backFromSettings":
                finestra.setGameSelector(user.isGuest());
                break;
            case"SETTINGS - changePass":
                passwordChangeView.clearFields();
                finestra.setSettingsView(e.getActionCommand());
                break;
            case"SETTINGS - addMoneyButton":
                addMoneyView.clearView();
                finestra.setSettingsView(e.getActionCommand());
                addMoneyView.noErrorPassword();
                addMoneyView.noErrorMoney();
                addMoneyView.noTransactionOK();
                break;
            case "SETTINGS - WALLETEVOLUTION":
                System.out.println("REQUESTING WALLET EVOLUTION");
                networkManager.getWalletEvolution();
                finestra.setSettingsView(e.getActionCommand());
                break;
            case "PASSWORD CHANGE  - CONFIRM PASSWORD":
                User userPass = new User(user.getUsername(),passwordChangeView.getNewPassword(),Transmission.CONTEXT_CHANGE_PASSWORD);
                new Transmission(userPass, networkManager);
                break;
            case "ADD MONEY":
                addMoney();
                break;
        }
    }

    public void manageChangePass(boolean approved){
        if(approved){
            passwordChangeView.clearFields();
            JOptionPane.showMessageDialog(passwordChangeView, "Password Changed correctly","Password Change",  JOptionPane.INFORMATION_MESSAGE);
        }else {
            passwordChangeView.clearFields();
            passwordChangeView.passwordKO("El servidor no ha acceptat la contrasenya");
        }
    }

    public long manageBJBet() {
        long value = -1;

        do {
            String res = blackJackView.showInputDialog();
            try {
                value = Long.parseLong(res);
            } catch (NumberFormatException error) {
               if(res == null){
                   value = 0;
               }else {
                   blackJackView.showDialog("Wallet Error","Only numbers were expected");
               }
            }
        }while(value == -1);
        return value;
    }

    public void showFinestra() {
        finestra.setVisible(true);
        finestra.requestFocus();
    }

    private void addMoney() {
        long deposit = addMoneyView.getAmount();
        String password = addMoneyView.getPassword();

        if(deposit == -1){
            addMoneyView.showErrorMoney("Choose an amount       ");
        }else{
            addMoneyView.noErrorMoney();
            Transaction transaction = new Transaction("deposit", user.getUsername(), deposit,0);
            transaction.setPassword(password);
            new Transmission(transaction, networkManager);
        }
    }

    public void transactionOK(int type){
        if (type == 0) {
            addMoneyView.showAddOK();
            addMoneyView.noErrorMoney();
        } else if(type == 1) {
            addMoneyView.noErrorMoney();
            addMoneyView.noTransactionOK();
            addMoneyView.showErrorMoney("Above the limit amount");
        }else{
            addMoneyView.noErrorMoney();
            addMoneyView.noTransactionOK();
            addMoneyView.showErrorPassword();
        }
    }

    public void setUser(User u){
        user = u;
        horseRaceController.setUser(u);
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
        this.top5View = settings.getWalletEvolutionView();
        this.addMoneyView = settings.getAddMoneyView();
        this.settingsView = settings.getSettingsView();
    }
    public void showGamesView() {
        logInView.clearFields();
        signInView.clearFields();
        finestra.setGameSelector(user.isGuest());
    }

    public void initBlackJack() {


        //crea el controlador de la nova partida amb un nou model
        if(BJController == null)
            BJController = new BlackJackController(blackJackView,networkManager);
        else
            BJController.initGame();

        finestra.setBlackJackView();
    }

    public void setBlackJackView(BlackJackView blackJackView) {
        this.blackJackView = blackJackView;
    }


    public void setHorseRaceView(HorseRaceView horseRaceView){
        this.horseRaceView = horseRaceView;
    }

    public void initHorses (){
        System.out.println("INIT HORSES REQUESTED");
        finestra.setHorseRaceView();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if(BJController != null) BJController.updateSizeBJ();
        if(horseRaceController != null) horseRaceController.updateSize();

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if(BJController != null)
            BJController.updateSizeBJ();

    }

    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int strength = 0;
        switch( ((JComponent)e.getSource()).getName()){
            case "PASSWORD FIELD CHANGE - NEW PASSWORD":
                strength = checkPassword(passwordChangeView.getNewPassword(), passwordChangeView);
                if(passwordChangeView.getPasswordChangeRequest() && strength > 0){
                    passwordChangeView.canConfirm(true);
                }else{
                    passwordChangeView.canConfirm(false);
                    if (passwordChangeView.getNewPassword().equals(user.getPassword())){
                        passwordChangeView.passwordKO("New and Old password must differ;");
                    }else{
                         passwordChangeView.passwordKO("");
                    }

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
                System.out.println("New: " + passwordChangeView.getNewPassword()+ ", equal: "+passwordChangeView.getPasswordChangeRequest() + ", check: " + checkPassword(passwordChangeView.getNewPassword(), passwordChangeView)) ;
                if(passwordChangeView.getPasswordChangeRequest() && checkPassword(passwordChangeView.getNewPassword(), passwordChangeView) > 0){
                    passwordChangeView.canConfirm(true);
                    passwordChangeView.manageError(false);
                }else {
                    if(checkPassword(passwordChangeView.getNewPassword(), passwordChangeView) == 0){
                        passwordChangeView.canConfirm(false);
                        passwordChangeView.manageError(true);
                        if (passwordChangeView.getNewPassword().equals(user.getPassword())) {
                            passwordChangeView.passwordKO("New and Old password must differ;");
                        }
                    }else if (checkPassword(passwordChangeView.getNewPassword(), passwordChangeView) < 0){
                            passwordChangeView.canConfirm(false);
                            passwordChangeView.manageError(true);

                    }
                }
                break;
            case "SIGNIN - PASSWORD FIELD":
                //TODO Errada: al escriure le contrasenya surt com un jpsswordField on el progressbar, AJUDA
                strength = checkPassword(signInView.getNewPassword(), signInView);
                if(signInView.getPasswordChangeRequest() && strength > 0){
                    signInView.canConfirm(true);
                }else {
                    if (strength <= 0) {
                        signInView.canConfirm(false);
                        signInView.setStrength(-strength);
                        signInView.manageError(true);
                    } else {
                        signInView.setStrength(strength);
                        signInView.manageError(false);
                    }
                }
                break;
            case"SIGNIN - PASSWORD CONFIRM FIELD":
                if(signInView.getPasswordChangeRequest() && checkPassword(signInView.getNewPassword(), signInView) > 0){
                    signInView.canConfirm(true);
                    signInView.manageError(false);
                }else {
                    strength = checkPassword(signInView.getNewPassword(), signInView);
                    if (strength <= 0) {
                        signInView.canConfirm(false);
                        signInView.manageError(true);
                    }else{
                        if(!signInView.getPasswordChangeRequest() ){
                            signInView.canConfirm(false);
                            signInView.passwordKO("Passwords must match");
                            signInView.manageError(true);
                        }
                    }
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
    public int checkPassword(String newPassword, PasswordConfirm passwordConfirm){
        int strength;
        try {
            if(newPassword.length() >= 8){
                strength = containsRequiredChars(newPassword);
                if(isValidUTF(newPassword)){
                   if(passwordConfirm instanceof PasswordChangeView){
                       if(!passwordConfirm.getNewPassword().equals(user.getPassword())){
                           return strength;
                       }else{
                           return 0;
                       }
                   }else{
                       return strength;
                   }

                }else{
                    passwordConfirm.passwordKO("Must contain valid UTF-8 characters");
                    return 0;
                }
            }else{
                passwordConfirm.passwordKO("Must be at least 8 charcaters");
                return -(int)(newPassword.length()*(float)1.75);
            }
        }catch (Exception e) {
            passwordConfirm.passwordKO(e.getMessage());
            return 0;
        }
    }

    /** Comprovem que no hi hagi ningun caracter non UTF-8*/
    private boolean isValidUTF(String string){
        CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();

        try {
            cs.decode(ByteBuffer.wrap(string.getBytes()));

            for(char c: string.toCharArray()){
                if(Character.isSpaceChar(c)){
                    return false;
                }else if(!Character.isDefined(c)){
                    return false;
                }
            }
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
        if(number <= 0 || upperCase <= 0 || lowerCase <= 0 || (upperCase + lowerCase < 6)){
            throw new Exception("Required: 1 number, 6 characters, 1 upper and lower case.");
        }else{
            relativeStrength = (int)((((length - number)*(length - upperCase)*(length-lowerCase)*(length-specialChars))/(float)(length*length*length))*10);
            return relativeStrength;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource() instanceof IconTextField){
            ((IconTextField)e.getSource()).setHint(false);
        }else if(e.getSource() instanceof IconPasswordField){
            ((IconPasswordField)e.getSource()).setHint(false);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(e.getSource() instanceof IconTextField){
            if(((IconTextField)e.getSource()).getText().equals(""))
                ((IconTextField)e.getSource()).setHint(true);
        }else if(e.getSource() instanceof IconPasswordField){
            if(((IconPasswordField)e.getSource()).getPassword().length == 0)
                ((IconPasswordField)e.getSource()).setHint(true);
        }
    }

    public void updateWalletEvolution(WalletEvolutionMessage newWallet) {
        finestra.updateWallet(newWallet);
    }

    public void exit() {
        HorseRaceController.exit();
        if(BJController != null)
            BJController.exitInGame();
    }
}
