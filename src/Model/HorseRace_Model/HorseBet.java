package Model.HorseRace_Model;

import java.io.Serializable;

/**Classe que representa una aposta en les carreres de cavalls*/
public class HorseBet implements Serializable {
    /** Aposta*/
    private long bet;

    /** Identificador pel cavall*/
    private int horse;

    /** Nom del que ha fet la aposta**/
    private String name;

    /** Indica si l'aposta es correcte*/
    private boolean betOK;

    /***
     * Crea una nova aposta per als cavalls
     * @param bet cantitat de monedes que es volen apostar
     * @param horse cavall que es vol apostar
     * @param name nom de l'usuari que vol fer l'aposta
     */
    public HorseBet (long bet, int horse, String name){
        this.bet = bet;
        this.horse = horse;
        this.name = name;
    }

    public HorseBet(boolean betOK, long bet){
        this.betOK = betOK;
        this.bet = bet;
    }

    /**
     * S'indica si una aposta s'a pogut trametre
     * @return boolea que indica si l'aposte s'ha afegit correctament
     */
    public boolean isBetOK() {
        return betOK;
    }

    public void setBetOK(boolean betOK) {
        this.betOK = betOK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBet() {
        return bet;
    }

    public void setBet(long bet) {
        this.bet = bet;
    }

    public int getHorse() {
        return horse;
    }

    public void setHorse(int horse) {
        this.horse = horse;
    }
}
