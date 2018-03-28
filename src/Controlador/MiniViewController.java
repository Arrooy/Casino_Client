package Controlador;

import Vista.MiniView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MiniViewController implements ActionListener,WindowListener {

    private Controller controller;
    private MiniView view;

    public MiniViewController(Controller controller, MiniView miniView) {
        this.controller = controller;
        this.view = miniView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public void show() {
        view.setVisible(true);
    }

    public void hide() {
        view.setVisible(false);
    }
}
