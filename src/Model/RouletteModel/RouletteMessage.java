package Model.RouletteModel;

import Network.Message;

/**
 * Missatge referent a una acció del joc de la ruleta, excloent
 * les apostes. S'encarrega de controlar la desconnexió i connexió de
 * l'usuari al joc notificant-ho al servidor, i de rebre la informació
 * necessària per a reproduir satisfactoriament cada tirada del joc de
 * la Roulette.
 */
public class RouletteMessage extends Message {

    /** Context del missatge */
    private final String context;

    /** Identificador del missatge */
    private final double ID;

    /* VARIABLES DEL MISSATGE */

    /** Velocitat inicial de la ruleta */
    private double rouletteVel;

    /** Velocitat vertical inicial de la bola */
    private double ballVel;

    /** Casella guanyadora de la tirada */
    private int winner;

    /** Moment en el que es realitzarà la següent tirada */
    private double timeTillNext;

    /** Offset de caselles inicial de la tirada */
    private int shotOff;

    /**
     * Constructor que genera el missatge de Servidor a Client que notifica
     * els valors de les variables aleatòries que actuen en cada tir
     * @param rouletteVel Velocitat inicial de la ruleta
     * @param ballVel Velocitat inicial de la bola
     * @param winner Casella guanyadora
     * @param shotOff Offset de caselles inicial de la ruleta
     */
    public RouletteMessage(double rouletteVel, double ballVel, int winner, int shotOff) {
        this.rouletteVel = rouletteVel;
        this.ballVel = ballVel;
        this.winner = winner;
        this.shotOff = shotOff;

        context = "roulette";

        ID = Math.random();
    }

    /**
     * Constructor del missatge de Client a Servidor en el que
     * el client notifica d'una connexió / desconnexió del joc
     * @param type Connexió (0) / Desconnexió (!0)
     */
    public RouletteMessage(int type) {
        if (type == 0) context = "rouletteConnection";
        else context = "rouletteDisconnection";
        ID = Math.random();
        rouletteVel = 0;
        ballVel = 0;
        winner = 100;
    }

    /** Getter de l'offset */
    public int getShotOff() {
        return shotOff;
    }

    /** Getter del temps restant per a la següent tirada */
    public double getTimeTillNext() {
        return timeTillNext;
    }

    /** Setter del moment pròxim en el que es realitzarà un tir */
    public void setTimeTillNext(double timeTillNext) {
        this.timeTillNext = timeTillNext;
    }

    /** Getter de la velocitat inicial de la ruleta */
    public double getRouletteVel() {
        return rouletteVel;
    }

    /** Getter de la velocitat inicial de la bola */
    public double getBallVel() {
        return ballVel;
    }

    /** Getter del guanyador de la partida */
    public int getWinner() {
        return winner;
    }

    /** Getter del context del missatge */
    @Override
    public String getContext() {
        return context;
    }

    /** Getter de l'identificador del missatge */
    @Override
    public double getID() {
        return ID;
    }
}
