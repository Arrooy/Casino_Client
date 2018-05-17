package Vista;

import Controlador.Controller;
import Model.AssetManager;
import Vista.SwingModifications.IconPasswordField;
import Vista.SwingModifications.IconTextField;

import javax.swing.*;
import java.awt.*;

/**Classe que crea la vista per fer LogIn*/
public class LogInView extends View {

    /**String constant per indicar que el camp s'ha d'omplir amb el nom d'usuari*/
    private final static String TOOLTIP_USERNAME_LOGIN = "Username field";

    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private final static String TOOLTIP_PASSWORD_LOGIN = "Password field";

    /**String constant per indicar que el camps s'ha d'omplir amb el nom d'usuari*/
    private static final String USERNAME_HINT = "Username";

    /**String constant per indicar que el camps s'ha d'omplir amb la contrasenya*/
    private static final String PASSWORD_HINT = "Password";

    /**Caracter constant que es mostra quan s'escriu la contrasenya enlloc dels caracters que l'usuari esta escribint*/
    private final static char PASSWORD_CHAR = '*';

    /**Color Crema*/
    private final static Color CREMA = new Color (218, 204, 164);

    /**Color Grana*/
    private final static Color GRANA = new Color(125, 28, 37);

    /**Color Transparent*/
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    /**Boto que quan es pitja indica que l'usuari vol fer logIn*/
    private JButton jbAccept;

    /**Nom d'usuari*/
    private IconTextField jtfUsername;

    /**Contrasenya de l'usuari*/
    private IconPasswordField jpfPassword;

    /**Chech que indica si l'usuari es vol iniciar sessio automaticament o no*/
    private JCheckBox jcbRememberLogIn;

    /**Boto per tornar al menu*/
    private JButton jbBack;

    /**Missatge d'error en cas d'introduir malament les dades*/
    private JLabel jlErrorMessage;

    /**Constructor de la vista del LogIn que defineix on i com es col·loquen els elements*/
    public LogInView(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        //Label missatge error
        jlErrorMessage = new JLabel("Error");
        jlErrorMessage.setHorizontalAlignment(JLabel.CENTER);

        //Panell per col·locar el boto back a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        jpgblBack.setOpaque(false);

        //Marges del boto
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;

        //Creació del boto
        jbBack = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbBack,"BACK_NO_SOMBRA.png","BACK_SOMBRA.png");
        jpgblBack.add(jbBack, c);

        //Flow Layout per a que el boto quedi a l'esquerra
        JPanel jpBack = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpBack.add(jpgblBack);
        jpBack.setOpaque(false);

        //Es col·loca el boto a la part inferior de la pantalla
        this.add(jpBack, BorderLayout.SOUTH);

        //Panell que te el titol de la pantalla a dalt a la dreta al mig
        JPanel jpTitle = new JPanel();
        jpTitle.setOpaque(false);
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setOpaque(false);
        JLabel jlTitle = new JLabel("Log In");

        //Es canvia la font i el color del titol
        jlTitle.setForeground(CREMA);
        jlTitle.setFont(AssetManager.getEFont(100));

        //Marges del titol
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els camps d'UserName, Password i l'opcio de Remember me centrats al mig de la pantalla
        JPanel jpgblInfo = new JPanel(new GridBagLayout());
        jpgblInfo.setOpaque(false);

        //Creacio del check per fer el remember me
        jcbRememberLogIn = new JCheckBox("Remember user");
        jcbRememberLogIn.setOpaque(false);
        jcbRememberLogIn.setFocusable(false);

        //Es canvia la font i el color
        jcbRememberLogIn.setForeground(CREMA);
        jcbRememberLogIn.setFont(AssetManager.getEFont(15));

        //S'afegeix el remember me
        c.gridy = 3;
        c.gridx = 0;
        c.insets = new Insets(0,0,0,0);
        jpgblInfo.add(jcbRememberLogIn, c);

