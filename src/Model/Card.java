package Model;

import Network.Message;

import java.util.Stack;

public class Card extends Message {

    /** Nom de la imatge de la carta*/
    private String cardName;

    /** Nombre del dorso de la carta*/
    private String reverseName;

    /** Valor de la carta dins del blackJack*/
    private int value;

    /** Defineix el identificador del missatge. Util per quan s'envia amb el networkManager*/
    private final double ID;

    private String context;

    private Stack<String> nomCartes;

    /** Indica si la carta es per a un jugador o per a la ia.*/
    private boolean forIA;

    /** Inidica si la carta esta girada o no*/
    private boolean girada;

    /** Coordenades de la carta en el joc del BJ*/
    private int x;
    private int y;

    /** Crea una nova carta per a iniciar el blackJack*/
    public Card(String cardName, String context, Stack<String> nomCartes, boolean ownerIA){
        forIA = ownerIA;

        girada = false;
        value = 0;

        this.nomCartes = nomCartes;
        this.cardName = cardName;
        ID = Math.random();
        reverseName = null;
        this.context = context;
    }

    /** Crea una carta basica, per a robar de la baralla en el blackJack*/
    public Card(String cardName, String context, boolean ownerIA){
        forIA = ownerIA;

        girada = false;
        value = 0;

        this.nomCartes = null;
        this.cardName = cardName;
        ID = Math.random();
        reverseName = null;
        this.context = context;
    }

    /** GETTERS I SETTERS*/
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

    public Stack<String> getNomCartes() {
        return nomCartes;
    }

    public void setNomCartes(Stack<String> nomCartes) {
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

    public boolean isGirada() {
        return girada;
    }

    public void setGirada(boolean girada) {
        this.girada = girada;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
}