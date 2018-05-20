package Controlador;

import Controlador.Game_Controlers.BlackJackController;
import Controlador.Game_Controlers.HorseRaceController;
import Controlador.Game_Controlers.RouletteController;
import Model.*;
import Utils.Baralla;
import Utils.JsonManager;
import Utils.Sounds;
import Vista.*;
import Network.*;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.GameViews.Roulette.RouletteView;
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

/**
 * Controlador principal del client que gestiona totes les interaccions amb la finestra
 * principal, i els elements de swing usats en les diferents vistes, ja que en les
 * finestres gràfiques s'han dedicat controladors personalitzats degut a que el seu control és
 * molt més concret i complex.
 */
public class Controller implements ActionListener, ComponentListener, KeyListener, FocusListener{

    /** Dimensions de la finestra en tot moment */
    private static int windowWidth, windowHeight;

    /** Finestra grafica del client*/
    private Finestra finestra;

    /** Conjunt de Vistes que conformen tot el programa del Client */

    /**Vista principal que conte els botons per escollir com es vol entrar al joc,
     * iniciant sessio, creant un nou usuari o com a convidat*/
    private MainViewClient mainView;

    /**Vista que conte els diferents camps necessaris per iniciar sessio*/
    private LogInView logInView;

    /**Vista que conte els diferents camps necessaris per crear un nou usuari*/
    private SignInView signInView;

    /**Vista que conte els botons per escollir a quin joc es vol jugar, a mes a mes tambe te el boto per accedir a settings*/
    private GameSelectorView gameSelectorView;

    /**Vista que conte els camps necessaris per afegir diners al compte*/
    private AddMoneyView addMoneyView;

    /**Vista que conte els camps necessaris per canviar de contrasenya*/
    private PasswordChangeView passwordChangeView;

    /**Vista del joc del Black Jack*/
    private BlackJackView blackJackView;

    /**Vista del joc de la ruleta*/
    private RouletteView rouletteView;

    /**Encarregat de gestionar els grafics del joc de la ruleta*/
    private GraphicsManager rouletteGraphicsManager;

    /**Encarregat de gestionar i controlar el joc del Black Jack*/
    private BlackJackController BJController;

    /**Vista del joc dels cavalls*/
    private HorseRaceView horseRaceView;

    /**Encarregat de gestionar i controlar el joc dels cavalls*/
    private HorseRaceController horseRaceController;

    /**Encarregat de gestionar i controlar el joc de la ruleta*/
    private RouletteController rouletteController;

    /** Usuari que controla el client*/
    private User user;

    /** Responsable de la connectivitat amb el servidor*/
    private NetworkManager networkManager;

    /** Gestor del JFrame. S'utilitza en aquesta classe per a sortir del casino*/
    private DraggableWindow draggableWindow;

    /** Inicialitza un nou controlador i realitza les relacions amb la vista i el gestor de la connectivitat*/
    public Controller(Finestra finestra, NetworkManager networkManager,DraggableWindow draggableWindow, HorseRaceView horseRaceView) {
        this.networkManager = networkManager;
        this.finestra = finestra;
        this.draggableWindow = draggableWindow;
        this.horseRaceView = horseRaceView;
        this.horseRaceController = new HorseRaceController(this.horseRaceView, this.networkManager, this.finestra);
    }

