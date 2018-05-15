package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Controlador.Sounds;

import Model.AssetManager;
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
import static Network.Transmission.CONTEXT_BJ_INIT;

/** Gestiona el joc blackJack, la seva animacio inicial, el tutorial i el gameOver */

public class BlackJackController implements GraphicsController {

    /** Simbol que es mostra en el gameOverScreen*/
    private final char MONEY_SYMBOL = '€';

    /** Temps que dura la animacio inicial*/
    private final long ANIMATION_TIME = 3000;

    /** Controla la posicio de cadascuna de les cartes de l'animacio inicial*/
    private AnimacioConjuntCartes animacio;

    /** Controla el temps transcorregut de l'animacio inicial*/
    private long AnimationTimer;

    /** Controla el temps desde que s'ha llençat l'ultima carta de l'animacio inicial*/
    private long lastCard;

    /** Vista del joc*/
    private BlackJackView blackJackView;

    /** Gestiona els graphics de la vista del joc*/
    private GraphicsManager gp;

    /** Network manager encarregat de solicitar cartes al servidor*/
    private NetworkManager networkManager;

    /** Model fictici del joc, es guarda l'informacio de les cartes rebudes pel servidor*/
    private Model_BJ model;

    /** Indica si la partida s'ha acabat*/
    private boolean gameOver;

    /** Text que apareix al finaltizar la partida*/
    private String gameOverText;

    /** Subtext que apareix al finalitzar la partida*/
    private String subText;

    /** Valor de les cartes actual del jugador*/
    private String userScore;

    /** Valor de les cartes actual de la IA*/
    private String IAScore;

    /** Boolea que para el so inicial de la partida un finalitza l'animacio inicial*/
    private boolean stopMusicOneTime;

    /** Controlador principal del programa*/
    private Controller controller;

    /** Aposta plantejada per l'usuari*/
    private long bet;

    /** Diners de l'usuari per a gastar*/
    private long moneyToSpend;
    private boolean dontjumpFirstIteration;

    private boolean showTutorial;

    private boolean animationIsOver;

    /** Crea un controlador del blackjack*/
    public BlackJackController(BlackJackView blackJackView, NetworkManager networkManager){
        this.model = new Model_BJ();
        this.networkManager = networkManager;
        this.blackJackView  = blackJackView;

        this.showTutorial = true;

        //S'inicialitzan els textos del GameOver
        gameOverText = "Game Over";
        subText = "Click to exit the game";
        stopMusicOneTime = true;

        animationIsOver = false;

        //S'inicia el grahpics manager i s'inicia lanimacio principal.
        initGame();
    }

    public void initGame(){
        gameOver = false;
        stopMusicOneTime = true;
        moneyToSpend = 0;

        dontjumpFirstIteration = false;

        animationIsOver = false;
        //Es borren les cartes guardades del model
        this.model.clearData();

        userScore = "0";
        IAScore = "0";

        //S'inicialitza el graphics manager, s'inidica que la vista el la del blackJack i que el controlador es aquesta classe
        this.gp = new GraphicsManager(blackJackView,this);
        gp.setClearColor(Color.red);
        lastCard = 0;

        //Es presenta l'animacio inicial
        animacio = new AnimacioConjuntCartes(1, 100, 100, 150, 0.95, blackJackView.getWidth(), blackJackView.getHeight());
        AnimationTimer = System.currentTimeMillis();
    }

    /** Cada cop que la finestra canvia de tamany es crida aquesta funcio. Serverix per a mantenir el panell de pintat
     * del mateix tamany que la vista*/
    public void updateSizeBJ(){
        gp.resize(blackJackView.getWidth(),blackJackView.getHeight());
    }

