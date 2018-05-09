package Vista.GraphicUtils;

import java.awt.*;

/**
 * Classe que serveix per a representar un piló de fitxes. Emmagatzema informació bàsica com
 * el valor de les fitxes que s'està acumulant i la quantitat d'elles.
 * A més a més també proporciona un mètode genèric per a representar-la gràficament.
 */
public class MoneyStack {

    private final int coinValue;
    private int num;

    /**
     * Constructor de la classe
     * @param coinValue Al crear la classe cal indicar el valor de les fitxes d'aquella pila.
     */
    public MoneyStack(int coinValue) {
        this.coinValue = coinValue;
    }

    /**
     * Mètode per a afegir fitxes a la pila
     * @param q Quantitat a afegir
     */
    public void addMoney(int q) {
        num += q;
    }

    /**
     * Calcula la quantitat total de diners que acumula la pila
     * @return Valor monetari de la pila
     */
    public int getTotalValue() {
        return coinValue * num;
    }

    /**
     * Representació gràfica de la pila.
     * @param g Component de gràfics on pintar la pila
     * @param x Coordenades de la fitxa base
     * @param y Coordenades de la fitxa base
     * @param width Amplada de les fitxes
     * @param height Alçada de les fitxes
     */
    public void render(Graphics g, int x, int y, int width, int height) {

        //TODO: Implementar

    }
}
