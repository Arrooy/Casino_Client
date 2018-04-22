package Controlador.CustomGraphics;

import Controlador.DraggableWindow;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GraphicsManager implements Runnable {

    private Thread thread;

    private boolean running;
    private Image image;

    private JPanel JPanelObjectiu;

    private GraphicsController controlador_extern;

    private Color clearColor;

    /**
     * Crea un gestor per a controlar els grafics custom d'un jpanel extern.
     *
     * @param PanellObjectiu Panell on s'enganxará l'imatge resultant del update i render. MOLT IMPORTANT QUE EL PANELL TINGUI MIDA!
     * @param c Controlador que gestiona les interaccions(Mouse&Key listeners) de la persona amb el custom rendering panel.
     */

    public GraphicsManager(JPanel PanellObjectiu, GraphicsController c, DraggableWindow dw) {
        clearColor = Color.white;

        if(PanellObjectiu.getWidth() == 0 || PanellObjectiu.getHeight() == 0)
            System.out.println("Error ultrafatal. El panell que mhas donat no te mida especificada!"); //ets un primo arroyo
        JPanelObjectiu = PanellObjectiu;
        JPanelObjectiu.setBackground(Color.white);
        JPanelObjectiu.setFocusable(true);
        JPanelObjectiu.requestFocus();
        registraControllador(c,dw);
        controlador_extern = c;

        initGame();
    }

    /** Modifica el color del fons al borrar el contingut cada frame*/
    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }

    public void resize(int width, int height) {
        image = JPanelObjectiu.createImage(width,height);
        JPanelObjectiu.updateUI();
    }
    private void initGame() {
        controlador_extern.init();
        running = true;
        thread = new Thread(this, "Game Thread");
        thread.start();
    }

    @Override
    public void run() {
        // These variables should sum up to 17 on every iteration
        long updateDurationMillis = 0; // Measures both update AND render
        long sleepDurationMillis = 0; // Measures sleep
        while (running) {
            long beforeUpdateRender = System.nanoTime();
            long deltaMillis = updateDurationMillis + sleepDurationMillis;

            updateAndRender(deltaMillis);

            updateDurationMillis = (System.nanoTime() - beforeUpdateRender) / 1000000L;
            sleepDurationMillis = Math.max(2, 17 - updateDurationMillis);

            try {
                Thread.sleep(sleepDurationMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateAndRender(long deltaMillis) {
        controlador_extern.update(deltaMillis / 1000f);
        prepareGameImage();
        controlador_extern.render(image.getGraphics());
        renderGameImage(JPanelObjectiu.getGraphics());
    }

    private void prepareGameImage() {
        if(image == null){
            image = JPanelObjectiu.createImage(JPanelObjectiu.getWidth(), JPanelObjectiu.getHeight());
            //System.out.println(image);
        }

        if (image != null) {
            Graphics g = image.getGraphics();
            g.setColor(clearColor);
            g.fillRect(0, 0, JPanelObjectiu.getWidth(), JPanelObjectiu.getHeight());
        }
    }

    public void exit() {
        running = false;
    }

    private void renderGameImage(Graphics g) {
        if (image != null) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g.drawImage(image, 0, 0, null);
        }
        g.dispose();
    }

    private void registraControllador(GraphicsController c, DraggableWindow dw) {

        if (JPanelObjectiu.getMouseListeners().length == 0)
            JPanelObjectiu.addMouseListener(c);

        if (JPanelObjectiu.getMouseMotionListeners().length == 0)
            JPanelObjectiu.addMouseMotionListener(c);



        if (JPanelObjectiu.getKeyListeners().length == 0)
            JPanelObjectiu.addKeyListener(c);
    }

}