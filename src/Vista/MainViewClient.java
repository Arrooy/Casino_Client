package Vista;

import Controlador.Controller;
import Model.AssetManager;

import javax.swing.*;
import java.awt.*;

/**Classe que crea la pantalla d'inici del joc*/
public class MainViewClient extends View{

    /**Boto per iniciar sessio*/
    private JButton logInButton;

    /**Boto per crear un compte*/
    private JButton jbSignIn;

    /**Boto per entrar com a convidat*/
    private JButton jbGuest;

    /** Crea la vista del client amb una amplada i una alçada determinades per width i height*/
    public MainViewClient(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        GridBagConstraints c = new GridBagConstraints();

        //Panell que te els botons per iniciar el joc amb el mode desitjat
        JPanel jpgblBotons = new JPanel(new GridBagLayout());

        //Es crea el boto logIn i se'n configuren les imatges associades segons l'estat del boto
        logInButton = new JButton();
        configButton(logInButton,"LOGIN_SOMBRA.png","LOGIN_NO_SOMBRA.png");

        //Es crea el boto signIn i se'n configuren les imatges associades segons l'estat del boto
        jbSignIn = new JButton();
        configButton(jbSignIn,"SIGNIN_SOMBRA.png","SIGNIN_NO_SOMBRA.png");

        //Es crea el boto guest i se'n configuren les imatges associades segons l'estat del boto
        jbGuest = new JButton();
        configButton(jbGuest,"GUEST_SOMBRA.png","GUEST_NO_SOMBRA.png");

        //Posicio logIn
        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        jpgblBotons.add(logInButton, c);

        //Posicio signIn
        c.gridy = 1;
        jpgblBotons.add(jbSignIn, c);

        //Posicio guest
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

    /**Metode que configura les imatges d'un boto per quan no esta apretat i per quan si que ho esta*/
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

    /**Metode que obre un panell que mostra un missatge d'error de connexio amb el servidor*/
    public boolean displayErrorConnection() {
        Object[] options1 = {"LogOut", "Quit program"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Server connection lost"));

        return JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(this, panel, "Connection error",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options1, null);
    }
}
