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
        font = AssetManager.getEFont();

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
//        g.drawString(vel + "", 10, 10);
        //g.setFont(new Font("Elephant", Font.PLAIN, 20));
        Graphics2D g2d = (Graphics2D) g;

        //g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.white);

        g.drawImage(background, 0, 0, Controller.getWinWidth(), Controller.getWinHeight(), null);

        double offset = backAnim ? 20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 - 2, 4)) : 20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 +0.4, 4));
        int cx = Controller.getWinWidth()/2;
        int cy = Controller.getWinHeight()/2 - 70 - (int)offset;

        g.setFont(font.deriveFont(15f));
        g.setColor(Color.red);
        table.render(g);

        g.setFont(font.deriveFont(50f));

        if (!hideRoulette) g.drawImage(boardImage, cx - 300, -(int)offset, 600, 600, null);

        g2d.rotate(rang, cx, cy);
        if (!hideRoulette) g2d.drawImage(rouletteImage, cx - 200, cy - 200, 400, 400, null);

        g2d.rotate(-rang, cx, cy);

       /* g.setColor(Color.white);
        g.drawOval(cx - 104, cy - 104, 208, 208);
        g.drawOval(cx - 100, cy - 100, 200, 200);
        g.drawOval(cx - 100, cy - 100, 200, 200);

        g.drawLine(cx - 3, cy - 3, cx + 3, cy + 3);
        g.drawLine(cx - 3, cy + 3, cx + 3, cy - 3);*/

        /*int in = 0;
        for (GRect r: bars) {
         //   r.render(g, (int) rx, (int) ry);
            int w = g.getFontMetrics().getStringBounds("" + in, g).getBounds().width;
            int h = g.getFontMetrics().getStringBounds("" + in, g).getBounds().height;
            g.setColor(Color.red);
            g.drawString("" + ((in + shotOff) % MAXCELLS),
                    (int) ((r.getX1() - 300) * 2 + cx + (r.getX1() - cx/2)*0.15) - w/2, (int) ((r.getY1() - 300) * 2 + cy + (r.getY1() - cy/2)*0.15) + h/2);
            in++;

           // g.setColor(new Color(240, 191, 31, 100));
           // g.drawLine((int) r.getX1(), (int) r.getY1(), (int)rx/2, (int)ry/2);
           // g.drawLine((int) r.getX3(), (int) r.getY3(), (int)rx/2, (int)ry/2);
        }*/

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
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    /**
     * @deprecated
     */
    public void updateSize(int width, int height) {
        //this.width = width;
        //this.height = height;

        System.out.println(width + " x " + height);
    }

    public void start() {
        //TODO: fer animacio de com cau la ruleta
        shoot();
    }

    //TODO: Convertir en constant
    private int[] rouletteAdapter() {
        int[] table = new int[37];
        table[0] = 0;
        table[1] = 32;
        table[2] = 15;
        table[3] = 19;
        table[4] = 4;
        table[5] = 21;
        table[6] = 2;
        table[7] = 25;
        table[8] = 17;
        table[9] = 34;
        table[10] = 6;
        table[11] = 27;
        table[12] = 13;
        table[13] = 36;
        table[14] = 11;
        table[15] = 30;
        table[16] = 8;
        table[17] = 23;
        table[18] = 10;
        table[19] = 5;
        table[20] = 24;
        table[21] = 26;
        table[22] = 33;
        table[23] = 1;
        table[24] = 20;
        table[25] = 14;
        table[26] = 31;
        table[27] = 9;
        table[28] = 22;
        table[29] = 18;
        table[30] = 29;
        table[31] = 7;
        table[32] = 28;
        table[33] = 12;
        table[34] = 35;
        table[35] = 3;
        table[36] = 26;

        return table;
    }

    public void bet(long bet, int cellID) {
        table.bet(bet, cellID);
    }
}
