package Model.HorseRace_Model;
import Network.Message;

public class HorseMessage extends Message {
    private String option;
    private HorseBet horseBet;
    private HorseSchedule horseSchedule;
    private float timeForRace;
    private HorseResult horseResult;
    private double ID;

    public void setID(double ID) {
        this.ID = ID;
    }

    public HorseMessage(HorseBet horseBet, String option){
        this.horseBet = horseBet;
        this.option = option;
        this.horseSchedule = null;
        this.horseResult = null;
    }
    public HorseMessage(HorseSchedule horseSchedule, String option){
        this.horseBet = null;
        this.option = option;
        this.horseSchedule = horseSchedule;
        this.horseResult = null;

    }

    public HorseMessage(float timeForRace, String option){
        this.timeForRace = timeForRace;
        this.option = option;
        this.horseSchedule = null;
        this.horseBet = null;
        this.horseResult = null;
    }

    public HorseMessage(HorseResult result, String option){
        this.horseBet = null;
        this.option = option;
        this.horseSchedule = null;
        this.horseResult = result;
    }

    public float getTimeForRace(){
        return timeForRace;
    }

    public HorseResult getHorseResult(){
        return this.horseResult;
    }

    /*
HorseMessage getContext()-

    TASK           |STRING                |DIRECTION  |DESCRIPTION
-------------------------------------------------------------------------------------------------------------------------------------
    Join            HORSES-Connection       C->S        Client wants to join game
    Leave           HORSES-Disconnection    C->S        Client wants to leave game
    Bet             HORSES-Bet              C->S        Client wants to place a bet
    FinishMessage   HORSES-Finished         C->S        Client's race has finished
    Result          HORSES-Result           S->C        Server is sending a client their prize, and informing about the winning horse
    Countdown       HORSES-Countdown        S->C        Server informs a client about the time remaining to begin a race
    Schedule        HORSES-Schedule         S->C        Server sends the race schedule for reproducing the race
 */
    @Override
    public String getContext() {
        return "HORSES-" + option;
    }

    @Override
    public double getID() {
        return this.ID;
    }

    public HorseBet getHorseBet(){
        return this.horseBet;
    }

    public HorseSchedule getHorseSchedule(){
        return this.horseSchedule;
    }
}
