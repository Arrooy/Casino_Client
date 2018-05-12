package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Model.AssetManager;
import Model.RouletteModel.RouletteBetMessage;
import Model.RouletteModel.RouletteMessage;
import Model.User;
import Network.NetworkManager;
import Network.Transmission;
import Vista.GraphicUtils.RouletteElements.GRect;
import Vista.GraphicUtils.RouletteElements.RouletteBall;
import Vista.GraphicUtils.RouletteElements.RouletteBetTable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;

public class RouletteController implements GraphicsController {

    private static final int MAXCELLS = 37;
    private static final double zeroAng = Math.PI/2 + (Math.PI*2/MAXCELLS)/2;
    private int width = 600, height = 600;

    public static final int LIST_DIM = 700;

    private static final int[] converTable = {0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 26, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26};
    private static final String[] listBetConversion = {"0", "3", "2", "1", "6", "5", "4", "9", "8", "7", "12", "11", "10", "15", "14", "13", "18", "17", "16", "21", "20", "19", "24", "23", "22", "27", "26", "25", "30", "29", "28", "33", "32", "31", "36", "35", "34", "First line", "Second line", "Third line", "First half", "Even", "Red", "Black", "Odd", "Second Half", "First dozen", "Second dozen", "Third dozen"};

    private LinkedList<GRect> bars;
    private RouletteBall ball;

    private double vel, acc;
    private static double initVel = 40;
    private boolean winnerE;
    private int winner;

    private int shotOff;

    private GraphicsManager gm;

    private static long nextTime = Timestamp.from(Instant.now()).getTime();

    private RouletteBetTable table;

    private Image background;
    private Image rouletteImage;
    private Image boardImage;
    private Font font;

    private Image wood;

    private double rx, ry, rang, roffTimer;
    private boolean backAnim, hideRoulette;

    private NetworkManager networkManager;

    private long wallet, bet;

    ////////////////////

    public static final int GAME_MODE = 0;
    public static final int LIST_MODE = 1;

    private int mode;

    private String[][] info;
    private int listOff;

    public RouletteController(int width, int height, NetworkManager networkManager) {
        this.width = width;
        this.height = height;
        this.gm = gm;
        this.networkManager = networkManager;

        rx = width/2;
        ry = height/2;
        rang = zeroAng;
        roffTimer = 0;
        backAnim = false;
        hideRoulette = true;

        shotOff = 0;
        mode = GAME_MODE;
        listOff = 0;

        wallet = 0;
        bet = 0;
    }

    public void initRoulette() {
        rx = width/2;
        ry = height/2;
        rang = zeroAng;
        roffTimer = 0;
        backAnim = false;
        hideRoulette = true;

        shotOff = 0;
        mode = GAME_MODE;
        listOff = 0;

        wallet = 0;
        bet = 0;
    }

    @Override
    public void init() {
        final int teoricWidth = 600;
        final int teoricHeight = 600;

        ball = new RouletteBall(teoricWidth / 2 - 20, teoricHeight / 2 - 50, teoricWidth / 2, teoricHeight / 2, this, 100, 80);

        table = new RouletteBetTable();
        bars = new LinkedList<>();

        vel = 0;
        acc = 0.1;
        winnerE = false;

        rouletteImage = AssetManager.getImage("Rulet.png");
        background = AssetManager.getImage("casino ruleta sin marco.png");
        boardImage = AssetManager.getImage("marco ruleta MAS PEQE 2.png");
        wood = AssetManager.getImage("Num.png");

        font = AssetManager.getEFont(50);

        info = new String[0][0];

        hideRoulette = true;
        backAnim = false;
    }

    public static void updateNextTime(long newTime) {
        nextTime = newTime;
    }

    public static String nextTimeToString() {
        long diff = nextTime - Timestamp.from(Instant.now()).getTime();
        return String.format("%02d:%02d", (int) (diff / (1000 * 60)), (int) (diff / 1000 % 60));
    }

