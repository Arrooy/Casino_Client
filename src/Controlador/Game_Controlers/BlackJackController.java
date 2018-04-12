package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Model.Baralla;
import Model.Card;
import Model.Model_BJ;
import Network.NetworkManager;
import Network.Transmission;
import Vista.GameViews.BlackJack.BlackJackView;
import Controlador.CustomGraphics.GraphicsManager;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static Model.Model_BJ.*;

public class BlackJackController implements GraphicsController {

    private BlackJackView blackJackView;
    private NetworkManager networkManager;
    private int mouseX,mouseY;
    private GraphicsManager gp;
    private Model_BJ model;

    public BlackJackController(BlackJackView blackJackView,NetworkManager networkManager){
        this.model = new Model_BJ();
        this.networkManager = networkManager;
        this.blackJackView  = blackJackView;

        this.gp = new GraphicsManager(blackJackView,this);
        gp.setClearColor(Color.red);
    }

    public void updateSizeBJ(){
        gp.resize(blackJackView.getWidth(),blackJackView.getHeight());
    }

    public void newBJCard(Card cartaResposta, Controller c) {

        System.out.println("Card: " + cartaResposta.getCardName());
        model.addCard(cartaResposta);

        //control de la carta nova!
        if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER)){
            model.giraIA();
        }

        if(cartaResposta.getDerrota().equals("user")){
            finishGame(false,c);
        }else if(cartaResposta.getDerrota().equals("IA")){
            finishGame(true,c);
        }
    }

    private void finishGame(boolean winner,Controller c) {
        if(winner){
            c.displayError("USER WIN GAME!","meh");
        }else{
            c.displayError("USER LOOSE GAME!","hurray");
        }
        gp.exit();

        c.showGamesView();
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
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
        System.out.println("Button pressed BJ: " + e.getButton());
        if(e.getButton() == 1){
            networkManager.newBlackJackCard(false);
        }else{
            networkManager.endBlackJackTurn();
        }
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }


    @Override
    public void init() {
        //Sounds.play("cardShuffle.wav");
    }

    @Override
    public void update(float delta) {
        updateCardsPosition(model.getIACards(),MARGIN_TOP,1);
        updateCardsPosition(model.getUserCards(),blackJackView.getSize().height - CARD_HEIGHT - MARGIN_BOTTOM,-1);
    }

    private void updateCardsPosition(LinkedList<Card> cardsInHand, int marginTop, int direction) {

        if(!cardsInHand.isEmpty()){
            int posicioInicialEsquerra;
            if(cardsInHand.size() <= MAX_CARDS_IN_HAND) {
                posicioInicialEsquerra = (blackJackView.getSize().width / 2) - (cardsInHand.size() / 2) * CARD_WIDTH;
            }else{
                posicioInicialEsquerra = (blackJackView.getSize().width / 2) - 3 * CARD_WIDTH;
            }

            int shiftCardsLeft = 0;

            int numberOfLayers = (cardsInHand.size() - 1) / (MAX_CARDS_IN_HAND);

            if(cardsInHand.size() <= MAX_CARDS_IN_HAND) {
                if (cardsInHand.size() % 2 != 0)
                    shiftCardsLeft = CARD_WIDTH/2 ;
            }

            for (int i = 0; i < cardsInHand.size(); i++) {

                if ((i + 1) % (MAX_CARDS_IN_HAND + 1) == 0) {
                    numberOfLayers--;
                }

                int newPosX;

                if(i+1 <= (MAX_CARDS_IN_HAND)) {
                    newPosX = posicioInicialEsquerra + (CARD_WIDTH + MARGIN_BETWEEN_CARDS) * (i + 1);
                }else{
                    newPosX = posicioInicialEsquerra + (CARD_WIDTH + MARGIN_BETWEEN_CARDS) * (i%MAX_CARDS_IN_HAND + 1);
                }
                cardsInHand.get(i).setCoords(newPosX - CARD_WIDTH - shiftCardsLeft, marginTop + (50 * numberOfLayers)*direction);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if(model.IAHasCards()) {
            for (Card card : model.getIACards()) {
                g.drawImage(Baralla.findImage(card), card.getX(), card.getY(), null);
            }
        }
        if(model.userHasCards()) {
            for (Card card : model.getUserCards()) {
                g.drawImage(Baralla.findImage(card), card.getX(), card.getY(), null);
            }
        }
    }

}
