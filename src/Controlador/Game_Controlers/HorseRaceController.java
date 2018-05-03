package Controlador.Game_Controlers;

import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Controlador.DraggableWindow;
import Network.NetworkManager;
import Vista.GameViews.HorseRaceView;
import Vista.MainFrame.Finestra;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class HorseRaceController implements GraphicsController, ActionListener {

    private GraphicsManager graphicsManager;
    private NetworkManager networkManager;
    private HorseRaceView horseRaceView;
    private Finestra finestra;



    public HorseRaceController(HorseRaceView horseRaceView, NetworkManager networkManager, DraggableWindow draggableWindow, Finestra finestra){
        this.horseRaceView = horseRaceView;
        this.networkManager = networkManager;
        //this.graphicsManager = new GraphicsManager(this.horseRaceView,this);
        this.finestra = finestra;




    }

    @Override
    public void init() {

    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(Graphics g) {
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

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case"HORSES-Back":
                System.out.println("yus");
                finestra.setGameSelector(false); //Un guest no podra jugar a cavalls
                networkManager.exitHorses();

        }
    }
}
