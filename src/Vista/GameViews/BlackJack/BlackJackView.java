package Vista.GameViews.BlackJack;

import Controlador.Controller;

import Controlador.Sounds;
import Model.Baralla;
import Model.Card;
import Vista.ToDraw;
import Vista.View;

import java.awt.*;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class BlackJackView extends View implements ToDraw {

    public static final int MARGIN_BETWEEN_CARDS = 5;
    public static final int MARGIN_TOP = 10;
    public static final int MARGIN_BOTTOM = 10;
    public static final int MARGIN_CENTER = 100;

    public static final int CARD_WIDTH = 150;
    public static final int CARD_HEIGHT = 210;

    public static final int MAX_CARDS_IN_HAND = 6;

    public static final int MIN_SCREEN_WIDTH = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
    public static final int MIN_SCREEN_HEIGHT = CARD_HEIGHT  + CARD_HEIGHT + MARGIN_TOP + MARGIN_BOTTOM + MARGIN_CENTER;

    private LinkedList<Card> userCards;
    private LinkedList<Card> IACards;

    public BlackJackView(){
        setBackground(Color.BLACK);
        setLayout(null);
        IACards = new LinkedList<>();
        userCards = new LinkedList<>();
    }

    public void addCardIntoGame(Card card){
        if(card.isForIA()){
            //La carta es per la ia
            IACards.add(card);

        }else {
            //La carta es per a la persona
            userCards.add(card);
        }
        Sounds.play("cardPlace1.wav");
    }

    private void updateHand(LinkedList<Card> cardsInHand, int marginTop, int direction) {

        if(!cardsInHand.isEmpty()){
            int posicioInicialEsquerra;
            if(cardsInHand.size() <= MAX_CARDS_IN_HAND) {
                posicioInicialEsquerra = (getSize().width / 2) - (cardsInHand.size() / 2) * CARD_WIDTH;
            }else{
                posicioInicialEsquerra = (getSize().width / 2) - 3 * CARD_WIDTH;
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
    public void addController(Controller c) {
        setName("BlackJack");
        addMouseListener(c);
    }

    @Override
    public void init() {
        Sounds.play("cardShuffle.wav");
    }

    @Override
    public void update(float delta) {
        updateHand(IACards,MARGIN_TOP,1);
        updateHand(userCards,getSize().height - CARD_HEIGHT - MARGIN_BOTTOM,-1);
    }

    @Override
    public void render(Graphics g) {
        for(Card card : IACards){
            g.drawImage(Baralla.findImage(card),card.getX(),card.getY(),null);
        }
        for(Card card : IACards){
            g.drawImage(Baralla.findImage(card),card.getX(),card.getY(),null);
        }
    }
}
