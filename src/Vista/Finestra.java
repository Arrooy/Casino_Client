package Vista;

import Controlador.Controller;
import Controlador.Game_Controlers.BlackJackController;
import Model.AssetManager;
import Model.User;
import Network.Transmission;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.SettingsViews.Settings;
import Vista.SettingsViews.SettingsView;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame {

    private CardLayout layout;
    private MainViewClient mainView;
    private LogInView logInView;
    private GameSelectorView gameSelectorView;
    private SignInView signInView;
    private Settings settings;
    private BlackJackView blackJackView;

    public Finestra() {

        Tray.init();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(640, 480);

        //Try to fullScreen
        setExtendedState(MAXIMIZED_BOTH);
        checkFullScreen();

        setMinimumSize(new Dimension(BlackJackView.MIN_SCREEN_WIDTH, BlackJackView.MIN_SCREEN_HEIGHT));
        System.out.println("[FRAME]: "+ getMinimumSize().width + " - " + getMinimumSize().height);

        requestFocus();

        setIconImage(AssetManager.getImage("ico.png"));

        layout = new CardLayout();
        getContentPane().setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();
        gameSelectorView = new GameSelectorView();
        signInView = new SignInView();
        settings = new Settings();
        blackJackView = new BlackJackView();

        add("main", mainView);
        add("logIn", logInView);
        add("gameSelectorView", gameSelectorView);
        add("signIn", signInView);
        add("settings", settings);
        add("blackJack", blackJackView);
    }

    /*
     * Verifica que el dispositiu ha pogut fer la full screen.
     * La documentacio de JFrame indica que pot no ferse full screen alguns cops
     */
    private void checkFullScreen() {
        if(getExtendedState() != MAXIMIZED_BOTH) {
            System.out.println("Full screen no esta disponible, tirant de minimitzada.");
            setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
        }
    }

    public void addController(Controller c) {
        Tray.addController(c);

        mainView.addController(c);
        logInView.addController(c);
        gameSelectorView.addController(c);
        signInView.addController(c);
        settings.addController(c);


        c.setMainView(mainView);
        c.setLogInView(logInView);
        c.setSignInView(signInView);
        c.setSettings(settings);
        c.setGameSelectorView(gameSelectorView);
        c.setBlackJackView(blackJackView);


        addWindowListener(c);
        addComponentListener(c);
        addMouseListener(c);
    }

    public void setMainView() {
        layout.show(getContentPane(), "main");
    }

    public void setLogInView() {
        layout.show(getContentPane(), "logIn");
    }

    public void setGameSelector(boolean guest) {
        layout.show(getContentPane(), "gameSelectorView");
        gameSelectorView.enableButtons(guest);
    }

    public void setSignInView() {
        layout.show(getContentPane(), "signIn");
    }

    public User getSignUpUser() {
        return new User(signInView.getUsername(), signInView.getPassword(), signInView.getMail(), Transmission.CONTEXT_SIGNUP);
    }

    public void setBlackJackView() {
        layout.show(getContentPane(), "blackJack");
    }

    public LogInView getLogInView() {
        return logInView;
    }

    public SignInView getSignInView() {
        return signInView;
    }

    /** Escollim la vista dels settings*/
    public void setSettingsView(String s) {
        layout.show(getContentPane(), "settings");
        settings.showSetting(s);
    }

}
