package Controlador;

import Network.NetworkManager;
import Vista.MainFrame.Finestra;
import Vista.Tray;

import java.awt.event.*;

import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;

public class DraggableWindow implements MouseMotionListener,WindowListener,ActionListener,MouseListener {

        private Finestra vista;
        private int fingerX,fingerY;
        private NetworkManager networkManager;

        public DraggableWindow(Finestra f, NetworkManager networkManager) {
            vista = f;
            this.networkManager = networkManager;
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            if (vista.getExtendedState() == MAXIMIZED_BOTH) {
                vista.setExtendedState(NORMAL);
                vista.goCenter();
                fingerX = vista.getWidth() / 2;
                fingerY = e.getY();
            }
            vista.setLocation(e.getXOnScreen() - fingerX, e.getYOnScreen() - fingerY);
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
                case "exitProgram":
                    exitProgram(0);
                    break;
                case "maximize":
                    toogleFullScreen();
                    break;
                case "iconify":
                    vista.setExtendedState(ICONIFIED);
                    break;
            }
        }

    private void toogleFullScreen() {
        if(vista.getExtendedState() == MAXIMIZED_BOTH) {
            vista.setExtendedState(NORMAL);
            vista.goCenter();
        }else{
            vista.setExtendedState(MAXIMIZED_BOTH);
        }
    }

    /** Metode per a tencar el client de forma segura.*/
    public void exitProgram(int status){
        networkManager.requestLogOut();

        Tray.exit();
        vista.dispose();
        System.exit(status);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() >= 2){
            toogleFullScreen();
        }
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
}
