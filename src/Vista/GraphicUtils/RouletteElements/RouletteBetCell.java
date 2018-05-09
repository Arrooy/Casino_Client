package Vista.GraphicUtils.RouletteElements;

import Controlador.Controller;
import Model.AssetManager;
import Vista.GraphicUtils.MoneyStack;

import java.awt.*;
import java.util.LinkedList;

public class RouletteBetCell {

    private static int cellWidth = 50;
    private static int cellHeight = 110; //TODO: Posar valor que toca

    public static final int ZERO_CELL = 0;
    public static final int NUMBER_CELL = 1;
    public static final int FIFTY_CELL = 2;
    public static final int THIRTY_CELL = 3;

    private int x, y, type;
    private int width, height;

    private long money;

    private LinkedList<MoneyStack> fitxes;

    public RouletteBetCell(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        money = 0;
        fitxes = new LinkedList<>();

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
        return (int) (Controller.getWinWidth() * .05473);// cellWidth;
    }

    public static int getCellHeight() {
        return (int) (Controller.getWinHeight() * .1375);//cellHeight;
    }

    public void update() {
        cellWidth = (int) (Controller.getWinWidth() * .05473);
        cellHeight = (int) (Controller.getWinHeight() * .1375);
    }

    public void render(Graphics g) {
        //g.setColor(Color.white);
        //g.fillRect(x, y, width, height);
        //g.setColor(Color.red);
        g.drawRect(x, y, width, height);

        //g.setFont(AssetManager.getEFont().deriveFont(12f));
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
