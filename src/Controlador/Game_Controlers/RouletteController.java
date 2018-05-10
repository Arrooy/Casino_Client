package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Model.AssetManager;
import Model.RouletteModel.RouletteBetMessage;
import Model.RouletteModel.RouletteMessage;
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

    private static final int[] converTable = {0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 26, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26};

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

    private double rx, ry, rang, roffTimer;
    private boolean backAnim, hideRoulette;

    private NetworkManager networkManager;

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
    }

    @Override
    public void init() {
        final int teoricWidth = 600;
        final int teoricHeight = 600;

        ball = new RouletteBall(teoricWidth / 2 - 20, teoricHeight / 2 - 50, teoricWidth / 2, teoricHeight / 2, this, 100, 80);
/*        bars = new LinkedList<>();

        for (int i = 0; i < 37; i++) {
            bars.add(new GRect(teoricWidth / 2 - 100, teoricHeight / 2 - 1, 20, 2, i * 2*Math.PI/37, teoricWidth/2, teoricHeight/2));
        }*/

        table = new RouletteBetTable();
        bars = new LinkedList<>();

        vel = 0;
        acc = 0.1;
        winnerE = false;

        rouletteImage = AssetManager.getImage("Rulet.png");
        background = AssetManager.getImage("casino ruleta sin marco.png");
        boardImage = AssetManager.getImage("marco ruleta MAS PEQE 2.png");
        font = AssetManager.getEFont(20);

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

        g.setColor(Color.white);

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
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("KEY PRESSED");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESCAPE PRESSED");
            networkManager.exitRoulette();
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

        rang = zeroAng - (Math.PI*2/MAXCELLS) * shotOff;
        roffTimer = System.nanoTime();
        backAnim = false;
        hideRoulette = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        new Transmission(new RouletteBetMessage(100, table.getCellID(e.getX(), e.getY())), networkManager);
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

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
}
