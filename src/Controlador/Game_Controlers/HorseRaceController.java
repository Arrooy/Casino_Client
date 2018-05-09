package Controlador.Game_Controlers;

import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Controlador.DraggableWindow;
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
//import Vista.MainFrame.Finestra;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

/**Controlador de la cursa de cavalls*/
public class HorseRaceController implements GraphicsController, ActionListener {

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

    private Countdown waitCountdown;
    private Countdown raceCountdown;


    private Countdown[] horseCountdowns;
    private Point[] horsePositions;
    private int[] horseFrames;

    private int winner;

    private long animationRate;
    private long prize;
    private long betAmount;


    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;
    private static final double HORSE_START_X = 0.252;
    private static final double HORSE_START_Y = 0.026;


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
        for (int i = 0; i < MAX_HORSES; i++) {
            this.horseCountdowns[i] = new Countdown();
            this.horsePositions[i] = new Point(0, 0);
            this.horseFrames[i] = 0;
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

    public boolean isPlaying() {
        return this.play;
    }

    /**
     * Inicialitza el GraphicsManager
     */
    public void setGraphics() {
        this.graphicsManager = new GraphicsManager(this.horseRaceView.getTrack(), this);
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
        waitCountdown.stopCount();
        raceCountdown.stopCount();
        startYourHorses();

    }

    /**
     * Començem a jugar
     */
    public void play() {
        setGraphics();
        this.play = true;
        this.isRacing = false;
        this.isBetting = false;
        this.isCountDown = false;
        this.firstRace = true;
        this.oncePerRace = true;
        waitCountdown.stopCount();
        raceCountdown.stopCount();
    }

    /**
     * Parem de jugar
     */
    public void stopPlay() {
        this.play = false;
        this.isRacing = false;
        this.isBetting = false;
        this.graphicsManager.exit();
        waitCountdown.stopCount();
        raceCountdown.stopCount();
    }

    @Override
    public void update(float delta) {
        HorseMessage horseMessage;
        if (play) {
            System.out.println(firstRace);
            if (!isRacing && !waitCountdown.isCounting()) {
                System.out.println("teteee");
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Countdown");
                if (horseMessage != null) {
                    this.isBetting = false;
                    this.isCountDown = true;
                    this.oncePerRace = true;
                    this.waitCountdown.newCount(horseMessage.getTimeForRace());
                }
            } else if (!isRacing && waitCountdown.isCounting()) {
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Schedule");
                if (horseMessage != null) {
                    System.out.println("HORSES- Schedule received");
                    this.horseRaceModel.setHorseSchedule(horseMessage.getHorseSchedule());
                    this.waitCountdown.stopCount();
                    this.raceCountdown.newCount(horseRaceModel.getHorseSchedule().getRaceTime());
                    this.isRacing = true;
                    this.isCountDown = false;
                    startYourHorses();
                    //Ja podem reproduir la carrera
                }
            } else if (isRacing) {
                this.waitCountdown.stopCount();
                if (isBetting) {
                    horseMessage = (HorseMessage) networkManager.readContext("HORSES-BetConfirm");
                    if (horseMessage != null) {
                        boolean betOK = horseMessage.getHorseBet().isBetOK();
                        if (betOK) {
                            new Transaction("HorseBet", user.getUsername(), -horseMessage.getHorseBet().getBet(), 1);
                            //TODO: Mostra missatge de aposta correcte

                        } else {
                            //TODO: Mostra missatge aposta incorrecte
                            isBetting = false;

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
                    System.out.println("HORSES- Done");
                    if (isBetting) {
                        betResult = true;
                    }
                    this.winner = horseMessage.getHorseResult().getWinner();
                    prize = horseMessage.getHorseResult().getPrize();
                    if (prize == 0) {
                        //TODO: Missatge no ha guanyat
                    } else {
                        new Transaction("HorseBet", user.getUsername(), prize, 1);
                        //TODO: Mostra missatge que ha guanyat aposta
                    }

                    //TODO: Mostra missatge guanyador = horseMessage.getHorseResult().getWinner();

                }
            }
        }
    }


    public void startYourHorses() {
        Random random = new Random();
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            horsePositions[horse] = new Point((int) (HORSE_START_X * horseRaceView.getWidth()), (int) (HORSE_START_Y * (float) horseRaceView.getHeight() + horse * 0.074 * (float) horseRaceView.getHeight()));
            horseFrames[horse] = random.nextInt(7);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(AssetManager.getImage("HORSES-PANEL.png"), 0, 0, horseRaceView.getWidth(), horseRaceView.getHeight(), null);
        for (int horse = 0; horse < MAX_HORSES; horse++) {
            g.drawImage(AssetManager.getImage("horse" + horse % 6 + "_" + horseFrames[horse] + ".png", (int) (horseRaceView.getWidth() / 14.44), (int) (horseRaceView.getHeight() / 10.4)), horsePositions[horse].x, horsePositions[horse].y, null);
        }
        if (isRacing && raceCountdown.isCounting()) {
            //TODO: Aqui cal fer que s'actualitzi la posicio dels caballs
            //TODO: Mostrar temps per acabar carrera amb raceCountdown.getCount()
            if (System.currentTimeMillis() - animationRate > 150) {
                for (int i = 0; i < horseFrames.length; i++) {
                    horseFrames[i]++;
                    if (horseFrames[i] > 6)
                        horseFrames[i] = 0;
                }
                animationRate = System.currentTimeMillis();
            }
            if(raceCountdown.isCounting()){
                g.drawString("RACE TIME: " + raceCountdown.getCount() / 1000, 40, 40);
            }

        } else {
            if (isCountDown && waitCountdown.getCount() > 0) {
                g.drawString("Time for next race: " + waitCountdown.getCount() / 1000, 40, 20);
            }
        }
        if (!isRacing&&!isCountDown&&!waitCountdown.isCounting()&&!raceCountdown.isCounting() && firstRace ) {
            //TODO Mostrar missatge esperant a que acabi una carrera
            g.drawString("Waiting for race to finish...", 40, 60);
        }

        if (isBetting) {
            g.drawString("You have bet", 40, 80);
            //TODO Mostrar aposta
        }
        if (isCountDown && betResult) {
            if (prize == 0) {
                g.drawString("Has perdut l'aposta", 40, 100);
            } else {
                g.drawString("Has guanyat  " + prize, 40, 100);
            }

            //TODO Mostrar missatge recompensa aposta

        }

        if(isCountDown && !firstRace ){
            g.drawString("Ha guanyat " + winner, 40, 120);
        }

        if(!isRacing && !raceCountdown.isCounting() && !waitCountdown.isCounting()){
            g.drawString("Començant carrera..." , 40, 140);
        }


    }








    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        HorseMessage horseMessage;
        if(e.getKeyCode() == 27){
            finestra.setGameSelector(false); //Un guest no podra jugar a cavalls
            networkManager.exitHorses();
            stopPlay();
        }if((e.getKeyCode() == 98 || e.getKeyCode() == 66) && !isRacing){
            horseMessage  = new HorseMessage(new HorseBet(100,10, user.getID()), "Bet");
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
        if(this.horseRaceView != null && this.graphicsManager!= null){
            this.graphicsManager.resize(horseRaceView.getWidth(),horseRaceView.getHeight());
        }

    }

    public static void exit(){
        if(graphicsManager != null){
            graphicsManager.exit();
        }

    }
}
