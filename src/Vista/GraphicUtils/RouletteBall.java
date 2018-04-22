package Vista.GraphicUtils;

import Controlador.Game_Controlers.RouletteController;

import java.awt.*;
import java.util.LinkedList;

public class RouletteBall {

    private final float gravity = 0.2f;
    private static double initVel = 250;

    private double x, y;
    private double velX, velY;
    private double r;

    private double centerX, centerY, limit, intLim;

    private RouletteController c;
    private boolean relrot;

    public RouletteBall(double x, double y, double centerX, double centerY, RouletteController c, double limit, double intLim) {
        this.x = x;
        this.y = y;
        this.centerX = centerX;
        this.centerY = centerY;
        this.limit = limit;
        this.intLim = intLim;

        velX = 0;
        velY = initVel;

        relrot = false;
        this.c = c;

        r = 3;
    }

    public void setDefaultVelY(double velY) {
        RouletteBall.initVel = velY;
    }

    private void enableRelativeRotation() {
        relrot = true;
        velY = 0;
        velX = 0;
    }

    /*public void setRelativeRotation(double vel, int refx, int refy) {
        double vectX = (x - refx);
        double vectY = (y - refy);

        double a = Math.PI / (vel);

        if (relrot) {
            x = ((vectX * Math.cos(a) - vectY * Math.sin(a))) + refx;
            y = ((vectX * Math.sin(a) + vectY * Math.cos(a))) + refy;
        }
    }*/

    public void update(float delta, double vel, int refx, int refy) {
        double vectX = (x - centerX);
        double vectY = (y - centerY);

        if (!relrot) {
            if (dist(x, y, centerX, centerY) > limit - r*3) {
                velX = 0;
                velY = 0;
                enableRelativeRotation();
                for (GRect r: c.getBars()) correctFinalPosition(r);
                c.setWinner(findWinner());
            }

            if (dist(x, y, centerX, centerY) < intLim){
                velX += vectX * gravity;
                velY += vectY * gravity;
            }

            x += velX * delta;
            y += velY * delta;

        } else {
            double a = Math.PI / (vel);

            x = ((vectX * Math.cos(a) - vectY * Math.sin(a))) + refx;
            y = ((vectX * Math.sin(a) + vectY * Math.cos(a))) + refy;

            if (c.getWinner() == 100) c.setWinner(findWinner());
        }
    }

    private double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillOval((int) (x - r), (int) (y - r), (int) (2*r), (int) (2*r));

