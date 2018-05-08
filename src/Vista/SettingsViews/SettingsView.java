package Vista.SettingsViews;

import Controlador.Controller;
import Model.WalletEvolutionMessage;


import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel {
    private CardLayout layout;
    private PasswordChangeView passwordChangeView;
    private Top5View top5View;
    private AddMoneyView addMoneyView;


    public SettingsView(){
        this.layout = new CardLayout();
        this.setLayout(layout);

        this.passwordChangeView = new PasswordChangeView();
        this.top5View = new Top5View();
        this.addMoneyView = new AddMoneyView();


        this.add("CHANGEPASSWORD", this.passwordChangeView);
        this.add("WALLETEVOLUTION", this.top5View);
        this.add("ADDMONEY", this.addMoneyView);

        setOpaque(false);
    }

    public void addController(Controller c){
        this.passwordChangeView.addController(c);
        this.addMoneyView.addController(c);
    }


    public PasswordChangeView getPasswordChangeView() {
        return passwordChangeView;
    }

    public Top5View getTop5View() {
        return top5View;
    }

    public AddMoneyView getAddMoneyView() {
        return addMoneyView;
    }

    /** Depenent del botó que s'ha premut, es mostra un JPanel del CardLayout que representa una fncio per l'usuari*/
    public void showSetting(String s) {

        switch (s){
            case"SETTINGS - addMoneyButton":
                layout.show(this, "ADDMONEY");
                break;
            case"SETTINGS - changePass":
                layout.show(this, "CHANGEPASSWORD");
                break;
            case"SETTINGS - WALLETEVOLUTION":
                layout.show(this, "WALLETEVOLUTION");
                break;

        }
    }

    public void updateWallet(WalletEvolutionMessage newWallet) {
        top5View.updateWallet(newWallet);
    }
}
