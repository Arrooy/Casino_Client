package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class MainViewClient extends View{

    private JButton logInButton;
    private JButton logOutButton;
    private JButton jbSignIn;
    private JButton jbGuest;

    /**
     *  Crea la vista del client amb una amplada i una alçada determinades per width i height
     */

    public MainViewClient(){
        this.setLayout(new BorderLayout());

        logOutButton = new JButton("EXIT");
        logOutButton.setFocusable(false);
        logOutButton.setPreferredSize(new Dimension(150,30));

        //Panell per col·locar el botó exit a la part baixa a l'esquerra
        JPanel jpgblExit = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(0,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jpgblExit.add(logOutButton, c);
        JPanel jpExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpExit.add(jpgblExit);
        this.add(jpExit, BorderLayout.SOUTH);

        //Panell que té els botons per iniciar el joc amb el mode desitjat
        JPanel jpgblBotons = new JPanel(new GridBagLayout());

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        logInButton = new JButton("LOGIN");
        logInButton.setFocusable(false);
        logInButton.setPreferredSize(new Dimension(150,30));
        jbSignIn = new JButton("SINGIN");
        jbSignIn.setFocusable(false);
        jbSignIn.setPreferredSize(new Dimension(150,30));
        jbGuest = new JButton("GUEST");
        jbGuest.setFocusable(false);
        jbGuest.setPreferredSize(new Dimension(150,30));

        jpgblBotons.add(logInButton, c);

        c.gridy = 1;
        jpgblBotons.add(jbSignIn, c);

        c.gridy = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbGuest, c);

        this.add(jpgblBotons, BorderLayout.CENTER);
    }

    /** Afegeix el controlador del programa a la vista*/
    @Override
    public void addController(Controller c){

        jbSignIn.setActionCommand("goSignIn");
        jbSignIn.addActionListener(c);

        //Tenen el mateix actionCommand perque les dues accions resulten en el mateix - wtf?
        logInButton.setActionCommand("goToLogIn");
        logInButton.addActionListener(c);

        logOutButton.setActionCommand("exitProgram");
        logOutButton.addActionListener(c);

        jbSignIn.setActionCommand("signIn");
        jbSignIn.addActionListener(c);

        jbGuest.setActionCommand("guest");
        jbGuest.addActionListener(c);
    }

    /** Obra una finestra indicant un error*/
    public void displayError(String title,String errorText) {
        JOptionPane.showMessageDialog(null,title,errorText,JOptionPane.ERROR_MESSAGE);
    }

    public boolean displayQuestion(String message) {
        //Retorna true si
        //Retorn false no
        return JOptionPane.showConfirmDialog(null,message,"Are you sure?",JOptionPane.YES_NO_OPTION) == 0;
    }
}
