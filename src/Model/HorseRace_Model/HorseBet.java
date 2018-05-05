package Model.HorseRace_Model;

import java.io.Serializable;

/**Classe que representa una aposta en les carreres de cavalls*/
public class HorseBet implements Serializable {
    private long bet;
    private int horse;
    private double ID;
    private boolean betOK;

    public double getID() {
        return ID;
    }

    public void setID(double ID) {
        this.ID = ID;
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

    public HorseBet (long bet, int horse, double ID){
        this.bet = bet;
        this.horse = horse;
        this.ID = ID;
    }


    public HorseBet(boolean betOK){
        this.betOK = betOK;
    }


    public boolean isBetOK() {
        return betOK;
    }

    public void setBetOK(boolean betOK) {
        this.betOK = betOK;
    }
}
