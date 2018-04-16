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
    private JLabel jlStrength;
    private JProgressBar jpbStrength;
    private final static char PASSWORD_CHAR = '卐';

    public  PasswordChangeView(){
        this.setLayout(new BorderLayout());

        jlCheckPassword = new JLabel();
        jpfConfirmPassword = new JPasswordField(20);
        jpfNewPassword = new JPasswordField(20);
        jpfConfirmPassword.setMaximumSize(jpfConfirmPassword.getSize());
        jpfNewPassword.setMaximumSize(jpfNewPassword.getSize());
        jpfNewPassword.setEchoChar(PASSWORD_CHAR);
        jpfConfirmPassword.setEchoChar(PASSWORD_CHAR);

        jbConfirmPassword = new JButton("Update Password");
        jbConfirmPassword.setFocusable(false);


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
        c.gridy = 3;
        c.gridwidth = 5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,20,0);
        this.jlCheckPassword.setText("");
        jpPasswordChange.add(this.jlCheckPassword, c);
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
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        jpbStrength = new JProgressBar();

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


        jpfNewPassword.setName("PASSWORD FIELD CHANGE - NEW PASSWORD");
        jpfConfirmPassword.setName("PASSWORD FIELD CHANGE - CONFIRM PASSWORD");
        jbConfirmPassword.setName("PASSWORD CHANGE  - CONFIRM PASSWORD");
    }

    /** Mostra un missatge d'error*/
    public void passwordKO(String message){
        jlCheckPassword.setText(message);
        jlCheckPassword.setForeground(Color.RED);


    }

    /** Retorna la contrasenya que es vol escollir i la de confirmació*/
    public boolean getPasswordChangeRequest(){
        String confirmPassword = new String(jpfConfirmPassword.getPassword());
        String newPassword  = new String(jpfNewPassword.getPassword());
        return confirmPassword.equals(newPassword);
    }



    /** Fixa el valor del JProgress bar
     * Depenent del "strength" actualitza color i text de l'indicador de seguretat*/
    public void setStrength(int strength){
        if(strength < 15){
            jlStrength.setForeground(Color.RED);
            jlStrength.setText("Weak Password");
            jpbStrength.setForeground(Color.red);
            jpbStrength.setValue(17);
        }else if(strength < 25){
            jlStrength.setForeground(Color.orange);
            jlStrength.setText("Average Password");
            jpbStrength.setForeground(Color.orange);
            jpbStrength.setValue(27);
        }else{
            jlStrength.setForeground(Color.GREEN);
            jlStrength.setText("Strong Password");
            jpbStrength.setForeground(Color.GREEN);
            jpbStrength.setValue(40);
        }

    }
    /** Controla la visibilitat del missatge d'error*/
   public void manageError(boolean error){
        if(error){
            this.jlCheckPassword.setVisible(true);
        }else{
            this.jlCheckPassword.setVisible(false);
        }
   }
    /** retorna la contrasenya que l'usuari ha introduit en el camp New Password*/
    public String getNewPassword() {
        return new String(jpfNewPassword.getPassword());
    }

    /** Controla el funcionament del boto que et permet sotmetrela nova contrasenya*/
   public void canConfirm(boolean ok){
        jbConfirmPassword.setEnabled(ok);
   }

    void setTextFit(JLabel label, String text) {
        Font originalFont = (Font)label.getClientProperty("originalfont"); // Get the original Font from client properties
        if (originalFont == null) { // First time we call it: add it
            originalFont = label.getFont();
            label.putClientProperty("originalfont", originalFont);
        }

        int stringWidth = label.getFontMetrics(originalFont).stringWidth(text);
        int componentWidth = label.getWidth();

        if (stringWidth > componentWidth) { // Resize only if needed
            // Find out how much the font can shrink in width.
            double widthRatio = (double)componentWidth / (double)stringWidth;

            int newFontSize = (int)Math.floor(originalFont.getSize() * widthRatio); // Keep the minimum size

            // Set the label's font size to the newly determined size.
            label.setFont(new Font(originalFont.getName(), originalFont.getStyle(), newFontSize));
        } else
            label.setFont(originalFont); // Text fits, do not change font size

        label.setText(text);
    }
}
