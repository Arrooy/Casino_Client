package Vista.GameViews.BlackJack;

import Controlador.Controller;

import Controlador.Sounds;
import Model.AssetManager;
import Model.Baralla;
import Model.Card;
import Vista.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.LinkedList;

/**TODO: NO TOCAR AIXO; STA EN FASE BETA PRO FUNCIONA 100%*/

//TODO: modificar @DELETE_ME_LATER
public class BlackJackView extends View {

    public static final int MARGIN_BETWEEN_CARDS = 5;
    public static final int MARGIN_TOP = 10;
    public static final int MARGIN_BOTTOM = 10;
    public static final int MARGIN_CENTER = 100;

    public static final int CARD_WIDTH = 150;
    public static final int CARD_HEIGHT = 210;

    public static final int MAX_CARDS_IN_HAND = 4;

    public static final int MIN_SCREEN_WIDTH = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
    public static final int MIN_SCREEN_HEIGHT = CARD_HEIGHT  + CARD_HEIGHT + MARGIN_TOP + MARGIN_BOTTOM + MARGIN_CENTER;

    private LinkedList<JLabel> userCards;
    private LinkedList<JLabel> IACards;

    private JLabel totalCardScorePlayer;
    private JLabel bet;

    private JButton incBet_BJ;
    private JButton decBet_BJ;


    public BlackJackView(){
        setBackground(Color.BLACK);
        setLayout(null);
        IACards = new LinkedList<>();
        userCards = new LinkedList<>();

        totalCardScorePlayer = new JLabel("0");
        bet = new JLabel("10");
        totalCardScorePlayer.setForeground(Color.white);
        bet.setForeground(Color.white);

        bet.setBounds(10,10,bet.getPreferredSize().width,bet.getPreferredSize().height);
        totalCardScorePlayer.setBounds(10,50,bet.getPreferredSize().width,bet.getPreferredSize().height);
        add(totalCardScorePlayer);
        add(bet);
        incBet_BJ = new JButton("incrementa aposta");
        decBet_BJ = new JButton("decrementa aposta");

    }

    public void modificaAposta(){

    }

    public void addCardIntoGame(Card card, MouseListener c){

        JLabel label = new JLabel(new ImageIcon(Baralla.findImage(card)));
        label.addMouseListener(c);
        label.setName(card.getCardName());
        add(label,0);


        if(card.isForIA()){
            //La carta es per la ia
            IACards.add(label);

        }else {
            //La carta es per a la persona
            userCards.add(label);
            totalCardScorePlayer.setText(userCards.size() + "");
        }

        Sounds.play("cardPlace1.wav");
        updateBoardPositions();
        updateUI();
    }

    public void updateBoardPositions(){
        displayHand(IACards,MARGIN_TOP);
        displayHand(userCards,getSize().height - CARD_HEIGHT - MARGIN_BOTTOM);
        updateUI();
    }

    private void displayHand(LinkedList<JLabel> cardsInHand,int marginTop) {

        if(!cardsInHand.isEmpty()){
            int posicioInicialEsquerra;
            if(cardsInHand.size() <= MAX_CARDS_IN_HAND) {
                posicioInicialEsquerra = (getSize().width / 2) - (cardsInHand.size() / 2) * CARD_WIDTH;
            }else{
                posicioInicialEsquerra = (getSize().width / 2) - 2 * CARD_WIDTH;
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
                    System.out.println("->  "  +  (i + 1) + " Layer " + numberOfLayers);
                    newPosX = posicioInicialEsquerra + (CARD_WIDTH + MARGIN_BETWEEN_CARDS) * (i + 1);
                }else{
                    System.out.println("->  "  +  (i%MAX_CARDS_IN_HAND + 1) + " Layer " + numberOfLayers);
                    newPosX = posicioInicialEsquerra + (CARD_WIDTH + MARGIN_BETWEEN_CARDS) * (i%MAX_CARDS_IN_HAND + 1);
                }


                cardsInHand.get(i).setBounds(newPosX - CARD_WIDTH - shiftCardsLeft, marginTop - 50 * numberOfLayers, CARD_WIDTH, CARD_HEIGHT);
            }

        }
    }

    @Override
    public void addController(Controller c) {
        addComponentListener(c);
    }
}
