package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Utils.Sounds;
import Utils.AssetManager;
import Model.HorseRace_Model.HorseBet;
import Model.HorseRace_Model.HorseMessage;
import Model.HorseRace_Model.HorseRaceModel;
import Model.User;
import Network.NetworkManager;
import Network.Transmission;
import Utils.Countdown;
import Vista.GameViews.HorseRaceView;
import Vista.GraphicsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;


/**Controlador de la cursa de cavalls encarregat a indidicar en el Graphics manager quines coses mostrar i a comunicar-se amb el
 * networkManager per establir una communicacio amb el servidor i aixi reroduir les curses correctament*/
public class HorseRaceController implements GraphicsController, ActionListener {

    /**Colors*/
    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color GREY = new Color(49, 63, 47);
    private final static Color TEXT_COLOR = new Color(216, 204, 163);

    /**nombre de cavalls i seccions*/
    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;

    /**Modes de visualitzacio*/
    private static final int GAME_MODE = 0;
    private static final int LIST_MODE = 1;
    /**Dimensions de la llista d'apostes*/
    private static final int LIST_DIM = 700;

    /**Posicions relatives dels missatges i cavalls*/
    private static final double HORSE_START_Y = 0.026;
    private static final double HORSE_START_X = 0.252;
    private static final double HORSE_SEPARATION = 0.074;
    private static final double HORSE_END_X = 0.895;
    private static final double HORSE_SECTION = (HORSE_END_X - HORSE_START_X) /(double)(SECTIONS); // Per reconstruir: HORSE_SECTION * section + HORSE_START_X
    private static final double TIME_MESSAGE_Y = 0.23;
    private static final double TIME_MESSAGE_X = 0.045;
    private static final double WINNER_MESSAGE_X = TIME_MESSAGE_X;
    private static final double WINNER_MESSAGE_Y = TIME_MESSAGE_Y + 0.06;
    private static final double WALLET_TITLE_X = TIME_MESSAGE_X;
    private static final double WALLET_TITLE_Y = WINNER_MESSAGE_Y + 0.06;
    private static final double WALLET_MESSAGE_X = TIME_MESSAGE_X + 0.01;
    private static final double WALLET_MESSAGE_Y = WALLET_TITLE_Y + 0.035;
    private static final double EARNINGS_TITLE_X = TIME_MESSAGE_X;
    private static final double EARNINGS_TITLE_Y = WALLET_MESSAGE_Y + 0.06;
    private static final double EARNINGS_MESSAGE_X = WALLET_MESSAGE_X;
    private static final double EARNINGS_MESSAGE_Y = EARNINGS_TITLE_Y + 0.035;
    private static final double BET_TITLE_X = TIME_MESSAGE_X;
    private static final double BET_TITLE_Y = 0.7;
    private static final double BET_STATUS_X = BET_TITLE_X;
    private static final double BET_STATUS_Y = BET_TITLE_Y + 0.036;
    private static final double BET_RESULT_X = BET_TITLE_X;
    private static final double BET_RESULT_Y = BET_STATUS_Y + 0.06;

    /**Permet la gestio de grafics*/
    private static GraphicsManager graphicsManager;
    /**Permet comunicar-se amb el servdor*/
    private NetworkManager networkManager;
    /**Vista del joc*/
    private HorseRaceView horseRaceView;
    /**Usuari que esta jugant*/
    private User user;
    /**Model del joc*/
    private HorseRaceModel horseRaceModel;


    /**Indica si s'esta fent una carrera*/
    private boolean isRacing;
    /**Indica si s'esta apostant*/
    private boolean isBetting;
    /**Indica si s'esta jugant*/
    private boolean play;
    /**Inica si estem esperant a correr*/
    private boolean isCountDown;
    /**Indica si s'ha de mostrar el resultat d'una aposta*/
    private boolean betResult;
    /**indica si es la primera carrera que fem*/
    private boolean firstRace;
    /**Permet enviar el missatge de carrera acabada una vegada per carrera*/
    private boolean oncePerRace;
    /**Indica si l'aposta s'ha pogut fer*/
    private boolean betOK;
    /**Indica si s'ha rebut confirmacio de l'aposta*/
    private boolean confirmReceived;

    /**Temps d'espera per començar la cursa*/
    private Countdown waitCountdown;
    /**Temps d'espera per acabar la cursa*/
    private Countdown raceCountdown;

