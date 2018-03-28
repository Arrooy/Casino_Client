package Vista;

import Controlador.MiniViewController;

import javax.swing.*;
import java.awt.*;

public class MiniView extends JFrame {

    public MiniView(){

        getContentPane().add(new JLabel("hola"));
        getContentPane().setBackground(Color.red);
        setUndecorated(true);
        setTitle("");

        pack();
        //Es centra la finestra en el centre de la pantalla
        Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setLocation(winSize.width - getSize().width, winSize.height  - getSize().height);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public void addController(MiniViewController c){

    }
}
