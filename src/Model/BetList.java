package Model;

import Network.Message;

/**
 * Missatge que serveix per a transmitir la informació
 * necessària per a representar totes les apostes realitzades pels
 * diferents usuaris.
 */
public class BetList extends Message {

    public static final int ROULETTE = 0;
    public static final int HORSES = 1;

    /** Context del missatge*/
    private String context;

    /** Identificador del missatge*/
    private double ID;

    private String[][] info;

    public BetList(String[][] info, int type) {
        if (type == ROULETTE) context = "rouletteListUpdate";
        else if (type == HORSES) context = "horsesListUpdate";
        ID = Math.random();

        this.info = info;
    }

    public String[][] getInfo() {
        return info;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public double getID() {
        return ID;
    }
}
