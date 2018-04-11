package Controlador.Game_Controlers;

import Controlador.GraphicsController;
import Model.Card;
import Vista.GameViews.BlackJack.BlackJackView;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class BlackJackController extends GraphicsController {

    private BlackJackView blackJackView;

    public BlackJackController(BlackJackView blackJackView){
        this.blackJackView  = blackJackView;
    }

    public void newBJCard(Card cartaResposta) {
        blackJackView.addCardIntoGame(cartaResposta);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