        g.setColor(Color.black);
        g.drawOval((int) (x - r), (int) (y - r), (int) (2*r), (int) (2*r));
    }

    private void correctFinalPosition(GRect rect) {
        double[] dist = new double[4];
        if (lineCollision(rect.getX4(), rect.getY4(), rect.getX2(), rect.getY2(), dist, 0)) correctPosition(rect.getX4(), rect.getY4(), rect.getX2(), rect.getY2());
        if (lineCollision(rect.getX2(), rect.getY2(), rect.getX1(), rect.getY1(), dist, 1)) correctPosition(rect.getX2(), rect.getY2(), rect.getX1(), rect.getY1());
        if (lineCollision(rect.getX3(), rect.getY3(), rect.getX4(), rect.getY4(), dist, 2)) correctPosition(rect.getX3(), rect.getY3(), rect.getX4(), rect.getY4());
        if (lineCollision(rect.getX1(), rect.getY1(), rect.getX3(), rect.getY3(), dist, 3)) correctPosition(rect.getX1(), rect.getY1(), rect.getX3(), rect.getY3());
    }

    public boolean rectCollision(GRect rect) {
        double[] dist = new double[4];
        boolean b = false;

        if (relrot) return false;

        if (lineCollision(rect.getX4(), rect.getY4(), rect.getX2(), rect.getY2(), dist, 0)) b = true;
        if (lineCollision(rect.getX2(), rect.getY2(), rect.getX1(), rect.getY1(), dist, 1)) b = true;
        if (lineCollision(rect.getX3(), rect.getY3(), rect.getX4(), rect.getY4(), dist, 2)) b = true;
        if (lineCollision(rect.getX1(), rect.getY1(), rect.getX3(), rect.getY3(), dist, 3)) b = true;

        if (!b) return false;

        int min = 0;
        for (int i = 0; i < 4; i++) min = dist[min] > dist[i] ? i : min;

        switch (min) {
            case 0:
                redirectBall(rect.getX4(), rect.getX2(), rect.getY4(), rect.getY2());
                break;
            case 1:
                redirectBall(rect.getX2(), rect.getX1(), rect.getY2(), rect.getY1());
                break;
            case 2:
                redirectBall(rect.getX3(), rect.getX4(), rect.getY3(), rect.getY4());
                break;
            case 3:
                redirectBall(rect.getX1(), rect.getX3(), rect.getY1(), rect.getY3());
                break;
        }

        return true;
    }

    private int getCollidedLine(GRect rect) {
        double xc = (rect.getX1() + rect.getX2() + rect.getX3() + rect.getX4()) / 4;
        double yc = (rect.getY1() + rect.getY2() + rect.getY3() + rect.getY4()) / 4;

        double vectX = x - xc;
        double vectY = y - yc;

        double[][] v = new double[4][2];

        v[0][0] = rect.getX1() - xc;
        v[0][1] = rect.getY1() - yc;

        v[1][0] = rect.getX2() - xc;
        v[1][1] = rect.getY2() - yc;

        v[2][0] = rect.getX3() - xc;
        v[2][1] = rect.getY3() - yc;

        v[3][0] = rect.getX4() - xc;
        v[3][1] = rect.getY4() - yc;

        double ab = Math.atan2(vectY, vectX);
        double[] a = new double[4];

        for (int i = 0; i < 4; i++) a[i] = Math.atan2(v[i][1], v[i][0]);
        int min = 0, max = 0;
        for (int i = 0; i < 4; i++) {
            if (a[max] < a[i]) max = i;
            if (a[min] > a[i]) min = i;
        }

        if (a[max] < ab || a[min] > ab) {
            if (min == 1 && max == 3) return 0;
            if (min == 0 && max == 1) return 1;
            if (min == 3 && max == 2) return 2;
            if (min == 2 && max == 0) return 3;
        }

        if (a[1] > ab && a[3] < ab) return 0;
        if (a[0] > ab && a[1] < ab) return 1;
        if (a[3] > ab && a[2] < ab) return 2;
        if (a[2] > ab && a[0] < ab) return 3;

        return 0;
    }

    private void redirectBall(double x1, double x2, double y1, double y2) {
        double dx = y1 - y2;
        double dy = x2 - x1;

        double b = Math.atan2(dy, dx);
        double a = Math.atan2(velY, velX);

        double module = Math.sqrt(velX*velX + velY*velY);

        double f = a + 2*(a - b);

        velY = module * Math.sin(f);
        velX = module * Math.cos(f);

        correctPosition(x2, y2, x1, y1);
    }

    private void correctPosition(double x1, double y1, double x2, double y2) {
        double mp = (x2 - x1) / (y1 - y2);
        double m = (y2 - y1) / (x2 - x1);
        double np = y - mp * x;
        double n = y1 - m * x1;

        double x0 = (n - np) / (mp - m);
        double y0 = mp * x0 + np;

        double e = 1 + mp*mp;
        double f = -2*x0 + 2*mp*np - 2*y0*mp;
        double g = x0*x0 + np*np -2*y0*np + y0*y0 - r*r;

        double xr1 = (-f + Math.sqrt(f*f - 4 * e * g)) / (2 * e);
        double xr2 = (-f - Math.sqrt(f*f - 4 * e * g)) / (2 * e);

        double yr1 = mp * xr1 + np;
        double yr2 = mp * xr2 + np;

        double diff1 = Math.sqrt(Math.pow(xr1 - 300, 2) + Math.pow(yr1 - 300, 2)); //TODO: fer centre generic
        double diff2 = Math.sqrt(Math.pow(xr2 - 300, 2) + Math.pow(yr2 - 300, 2)); //TODO: fer centre generic

        if (diff1 - diff2 > r/2) {
            x = diff1 > diff2 ? xr2 : xr1;
            y = diff1 > diff2 ? yr2 : yr1;
        } else {
            if (Math.atan2(velY, velX) > 0) {
                if (yr1 < yr2) {
                    x = xr1;
                    y = yr1;
                } else {
                    x = xr2;
                    y = yr2;
                }
            } else {
                if (yr1 > yr2) {
                    x = xr1;
                    y = yr1;
                } else {
                    x = xr2;
                    y = yr2;
                }
            }
        }
    }

    private int findWinner() {
        LinkedList<GRect> bars = c.getBars();
        int i;
        int found = 100;

        for (i = 0; i < bars.size(); i++) {
            GRect r0 = bars.get(i);
            GRect r1 = bars.get((i + 1) % bars.size());

            double a0 = (Math.atan2(r0.getY1() - centerY, r0.getX1() - centerX) + Math.atan2(r0.getY3() - centerY, r0.getX3() - centerX))/2;
            double a1 = (Math.atan2(r1.getY3() - centerY, r1.getX3() - centerX) + Math.atan2(r1.getY1() - centerY, r1.getX1() - centerX))/2;

            double ab = Math.atan2(y - centerY, x - centerX);

            if (ab > a0 && ab < a1) found = i;
        }

        return found;
    }

    private boolean lineCollision(double x1, double y1, double x2, double y2, double[] dist, int index) {
        double m1 = (y2 - y1) / (x2 - x1);
        double m2 = (x2 - x1) / (y1 - y2);//(x1 - x2) / (y2 - y1);
        double n1 = y1 - m1*x1;
        double n2 = y - m2 * x;
        double xr = (n2 - n1) / (m1 - m2);
        double yr = m1 * xr + n1;
        double d = Math.sqrt(Math.pow(xr - x, 2) + Math.pow(yr - y, 2));

        boolean b;
        if (x2 > x1) b = xr > x1 && xr < x2;
        else b = xr > x2 && xr < x1;

        if (y1 < y2) b &= yr > y1 && yr < y2;
        else b &= yr > y2 && yr < y1;

        dist[index] = d;
        return d < r && b;//d < r &&
    }

    public boolean outOfScreen(){
        return x < -1000 || x > 10000|| y < -1000 || y > 10000;
    }
}
