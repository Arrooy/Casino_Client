package Network;

import Controlador.Game_Controlers.RouletteController;
import Model.RouletteModel.RouletteBetMessage;
import Model.RouletteModel.RouletteMessage;

import java.sql.Timestamp;
import java.time.Instant;

public class RouletteManager extends Thread {

    private NetworkManager networkManager;
    private boolean connected;

    private RouletteController roulette;

    public RouletteManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    public void init(RouletteController roulette) {
        this.roulette = roulette;
        connected = true;

        start();
    }

    @Override
    public void run() {
        //S'estableix la communicació amb el servidor
        RouletteMessage msg = new RouletteMessage(0);
        Transmission t = new Transmission(msg, networkManager);

        try {
            msg = (RouletteMessage) t.waitResponse(msg);
        } catch (InterruptedException e) {
            System.out.println("No s'ha pogut llegir la resposta del servidor");
            e.printStackTrace();
        }

        RouletteController.updateNextTime((long) msg.getTimeTillNext());
        waitTillNext(Math.max(0, (long) msg.getTimeTillNext() - Timestamp.from(Instant.now()).getTime() - 10));

        while (connected) {
            RouletteMessage shot = (RouletteMessage) networkManager.waitForContext("roulette");
            System.out.println("[ROULETTE]: s'ha rebut una tirada");

            roulette.setParams(shot.getRouletteVel(), shot.getBallVel(), shot.getShotOff());
            RouletteController.updateNextTime((long) shot.getTimeTillNext());
            roulette.shoot();

            waitTillNext(Math.max(0, (long) shot.getTimeTillNext() - Timestamp.from(Instant.now()).getTime() - 10));
        }

        //Es comunica al servidor la desconnexió de l'usuari de la ruleta
        msg = new RouletteMessage(1);
        new Transmission(msg, networkManager);
        System.out.println("[ROULETTE]: Disconnection");
    }

    private void waitTillNext(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            System.out.println("[ROULETTE THREAD]: SLEEP INTERRUPTED");
        }
    }

    public void disconnect() {
        interrupt();
        connected = false;
    }

    public void bet(RouletteBetMessage msg) {
        roulette.bet(msg.getBet(), msg.getCellID());
    }
}
