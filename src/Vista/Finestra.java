package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame {

    private CardLayout layout;
    private MainViewClient mainView;
    private LogInView logInView;
    private GameSelector gameSelector;
    private SignInView signInView;
    private SettingsView settingsView;

    public Finestra() {

        Tray.init();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(640, 480);
        //Try to fullScreen
        setExtendedState(MAXIMIZED_BOTH);
        checkFullScreen();

        layout = new CardLayout();
        getContentPane().setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();
        gameSelector = new GameSelector();
        signInView = new SignInView();
        settingsView = new SettingsView();

        add("main", mainView);
        add("logIn", logInView);
        add("gameSelector", gameSelector);
        add("signIn", signInView);
        add("settings", settingsView);
        //add("blackJack", );
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
        gameSelector.addController(c);
        signInView.addController(c);
        settingsView.addController(c);

        c.setMainView(mainView);
        c.setLogInView(logInView);
        c.setSignInView(signInView);
        c.setSettingsView(settingsView);
        c.setGameSelector(gameSelector);

        addWindowListener(c);
    }

    public void setMainView() {
        layout.show(getContentPane(), "main");
    }

    public void setLogInView() {
        layout.show(getContentPane(), "logIn");
    }

    public void setGameSelector() {
        layout.show(getContentPane(), "gameSelector");
    }

    public void setSignInView() {
        layout.show(getContentPane(), "signIn");
    }

    public void setSettingsView() {
        layout.show(getContentPane(), "settings");
    }

    public void setBlackJackView() {
        layout.show(getContentPane(), "blackJack");
    }
}
