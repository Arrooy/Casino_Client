package Network;

import Controlador.Game_Controlers.RouletteController;
import Model.RouletteModel.RouletteBetMessage;
import Vista.MainFrame.Finestra;

public class BetGetter extends Thread {

    private int cellID;
    private NetworkManager networkManager;

    private String message;
    private String title;

    public BetGetter(int cellID, NetworkManager networkManager, String message, String title) {
        this.cellID = cellID;
        this.networkManager = networkManager;
        this.message = message;
        this.title = title;

        start();
    }

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