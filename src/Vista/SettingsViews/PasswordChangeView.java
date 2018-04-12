package Vista.SettingsViews;

import Controlador.Controller;
import Vista.View;



import javax.swing.*;
import java.awt.*;



public class PasswordChangeView extends View {
    private JPasswordField jpfNewPassword;
    private JPasswordField jpfConfirmPassword;
    private JButton jbConfirmPassword;
    private JLabel  jlCheckPassword;
    private JProgressBar jpbStrength;

    public  PasswordChangeView(){
        this.setLayout(new BorderLayout());

        jlCheckPassword = new JLabel();
        jpfConfirmPassword = new JPasswordField(20);
        jpfNewPassword = new JPasswordField(20);
        jbConfirmPassword = new JButton("Update Password");


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
        c.gridx = 2;
        c.gridy = 3;
        c.insets = new Insets(0,20,20,0);
        this.jlCheckPassword.setText("");
        jpPasswordChange.add(this.jlCheckPassword, c);

        c = new GridBagConstraints();
        c.insets = new Insets(0,0,20,0);
        c.gridy = 4;
        c.gridx = 1;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        jpbStrength = new JProgressBar();
        jpbStrength.setMinimum(0);
        jpbStrength.setMaximum(10);
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

        this.add(jpPasswordChange, BorderLayout.CENTER);

    }
    @Override
    public void addController(Controller c) {
        jpfConfirmPassword.addKeyListener(c);
        jpfNewPassword.addKeyListener(c);
        jbConfirmPassword.addActionListener(c);


        jpfNewPassword.setName("PASSWORD FIELD CHANGE - NEW PASSWORD ");
        jpfConfirmPassword.setName("PASSWORD FIELD CHANGE - CONFIRM PASSWORD");
        jbConfirmPassword.setName("PASSWORD CHANGE  - CONFIRM PASSWORD");
    }



    public void passwordOK(int strength){
        System.out.println("Password Strength: " + strength);
        setStrength(strength);
        if(strength < 3){
            jlCheckPassword.setForeground(Color.RED);
            jlCheckPassword.setText("Weak Password");
        }else if(strength < 7){
            jlCheckPassword.setForeground(Color.orange);
            jlCheckPassword.setText("Average Password");
        }else{
            jlCheckPassword.setForeground(Color.GREEN);
            jlCheckPassword.setText("Strong Password");
        }


    }

    public void passwordKO(String message){
        jlCheckPassword.setText(message);
        jlCheckPassword.setForeground(Color.RED);
        setStrength(0);
    }

    /** Retorna la contrasenya que es vol escollir i la de confirmació*/
    public boolean getPasswordChangeRequest(){
        String confirmPassword = new String(jpfConfirmPassword.getPassword());
        String newPassword  = new String(jpfNewPassword.getPassword());
        return confirmPassword.equals(newPassword);
    }

    public String getPassword(){
        return new String(jpfConfirmPassword.getPassword());
    }

    private void setStrength(int strength){

        jpbStrength.setValue(strength);
        if(strength < 3){
            jpbStrength.setForeground(Color.red);
        }else if(strength < 7){
            jpbStrength.setForeground(Color.orange);
        }else {
            jpbStrength.setForeground(Color.GREEN);
        }
    }

    public int getStrength(){
        return jpbStrength.getValue();
    }

}