    @Override
    public void update(float delta) {



        //////////////////////////////////////////////////////////////////////////////////////////////////////

        /* --- UNTOUCHABLE --- */

        final int teoricWidth = 600;
        final int teoricHeight = 600;

        acc = vel > 100 ? vel > 300 ? 2 : 0.5 : 0.1;

        vel += acc;
        vel = Math.min(100000, vel);

        for (GRect r: bars) r.updateRotation( (float) vel, 0.017f, teoricWidth/2, teoricHeight/2);

        ball.update(0.017f, vel, teoricWidth/2, teoricHeight/2);

        boolean bool = true;
        for (int i = bars.size() - 1; i >= 0 && bool; i--) if (ball.rectCollision(bars.get(i))) bool = false;

        //////////////////////////////////////////////////////////////////////////////////////////////////////

        rx = Controller.getWinWidth()/2;
        ry = Controller.getWinHeight()/2;
        rang += Math.PI / vel;
        if ((System.nanoTime() - roffTimer)/1000000000 > 10) {
            if (!backAnim) roffTimer = System.nanoTime();
            backAnim = true;
        }
        if (backAnim && ((20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 - 2, 4)) > 1000) || (System.nanoTime() - roffTimer)/1000000000 > 3)) hideRoulette = true;

        table.update();
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.white);

        switch (mode) {
            case GAME_MODE:
                renderRoulette(g);
                break;
            case LIST_MODE:
                renderList(g);
                break;
        }

        g.drawRect(20, Controller.getWinHeight()/2 - 20, 40,40);
        g.drawRect(Controller.getWinWidth() - 100, Controller.getWinHeight() - 100, 30,30);
    }

    private void renderList(Graphics g) {
        g.setColor(Color.red);
        g.drawRect(Controller.getWinWidth()/2 - LIST_DIM/2, Controller.getWinHeight()/2 - LIST_DIM/2, LIST_DIM, LIST_DIM);
        g.drawRect(Controller.getWinWidth()/2 + LIST_DIM/2 - 10, Controller.getWinHeight()/2 - 20, 20, 20);
        g.drawRect(Controller.getWinWidth()/2 + LIST_DIM/2 - 10, Controller.getWinHeight()/2, 20, 20);

        int zx = Controller.getWinWidth()/2 - LIST_DIM/2;
        int zy = Controller.getWinHeight()/2 - LIST_DIM/2 + 100;

        int[] cx = {zx + LIST_DIM/6, zx + LIST_DIM*3/6, zx + LIST_DIM*5/6};

        String[][] aux = networkManager.updateRouletteList();
        info = aux == null ? info : aux;

        if (info.length != 0) {
            g.setColor(new Color(216, 204, 163));
            g.setFont(font.deriveFont(17f));
            for (int i = 0; i < Math.min((info.length > 0 ? info[0].length : 0), 33); i++) {
                for (int j = 0; j < 3; j++) {
                    //System.out.println(info.length + " x " + info[0].length);
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

    private void renderRoulette(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(background, 0, 0, Controller.getWinWidth(), Controller.getWinHeight(), null);

        double offset = backAnim ? 20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 - 2, 4)) : 20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 +0.4, 4));
        int cx = Controller.getWinWidth()/2;
        int cy = 370 - (int)offset;

        g.setFont(font.deriveFont(15f));
        g.setColor(Color.red);
        table.render(g);

        g.setFont(font.deriveFont(50f));

        if (!hideRoulette) g.drawImage(boardImage, cx - 300, -(int)offset, 600, 600, null);

        g2d.rotate(rang, cx, cy);
        if (!hideRoulette) g2d.drawImage(rouletteImage, cx - 200, cy - 200, 400, 400, null);
        g2d.rotate(-rang, cx, cy);

        if (!hideRoulette) ball.render(g, cx, cy, 200);

        g.setColor(Color.white);
        g.drawString(nextTimeToString(), 10, 80);

        if (winnerE) g.drawString("" + getWinner(), (int)rx/2, (int)ry*4/5);
        g.drawString(wallet - bet + "", 20, Controller.getWinHeight() - 70);
    }

    public void setWinner(int winer) {
        winnerE = true;
        this.winner = winer;
    }

    public LinkedList<GRect> getBars() {
        return bars;
    }

    public void setParams(double vel, double ballVel, int shotOff) {
        RouletteController.initVel = vel;
        ball.setDefaultVelY(ballVel);
        this.shotOff = shotOff;
    }

    public int getWinner() {
        return converTable[(shotOff + winner) % MAXCELLS];
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("KEY PRESSED");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESCAPE PRESSED");
            networkManager.exitRoulette();
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            mode = mode == GAME_MODE ? LIST_MODE : GAME_MODE;
        }
    }

    public void shoot() {
        bars = new LinkedList<>();
        for (int i = 0; i < 37; i++) {
            bars.add(new GRect(width / 2 - 100, height / 2 - 1, 20, 2, i * 2*Math.PI/37, width/2, height/2));
        }
        ball = new RouletteBall(width / 2 - 20, height / 2 - 50, width / 2, height / 2, this, 100, 80);
        vel = initVel;
        winnerE = false;

        table.cleanTable();
        resetBetList();

        rang = zeroAng - (Math.PI*2/MAXCELLS) * shotOff;
        roffTimer = System.nanoTime();
        backAnim = false;
        hideRoulette = false;
    }

    public void requestWallet() {
        new Transmission(new User("", "", "walletRequest"), networkManager);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == GAME_MODE) {
            new Transmission(new RouletteBetMessage(100, table.getCellID(e.getX(), e.getY())), networkManager);
            System.out.println("[ROULETTE CELL]: " + table.getCellID(e.getX(), e.getY()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getX() < 60 && e.getX() > 20 && e.getY() > Controller.getWinHeight()/2 - 20 && e.getY() < Controller.getWinHeight() + 20) {
            System.out.println("Entra a " + (GAME_MODE == mode ? "Game mode" : "List mode")); //TODO:revisar
            mode = mode == GAME_MODE ? LIST_MODE : GAME_MODE;
            System.out.println("Entra a " + (GAME_MODE == mode ? "Game mode" : "List mode")); //TODO:revisar
        }
        if (mode == LIST_MODE) {
            if (e.getX() > Controller.getWinWidth()/2 + LIST_DIM/2 - 10 && e.getX() < Controller.getWinWidth()/2 + LIST_DIM/2 + 10
                    && e.getY() > Controller.getWinHeight()/2 - 20 && e.getY() < Controller.getWinHeight()/2)
                if (listOff > 0) listOff--;
            if (e.getX() > Controller.getWinWidth()/2 + LIST_DIM/2 - 10 && e.getX() < Controller.getWinWidth()/2 + LIST_DIM/2 + 10
                    && e.getY() > Controller.getWinHeight()/2 && e.getY() < Controller.getWinHeight()/2 + 20)
                if (info.length > 0 && info[0].length - listOff > 33) listOff++;

        }
        if (e.getX() > Controller.getWinWidth() - 100 && e.getX() < Controller.getWinWidth() - 70 && e.getY() > Controller.getWinHeight() - 100 && e.getY() < Controller.getWinHeight() - 70)
            networkManager.exitRoulette();
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    public void bet(long bet, int cellID) {
        table.bet(bet, cellID);
    }

    public void setWallet(long wallet) {
        this.wallet = wallet;
    }

    public void resetBet() {
        bet = 0;
    }

    public void increaseBet(long amount) {
        bet += amount;
    }
}
