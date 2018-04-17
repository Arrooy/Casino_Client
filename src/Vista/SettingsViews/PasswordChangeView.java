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
        jpfConfirmPassword = new IconPasswordField("padlockBad.png",CONFIRM_PASSWORD_HINT,20,TOOL_TIP_CONFIRM_PASSWORD);
        jpfNewPassword = new IconPasswordField("padlockBad.png",NEW_PASSWORD_HINT,20,TOOL_TIP_NEW_PASSWORD);
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
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0, 20, 20);
        jpPasswordChange.add(new JLabel("New Password"), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 2;
        c.insets = new Insets(0,0,20,0);
        c.gridwidth = 3;
        jpfNewPassword.setText("");
        jpPasswordChange.add(this.jpfNewPassword, c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.gridx = 0;
        c.insets = new Insets(0,0,20,20);
        jpPasswordChange.add(new JLabel("Confirm Password"), c);

        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 3;
        c.insets = new Insets(0,0,20,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        jpPasswordChange.add(this.jpfConfirmPassword, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,20,0);
        this.jlCheckPassword.setText("");
        jpGeneric.add(this.jlCheckPassword, c);
        //TODO: com faig que no em canvii la mida del Jlabel i els JPasswordfields depenent del missatge que posi?

        jlStrength = new JLabel("");
        c = new GridBagConstraints();
        c.insets = new Insets(0,0,20,10);
        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 1;
        jpPasswordChange.add(jlStrength, c);


        c = new GridBagConstraints();
        c.insets = new Insets(0,0,20,0);
        c.gridy = 4;
        c.gridx = 1;
        c.ipadx = 100 ;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        jpbStrength = new JProgressBar(0, 40);

        jpbStrength.setValue(0);
        jpbStrength.setForeground(Color.GREEN);
        jpPasswordChange.add(jpbStrength, c);



        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 5;
        c.gridwidth = 1;
        c.insets = new Insets(0,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        jpPasswordChange.add(jbConfirmPassword, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10,0,0,0);
        jpGeneric.add(jpPasswordChange, c);

        this.add(jpGeneric, BorderLayout.CENTER);

    }


    @Override
    public void addController(Controller c) {
        jpfConfirmPassword.addKeyListener(c);
        jpfNewPassword.addKeyListener(c);
        jbConfirmPassword.addActionListener(c);


        jpfNewPassword.setName("PASSWORD FIELD CHANGE - NEW PASSWORD");
        jpfConfirmPassword.setName("PASSWORD FIELD CHANGE - CONFIRM PASSWORD");
        jbConfirmPassword.setActionCommand("PASSWORD CHANGE  - CONFIRM PASSWORD");
    }

    /** Mostra un missatge d'error*/
    @Override
    public void passwordKO(String message){
        jlCheckPassword.setText(message);
        jlCheckPassword.setForeground(Color.RED);


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
            jlStrength.setForeground(new Color(201, 47, 32));
            jlStrength.setText("Weak Password");
            jpbStrength.setForeground(new Color(227, 53, 36));
            jpbStrength.setValue(17);
        }else if(strength < 25){
            jlStrength.setForeground(new Color(223, 159, 51));
            jlStrength.setText("Average Password");
            jpbStrength.setForeground(new Color(237, 175, 67));
            jpbStrength.setValue(27);
        }else{
            jlStrength.setForeground(new Color(47, 127, 45));
            jlStrength.setText("Strong Password");
            jpbStrength.setForeground(new Color(47, 127, 45));
            jpbStrength.setValue(40);
        }

    }
    @Override
    /** Controla la visibilitat del missatge d'error*/
   public void manageError(boolean error){
        if(error){
            this.jlCheckPassword.setVisible(true);
        }else{
            this.jlCheckPassword.setVisible(false);
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
