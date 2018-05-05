package Vista.GameViews;

import Controlador.Controller;
import Controlador.Game_Controlers.HorseRaceController;
import Model.HorseRace_Model.Horse;
import Vista.View;

import javax.swing.*;
import java.awt.*;

/**Vista per la cursa de la carrera dels cavalls*/
public class HorseRaceView extends View {
    private static final int INITIAL_X = 0;
    private static final int INITIAL_Y= 0;
    private static final int HORSE_WIDTH = 0;
    private static final int HORSE_HEIGHT = 0;
    private static final int NUM_HORSES = 12;
    private static final int HORSE_SEPARATION = 0;


    private JPanel jpSouth;
    private JPanel jpEast;
    private JPanel jpCenter;
    private JPanel jpTrack;
    private JPanel jpBets;
    private Horse[] horses;
    private JButton jbBack;
    private long betAmount;
    private int betHorse;

    public HorseRaceView (){
        this.setLayout(new BorderLayout());

        this.jpEast = new JPanel(new GridBagLayout());
        this.jpCenter = new JPanel(new GridBagLayout());
        this.jpSouth = new JPanel(new GridBagLayout());
        this.jpTrack = new JPanel(new BorderLayout());
        this.jpTrack.setSize(600,300);
        this.jpBets = new JPanel(new BorderLayout());

        JLabel jlProba = new JLabel("Cavalls");
        this.jpTrack.add(jlProba);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 100, 300, 100);
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 4;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        this.jpCenter.add(jpTrack, c);

        JLabel jlProba2 = new JLabel("Apostes");
        this.jpBets.add(jlProba2);

        c = new GridBagConstraints();
        c.insets = new Insets(0, 100, 100, 100);
        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 4;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        this.jpCenter.add(jpBets, c);

        jbBack = new JButton("BACK");
        jbBack.setFocusable(false);
        jbBack.setPreferredSize(new Dimension(100,30));

        //Panell per col·locar el botó back a la part baixa a l'esquerra
        JPanel jpgblExit = new JPanel(new GridBagLayout());
        //Marges
        c.insets = new Insets(0,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jpgblExit.add(jbBack, c);
        JPanel jpExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpExit.add(jpgblExit);
        this.add(jpExit, BorderLayout.SOUTH);


        this.add(jpEast, BorderLayout.EAST);
        this.add(jpCenter, BorderLayout.CENTER);
    }


    public JPanel getTrack(){
        return this.jpTrack;
    }


    @Override
    public void addController(Controller c) {

    }

    /**Metode que ens permet afegir un controlador per gestionar la vista*/
    public void addHorseController(HorseRaceController horseRaceController){
        this.jbBack.setActionCommand("HORSES-Back");
        this.jbBack.addActionListener(horseRaceController);
    }

    public long getBetAmount() {
        return this.betAmount;
    }

    public int getBetHorse() {
        return this.betHorse;
    }
}
