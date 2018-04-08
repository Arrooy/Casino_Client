package Vista.GameViews.BlackJack;

import Controlador.Controller;

import Vista.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.LinkedList;

/**TODO: NO TOCAR AIXO; STA EN FASE BETA PRO FUNCIONA 100%*/

public class BlackJackView extends View {

    private LinkedList<JLabel> userCards;
    private LinkedList<JLabel> IACards;

    public static final int MARGIN_BETWEEN_CARDS = 5;
    public static final int MARGIN_TOP = 10;
    public static final int MARGIN_BOTTOM = 10;
    public static final int MARGIN_CENTER = 100;

    public static final int ZOOM_SPACE = 20;

    public static final int CARD_WIDTH = 150;
    public static final int CARD_HEIGHT = 210;

    public static final int MIN_SCREEN_WIDTH = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
    public static final int MIN_SCREEN_HEIGHT = CARD_HEIGHT  + CARD_HEIGHT + MARGIN_TOP + MARGIN_BOTTOM + MARGIN_CENTER;

    public BlackJackView(){
        setBackground(Color.BLACK);
        setLayout(null);
        IACards = new LinkedList<>();
        userCards = new LinkedList<>();
    }

    /*
    public void moveCard(String cardName,int x,int y,boolean translate){
        for(JLabel carta : cardsInGame){
            if(carta.getName().equals(cardName)){
                Rectangle rect = carta.getBounds();

                if(translate)
                    carta.setBounds( x, y,CARD_WIDTH,210);
                else
                    carta.setBounds(rect.x + x,rect.y + y,CARD_WIDTH,CARD_HEIGHT);

                updateUI();
                break;
            }
        }
    }
    */

    public void addCardIntoGame(String cardName,boolean forIa,Image card, MouseListener c){

        JLabel label = new JLabel(new ImageIcon(card));
        label.addMouseListener(c);
        label.setName(cardName);
        add(label);

        if(forIa){
            //La carta es per la ia
            IACards.add(label);
        }else {
            //La carta es per a la persona
            userCards.add(label);
        }

        updateBoardPositions(null);
        updateUI();
    }
    public void updateBoardPositions(String SelectedName){

        int centerScreen = getSize().width / 2;
        int screenHeight = getSize().height;

        if(!IACards.isEmpty()){

            int difx;
            int shiftCardsLeft = 0;

            if(IACards.size() % 2 != 0)
                shiftCardsLeft = CARD_WIDTH / 2;
            difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS;

            for(int i = 0; i < IACards.size() / 2; i++){

               /* if(userCards.get(i).getName().equals(SelectedName)){
                    System.out.println("Zoom Space on IA RIGHT");
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS + ZOOM_SPACE;
                }else{
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
                }*/

                int newPosX = centerScreen - difx * (i + 1);
                IACards.get(i).setBounds(newPosX - shiftCardsLeft,MARGIN_TOP,CARD_WIDTH,CARD_HEIGHT);
            }

            for(int i = IACards.size() / 2; i < IACards.size(); i++){

                /*if(userCards.get(i).getName().equals(SelectedName)){
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS + ZOOM_SPACE;
                    System.out.println("Zoom Space on IA LEFT");
                }else{
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
                }*/

                int newPosX = centerScreen + difx * (i - IACards.size() / 2);
                IACards.get(i).setBounds(newPosX - shiftCardsLeft, MARGIN_TOP,CARD_WIDTH,CARD_HEIGHT);
            }
        }

        if(!userCards.isEmpty()){
            int difx;

            int shiftCardsLeft = 0;

            if(userCards.size() % 2 != 0)
                shiftCardsLeft = CARD_WIDTH / 2;

            difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS;

            //Center -> RIGHT
            for(int i = 0; i < userCards.size() / 2; i++){

               /* if(userCards.get(i).getName().equals(SelectedName)){
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS + ZOOM_SPACE;
                    System.out.println("Zoom Space on HUMAN RIGHT");
                }else{
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
                }
*/
                int newPosX = centerScreen - difx * (i + 1);
                userCards.get(i).setBounds(newPosX - shiftCardsLeft,screenHeight  - CARD_HEIGHT - MARGIN_BOTTOM,CARD_WIDTH,CARD_HEIGHT);
            }

            //Center -> left
            for(int i = userCards.size() / 2; i < userCards.size(); i++){

               /*  if(userCards.get(i).getName().equals(SelectedName)){
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS + ZOOM_SPACE;
                    System.out.println("Zoom Space on HUMAN LEFT");
                }else{
                    difx = CARD_WIDTH + MARGIN_BETWEEN_CARDS;
                }*/

                int newPosX = centerScreen + difx * (i - userCards.size() / 2);
                userCards.get(i).setBounds( newPosX - shiftCardsLeft, screenHeight - CARD_HEIGHT - MARGIN_BOTTOM,CARD_WIDTH,CARD_HEIGHT);
            }
        }

        updateUI();
    }

    @Override
    public void addController(Controller c) {
        addComponentListener(c);
    }
}
