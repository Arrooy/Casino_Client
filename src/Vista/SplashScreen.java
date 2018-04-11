package Vista;

import Model.AssetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class SplashScreen extends JFrame implements ToDraw,Runnable{

    private int x,y,x1,y1;
    private long currentTime;
    private int numberOfLines;
    private Color[] colors;
    private GraphicsPanel backGround;
    private String info;
    private Color textColor;
    private int stroke[];
    public SplashScreen(){

        info = "Loading AssetManager...";
        textColor = Color.black;
        //Declarem el nombre de ralles de l'animacio d'inici
        numberOfLines = (int)(Math.random() * 20) + 15;

        //Es declaren els colors de les ralles
        colors = new Color[numberOfLines];
        stroke = new int[numberOfLines];

        //Es crea el panell per pintar l'animacio i es configura per ocupar tot el JFrame
        backGround = new GraphicsPanel(350,475);
        backGround.setBackgroundColor(Color.black);
        backGround.setCurrentDrawing(this,null);
        getContentPane().add(backGround);

        //Es configura el JFrame per apareixer un una sola animacio al centre de la screen
        setUndecorated(true);
        pack();
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
        setVisible(true);
        requestFocus();

        //Iniciem la carrega d'arxius del casino
        Thread loader = new Thread(this);
        loader.start();

        try {
            loader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void exit(){
        backGround.exit();
        dispose();
    }

    double x2(float ti) {
        return Math.sin(ti / 20) + 100 * Math.cos(ti / 25);
    }
    double y2(float t) {
        return 20 * Math.sin(t / 10)+ 20 * Math.cos(t / 10);
    }
    double x1(float ti) {
        return 10 * Math.sin(ti / 80) + 10 * Math.cos(ti / 25);
    }
    double y1(float t) {
        return 200 * Math.sin(t / 20)+ 10 * Math.cos(t / 60);
    }

    @Override
    public void init() {
        x = (int)x1(0);
        x1 = (int)x2(0);
        y = (int)y1(0);
        y1 = (int)y2(0);
        int aux = 0;
        int colorSelecionado = (int)(Math.random() * 6);

        for(int i = 0;i < numberOfLines;i++) {

            switch (colorSelecionado){
                case 0:
                    colors[i] = new Color((int)map(i,0,numberOfLines,0,200),0,0);
                    break;
                case 1:
                    colors[i] = new Color(0,(int)map(i,0,numberOfLines,0,200),0);
                    break;
                case 2:
                    colors[i] = new Color((int)map(i,0,numberOfLines,0,200),(int)map(i,0,numberOfLines,0,200),0);
                    break;
                case 3:
                    colors[i] = new Color(0,(int)map(i,0,numberOfLines,0,200),(int)map(i,0,numberOfLines,0,200));
                    break;
                case 4:
                    colors[i] = new Color((int)map(i,0,numberOfLines,0,200),(int)map(i,0,numberOfLines,0,200),(int)map(i,0,numberOfLines,0,200));
                    break;
                default:
                    colors[i] = new Color(0,0,(int)map(i,0,numberOfLines,0,200));
            }
            stroke[i] = 2 + aux;
            if(i % 5 == 0)
                aux++;
        }
    }

    @Override
    public void update(float delta) {
        int speed = 350;

        currentTime = (long)(delta * System.nanoTime() / (float) (speed*1000));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(textColor);
        g.setFont(new Font(g.getFont().getFontName(),Font.PLAIN,15));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.drawString(info,175 - metrics.stringWidth(info)/2,450);
        g.translate(175,220);
        g.setColor(Color.white);

        for(int i = 0; i < numberOfLines;i++){
            g.setColor(colors[i]);
            x = (int) x1(currentTime - i*100/numberOfLines);
            x1 = (int) x2(currentTime - i*100/numberOfLines);
            y = (int) y1(currentTime - i*100/numberOfLines);
            y1 = (int) y2(currentTime - i*100/numberOfLines);

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(stroke[i]));
            g2.draw(new Line2D.Float(x1, y1,x,y));
        }
    }

    @Override
    public void run() {
        AssetManager.loadData(this);
    }

    public void infoMessage(String message){
        this.info = message;
        this.textColor = Color.white;
    }

    public void showError(String error) {
        this.info = error;
        this.textColor = Color.red;
    }

    public void stop() {
        while(true){
            System.out.println("ERROR");
        }
    }
    public float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }
}
