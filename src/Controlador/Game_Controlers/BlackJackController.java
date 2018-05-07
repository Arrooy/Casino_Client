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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import static Model.Model_BJ.*;
import static Network.Transmission.CONTEXT_BJ_INIT;
import static java.lang.Thread.sleep;

/** Gestiona el joc blackJack */

public class BlackJackController implements GraphicsController {

    private final char MONEY_SYMBOL = '€';
    private final long ANIMATION_TIME = 3000;

    private long AnimationTimer;

    private BlackJackView blackJackView;
    private NetworkManager networkManager;
    private GraphicsManager gp;

    private Model_BJ model;
    private long lastCard;

    private boolean gameOver;
    private String gameOverText;
    private String subText;

    private String userScore;
    private String IAScore;

    private AnimacioConjuntCartes animacio;

    private boolean stopMusicOneTime;
    private Controller controller;

    private boolean firstTimeOpened;
    private long bet;
    private long moneyToSpend;


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
        gameOver = false;
        stopMusicOneTime = true;
        moneyToSpend = 0;

        this.model.clearData();

        userScore = "0";
        IAScore = "0";

        this.gp = new GraphicsManager(blackJackView,this);
        gp.setClearColor(Color.green);
        lastCard = 0;

        if(firstTimeOpened) {
            animacio = new AnimacioConjuntCartes(1, 100, 100, 150, 0.95, blackJackView.getWidth(), blackJackView.getHeight());
            AnimationTimer = System.currentTimeMillis();
        }
    }


    public void updateSizeBJ(){
        gp.resize(blackJackView.getWidth(),blackJackView.getHeight());
    }

    public synchronized void newBJCard(Card cartaResposta, Controller c) {

        if(cartaResposta.getContext().equals(CONTEXT_BJ_INIT)){
            bet = cartaResposta.getBet();
            moneyToSpend = cartaResposta.getWallet();
        }

        if(controller == null)
            controller = c;

        if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER)){
            model.giraIA();
        }

        if(cartaResposta.getDerrota().equals("user-instant")) {
            gameOverText = "You loose " + bet + MONEY_SYMBOL;
            subText = "Click to exit the game";
            gameOver = true;
        }else{
            model.addCard(cartaResposta);
        }

        userScore = String.valueOf(model.getValueUser());
        IAScore = String.valueOf(model.getValueIA());

        if(cartaResposta.getDerrota().equals("false")){
            if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER))
                networkManager.newCardForIaTurn();

        }else if(cartaResposta.getDerrota().equals("user")) {
            gameOverText = "You loose " + bet + MONEY_SYMBOL;
            subText = "Click to exit the game";
            gameOver = true;
        }else if(cartaResposta.getDerrota().equals("IA")) {
            if(model.getValueUser() == 21) {
                gameOverText = "You win " + bet * 1.5 + MONEY_SYMBOL;
            }else{
                gameOverText = "You win " + bet * 2.0 + MONEY_SYMBOL;
            }
            subText = "Click to exit the game";
            gameOver = true;
        }
    }

    private void exitGame(){
        gp.exit();
        controller.showGamesView();
    }

    private void exitInGame(){
        gp.exit();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(firstTimeOpened) {
            firstTimeOpened = false;
        }else{
            if(gameOver) {
                if(e.getKeyChar() == 'r' || e.getKeyChar() == 'R'){
                    networkManager.initBlackJack(Baralla.getNomCartes(), controller.manageBJBet());
                    gp.exit();
                }else{
                    exitGame();
                }
            }else{
                if (e.getKeyChar() == '+') {
                        networkManager.newBlackJackCard(false);
                        Sounds.play("cardPlace1.wav");
                } else if(e.getKeyChar() == ' ') {
                        networkManager.newCardForIaTurn();
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 27 && gameOver)
            exitGame();
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
            gp.requestFocus();
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
        if(firstTimeOpened){
            firstTimeOpened = false;

        }else{
            if(gameOver){
                exitGame();
            }
        }
    }

    private void renderGame(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        int width = blackJackView.getWidth();
        int height = blackJackView.getHeight();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(AssetManager.getImage("BJbackground.png"),0,0,width,height,null);

        if(firstTimeOpened){
            showHowToPlay(g,width,height);
        }else{
            drawCards(g);
            if(gameOver){
                g.setColor(new Color(20,20,20,230));
                g.fillRect(0,0,width,height);

                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, width/10));
                FontMetrics metrics = g.getFontMetrics(g.getFont());

                g.setColor(new Color(255,255,255));
                g.drawString(gameOverText,width/2 - metrics.stringWidth(gameOverText)/2,height/3);

                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, width/40));
                metrics = g.getFontMetrics(g.getFont());

                g.drawString("Press R to restart",75,(int)((float)height/1.5));

                g.drawString("Press ESC to exit",width - metrics.stringWidth(subText),(int)((float)height/1.5));
                g.drawString("User final score :" + userScore, 75, height / 2);
                g.drawString("IA final score: " + IAScore, width - 100 - metrics.stringWidth(("IA final score: " + IAScore)), height / 2);

            }else{
                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, width/50));
                FontMetrics metrics = g.getFontMetrics(g.getFont());

                g.drawString("Cards score :" + userScore, 50, height / 2);
                g.drawString("Bet: " + bet, width - 75 - metrics.stringWidth(("Bet: " + bet)), 75);
                g.drawString("User wallet: " + moneyToSpend + "[" + (moneyToSpend - bet) + " / " + (moneyToSpend + bet*1.5) + "]", 50, height - metrics.getAscent() * 2);
            }
        }
    }

    private void showHowToPlay(Graphics2D g,int width,int height) {
        g.setColor(new Color(20,20,20,230));
        g.fillRect(0,0,width,height);
        Image image = AssetManager.getImage("tutoBJ.png");
        if(image.getWidth(null) > blackJackView.getWidth() || blackJackView.getHeight() < image.getHeight(null)){
            g.drawImage(image,0,0,blackJackView.getWidth(),blackJackView.getHeight(),null);
        }else{
            g.drawImage(image,width/2 - image.getWidth(null)/2,10,null);
        }

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

