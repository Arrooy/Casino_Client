package Controlador;

import Vista.Finestra;

import java.awt.event.*;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;

public class DraggableWindow implements MouseMotionListener,WindowListener,ActionListener {

        private Finestra vista;
        private int fingerX,fingerY;

        public DraggableWindow(Finestra f) {
            vista = f;

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(vista.getExtendedState() == MAXIMIZED_BOTH){
                vista.setExtendedState(NORMAL);
                vista.goCenter();
                fingerX = vista.getWidth() / 2;
                fingerY = e.getY();
            }
            vista.setLocation(e.getXOnScreen() - fingerX,e.getYOnScreen() - fingerY);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            fingerX = e.getX();
            fingerY = e.getY();
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

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()){

            }
        }
    }
