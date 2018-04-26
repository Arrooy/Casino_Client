package Vista.SettingsViews;

import Controlador.Controller;
import Vista.Tray;
import Vista.View;

import javax.swing.*;
import java.awt.*;

public class Settings extends View {
    private JButton jbChangePassword;
    private JButton jbBack;
    private JButton jbAddMoney;
    private JButton jbWalletEvolution;
    private SettingsView settingsView;

    public Settings(){

        this.setLayout(new BorderLayout());

        JPanel jpDivisor = new JPanel();
        jpDivisor.setOpaque(false);

        jpDivisor.setLayout(new GridLayout(1, 2));

        //Panell de l'esquerra que conté tots els botons
        JPanel jpLeft = new JPanel();
        jpLeft.setOpaque(false);

        jpLeft.setLayout(new BorderLayout());

        this.settingsView = new SettingsView();

        //Panell que té el títol de la pantalla a dalt a la dreta a l
        JPanel jpTitle = new JPanel();
        jpTitle.setOpaque(false);

        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        JLabel jlTitle = new JLabel("SETTINGS");
        jlTitle.setOpaque(false);

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
        jpgblExit.setOpaque(false);

        //Marges
        c.insets = new Insets(0,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jpgblExit.add(jbBack, c);
        JPanel jpExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpExit.setOpaque(false);

        jpExit.add(jpgblExit);
        this.add(jpExit, BorderLayout.SOUTH);

        //Panell que té els botons per accedir a les diferents opcions
        JPanel jpgblBotons = new JPanel(new GridBagLayout());
        jpgblBotons.setOpaque(false);

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
        jpDivisor.add(settingsView, 1);
        this.add(jpDivisor, BorderLayout.CENTER);
        setOpaque(false);
    }

    public SettingsView getSettingsView() {
        return settingsView;
    }

    public void addController(Controller c) {

        this.settingsView.addController(c);

        jbChangePassword.setActionCommand("SETTINGS - changePass");
        jbChangePassword.addActionListener(c);

        jbBack.setActionCommand("SETTINGS - backFromSettings");
        jbBack.addActionListener(c);

        jbAddMoney.setActionCommand("SETTINGS - addMoneyButton");
        jbAddMoney.addActionListener(c);

        jbWalletEvolution.setActionCommand("SETTINGS - walletEvolution");
        jbWalletEvolution.addActionListener(c);

    }
    /** Per tal de fer que la prmera vegada que entris no es mostri nunguna opcio, el métode es crida
     * amb el String "NOTHING"*/
    public void showSetting(String s){
        if(s.equals("NOTHING")){
            this.settingsView.setVisible(false);

        }else{
            this.settingsView.setVisible(true);
            setSetting(s);

        }

    }

    private void setSetting(String s){
        this.settingsView.showSetting(s);
    }


    public WalletEvolutionView getWalletEvolutionView() {
        return settingsView.getWalletEvolutionView();
    }

    public PasswordChangeView getPasswordChangeView() {
        return settingsView.getPasswordChangeView();
    }

    public AddMoneyView getAddMoneyView() {
        return settingsView.getAddMoneyView();
    }
}