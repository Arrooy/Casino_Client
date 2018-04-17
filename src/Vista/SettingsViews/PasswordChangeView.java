package Vista.SettingsViews;

import Controlador.Controller;
import Vista.SwingModifications.IconPasswordField;
import Vista.PasswordConfirm;
import Vista.View;



import javax.swing.*;
import java.awt.*;



public class PasswordChangeView extends View implements PasswordConfirm{

    private static final String TOOL_TIP_NEW_PASSWORD = "Your new password";
    private static final String TOOL_TIP_CONFIRM_PASSWORD = "Repeat the same password";

    private static final String NEW_PASSWORD_HINT = "New password";
    private static final String CONFIRM_PASSWORD_HINT = "Confirm password";

    private final static char PASSWORD_CHAR = '*';

    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color VERD = new Color(25, 151, 6);
    private final static Color GROC = new Color(237, 175, 67);
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    private IconPasswordField jpfNewPassword;
    private IconPasswordField jpfConfirmPassword;
    private JButton jbConfirmPassword;
    private JLabel  jlCheckPassword;
    private JLabel jlStrength;
    private JProgressBar jpbStrength;


    public  PasswordChangeView(){
        this.setLayout(new BorderLayout());

        JPanel jpGeneric = new JPanel(new GridBagLayout());

        jlCheckPassword = new JLabel();
        jpfConfirmPassword = new IconPasswordField("padlock.png",CONFIRM_PASSWORD_HINT,20,TOOL_TIP_CONFIRM_PASSWORD);
        jpfNewPassword = new IconPasswordField("padlock.png",NEW_PASSWORD_HINT,20,TOOL_TIP_NEW_PASSWORD);
        jpfConfirmPassword.setMaximumSize(jpfConfirmPassword.getSize());
        jpfNewPassword.setMaximumSize(jpfNewPassword.getSize());
        jpfNewPassword.setEchoChar(PASSWORD_CHAR);
        jpfConfirmPassword.setEchoChar(PASSWORD_CHAR);

        jbConfirmPassword = new JButton("Update Password");
        jbConfirmPassword.setFocusable(false);
        jbConfirmPassword.setEnabled(false);


        JPanel jpPasswordChange = new JPanel();
        jpPasswordChange.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(0,0,20,0);
        jpfNewPassword.setText("");
        jpPasswordChange.add(this.jpfNewPassword, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0,0,20,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        jpPasswordChange.add(this.jpfConfirmPassword, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,20,0);
        this.jlCheckPassword.setText("Check");
        this.jlCheckPassword.setPreferredSize(new Dimension(300,15));
        this.jlCheckPassword.setForeground(TRANSPARENT);
        jpGeneric.add(this.jlCheckPassword, c);
        //TODO: com faig que no em canvii la mida del Jlabel i els JPasswordfields depenent del missatge que posi?

        jlStrength = new JLabel("Strength");
        jlStrength.setForeground(TRANSPARENT);
        jlStrength.setPreferredSize(new Dimension(150,15));
        c = new GridBagConstraints();
        c.insets = new Insets(0,10,20,0);
        c.gridy = 2;
        c.gridx = 1;
        c.gridwidth = 1;
        jpPasswordChange.add(jlStrength, c);

        c = new GridBagConstraints();
        c.insets = new Insets(0,0,20,0);
        c.gridy = 2;
        c.gridx = 0;
        c.ipadx = 100 ;
        c.fill = GridBagConstraints.HORIZONTAL;
        jpbStrength = new JProgressBar(0, 40);

        jpbStrength.setValue(0);
        jpbStrength.setForeground(Color.GREEN);
        jpPasswordChange.add(jpbStrength, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0,0,0,0);
        jpPasswordChange.add(jbConfirmPassword, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0,0,0,0);
        jpGeneric.add(jpPasswordChange, c);

        this.add(jpGeneric, BorderLayout.CENTER);
    }


    @Override
    public void addController(Controller c) {
        jpfConfirmPassword.addKeyListener(c);
        jpfNewPassword.addKeyListener(c);

        jbConfirmPassword.addActionListener(c);

        jpfConfirmPassword.addFocusListener(c);
        jpfNewPassword.addFocusListener(c);

        jpfNewPassword.setName("PASSWORD FIELD CHANGE - NEW PASSWORD");
        jpfConfirmPassword.setName("PASSWORD FIELD CHANGE - CONFIRM PASSWORD");
        jbConfirmPassword.setActionCommand("PASSWORD CHANGE  - CONFIRM PASSWORD");
    }

    /** Mostra un missatge d'error*/
    @Override
    public void passwordKO(String message){
        jlCheckPassword.setText(message);
        jlCheckPassword.setForeground(GRANA);
    }

    @Override
    /** Retorna la contrasenya que es vol escollir i la de confirmació*/
    public boolean getPasswordChangeRequest(){
        String confirmPassword = new String(jpfConfirmPassword.getPassword());
        System.out.println("Confirm: " + confirmPassword);
        String newPassword  = new String(jpfNewPassword.getPassword());
        return confirmPassword.equals(newPassword);
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
            this.jlCheckPassword.setForeground(GRANA);
        }else{
            this.jlCheckPassword.setForeground(TRANSPARENT);
        }
   }
    @Override
    /** retorna la contrasenya que l'usuari ha introduit en el camp New Password*/
    public String getNewPassword() {
        return new String(jpfNewPassword.getPassword());
    }

    @Override
    /** Controla el funcionament del boto que et permet sotmetrela nova contrasenya*/
   public void canConfirm(boolean ok) {
       jbConfirmPassword.setEnabled(ok);
       //Es modifica l'icono de la PasswordField per a indicar error
       if (ok){
           jpfNewPassword.setIcon("padlockGood.png");
           jpfConfirmPassword.setIcon("padlockGood.png");
       }else {
           jpfNewPassword.setIcon("padlockBad.png");
           jpfConfirmPassword.setIcon("padlockBad.png");
       }
   }

    public void clearFields(){
       jpfNewPassword.setText("");
       jpfConfirmPassword.setText("");
       jlCheckPassword.setText("");
       jlStrength.setText("");
       jpbStrength.setValue(0);
    }
}
