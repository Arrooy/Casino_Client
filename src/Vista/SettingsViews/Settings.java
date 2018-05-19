package Vista.SettingsViews;

import Controlador.Controller;
import Model.AssetManager;
import Model.WalletEvolutionMessage;
import Vista.View;

import javax.swing.*;
import java.awt.*;

/**Classe que crea el panell que conte els botons per triar quina de les diferents funcions dels settings li interessa a l'usuari*/
public class Settings extends View {
    /**Boto per anar a la funcio de canviar la contrasenya*/
    private JButton jbChangePassword;

    /**Boto per tornar al menu de jocs*/
    private JButton jbBack;

    /**Boto per anar a la funcio d'afegir diners*/
    private JButton jbAddMoney;

    /**Boto per anar a la funcio de visualitzar l'evolucio monetaria de l'usuari*/
    private JButton jbWalletEvolution;

    /**Panell amb CardLayout que conte els panells amb les diferents funcions dels settings*/
    private SettingsView settingsView;

    /**Color Crema*/
    private final static Color CREMA = new Color (218, 204, 164);

    /**Constructor de la classe que col·loca els botons per a escollir la funcio a
     * l'esquerra i els panells que contenen aquestes funcions a la dreta*/
    public Settings(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        //Panell que divideix el panell entre dreta i esquerra
        JPanel jpDivisor = new JPanel();
        jpDivisor.setOpaque(false);
        jpDivisor.setLayout(new GridLayout(1, 2));

        //Panell de l'esquerra que conté tots els botons
        JPanel jpLeft = new JPanel();
        jpLeft.setOpaque(false);
        jpLeft.setLayout(new BorderLayout());

        this.settingsView = new SettingsView();

        //Panell que te el titol de la pantalla a dalt a la dreta
        JPanel jpTitle = new JPanel();
        jpTitle.setOpaque(false);

        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        jpgblTitle.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        JLabel jlTitle = new JLabel("SETTINGS");
        jlTitle.setOpaque(false);

        //Es canvia la font i color del titol
        jlTitle.setFont(AssetManager.getEFont(100));
        jlTitle.setForeground(CREMA);

        //Marges del titol
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Creacio del boto back
        jbBack = new JButton();

        //Es dexineixen les imatges corresponents a aquest boto
        configButton(jbBack,"BACK_NO_SOMBRA.png","BACK_SOMBRA.png");

        //Panell per col·locar el boto back a la part baixa a l'esquerra
        JPanel jpgblExit = new JPanel(new GridBagLayout());
        jpgblExit.setOpaque(false);

        //Marges del boto
        c.insets = new Insets(0,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jpgblExit.add(jbBack, c);
        JPanel jpExit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jpExit.setOpaque(false);

        jpExit.add(jpgblExit);
        this.add(jpExit, BorderLayout.SOUTH);

        //Panell que te els botons per accedir a les diferents opcions
        JPanel jpgblBotons = new JPanel(new GridBagLayout());
        jpgblBotons.setOpaque(false);

        //Creacio dels diferents botons i associacio dels botons amb les imatges corresponents a cada boto
        jbChangePassword = new JButton();
        configButton(jbChangePassword, "CP.png", "CPS.png");
        jbAddMoney = new JButton();
        configButton(jbAddMoney, "ADDMONEY_NO_SOMBRA.png", "ADDMONEY_SOMBRA.png");
        jbWalletEvolution = new JButton();
        configButton(jbWalletEvolution, "WE.png", "WES.png");

        //Posicio boto opcio canvi contrasenya
        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        jpgblBotons.add(jbChangePassword, c);

        //Posicio boto opcio afegir diners
        c.gridy = 1;
        jpgblBotons.add(jbAddMoney, c);

        //Posicio boto opcio visualitzar taula evolucio monetaria
        c.gridy = 2;
        c.insets = new Insets(0,0,0,0);
        jpgblBotons.add(jbWalletEvolution, c);

        //S'afegeixen tots els botons a l'esquerra
        jpLeft.add(jpgblBotons, BorderLayout.CENTER);

        jpDivisor.add(jpLeft, 0);

        //S'afegeixen els panells de les diferents funcions dels settings a la dreta
        jpDivisor.add(settingsView, 1);
        this.add(jpDivisor, BorderLayout.CENTER);
        setOpaque(false);
    }

    /**
     * Metode que relaciona cada element amb el seu Listener, i per tant es relaciona la vista amb el controlador
     * @param c Controlador
     * */
    public void addController(Controller c) {

        this.settingsView.addController(c);

        jbChangePassword.setActionCommand("SETTINGS - changePass");
        jbChangePassword.addActionListener(c);

        jbBack.setActionCommand("SETTINGS - backFromSettings");
        jbBack.addActionListener(c);

        jbAddMoney.setActionCommand("SETTINGS - addMoneyButton");
        jbAddMoney.addActionListener(c);

        jbWalletEvolution.setActionCommand("SETTINGS - WALLETEVOLUTION");
        jbWalletEvolution.addActionListener(c);

    }

    /**
     * Metode que assigna quin dels tres panells que contenen les funcions dels settings
     * es visualitza per pantalla i el posa visible
     * @param s String que indica quina de les tres opcions es visualitza
     */
    public void showSetting(String s){
        this.settingsView.setVisible(true);
        setSetting(s);
    }

    /**
     * Metode que assigna quin dels tres panells que contenen les funcions dels settings es visualitza per pantalla
     * @param s String que indica quina de les tres opcions es visualitza
     */
    private void setSetting(String s){
        this.settingsView.showSetting(s);
    }

    /**Metode que actualitza la taula segons les diferents fluctuacions de diners que ha patit l'usuari
     * @param newWallet Missatge que retorna les transaccions de l'usuari*/
    public void updateWallet(WalletEvolutionMessage newWallet) {
        settingsView.updateWallet(newWallet);
    }

    /**Metode que configura les imatges d'un boto per quan no esta apretat i per quan si que ho esta
     * @param boto JButton al que es volen associal les imatges
     * @param normal Imatge per quan el boto no esta apretat
     * @param onSelection Imatge per quan el boto esta apretat*/
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

    //Getters
    public SettingsView getSettingsView() {
        return settingsView;
    }

    public Top5View getWalletEvolutionView() {
        return settingsView.getTop5View();
    }

    public PasswordChangeView getPasswordChangeView() {
        return settingsView.getPasswordChangeView();
    }

    public AddMoneyView getAddMoneyView() {
        return settingsView.getAddMoneyView();
    }

}