package Controlador.Game_Controlers;

import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Controlador.DraggableWindow;
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
import java.util.concurrent.CountDownLatch;

/**Controlador de la cursa de cavalls*/
public class HorseRaceController implements GraphicsController, ActionListener {

    private GraphicsManager graphicsManager;
    private NetworkManager networkManager;
    private HorseRaceView horseRaceView;
    private User user;
    private HorseRaceModel horseRaceModel;
    private Finestra finestra;

    private boolean isRacing;
    private boolean isBetting;
    private boolean play;
    private boolean firstFinish;

    private Countdown waitCountdown;
    private Countdown raceCountdown;


    private Countdown[][] horseCountdowns;
    private Point[][] horsePositions;


    private static final int MAX_HORSES = 12;
    private static final int SECTIONS = 5;




    public HorseRaceController(HorseRaceView horseRaceView, NetworkManager networkManager, Finestra finestra){
        this.horseRaceModel = new HorseRaceModel();
        this.horseRaceView = horseRaceView;
        this.networkManager = networkManager;
        this.finestra = finestra;
        this.isRacing = false;
        this.waitCountdown = new Countdown();
        this.raceCountdown = new Countdown();
        this.horseCountdowns = new Countdown[MAX_HORSES][SECTIONS];
        this.horsePositions = new Point[MAX_HORSES][SECTIONS];
        for(int i = 0; i < MAX_HORSES; i++){
            for(int j = 0; j < SECTIONS; j++){
                this.horseCountdowns[i][j] = new Countdown();
                this.horsePositions[i][j] = new Point(0,0);
            }
        }
        this.play = false;
        this.firstFinish = true;
    }

    /**Inicialitza el GraphicsManager*/
    public void setGraphics(){
        this.graphicsManager = new GraphicsManager(this.horseRaceView.getTrack(),this);
    }

    public void setUser(User user){
        this.user = user;
    }

    @Override
    public void init() {
        this.isRacing = false;
        this.isBetting = false;
        waitCountdown.stopCount();
        raceCountdown.stopCount();
    }

    /**Començem a jugar*/
    public void play(){
        setGraphics();
        this.play = true;
        this.isRacing = false;
        this.isBetting = false;
        waitCountdown.stopCount();
        raceCountdown.stopCount();
    }

    @Override
    public void update(float delta) {
        HorseMessage horseMessage;
        if(play){
            if(!isRacing && !waitCountdown.isCounting()){
                //TODO: mostra missatge esperant a que acabi countdown
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Countdown");
                if(horseMessage != null){
                    this.waitCountdown.newCount(horseMessage.getTimeForRace());
                    this.firstFinish = true;
                }
            }else if(!isRacing && waitCountdown.isCounting()){
                //TODO: mostra countdown.getCount();
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Schedule");
                if(horseMessage != null){
                    System.out.println("HORSES- Schedule received");
                    this.horseRaceModel.setHorseSchedule(horseMessage.getHorseSchedule());
                    this.isRacing = true;
                    this.raceCountdown.newCount(horseRaceModel.getHorseSchedule().getRaceTime());
                    //Ja podem reproduir la carrera
                }
            }else if(isRacing){
            /*TODO    mostra carrera, per veure els temps de cada cavall a arribar als 5 segments de la cursa
              TODO    fes servir horseRaceModel.getHorseSchedule().getTime(i,j)*/

                if(isBetting){
                    horseMessage = (HorseMessage) networkManager.readContext("HORSES-BetConfirm");
                    if(horseMessage != null){
                        boolean betOK = horseMessage.getHorseBet().isBetOK();
                        if (betOK) {
                            new Transaction("HorseBet", user.getUsername(), -horseMessage.getHorseBet().getBet(), 1);
                            //TODO: Mostra missatge de aposta correcte
                        } else {
                            //TODO: Mostra missatge aposta incorrecte
                        }
                    }

                }
                if(firstFinish){
                    if(true/*TODO Metode per dir si s'ha acabat la carrera*/){
                        System.out.println("HORSES- Race Finished");
                        horseMessage = new HorseMessage((HorseBet) null, "Finished");
                        horseMessage.setID(user.getID());
                        new Transmission(horseMessage, this.networkManager);
                        firstFinish = false;

                    }
                }
                horseMessage = (HorseMessage) networkManager.readContext("HORSES-Result");
                if(horseMessage!= null){
                    System.out.println("HORSES- Done");
                    this.isRacing = false;
                    this.raceCountdown.stopCount();
                    this.waitCountdown.stopCount();
                    if(horseMessage.getHorseResult().getPrize() == 0){
                        //TODO: Missatge no ha guanyat
                    }else{
                        new Transaction("HorseBet", user.getUsername(), horseMessage.getHorseResult().getPrize(), 1);
                        //TODO: Mostra missatge que ha guanyat aposta
                    }
                    //TODO: Mostra missatge guanyador = horseMessage.getHorseResult().getWinner();
                }

            }
        }
    }

    /**Parem de jugar*/
    public void stopPlay(){
        this.play = false;
        this.isRacing = false;
        this.isBetting = false;
        this.graphicsManager.exit();
        waitCountdown.stopCount();
        raceCountdown.stopCount();
    }


    @Override
    public void render(Graphics g) {
        //TODO: Aqui cal fer que s'actualitzi la posicio dels caballs

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
    public void mouseClicked(MouseEvent e) {

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

    @Override
    public void actionPerformed(ActionEvent e) {
        HorseMessage horseMessage;
        switch (e.getActionCommand()){
            case"HORSES-Back":
                finestra.setGameSelector(false); //Un guest no podra jugar a cavalls
                networkManager.exitHorses();
                stopPlay();
                break;
            case "Horses-Bet":
                horseMessage  = new HorseMessage(new HorseBet(horseRaceView.getBetAmount(),horseRaceView.getBetHorse(), user.getID()), "Bet");
                horseMessage.setID(user.getID());
                new Transmission(horseMessage, this.networkManager);
                this.isBetting = true;

        }
    }
}
