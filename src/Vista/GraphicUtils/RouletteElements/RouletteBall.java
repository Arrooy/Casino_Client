package Vista.GraphicUtils.RouletteElements;

import Controlador.Game_Controlers.RouletteController;
import Controlador.Sounds;
import java.awt.*;
import java.util.LinkedList;


/**
 * Classe que gestiona la bola que es tira a la ruleta i que gestiona
 * totes les fisiques necessàries.
 *
 * Concretament genera una força que empeny a la bola cap a fora del centre de
 * la ruleta, afectant a la velocitat i direcció d'aquesta. A més a més es controlen
 * les fisiques de col·lisió contra els separadors de les cel·les que fan que la bola
 * reboti normalment vàries vegades abans de caure a una cel·la final.
 *
 * Seguidament també es controla si la bola cau en una cel·la, i en cas afirmatiu
 * s'anul·la la velocitat i acceleracio de la bola, fent que aquesta giri coordinadament
 * amb la resta del tauler. Aquest mode s'anomena "Relative Rotation"
 */
public class RouletteBall {

    /** Gravetat que s'aplica a la bola */
    private final float gravity = 0.2f;

    /** Velocitat inicial de la bola */
    private static double initVel = 250;

    /** Posició de la bola dins un panell de 600x600 */
    private double x, y;

    /** Velocitat en els eixos x i y */
    private double velX, velY;

    /** Radi de la bola */
    private double r;

    /** Centre del panell i radi de la circumferencia limit */
    private double centerX, centerY, limit, intLim;

    /** Classe que gestiona la simulació sencera de la tirada */
    private RouletteController c;

    /** Atribut que indica si la bola ha de rotar de manera sincronitzada amb la ruleta */
    private boolean relrot;

    /**
     * Constructor de la classe
     * @param x Posició x inicial
     * @param y Posició y inicial
     * @param centerX Posició central del panell
     * @param centerY Posició central del panell
     * @param c Gestor de la simulació
     * @param limit Limit fins el que es pot moure lliurement la bola
     * @param intLim Limit interior
     */
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

    /**
     * Setter de la velocitat inicial de la simulació
     * @param velY Velocitat
     */
    public void setDefaultVelY(double velY) {
        RouletteBall.initVel = velY;
    }

    /** Mètode que activa la rotació relativa a la ruleta */
    private void enableRelativeRotation() {
        relrot = true;
        velY = 0;
        velX = 0;
    }

    /**
     * Mètode que actulitza la posició de la bola
     * @param delta Valor per a escalar la velocitat
     * @param vel Veocitat en la que rota la ruleta
     * @param refx Posició central de la ruleta
     * @param refy Posició central de la ruleta
     */
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

                Sounds.stopOneAudioFile("RRun.wav");
                Sounds.play("REnd.wav");
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

    /**
     * Funció que calcula el mòdul d'una distància entre dos punts
     * @return Mòdul de la distancia
     */
    private double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
    }

    /**
     * Mètode per a pintar la bola. Simplement pinta un cercle blanc amb una
     * vora negre, a la posició indicada.
     *
     * A diferencia de la funcio update() que actualitza la logoca en un panell de
     * 600x600, aquesta funció s'aplica en la totalitat de la pantalla, de manera
     * que aquesta requereix com a parametres el resplaçament necessari que se
     * l'hi ha d'aplicar per a mantenir una completa sincronització amb
     * la resta d'elements visuals de la pantalla.
     * @param g Graphics en els que pintar
     * @param rad Radi de la ruleta
     */
    public void render(Graphics g, double rx, double ry, int rad) {
        g.setColor(Color.white);
        g.fillOval((int) ((x - centerX) * (rad/100) + rx - r * (rad/100)), (int) ((y - centerY) * (rad/100) + ry - r * (rad/100)), (int) (2*r) * (rad/100), (int) (2*r) * (rad/100));

        g.setColor(Color.black);
        g.drawOval((int) ((x - centerX) * (rad/100) + rx - r * (rad/100)), (int) ((y - centerY) * (rad/100) + ry - r * (rad/100)), (int) (2*r) * (rad/100), (int) (2*r) * (rad/100));
    }

    /**
     * Mètode que corregeix la posició final de la bola en la ruleta
     * per evitar que aquesta quedi en posicions incertes en les que es
     * dificil apreciar la cel·la en la que ha caigut
     * @param rect Rectangle amb el que pot estar col·lisionant i es requereix corregir el fet
     */
    private void correctFinalPosition(GRect rect) {
        double[] dist = new double[4];
        if (lineCollision(rect.getX4(), rect.getY4(), rect.getX2(), rect.getY2(), dist, 0)) correctPosition(rect.getX4(), rect.getY4(), rect.getX2(), rect.getY2());
        if (lineCollision(rect.getX2(), rect.getY2(), rect.getX1(), rect.getY1(), dist, 1)) correctPosition(rect.getX2(), rect.getY2(), rect.getX1(), rect.getY1());
        if (lineCollision(rect.getX3(), rect.getY3(), rect.getX4(), rect.getY4(), dist, 2)) correctPosition(rect.getX3(), rect.getY3(), rect.getX4(), rect.getY4());
        if (lineCollision(rect.getX1(), rect.getY1(), rect.getX3(), rect.getY3(), dist, 3)) correctPosition(rect.getX1(), rect.getY1(), rect.getX3(), rect.getY3());
    }

    /**
     * Mètode que indica si la bola col·lisiona amb un rectangle concret,
     * comprovant la intersecció amb els marges d'aquest. No és necessari comprovar
     * si es troba a dins ja que tots els rectangles als que s'aplica aquesta funció
     * són més estrets que la propia bola.
     * @param rect Rectangle a comprovar
     * @return Boolea que indica si existeix o no la col·lisió
     */
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

    /**
     * Mètode que redirigeix la velocitat de la bola després d'una col·lisió contra
     * un rectangle. Concretament el mètode serveix per redirigir la bola
     * respecte una recta concreta donada per dues coordenades.
     */
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

    /**
     *  Metode que corregeix la posició de la bola.
     *  La seva finalitat consisteix en evitar la situació en la que la bola s'actualitza
     *  col·lisionant contra un rectangle i al redirigir la direcció, la gravetat fa que en
     *  la següent actualització la bola segueixi dins el rectangle, i així entrar en un bucle
     *  de col·lisions constants. Per a evitar aixó, es treu la bola fora de la zona de col·lisió.
     *  De manera que aques mètode es dedica a calcular la posició òptima a la que recol·locar la
     *  bola de manera que sigui el minim perceptible possible.
     */
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

        double diff1 = Math.sqrt(Math.pow(xr1 - 300, 2) + Math.pow(yr1 - 300, 2));
        double diff2 = Math.sqrt(Math.pow(xr2 - 300, 2) + Math.pow(yr2 - 300, 2));

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

    /**
     * Mètode que comprova a quina cel·la es troba de la ruleta
     * @return Index de la cel·la estacionada.
     */
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

    /**
     * Mètode que comprova la intersecció de la bola amb una linia indicada.
     * @param x1 Posició dels extrems de la linia
     * @param dist Distancia del centre a la linia
     * @param index Index del costat comprovat
     * @return Booleà que indica si hi ha o no col·lisió contra la linia
     */
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
}
