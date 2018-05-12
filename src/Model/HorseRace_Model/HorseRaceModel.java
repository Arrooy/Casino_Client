package Model.HorseRace_Model;


/**Model de la cursa de cavalls*/
public class HorseRaceModel {
    HorseSchedule horseSchedule;
    HorseBet horseBet;

    public HorseRaceModel(){
    }

    public void setHorseSchedule(HorseSchedule horseSchedule){
        this.horseSchedule = horseSchedule;
    }

    public HorseSchedule getHorseSchedule() {
        return horseSchedule;
    }

    public HorseBet getHorseBet(){
        return horseBet;


    }
}
