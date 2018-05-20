package Model.HorseRace_Model;

import java.io.Serializable;
import java.util.Random;

/**Classe que permet enviar el temps que trigara cada cavall en arribar a diferents seccions de la cursa*/
public class HorseSchedule implements Serializable {
    private int[][] times;
    private int winner;

    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;

    private static final int MAX_SECTION_TIME = 2000;
    private static final int MIN_SECTION_TIME = 1000;

    /**
     * Es creen temps aleatoris entre MAX_SECTION_TIME i MIN_SECTION_TIME per cada cavall a cada seccio de les SECTIONS seccions.
     * El temps de la cursa també es aleatori i oscillara entre 5 i 10 segons.
     */
    public HorseSchedule(){
        Random rand = new Random();
        int[] totalTime = new int[12];
        int bestTime = 13000;
        this.winner = -1;
        this.times = new int[MAX_HORSES][SECTIONS];

        for(int i = 0; i < MAX_HORSES; i++){
            for(int j = 0; j < SECTIONS; j++){
                this.times[i][j] = rand.nextInt(MIN_SECTION_TIME) + MIN_SECTION_TIME;
                totalTime[i]+=times[i][j];
            }
            if(totalTime[i] < bestTime){
                bestTime = totalTime[i];
                this.winner = i;
            }
        }
    }

    /**Retorna el temps total de la carrera*/
    public int getRaceTime(){
        int slowestTime = -1;
        int[] totalTime = new int[12];

        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 5; j++){
                totalTime[i]+=times[i][j];
            }
            if(totalTime[i] > slowestTime){
                slowestTime = totalTime[i];
            }
        }
        return slowestTime;
    }

    /**
     * Retorna el temps d'un cavall en una seccio
     * @param i Cavall
     * @param j Seccio
     * @return temps d'un cavall en una seccio
     */
    public int getTime(int i,int j){
        return times[i][j];
    }

    /** Retorna el guanyador de la cursa**/
    public int getWinner(){
        return winner;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WINNER: ").append(this.winner).append("\nRACETIME: ").append(getRaceTime()).append("\n");
        for(int i = 0; i < 12; i++){
            stringBuilder.append("HORSE ").append(i).append("\n");
            for(int j = 0; j < 5; j++){
                stringBuilder.append("\tTime ").append(j).append(": ").append(times[i][j]).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
