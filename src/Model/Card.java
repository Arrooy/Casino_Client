package Model;

import Network.Message;

import java.util.Stack;


/** Carta per a jugar al blackJack*/
public class Card extends Message {

    /** Nom de la imatge de la carta*/
    private String cardName;

    /** Nombre del dorso de la carta*/
    private String reverseName;

    /** Valor de la carta dins del blackJack*/
    private int value;

    /** Defineix el identificador del missatge. Util per quan s'envia amb el networkManager*/
    private final double ID;

    /** Context del missatge*/
    private String context;

    /** Conjunt de cartes de la baralla*/
    private Stack<String> nomCartes;

    /** Indica si la carta es per a un jugador o per a la ia.*/
    private boolean forIA;

    /** Inidica si la carta esta girada o no*/
    private boolean girada;

    /** Coordenades de la carta en el joc del BJ*/
    private int x;
    private int y;

    /** Indica si l'usuari ha perdut la partida o no. En el cas de ser empat, equival a empat*/
    private String derrota;

    /** Indica quants As té el jugador valent 11*/
    private int valent11;

    /** Valor de l'aposta que vol realitzar l'usuari*/
    private long bet;

    /** Indica si la aposta es correcte*/
    private boolean isBetOk;

    /** Valor de la wallet actual del usuari, nomes de lectura per al client*/
    private long wallet;

    /**
     * Crea una nova carta per a iniciar el blackJack
     * @param cardName nom de la carta
     * @param bet aposta que ha realitzat l'usuari al inici de la partida
     * @param context context de la carta. Indica la situacio en la que s'ha llançat la carta
     * @param nomCartes nom de totes les cartes de la baralla
     * @param ownerIA indica si la carta es per a la IA
     */
    public Card(String cardName,long bet, String context, Stack<String> nomCartes, boolean ownerIA){
        forIA = ownerIA;

        this.bet = bet;
        this.isBetOk = false;

        girada = false;
        value = 0;

        valent11 = 0;

        this.nomCartes = nomCartes;
        this.cardName = cardName;
        ID = Math.random();
        reverseName = null;
        this.context = context;

        //Es defineix una coordenada fora de la pantalla, per no mostrar la carta directament al carregarla al tauler
        this.y = -300;
    }

    /**
     * Crea una carta basica, per a robar de la baralla en el blackJack
     * @param cardName nom de la carta
     * @param context context de la carta. Indica la situacio en la que s'ha llançat la carta
     * @param ownerIA indica si la carta es per a la IA
     */
    public Card(String cardName, String context, boolean ownerIA){
        forIA = ownerIA;

        girada = false;
        value = 0;

        valent11 = 0;

        this.nomCartes = null;
        this.cardName = cardName;
        ID = Math.random();
        reverseName = null;
        this.context = context;

        //Es defineix una coordenada fora de la pantalla, per no mostrar la carta directament al carregarla al tauler
        this.y = -300;
    }

    /** GETTERS I SETTERS*/
    public boolean isForIA() {
        return forIA;
    }

    public void setForIA(boolean a) {
        forIA = a;
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

    public void setDerrota(String derrota) {
        this.derrota = derrota;
    }

    public String getDerrota() {
        return derrota;
    }

    public int getValent11() {
        return valent11;
    }

    public void setValent11(int valent11) {
        this.valent11 = valent11;
    }

    public long getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public boolean isBetOk() {
        return isBetOk;
    }

    public void setBetOk(boolean betOk) {
        isBetOk = betOk;
    }

    public long getWallet() {
        return wallet;
    }

    public void setWallet(long wallet) {
        this.wallet = wallet;
    }
}