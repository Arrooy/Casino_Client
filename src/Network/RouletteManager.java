package Network;

import Controlador.Game_Controlers.RouletteController;
import Model.RouletteModel.RouletteBetMessage;
import Model.RouletteModel.RouletteMessage;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * Classe que s'encarregga de gestionar la comunicació entre el servidor
 * i el joc de la ruleta. Aquest es dedica a anar disparant una nova tirada
 * de la ruleta cada cop que rep l'ordre del servidor.
 *
 * A més a més controla la connexió i desconnexió al joc, notificant al servidor
 * sobre cada acció.
 */
public class RouletteManager extends Thread {

    /** Intermediari entre el servidor i el client */
    private NetworkManager networkManager;

    /** Indica si l'usuari es manté o no connectat al joc */
    private boolean connected;

    /** Controlador del joc de la ruleta */
    private RouletteController roulette;

    /**
     * Constructor de la classe
     * @param networkManager Intermediari Servidor<->Client
     */
    public RouletteManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    /**
     * Inicialitza el controlador vinculat i engega el fil d'execució
     * encarregat de controlar la comunicació amb el thread de la ruleta del
     * servidor
     * @param roulette Controlador al que se l'hi donarà les ordres
     */
    public void init(RouletteController roulette) {
        this.roulette = roulette;
        connected = true;

        start();
    }

    /**
     * Fil d'execució que gestiona les tirades de la ruleta. Inicialment envia
     * un missatge al servidor per a indicar la connexió; seguidament espera la resposta,
     * i en ella el servidor indica el temps a esperar per a la seva primera tirada.
     *
     * Un cop s'ha realitzat la primera tirada s'entra en el bucle en el que s'espera que
     * arribi una nova tirada, i es reposa el temps que indiqui el servidor.
     * Cada cop que es rep una tirada, aquesta es notifica al controlador de la ruleta i
     * seguidament es "dispara" la tirada.
     *
     * Finalment quan l'usuari desitgi desconectar-se es desactivarà "connected",
     * s'interromprà el descans del thread, i s'enviarà un missatge al servidor
     * notificant de la desconnexió.
     */
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

        roulette.requestWallet();

        RouletteController.updateNextTime((long) msg.getTimeTillNext());
        waitTillNext(Math.max(0, (long) msg.getTimeTillNext() - Timestamp.from(Instant.now()).getTime() - 10));

        while (connected) {
            RouletteMessage shot = (RouletteMessage) networkManager.waitForContext("roulette");

            roulette.setParams(shot.getRouletteVel(), shot.getBallVel(), shot.getShotOff());
            RouletteController.updateNextTime((long) shot.getTimeTillNext());
            roulette.shoot();

            waitTillNext(Math.max(0, (long) shot.getTimeTillNext() - Timestamp.from(Instant.now()).getTime() - 10));
        }

        //Es comunica al servidor la desconnexió de l'usuari de la ruleta
        msg = new RouletteMessage(1);
        new Transmission(msg, networkManager);
    }

    /**
     * Mètode que deixa un fil d'execució en repos. La seva unica finalitat consisteix
     * en natejar el codi i evitar el try/catch del sleep pel vell mig del mètode run().
     * @param time Tempsa esperar
     */
    private void waitTillNext(long time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            //System.out.println("[ROULETTE THREAD]: SLEEP INTERRUPTED");
        }
    }

    /**
     * Mètode per a aturar el fil d'execució i sortir del joc. Realment el que fa consisteix
     * en aturar el repos entre tirades del fil d'execució, i sortir del bucle de tirades.
     * D'aquesta manera s'executa igualment la notificació al servidor sobre la desconnexió
     * de l'usuari
     */
    public void disconnect() {
        interrupt();
        connected = false;
    }

    /**
     * Mètode que serveix per realitzar una aposta. Per una banda afegeix la aposta al
     * tauler d'apostes, i per altre l'afegeix al acumulador del propi joc que descompta
     * del moneder les monedes ja apostades.
     * @param msg Aposta realitzada satisfactoriament i aprovada pel servidor
     */
    public void bet(RouletteBetMessage msg) {
        roulette.bet(msg.getBet(), msg.getCellID());
        roulette.increaseBet(msg.getBet());
    }

    /**
     * Mètode per a actualitzar el valor del moneder.
     * @param wallet Nou valor del moneder
     */
    public void setWallet(long wallet) {
        roulette.resetBet();
        roulette.setWallet(wallet);
    }
}
