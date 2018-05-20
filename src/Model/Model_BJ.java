package Model;

import java.util.LinkedList;


/** En aquesta classe es guarden les dades de la partida actual del BlackJack*/
public class Model_BJ {

    /** Constants per al posicionament de les cartes al tauler*/
    public static final int MARGIN_BETWEEN_CARDS = 5;

    /** Constants per al posicionament de les cartes al tauler*/
    public static final int MARGIN_TOP = 10;

    /** Constants per al posicionament de les cartes al tauler*/
    public static final int MARGIN_BOTTOM = 10;

    /** Constants per al posicionament de les cartes al tauler*/
    public static final int CARD_WIDTH = 150;

    /** Constants per al posicionament de les cartes al tauler*/
    public static final int CARD_HEIGHT = 210;

    /** Constants per al posicionament de les cartes al tauler*/
    public static final int MAX_CARDS_IN_HAND = 6;

    /** Llista de cartes del tauler que pertanyen al usuari*/
    private LinkedList<Card> userCards;

    /** Llista de cartes del tauler que pertanyen a la IA*/
    private LinkedList<Card> IACards;

    /** Valor de les cartes del Usuari*/
    private int valueDisplayUser;

    /** Valor de les cartes de la IA*/
    private int valueDisplayIa;

    /** Resultat economic de repetides partides*/
    private long earnings;

    /** Crea el model del BlackJack*/
    public Model_BJ(){
        IACards = new LinkedList<>();
        userCards = new LinkedList<>();

        earnings = 0;
        valueDisplayUser = 0;
        valueDisplayIa = 0;
    }

    /**
     * Afegeix una carta al model, actualitza el valor total de les cartes del jugador desti.
     * @param card Carta a repartir
     */
    public void addCard(Card card){
        if(card.isForIA()){
            //La carta es per la ia
            IACards.add(card);
            valueDisplayIa += card.getValue();
        }else {
            //La carta es per a la persona
            userCards.add(card);
            valueDisplayUser += card.getValue();
        }
    }

    /** Gira totes les cartes de la IA fent-les visibles*/
    public void giraIA() {
        int arraySize = IACards.size();
        for(int i = 0; i < arraySize; i++){
            IACards.get(i).setGirada(false);
        }
    }
    /** Reinicia el model del BlackJack*/
    public void clearData() {
        userCards.clear();
        IACards.clear();
        earnings = 0;
        valueDisplayUser = 0;
        valueDisplayIa = 0;
    }

    /** Indica si les 4 cartes inicials s'han afegit al model*/
    public boolean areCardsLoaded() {
        return IACards.size() + userCards.size() >= 4;
    }

    public LinkedList<Card> getIACards() {
        return IACards;
    }

    public LinkedList<Card> getUserCards() {
        return userCards;
    }

    public boolean IAHasCards() {
        return !IACards.isEmpty();
    }

    public boolean userHasCards() {
        return !userCards.isEmpty();
    }

    public int getValueIA(){
        return valueDisplayIa;
    }

    public int getValueUser(){
        return valueDisplayUser;
    }

    public long getEarnings(){
        return earnings;
    }

    public void addEarnings(long earnings){
        this.earnings += earnings;
    }

}
