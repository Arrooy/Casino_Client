package Controlador.CustomGraphics;

import java.awt.*;
import javax.swing.*;

/**
 * TODO: Comentar degudament
 * TODO: Passar a package Vista (Recomanació de @edetorres)
 */
@SuppressWarnings("serial")
public class GraphicsManager implements Runnable {

    private Thread thread;

    private boolean running;

    private Image image;

    /** Panell que sera pintat*/
    private JPanel JPanelObjectiu;

    private GraphicsController controlador_extern;

    /** Color que s'estableix de fons cada nou frame*/
    private Color clearColor;

    private Image clearImage;

    private int width, height;

    /**
     * Crea un gestor per a controlar els grafics custom d'un jpanel extern.
     *
     * @param PanellObjectiu Panell on s'enganxará l'imatge resultant del update i render. MOLT IMPORTANT QUE EL PANELL TINGUI MIDA!
     * @param c              Controlador que gestiona les interaccions(Mouse&Key listeners) de la persona amb el custom rendering panel.
     */

    public GraphicsManager(JPanel PanellObjectiu, GraphicsController c) {
        clearColor = Color.white;

        if (PanellObjectiu.getWidth() == 0 || PanellObjectiu.getHeight() == 0)
            System.out.println("Error ultrafatal. El panell que mhas donat no te mida especificada!"); //ets un primo arroyo
        JPanelObjectiu = PanellObjectiu;
        JPanelObjectiu.setBackground(Color.white);
        JPanelObjectiu.setFocusable(true);
        JPanelObjectiu.requestFocus();
        registraControllador(c);
        controlador_extern = c;

        width = 0;
        height = 0;

        initGame();
    }

    /**
     * Modifica el color del fons al borrar el contingut cada frame
     */
    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }

    public void resize(int width, int height) {
        image = JPanelObjectiu.createImage(width, height);
        JPanelObjectiu.updateUI();
    }

    private void initGame() {
        controlador_extern.init();
        running = true;

        thread = new Thread(this, "Game Thread");
        thread.start();
        JPanelObjectiu.requestFocus();
    }

    @Override
    public void run() {
        // These variables should sum up to 17 on every iteration
        long updateDurationMillis = 0; // Measures both update AND render
        long sleepDurationMillis = 0; // Measures sleep
        while (running) {
            long beforeUpdateRender = System.nanoTime();
            long deltaMillis = updateDurationMillis + sleepDurationMillis;

            JPanelObjectiu.requestFocus();
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
        int w = width <= 0 ? JPanelObjectiu.getWidth() : width;
        int h = height <= 0 ? JPanelObjectiu.getHeight() : height;
        if (image == null) {
            image = JPanelObjectiu.createImage(JPanelObjectiu.getWidth(), JPanelObjectiu.getHeight());
        }
        if (image.getWidth(null) != width || image.getHeight(null) != height) {
            image = JPanelObjectiu.createImage(w, h);
        }

        if (image != null) {
            Graphics g = image.getGraphics();
            g.setColor(clearColor);
            g.fillRect(0, 0, JPanelObjectiu.getWidth(), JPanelObjectiu.getHeight());
            g.fillRect(0, 0, width, height);//JPanelObjectiu.getWidth(), JPanelObjectiu.getHeight());
        }
        //TODO:Revisar
        if (clearImage == null) {
            image.getGraphics().setColor(clearColor);
            image.getGraphics().fillRect(0, 0, JPanelObjectiu.getWidth(), JPanelObjectiu.getHeight());
        }
    }

    public void exit() {
        running = false;
        Graphics g = image.getGraphics();
        g.clearRect(0, 0, width, height);
    }

    private void renderGameImage(Graphics g1) {
        if (image != null && g1 != null) {

            Graphics2D g = (Graphics2D)g1;

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g.drawImage(image, 0, 0, null);
        }

        g1.dispose();
    }

    private void registraControllador(GraphicsController c) {

        if (JPanelObjectiu.getMouseListeners().length == 0)
            JPanelObjectiu.addMouseListener(c);

        if (JPanelObjectiu.getMouseMotionListeners().length == 0)
            JPanelObjectiu.addMouseMotionListener(c);

        if (JPanelObjectiu.getKeyListeners().length == 0)
            JPanelObjectiu.addKeyListener(c);
    }
    public void requestFocus(){
        JPanelObjectiu.requestFocus();
    }

    public void updateSize(int width, int height, boolean fully){
        this.width = width;
        this.height = height;
        JPanelObjectiu.setPreferredSize(new Dimension(width, height));
        if(fully)
            image = JPanelObjectiu.createImage(width, height);
    }
}