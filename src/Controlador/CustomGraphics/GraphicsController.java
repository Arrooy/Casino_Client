package Controlador.CustomGraphics;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Interficie genèrica per a definir el comportament d'un controlador per a qualsevol
 * panell gràfic. Està pensat per poder-se actualitzar i renderitzar 60 cops per segon
 * i per a ser controlat mitjançant teclat o bé ratolí
 *
 * @since 12/04/2018
 * @version 1.0.1
 */
public interface GraphicsController extends KeyListener, MouseInputListener {

    /**
     * Mètode per inicialitzar la lògica dels gràfics
     */
    void init();

    /**
     * Mètode per a actualitzar la lògica de la vista en tot moment
     * @param delta Periode d'actualitzacio de la pantalla (0.017s)
     */
    void update(float delta);

    /**
     * Mètode per a organitzar tots els components a mostrar per pantalla
     * @param g Element en el que pintar el contingut
     */
    void render(Graphics g);
}
