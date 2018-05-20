package Network;

import Model.RouletteModel.RouletteBetMessage;
import Vista.MainFrame.Finestra;

/**
 * Classe que realitza una petició d'aposta al servidor.
 * Aquesta obre un JInputDialog on es demana al usuari un
 * valor de diners a apostar, i un cop introduit, en cas
 * d'haver introduit efectivament un numero i no qualsevol altre
 * caràcter, s'envia una petició d'aposta al servidor.
 */
public class BetGetter extends Thread {

    /** Cel·la en la que s'està apostant */
    private int cellID;

    /** Punt de comunicació amb el servidor */
    private NetworkManager networkManager;

    /** Missatge que apareix al Dialog */
    private String message;

    /** Titol del dialog */
    private String title;

    /**
     * Constructor de la classe. A més a més d'inicialitzar els atributs,
     * també s'encarrega d'iniciar el fil d'execució en el que es realitza
     * la petició del Dialog i la comunicació amb el servidor
     * @param cellID Cel·la a la que apostar
     * @param networkManager Comunicador entre el client i el servidor
     * @param message Text que es mostra en el Dialog
     * @param title Títol del Dialog
     */
    public BetGetter(int cellID, NetworkManager networkManager, String message, String title) {
        this.cellID = cellID;
        this.networkManager = networkManager;
        this.message = message;
        this.title = title;

        start();
    }

    /**
     * Fil d'execució en el que es realitza tota l'acció de la classe
     */
    @Override
    public void run() {
        try {
            if (cellID >= 0) {
                long bet = Long.parseLong(Finestra.showInputDialog(message, title));

                if (bet > 0) new Transmission(new RouletteBetMessage(bet, cellID), networkManager);
                else Finestra.showDialog("Input not valid", "Error");
            }
        } catch (Exception e1) {
            Finestra.showDialog("Input not valid", "Error");
        }
    }
}