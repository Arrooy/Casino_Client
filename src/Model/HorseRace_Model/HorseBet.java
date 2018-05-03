package Model.HorseRace_Model;

import java.io.Serializable;

public class HorseBet implements Serializable {
    private long bet;
    private int horse;

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

    public HorseBet (long bet, int horse){
        this.bet = bet;
        this.horse = horse;

    }

}
