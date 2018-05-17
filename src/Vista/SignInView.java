package Vista;

import Controlador.Controller;
import Model.AssetManager;
import Vista.SwingModifications.IconPasswordField;
import Vista.SwingModifications.IconTextField;

import javax.swing.*;
import java.awt.*;

/**Classe que crea la vista per fer SignIn*/
public class SignInView extends View implements PasswordConfirm {

    /**String constant per indicar que el camp s'ha d'omplir amb el nom d'usuari*/
    private static final String USERNAME_HINT = "New username";

    /**String constant per indicar que el camp s'ha d'omplir amb el nom d'usuari*/
    private static final String USERNAME_TOOL_TIP = "Your new username";

    /**String constant per indicar que el camp s'ha d'omplir amb l'email de l'usuari*/
    private static final String EMAIL_HINT = "New email";

    /**String constant per indicar que el camp s'ha d'omplir amb l'email de l'usuari*/
    private static final String EMAIL_TOOL_TIP = "Your new email name";

    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private static final String PASSWORD_HINT = "New password";

    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private static final String PASSWORD_TOOL_TIP = "Your new password";

    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private static final String PASSWORD_CONFIRM_HINT = "Repeat your password here";

    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private static final String PASSWORD_CONFIRM_TOOL_TIP = "Repeat the password introduced above";

    /**Color Crema*/
    private final static Color CREMA = new Color (218, 204, 164);

    /**Color Grana*/
    private final static Color GRANA = new Color(125, 28, 37);

    /**Color Verd*/
    private final static Color VERD = new Color(40, 73, 7);

    /**Color Groc*/
    private final static Color GROC = new Color(237, 175, 67);

    /**Color Transparent*/
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    /**Boto per tornar a l'inici*/
    private JButton jbBack;

    /**Nom d'usuari*/
    private IconTextField jtfName;

    /**E-mail de l'usuari*/
    private IconTextField jtfEmail;

    /**Contrasenya de l'usuari*/
    private IconPasswordField jpfPassword;

    /**Confirmacio de la contrasenya*/
    private IconPasswordField jpfConfirmPassword;

    /**Boto que quan es pitja indica que l'usuari vol fer SignIn*/
    private JButton jbAccept;

    /**Indica la fortalesa de la contrasenya*/
    private JLabel jlStrength;

    /**Indica de manera mes visual la fortalesa de la contrasenya*/
    private JProgressBar jpbStrength;

    /**Missatge d'error*/
    private JLabel jlErrorMessage;

    /**Constructor de la vista del SignIn que defineix on i com es col·loquen els elements*/
    public SignInView(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        //Label missatge error
        jlErrorMessage = new JLabel("Error Message");
        jlErrorMessage.setHorizontalAlignment(JLabel.CENTER);

        //Es posa el missatge transparent ja que no es vol visualitzar a l'inici
        jlErrorMessage.setForeground(TRANSPARENT);

        //Panell per col·locar el boto back a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        jpgblBack.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();

        //Marges del boto
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;

        //Creacio del boto
        jbBack = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbBack,"BACK_NO_SOMBRA.png","BACK_SOMBRA.png");
        jpgblBack.add(jbBack, c);

        //Flow Layout per a que el boto quedi a l'esquerra
        JPanel jpBack = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpBack.setOpaque(false);

        //S'afegeix el boto a la part inferior de la pantalla
        jpBack.add(jpgblBack);
        this.add(jpBack, BorderLayout.SOUTH);

        //Panell que te el titol de la pantalla a dalt a la dreta al mig
        JPanel jpTitle = new JPanel();
        jpTitle.setOpaque(false);
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setOpaque(false);
        JLabel jlTitle = new JLabel("Sign In");

        //Es canvia la font i el color del titol
        jlTitle.setFont(AssetManager.getEFont(100));
        jlTitle.setForeground(CREMA);

        //Marges del titol
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els camps d'UserName, email, Password i confirmar Password centrats al mig de la pantalla
        JPanel jpgblInfo = new JPanel(new GridBagLayout());
        jpgblInfo.setOpaque(false);

        //S'afegeixen els camps per omplir
        jtfName = new IconTextField("user.png",USERNAME_HINT,USERNAME_TOOL_TIP);
        jtfEmail = new IconTextField("email.png",EMAIL_HINT,EMAIL_TOOL_TIP);
        jpfPassword = new IconPasswordField("padlock.png",PASSWORD_HINT,20,PASSWORD_TOOL_TIP);
        jpfConfirmPassword = new IconPasswordField("padlock.png",PASSWORD_CONFIRM_HINT,20,PASSWORD_CONFIRM_TOOL_TIP);

        //Posicio camp username
        c.insets = new Insets(0,130,20,0);
        c.gridy = 0;
        c.gridx = 0;
        c.ipadx = 100;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        jpgblInfo.add(jtfName, c);

        //Posicio camp email
        c.gridy = 1;
        jpgblInfo.add(jtfEmail, c);

        //Posicio camp contrasenya
        c.gridy = 2;
        jpgblInfo.add(jpfPassword, c);

        //Posicio camp repetir contrasenya
        c.gridy = 3;
        jpgblInfo.add(jpfConfirmPassword, c);

        //S'afegeix el boto per acceptar la informacio introduida
        jbAccept = new JButton();

        //Es dexineixen les imatges corresponents a aquest boto
        configButton(jbAccept,"ACCEPT_NO_SOMBRA.png","ACCEPT_SOMBRA.png","ACCEPT_DIS.png");
        jbAccept.setEnabled(false);

