package Vista.GraphicUtils.RouletteElements;

/**
 * Classe que representa un rectangle. Aquest es genera
 * a partir de 4 punts, i es proporcionen mètodes per a rotar
 * amb facilitat aquest rectangle.
 */
public class GRect {

    /** Coordenades dels 4 punts que conformen el rectangle */
    /*
    p1 - p2
    |     |
    p3 - p4
     */
    private double x1, y1, x2, y2, x3, y3, x4, y4;

    /**
     * Constructor de la classe. Inicialitza la posició dels 4 punts, i es
     * roten els 4 en funció dels parametres introduits i respecte el punt indicat
     * @param x Posició del punt superior esquerra
     * @param y Posició del punt superior esquerra
     * @param width Amplada del punt
     * @param height Alçada del punt
     * @param a Angle inicial de rotació
     * @param xref Coordenada respecte la que es rota
     * @param yref Coordenada respecte la que es rota
     */
    public GRect(int x, int y, int width, int height, double a, int xref, int yref) {
        this.x1 = x;
        this.y1 = y;

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

    /**
     * Mètode que desplaça un punt sobre un eix circular
     * @param a Randiants que es desplaça el punt
     * @param x Coordenades inicials del punt a desplaçar
     * @param y Coordenades inicials del punt a desplaçar
     * @param refx Referencia respecte la que es realitza la rotacio
     * @param refy Referencia respecte la que es realitza la rotacio
     * @return Noves coordenades del punt en format d'array de dues caselles
     */
    private double[] rotate(double a, double x, double y, int refx, int refy){
        double vectX = (x - refx);
        double vectY = (y - refy);

        x = (vectX * Math.cos(a) - vectY * Math.sin(a));
        y = (vectX * Math.sin(a) + vectY * Math.cos(a));

        return new double[] {x, y};
    }

    /**
     * mètode que rota el rectangle sencer.
     * @param vel Velocitat de rotacio
     * @param delta Parametre per a escalar la velocitat
     * @param refx Referencia respecte la que es realitza la rotacio
     * @param refy Referencia respecte la que es realitza la rotacio
     */
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

    /**
     * Mètode que rota un punt usant com a referencia la velocitat de rotació
     * i no l'angle en si
     * @param vel Velocitat de rotació (Invers: Quant més gran es el valor més lenta es la rotacio -> creixement logaritmic)
     * @param x Punt a rotar
     * @param y Punt a rotar
     * @param refx Referencia respecte la que es realitza la rotacio
     * @param refy Referencia respecte la que es realitza la rotacio
     * @return Nou punt
     */
    private double[] rotatePoint(final float vel, float delta, double x, double y, int refx, int refy) {
        double vectX = (x - refx);
        double vectY = (y - refy);

        double a = Math.PI/(vel);
        x = ((vectX * Math.cos(a) - vectY * Math.sin(a))) +refx;
        y = ((vectX * Math.sin(a) + vectY * Math.cos(a))) +refy;

        return new double[] {x, y};
    }

    /** Getter de X1 */
    public double getX1() {
        return x1;
    }

    /** Getter de Y1 */
    public double getY1() {
        return y1;
    }

    /** Getter de X2 */
    public double getX2() {
        return x2;
    }

    /** Getter de Y2 */
    public double getY2() {
        return y2;
    }

    /** Getter de X3 */
    public double getX3() {
        return x3;
    }

    /** Getter de Y3 */
    public double getY3() {
        return y3;
    }

    /** Getter de X4 */
    public double getX4() {
        return x4;
    }

    /** Getter de Y4 */
    public double getY4() {
        return y4;
    }
}
