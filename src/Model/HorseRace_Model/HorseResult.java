package Model.HorseRace_Model;

import java.io.Serializable;

/**Classe que permet transmetre el guayador de la cursa de cavalls i el premi d'una aposta*/
public class HorseResult implements Serializable {
    /**Guanyador de la cursa*/
    private int winner;
    /**Premi d'una aposta*/
    private long prize;

    /**
     * Guardem el guanyador de la cursa i el premi que ha rebut l'usuari
     * @param winner guanyador de la cursa
     * @param prize premi per apostar
     */
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