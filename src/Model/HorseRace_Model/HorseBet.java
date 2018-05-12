package Model.HorseRace_Model;

import java.io.Serializable;

/**Classe que representa una aposta en les carreres de cavalls*/
public class HorseBet implements Serializable {
    private long bet;
    private int horse;
    private String name;
    private boolean betOK;

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

    public HorseBet (long bet, int horse, String name){
        this.bet = bet;
        this.horse = horse;
        this.name = name;
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