    /**
     * Mètode que s'executa amb cada interacció de l'usuari amb qualsevol element
     * de Swing del programa, fora de qualsevol joc.
     * @param e Event generat per l'acció
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
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
                setRoulette();
                break;
            case "horseRace":
                // Es vol començar a jugar als cavalls
                finestra.setHorseRaceView();
                networkManager.sendHorseRaceRequest();
                horseRaceController.play();
                finestra.showUserconfig(false);
                break;

            case "blackJack":
                if (user.isGuest()){
                    //Si la partida s'inicia correctament perque l'aposta es adient, s'amaga l'icono de settings de la
                    //barra superior del Jframe, de lo contrari, l'icono es conserva
                    networkManager.initBlackJack(Baralla.getNomCartes(), manageBJBet());
                    finestra.showUserconfig(false);
                }else {
                    finestra.showUserconfig(networkManager.initBlackJack(Baralla.getNomCartes(), manageBJBet()));
                }
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
                    finestra.setSettingsView("SETTINGS - changePass");
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

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        mainView.displayError(title,errorText);
    }

    /** Retorna l'estat de la JCheckBox RememberLogIn inidcant doncs si s'ha de guardar localment el login del usuari*/
    public boolean rememberLogIn(){
        return logInView.getRememberLogIn();
    }

    /**
     * Permet mostrar feedback sobre si s'ha canviat la contrasenya correctament
     * @param approved boolea que indica si s'ha canviat la contrasenya correctament
     */
    public void manageChangePass(boolean approved){
        if(approved){
            passwordChangeView.clearFields();
            JOptionPane.showMessageDialog(passwordChangeView, "Password Changed correctly","Password Change",  JOptionPane.INFORMATION_MESSAGE);
        }else {
            passwordChangeView.clearFields();
            passwordChangeView.passwordKO("El servidor no ha acceptat la contrasenya");
        }
    }

    /**
     * Gestiona el dialeg d'aposta per al BlackJack. Demana una aposta al usuari i si el format de l'aposta no es correcte,
     * la torna a demanar
     * @return el valor de l'aposta definitiu. Si el valor es 0 indica que l'aposta s'ha volgut cancelar
     */
    public long manageBJBet() {
        long value = -1;

        do {
            String res = blackJackView.showInputDialog();
            //Si la resposta te un caracter a, significa que l'usuari vol apostar
            if(res.charAt(0) == 'a'){
                res = res.substring(1);
                try {
                    value = Long.parseLong(res);
                } catch (NumberFormatException error) {
                    blackJackView.showDialog("Bet error","Only numbers are expected as a bet");
                }
            }else{
                //Es torna al gameMenu
                value = 0;
            }


        }while(value == -1);
        return value;
    }

    /**
     * Mostra la finestra despres de apareixer la loadingScreen
     */
    public void showFinestra() {
        finestra.setVisible(true);
        finestra.requestFocus();
    }

    /**
     * Gestiona els errors de l'opcio d'addMoney del menu de settings. Si no existeix cap error, envia la
     * solicitud d'afegir diners al servidor
     */
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

    /**
     * Gestiona els errors que el servidor ha detectat al intentar ingresar diners
     * @param type motiu de l'error.
     *             type == 0 si no hi ha error
     *             type == 1 si s'ha demanat masses diners
     */
    public void transactionOK(int type){
        if (type == 0) {
            addMoneyView.showAddOK();
            addMoneyView.noErrorMoney();
            addMoneyView.clearFields();
        } else if(type == 1) {
            addMoneyView.noErrorPassword();
            addMoneyView.noTransactionOK();
            addMoneyView.showErrorMoney("Above the limit amount");
        }else{
            addMoneyView.noErrorMoney();
            addMoneyView.noTransactionOK();
            addMoneyView.showErrorPassword();
        }
    }

    /**
     * Defineix l'usuari que esta autentificat amb el servidor
     * @param u usuari que s'ha autentificat
     */
    public void setUser(User u){
        user = u;
        horseRaceController.setUser(user);
    }

    /** Redirecciona la carta rebuda pel servidor al BlackJack controller*/
    public void newBJCard(Card cartaResposta) {
        BJController.newBJCard(cartaResposta,this);
    }

    /** Crea una solicitud de signIn al servidor*/
    private void signUp() {
        networkManager.requestSignUp(new User(signInView.getUsername(), signInView.getPassword(), signInView.getMail(), Transmission.CONTEXT_SIGNUP));
    }

