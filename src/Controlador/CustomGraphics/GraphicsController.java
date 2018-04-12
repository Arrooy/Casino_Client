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
    void init();
    void update(float delta);
    void render(Graphics g);
}
