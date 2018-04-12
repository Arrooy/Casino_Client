package Vista.SettingsViews;

import Controlador.Controller;


import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel {
    private CardLayout layout;
    private PasswordChangeView passwordChangeView;
    private WalletEvolutionView walletEvolutionView;
    private AddMoneyView addMoneyView;


    public SettingsView(){
        this.setBackground(Color.BLACK);
        this.layout = new CardLayout();
        this.setLayout(layout);

        this.passwordChangeView = new PasswordChangeView();
        this.walletEvolutionView = new WalletEvolutionView();
        this.addMoneyView = new AddMoneyView();


        this.add("CHANGEPASSWORD", this.passwordChangeView);
        this.add("WALLETEVOLUTION", this.walletEvolutionView);
        this.add("ADDMONEY", this.addMoneyView);


    }

    public void addController(Controller c){
        this.passwordChangeView.addController(c);
        this.walletEvolutionView.addController(c);
        this.addMoneyView.addController(c);
    }


    public PasswordChangeView getPasswordChangeView() {
        return passwordChangeView;
    }

    public WalletEvolutionView getWalletEvolutionView() {
        return walletEvolutionView;
    }

    public AddMoneyView getAddMoneyView() {
        return addMoneyView;
    }


    public void showSetting(String s) {

        switch (s){
            case"SETTINGS - addMoneyButton":
                layout.show(this, "ADDMONEY");
                break;
            case"SETTINGS - changePass":
                layout.show(this, "CHANGEPASSWORD");
                break;
            case"SETTINGS - walletEvolution":
                layout.show(this, "WALLETEVOLUTION");
                break;

        }
    }
}
