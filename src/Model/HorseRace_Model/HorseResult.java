package Model.HorseRace_Model;

import java.io.Serializable;

/**Classe que permet transmetre el guayador de la cursa de cavalls i el premi d'una aposta*/
public class HorseResult implements Serializable {
    private int winner;
    private long prize;


    public HorseResult(int winner, long prize){
        this.winner = winner;
        this.prize = prize;
    }

    public int getWinner() {
        return winner;
    }

    public long getPrize() {
        return prize;
    }
}