    /**
     * Cada nova carta que es rebi del servidor, sera processada amb aquesta funcio.
     * @param cartaResposta nova carta a processar.
     * @param c controlador generic del programa per al gameover
     */
    public synchronized void newBJCard(Card cartaResposta, Controller c) {

        //En el cas de ser una de les primeres cartes, s'agafa l'informacio de l'aposta en la partida.
        //Tambe s'agafa el valor de la wallet de l'usuari i es guarda en variables auxiliars.
        if(cartaResposta.getContext().equals(CONTEXT_BJ_INIT)){
            bet = cartaResposta.getBet();
            moneyToSpend = cartaResposta.getWallet();
        }
        //En el cas de no estar definit el controller, es defineix
        if(controller == null)
            controller = c;

        //Si la carta correspon a una carta per a la IA, un cop ha acabat el torn del usuari, es gira
        if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER)){
            model.giraIA();
        }

        //En el cas de que l'usuari, al finalitzar el torn, perdi de cop
        if(cartaResposta.getDerrota().equals("user-instant")) {
            //S'omple el text de game over i s'activa el gameOver
            gameOverText = "You loose " + bet + MONEY_SYMBOL;
            subText = "Click to exit the game";
            gameOver = true;
        }else{
            //De lo contrari, s'afegeix la carta al model
            model.addCard(cartaResposta);
        }

        //S'actualitzan els valors de les cartes que es troben en el model.
        userScore = String.valueOf(model.getValueUser());
        IAScore = String.valueOf(model.getValueIA());


        if(cartaResposta.getDerrota().equals("false")){
            //Si la carta no indica derrota i es el torn de la IA, es demana unaltre carta per la IA
            if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER))
                networkManager.newCardForIaTurn();

        }else if(cartaResposta.getDerrota().equals("user")) {
            //En el cas d'inidicar derrota per l'user, s'indica el missatge i sacaba la partida
            gameOverText = "You loose " + bet + MONEY_SYMBOL;
            subText = "Click to exit the game";
            gameOver = true;
        }else if(cartaResposta.getDerrota().equals("IA")) {
            //En el cas d'inidicar derrota per la IA, s'inidica el valor dels diners obtinguts
            if(model.getValueUser() == 21 && model.getUserCards().size() == 2) {
                gameOverText = "You win " + bet * 1.5 + MONEY_SYMBOL;
            }else{
                gameOverText = "You win " + bet * 2.0 + MONEY_SYMBOL;
            }
            //S'indica el subtext i que s'ha acabat la partida
            subText = "Click to exit the game";
            gameOver = true;
        }
    }

    /** Gestiona el sortir del joc*/
    private void exitGame(){
        gp.exit();
        controller.showGamesView();
        updateSizeBJ();
    }

    public void exitInGame(){
        gp.exit();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key pressed" + e.getKeyChar());
        if (animationIsOver) {

            //En el cas d'estar mostrant el tutorial i premer una tecla, fem desapareixer aquest
            if (showTutorial){
                showTutorial = false;
            } else {
                //En el cas de estar en la gameOverScreen i apretar una tecla es surt del joc
                if (gameOver) {
                    //Si la tecla es la r, es crea una nova partida
                    if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
                        networkManager.initBlackJack(Baralla.getNomCartes(), controller.manageBJBet());
                        gp.exit();
                    } else {
                        exitGame();
                    }
                } else {
                    //En el cas d'estar dins d'una partida i apretar el '+' es solicita una nova carta
                    if (e.getKeyChar() == '+') {
                        networkManager.newBlackJackCard(false);

                        //Es reprodueix el soroll de la carta
                        Sounds.play("cardPlace1.wav");
                    } else if (e.getKeyChar() == ' ') {
                        //En el cas de apretar l'espai, es pasa el torn a la IA
                        networkManager.newCardForIaTurn();
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //En el cas d'apretar la tecla ESC estant en el gameOver, es surt del joc
        if(e.getKeyCode() == 27 && gameOver)
            exitGame();
    }

    @Override
    public void init() {
        //Al iniciar el graphics manager, s'inicia el audio de barrejar les cartes
        Sounds.play("cardShuffle.wav");
    }

    @Override
    public void update(float delta) {
        //Aquest boolea serveix per a saltar la primera iteracio del update, ja que dona errors degut a que no dona
        //temps d'inicialitzar totes les variables
        if(dontjumpFirstIteration) {
            //En el cas d'estar en el temps d'animacio
            if (System.currentTimeMillis() - AnimationTimer <= ANIMATION_TIME) {
                //S'actualitza la posicio de les cartes de l'animacio
                updateAnimation();
            } else {

                //De lo contrari, es para la musica
                if (stopMusicOneTime) {
                    Sounds.stopOneAudioFile("cardShuffle.wav");
                    animationIsOver = true;
                    stopMusicOneTime = false;
                }
                //Es demana focus al teclat
                gp.requestFocus();

                //S'actualitza la posicio de les cartes al tauler
                updateCardsPosition(model.getIACards(), MARGIN_TOP, 1);
                updateCardsPosition(model.getUserCards(), blackJackView.getSize().height - CARD_HEIGHT - MARGIN_BOTTOM, -1);
            }
        }
        dontjumpFirstIteration = true;
    }

    //Gestiona la generacio de cartes a l'animacio inicial
    private void updateAnimation() {
        //Cada 100 ms s'afegeix una nova carta a l'animacio inicial
        if(System.currentTimeMillis() - lastCard >= 150){
            //Si l'animacio ja s'ha creat, afegeix una carta
            if(animacio != null)
                animacio.add(0,0, 125, 0.97, blackJackView.getWidth(), blackJackView.getHeight());

            lastCard = System.currentTimeMillis();
        }
        //Si l'animacio ja esta creada, actualitza la posicio de les cartes
        if(animacio != null)
            animacio.updateCards(blackJackView.getWidth(),blackJackView.getHeight());
    }

    //Pinta les cartes de l'animacio en els graphics que obte per parametres.
    private void renderAnimation(Graphics g) {
        //En el cas d'estar creada l'animacio
        if(animacio != null) {
            //Es fa display de totes les cartes que hi figuren
            animacio.displayCards(g);
        }
    }
    //Actualitza la posicio de les cartes del tauler un cop el joc esta funcionant
    //En el cas de rebre mes de 4 cartes, aquestes s'apilen una a sobre de laltre.
    //Amb cada nova carta afegida, s'actualitzan totes les coordenades de les cartes.
    private void updateCardsPosition(LinkedList<Card> cardsInHand, int marginTop, int direction) {
        //Si existeixen cartes a la ma
        if(!cardsInHand.isEmpty()){
            int posicioInicialEsquerra;
            //Si el numero de cartes es menor al maxim de cartes per ma
            if(cardsInHand.size() <= MAX_CARDS_IN_HAND) {
                //Es posicionen les cartes de forma normal
                posicioInicialEsquerra = (blackJackView.getSize().width / 2) - (cardsInHand.size() / 2) * CARD_WIDTH;
            }else{
                //Es posicionen les cartes una sobre laltre
                posicioInicialEsquerra = (blackJackView.getSize().width / 2) - 3 * CARD_WIDTH;
            }

            int shiftCardsLeft = 0;

            //Controlem el nombre de layers que s'han de crear
            int numberOfLayers = (cardsInHand.size() - 1) / (MAX_CARDS_IN_HAND);

            //En el cas de tenir 1 layer, busquem si es necesari fer un shift a la esquerra per quadrar les cartes
            if(cardsInHand.size() <= MAX_CARDS_IN_HAND) {
                if (cardsInHand.size() % 2 != 0)
                    shiftCardsLeft = CARD_WIDTH/2 ;
            }

            //Per totes les cartes de la ma
            for (int i = 0; i < cardsInHand.size(); i++) {
                //Es decideix la layer de la carta
                if ((i + 1) % (MAX_CARDS_IN_HAND + 1) == 0) {
                    numberOfLayers--;
                }

                int newPosX;
                //Es decideix la nova posicio de la carta
                if(i+1 <= (MAX_CARDS_IN_HAND)) {
                    newPosX = posicioInicialEsquerra + (CARD_WIDTH + MARGIN_BETWEEN_CARDS) * (i + 1);
                }else{
                    newPosX = posicioInicialEsquerra + (CARD_WIDTH + MARGIN_BETWEEN_CARDS) * (i%MAX_CARDS_IN_HAND + 1);
                }
                //Es guarden les coordenades de la carta
                cardsInHand.get(i).setCoords(newPosX - CARD_WIDTH - shiftCardsLeft, marginTop + (50 * numberOfLayers)*direction);
            }
        }
    }

    @Override
    public void render(Graphics g) {
        //Si estem en el temps inicial d'animacio, renderitzem l'animacio
        if(System.currentTimeMillis() - AnimationTimer <= ANIMATION_TIME){
            renderAnimation(g);
        }else{
            //De lo contrari, si les cartes estan carregades, les mostrem
            if(model.areCardsLoaded())
                renderGame(g);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (System.currentTimeMillis() - AnimationTimer > ANIMATION_TIME) {
            //Si l'usuari esta al tutorial inicial, i fa click, aquest desapareix
            if(showTutorial){
                showTutorial= false;
            }else{
                //Si l'usuari esta en el gameOver i fa click, es surt del joc
                if(gameOver){
                    exitGame();
                }
            }
        }
    }

    //Renderitza les cartes i la UI del joc
    private void renderGame(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;

        //Es guarden les dimensions de la screen
        int width = blackJackView.getWidth();
        int height = blackJackView.getHeight();

        //Es defineix el renderHint per al text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //Es pinta el fons de pantalla
        g.drawImage(AssetManager.getImage("BJbackground.png"),0,0,width,height,null);

        //En el cas de ser el primer cop que s'obra el joc, es mostra el tutorial
        if(showTutorial){
            showHowToPlay(g,width,height);
        }else{
            //De lo contrari, es pinten les cartes
            drawCards(g);

            if(gameOver){
                //Si s'ha acabat la partida, mostrem la pantalla de gameOver
                g.setColor(new Color(20,20,20,230));
                g.fillRect(0,0,width,height);

                g.setFont(AssetManager.getEFont(width/10));
                FontMetrics metrics = g.getFontMetrics(g.getFont());

                g.setColor(new Color(216, 202, 168));
                g.drawString(gameOverText,width/2 - metrics.stringWidth(gameOverText)/2,height/3);

                g.setFont(AssetManager.getEFont(width/40));
                metrics = g.getFontMetrics(g.getFont());

                g.drawString("Press R to restart",75,(int)((float)height/1.5));

                g.drawString("Press ESC to exit",width - metrics.stringWidth(subText),(int)((float)height/1.5));
                g.drawString("User final score :" + userScore, 75, height / 2);
                g.drawString("IA final score: " + IAScore, width - 100 - metrics.stringWidth(("IA final score: " + IAScore)), height / 2);

            }else{
                //Si la partida no ha acabat, es mostra informacio bàsica de la UI
                g.setFont(AssetManager.getEFont(width/50));
                FontMetrics metrics = g.getFontMetrics(g.getFont());

                g.setColor(new Color(216, 202, 168));

                g.drawImage(AssetManager.getImage("Num.png",metrics.stringWidth(("Bet: " + bet + MONEY_SYMBOL)) + 20,metrics.getAscent() + 20),50 - 10,75 - metrics.getAscent() - 7,null);
                g.drawString("Bet: " + bet + MONEY_SYMBOL, 50, 75);

                g.drawImage(AssetManager.getImage("Num.png",metrics.stringWidth(("Cards score: " + userScore)) + 20,metrics.getAscent() + 20),width - metrics.stringWidth(("Cards score :" + userScore)) - 15 - 10,height - metrics.getAscent() * 2 - metrics.getAscent() - 7,null);
                g.drawString("Cards score: " + userScore, width - metrics.stringWidth(("Cards score: " + userScore)) - 15, height - metrics.getAscent() * 2);

                g.drawImage(AssetManager.getImage("Num.png",metrics.stringWidth(("Wallet: " + moneyToSpend + MONEY_SYMBOL)) + 20,metrics.getAscent() * 3  + 20),50 - 10,height - metrics.getAscent() * 4 - metrics.getAscent() - 7,null);
                g.drawString("Wallet: " + moneyToSpend + MONEY_SYMBOL, 50, height - metrics.getAscent() * 4);
                g.drawString("Profit: " + bet * 2.0 + MONEY_SYMBOL, 50, height - metrics.getAscent() * 2);

            }
        }
    }

    //Mostra la imatge del tutorial
    private void showHowToPlay(Graphics2D g,int width,int height) {
        g.setColor(new Color(20,20,20,230));
        g.fillRect(0,0,width,height);
        g.setColor(new Color(216, 202, 168));
        g.setFont(AssetManager.getEFont(width/12));
        FontMetrics m = g.getFontMetrics(g.getFont());

        int imageWidth = m.stringWidth("How to play");
        g.drawImage(AssetManager.getImage("Num.png",imageWidth + 20,m.getHeight() + 10),width / 2 - imageWidth / 2 - 10, 25,null);
        g.drawString("How to play",width / 2 - imageWidth / 2,m.getAscent() + 25);

        g.setFont(AssetManager.getEFont(height/14));
        m = g.getFontMetrics(g.getFont());

        Image image = AssetManager.getImage("SBBJ.png");

        g.drawImage(AssetManager.getImage("SBBJ.png"),width / 2 - 50 - m.stringWidth("Request a card")/3 - image.getWidth(null),height - m.getAscent() * 5 - m.getAscent()/2 - image.getHeight(null)/2 ,null);
        image = AssetManager.getImage("BBBJ.png");
        g.drawImage(AssetManager.getImage("BBBJ.png"),width / 2 - m.stringWidth("Finish turn")/3 - image.getWidth(null),height - m.getAscent() * 2 - m.getAscent()/2 - image.getHeight(null)/2,null);

        g.drawString("Request a card",width / 2 + 50 - m.stringWidth("Request a card")/3, height - m.getAscent() * 5);
        g.drawString("Finish turn",width / 2 + 50 - m.stringWidth("Finish turn")/3, height - m.getAscent() * 2);
    }

    //Dibuixa les cartes del tauler
    private void drawCards(Graphics2D g) {
        if (model.IAHasCards()) {
            int arraySize = model.getIACards().size();
            for (int i = 0; i < arraySize; i++) {
                Card card = model.getIACards().get(i);
                g.drawImage(Baralla.findImage(card), card.getX(), card.getY(), null);
            }
        }
        if (model.userHasCards()) {
            int arraySize = model.getUserCards().size();
            for (int i = 0; i < arraySize; i++) {
                Card card = model.getUserCards().get(i);
                g.drawImage(Baralla.findImage(card), card.getX(), card.getY(), null);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}

