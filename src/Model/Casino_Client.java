package Model;
import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsManager;
import Controlador.DraggableWindow;
import Controlador.Game_Controlers.RouletteController;
import Network.*;
//import Vista.MainFrame.Finestra;
import Utils.Seguretat;
import Vista.MainFrame.Finestra;
import Controlador.SplashScreen;
import Vista.MainFrame.Finestra;

import javax.swing.*;
import java.awt.*;

public class Casino_Client {
    public static void main(String[] args) {

        /*
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setSize(600, 600);

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.setSize(600, 600);
        jFrame.add(jPanel);

        //Posar la ruleta ja en la finestra

        new GraphicsManager(jPanel, new RouletteController(jFrame.getWidth(), jFrame.getHeight()), null);*/


        //Es carreguen totes les dades del joc
        SplashScreen splashScreen = new SplashScreen();

        //Es crea la vista del Client
        Finestra finestra = new Finestra();

        //Es defineix el gestor de connectivitat amb el servidor
        NetworkManager networkManager = new NetworkManager(splashScreen);

        //Es crea el controlador de tamany per a la finestra
        DraggableWindow controladorFinestra = new DraggableWindow(finestra,networkManager);

        //Es crea el controlador del sistema i es relacionen controlador amb vista i controlador amb network
        Controller controller = new Controller(finestra, networkManager,controladorFinestra, finestra.getHorseRaceView());

        //Es crea l'enllaç vista amb controlador
        finestra.addController(controller,controladorFinestra);
        finestra.setMainView();

        //Es realitza la conexio amb el servidor i sagafen els streams d'entrada / sortida
        networkManager.connectarAmbServidor(controller);
    }
}
