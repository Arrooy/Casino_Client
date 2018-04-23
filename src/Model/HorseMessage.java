package Model;

import Network.Message;

public class HorseMessage extends Message {
    int option;
    HorseBet horseBet;
    HorseSchedule horseSchedule;

    public HorseMessage(HorseBet horseBet, int option){
       this.horseBet = horseBet;
       this.option = option;
       this.horseSchedule = null;
    }
    public HorseMessage(HorseSchedule horseSchedule, int option){
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
