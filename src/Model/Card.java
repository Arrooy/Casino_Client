package Model;

import Network.Message;

import java.util.LinkedList;

public class Card extends Message{

    /** Nom de la imatge de la carta*/
    private String cardName;

    /** Nombre del dorso de la carta*/
    private String reverseName;

    /** Defineix el identificador del missatge. Util per quan s'envia amb el networkManager*/
    private final double ID;

    private String context;

    private LinkedList<String> nomCartes;

    /** Indica si la carta es per a un jugador o per a la ia.*/
    private boolean forIA;

    public Card(String cardName, String context, LinkedList<String> nomCartes, boolean ownerIA){
        forIA = ownerIA;

        this.nomCartes = nomCartes;
        this.cardName = cardName;
        ID = Math.random();
        reverseName = null;
        this.context = context;
    }


    public boolean isForIA() {
        return forIA;
    }
    public String getCardName() {return cardName;}
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getReverseName() {
        return reverseName;
    }
    public void setReverseName(String reverseName) {
        this.reverseName = reverseName;
    }

    public LinkedList<String> getNomCartes() {
        return nomCartes;
    }
    public void setNomCartes(LinkedList<String> nomCartes) {
        this.nomCartes = nomCartes;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public double getID() {
        return ID;
    }
}