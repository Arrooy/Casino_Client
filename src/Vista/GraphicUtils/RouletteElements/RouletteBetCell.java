package Vista.GraphicUtils.RouletteElements;

import Controlador.Controller;
import java.awt.*;

/**
 * Classe que representa una cel·la del taulell d'apostes de la ruleta, la
 * qual permet realitzar l'acció d'apostar.
 *
 * Cada cel·la del taulell d'apostes està col·Locada sobre la seva respectiva
 * cel·la de la imatge carregada del tauler, de manera que fent resize de la
 * pantalla o qualsevol acció, aquesta sempre quedi en la posició i tamany corresponents.
 *
 * En cas de realitzar-se alguna aposta, simplement apareixeria un número indicant
 * la quantitat de monedes apostades a la part suprtior central del requadre.
 */
public class RouletteBetCell {

    /** Tipus de cel·la de format 0 */
    public static final int ZERO_CELL = 0;

    /** Tipus de cel·la de format de número */
    public static final int NUMBER_CELL = 1;

    /** Tipus de cel·la de format d'aposta senzilla */
    public static final int FIFTY_CELL = 2;

    /** Tipus de cel·la de format d'aposta de 33% */
    public static final int THIRTY_CELL = 3;

    /** Variables que indiquen la posició en pantalla i el format de dimensions */
    private int x, y, type;

    /** Dimensions de la cel·la */
    private int width, height;

    /** Quantitat de monedes apostades */
    private long money;

    /**
     * Constructor de la classe. Inicialitza les dimansions de la cel·la
     * en funció del tipus indicat
     * @param x Coordenada de la cel·la
     * @param y Coordenada de la cel·la
     * @param type Tipus de cel·la
     */
    public RouletteBetCell(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        money = 0;

        switch (type) {
            case ZERO_CELL:
                width = getCellWidth();
                height = 3 * getCellHeight();
                break;
            case NUMBER_CELL:
                width = getCellWidth();
                height = getCellHeight();
                break;
            case FIFTY_CELL:
                width = 2 * getCellWidth();
                height = getCellHeight();
                break;
            case THIRTY_CELL:
                width = 4 * getCellWidth();
                height = getCellHeight();
                break;
        }
    }

    /**
     * Mètode per a actualitzar la posició al modificar el tamany
     * de la finestra
     * @param x Nova coordenada de la cel·la
     * @param y Nova coordenada de la cel·la
     */
    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Mètode per afegir diners a la cel·la al realitzar una aposta
     * @param bet Quantitat a afegir
     */
    public void addMoney(long bet) {
        money += bet;
    }

    /**
     * Mètode que calcua l'amplada que hauria de tenir la cel·la més petita
     * @return Amplada teòrica de la cel·la
     */
    public static int getCellWidth() {
        return (int) (Controller.getWinWidth() * 190.3f/3508f);
    }

    /**
     * Mètode que calcua l'alçada que hauria de tenir la cel·la més petita
     * @return Alçada teòrica de la cel·la
     */
    public static int getCellHeight() {
        return (int) (Controller.getWinHeight() * 340f/2480f);
    }

    /**
     * Mètode que actualitza les dimensions reals de la cel·la
     */
    public void update() {
        switch (type) {
            case ZERO_CELL:
                width = getCellWidth();
                height = 3 * getCellHeight();
                break;
            case NUMBER_CELL:
                width = getCellWidth();
                height = getCellHeight();
                break;
            case FIFTY_CELL:
                width = 2 * getCellWidth();
                height = getCellHeight();
                break;
            case THIRTY_CELL:
                width = 4 * getCellWidth();
                height = getCellHeight();
                break;
        }
    }

    /**
     * Mètode que renderitza el numero de diners apostats a la cel·la
     */
    public void render(Graphics g) {
        int sw = g.getFontMetrics().getStringBounds("" + money, g).getBounds().width;
        if (money != 0) g.drawString("" + money, x + width/2 - sw/2, y + 20);
    }

    /** Getter de X */
    public int getX() {
        return x;
    }

    /** Setter de X */
    public void setX(int x) {
        this.x = x;
    }

    /** Getter de Y */
    public int getY() {
        return y;
    }

    /** Setter de Y */
    public void setY(int y) {
        this.y = y;
    }

    /** Getter de l'amplitud */
    public int getWidth() {
        return width;
    }

    /** Setter de l'amplitud */
    public void setWidth(int width) {
        this.width = width;
    }

    /** Getter de l'alçada */
    public int getHeight() {
        return height;
    }

    /** Setter de la alçada */
    public void setHeight(int height) {
        this.height = height;
    }

    /** Setter dels diners apostats */
    public void setMoney(long money) {
        this.money = money;
    }
}
