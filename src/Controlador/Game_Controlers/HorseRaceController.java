package Controlador.Game_Controlers;

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;


/**Controlador de la cursa de cavalls*/
public class HorseRaceController implements GraphicsController, ActionListener {

    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color GREY = new Color(49, 63, 47);


    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;

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
        if (play) {
            if (!isRacing && !waitCountdown.isCounting()) {
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Countdown");
                if (horseMessage != null) {
                    this.isBetting = false;
                    this.isCountDown = true;
                    this.oncePerRace = true;
                    this.betOK = false;
                    this.waitCountdown.newCount(horseMessage.getTimeForRace());
                    endYourHorses();
                }
            } else if (!isRacing) {
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
                    System.out.println("HORSES- Done");
                    if (isBetting) {
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
        //TODO Change font size in respect to window size
        g.drawImage(AssetManager.getImage("HORSES-PANEL.png"), 0, 0, horseRaceView.getWidth(), horseRaceView.getHeight(), null);
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            g.drawImage(AssetManager.getImage("horse" + horse % 6 + "_" + horseFrames[horse] + ".png", (int) (horseRaceView.getWidth() / 14.44), (int) (horseRaceView.getHeight() / 10.4)), horsePositions[horse].x, horsePositions[horse].y, null);
        }
        g.setFont(font.deriveFont(15f));
        g.setColor(GREY);
        g.drawString("Press \"Esc\" to exit." , (int)(0.01), (int)(horseRaceView.getHeight()*0.03));
        g.setFont(font.deriveFont(20f));
        g.setColor(Color.white);
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
                g.setColor(Color.white);
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
            //TODO No surt del joc
            finestra.setGameSelector(false); //Un guest no podra jugar a cavalls
            stopPlay();
        }if((e.getKeyCode() == 98 || e.getKeyCode() == 66) && !isRacing){
            //TODO Meri: Boto apostes + panell apostes amb thread per no parar execució joc
            horseMessage  = new HorseMessage(new HorseBet(100,10, user.getUsername()), "Bet");
            horseMessage.setID(user.getID());
            new Transmission(horseMessage, this.networkManager);
            this.isBetting = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("CLICK: x:"+ e.getPoint().x/(float)horseRaceView.getWidth() + "   y: " +  e.getPoint().y/(float)horseRaceView.getHeight());
        System.out.println("Rel Click X:  " + (HORSE_END_X/((double)horseRaceView.getWidth()) - HORSE_START_X));
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
        }
    }


    public static void exit(){
        if(graphicsManager != null){
            graphicsManager.exit();
        }
    }
}
