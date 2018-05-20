package Model;
import Controlador.Controller;
import Controlador.DraggableWindow;
import Network.*;
//import Vista.MainFrame.Finestra;
import Vista.MainFrame.Finestra;
import Controlador.SplashScreen;

/**
 * Classe principal del Client. Inicia les finestres necessaries,
 * carrega la informació adient i inicia tot el joc.
 *
 * Concretament inicia en una primera instancia una finestra de càrrega
 * que informa a l'usuari sobre els recursos que s'estàn carregant, els que
 * consisteixen principalment en imatges i audios.
 *
 * Un cop carregada tota la informació, s'inicialitza la finestra principal del
 * joc, juntament amb la barra superior personalitzada que permet arrossegar la
 * finestra, tancar-la, minimitzar-la, fer full screen, silenciar els sons i
 * la música i dins del joc, accedir als Settings de l'usuari.
 *
 * Juntament amb la finestra s'inicialitza el controlador, amb el que relitza el
 * vincle que permetrà a l'usuari interaccionar amb la finestra. Paral·lelament
 * es carrega el Network Manager, que consisteix en l'encarregat de controlar
 * la comunicació amb el servidor.
 */
public class Casino_Client {
    public static void main(String[] args) {

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
