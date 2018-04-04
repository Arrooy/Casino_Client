package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class Finestra extends JFrame {

    private CardLayout layout;
    private MainViewClient mainView;
    private LogInView logInView;

    public Finestra() {
        //TODO: Configurar accions byDefault de manera correcte
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 720);

        layout = new CardLayout();
        getContentPane().setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();

        add("main", mainView);
        add("logIn", logInView);
    }

    public void addController(Controller c) {
        mainView.addController(c);
        logInView.addController(c);

        c.setMainView(mainView);
        c.setLogInView(logInView);
    }

    public void setMainView() {
        layout.show(getContentPane(), "main");
    }

    public void setLogInView() {
        layout.show(getContentPane(), "logIn");
    }
}