    public void showErrorLogIn(String s) {
        logInView.setError(s);
    }

    /**
     * Es configuren les vistes dels settings.
     * @param settings
     */
    public void setSettings(Settings settings) {
        this.passwordChangeView = settings.getPasswordChangeView();
        this.addMoneyView = settings.getAddMoneyView();
    }

    /**
     * Mètode que fa que l'usuari accedeixi a la vista de selecció
     * de joc. Automàticament nateja tots els camps de les vistes de SignIn i LogIn
     * i atura tots els sons actius
     */
    public void showGamesView() {//TODO: aqui s'ha fet una lambda automatica, qui sap si peta
        SwingUtilities.invokeLater(() -> {
            logInView.clearFields();
            signInView.clearFields();
            finestra.setGameSelector(user.isGuest());
            gameSelectorView.updateUI();
            SwingUtilities.updateComponentTreeUI(finestra);
            Sounds.stopAllAudio();
            Sounds.songNoEnd("wii.wav");
        });
    }
//TODO
    public void initBlackJack() {
        //crea el controlador de la nova partida amb un nou model
        if(BJController == null)
            BJController = new BlackJackController(blackJackView,networkManager);
        else
            BJController.initGame();

        finestra.setBlackJackView();
    }

    /**
     * Mètode que s'executa quan es varia el tamany de la finestra.
     * S'utilitza per notificar a alguns panells gràfics el nou tamany de la finestra
     */
    @Override
    public void componentResized(ComponentEvent e) {
        if(BJController != null) BJController.updateSizeBJ();
        if(horseRaceController != null) horseRaceController.updateSize();

        if(BJController != null) BJController.updateSizeBJ();
        windowWidth = finestra.getWidth();
        windowHeight = finestra.getHeight();
    }

    /**
     * En cas de fer pantalla completa o alguna acció que varii el tamany de la finestra
     * sense necessitat de fer un resize de la finestra manual, s'executa aquest codi que
     * notifica del canvi de tamany
     */
    @Override
    public void componentMoved(ComponentEvent e) {
        if(BJController != null)
            BJController.updateSizeBJ();

        if(BJController != null) BJController.updateSizeBJ();
        windowWidth = finestra.getWidth();
        windowHeight = finestra.getHeight();
    }

    /**
     * Metodes genèrics per a obtenir amb facilitat les dimensions de la finestra
     * @return Dimensions de la finestra
     */
    public static int getWinWidth() {
        return windowWidth;
    }

    /**
     * Metodes genèrics per a obtenir amb facilitat les dimensions de la finestra
     * @return Dimensions de la finestra
     */
    public static int getWinHeight() {
        return windowHeight - Finestra.getTopBarHeight();
    }

    /**
     * Mètode que s'executa al deixar de prèmer una tecla estant sobre un JTextField i similars.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int strength = 0;
        switch (((JComponent)e.getSource()).getName()) {
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
                strength = checkPassword(signInView.getNewPassword(), signInView);
                if(signInView.getPasswordChangeRequest() && strength > 0){
                    signInView.canConfirm(true);
                }else {
                    if (strength <= 0) {
                        signInView.setStrength(-strength);
                        signInView.canConfirm(false);
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

    /**
     * Mostrem si el canvi de contrasenya s'ha pogut efectuar correctament
     * @param newPassword contrsenya que es vol introduir
     * @param passwordConfirm contrasenya atniga
     * @return Enter que permet detectar el tipus d'error
     */
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

    /**
     * Comprovem que no hi hagi ningun caracter non UTF-8
     * @param string contrasenya
     * @return si es UTF encoded
     */
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

    /**
     * Mirem que solida es la contrasenya, en cas de que no s'hagin introduit
     * un mínim d0un nombre, una Majuscula i una minuscula el llença
     * una excepcio amb el text d'error que cal mostrar
     * @param string contrasenya a comprovar
     * @return  entter que indica que forta es la contrasenya
     *
     */
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

