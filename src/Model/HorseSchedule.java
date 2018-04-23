package Model;

import java.io.Serializable;
import java.util.Random;

public class HorseSchedule implements Serializable {
    private int[][] times;
    private int winner;

    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;
    private static final int MAX_SECTION_TIME = 200;
    private static final int MIN_SECTION_TIME = 100;



    public HorseSchedule(){
        this.times = new int[12][5];
    }

    public int generateRace(){
        Random rand = new Random();
        int[] totalTime = new int[12];
        int bestTime = 1300;
        int winner = -1;

        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 5; j++){
                times[i][j] = rand.nextInt(100) + 100;
                totalTime[i]+=times[i][j];
            }
            if(totalTime[i] < bestTime){
                bestTime = totalTime[i];
                winner = i;
            }
        }
        return winner;

    }
}
