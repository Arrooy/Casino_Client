package Model.HorseRace_Model;

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
        return -1;
    }

    public int[][] getTimes(){
        return times;
    }

    public int getWinner(){
        return winner;
    }


}
