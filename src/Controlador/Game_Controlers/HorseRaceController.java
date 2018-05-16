package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Model.AssetManager;
import Model.HorseRace_Model.HorseBet;
import Model.HorseRace_Model.HorseMessage;
import Model.HorseRace_Model.HorseRaceModel;
import Model.Transaction;
import Model.User;
import Network.NetworkManager;
import Network.Transmission;
import Utils.Countdown;
import Vista.GameViews.HorseRaceView;
import Vista.MainFrame.Finestra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;


/**Controlador de la cursa de cavalls*/
public class HorseRaceController implements GraphicsController, ActionListener {

    private static final String[] listBetConversion = {"0", "3", "2", "1", "6", "5", "4", "9", "8", "7", "12", "11", "10", "15", "14", "13", "18", "17", "16", "21", "20", "19", "24", "23", "22", "27", "26", "25", "30", "29", "28", "33", "32", "31", "36", "35", "34", "First line", "Second line", "Third line", "First half", "Even", "Red", "Black", "Odd", "Second Half", "First dozen", "Second dozen", "Third dozen"};


    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color GREY = new Color(49, 63, 47);
    private static final Color TEXT_COLOR = new Color(216, 204, 163);

    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;

    public static final int GAME_MODE = 0;
    public static final int LIST_MODE = 1;

    public static final int LIST_DIM = 700;
    private static final double HORSE_START_Y = 0.026;
    private static final double HORSE_START_X = 0.252;
    private static final double HORSE_SEPARATION = 0.074;
    private static final double HORSE_END_X = 0.895;
    private static final double HORSE_SECTION = (HORSE_END_X - HORSE_START_X) /(double)(SECTIONS); // Per reconstruir: HORSE_SECTION * section + HORSE_START_X
    private static final double TIME_MESSAGE_Y = 0.23;
    private static final double TIME_MESSAGE_X = 0.048;
    private static final double WINNER_MESSAGE_X = TIME_MESSAGE_X;
    private static final double WINNER_MESSAGE_Y = TIME_MESSAGE_Y + 0.035;
    private static final double WALLET_MESSAGE_X = TIME_MESSAGE_X;
    private static final double WALLET_MESSAGE_Y = WINNER_MESSAGE_Y + 0.035;
    private static final double BET_TITLE_X = TIME_MESSAGE_X;
    private static final double BET_TITLE_Y = 0.7;
    private static final double BET_STATUS_X = BET_TITLE_X;
    private static final double BET_STATUS_Y = BET_TITLE_Y + 0.035;
    private static final double BET_RESULT_X = BET_TITLE_X;
    private static final double BET_RESULT_Y = BET_STATUS_Y + 0.06;

    private static GraphicsManager graphicsManager;
    private NetworkManager networkManager;
    private HorseRaceView horseRaceView;
    private User user;
    private HorseRaceModel horseRaceModel;
    private Finestra finestra;

    private boolean isRacing;
    private boolean isBetting;
    private boolean play;
    private boolean isCountDown;
    private boolean betResult;
    private boolean firstRace;
    private boolean oncePerRace;
    private boolean betOK;

    private Countdown waitCountdown;
    private Countdown raceCountdown;


    private Countdown[] horseCountdowns;
    private Point[] horsePositions;
    private int[] horseFrames;
    private int[] horseSections;

    private int winner;
    private long animationRate;
    private long prize;
    private long betAmount;
    private int betHorse;

    private Font font;

    private int mode;

    private Image wood;
    private Image viewList;
    private Image viewListSelected;
    private Image returnButton;
    private Image returnButtonSelected;
    private Image listBackground;
    private Image upButton;
    //private Image upButtonSelected;
    private Image downButton;
    //private Image getDownButtonSelected;
    private Image listTable;

    private boolean viewListPressed;
    private boolean returnPressed;

    private int ebx;
    private int eby;
    private int vlx;
    private int vly;

    private String[][] info;
    private int listOff;


    public HorseRaceController(HorseRaceView horseRaceView, NetworkManager networkManager, Finestra finestra) {
        this.horseRaceModel = new HorseRaceModel();
        this.horseRaceView = horseRaceView;
        this.networkManager = networkManager;
        this.finestra = finestra;
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
        this.betResult = false;
        this.winner = -1;
        this.firstRace = true;
        this.oncePerRace = true;
        this.mode = GAME_MODE;
        this.listOff = 0;
    }


