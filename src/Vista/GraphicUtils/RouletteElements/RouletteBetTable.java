package Vista.GraphicUtils.RouletteElements;

import Controlador.Controller;
import Vista.GraphicUtils.MoneySource;

import java.awt.*;
import java.util.LinkedList;

public class RouletteBetTable {

    private static final int NUMOFSOURCES = 4;//TODO: posar valor correcte

    private LinkedList<RouletteBetCell> cells;
    private boolean ableToBet;

    private int x, y;

    private MoneySource[] sources;

    public RouletteBetTable() {
        cells = new LinkedList<>();

        x = Controller.getWinWidth()/2 - RouletteBetCell.getCellWidth() * 14 / 2 + 30;
        y = Controller.getWinHeight()/2 - RouletteBetCell.getCellHeight() * 5 / 2 - 37;

        cells.add(new RouletteBetCell(x, y + RouletteBetCell.getCellHeight(), RouletteBetCell.ZERO_CELL));
        for (int i = 0; i < 36 + 3; i++) cells.add(new RouletteBetCell(x + (1 + i / 3) * RouletteBetCell.getCellWidth(), y + (3 - i % 3) * RouletteBetCell.getCellHeight(), RouletteBetCell.NUMBER_CELL));
        for (int i = 0; i < 6; i++) cells.add(new RouletteBetCell(x + (1 + 2*i) * RouletteBetCell.getCellWidth(), y, RouletteBetCell.FIFTY_CELL));
        for (int i = 0; i < 3; i++) cells.add(new RouletteBetCell(x + (1 + 4*i) * RouletteBetCell.getCellWidth(), y + RouletteBetCell.getCellHeight() * 4, RouletteBetCell.THIRTY_CELL));
    }

    public void update() {
        cells.getFirst().updatePos(x, y + RouletteBetCell.getCellHeight());
        for (int i = 0; i < 36 + 3; i++) cells.get(i+1).updatePos(x + (1 + i / 3) * RouletteBetCell.getCellWidth(), y + (3 - i % 3) * RouletteBetCell.getCellHeight());
        for (int i = 0; i < 6; i++) cells.get(i+1+39).updatePos(x + (1 + 2*i) * RouletteBetCell.getCellWidth(), y);
        for (int i = 0; i < 3; i++) cells.get(i+1+39+6).updatePos(x + (1 + 4*i) * RouletteBetCell.getCellWidth(), y + RouletteBetCell.getCellHeight() * 4);
    }

    public void render(Graphics g) {
        for (RouletteBetCell rbc: cells) rbc.render(g);
    }


    public void bet(long bet, int cellID) {
        cells.get(cellID).addMoney(bet);
    }

    public int getCellID(int x, int y) {
        for (int i = 0; i < cells.size(); i++) {
            RouletteBetCell c = cells.get(i);
            if (c.getX() < x && (c.getX() + c.getWidth() > x && c.getY() < y && (c.getY() + c.getHeight()) > y)) return i;
        }
        return -1;
    }

    public void cleanTable() {
        for (RouletteBetCell c: cells) c.setMoney(0);
    }
}