package Model;
import Controlador.Controller;
import Controlador.DraggableWindow;
import Network.*;
import Vista.MainFrame.Finestra;
import Vista.SplashScreen.SplashScreen;

public class Casino_Client {
    public static void main(String[] args) {

        //Es carreguen totes les dades del joc
        SplashScreen splashScreen = new SplashScreen();

        //Es crea la vista del Client
        Finestra finestra = new Finestra();

        //Es defineix el gestor de connectivitat amb el servidor
        NetworkManager networkManager = new NetworkManager(splashScreen);

        DraggableWindow controladorFinestra = new DraggableWindow(finestra,networkManager);

        //Es crea el controlador del sistema i es relacionen controlador amb vista i controlador amb network
        Controller controller = new Controller(finestra, networkManager,controladorFinestra);

        //Es crea l'enllaç vista amb controlador
        finestra.addController(controller,controladorFinestra);
        finestra.setMainView();

        //Es realitza la conexio amb el servidor i sagafen els streams d'entrada / sortida
        networkManager.connectarAmbServidor(controller);
    }
}
