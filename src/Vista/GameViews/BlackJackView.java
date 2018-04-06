package Vista.GameViews;

import Controlador.Controller;

import Model.Card;
import Vista.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class BlackJackView extends View {



    public BlackJackView(){
        setBackground(Color.BLACK);
    }

    class CardView extends JPanel{
        private Image img;
        private int x,y;
        public CardView(Image img,int x,int y){
            this.img = img;
            this.x = x;
            this.y = y;
        }
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img,x,y,null);
            repaint();
        }
    }

    public void addCardIntoGame(Image card, MouseListener c){
        System.out.println("Afegint");
        CardView panell = new CardView(card,50,50);
        panell.addMouseListener(c);
        panell.setBackground(Color.white);
        add(panell);
        updateUI();
    }

    @Override
    public void addController(Controller c) {

    }
}
