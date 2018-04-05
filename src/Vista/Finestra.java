package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame {

    private CardLayout layout;
    private MainViewClient mainView;
    private LogInView logInView;
    private GamesView gamesView;
    private SignInView signInView;
    private SettingsView settingsView;

    public Finestra() {
        //TODO: Configurar accions byDefault de manera correcte
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 720);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //FULL SCREEN

        layout = new CardLayout();
        getContentPane().setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();
        gamesView = new GamesView();
        signInView = new SignInView();
        settingsView = new SettingsView();

        add("main", mainView);
        add("logIn", logInView);
        add("games", gamesView);
        add("signIn", signInView);
        add("settings", settingsView);
    }

    public void addController(Controller c) {
        mainView.addController(c);
        logInView.addController(c);
        //TODO: controler del games view

        c.setMainView(mainView);
        c.setLogInView(logInView);
    }

    public void setMainView() {
        layout.show(getContentPane(), "main");
    }

    public void setLogInView() {
        layout.show(getContentPane(), "logIn");
    }

    public void setGamesView() {
        layout.show(getContentPane(), "games");
    }

    public void setSignInView() {
        layout.show(getContentPane(), "signIn");
    }

    public void setSettingsView() {
        layout.show(getContentPane(), "settings");
    }
}
