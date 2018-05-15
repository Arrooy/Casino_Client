package Vista;

import Controlador.Controller;
import Model.AssetManager;

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

        GridBagConstraints c = new GridBagConstraints();

        //Panell que té els botons per iniciar el joc amb el mode desitjat
        JPanel jpgblBotons = new JPanel(new GridBagLayout());

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        logInButton = new JButton();
        configButton(logInButton,"LOGIN_SOMBRA.png","LOGIN_NO_SOMBRA.png");

        jbSignIn = new JButton();
        configButton(jbSignIn,"SIGNIN_SOMBRA.png","SIGNIN_NO_SOMBRA.png");

        jbGuest = new JButton();
        configButton(jbGuest,"GUEST_SOMBRA.png","GUEST_NO_SOMBRA.png");

        jpgblBotons.add(logInButton, c);

        c.gridy = 1;
        jpgblBotons.add(jbSignIn, c);

        c.gridy = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbGuest, c);
        jpgblBotons.setOpaque(false);
        this.add(jpgblBotons, BorderLayout.CENTER);
        setOpaque(false);
    }

    /** Afegeix el controlador del programa a la vista*/
    @Override
    public void addController(Controller c){

        jbSignIn.setActionCommand("goSignIn");
        jbSignIn.addActionListener(c);

        //Tenen el mateix actionCommand perque les dues accions resulten en el mateix - wtf?
        logInButton.setActionCommand("goToLogIn");
        logInButton.addActionListener(c);

        jbSignIn.setActionCommand("signIn");
        jbSignIn.addActionListener(c);

        jbGuest.setActionCommand("guest");
        jbGuest.addActionListener(c);
    }

    /** Obra una finestra indicant un error*/
    public void displayError(String title,String errorText) {
        JOptionPane.showMessageDialog(null,errorText,title,JOptionPane.ERROR_MESSAGE);
    }

    public boolean displayQuestion(String message) {
        //Retorna true si
        //Retorn false no
        return JOptionPane.showConfirmDialog(null,message,"Are you sure?",JOptionPane.YES_NO_OPTION) == 0;
    }
    private void configButton(JButton boto, String normal,String onSelection){
        boto.setBorderPainted(false);
        boto.setBorder(null);
        boto.setFocusable(false);
        boto.setMargin(new Insets(0, 0, 0, 0));
        boto.setContentAreaFilled(false);

        boto.setIcon(new ImageIcon(AssetManager.getImage(onSelection)));
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(onSelection)));
        boto.setRolloverIcon(new ImageIcon(AssetManager.getImage(onSelection)));
        boto.setPressedIcon(new ImageIcon(AssetManager.getImage(normal)));
    }

    public boolean displayErrorConnection() {
        Object[] options1 = {"LogOut", "Quit program"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Server connection lost"));

        return JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(this, panel, "Connection error",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options1, null);
    }
}
