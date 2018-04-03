package Vista;

import javax.swing.*;
import java.awt.*;

public class MainViewClient extends JPanel{
    public MainViewClient(){
        this.setLayout(new BorderLayout());

        JButton jbExit = new JButton("EXIT");
        jbExit.setFocusable(false);
        jbExit.setPreferredSize(new Dimension(150,30));

        //Panell per col·locar el botó exit a la part baixa a l'esquerra
        JPanel jpgblExit = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(0,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jpgblExit.add(jbExit, c);
        JPanel jpExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpExit.add(jpgblExit);
        this.add(jpExit, BorderLayout.SOUTH);

        //Panell que té els botons per iniciar el joc amb el mode desitjat
        JPanel jpgblBotons = new JPanel(new GridBagLayout());

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        JButton jbLogIn = new JButton("LOGIN");
        jbLogIn.setFocusable(false);
        jbLogIn.setPreferredSize(new Dimension(150,30));
        JButton jbSingIn = new JButton("SINGIN");
        jbSingIn.setFocusable(false);
        jbSingIn.setPreferredSize(new Dimension(150,30));
        JButton jbGuest = new JButton("GUEST");
        jbGuest.setFocusable(false);
        jbGuest.setPreferredSize(new Dimension(150,30));

        jpgblBotons.add(jbLogIn, c);

        c.gridy = 1;
        jpgblBotons.add(jbSingIn, c);

        c.gridy = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbGuest, c);

        this.add(jpgblBotons, BorderLayout.CENTER);
    }
}
