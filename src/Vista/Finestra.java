package Vista;

import Controlador.Controller;
import Model.User;
import Network.Transmission;
import Vista.GameViews.BlackJack.BlackJackView;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame {

    private CardLayout layout;
    private MainViewClient mainView;
    private LogInView logInView;
    private GameSelectorView gameSelectorView;
    private SignInView signInView;
    private SettingsView settingsView;
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

        layout = new CardLayout();
        getContentPane().setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();
        gameSelectorView = new GameSelectorView();
        signInView = new SignInView();
        settingsView = new SettingsView();
        blackJackView = new BlackJackView();

        add("main", mainView);
        add("logIn", logInView);
        add("gameSelectorView", gameSelectorView);
        add("signIn", signInView);
        add("settings", settingsView);
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
        settingsView.addController(c);
        blackJackView.addController(c);

        c.setMainView(mainView);
        c.setLogInView(logInView);
        c.setSignInView(signInView);
        c.setSettingsView(settingsView);
        c.setGameSelectorView(gameSelectorView);
        c.setBlackJackView(blackJackView);

        addWindowListener(c);
    }

    public void setMainView() {
        layout.show(getContentPane(), "main");
    }

    public void setLogInView() {
        layout.show(getContentPane(), "logIn");
    }

    public void setGameSelector() {
        layout.show(getContentPane(), "gameSelectorView");
    }

    public void setSignInView() {
        layout.show(getContentPane(), "signIn");
    }

    public void setSettingsView() {
        layout.show(getContentPane(), "settings");
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
}
