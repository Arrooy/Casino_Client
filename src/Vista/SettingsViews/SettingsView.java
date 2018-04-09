package model;

import Controlador.Controller;
import Vista.View;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends View {
    private JButton jbChangePassword;
    private JButton jbBack;
    private JButton jbAddMoney;
    private JButton jbWalletEvolution;
    private JPanel  jpRight;
    private JPanel
    private JFrame  jfPasswordChange;

    private JPasswordField jpfNewPassword;
    private JPasswordField jpfConfirmPassword;
    private JButton jbConfirmPassword;
    private JLabel  jlCheckPassword;



    public SettingsView(){
        jlCheckPassword = new JLabel();
        jpfConfirmPassword = new JPasswordField(20);
        jpfNewPassword = new JPasswordField(20);
        jbConfirmPassword = new JButton("Confirm Password");

        this.setLayout(new BorderLayout());

        JPanel jpDivisor = new JPanel();
        jpDivisor.setLayout(new GridLayout(1, 2));

        //Panell de l'esquerra que conté tots els botons
        JPanel jpLeft = new JPanel();
        jpLeft.setLayout(new BorderLayout());

        this.jpRight = new JPanel();
        this.jpRight.setLayout(new CardLayout());

        //Panell que té el títol de la pantalla a dalt a la dreta a l
        JPanel jpTitle = new JPanel();
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel jlTitle = new JLabel("SETTINGS");
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 24));
        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        jbBack = new JButton("BACK");
        jbBack.setFocusable(false);
        jbBack.setPreferredSize(new Dimension(100,30));

        //Panell per col·locar el botó back a la part baixa a l'esquerra
        JPanel jpgblExit = new JPanel(new GridBagLayout());
        //Marges
        c.insets = new Insets(0,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jpgblExit.add(jbBack, c);
        JPanel jpExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpExit.add(jpgblExit);
        this.add(jpExit, BorderLayout.SOUTH);

        //Panell que té els botons per accedir a les diferents opcions
        JPanel jpgblBotons = new JPanel(new GridBagLayout());

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;

        jbChangePassword = new JButton("CHANGE PASSWORD");
        jbChangePassword.setFocusable(false);
        jbAddMoney = new JButton("ADD MONEY");
        jbAddMoney.setFocusable(false);
        jbWalletEvolution = new JButton("WALLET EVOLUTION");
        jbWalletEvolution.setFocusable(false);

        jpgblBotons.add(jbChangePassword, c);

        c.gridy = 1;
        jpgblBotons.add(jbAddMoney, c);

        c.gridy = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbWalletEvolution, c);

        jpLeft.add(jpgblBotons, BorderLayout.CENTER);

        jpDivisor.add(jpLeft, 0);
        jpDivisor.add(jpRight, 1);
        this.add(jpDivisor, BorderLayout.CENTER);

        setChangePasswordView();

    }

    /** Mostrem les opcions per canviar de contrasenya*/
    public void setChangePasswordView(){

        jpRight.setLayout(new BoxLayout(jpRight, BoxLayout.PAGE_AXIS));

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
        c.gridx = 5;
        c.gridy = 1;
        c.insets = new Insets(0,20,20,0);
        this.jlCheckPassword.setText("");
        jpPasswordChange.add(this.jlCheckPassword, c);

        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(0,0,0,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        jpPasswordChange.add(jbConfirmPassword, c);

        jpRight.add(jpPasswordChange, BorderLayout.CENTER);

        setPasswordMatch(false);



    }
    @Override
    public void addController(Controller c) {
        jbChangePassword.setActionCommand("changePass");
        jbChangePassword.addActionListener(c);

        jbBack.setActionCommand("backFromSettings");
        jbBack.addActionListener(c);

        jbAddMoney.setActionCommand("addMoneyButton");
        jbAddMoney.addActionListener(c);

        jbWalletEvolution.setActionCommand("walletEvolution");
        jbWalletEvolution.addActionListener(c);
    }

    /** Mostrem si el canvi de contrasenya s'ha pogut efectuar correctament*/
    public void setPasswordMatch(boolean passwordMatch){
        if(passwordMatch == true){
            jlCheckPassword.setText("Password changed correctly");
            jlCheckPassword.setForeground(Color.GREEN);
        }else{
            jlCheckPassword.setText("Passwords must match");
            jlCheckPassword.setForeground(Color.RED);
            jpfConfirmPassword.setText("");
        }
    }

    /** Retorna la contrasenya que es vol escollir i la de confirmació*/
    public String getPasswordChangeRequest() throws Exception{
        String passwordChangeRequest = null;
        boolean ok = jpfNewPassword.getPassword().toString() == jpfConfirmPassword.getPassword().toString();
        setPasswordMatch(ok);
        if(!ok){
            throw new Exception("Les contrasenyes no son iguals");
        }else{
            passwordChangeRequest = jpfConfirmPassword.getPassword().toString();
        }
        return passwordChangeRequest;
    }
}