    /**
     * Mètode que inicia el joc de la Roulette. En cas necessari genera el controlador,
     * i si no, simplement el reinicia per a realitzar correctament l'inicialització del joc.
     */
    private void setRoulette() {
        if (rouletteController == null) {
            rouletteController = new RouletteController(rouletteView, networkManager);
            rouletteController.initRoulette();
        } else rouletteController.initRoulette();

        rouletteGraphicsManager = new GraphicsManager(rouletteView, rouletteController);
        finestra.requestRouletteFocus();

        networkManager.initRoulette(rouletteController);
        finestra.setRouletteView();
    }

    /**
     * Mètode que tanca els gràfics de la ruleta de manera segura
     */
    public void endRouletteGraphics() {
        if (rouletteGraphicsManager != null) rouletteGraphicsManager.exit();
        networkManager.endRoulette();
    }

    /**
     * Mètode que carrega la informació del wallet evolution
     * a la JTable dels Settings de l'usuari
     * @param newWallet Informació a carregar
     */
    public void updateWalletEvolution(WalletEvolutionMessage newWallet) {
        finestra.updateWallet(newWallet);
    }

    /**
     * Mètode que expulsa del joc actual a l'usuari
     */
    public void exit() {
        HorseRaceController.exit();
        if(BJController != null)
            BJController.exitInGame();
    }

    //TODO: comentar
    public void showErrorConnection() {
        if(user != null){
            //Si l'usuari solicita logOut
            if(mainView.displayErrorConnection()){
                //S'inicia el logOut
                JsonManager.removeRemember();
                networkManager.requestLogOut();
                finestra.setMainView();
            }else {
                //De lo contrari, es surt del programa
                draggableWindow.exitProgram(1);
            }
        }
    }

    /**
     * Mostra un missatge d'error a al finestra del sign in
     * @param message missatge a mostrar
     */
    public void signUpErrorMessage(String message){
        signInView.passwordKO(message);
        signInView.manageError(true);
    }

    //TODO: Comentar
    @Override
    public void focusGained(FocusEvent e) {
        //En el moment que s'obtingui focus en una IconTextField o una IconPasswordField
        if(e.getSource() instanceof IconTextField){
            //S'elimina la hint
            ((IconTextField)e.getSource()).setHint(false);
        }else if(e.getSource() instanceof IconPasswordField){
            //S'elimina la hint
            ((IconPasswordField)e.getSource()).setHint(false);
        }
    }

    //TODO: Comentar
    @Override
    public void focusLost(FocusEvent e) {
        //En el moment que es perdi el focus en una IconTextField o una IconPasswordField
        if(e.getSource() instanceof IconTextField){
            //Si no s'ha introduit cap text, es torna a mostrar la hint
            if(((IconTextField)e.getSource()).getText().equals(""))
                ((IconTextField)e.getSource()).setHint(true);
        }else if(e.getSource() instanceof IconPasswordField){
            //Si no s'ha introduit cap text, es torna a mostrar la hint
            if(((IconPasswordField)e.getSource()).getPassword().length == 0)
                ((IconPasswordField)e.getSource()).setHint(true);
        }
    }

    //TODO: Comentar

    public void setMainView(MainViewClient mainView) {
        this.mainView = mainView;
    }
    public void setLogInView(LogInView logInView) {
        this.logInView = logInView;
    }
    public void setSignInView(SignInView signInView) {this.signInView = signInView;}
    public void setGameSelectorView(GameSelectorView gameSelectorView) {this.gameSelectorView = gameSelectorView;}
    public void setRouletteView(RouletteView rouletteView) {
        this.rouletteView = rouletteView;
    }
    public void setBlackJackView(BlackJackView blackJackView) {
        this.blackJackView = blackJackView;
    }

    @Override
    public void componentShown(ComponentEvent e) {}
    @Override
    public void componentHidden(ComponentEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}
}
