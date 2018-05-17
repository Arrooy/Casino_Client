package Vista.GameViews.Roulette;

import Controlador.Controller;
import Network.BetGetter;
import Network.NetworkManager;
import Vista.View;

/**
 * Vista de la ruleta. Ja que aquesta no conté cap element
 * de Swing, practicament es troba buida, ja que a més a més,
 * en aquest panell, Graphics Manager i el Controlador general
 * són els qui s'encarreguen de vincular els graphics generats per
 * RouletteController en aquest JPanel.
 */
public class RouletteView extends View {

    /**
     * Mètode per a registrar el controlador de la vista
     * @param c Controlador a registrar
     */
    @Override
    public void addController(Controller c) {
        addComponentListener(c);
    }

    /**
     * Mètode per a fer una petició d'aposta. Aquest genera un
     * JInputDialog en un fil d'execució apart, en el que s'espera
     * la resposta de l'usuari, i un cop rebuda, es genera una petició d'aposta
     * al servidor.
     * @param cellID Cel·la del tauler en la que es vol apostar
     * @param networkManager Comunicador entre el servidor i el client
     * @param message Text a mostrar al JInputDialog
     * @param title Text a mostrar com a títol de la finestra
     */
    public void bet(int cellID, NetworkManager networkManager, String message, String title) {
        new BetGetter(cellID, networkManager, message, title);
    }
}