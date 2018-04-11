package Controlador;

import javax.swing.event.MouseInputListener;
import java.awt.event.*;


public abstract class GraphicsController implements KeyListener, MouseInputListener{

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
}
