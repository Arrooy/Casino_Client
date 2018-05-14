package Vista.GameViews;

import Controlador.Controller;
import Vista.View;

import javax.swing.*;

/**Vista per la cursa de la carrera dels cavalls*/
public class HorseRaceView extends View {
    private static final int INITIAL_X = 0;
    private static final int INITIAL_Y= 0;
    private static final int HORSE_WIDTH = 0;
    private static final int HORSE_HEIGHT = 0;
    private static final int NUM_HORSES = 12;
    private static final int HORSE_SEPARATION = 0;


    public HorseRaceView (){
        setFocusable(true);
    }


    @Override
    public void addController(Controller c) {

    }


}