    /**Temps per que cada cavall acab una secco*/
    private Countdown[] horseCountdowns;
    /**Posicio de cada cavall*/
    private Point[] horsePositions;
    /**Frame de cada cavall*/
    private int[] horseFrames;
    /**Seccio de cada cavall*/
    private int[] horseSections;

    /**Guanyador de la cursa*/
    private int winner;
    /**Velocitat d'animacio dels cavalls*/
    private long animationRate;
    /**Premi en cas d'aposta*/
    private long prize;
    /**Quantitat apostada*/
    private long betAmount;
    /**Cavall en el que s'ha apostat*/
    private int betHorse;
    /**Font del texte*/
    private Font font;
    /**Indica si mostrar la llista d'apostes o el joc*/
    private int mode;

    /**Imatges necessaries per reproduir el joc*/
    private Image viewList;
    private Image viewListSelected;
    private Image returnButton;
    private Image returnButtonSelected;
    private Image listBackground;
    private Image upButton;
    private Image downButton;
    private Image listTable;

    /**Estat dels botons per la llista*/
    private boolean viewListPressed;
    private boolean returnPressed;

    /**Posicions de la llista d'apostes i botons*/
    private int ebx;
    private int eby;
    private int vlx;
    private int vly;

    /**Array d'informacio per mostrar a la llista d'apostes*/
    private String[][] info;
    private int listOff;

    /**Tamany de la pantalla, utilitzar per escalar el joc en cas de resize*/
    private int frameWidth;
    private int frameHeight;
    /**Guanys relatius en la partida*/
    private int gameEarnings;

    /** Permet la reproduccio una unica vegada d'un arxiu d'audio en el bucle update*/
    private boolean singleAudioPlay;

    public HorseRaceController(HorseRaceView horseRaceView, NetworkManager networkManager) {
        this.horseRaceModel = new HorseRaceModel();
        this.horseRaceView = horseRaceView;
        this.networkManager = networkManager;
        this.isRacing = false;
        this.waitCountdown = new Countdown();
        this.raceCountdown = new Countdown();
        this.horseCountdowns = new Countdown[MAX_HORSES];
        this.horsePositions = new Point[MAX_HORSES];
        this.horseFrames = new int[MAX_HORSES];
        this.horseSections = new int[MAX_HORSES];
        for (int i = 0; i < MAX_HORSES; i++) {
            this.horseCountdowns[i] = new Countdown();
            this.horsePositions[i] = new Point(0, 0);
            this.horseFrames[i] = 0;
            this.horseSections[i] = 0;
        }
        this.play = false;
        this.animationRate = 0;
        this.prize = 0;
        this.betAmount = 0;
        confirmReceived = false;
        this.betResult = false;
        this.winner = -1;
        this.firstRace = true;
        this.oncePerRace = true;
        this.mode = GAME_MODE;
        this.listOff = 0;
        this.frameHeight = horseRaceView.getHeight();
        this.frameWidth = horseRaceView.getWidth();
        this.gameEarnings = 0;
    }


    /**
     * Inicialitza el GraphicsManager
     */
    private void setGraphics() {
        graphicsManager = new GraphicsManager(this.horseRaceView, this);
        graphicsManager.setClearColor(Color.black);
    }

    /**
     * S'afegeix l'usuari que esta jugant al joc
     * @param user Usuari que esta jugant
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * S'inicialitzen variables i assets per tal de reproduir les carreres de manera correcte
     */
    @Override
    public void init() {
        this.isRacing = false;
        this.isCountDown = false;
        this.firstRace = true;
        this.oncePerRace = true;

        this.isBetting = false;
        this.confirmReceived = false;
        this.betResult = false;
        this.betHorse = 0;
        this.betOK = false;
        this.gameEarnings = 0;

        this.waitCountdown.stopCount();
        this.raceCountdown.stopCount();

        this.font = AssetManager.getEFont(20);

        this.mode = GAME_MODE;

        this.viewList = AssetManager.getImage("VG.png");
        this.viewListSelected = AssetManager.getImage("VGS.png");
        this.returnButton = AssetManager.getImage("EXIT_NO_SOMBRA.png");
        this.returnButtonSelected = AssetManager.getImage("EXIT_SOMBRA.png");

        this.viewListPressed = false;
        this.returnPressed = false;

        this.listBackground = AssetManager.getImage("background.png");
        this.upButton = AssetManager.getImage("SUB.png");
        this.downButton = AssetManager.getImage("BAJ.png");
        this.listTable = AssetManager.getImage("POnline.png");

        this.vlx = 20;
        this.vly = Controller.getWinHeight() - viewList.getHeight(null) - 20;
        this.ebx = Controller.getWinWidth() - returnButton.getHeight(null) - 20;
        this.eby = Controller.getWinHeight() - returnButton.getHeight(null) - 20;

        this.info = new String[0][0];

        this.frameHeight = horseRaceView.getHeight();
        this.frameWidth = horseRaceView.getWidth();

        endYourHorses();
    }

