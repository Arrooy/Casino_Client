package Vista.GraphicUtils.RouletteElements;

import Controlador.Controller;
import java.awt.*;

public class RouletteBetCell {

    private static int cellWidth = 50;
    private static int cellHeight = 110; //TODO: Posar valor que toca

    public static final int ZERO_CELL = 0;
    public static final int NUMBER_CELL = 1;
    public static final int FIFTY_CELL = 2;
    public static final int THIRTY_CELL = 3;

    private int x, y;
    private int width, height;

    private long money;

    public RouletteBetCell(int x, int y, int type) {
        this.x = x;
        this.y = y;

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

    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addMoney(long bet) {
        money += bet;
    }

    public static int getCellWidth() {
        return (int) (Controller.getWinWidth() * .0537);
    }

    public static int getCellHeight() {
        return (int) (Controller.getWinHeight() * .1375);
    }

    public void update() {
        cellWidth = (int) (Controller.getWinWidth() * .0537);
        cellHeight = (int) (Controller.getWinHeight() * .1375);
    }

    public void render(Graphics g) { //TODO: Versio final
        g.drawRect(x, y, width, height);
        g.drawString("" + money, x + 3, y + 20);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
