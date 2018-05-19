package Vista.SettingsViews;

import Controlador.Controller;
import Model.WalletEvolutionMessage;


import javax.swing.*;
import java.awt.*;

/**Classe que gestiona les diferents pestanyes dels settings*/
public class SettingsView extends JPanel {
    /**Layout del panell*/
    private CardLayout layout;

    /**Panell amb els camps necessaris per canviar de contrasenya*/
    private PasswordChangeView passwordChangeView;

    /**Panell que visualitza en format de taula les fluctuacios monetaries de l'usuari*/
    private Top5View top5View;

    /**Panell que te els camps necessaris per afegir diners al compte*/
    private AddMoneyView addMoneyView;

    /**Constructor de la classe que relaciona els diferents panells de setings amb un CardLayout*/
    public SettingsView(){
        //Es defineix el Layout
        this.layout = new CardLayout();
        this.setLayout(layout);

        //Es creen els panells de les diferents opcions dels settings
        this.passwordChangeView = new PasswordChangeView();
        this.top5View = new Top5View();
        this.addMoneyView = new AddMoneyView();

        //S'afegeifen aquests panells i se'ls associa un nom per tal d'indentificar-los
        this.add("CHANGEPASSWORD", this.passwordChangeView);
        this.add("WALLETEVOLUTION", this.top5View);
        this.add("ADDMONEY", this.addMoneyView);

        setOpaque(false);
    }

    /**
     * Metode override que relaciona cada element amb el seu Action Listener
     * @param c Controlador
     * */
    public void addController(Controller c){
        this.passwordChangeView.addController(c);
        this.addMoneyView.addController(c);
    }

    /**
     * Depenent del botó que s'ha premut, es mostra un JPanel del CardLayout que representa una fncio per l'usuari
     * @param s String que indica el boto que s'ha premut
     */
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

    /**
     * Metode que actualitza la taula segons les diferents fluctuacions de diners que ha patit l'usuari
     * @param newWallet Missatge que retorna les transaccions de l'usuari
     * */
    public void updateWallet(WalletEvolutionMessage newWallet) {
        top5View.updateWallet(newWallet);
    }

    //Getters
    public PasswordChangeView getPasswordChangeView() {
        return passwordChangeView;
    }

    public Top5View getTop5View() {
        return top5View;
    }

    public AddMoneyView getAddMoneyView() {
        return addMoneyView;
    }
}
