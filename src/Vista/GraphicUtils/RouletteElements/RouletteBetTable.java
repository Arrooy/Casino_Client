package Vista.GraphicUtils.RouletteElements;

import Controlador.Controller;
import java.awt.*;
import java.util.LinkedList;

/**
 * Classe que gestiona el tauler d'apostes del joc de la ruleta.
 * Genera cel·les per apostar-hi ajustant-se a la mida de la pantalla i
 * a les proporcions de la imatge de fons que conté el tauler.
 *
 * Les cel·les que afegeix consisteixen en les 37 de les caselles de la ruleta,
 * 3 que corresponen a cada fila del tauler, 1 pels numeros del 1 al 18 i un pel
 * 19 al 36, una per les caselles pars i una altre per les impars, una per les vermelles
 * i una altre per a les negres, i finalment 3 més per a cada dotzena de caselles.
 *
 * En cas de rebre la instrucció d'afegir una nova aposta des del servidor, s'afegeix
 * un numero que va incrementant en funció de l'aposta realitzada, i el qual es renderitza
 * a la part superior de la cel·la per a indicar la quantitat de diners que s'han apostat.
 */
public class RouletteBetTable {

    /** Llistat de cel·les */
    private LinkedList<RouletteBetCell> cells;

    /** Coordenades (0, 0) relatives de la taula respecte el panell*/
    private double offx = 496f / 3508f * Controller.getWinWidth(), offy = 291f / 2480f * Controller.getWinHeight();

    /** Coordenades enteres de la taula */
    private int x, y;

    /**
     * Constructor de la classe. Inicialitza totes les cel·les i les afegeix al llistat,
     * en el que inicialment afegeix els numeros del 0 al 36, seguidament les apostes a linies,
     * les apostes de 50/50 (vermell/negre, parell/imparell, 1-18/19-36) i finalment les apostes
     * de 33% que consisteixen en les dotzenes.
     */
    public RouletteBetTable() {
        cells = new LinkedList<>();

        x = (int) offx;
        y = (int) offy;

        cells.add(new RouletteBetCell(x, y + RouletteBetCell.getCellHeight(), RouletteBetCell.ZERO_CELL));
        for (int i = 0; i < 36 + 3; i++) cells.add(new RouletteBetCell(x + (1 + i / 3) * RouletteBetCell.getCellWidth(), y + (3 - i % 3) * RouletteBetCell.getCellHeight(), RouletteBetCell.NUMBER_CELL));
        for (int i = 0; i < 6; i++) cells.add(new RouletteBetCell(x + (1 + 2*i) * RouletteBetCell.getCellWidth(), y, RouletteBetCell.FIFTY_CELL));
        for (int i = 0; i < 3; i++) cells.add(new RouletteBetCell(x + (1 + 4*i) * RouletteBetCell.getCellWidth(), y + RouletteBetCell.getCellHeight() * 4, RouletteBetCell.THIRTY_CELL));
    }

    /**
     * Mètode que actualitza el tamany i la posició de la taula en funció
     * del tamany de la pantalla
     */
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

    /**
     * Mètode que renderitza el numero de diners apostats en una cel·la qualsevol
     */
    public void render(Graphics g) {
        for (RouletteBetCell rbc: cells) rbc.render(g);
    }

    /**
     * Mètode per a inserir una aposta a una cel·la concreta
     * @param bet Quantitat apostada
     * @param cellID Cel·la apostada
     */
    public void bet(long bet, int cellID) {
        cells.get(cellID).addMoney(bet);
    }

    /**
     * Mètode que indica quina cel·la es troba en les coordenades indicades de
     * la pantalla. En cas de no existir-hi cap, es retornarà '-1'.
     * @param x Coordenada del JFrame
     * @param y Coordenada del JFrame
     * @return Identificador de la cel·la a apostar-hi
     */
    public int getCellID(int x, int y) {
        for (int i = 0; i < cells.size(); i++) {
            RouletteBetCell c = cells.get(i);
            if (c.getX() < x && (c.getX() + c.getWidth() > x && c.getY() < y && (c.getY() + c.getHeight()) > y)) return i;
        }
        return -1;
    }

    /**
     * Mètode que nateja totes les apostes realitzades en totes les cel·les
     */
    public void cleanTable() {
        for (RouletteBetCell c: cells) c.setMoney(0);
    }
}