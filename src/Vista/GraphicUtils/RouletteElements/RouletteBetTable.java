package Vista.GraphicUtils.RouletteElements;

import Controlador.Controller;

import java.awt.*;
import java.util.LinkedList;

public class RouletteBetTable {

    private LinkedList<RouletteBetCell> cells;

    private double offx = 496f / 3508f * Controller.getWinWidth();
    private double offy = 291f / 2480f * Controller.getWinHeight();

    private int x, y;

    public RouletteBetTable() {
        cells = new LinkedList<>();

        x = (int) offx;
        y = (int) offy;

        cells.add(new RouletteBetCell(x, y + RouletteBetCell.getCellHeight(), RouletteBetCell.ZERO_CELL));
        for (int i = 0; i < 36 + 3; i++) cells.add(new RouletteBetCell(x + (1 + i / 3) * RouletteBetCell.getCellWidth(), y + (3 - i % 3) * RouletteBetCell.getCellHeight(), RouletteBetCell.NUMBER_CELL));
        for (int i = 0; i < 6; i++) cells.add(new RouletteBetCell(x + (1 + 2*i) * RouletteBetCell.getCellWidth(), y, RouletteBetCell.FIFTY_CELL));
        for (int i = 0; i < 3; i++) cells.add(new RouletteBetCell(x + (1 + 4*i) * RouletteBetCell.getCellWidth(), y + RouletteBetCell.getCellHeight() * 4, RouletteBetCell.THIRTY_CELL));
    }

    public void update() {
        offx = 496f / 3508f * Controller.getWinWidth();
        offy = 291f / 2480f * Controller.getWinHeight();

        x = (int) offx;
        y = (int) offy;

        for (RouletteBetCell betCell: cells) betCell.update();

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