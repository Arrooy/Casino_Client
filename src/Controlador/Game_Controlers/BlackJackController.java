package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.GraphicsController;
import Model.Card;
import Network.NetworkManager;
import Network.Transmission;
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
        gp.setBounds(0,0,blackJackView.getWidth(),blackJackView.getHeight());
        blackJackView.add(gp);
    }
    public void updateSize(boolean fully){
        gp.updateSize(blackJackView.getWidth(),blackJackView.getHeight(),fully);
        gp.setBounds(0,0,blackJackView.getWidth(),blackJackView.getHeight());
        blackJackView.updateUI();
    }

    public void newBJCard(Card cartaResposta, Controller c) {

        blackJackView.addCardIntoGame(cartaResposta);

        //control de la carta nova!
        if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_FINISH_USER)){
            blackJackView.giraIA();
        }

        if(cartaResposta.getDerrota().equals("user")){
            finishGame(false,c);
        }else if(cartaResposta.getDerrota().equals("IA")){
            finishGame(true,c);
        }
    }

    private void finishGame(boolean winner,Controller c) {
        if(winner){
            c.displayError("USER WIN GAME!","meh");
        }else{
            c.displayError("USER LOOSE GAME!","hurray");
        }
        gp.exit();
        blackJackView.remove(gp);
        blackJackView.reset();
        c.showGamesView();
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
        System.out.println(e.getButton());
        if(e.getButton() == 1){
            networkManager.newBlackJackCard(false);
        }else{
            networkManager.endBlackJackTurn();
        }

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
