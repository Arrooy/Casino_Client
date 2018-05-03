package Model.HorseRace_Model;
import Network.Message;

public class HorseMessage extends Message {
    String option;
    HorseBet horseBet;
    HorseSchedule horseSchedule;

    public HorseMessage(HorseBet horseBet, String option){
        this.horseBet = horseBet;
        this.option = option;
        this.horseSchedule = null;
    }
    public HorseMessage(HorseSchedule horseSchedule, String option){
        this.horseBet = null;
        this.option = option;
        this.horseSchedule = horseSchedule;
    }


    @Override
    public String getContext() {
        return "HORSES-" + option;
    }

    @Override
    public double getID() {
        return 0;
    }
}
