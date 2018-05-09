package Vista;

import Controlador.Controller;
import Model.AssetManager;

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

        //Panell per col·locar el botó LogOut a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        jpgblBack.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jbLogOut = new JButton("LOGOUT");
        jbLogOut.setFocusable(false);
        jpgblBack.add(jbLogOut, c);
        //Flow Layout per a que el botó quedi a l'esquerra
        JPanel jpBack = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpBack.setOpaque(false);

        jpBack.add(jpgblBack);
        this.add(jpBack, BorderLayout.SOUTH);

        //Panell que té el títol de la pantalla a dalt a la dreta al mig
        JPanel jpTitle = new JPanel();
        jpTitle.setOpaque(false);
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setOpaque(false);
        JLabel jlTitle = new JLabel("GAMES");
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 24));

        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els botons dels jocs centrats al mig de la pantalla
        JPanel jpgblBotons = new JPanel(new GridBagLayout());
        jpgblBotons.setOpaque(false);

        jbBlackJack = new JButton("vlakYak");
        //configButton(jbBlackJack,"blackJackButton.png","blackJackButtonOnMouse.png","blackJackButton.png");

        jbHorseRace = new JButton("Jorses");
        //configButton(jbHorseRace,"horseButton.png","horseButtonOnMouse.png","horseButtonDisabled.png");

        jbRoulette = new JButton("Perejil");
        //configButton(jbRoulette,"rouletteButton.png","rouletteButtonOnMouse.png","rouletteButtonDisabled.png");

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
        setOpaque(false);
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
