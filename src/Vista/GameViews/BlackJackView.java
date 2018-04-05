package Vista.GameViews;

import Controlador.Controller;
import Vista.View;
import java.awt.*;

public class BlackJackView extends View {

    private Baralla cartas;

    public BlackJackView(){
        cartas = new Baralla();
    }

    @Override
    public void addController(Controller c) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);


        repaint();
    }
}
