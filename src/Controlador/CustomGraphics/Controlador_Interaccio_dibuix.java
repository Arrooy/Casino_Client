package Controlador.CustomGraphics;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;

/** #RENAME ME?????*/
public abstract class Controlador_Interaccio_dibuix implements KeyListener, MouseInputListener {

    @Override
    public abstract void keyTyped(KeyEvent e);

    @Override
    public abstract void keyPressed(KeyEvent e);

    @Override
    public abstract void keyReleased(KeyEvent e);

    @Override
    public abstract void mouseClicked(MouseEvent e);

    @Override
    public abstract void mousePressed(MouseEvent e);

    @Override
    public abstract void mouseReleased(MouseEvent e);

    @Override
    public abstract void mouseEntered(MouseEvent e);

    @Override
    public abstract void mouseExited(MouseEvent e);
    @Override
    public abstract void mouseDragged(MouseEvent e);

    @Override
    public abstract void mouseMoved(MouseEvent e);

    public abstract void init();
    public abstract void update(float delta);
    public abstract void render(Graphics g);
}
