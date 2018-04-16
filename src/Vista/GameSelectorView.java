package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class GameSelectorView extends View {

    private JButton jbSettings;
    private JButton jbLogOut;
    private JButton jbHorseRace;
    private JButton jbBlackJack;
    private JButton jbRoulette;

    public GameSelectorView(){

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0,0,0,0));

        //Panell per col·locar el botó LogOut a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        jpgblBack.setBackground(new Color(0,0,0,0));
        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jbLogOut = new JButton("LOGOUT");
        jbLogOut.setFocusable(false);
        jpgblBack.add(jbLogOut, c);
        //Flow Layout per a que el botó quedi a l'esquerra
        JPanel jpBack = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpBack.setBackground(new Color(0,0,0,0));
        jpBack.add(jpgblBack);
        this.add(jpBack, BorderLayout.SOUTH);

        //Panell que té el títol de la pantalla a dalt a la dreta al mig
        JPanel jpTitle = new JPanel();
        jpTitle.setBackground(new Color(0,0,0,0));
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setBackground(new Color(0,0,0,0));
        JLabel jlTitle = new JLabel("GAMES");
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 24));
        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els botons dels jocs centrats al mig de la pantalla
        JPanel jpgblBotons = new JPanel(new GridBagLayout());
        jpgblBotons.setBackground(new Color(0,0,0,0));
        jbBlackJack = new JButton("BLACKJACK");
        jbBlackJack.setFocusable(false);
        jbHorseRace = new JButton("HORSE RACE");
        jbHorseRace.setFocusable(false);
        jbRoulette = new JButton("ROULETTE");
        jbRoulette.setFocusable(false);
        //Marges
        c.insets = new Insets(0,0,0,20);

        //S'afegeixen els botons dels jocs
        c.gridy = 0;
        c.gridx = 0;
        jpgblBotons.add(jbBlackJack, c);

        c.gridx = 1;
        jpgblBotons.add(jbHorseRace, c);

        c.gridx = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbRoulette, c);

        //S'afegeix el botó per accedir a la configuració de l'usuari
        jbSettings = new JButton("Settings");
        jbSettings.setFocusable(false);

        c.gridy = 1;
        c.gridx = 1;
        c.insets = new Insets(100,0,0,20);

        jpgblBotons.add(jbSettings, c);

        this.add(jpgblBotons, BorderLayout.CENTER);
    }

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

        jbHorseRace.setActionCommand("horse");
        jbHorseRace.addActionListener(c);

        jbBlackJack.setActionCommand("blackJack");
        jbBlackJack.addActionListener(c);

        jbRoulette.setActionCommand("roulette");
        jbRoulette.addActionListener(c);
    }
}
