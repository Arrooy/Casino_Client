package Vista;

import Controlador.Controller;
import Vista.SwingModifications.IconPasswordField;
import Vista.SwingModifications.IconTextField;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;

public class SignInView extends View implements PasswordConfirm {

    private static final String USERNAME_HINT = "New username";
    private static final String USERNAME_TOOL_TIP = "Your new username";

    private static final String EMAIL_HINT = "New email";
    private static final String EMAIL_TOOL_TIP = "Your new email name";

    private static final String PASSWORD_HINT = "New password";
    private static final String PASSWORD_TOOL_TIP = "Your new password";

    private static final String PASSWORD_CONFIRM_HINT = "Repeat your password here";
    private static final String PASSWORD_CONFIRM_TOOL_TIP = "Repeat the password introduced above";

    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color VERD = new Color(25, 151, 6);
    private final static Color GROC = new Color(237, 175, 67);
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    private JButton jbBack;
    private IconTextField jtfName;
    private IconTextField jtfEmail;

    private IconPasswordField jpfPassword;
    private IconPasswordField jpfConfirmPassword;

    private JButton jbAccept;
    private JLabel jlStrength;
    private JProgressBar jpbStrength;
    private JLabel jlErrorMessage;

    public SignInView(){

        this.setLayout(new BorderLayout());

        //Label missatge error
        JPanel jpGeneric = new JPanel(new GridBagLayout());
        jpGeneric.setOpaque(false);

        jlErrorMessage = new JLabel("");
        jlErrorMessage.setHorizontalAlignment(JLabel.CENTER);

        //Panell per col·locar el botó back a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        jpgblBack.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(20,20,0,0);
        c.fill = GridBagConstraints.BOTH;
        jbBack = new JButton("BACK");
        jbBack.setFocusable(false);
        jpgblBack.add(jbBack, c);
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

        JLabel jlTitle = new JLabel("Sign In");
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 100));
        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els camps d'UserName, email, Password i confirmar Password centrats al mig de la pantalla
        JPanel jpgblInfo = new JPanel(new GridBagLayout());
        jpgblInfo.setOpaque(false);

        c.insets = new Insets(0,0,20,10);
        JLabel jlName = new JLabel("UserName:");
        JLabel jlEmail = new JLabel("e-mail:");
        JLabel jlPassword = new JLabel("Password:");
        JLabel jlConfirmPassword = new JLabel("Confirm Password:");

        //S'afegeixen les etiquetes
        c.gridy = 0;
        jpgblInfo.add(jlName, c);

        c.gridy = 1;
        jpgblInfo.add(jlEmail, c);

        c.gridy = 2;
        jpgblInfo.add(jlPassword, c);

        c.gridy = 3;
        c.insets = new Insets(0,0,20,10);
        jpgblInfo.add(jlConfirmPassword, c);

        //S'afegeixen els camps per omplir
        jtfName = new IconTextField("user.png",USERNAME_HINT,USERNAME_TOOL_TIP);
        jtfEmail = new IconTextField("email.png",EMAIL_HINT,EMAIL_TOOL_TIP);
        jpfPassword = new IconPasswordField("padlock.png",PASSWORD_HINT,20,PASSWORD_TOOL_TIP);
        jpfConfirmPassword = new IconPasswordField("padlock.png",PASSWORD_CONFIRM_HINT,20,PASSWORD_CONFIRM_TOOL_TIP);

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.gridx = 1;
        c.ipadx = 100;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;

        jpgblInfo.add(jtfName, c);

        c.gridy = 1;
        jpgblInfo.add(jtfEmail, c);

        c.gridy = 2;
        jpgblInfo.add(jpfPassword, c);

        c.gridy = 3;
        c.insets = new Insets(0,0,20,0);
        jpgblInfo.add(jpfConfirmPassword, c);

        //S'afegeix el botó per acceptar la info introduida
        jbAccept = new JButton("Accept");
        jbAccept.setFocusable(false);
        jbAccept.setEnabled(false);

        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 0;

        this.jlStrength = new JLabel("");
        c.insets = new Insets(0,0,20,10);
        jpgblInfo.add(jlStrength, c);

        c = new GridBagConstraints();
        c.gridy = 4;
        c.gridx = 1;
        c.ipadx = 100;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,20,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        this.jpbStrength = new JProgressBar();
        this.jpbStrength.setMinimum(0);
        this.jpbStrength.setMaximum(40);
        jpgblInfo.add(jpbStrength, c);

        c.gridy = 6;
        c.gridx = 1;
        c.gridwidth = 1;
        c.insets = new Insets(20,0,0,0);
        jpgblInfo.add(jbAccept, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,20,0);
        jpGeneric.add(jlErrorMessage, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.insets = new Insets(0,0,0,0);
        c.anchor = GridBagConstraints.CENTER;
        jpGeneric.add(jpgblInfo, c);

        add(jpGeneric, BorderLayout.CENTER);
        setOpaque(false);
    }



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
    }

    @Override
    public boolean getPasswordChangeRequest() {
        return getPassword().equals(getConfirmation());
    }

    @Override
    /** Mostra un missatge d'error*/
    public void passwordKO(String message){
        jlErrorMessage.setText(message);
        jlErrorMessage.setForeground(GRANA);
    }

    @Override
    /** Fixa el valor del JProgress bar
     * Depenent del "strength" actualitza color i text de l'indicador de seguretat*/
    public void setStrength(int strength){
        if(strength < 15){
            jlStrength.setForeground(GRANA);
            jlStrength.setText("Weak Password");
            jpbStrength.setForeground(GRANA);
            jpbStrength.setValue(17);
        }else if(strength < 25){
            jlStrength.setForeground(GROC);
            jlStrength.setText("Average Password");
            jpbStrength.setForeground(GROC);
            jpbStrength.setValue(27);
        }else{
            jlStrength.setForeground(VERD);
            jlStrength.setText("Strong Password");
            jpbStrength.setForeground(VERD);
            jpbStrength.setValue(40);
        }

    }
    @Override
    /** Controla la visibilitat del missatge d'error*/
    public void manageError(boolean error){
        if(error){
            this.jlErrorMessage.setForeground(GRANA);
        }else{
            this.jlErrorMessage.setForeground(TRANSPARENT);
        }
    }
    @Override
    /** retorna la contrasenya que l'usuari ha introduit en el camp New Password*/
    public String getNewPassword() {
        return new String(jpfPassword.getPassword());
    }

    @Override
    /** Controla el funcionament del boto que et permet sotmetrela nova contrasenya*/
    public void canConfirm(boolean ok){
        jbAccept.setEnabled(ok);
    }

    public String getEmail(){
        return jtfEmail.getText();
    }
}
