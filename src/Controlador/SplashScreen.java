package Controlador;

import Controlador.CustomGraphics.GraphicsController;
import Controlador.CustomGraphics.GraphicsManager;
import Model.AssetManager;
import Vista.SplashScreen.SplashScreenVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;


/** Pantalla inicial de carrega*/
public class SplashScreen implements GraphicsController, Runnable{

    /** Coordenades del inici i el final del pal */
    private int x,y,x1,y1;

    /** Temps en ms actual*/
    private long currentTime;

    /** Nombre total de lineas de l'animacio*/
    private int numberOfLines;

    /** Array amb els colors de cada barra*/
    private Color[] colors;

    /** GraphicsManager per gestionar el pintat de l'animacio*/
    private GraphicsManager backGround;

    /** Text informatiu de la loadingScreen*/
    private String info;
    /** Color del text informatiu*/
    private Color textColor;

    /** Array de les diferents amplades de cada barra*/
    private int stroke[];

    /** Vista on treballa el graphics manager*/
    private SplashScreenVista vista;

    /** Crea una splashScreen i carrega tots els assets*/
    public SplashScreen(){

        info = "Loading AssetManager...";
        textColor = Color.black;
        //Declarem el nombre de ralles de l'animacio d'inici
        numberOfLines = (int)(Math.random() * 20) + 15;

        //Es declaren els colors de les ralles
        colors = new Color[numberOfLines];
        stroke = new int[numberOfLines];

        //Es configura la vista
        vista = new SplashScreenVista(350,475);
        JPanel aux = new JPanel();
        aux.setSize(350,475);
        vista.getContentPane().add(aux);

        //Es crea el panell per pintar l'animacio i es configura per ocupar tot el JFrame
        backGround = new GraphicsManager(aux,this);
        backGround.setClearColor(Color.black);

        //Iniciem la carrega d'arxius del casino
        Thread loader = new Thread(this);
        loader.start();

        //S'espera a la finalitzacio del thread que carrega el contingut
        try {
            loader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Surt de la loading Screen*/
    public void exit(){
        backGround.exit();
        vista.dispose();
    }

    /** Formules per a calcular els 4 punts de les barres*/
    private double x2(float ti) {
        return Math.sin(ti / 20) + 100 * Math.cos(ti / 25);
    }

    /** Formules per a calcular els 4 punts de les barres*/
    private double y2(float t) {
        return 20 * Math.sin(t / 10)+ 20 * Math.cos(t / 10);
    }

    /** Formules per a calcular els 4 punts de les barres*/
    private double x1(float ti) {
        return 10 * Math.sin(ti / 80) + 10 * Math.cos(ti / 25);
    }

    /** Formules per a calcular els 4 punts de les barres*/
    private double y1(float t) {
        return 200 * Math.sin(t / 20)+ 10 * Math.cos(t / 60);
    }

    @Override
    public void init() {
        //Es calcula la posicio inicial
        x = (int)x1(0);
        x1 = (int)x2(0);
        y = (int)y1(0);
        y1 = (int)y2(0);

        int aux = 0;
        int colorSelecionado = (int)(Math.random() * 6);

        //Es crea la configuracio de colors
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
            //Es defineix el grosor de les linies de manera incremental
            stroke[i] = 2 + aux;

            if(i % 5 == 0)
                aux++;
        }
    }

    @Override
    public void update(float delta) {
        int speed = 350;
        //S'acturalitza l'instant actual d'acord amb el delta time
        currentTime = (long)(delta * System.nanoTime() / (float) (speed*1000));
    }

    @Override
    public void render(Graphics gb) {
        Graphics2D g = (Graphics2D) gb;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Es printa el text infomatiu
        g.setColor(textColor);
        g.setFont(new Font(g.getFont().getFontName(),Font.PLAIN,15));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        g.drawString(info,175 - metrics.stringWidth(info)/2,450);
        g.translate(175,220);
        g.setColor(Color.white);

        //Es pinten totes les linies amb el seu color i grosor personalitzats
        for(int i = 0; i < numberOfLines;i++){
            g.setColor(colors[i]);
            x = (int) x1(currentTime - i*100/numberOfLines);
            x1 = (int) x2(currentTime - i*100/numberOfLines);
            y = (int) y1(currentTime - i*100/numberOfLines);
            y1 = (int) y2(currentTime - i*100/numberOfLines);

            g.setStroke(new BasicStroke(stroke[i]));
            g.draw(new Line2D.Float(x1, y1,x,y));
        }
    }

    @Override
    public void run() {
        //El thread carrega tota la informacio amb l'ajuda del AssetManager
        AssetManager.loadData(this);
    }

    /**
     * Modifica el missatge informatiu
     * @param message nou missatge per mostrar
     */
    public void infoMessage(String message){
        this.info = message;
        this.textColor = Color.white;
    }

    /**
     * Modifica el missatge informatiu a missatge amb error
     * @param error nou missatge per mostrar amb error
     */
    public void showError(String error) {
        this.info = error;
        this.textColor = Color.red;
    }

    /**
     * Para la carrega de Assets i tenca el programa
     */
    public void stop() {
        backGround.exit();
        vista.dispose();
        System.out.println("Lectura dels assets incorrecte.");
        System.exit(0);
    }

    //Escalador de variables
    private float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
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
}
