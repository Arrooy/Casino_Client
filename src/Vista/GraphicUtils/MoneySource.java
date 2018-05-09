package Vista.GraphicUtils;

import java.awt.*;

/**
 * Classe que te la finalitat de representar un punt d'extracció de
 * fitxes. Consisteix en un punt d'on l'usuari pot extreure'n fitxes
 * per anar apostant a un tauler.
 */
public class MoneySource {

    private int value;
    private int position;

    public MoneySource(int value) {
        this.value = value;

        //TODO: Calcular posició segons el valor
    }

    public void render(Graphics g) {

        //TODO: Pintar la fitxa a un dels forats de la part inferior de la pantalla

    }
}