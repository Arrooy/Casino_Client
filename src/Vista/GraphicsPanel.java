package Vista;

import Controlador.GraphicsController;

import java.awt.*;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphicsPanel extends JPanel implements Runnable {
    private int width;
    private int height;
    private Image image;

    private Thread thread;
    private volatile boolean running;
    private volatile ToDraw currentDrawing;

    private GraphicsController controller;

    public GraphicsPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
    }

    public void setCurrentDrawing(ToDraw newState, GraphicsController controller) {
        System.gc();
        newState.init();
        currentDrawing = newState;
        this.controller = controller;
        registraControllador(controller);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        //registraControllador();
        //setCurrentDrawing(new DrawProva(), new Controlador.GraphicsController());
        initGame();
    }

    private void initGame() {
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
        // End game immediately when running becomes false.
        System.exit(0);
    }

    private void updateAndRender(long deltaMillis) {
        currentDrawing.update(deltaMillis / 1000f);
        prepareGameImage();
        currentDrawing.render(image.getGraphics());
        renderGameImage(getGraphics());
    }

    private void prepareGameImage() {
        if (image == null) {
            image = createImage(width, height);
        }
        Graphics g = image.getGraphics();
        g.clearRect(0, 0, width, height);
    }

    public void exit() {
        running = false;
    }

    private void renderGameImage(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
        g.dispose();
    }

    private void registraControllador(GraphicsController c) {
        addKeyListener(c);
        addMouseListener(c);
    }
}