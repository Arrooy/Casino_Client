package Model;

import Network.Message;

/**
 * Missatge que serveix per a transmitir la informació
 * necessària per a representar totes les apostes realitzades pels
 * diferents usuaris. I anar actualitzant el seu contingut
 */
public class BetList extends Message {

    /** Tipus de missatge especific per a la ruleta */
    private static final int ROULETTE = 0;

    /** Tipus de missatge especific per als cavalls */
    private static final int HORSES = 1;

    /** Context del missatge*/
    private String context;

    /** Identificador del missatge*/
    private double ID;

    /** Conjunt d'informació a mostrar al llistat d'usuaris i apostes */
    private String[][] info;

    /**
     * Constructor de la classe.
     * @param info Nova llista a actualitzar
     * @param type Tipus de joc al que correspon
     */
    public BetList(String[][] info, int type) {
        if (type == ROULETTE) context = "rouletteListUpdate";
        else if (type == HORSES) context = "HORSES-ListUpdate";
        ID = Math.random();

        this.info = info;
    }

    /** Getter de la informació a mostrar */
    public String[][] getInfo() {
        return info;
    }

    /** Getter del context del missatge */
    @Override
    public String getContext() {
        return context;
    }

    /** Getter del context de l'identificador del missatge */
    @Override
    public double getID() {
        return ID;
    }
}
