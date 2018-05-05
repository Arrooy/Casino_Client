package Model.HorseRace_Model;


/**Model de la cursa de cavalls*/
public class HorseRaceModel {
    HorseSchedule horseSchedule;
    HorseBet horseBet;
    Horse[] horses;


    public HorseRaceModel(){
        horses = new Horse[12];

    }


    public void setHorseSchedule(HorseSchedule horseSchedule){
        this.horseSchedule = horseSchedule;
    }

    public HorseSchedule getHorseSchedule() {
        return horseSchedule;
    }

    public void setHorseBet(HorseBet horseBet) {
        this.horseBet = horseBet;
    }

    public Horse[] getHorses() {
        return horses;
    }

    public void setHorses(Horse[] horses) {
        this.horses = horses;
    }

    public HorseBet getHorseBet(){
        return horseBet;


    }


}
