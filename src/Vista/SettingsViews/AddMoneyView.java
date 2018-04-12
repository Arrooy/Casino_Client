package Vista.SettingsViews;

import Controlador.Controller;
import Vista.View;

import javax.swing.*;
import java.awt.*;

//TODO arreglar missatges error money i log in, sign in

public class AddMoneyView extends View {
    private JButton jbAddMoney;
    private JNumberTextField jntfAmount;
    private JPasswordField jpfPassword;
    private JLabel jlErrorMoney;
    private JLabel jlErrorPassword;

    public AddMoneyView(){
        this.setLayout(new BorderLayout());
        JPanel jpMoneyView = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets (0,0,20,20);
        JLabel jlMoneyLabel = new JLabel("Choose Amount: ");
        jpMoneyView.add(jlMoneyLabel, c);

        JLabel jlPassword = new JLabel("Password: ");
        c.gridy = 1;
        jpMoneyView.add(jlPassword, c);

        c.gridx = 1;
        c.gridy = 0;
        c.ipadx = 150;
        c.insets = new Insets(0,0,20,0);
        jntfAmount = new JNumberTextField();
        jntfAmount.setEditable(true);
        jpMoneyView.add(jntfAmount, c);

        jpfPassword = new JPasswordField();
        c.gridy = 1;
        c.ipadx = 150;
        jpMoneyView.add(jpfPassword, c);

        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0,20,20,0);
        jlErrorMoney = new JLabel("Above the limit amount");
        jlErrorMoney.setForeground(new Color(0, 0, 0, 0));
        jpMoneyView.add(jlErrorMoney, c);

        c.gridy = 1;
        jlErrorPassword = new JLabel("Wrong password");
        jlErrorPassword.setForeground(new Color(0, 0, 0, 0));
        jpMoneyView.add(jlErrorPassword, c);

        c.gridx = 1;
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
    }

    /** Retorna la quantitat de diners que es desitgen introduir*/
    public int getAmount(){
        return jntfAmount.getNumber();
    }

    /** retorna la contrasenya de l'usuari*/
    public String getPassword(){
        return new String(jpfPassword.getPassword());
    }
    /** Controla el missatge d'error*/
    public void showErrorMoney(){
        this.jlErrorMoney.setForeground(new Color(201, 25, 25));
    }
    /** Controla el missatge d'error*/
    public void showErrorPassword(){
        this.jlErrorPassword.setForeground(new Color(201, 25, 25));
    }
    /** Indica que no hi ha error*/
    public void noErrorMoney(){
        this.jlErrorMoney.setForeground(new Color(0, 0, 0, 0));
    }
    /** Indica que no hi ha error*/
    public void noErrorPassword(){
        this.jlErrorPassword.setForeground(new Color(0, 0, 0, 0));
    }




}
