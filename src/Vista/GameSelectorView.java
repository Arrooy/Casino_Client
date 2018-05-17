package Vista;

import Controlador.Controller;
import Model.AssetManager;

import javax.swing.*;
import java.awt.*;

/**Classe que crea la vista on l'usuari pot escollir el joc al que vol jugar*/
public class GameSelectorView extends View {

    /**Boto per anar als settings*/
    private JButton jbSettings;

    /**Boto per fer logOut*/
    private JButton jbLogOut;

    /**Boto per jugar als cavalls*/
    private JButton jbHorseRace;

    /**Boto per jugar al BJ*/
    private JButton jbBlackJack;

    /**Boto per jugar a la ruleta*/
    private JButton jbRoulette;

    /**Color Crema*/
    private final static Color CREMA = new Color (218, 204, 164);

    /**Constructor de la vista que defineix on i com es col·loquen els elements*/
    public GameSelectorView(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        //Panell per col·locar el botó LogOut a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        jpgblBack.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();

        //Marges del boto
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jbLogOut = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbLogOut,"LGLGOUT.png", "LGOUTS.png", "LGOUTS.png");
        jpgblBack.add(jbLogOut, c);

        //Flow Layout per a que el boto quedi a l'esquerra
        JPanel jpBack = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpBack.setOpaque(false);

        //Es col·loca el boto a la part inferior de la pantalla
        jpBack.add(jpgblBack);
        this.add(jpBack, BorderLayout.SOUTH);

        //Panell que te el titol de la pantalla a dalt a la dreta al mig
        JPanel jpTitle = new JPanel();
        jpTitle.setOpaque(false);
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setOpaque(false);
        JLabel jlTitle = new JLabel("GAMES");

        //Es canvia la font i el color del titol
        jlTitle.setFont(AssetManager.getEFont(100));
        jlTitle.setForeground(CREMA);

        //Marges del titol
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els botons dels jocs centrats al mig de la pantalla
        JPanel jpgblBotons = new JPanel(new GridBagLayout());
        jpgblBotons.setOpaque(false);

        jbBlackJack = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbBlackJack,"BJ.png","BJS.png","BJN.png");

        jbHorseRace = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbHorseRace,"HR.png","HRS.png","HRN.png");

        jbRoulette = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbRoulette,"R.png","RS.png","RN.png");

        //Marges
        c.insets = new Insets(0,0,0,20);

        //S'afegeixen els botons dels jocs

        //Posicio boto BJ
        c.gridy = 0;
        c.gridx = 0;
        jpgblBotons.add(jbBlackJack, c);

        //Posicio boto cavalls
        c.gridx = 1;
        jpgblBotons.add(jbHorseRace, c);

        //Posicio boto ruleta
        c.gridx = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbRoulette, c);

        //S'afegeix el boto per accedir a la configuració de l'usuari
        jbSettings = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbSettings, "ST.png", "STS.png", "STS.png");

        //Posicio boto settings
        c.gridy = 1;
        c.gridx = 1;
        c.insets = new Insets(100,0,0,20);
        jpgblBotons.add(jbSettings, c);

        //S'afegeixen aquests elements al centre de la pantalla
        this.add(jpgblBotons, BorderLayout.CENTER);
        setOpaque(false);
    }

    /***/
    public void enableButtons(boolean guest){
        if(guest){
            jbHorseRace.setEnabled(false);
            jbRoulette.setEnabled(false);
            jbSettings.setVisible(false);
        } else{
            jbHorseRace.setEnabled(true);
            jbRoulette.setEnabled(true);
            jbSettings.setVisible(true);
        }
    }


    @Override
    public void addController(Controller c) {
        jbSettings.setActionCommand("settings");
        jbSettings.addActionListener(c);

        jbLogOut.setActionCommand("logOut");
        jbLogOut.addActionListener(c);

        jbHorseRace.setActionCommand("horseRace");
        jbHorseRace.addActionListener(c);

        jbBlackJack.setActionCommand("blackJack");
        jbBlackJack.addActionListener(c);

        jbRoulette.setActionCommand("roulette");
        jbRoulette.addActionListener(c);
    }

    private void configButton(JButton boto, String normal, String onSelection, String disabled){
        boto.setBorderPainted(false);
        boto.setBorder(null);
        boto.setFocusable(false);
        boto.setMargin(new Insets(0, 0, 0, 0));
        boto.setContentAreaFilled(false);

        boto.setIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(disabled)));
        boto.setRolloverIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setPressedIcon(new ImageIcon(AssetManager.getImage(onSelection)));
    }
}
