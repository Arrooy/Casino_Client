package Vista.SettingsViews;

import Controlador.Controller;
import Vista.SwingModifications.IconPasswordField;
import Vista.View;

import javax.swing.*;
import java.awt.*;

//TODO arreglar missatges error money i log in, sign in

public class AddMoneyView extends View {

    //TODO: mirar aixo
    private static final String TOOL_TIP_PASSWORD = "Your password";
    private static final String TOOL_TIP_AMOUNT = "How much money do you want?";

    private static final String AMOUNT_HINT = "Choose Amount";
    private static final String PASSWORD_HINT = "Password";

    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color VERD = new Color(25, 151, 6);
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    private JButton jbAddMoney;
    private JNumberTextField jntfAmount;
    private IconPasswordField jpfPassword;
    private JLabel jlErrorMoney;
    private JLabel jlErrorPassword;
    private JLabel jlAddOK;

    public AddMoneyView(){
        this.setLayout(new BorderLayout());

        JPanel jpMoneyView = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 150;
        c.insets = new Insets(0,0,20,0);
        jntfAmount = new JNumberTextField("money.png",AMOUNT_HINT,TOOL_TIP_AMOUNT);
        jntfAmount.setEditable(true);
        jpMoneyView.add(jntfAmount, c);

        jpfPassword = new IconPasswordField("padlock.png",PASSWORD_HINT,TOOL_TIP_PASSWORD);
        c.gridy = 1;
        c.ipadx = 150;
        jpMoneyView.add(jpfPassword, c);

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,20,20,0);
        jlErrorMoney = new JLabel("Above the limit amount ");
        jpMoneyView.add(jlErrorMoney, c);

        c.gridy = 1;
        jlErrorPassword = new JLabel("Wrong password");
        jpMoneyView.add(jlErrorPassword, c);

        jlAddOK = new JLabel("Transaction OK");

        c.gridy = 4;
        c.gridx = 0;
        c.ipadx = 50;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.CENTER;
        c.insets = new Insets(20,45,0,0);
        jpMoneyView.add(jlAddOK, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0,0,0,0);
        c.ipadx = 50;
        jbAddMoney = new JButton("ADD");
        jbAddMoney.setFocusable(false);
        jpMoneyView.add(jbAddMoney, c);

        add(jpMoneyView, BorderLayout.CENTER);

    }
    @Override
    public void addController(Controller c) {
        jbAddMoney.addActionListener(c);
        jbAddMoney.setActionCommand("ADD MONEY");

        jntfAmount.addFocusListener(c);
        jpfPassword.addFocusListener(c);
    }

    /** Retorna la quantitat de diners que es desitgen introduir*/
    public long getAmount(){
        return jntfAmount.getNumber();
    }

    /** retorna la contrasenya de l'usuari*/
    public String getPassword(){
        return new String(jpfPassword.getPassword());
    }
    /** Controla el missatge d'error*/
    public void showErrorMoney(String t){
        this.jlErrorMoney.setText(t);
        this.jlErrorMoney.setForeground(GRANA);
    }
    /** Controla el missatge d'error*/
    public void showErrorPassword(){
        this.jlErrorPassword.setForeground(GRANA);
    }
    /** Controla el missatge de Transaction OK*/
    public void showAddOK(){ this.jlAddOK.setForeground(VERD); }
    /** Indica que no hi ha error*/
    public void noErrorMoney(){
        this.jlErrorMoney.setForeground(TRANSPARENT);
    }
    /** Indica que no hi ha error*/
    public void noErrorPassword(){ this.jlErrorPassword.setForeground(TRANSPARENT); }
    /** Indica que la transacció no s'ha fet*/
    public void noTransactionOK(){ this.jlAddOK.setForeground(TRANSPARENT); }
}
