package Controlador;

import Network.NetworkManager;
import Vista.MainFrame.Finestra;
import Vista.Tray;

import java.awt.event.*;

import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;

/** Controlador de la finestra principal del casino*/
public class DraggableWindow implements MouseMotionListener,WindowListener,ActionListener,MouseListener {

    /** Finestra a controlar*/
    private Finestra vista;

    /** Posicio del mouse*/
    private int mouseX, mouseY;

    /** NetworkManager del casino per a solicitar el logOut*/
    private NetworkManager networkManager;

    /**
     * Crea el controlador de la finestra principal
     * @param f finestra que es vol controlar
     * @param networkManager controlador de la network per comunicarse amb el servidor
     */
    public DraggableWindow(Finestra f, NetworkManager networkManager) {
        vista = f;
        this.networkManager = networkManager;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        //Si es fa dragg i la finestra esta fullScreen, fem la finestra petita
        if (vista.getExtendedState() == MAXIMIZED_BOTH) {
            vista.setExtendedState(NORMAL);
            vista.goCenter();
            mouseX = vista.getWidth() / 2;
            mouseY = e.getY();
        }
        //Movem la finestra a la posicio del mouseX
        vista.setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //Es guarda la posicio del mouse cada cop que es mous
        mouseX = e.getX();
        mouseY = e.getY();
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

        //Gestio dels botons de la part superior de la finestra,on es troba normalment la decoration de windows
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

    //Canvia entre fullScreen i expansio normal
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
        //Si es fa dobleClick a la barra superior de la finestra, es maximitza/minimitza la finestra
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
