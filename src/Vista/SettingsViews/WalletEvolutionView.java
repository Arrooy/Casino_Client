package Vista.SettingsViews;

import Controlador.Controller;
import Vista.View;

import javax.swing.*;
import java.awt.*;


public class WalletEvolutionView extends View {


    public WalletEvolutionView(){
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0,0,0,0));
        add(new JLabel("Wallet evolution"), BorderLayout.CENTER);
    }
    @Override
    public void addController(Controller c) {

    }
}
