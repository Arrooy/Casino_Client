package Model.HorseRace_Model;


/**Model de la cursa de cavalls*/
public class HorseRaceModel {
    /**Temps de cada cavall a cada seccio*/
    private HorseSchedule horseSchedule;

    public HorseRaceModel(){
    }

    public void setHorseSchedule(HorseSchedule horseSchedule){
        this.horseSchedule = horseSchedule;
    }

    public HorseSchedule getHorseSchedule() {
        return horseSchedule;
    }


}