    /**
     * Inicialitza el GraphicsManager
     */
    public void setGraphics() {
        this.graphicsManager = new GraphicsManager(this.horseRaceView, this);
        this.graphicsManager.setClearColor(Color.black);
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void init() {
        this.isRacing = false;
        this.isBetting = false;
        this.isCountDown = false;
        this.betResult = false;
        this.firstRace = true;
        this.oncePerRace = true;
        this.betHorse = 0;
        this.betOK = false;
        waitCountdown.stopCount();
        raceCountdown.stopCount();
        endYourHorses();
        font = AssetManager.getEFont(20);
        this.mode = GAME_MODE;

        viewList = AssetManager.getImage("VG.png");
        viewListSelected = AssetManager.getImage("VGS.png");
        returnButton = AssetManager.getImage("EXIT_NO_SOMBRA.png");
        returnButtonSelected = AssetManager.getImage("EXIT_SOMBRA.png");

        viewListPressed = false;
        returnPressed = false;

        listBackground = AssetManager.getImage("background.png");
        upButton = AssetManager.getImage("SUB.png");
        downButton = AssetManager.getImage("BAJ.png");
        listTable = AssetManager.getImage("POnline.png");
        wood = AssetManager.getImage("Num.png");

        vlx = 20;
        vly = Controller.getWinHeight() - viewList.getHeight(null) - 20;
        ebx = Controller.getWinWidth() - returnButton.getHeight(null) - 20;
        eby = Controller.getWinHeight() - returnButton.getHeight(null) - 20;

        info = new String[0][0];

    }

    /**
     * Començem a jugar
     */
    public void play() {

        setGraphics();
        this.play = true;
        this.betHorse = 0;
        this.betOK = false;
        this.isRacing = false;
        this.isBetting = false;
        this.betResult = false;
        this.isCountDown = false;
        this.firstRace = true;
        this.oncePerRace = true;
        waitCountdown.stopCount();
        raceCountdown.stopCount();
        endYourHorses();
        this.mode = GAME_MODE;
        this.listOff = 0;
        resetBetList();
    }

    /**
     * Parem de jugar
     */
    public void stopPlay() {
        this.play = false;
        this.isRacing = false;
        this.isBetting = false;
        this.isCountDown = false;
        this.oncePerRace = false;
        this.betResult = false;
        this.betOK = false;
        this.graphicsManager.exit();
        this.networkManager.exitHorses();
        waitCountdown.stopCount();
        raceCountdown.stopCount();
    }

    @Override
    public void update(float delta) {
        HorseMessage horseMessage;

        vlx = 20;
        vly = Controller.getWinHeight() - viewList.getHeight(null) - 20;
        ebx = Controller.getWinWidth() - returnButton.getWidth(null) - 20;
        eby = Controller.getWinHeight() - returnButton.getHeight(null) - 20;

        String[][] aux = networkManager.updateHorseList();
        info = aux == null ? info : aux;

        if (play) {
            if (!isRacing) {
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Countdown");
                if (horseMessage != null) {
                    this.isBetting = false;
                    this.isCountDown = true;
                    this.oncePerRace = true;
                    this.betOK = false;
                    this.waitCountdown.newCount(horseMessage.getTimeForRace());
                    endYourHorses();
                }
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Schedule");
                if (horseMessage != null) {
                    System.out.println("HORSES- Schedule received");
                    this.horseRaceModel.setHorseSchedule(horseMessage.getHorseSchedule());
                    this.waitCountdown.stopCount();
                    this.raceCountdown.newCount(horseRaceModel.getHorseSchedule().getRaceTime());
                    this.isRacing = true;
                    this.oncePerRace = true;
                    this.isCountDown = false;
                    initRace();
                    //Ja podem reproduir la carrera
                }
            } else if (isRacing) {
                moveHorses(horsePositions, horseCountdowns, horseSections, raceCountdown);
                this.waitCountdown.stopCount();
                if (isBetting) {
                    horseMessage = (HorseMessage) networkManager.readContext("HORSES-BetConfirm");
                    if (horseMessage != null) {
                        betOK = horseMessage.getHorseBet().isBetOK();
                        if (betOK) {
                            betAmount = horseMessage.getHorseBet().getBet();
                            betHorse = horseMessage.getHorseBet().getHorse() + 1;
                            new Transaction("HorseBet", user.getUsername(), -betAmount, 1);

                        }else{
                            betResult = false;
                        }
                    }

                }else{
                    betResult = false;
                }
                if (oncePerRace) {
                    if (raceCountdown.getCount() <= 0) {
                        System.out.println("HORSES- Race Finished");
                        horseMessage = new HorseMessage((HorseBet) null, "Finished");
                        horseMessage.setID(user.getID());
                        new Transmission(horseMessage, this.networkManager);
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
                    System.out.println("HORSES- Done");
                    if (isBetting && betOK) {
                        betResult = true;
                    }
                    this.winner = horseMessage.getHorseResult().getWinner();
                    prize = horseMessage.getHorseResult().getPrize();
                    if (prize == 0) {
                    } else {
                        new Transaction("HorseBet", user.getUsername(), prize, 1);
                    }
                }
            }
        }
    }

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

    private long getTime(int horse, int section){
        return horseRaceModel.getHorseSchedule().getTime(horse, section);
    }


    public void endYourHorses(){
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            horsePositions[horse] = new Point((int) (HORSE_START_X * horseRaceView.getWidth()), (int) (HORSE_START_Y * (float) horseRaceView.getHeight() + horse * HORSE_SEPARATION * (float) horseRaceView.getHeight()));
            horseFrames[horse] = 2;
            horseSections[horse] = 0;
        }
    }

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
                    String s = j == 1 ? listBetConversion[Integer.parseInt(info[j][i + listOff])] : info[j][i + listOff];
                    int width = g.getFontMetrics().getStringBounds(s, g).getBounds().width / 2;

                    g.drawString(s, cx[j] - width, zy + 18 * i);
                }
            }
        }
    }

    private void resetBetList() {
        info = new String[0][0];
    }


    private void renderHorses(Graphics g) {
        //TODO Change font size in respect to window size
        g.drawImage(AssetManager.getImage("HORSES-PANEL.png"), 0, 0, horseRaceView.getWidth(), horseRaceView.getHeight(), null);
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            g.drawImage(AssetManager.getImage("horse" + horse % 6 + "_" + horseFrames[horse] + ".png", (int) (horseRaceView.getWidth() / 14.44), (int) (horseRaceView.getHeight() / 10.4)), horsePositions[horse].x, horsePositions[horse].y, null);
        }
        g.setFont(font.deriveFont(15f));
        g.setColor(GREY);
        g.drawString("Press \"Esc\" to exit." , (int)(0.01), (int)(horseRaceView.getHeight()*0.03));
        g.setFont(font.deriveFont(20f));
        g.setColor(TEXT_COLOR);
        g.drawString("Bet:" , (int)(horseRaceView.getWidth()*BET_TITLE_X), (int)(horseRaceView.getHeight()*BET_TITLE_Y));
        g.drawString("Wallet: " + this.user.getWallet() , (int)(horseRaceView.getWidth()*WALLET_MESSAGE_X), (int)(horseRaceView.getHeight()*WALLET_MESSAGE_Y));
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
                    g.drawString(("Race: " + (raceCountdown.getCount() / 1000 + "s")),(int)(horseRaceView.getWidth()*TIME_MESSAGE_X) ,  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }
            }
        } else {
            if (isCountDown && waitCountdown.getCount() > 0) {
                if(waitCountdown.getCount() < 10/1000){
                    g.drawString("Wait: 0" + (waitCountdown.getCount() / 1000) + "s",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X) ,  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }else{
                    g.drawString("Wait: " + (waitCountdown.getCount() / 1000) + "s",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X),  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

                }
            }
        }
        if (!isRacing&&!isCountDown&&!waitCountdown.isCounting()&&!raceCountdown.isCounting() && firstRace ) {
            g.setFont(font.deriveFont(10f));
            g.drawString("Race: ...",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X),  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));
        }

        if (isBetting) {
            if(this.betOK){
                g.setFont(font.deriveFont(18f));
                g.drawString("Amount: " + betAmount , (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y )));
                g.drawString("Horse: " + betHorse, (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y + 0.03)));
            }else{
                g.setFont(font.deriveFont(18f));
                g.setColor(GRANA);
                g.drawString("Insufficient funds", (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*BET_STATUS_Y));
                g.setColor(TEXT_COLOR);
            }
        }else{
            g.setFont(font.deriveFont(18f));
            g.drawString("Amount: " , (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y )));
            g.drawString("Horse: " , (int)(horseRaceView.getWidth()*BET_STATUS_X), (int)(horseRaceView.getHeight()*(BET_STATUS_Y + 0.03)));
        }
        if (isCountDown && betResult) {
            g.setFont(font.deriveFont(18f));
            if (prize == 0) {
                g.drawString("Bet Lost", (int)(horseRaceView.getWidth()*BET_RESULT_X), (int)(horseRaceView.getHeight()*BET_RESULT_Y));
            } else {
                g.drawString("Prize:  " + prize, (int)(horseRaceView.getWidth()*BET_RESULT_X), (int)(horseRaceView.getHeight()*BET_RESULT_Y));
            }
        }
        g.setFont(font.deriveFont(20f));
        if(isCountDown && !firstRace ){
            g.drawString("Winner: Horse " + (12 - winner), (int)(horseRaceView.getWidth()*WINNER_MESSAGE_X), (int)(horseRaceView.getHeight()*WINNER_MESSAGE_Y));
        }else{
            g.drawString("Winner: ", (int)(horseRaceView.getWidth()*WINNER_MESSAGE_X), (int)(horseRaceView.getHeight()*WINNER_MESSAGE_Y));

        }

        if(!isRacing && !raceCountdown.isCounting() && !waitCountdown.isCounting()){
            g.setFont(font.deriveFont(20f));
            g.drawString("Wait: ...",(int)(horseRaceView.getWidth()*TIME_MESSAGE_X),  (int)(horseRaceView.getHeight()*TIME_MESSAGE_Y));

        }
        g.drawImage(viewListPressed ? viewListSelected : viewList, vlx, vly, null);
    }

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
                    horsePositions[horse].x = (int)(horseRaceView.getWidth() * HORSE_SECTION * (horseSections[horse]  + (double)(getTime(horse, horseSections[horse]) - horseCountdowns[horse].getCount())/(double)(getTime(horse, horseSections[horse]))) + horseRaceView.getWidth()*HORSE_START_X);
                }
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        HorseMessage horseMessage;
        if(e.getKeyCode() == 27){
            stopPlay();
            networkManager.showGamesView();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        long bet = -1;
        HorseMessage horseMessage;
        System.out.println("CLICK: x:"+ e.getPoint().x/(float)horseRaceView.getWidth() + "   y: " +  e.getPoint().y/(float)horseRaceView.getHeight());
        System.out.println("Rel Click X:  " + (HORSE_END_X/((double)horseRaceView.getWidth()) - HORSE_START_X));

        if(!isRacing && ! isBetting){
            if(e.getX() >= horseRaceView.getWidth()*HORSE_START_X - (horseRaceView.getWidth() / 14.44) && e.getX() <= horseRaceView.getWidth()*HORSE_START_X + (horseRaceView.getWidth() / 14.44)) {
                for (int horse = 0; horse < MAX_HORSES; horse++) {
                    if(e.getY() >= horsePositions[horse].y - (horseRaceView.getHeight() / 10.4) && e.getY() <= horsePositions[horse].y + (horseRaceView.getHeight() / 10.4) ){
                        try {
                            bet = Long.parseLong((String) JOptionPane.showInputDialog(null, "Bet:", "HORSE " + (12 - horse), JOptionPane.INFORMATION_MESSAGE, new ImageIcon(AssetManager.getImage("horse" + (horse) + "_"+ horseFrames[horse]+".png")), null, 0));
                            System.out.println(bet);
                            if(bet <= 0){
                                JOptionPane.showMessageDialog(horseRaceView, "Bet must be greater than 0", "BET ERROR", JOptionPane.ERROR_MESSAGE, null);
                            }else{
                                horseMessage  = new HorseMessage(new HorseBet(bet,horse, user.getUsername()), "Bet");
                                horseMessage.setID(user.getID());
                                new Transmission(horseMessage, this.networkManager);
                                this.isBetting = true;
                            }

                        }catch (Exception exception){
                            this.isBetting = false;
                        }
                        break;
                    }
                }

            }
        }



    }

    @Override
    public void mousePressed(MouseEvent e) {
        returnPressed = e.getX() > ebx && e.getX() < ebx + returnButton.getWidth(null) && e.getY() > eby && e.getY() < eby + returnButton.getHeight(null);
        if (returnPressed && mode == GAME_MODE){
            stopPlay();
            networkManager.showGamesView();
        }
    }

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

        System.out.println(Controller.getWinWidth() + " X "+ Controller.getWinHeight());

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
        //Per saber si estan en el boto
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        HorseMessage horseMessage;
        /*switch (e.getActionCommand()){
           case "Horses-Bet":
                horseMessage  = new HorseMessage(new HorseBet(horseRaceView.getBetAmount(),horseRaceView.getBetHorse(), user.getID()), "Bet");
                horseMessage.setID(user.getID());
                new Transmission(horseMessage, this.networkManager);
                this.isBetting = true;

        }*/
    }

    public void updateSize(){
        if(this.horseRaceView != null && graphicsManager!= null){
            graphicsManager.resize(horseRaceView.getWidth(),horseRaceView.getHeight());
            for(int i = 0; i < MAX_HORSES; i++){

            }
        }
    }


    public static void exit(){
        if(graphicsManager != null){
            graphicsManager.exit();
        }
    }


}