    /**
     * Començem a jugar, s'inicialitzen variables , comptadors, posicions i frames dels cavalls
     */
    public void play() {

        this.play = true;
        this.isRacing = false;
        this.isCountDown = false;
        this.firstRace = true;
        this.oncePerRace = true;
        this.waitCountdown.stopCount();
        this.raceCountdown.stopCount();

        this.frameHeight = horseRaceView.getHeight();
        this.frameWidth = horseRaceView.getWidth();

        this.gameEarnings = 0;
        this.isBetting = false;
        this.betResult = false;
        this.betHorse = 0;
        this.betOK = false;
        this.confirmReceived = false;

        this.mode = GAME_MODE;
        this.listOff = 0;


        setGraphics();
        endYourHorses();

        resetBetList();
        requestWallet();
    }

    /**
     * Parem de jugar, tot indicant al servidor de que ens desconecti del joc
     */
    private void stopPlay() {

        this.play = false;
        this.isRacing = false;
        this.isCountDown = false;
        this.oncePerRace = false;

        this.isBetting = false;
        this.betResult = false;
        this.confirmReceived = false;
        this.betOK = false;
        this.gameEarnings = 0;

        this.waitCountdown.stopCount();
        this.raceCountdown.stopCount();

        graphicsManager.exit();
        this.networkManager.exitHorses();
    }

    /**
     * Metode en el que controlem la carrera i la comunicacio amb el servidor a traves de les classes de NetworkManager i GraphicsManager
     * @param delta Periode d'actualitzacio de la pantalla (0.017s)
     */
    @Override
    public void update(float delta) {
        HorseMessage horseMessage;

        this.vlx = 20;
        this.vly = Controller.getWinHeight() - viewList.getHeight(null) - 20;
        this.ebx = Controller.getWinWidth() - returnButton.getWidth(null) - 20;
        this.eby = Controller.getWinHeight() - returnButton.getHeight(null) - 20;

        this.info = this.networkManager.updateHorseList(info);
        if (play) {
            horseMessage = (HorseMessage) networkManager.readContext("HORSES-WalletRequest");
            if(horseMessage!= null){
                this.user.setWallet(horseMessage.getWallet());
            }

            if (isBetting) {
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-BetConfirm");
                if (horseMessage != null) {
                    this.confirmReceived = true;
                    this.betOK = horseMessage.getHorseBet().isBetOK();
                    requestWallet();
                    if (!betOK) {
                        this.betResult = false;
                    }else{
                        this.gameEarnings-=horseMessage.getHorseBet().getBet();
                    }
                }
            }
            if (!isRacing) {
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Countdown");
                if (horseMessage != null) {
                    this.isBetting = false;
                    this.betOK = false;
                    this.isCountDown = true;
                    this.oncePerRace = true;
                    this.confirmReceived = false;
                    this.waitCountdown.newCount(horseMessage.getTimeForRace());
                    this.singleAudioPlay = true;

                    endYourHorses();
                    requestWallet();
                }
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Schedule");
                if (horseMessage != null) {
                    this.horseRaceModel.setHorseSchedule(horseMessage.getHorseSchedule());
                    this.waitCountdown.stopCount();
                    this.raceCountdown.newCount(horseRaceModel.getHorseSchedule().getRaceTime());
                    this.isRacing = true;
                    this.firstRace = false;
                    this.oncePerRace = true;
                    this.isCountDown = false;
                    this.betResult = false;

                    Sounds.stopOneAudioFile("HStart.wav");
                    Sounds.play("HRun.wav");

                    initRace();
                    //Ja podem reproduir la carrera
                }
            } else {
                moveHorses(horsePositions, horseCountdowns, horseSections, raceCountdown);
                this.waitCountdown.stopCount();
                if (oncePerRace) {
                    if (raceCountdown.getCount() <= 0) {
                        Sounds.stopOneAudioFile("HRun.wav");
                        horseMessage = new HorseMessage((HorseBet) null, "Finished");
                        horseMessage.setID(user.getID());
                        new Transmission(horseMessage, networkManager);
                        this.raceCountdown.stopCount();
                        oncePerRace = false;
                    }
                }
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Result");
                if (horseMessage != null) {
                    this.isRacing = false;
                    this.firstRace = false;
                    this.waitCountdown.stopCount();
                    resetBetList();
                    requestWallet();
                    if (isBetting && betOK) {
                        betResult = true;
                    }
                    this.winner = horseMessage.getHorseResult().getWinner();
                    prize = horseMessage.getHorseResult().getPrize();
                    gameEarnings+=prize;
                }
            }
        }
    }

