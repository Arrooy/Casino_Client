package Vista.GraphicUtils;

import java.awt.*;
import java.util.Vector;

public class GRect {

    private int width, height;
    private float ang;

    /*
    x1 - x2
    |     |
    x3 - x4
     */
    private double x1, y1, x2, y2, x3, y3, x4, y4;
    private double a, vel;

    /*TODO: corregir agrandament
    Al rotar a trossos (més grans al laggegar-se), el angle hauria de ser
    una mica menor de 90º
     */
    public GRect(int x, int y, int width, int height) {
        this.x1 = x;
        this.y1 = y;
        this.width = width;
        this.height = height;

        x2 = x + width;
        y2 = y;

        x3 = x;
        y3 = y + height;

        x4 = x + width;
        y4 = y + height;

        a = 0.001;
        vel = 1;

        ang = 0;
    }

    public GRect(int x, int y, int width, int height, double a, int xref, int yref) {
        this.x1 = x;
        this.y1 = y;
        this.width = width;
        this.height = height;

        x2 = x + width;
        y2 = y;

        x3 = x;
        y3 = y + height;

        x4 = x + width;
        y4 = y + height;

        double[] aux;

        aux = rotate(a, x1, y1, xref, yref);
        x1 = aux[0] + xref;
        y1 = aux[1] + yref;

        aux = rotate(a, x2, y2, xref, yref);
        x2 = aux[0] + xref;
        y2 = aux[1] + yref;

        aux = rotate(a, x3, y3, xref, yref);
        x3 = aux[0] + xref;
        y3 = aux[1] + yref;

        aux = rotate(a, x4, y4, xref, yref);
        x4 = aux[0] + xref;
        y4 = aux[1] + yref;
    }

    private double[] rotate(double a, double x, double y, int refx, int refy){
        double vectX = (x - refx);
        double vectY = (y - refy);

        x = (vectX * Math.cos(a) - vectY * Math.sin(a));
        y = (vectX * Math.sin(a) + vectY * Math.cos(a));

        return new double[] {x, y};
    }

    public void updateRotation(final float vel, float delta, int refx, int refy){
        double[] aux;

        aux = rotatePoint(vel, delta, x1, y1, refx, refy);
        x1 = aux[0];
        y1 = aux[1];

        aux = rotatePoint(vel, delta, x2, y2, refx, refy);
        x2 = aux[0];
        y2 = aux[1];

        aux = rotatePoint(vel, delta, x3, y3, refx, refy);
        x3 = aux[0];
        y3 = aux[1];

        aux = rotatePoint(vel, delta, x4, y4, refx, refy);
        x4 = aux[0];
        y4 = aux[1];
    }

    private double[] rotatePoint(final float vel, float delta, double x, double y, int refx, int refy) {
        double vectX = (x - refx);
        double vectY = (y - refy);

        double a = Math.PI/(vel);
        x = ((vectX * Math.cos(a) - vectY * Math.sin(a))) +refx;
        y = ((vectX * Math.sin(a) + vectY * Math.cos(a))) +refy;

        return new double[] {x, y};
    }

    private double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    public void render(Graphics g) {
        final int[] x = {(int) x1, (int) x2, (int) x4, (int) x3};
        final int[] y = {(int) y1, (int) y2, (int) y4, (int) y3};

        g.setColor(Color.white);
        g.fillPolygon(x, y, 4);
        g.setColor(Color.BLACK);
        g.drawPolygon(x, y, 4);
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getX3() {
        return x3;
    }

    public double getY3() {
        return y3;
    }

    public double getX4() {
        return x4;
    }

    public double getY4() {
        return y4;
    }
}
