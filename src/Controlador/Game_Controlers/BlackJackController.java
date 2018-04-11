package Controlador.Game_Controlers;

import Controlador.GraphicsController;
import Model.Card;
import Network.NetworkManager;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.GraphicsPanel;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class BlackJackController extends GraphicsController {

    private BlackJackView blackJackView;
    private NetworkManager networkManager;
    private int mouseX,mouseY;
    private GraphicsPanel gp;

    public BlackJackController(BlackJackView blackJackView,NetworkManager networkManager){
        this.networkManager = networkManager;
        this.blackJackView  = blackJackView;
        this.gp = new GraphicsPanel(blackJackView.getWidth(),blackJackView.getHeight());
        gp.setCurrentDrawing(blackJackView,this);
        gp.setBackgroundColor(Color.red);
        blackJackView.add(gp);
    }
    public void updateSize(boolean fully){
        gp.updateSize(blackJackView.getWidth(),blackJackView.getHeight(),fully);
        blackJackView.updateUI();
    }

    public void newBJCard(Card cartaResposta) {
        blackJackView.addCardIntoGame(cartaResposta);
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        networkManager.newBlackJackCard(false);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
