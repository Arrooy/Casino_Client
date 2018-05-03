package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Controlador.DraggableWindow;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static Model.Model_BJ.*;
import static java.lang.Thread.sleep;

public class BlackJackController implements GraphicsController {

    private final static int MIN_BUTTON_SPEED = 320;
    private static final String USER_LOST_TEXT = "Has perdut";
    private static final String IA_LOST_TEXT = "Has ganyat!";
    private static final String USER_LOST_TEXT_INSTANT = "Derrota...";

    private static final String SUB_USER_LOST_TEXT = "Has perdut";
    private static final String SUB_IA_LOST_TEXT = "Has ganyat!";
    private static final String SUB_USER_LOST_TEXT_INSTANT = "Per a guanyar, has de superar el valor del teu contrincant";

    private final long ANIMATION_TIME = 3000;
    private long AnimationTimer;

    private static final int INICI = 1;
    private static final int JOC = 2;
    private static final int GAME_OVER = 2;


    private BlackJackView blackJackView;
    private NetworkManager networkManager;
    private int mouseX,mouseY;
    private GraphicsManager gp;
    private Model_BJ model;
    private long lastClick;
    private long lastCard;

    private boolean usuariDone;
    private boolean gameOver;
    private String gameOverText;
    private String subText;

    private String userScore;
    private String IAScore;

    private AnimacioConjuntCartes animacio;

    private DraggableWindow dw;
    private boolean stopMusicOneTime;
    private Controller controller;

    private boolean firstTimeOpened;

    public BlackJackController(BlackJackView blackJackView, NetworkManager networkManager){
        this.model = new Model_BJ();
        this.networkManager = networkManager;
        this.blackJackView  = blackJackView;

        this.firstTimeOpened = true;

        gameOverText = "Game Over";
        subText = "Click to exit the game";
        stopMusicOneTime = true;

        initGame();
    }

    public void initGame(){
        lastClick = 0;
        gameOver = false;
        usuariDone = false;
        stopMusicOneTime = true;

        this.model.clearData();

        userScore = "0";
        IAScore = "0";

        this.gp = new GraphicsManager(blackJackView,this);
        gp.setClearColor(Color.red);
        lastCard = 0;

        gp.setClearImage(AssetManager.getImage("BJbackground.png"));
        if(firstTimeOpened) {
            animacio = new AnimacioConjuntCartes(1, 100, 100, 150, 0.95, blackJackView.getWidth(), blackJackView.getHeight());
            AnimationTimer = System.currentTimeMillis();
        }
    }


    public void updateSizeBJ(){
        gp.resize(blackJackView.getWidth(),blackJackView.getHeight());
    }

    public synchronized void newBJCard(Card cartaResposta, Controller c) {
        if(controller == null)
            controller = c;

        if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER)){
            model.giraIA();
        }

        if(cartaResposta.getDerrota().equals("user-instant")) {
            gameOver = true;
        }else{
            model.addCard(cartaResposta);
        }

        System.out.println("Adding scores: User " + model.getValueUser() + " ----- IA " + model.getValueIA());

        userScore = String.valueOf(model.getValueUser());
        IAScore = String.valueOf(model.getValueIA());

        if(cartaResposta.getDerrota().equals("false")){
            if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER))
                networkManager.newCardForIaTurn();
        }else if(cartaResposta.getDerrota().equals("user")) {
            gameOver = true;
        }else if(cartaResposta.getDerrota().equals("IA")) {
            gameOver = true;
        }
        System.out.println("Card: " + cartaResposta.getCardName() + "  GAMEOVER? " + gameOver);
    }

    private void exitGame(){
        gp.exit();
        firstTimeOpened = false;
        controller.showGamesView();
    }

    private void exitInGame(){
        gp.exit();
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
        mouseX = e.getX();
        mouseY = e.getY();
    }


    @Override
    public void init() {
        Sounds.play("cardShuffle.wav");
    }

    @Override
    public void update(float delta) {
        if(System.currentTimeMillis() - AnimationTimer <= ANIMATION_TIME){
            updateAnimation();
        }else {
            if(stopMusicOneTime) {
                Sounds.stopOneAudioFile("cardShuffle.wav");
                stopMusicOneTime = false;
            }
            updateCardsPosition(model.getIACards(), MARGIN_TOP, 1);
            updateCardsPosition(model.getUserCards(), blackJackView.getSize().height - CARD_HEIGHT - MARGIN_BOTTOM, -1);
        }
    }

    private void updateAnimation() {
        if(System.currentTimeMillis() - lastCard >= 100){
            if(animacio != null)
                animacio.add(0,0, 125, 0.97, blackJackView.getWidth(), blackJackView.getHeight());

            lastCard = System.currentTimeMillis();
        }

        if(animacio != null)
            animacio.updateCards(blackJackView.getWidth(),blackJackView.getHeight());
    }

    private void renderAnimation(Graphics g) {
        if(animacio != null) {
            animacio.displayCards(g);
            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, blackJackView.getWidth() / 9));
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            g.setColor(new Color(200, 100, 50));
            g.drawString("Loading", blackJackView.getWidth() / 2 - metrics.stringWidth("Loading") / 2, blackJackView.getHeight() / 2);
        }
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
        if(System.currentTimeMillis() - AnimationTimer <= ANIMATION_TIME){
            renderAnimation(g);
        }else{
            if(model.areCardsLoaded())
            renderGame(g);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(gameOver){
            exitGame();
        }
        networkManager.newBlackJackCard(false);
        networkManager.newCardForIaTurn();
    }

    private void renderGame(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        drawCards(g);
        //drawButtons(g);
        //drawChips(g);
        if(gameOver){

            g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 30));
            FontMetrics metrics = g.getFontMetrics(g.getFont());

            g.setColor(new Color(100,200,100,100));
            g.drawRect(0,0,blackJackView.getWidth(),blackJackView.getHeight());

            g.setColor(new Color(255,255,255,100));
            g.drawString(gameOverText,blackJackView.getWidth()/2 - metrics.stringWidth(gameOverText)/2,blackJackView.getHeight()/3);
            g.drawString(subText,blackJackView.getWidth()/2 - metrics.stringWidth(subText)/2,(int)((float)blackJackView.getHeight()/2.5));

            g.drawString("User final score :" + userScore, 100, blackJackView.getHeight() / 2);
            g.drawString("IA final score: " + IAScore, blackJackView.getWidth() - 100 - metrics.stringWidth(("IA final score: " + IAScore)), blackJackView.getHeight() / 2);

        }else{
            g.drawString("User :" + userScore, 100, blackJackView.getHeight() / 2);
            g.drawString("IA: " + IAScore, blackJackView.getWidth() - 100, blackJackView.getHeight() / 2);
        }
    }

    private void drawButtons(Graphics2D g) {
        g.setColor(new Color(200,200,100));

        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 20));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.drawString("asd",metrics.stringWidth("asd"),blackJackView.getHeight() - 150);
        g.fillRect(10,blackJackView.getHeight() - 125,10 + metrics.stringWidth("asd"),blackJackView.getHeight() - 175);

    }

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

}
