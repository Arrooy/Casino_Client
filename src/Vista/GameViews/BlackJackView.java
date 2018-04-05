package Vista.GameViews;

import Controlador.Controller;
import Vista.View;

import javax.swing.*;
import java.awt.*;

public class BlackJackView extends View {

    public BlackJackView(){
        Baralla cartas = new Baralla();

    }

    @Override
    public void addController(Controller c) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Text
        g.drawString("This is my custom Panel!",10,20);
    }
}
