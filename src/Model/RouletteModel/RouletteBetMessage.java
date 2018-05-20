package Model.RouletteModel;

import Network.Message;

/**
 * Missatge referent a una aposta realitzada al joc de la Ruleta.
 * Aquest notofica al servidor de la quantitat apostada i de la cel·la apostada,
 * i el servidor respon omplint el camp Successful en consequencia de si s'ha pogut
 * o no realitzar l'aposta, tenint en compte que no es pot apostar més diners
 * dels que l'usuari te.
 */
public class RouletteBetMessage extends Message {

    /** Identificador del missatge */
    private double ID;

    /** Context del missatge */
    private String context;

    /** Quantitat de monedes apostades */
    private long bet;

    /** Cel·la del tauler en la que s'ha realitzat la aposta */
    private int cellID;

    /** Indica si s'ha realitzat satisfactoriament o no l'aposta en el servidor */
    private boolean successful;

    /**
     * Constructor del missatge
     * @param bet Quantitat de monedes apostades
     * @param cellID Cel·la per la que s'ha apostat
     */
    public RouletteBetMessage(long bet, int cellID) {
        this.bet = bet;
        this.cellID = cellID;

        context = "rouletteBet";
        ID = Math.random();

        successful = false;
    }

    /** Setter de successful */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    /** Getter de l'identificador de la cel·la apostada */
    public int getCellID() {
        return cellID;
    }

    /** Getter de la quantitat de monedes apostades */
    public long getBet() {
        return bet;
    }

    /** Indica si ha estat o no satisfactòria la aposta en el servidor */
    public boolean isSuccessful() {
        return successful;
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
