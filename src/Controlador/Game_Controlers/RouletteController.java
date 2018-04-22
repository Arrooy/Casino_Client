package Controlador.Game_Controlers;

import Controlador.CustomGraphics.GraphicsController;
import Vista.GraphicUtils.GRect;
import Vista.GraphicUtils.RouletteBall;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;

public class RouletteController implements GraphicsController {

    private int width = 600, height = 600;

    private LinkedList<GRect> bars;
    private RouletteBall ball;

    private LinkedList<Point[]> tests = new LinkedList<>();

    private double vel, acc;
    private static double initVel = 40;
    private boolean winnerE;
    private int winner;

    @Override
    public void init() {
        bars = new LinkedList<>();
        ball = new RouletteBall(width / 2 - 20, height / 2 - 50, width / 2, height / 2, this, 100, 80);

        for (int i = 0; i < 37; i++) {
            bars.add(new GRect(width / 2 - 100, height / 2 - 1, 20, 2, i * 2*Math.PI/37, width/2, height/2));
        }

        vel = initVel;
        acc = 0.1;
        winnerE = false;
    }

    @Override
    public void update(float delta) {
        acc = vel > 100 ? vel > 300 ? 2 : 0.5 : 0.1;

        vel += acc;
        vel = Math.min(100000, vel);

        for (GRect r: bars) r.updateRotation( (float) vel, 0.017f, width/2, height/2);

        ball.update(0.017f, vel, width/2, height/2);
        //ball.setRelativeRotation(vel, 300, 300); //TODO: generisa tete

        boolean bool = true;
        for (int i = bars.size() - 1; i >= 0 && bool; i--) if (ball.rectCollision(bars.get(i))) bool = false;
    }

    @Override
    public void render(Graphics g) {
        g.drawString(vel + "", 10, 10);

        g.drawOval(width/2 - 104, height/2 - 104, 208, 208);
        g.drawOval(width/2 - 100, height/2 - 100, 200, 200);
        g.drawOval(width/2 - 100, height/2 - 100, 200, 200);

        g.drawLine(width/2 - 3, height/2 - 3, width/2 + 3, height/2 + 3);
        g.drawLine(width/2 - 3, height/2 + 3, width/2 + 3, height/2 - 3);

        int in = 0;
        for (GRect r: bars) {
            r.render(g);
            int w = g.getFontMetrics().getStringBounds("" + in, g).getBounds().width;
            int h = g.getFontMetrics().getStringBounds("" + in, g).getBounds().height;
            g.drawString("" + in, (int) (r.getX1() + (r.getX1() - width/2)*0.15) - w/2, (int) (r.getY1() + (r.getY1() - height/2)*0.15) + h/2);
            in++;

            g.setColor(new Color(240, 191, 31, 100));
            g.drawLine((int) r.getX1(), (int) r.getY1(), width/2, height/2);
            g.drawLine((int) r.getX3(), (int) r.getY3(), width/2, height/2);
        }

        ball.render(g);

        if (winnerE) g.drawString("" + winner, width/2, height*4/5);
    }

    public void setWinner(int winer) {
        winnerE = true;
        this.winner = winer;
    }

    public void testLine(double x1, double x2, double y1, double y2) {
        Point[] p = new Point[2];
        p[0] = new Point((int) x1, (int) y1);
        p[1] = new Point((int) x2, (int) y2);
        tests.add(p);
    }

    public LinkedList<GRect> getBars() {
        return bars;
    }

    private void setRandomParams() {
        final int MAXRVEL = 35;
        final int MINRVEL = 65;

        final int MAXBVEL = 100;
        final int MINBVEL = 300;

        RouletteController.initVel = (MINRVEL - MAXRVEL) * Math.random() + MAXRVEL;
        ball.setDefaultVelY((MINBVEL - MAXBVEL) * Math.random() + MAXBVEL);
    }

    public int getWinner() {
        return winner;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            bars = new LinkedList<>();
            for (int i = 0; i < 37; i++) {
                bars.add(new GRect(width / 2 - 100, height / 2 - 1, 20, 2, i * 2*Math.PI/37, width/2, height/2));
            }
            ball = new RouletteBall(width / 2 - 20, height / 2 - 50, width / 2, height / 2, this, 100, 80);
            vel = initVel;
            winnerE = false;
        }

        if (e.getKeyChar() == KeyEvent.VK_ENTER) setRandomParams();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ball = new RouletteBall(width / 2 - 20, height / 2 - 50, width / 2, height / 2, this, 100, 80);
        vel = 40;
        winnerE = false;
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