        //S'afegeixen els camps per omplir
        jtfUsername = new IconTextField("user.png",USERNAME_HINT,TOOLTIP_USERNAME_LOGIN);
        jtfUsername.requestFocus();
        jpfPassword = new IconPasswordField("padlock.png",PASSWORD_HINT,TOOLTIP_PASSWORD_LOGIN);
        jpfPassword.setEchoChar(PASSWORD_CHAR);

        //Posicio camp username
        c.gridy = 1;
        c.gridx = 0;
        c.insets = new Insets(0,0,20,0);
        c.ipadx = 200;
        jpgblInfo.add(jtfUsername, c);

        //Posicio camp contrasenya
        c.gridy = 2;
        jpgblInfo.add(jpfPassword, c);

        //S'afegeix el boto per acceptar la info introduida
        jbAccept = new JButton();

        //Es dexineixen les imatges corresponents a aquest boto
        configButton(jbAccept,"ACCEPT_NO_SOMBRA.png","ACCEPT_SOMBRA.png");

        //Posicio boto acceptar
        c.gridy = 4;
        c.gridx = 0;
        c.insets = new Insets(20,0,0,0);
        jpgblInfo.add(jbAccept, c);

        //Panell que conte centrats els camps, els botons i el missatge d'error
        JPanel jpGeneric = new JPanel(new GridBagLayout());
        jpGeneric.setOpaque(false);
        c.gridy = 1;
        c.gridx = 0;
        c.insets = new Insets(0,0,0,0);
        jpGeneric.add(jpgblInfo, c);

        //S'afegeix el missatge d'error
        c.insets = new Insets(0,0,20,0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 0;
        c.gridx = 0;

        //Es posa el missatge transparent ja que no es vol visualitzar a l'inici
        jlErrorMessage.setForeground(TRANSPARENT);
        jpGeneric.add(jlErrorMessage, c);

        //S'afegeixen aquests elements al centre de la pantalla
        add(jpGeneric, BorderLayout.CENTER);
        setOpaque(false);
    }

    /**
     * Metode que posa el missatge d'error que es passa per parametres visible
     * @param error String amb el missatge d'error que es vol mostrar
     * */
    public void setError(String error) {
        jlErrorMessage.setText(error);
        jlErrorMessage.setForeground(GRANA);
    }

    /**
     * Metode override que relaciona cada element amb el seu Action Listener
     * @param c Controlador
     * */
    @Override
    public void addController(Controller c) {
        jbAccept.setActionCommand("logIn");
        jbAccept.addActionListener(c);

        jpfPassword.setActionCommand("logIn");
        jpfPassword.addActionListener(c);
        jpfPassword.addFocusListener(c);

        jtfUsername.addFocusListener(c);

        jbBack.setActionCommand("backToMain");
        jbBack.addActionListener(c);
    }

    /**Metode que s'encarrega de netejar tots els camps*/
    public void clearFields() {
        jpfPassword.setText("");
        jpfPassword.setHint(true);
        jtfUsername.setText("");
        jtfUsername.setHint(true);
        jcbRememberLogIn.setSelected(false);
        jlErrorMessage.setForeground(TRANSPARENT);
    }

    /**Metode que configura les imatges d'un boto per quan no esta apretat i per quan si que ho esta*/
    private void configButton(JButton boto, String normal,String onSelection){
        boto.setBorderPainted(false);
        boto.setBorder(null);
        boto.setFocusable(false);
        boto.setMargin(new Insets(0, 0, 0, 0));
        boto.setContentAreaFilled(false);
        boto.setIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setRolloverIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setPressedIcon(new ImageIcon(AssetManager.getImage(onSelection)));
    }

    /**Getters*/
    public String getUsername() {
        return jtfUsername.getText();
    }

    public String getPassword() {
        return String.valueOf(jpfPassword.getPassword());
    }

    public boolean getRememberLogIn() {
        return jcbRememberLogIn.isSelected();
    }
}
