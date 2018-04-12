package Vista.SettingsViews;

import Controlador.Controller;
import Vista.View;

import javax.swing.*;
import java.awt.*;


public class AddMoneyView extends View {
    private JButton jbAddMoney;
    private JNumberTextField jntfAmount;
    private JLabel jlError;

    public AddMoneyView(){
        this.setLayout(new BorderLayout());
        JPanel jpMoneyView = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets (0,0,20,10);
        JLabel jlMoneyLabel = new JLabel("Choose Amount: ");
        jpMoneyView.add(jlMoneyLabel, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,0,20,10);
        jntfAmount = new JNumberTextField();
        jpMoneyView.add(jntfAmount, c);

        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 0;
        c.insets = new Insets(0,0,20,0);
        jlError = new JLabel("Introdueix una quantitat mes gran que 0");
        jlError.setForeground(Color.red);
        jlError.setVisible(false);


        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0,10,0,0);
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        jbAddMoney = new JButton("ADD");
        jpMoneyView.add(jbAddMoney, c);

    }
    @Override
    public void addController(Controller c) {
        jbAddMoney.addActionListener(c);
        jbAddMoney.setName("ADD MONEY");
    }

    public int getAmount(){
        if(jntfAmount.getNumber() != 0){
            return jntfAmount.getNumber();
        }else{
            return -1;
        }
    }

    public void showError(){
        this.jlError.setVisible(true);
    }

    public void noError(){
        this.jlError.setVisible(false);
    }




}
