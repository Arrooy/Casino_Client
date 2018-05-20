package Vista.SettingsViews;

import Controlador.Controller;
import Utils.AssetManager;
import Vista.SwingModifications.IconPasswordField;
import Vista.View;

import javax.swing.*;
import java.awt.*;

//TODO arreglar missatges error money i log in, sign in

/**Classe que crea el panell que implementa la funcio dels settings d'afegir diners al compte*/
public class AddMoneyView extends View {
    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private static final String TOOL_TIP_PASSWORD = "Your password";

    /**String constant per indicar que el camp s'ha d'omplir amb la quantitat de diners que l'usuari vol afegir al compte*/
    private static final String TOOL_TIP_AMOUNT = "How much money do you want?";

    /**String constant per indicar que el camp s'ha d'omplir amb la quantitat de diners que l'usuari vol afegir al compte*/
    private static final String AMOUNT_HINT = "Choose Amount";

    /**String constant per indicar que el camp s'ha d'omplir amb la contrasenya*/
    private static final String PASSWORD_HINT = "Password";

    /**Color Grana*/
    private final static Color GRANA = new Color(125, 28, 37);

    /**Color Verd*/
    private final static Color VERD = new Color(40, 73, 7);

    /**Color Transparent*/
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    /**Boto que indica que l'usuari vol afegir diners al compte*/
    private JButton jbAddMoney;

    /**Quantitat de diners que l'usuari vol afegir*/
    private JNumberTextField jntfAmount;

    /**Contrasenya de l'usuari*/
    private IconPasswordField jpfPassword;

    /**Missatge d'error amb els diners*/
    private JLabel jlErrorMoney;

    /**Missatge d'error amb la contrasenya*/
    private JLabel jlErrorPassword;

    /**Missatge que indica que la transaccio s'ha fet correctament*/
    private JLabel jlAddOK;

    /**Constructor del panell que defineix on i com es col·loquen els elements*/
    public AddMoneyView(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        JPanel jpMoneyView = new JPanel(new GridBagLayout());
        jpMoneyView.setOpaque(false);

        //Safegeixen separadors per tal que els elements quedin centrats al centre del panell
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10,20,10,25);
        JLabel jlaux = new JLabel("Strut");
        jlaux.setForeground(TRANSPARENT);
        jpMoneyView.add(jlaux, c);

        c.gridx = 1;
        JLabel jlaux1 = new JLabel("Strut");
        jlaux1.setForeground(TRANSPARENT);
        jpMoneyView.add(jlaux1,c);

        //Posicio del camp de la quantitat de diners que l'usuari vol afegir
        c.gridy = 1;
        c.ipadx = 200;
        c.insets = new Insets(0,0,20,0);
        //S'associa al camp una icona per tal d'identificar-lo millor
        jntfAmount = new JNumberTextField("money.png",AMOUNT_HINT,TOOL_TIP_AMOUNT);
        jntfAmount.setEditable(true);
        jpMoneyView.add(jntfAmount, c);

        //S'associa al camp una icona per tal d'identificar-lo millor
        jpfPassword = new IconPasswordField("padlock.png",PASSWORD_HINT,TOOL_TIP_PASSWORD);

        //Posicio camp de la contrasenya
        c.gridy = 2;
        c.ipadx = 200;
        jpMoneyView.add(jpfPassword, c);

        //Posicio del missatge d'error dels diners
        c.gridx = 2;
        c.gridy = 1;
        c.ipadx = 200;
        c.insets = new Insets(0,20,20,0);
        jlErrorMoney = new JLabel("Above the limit amount ");
        jpMoneyView.add(jlErrorMoney, c);

        //Posicio del missatge d'error de la contrasenya
        c.gridy = 2;
        jlErrorPassword = new JLabel("Wrong password");
        jpMoneyView.add(jlErrorPassword, c);

        jlAddOK = new JLabel("Transaction OK");

        //Posicio del missatge que indica que la transaccio s'ha fet correctament
        c.gridy = 5;
        c.gridx = 1;
        c.ipadx = 50;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.CENTER;
        c.insets = new Insets(20,45,0,0);
        jpMoneyView.add(jlAddOK, c);

        //Posicio del boto afegir
        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 50;
        jbAddMoney = new JButton();
        configButton(jbAddMoney, "ADD.png", "ADDS.png");
        jpMoneyView.add(jbAddMoney, c);

        //Es col·loquen tots elements al centre del panell
        add(jpMoneyView, BorderLayout.CENTER);
        setOpaque(false);
    }

    /**
     * Metode override que relaciona cada element amb el seu Listener, i per tant es relaciona la vista amb el controlador
     * @param c Controlador
     * */
    @Override
    public void addController(Controller c) {
        jbAddMoney.addActionListener(c);
        jbAddMoney.setActionCommand("ADD MONEY");

        jntfAmount.addFocusListener(c);
        jpfPassword.addFocusListener(c);
    }

    /** @return long amb la quantitat de diners que es desitgen introduir*/
    public long getAmount(){
        return jntfAmount.getNumber();
    }

    /** @return String amb la contrasenya de l'usuari*/
    public String getPassword(){
        return new String(jpfPassword.getPassword());
    }

    /** Mostra el missatge d'error que es passa per parametre
     * @param t missatge d'error*/
    public void showErrorMoney(String t){
        this.jlErrorMoney.setText(t);
        this.jlErrorMoney.setForeground(GRANA);
    }

    /**Mostra el missatge d'error de la contrasenya*/
    public void showErrorPassword(){
        this.jlErrorPassword.setForeground(GRANA);
    }

    /**Mostra el missatge de Transaction OK*/
    public void showAddOK(){ this.jlAddOK.setForeground(VERD); }

    /**No mostra o amaga el missatge d'error*/
    public void noErrorMoney(){
        this.jlErrorMoney.setForeground(TRANSPARENT);
    }

    /**No mostra o amaga el missatge d'error de la contrasenya*/
    public void noErrorPassword(){ this.jlErrorPassword.setForeground(TRANSPARENT); }

    /**No mostra el missatge ja que la transacció no s'ha fet*/
    public void noTransactionOK(){ this.jlAddOK.setForeground(TRANSPARENT); }

    /**Metode que s'encarrega de netejar tots els camps*/
    public void clearView(){
        noTransactionOK();
        clearFields();
    }

    /**Metode que s'encarrega de netejar tots els camps, menys eliminar el missatge de TransactionOK*/
    public void clearFields(){
        noErrorMoney();
        noErrorPassword();
        jpfPassword.setText("");
        jpfPassword.setHint(true);
        jntfAmount.setText("");
        jntfAmount.setHint(true);
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
}