    /**
     * Inicialitzem als cavalls per tal de reproduir la carrera,
     * s'escull un frame aleatori per cada cavall.
     */
    private void initRace() {
        Random random = new Random();
        int section = 0;
        for(int horse = 0; horse < MAX_HORSES; horse++){
            horseCountdowns[horse].newCount(getTime(horse, section));
            horsePositions[horse] = new Point((int) (HORSE_START_X * horseRaceView.getWidth()), (int) (HORSE_START_Y * (float) horseRaceView.getHeight() + horse * 0.074 * (float) horseRaceView.getHeight()));
            horseFrames[horse] = random.nextInt(7);
            horseSections[horse] = section;
        }
    }

    /**
     * Es retorna el temps d'una seccio per a un cavall
     * @param horse cavall
     * @param section seccio
     * @return temps del cavall "horse" a la secco "section"
     */
    private long getTime(int horse, int section){
        return horseRaceModel.getHorseSchedule().getTime(horse, section);
    }

    /**
     * Posem els cavalls a l'inici de la cursa
     */
    private void endYourHorses(){
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            horsePositions[horse] = new Point((int) (HORSE_START_X * horseRaceView.getWidth()), (int) (HORSE_START_Y * (float) horseRaceView.getHeight() + horse * HORSE_SEPARATION * (float) horseRaceView.getHeight()));
            horseFrames[horse] = 2;
            horseSections[horse] = 0;
        }
    }

    /**
     * Metode que permet gestionar el que ha de fer el graphicsmanager per tal de mostrar la cursa i la llista d'apostes
     * @param g Element en el que pintar el contingut
     */
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        switch (mode) {
            case GAME_MODE:
                renderHorses(g);
                break;
            case LIST_MODE:
                renderList(g);
                break;
        }
        g.drawImage(returnPressed ? returnButtonSelected : returnButton, ebx, eby, null);
    }

    /**
     * Metode que permet gestionar el que ha de fer el graphicsmanager per tal de mostrar llista d'apostes
     * @param g Element en el que pintar el contingut
     */
    private void renderList(Graphics g) {
        g.drawImage(listBackground, 0, 0, Controller.getWinWidth(), Controller.getWinHeight(), null);

        g.drawImage(listTable, Controller.getWinWidth()/2 - LIST_DIM/2, Controller.getWinHeight()/2 - LIST_DIM/2, null);

        g.drawImage(upButton, Controller.getWinWidth()/2 + LIST_DIM/2 - 10, Controller.getWinHeight()/2 - 20, 20, 20, null);
        g.drawImage(downButton, Controller.getWinWidth()/2 + LIST_DIM/2 - 10, Controller.getWinHeight()/2, 20, 20, null);

        int zx = Controller.getWinWidth()/2 - LIST_DIM/2;
        int zy = Controller.getWinHeight()/2 - LIST_DIM/2 + 112;

        int[] cx = {zx + LIST_DIM/6, zx + LIST_DIM*3/6, zx + LIST_DIM*5/6};

        if (info.length != 0) {
            g.setColor(TEXT_COLOR);
            g.setFont(font.deriveFont(17f));

            for (int i = 0; i < Math.min((info.length > 0 ? info[0].length : 0), 33); i++) {
                for (int j = 0; j < 3; j++) {
                    String s =  info[j][i + listOff];
                    int width = g.getFontMetrics().getStringBounds(s, g).getBounds().width / 2;

                    g.drawString(s, cx[j] - width, zy + 18 * i);
                }
            }
        }
    }

    /**
     * Es buida la llista d'apostes
     */
    private void resetBetList() {
        info = new String[0][0];
    }


    /**
     * Metode que permet gestionar el que ha de fer el graphicsmanager per tal de mostrar la cursa amb els missatgees
     * pertinents d'aquesta i reproduir els sorolls de la cursa
     * @param g Element en el que pintar el contingut
     */
    private void renderHorses(Graphics g) {
        g.drawImage(AssetManager.getImage("HORSES-PANEL.png"), 0, 0, horseRaceView.getWidth(), horseRaceView.getHeight(), null);
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            g.drawImage(AssetManager.getImage("horse" + horse % 6 + "_" + horseFrames[horse] + ".png", (int) (horseRaceView.getWidth() / 14.44), (int) (horseRaceView.getHeight() / 10.4)), horsePositions[horse].x, horsePositions[horse].y, null);
        }
        g.setFont(font.deriveFont((float)(0.0098039215686275*(horseRaceView.getWidth()))));
        g.setColor(GREY);
        g.drawString("Press \"Esc\" to exit." , (int)(0.01), (int)(horseRaceView.getHeight()*0.03));
        g.drawString("Click on a Horse to Bet ", (int)(0.01), (int)(horseRaceView.getHeight()*0.054));
        g.setFont(font.deriveFont((float)(horseRaceView.getWidth()*0.0130718954248366)));
        g.setColor(TEXT_COLOR);
        g.drawString("Earnings:", (int)(horseRaceView.getWidth()*EARNINGS_TITLE_X), (int)(horseRaceView.getHeight()*EARNINGS_TITLE_Y));
        if(gameEarnings < 0){
            g.setColor(GRANA);
        }else{
            g.setColor(TEXT_COLOR);
        }
        g.drawString(Integer.toString(gameEarnings), (int)(horseRaceView.getWidth()*EARNINGS_MESSAGE_X), (int)(horseRaceView.getHeight()*EARNINGS_MESSAGE_Y));
        g.setColor(TEXT_COLOR);
        g.drawString("Bet" , (int)(horseRaceView.getWidth()*BET_TITLE_X), (int)(horseRaceView.getHeight()*BET_TITLE_Y));
        g.drawString("Wallet: " , (int)(horseRaceView.getWidth()*WALLET_TITLE_X), (int)(horseRaceView.getHeight()*WALLET_TITLE_Y));
        if(this.user.getWallet() <= 0){
            g.setColor(GRANA);
        }else{
            g.setColor(TEXT_COLOR);
        }
        g.drawString(Long.toString(this.user.getWallet()),(int)(horseRaceView.getWidth()*WALLET_MESSAGE_X), (int)(horseRaceView.getHeight()*WALLET_MESSAGE_Y));
        g.setColor(TEXT_COLOR);
        if (isRacing && raceCountdown.isCounting()) {
            if (System.currentTimeMillis() - animationRate > 50) {
                for (int i = 0; i < horseFrames.length; i++) {
                    horseFrames[i]++;
                    if (horseFrames[i] > 6)
                        horseFrames[i] = 0;
                }
                animationRate = System.currentTimeMillis();
            }
            if(raceCountdown.isCounting()){
                if(raceCountdown.getCount() < 10/1000){
                    g.drawString(("Race: 0" + raceCountdown.getCount() / 1000 + "s"),(int)(horseRaceView.getWidth()*TIME_MESSAGE_X) ,  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }else{
                    g.drawString("Racing...",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X) ,  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }
            }
        } else {
            if (isCountDown && waitCountdown.getCount() > 0) {
                if(waitCountdown.getCount()/1000 < 10){
                    g.drawString("Wait: 0" + (waitCountdown.getCount() / 1000) + "s",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X) ,  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }else{
                    g.drawString("Wait: " + (waitCountdown.getCount() / 1000) + "s",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X),  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }
                if(singleAudioPlay && waitCountdown.getCount() <7){
                    //Reprodueix l'audio de la trompeta inicial tot intentant sincronitzarse amb el temps actual del joc
                    Sounds.play("HStart.wav",7000 - waitCountdown.getCount());
                    singleAudioPlay = false;
                }
            }
        }
        if (!isRacing&&!isCountDown&&!waitCountdown.isCounting()&&!raceCountdown.isCounting() && firstRace ) {
            g.setFont(font.deriveFont((float)(0.0065359477124183*horseRaceView.getWidth())));
            g.drawString("Racing ...",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X),  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));
        }

        if (isBetting) {
            if(this.betOK){
                g.setFont(font.deriveFont((float)(0.0117647058823529*horseRaceView.getWidth())));
                g.drawString("Amount: " + betAmount , (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y )));
                g.drawString("Horse: " + (12 - betHorse), (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y + 0.03)));
            }else if(confirmReceived){
                g.setFont(font.deriveFont((float)(0.0117647058823529*horseRaceView.getWidth())));
                g.setColor(GRANA);
                g.drawString("Insufficient funds", (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*BET_STATUS_Y));
                g.setColor(TEXT_COLOR);
            }
        }else{
            g.setFont(font.deriveFont((float)(0.0117647058823529*horseRaceView.getWidth())));
            g.drawString("Amount: " , (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y )));
            g.drawString("Horse: " , (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y + 0.03)));
        }
        if (isCountDown && betResult) {
            g.setFont(font.deriveFont((float) (0.0117647058823529 * horseRaceView.getWidth())));
            if (prize == 0) {
                g.setColor(GRANA);
                g.drawString("Bet Lost", (int) (horseRaceView.getWidth() * BET_RESULT_X), (int) (horseRaceView.getHeight() * BET_RESULT_Y));
                g.setColor(TEXT_COLOR);
            } else {
                g.drawString("Prize:  " + prize, (int) (horseRaceView.getWidth() * BET_RESULT_X), (int) (horseRaceView.getHeight() * BET_RESULT_Y));
            }
        }
        g.setFont(font.deriveFont((float)(horseRaceView.getWidth()*0.0130718954248366)));
        if(isCountDown && !firstRace ){
            g.drawString("Winner: Horse " + (12 - winner), (int)(horseRaceView.getWidth()*WINNER_MESSAGE_X), (int)(horseRaceView.getHeight()*WINNER_MESSAGE_Y));
        }else{
            g.drawString("Winner: ", (int)(horseRaceView.getWidth()*WINNER_MESSAGE_X), (int)(horseRaceView.getHeight()*WINNER_MESSAGE_Y));

        }

        if(!isRacing && !raceCountdown.isCounting() && !waitCountdown.isCounting()){
            g.setFont(font.deriveFont((float)(horseRaceView.getWidth()*0.0130718954248366)));
            g.drawString("Wait: ...",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X),  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

        }
        g.drawImage(viewListPressed ? viewListSelected : viewList, vlx, vly, null);
    }

    /**
     * Actualitza la posicio de tots els cavalls depenent del temps restant de la cursa i de la seccio en la que es troben els cavalls
     * @param horsePositions posicions de tots els cavalls a la pantalla
     * @param horseCountdowns temps restant per acabar una seccio per cada cavall
     * @param horseSections seccio en que es troba cada cavall
     * @param raceCountdown temps restant de la cursa
     */
    private void moveHorses(Point[] horsePositions, Countdown[] horseCountdowns, int[] horseSections, Countdown raceCountdown) {
        if(raceCountdown.isCounting()){
            for (int horse = 0; horse < MAX_HORSES; horse++){
                if(!horseCountdowns[horse].isCounting()){
                    if(horseSections[horse] == SECTIONS - 1){
                        this.raceCountdown.stopCount();
                        break;
                    }else {
                        horseSections[horse] ++;
                        horseCountdowns[horse].newCount(getTime(horse, horseSections[horse]));
                    }
                }else{
                    horsePositions[horse].x = (int) Math.round(horseRaceView.getWidth() * HORSE_SECTION * (horseSections[horse]  + (double)(getTime(horse, horseSections[horse]) - horseCountdowns[horse].getCount())/(double)(getTime(horse, horseSections[horse]))) + horseRaceView.getWidth()*HORSE_START_X);
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * En cas d'escape sortim del joc
     * @param e Key event que permet mostrar la coordenada del ratoli al premer-lo
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 27){
            stopPlay();
            networkManager.showGamesView();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    /**
     * Si es prem sobre un cavall quan no s'esta fent una cursa mostrarem un panell que
     * permet a l'usuari introduir els diners que vol apostar
     * @param e  Key event que permet mostrar la coordenada del ratoli al fer click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        HorseMessage horseMessage;
        if(!isRacing && ! (isBetting && betOK)){
            if(e.getX() >= horseRaceView.getWidth()*HORSE_START_X - (horseRaceView.getWidth() / 14.44) && e.getX() <= horseRaceView.getWidth()*HORSE_START_X + (horseRaceView.getWidth() / 14.44)) {
                for (int horse = 0; horse < MAX_HORSES; horse++) {
                    if(e.getY() >= (horsePositions[horse].y - (horseRaceView.getHeight() / 10.4)) && e.getY() <= (horsePositions[horse].y + (horseRaceView.getHeight() / 10.4)) ){
                        try {
                            Sounds.play("HClick.wav");
                            betAmount = Long.parseLong((String) JOptionPane.showInputDialog(null, "Bet:", "HORSE " + (12 - horse) ,  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(AssetManager.getImage("horse" + (horse % 6) + "_"+ horseFrames[horse]+".png")), null, 0));
                            if(betAmount <= 0){
                                JOptionPane.showMessageDialog(horseRaceView, "Bet must be greater than 0", "BET ERROR", JOptionPane.ERROR_MESSAGE, null);
                            }else{
                                betHorse = horse;
                                horseMessage  = new HorseMessage(new HorseBet(betAmount,  betHorse, user.getUsername()), "Bet");
                                horseMessage.setID(user.getID());
                                if(!isRacing){
                                    new Transmission(horseMessage, this.networkManager);
                                    this.isBetting = true;
                                    this.confirmReceived = false;
                                }

                            }
                        }catch (Exception exception){
                            betHorse = 0;
                            betAmount = -1;


                        }
                        break;
                    }
                }

            }
        }



    }

    /**
     * En cas de premer el boto d'Ext sortirem del joc
     * @param e  Key event que permet mostrar la coordenada del ratoli al premer-lo
     */
    @Override
    public void mousePressed(MouseEvent e) {
        returnPressed = e.getX() > ebx && e.getX() < ebx + returnButton.getWidth(null) && e.getY() > eby && e.getY() < eby + returnButton.getHeight(null);
        if (returnPressed && mode == GAME_MODE){
            stopPlay();
            networkManager.showGamesView();
        }
    }

    /**
     * Mostrem la llista d'apostes en curs per la carrera en temps real
     * @param e Mouse Event que permet obtenir la coordenada del ratoli
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (mode == LIST_MODE) {
            if (e.getX() > Controller.getWinWidth()/2 + LIST_DIM/2 - 10 && e.getX() < Controller.getWinWidth()/2 + LIST_DIM/2 + 10
                    && e.getY() > Controller.getWinHeight()/2 - 20 && e.getY() < Controller.getWinHeight()/2)
                if (listOff > 0) listOff--;
            if (e.getX() > Controller.getWinWidth()/2 + LIST_DIM/2 - 10 && e.getX() < Controller.getWinWidth()/2 + LIST_DIM/2 + 10
                    && e.getY() > Controller.getWinHeight()/2 && e.getY() < Controller.getWinHeight()/2 + 20)
                if (info.length > 0 && info[0].length - listOff > 33) listOff++;

        } else {
            if (e.getX() > vlx && e.getY() > vly && e.getY() < vly + viewList.getHeight(null) && e.getX() < vlx + viewList.getWidth(null)){
                mode = LIST_MODE;
            }
        }


        if (e.getX() > ebx && e.getX() < ebx + returnButton.getWidth(null) && e.getY() > eby && e.getY() < eby + returnButton.getHeight(null)) {
            if (mode == GAME_MODE) networkManager.exitRoulette();
            else mode = GAME_MODE;
        }

        returnPressed = false;
        viewListPressed = false;

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

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * S'actualitza el tamany dels cavalls i les seves posicions per tal de que respectn el tamany de la pantalla
     */
    public void updateSize(){
        if(this.horseRaceView != null && graphicsManager!= null){
            graphicsManager.resize(horseRaceView.getWidth(),horseRaceView.getHeight());
            for(int i = 0; i < MAX_HORSES; i++){
                horsePositions[i].x = ((horsePositions[i].x * horseRaceView.getWidth())/ this.frameWidth);
                horsePositions[i].y = ((horsePositions[i].y * horseRaceView.getHeight())/ this.frameHeight);
            }

        }
        this.frameHeight = horseRaceView.getHeight();
        this.frameWidth = horseRaceView.getWidth();
    }

    /**
     * Es demana al servidor de que ens envii el wallet de l'usuari
     */
    private void requestWallet(){
        new Transmission(new HorseMessage((long)0), networkManager);
    }


    /**
     * Sortim del joc
     */
    public static void exit(){
        if(graphicsManager != null){
            graphicsManager.exit();
        }
    }
}