        //Posicio missatge fortalesa de la contrasenya
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 4;
        this.jlStrength = new JLabel("Average Password");
        jlStrength.setForeground(TRANSPARENT);
        c.insets = new Insets(0,20,20,0);
        jpgblInfo.add(jlStrength, c);

        //Posicio indicador fortalesa de la contrasenya
        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 0;
        c.ipadx = 100;
        c.gridwidth = 3;
        c.insets = new Insets(0,130,20,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        this.jpbStrength = new JProgressBar();
        this.jpbStrength.setMinimum(0);
        this.jpbStrength.setMaximum(40);
        jpgblInfo.add(jpbStrength, c);

        //Posicio boto acceptar
        c.gridy = 6;
        c.gridx = 1;
        c.gridwidth = 2;
        c.insets = new Insets(20,120,0,0);
        jpgblInfo.add(jbAccept, c);

        //Panell que conte centrats els camps, els botons i el missatge d'error
        JPanel jpGeneric = new JPanel(new GridBagLayout());
        jpGeneric.setOpaque(false);
        c = new GridBagConstraints();

        //S'afegeix el missatge d'error
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,20,0);
        jpGeneric.add(jlErrorMessage, c);

        //S'afegeixen els camps, els indicadors de fortalesa i el boto
        c.gridy = 1;
        c.gridx = 1;
        c.gridwidth = 1;
        c.insets = new Insets(0,0,0,0);
        c.anchor = GridBagConstraints.CENTER;
        jpGeneric.add(jpgblInfo, c);

        //S'afegeixen aquests elements al centre de la pantalla
        add(jpGeneric, BorderLayout.CENTER);
        setOpaque(false);
    }

    /**
     * Metode override que relaciona cada element amb el seu Action Listener
     * @param c Controlador
     * */
    @Override
    public void addController(Controller c) {
        jbBack.setActionCommand("backToMain");
        jbBack.addActionListener(c);

        this.jtfEmail.addFocusListener(c);
        this.jtfName.addFocusListener(c);

        this.jpfConfirmPassword.setName("SIGNIN - PASSWORD CONFIRM FIELD");
        this.jpfConfirmPassword.addKeyListener(c);
        this.jpfConfirmPassword.addFocusListener(c);

        this.jpfPassword.setName("SIGNIN - PASSWORD FIELD");
        this.jpfPassword.addKeyListener(c);
        this.jpfPassword.addFocusListener(c);

        jbAccept.setActionCommand("acceptSignIn");
        jbAccept.addActionListener(c);
    }

    /**Metode que s'encarrega de netejar tots els camps*/
    public void clearFields() {
        jtfName.setText("");
        jtfName.setHint(true);
        jtfEmail.setText("");
        jtfEmail.setHint(true);
        jpfPassword.setText("");
        jpfConfirmPassword.setHint(true);
        jlErrorMessage.setForeground(TRANSPARENT);
        jpfConfirmPassword.setText("");
        jpbStrength.setValue(0);
        jlStrength.setForeground(TRANSPARENT);
    }

    /**
     * Metode que indica si la contrasenya i la confirmacio de la contrasenya son iguals
     * @return boolean que indica si son iguals (true) o no (false)
     * */
    @Override
    public boolean getPasswordChangeRequest() {
        return getPassword().equals(getConfirmation());
    }

    /** Metode que mostra un missatge d'error*/
    @Override
    public void passwordKO(String message){
        jlErrorMessage.setText(message);
        jlErrorMessage.setForeground(GRANA);
    }

    /** Metode que fixa el valor del JProgress bar
     * Depenent del "strength" actualitza color i text de l'indicador de seguretat*/
    @Override
    public void setStrength(int strength){
        if(strength < 15){
            jlStrength.setForeground(GRANA);
            jlStrength.setText("Weak Password    ");
            jpbStrength.setForeground(GRANA);
            jpbStrength.setValue(17);
        }else if(strength < 25){
            jlStrength.setForeground(GROC);
            jlStrength.setText("Average Password");
            jpbStrength.setForeground(GROC);
            jpbStrength.setValue(27);
        }else{
            jlStrength.setForeground(VERD);
            jlStrength.setText("Strong Password  ");
            jpbStrength.setForeground(VERD);
            jpbStrength.setValue(40);
        }

    }

    /** Controla la visibilitat del missatge d'error*/
    @Override
    public void manageError(boolean error){
        if(error){
            this.jlErrorMessage.setForeground(GRANA);
        }else{
            this.jlErrorMessage.setForeground(TRANSPARENT);
        }
    }

    /**@return la contrasenya que l'usuari ha introduit en el camp New Password*/
    @Override
    public String getNewPassword() {
        return new String(jpfPassword.getPassword());
    }

    /** Controla el funcionament del boto que et permet sotmetre la nova contrasenya*/
    @Override
    public void canConfirm(boolean ok){
        jbAccept.setEnabled(ok);
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

    /**Ampliacio del configButton per a poder concretar l'imatge del boto disabled*/
    private void configButton(JButton boto, String normal,String onSelection, String disabled){
        configButton(boto,normal,onSelection);
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(disabled)));
    }

    /**Getters*/
    public String getUsername() {
        return jtfName.getText();
    }

    public String getPassword() {
        return String.valueOf(jpfPassword.getPassword());
    }

    public String getConfirmation() {
        return String.valueOf(jpfConfirmPassword.getPassword());
    }

    public String getMail() {
        return jtfEmail.getText();
    }

    public String getEmail(){
        return jtfEmail.getText();
    }
}
