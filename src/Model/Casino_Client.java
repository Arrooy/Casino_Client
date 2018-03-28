package Model;
import Controlador.Controller;
import Network.*;
import Vista.MainView;

public class Casino_Client {
    public static void main(String[] args) {

        // Es crea la vista del Client
        MainView view = new MainView(640,480);

        //Es defineix el gestor de connectivitat amb el servidor
        NetworkManager networkManager = new NetworkManager();

        //Es crea el controlador del sistema i es relacionen controlador amb vista i controlador amb network
        Controller controller = new Controller(view, networkManager);

        //Es crea l'enllaç vista amb controlador
        view.addController(controller);

        //Es realitza la conexio amb el servidor i sagafen els streams d'entrada / sortida
        networkManager.connectarAmbServidor(controller);

        //Es fa visible la finestra grafica
        view.setVisible(true);
    }
}
