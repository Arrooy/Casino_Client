package Controlador.Game_Controlers;

import Utils.AssetManager;
import Utils.Baralla;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *  Aquesta classe serveix per a generar animacions realistes de imatges.
 *  Va crear-se en el seu moment per millorar l'estetica i el manegament de les cartes del BlackJack, pero
 *  per falta de temps, s'ha quedat en una classe que genera la mateixa animacio al inici de cada partida
 */

public class AnimacioConjuntCartes {

    /** Conjunt de cartes que es volen animar*/
    private ArrayList<Carta> cartes;
    /** Tamany del JPanel on apareix L'animacio*/
    private int width,height;


    /**
     * Crea una nova animacio amb numberOfCards inicials, que apareixen en la posicio (posX,posY).
     * Aquestes cartes que es crean tenen una massa anomenada mass i cada cop que rebotin amb el terra perden velocitat
     * en funcio de Perdua Terra.
     * @param numberOfCards nombre de cartes que apareixen al inici
     * @param posX Posicio inicial x de totes les noves cartes
     * @param posY Posicio inicial y de totes les noves cartes
     * @param Mass Massa de totes les noves cartes
     * @param PerduaTerra percentatge de velocitat que es conserva al finalitzar un rebot amb el terra
     * @param ScreenWidth Tamany horitzontal del JPanel on apareix l'animacio al crear-se
     * @param ScreenHeight Tamany vertical del JPanel on apareix l'animacio al crear-se
     */

    public AnimacioConjuntCartes(int numberOfCards, int posX, int posY, int Mass, double PerduaTerra, int ScreenWidth, int ScreenHeight) {
        cartes = new ArrayList<>();
        width = ScreenWidth;
        height = ScreenHeight;
        for (int i = 0; i < numberOfCards; i++) {
            cartes.add(new Carta(posX, posY, Mass, PerduaTerra,ScreenWidth,ScreenHeight));
        }
    }

    /**
     * Actualitza la posicio de totes les cartes, redefineix el marges del JPanel si aquest ha fet resize i
     * Gestiona la colisio entre cartes
     * @param ScreenWidth Amplada actual del JPanel
     * @param ScreenHeight Altura actual del JPanel
     */

    public void updateCards( int ScreenWidth, int ScreenHeight) {
        for (int i = cartes.size() - 1; i >= 0; i--) {

            //Si s'ha modificat la mida guardada del JPanel, s'actualitza
            if(width != ScreenWidth || height != ScreenHeight){
                cartes.get(i).setMargins(ScreenWidth, ScreenHeight);
            }
            //Es gestiona la coolisio
            colide(i);
            //S'actualitza la posicio de les cartes en el JPanel
            cartes.get(i).update();
        }

        width = ScreenWidth;
        height = ScreenHeight;
    }

    /**
     * Actualitza els graphics del panell objectiu on apareixera l'animacio
     * @param g graphics del panell
     */

    public void displayCards(Graphics g) {
        g.drawImage(AssetManager.getImage("BJbackground.png"),0,0,width,height,null);

        //S'actualitza cada carta per separat
        for (int i = cartes.size() - 1; i >= 0; i--) {
            cartes.get(i).display(g);
        }
    }



    /**
     * Crea una nova carta que apareixe en la posicio (posX,posY).
     * Aquesta carta te una massa anomenada mass i cada cop que reboti amb el terra perd velocitat
     * en funcio de Perdua Terra.
     * @param posX Posicio inicial x de la nova carta
     * @param posY Posicio inicial y de la nova carta
     * @param Mass Massa de la carta
     * @param PerduaTerra percentatge de velocitat que es conserva al finalitzar un rebot amb el terra
     * @param ScreenWidth Tamany horitzontal del JPanel on apareix l'animacio al crear-se
     * @param ScreenHeight Tamany vertical del JPanel on apareix l'animacio al crear-se
     */

    public void add(int posX, int posY, int Mass, double PerduaTerra, int ScreenWidth, int ScreenHeight) {
        Carta B =  new Carta(posX, posY, Mass, PerduaTerra,ScreenWidth,ScreenHeight);
        cartes.add(B);
    }

    /**
     * Controla la colisio de totes les cartes de l'animacio.
     * La colisio per a simplificar-se es fa interpretant les cartes com cercles.
     * @param i index de la carta que s'ha de revisar per si esta colisionant amb algu
     */

    public void colide(int i) {
        double x = cartes.get(i).Pos[0];
        double y = cartes.get(i).Pos[1];
        double spring = 0.35;
        for (int j = i; j >= 0; j--) {
            double dx = cartes.get(j).Pos[0] - x;
            double dy = cartes.get(j).Pos[1] - y;
            double distance = Math.sqrt(dx*dx + dy*dy);
            double minDist = (cartes.get(i).size)/2  +(cartes.get(j).size)/2;
            if (distance < minDist) {
                double angle = Math.atan2(dy, dx);
                double targetX = x + Math.cos(angle) * minDist;
                double targetY = y + Math.sin(angle) * minDist;
                double ax = (targetX - cartes.get(j).Pos[0]) * spring;
                double ay = (targetY - cartes.get(j).Pos[1]) * spring;
                cartes.get(i).Vel[0] -= ax;
                cartes.get(i).Vel[1] -= ay;
                cartes.get(j).Vel[0] += ax;
                cartes.get(j).Vel[1] += ay;
            }
        }
    }

    /**
     * Carta es l'objecte que AnimacioConjuntCartes controla. Es tracta d'una esfera que en tot moment actualitza
     * la seva posicio basant-se amb el conjunt de forces que se li apliquen. De les forces es troba l'acceleracio,
     * la velocitat i la posicio de la "carta".
     * Aquesta esfera es veu representada com una imatge d'una carta, pero tota la logica s'aplica com si la carta fos
     * un cercle amb un radi size i una massa anomenada massa.
     * Nota: tots els arrays estatics son de 2 posicions. La x figura a la posicio 0 i la y es troba a la posicio 1
     */

    private class Carta {

        /** Acceleracio que te la carta despres d'haver cridat update*/
        private double[] Acc;
        /** Velocitat que te la carta despres d'haver cridat update*/
        private double[] Vel;
        /**Posicio que te la carta despres d'haver cridat update*/
        private double[] Pos;


        /** Resum de totes les forces que se li apliquen a la carta. Serveix perque l'ArryList forces no es saturi*/
        private double[] Ftot;

        /** Llista de totes les forces que s'apliquen a la carta*/
        private ArrayList<double[]> Forces;

        /** Massa de la carta*/
        private int massa;

        /** Radi de la carta*/
        private int size;

        /** Percentatge de la velocitat de la carta resultant despres de colisionar amb el terra*/
        private double PerduaTerra;

        /** Amplada del JPanel que conte l'animacio*/
        private int width;

        /** Altura del JPanel que conte l'animacio*/
        private int height;

        /** Altura de la carta*/
        private int sizeHeight;

        /** Amplada de la carta*/
        private int sizeWidth;

        /** Imatge de la carta*/
        private BufferedImage cartaIMG;


        /**
         * Crea una nova carta i l'afegeix a la posicio PosX,PosY del JPanel que conte l'animacio.
         * @param posX Posicio inicial x de la Carta
         * @param posY Posicio inicial y de la Carta
         * @param Mass Massa de la carta
         * @param PerduaTerra Percentatge de la velocitat de la carta resultant despres de colisionar amb el terra
         * @param ScreenWidth Amplada del JPanel
         * @param ScreenHeight Altura del JPanel
         */
        private Carta(int posX, int posY, int Mass, double PerduaTerra,int ScreenWidth,int ScreenHeight) {
            Acc = new double[2];
            Vel = new double[2];
            Pos = new double[2];

            Vel[0] = 9;

            Pos[0] = posX;
            Pos[1] = posY;

            Ftot = new double[2];
            Forces = new ArrayList<>();

            height = ScreenHeight;
            width = ScreenWidth;

            massa = Mass;
            addGravity();
            cartaIMG = Baralla.getRandomCard();

            sizeWidth = cartaIMG.getWidth();
            sizeHeight = cartaIMG.getHeight();

            size = 40;

            this.PerduaTerra = PerduaTerra;

        }

        /** Pinta l'imatge assignada al contruir la carta als graphics del panell de l'animacio*/
        private void display(Graphics g) {
            g.drawImage(cartaIMG,(int)Pos[0],(int)Pos[1],sizeWidth,sizeHeight,null);
        }

        /** Actualitza la posicio de la carta i rebota si es colisiona amb els marges de la pantalla*/
        private void update() {
            calculateSUMF();
            UpdateAVP();
            checkMargins();
        }

        /** Simplifica la llista de forces per a millorar el rendiment de l'animacio*/
        private void calculateSUMF() {
            Ftot[0] = 0;
            Ftot[1] = 0;
            for (double[] f : Forces) {
                Ftot[0] += f[0];
                Ftot[1] += f[1];
            }
            ResumeForces();
        }

        /**
         * Verifica si la posicio de la carta ha sobrepassat els marges de JPanel.
         * En cas afirmatiu, obliga a la carta a rebotar en direccio contraria amortiguant la velocitat.
         */
        private void checkMargins() {

            if (this.Pos[0] > width - sizeWidth) {
                this.Pos[0] = width - sizeWidth;
                Vel[0] *= PerduaTerra;
                Vel[1] *= PerduaTerra;
                this.Vel[0] *= -1;
            }

            if (this.Pos[0] < 0) {
                this.Pos[0] = 0;
                Vel[0] *= PerduaTerra;
                Vel[1] *= PerduaTerra;
                this.Vel[0] *= -1;
            }

            if (this.Pos[1] > height - sizeHeight) {
                this.Pos[1] = height - sizeHeight;
                Vel[0] *= PerduaTerra;
                Vel[1] *= PerduaTerra;
                this.Vel[1] *= -1;
            }

            if (this.Pos[1] < 0) {
                this.Pos[1] = 0;
                Vel[0] *= PerduaTerra;
                Vel[1] *= PerduaTerra;
                this.Vel[1] *= -1;
            }
        }

        /** Actualitza la acceleracio velocitat i posicio de la carta*/
        private void UpdateAVP() {
            Acc[0] = Ftot[0] / massa;
            Acc[1] = Ftot[1] / massa;
            Vel[0] += Acc[0];
            Vel[1] += Acc[1];
            Pos[0] += Vel[0];
            Pos[1] += Vel[1];
        }

        /**
         * Permet afegir una força a la carta.
         * @param newtonsX cantitat de força en l'eix x expresada en newtowns
         * @param newtonsY cantitat de força en l'eix y expresada en newtowns
         */
        private void addForce(double newtonsX, double newtonsY) {
            double[] force = new double[2];
            force[0] = newtonsX;
            force[1] = newtonsY;
            Forces.add(force);
        }

        /**Afegeix 1/10 cops la gravetat de la terra*/
        private void addGravity() {
            addForce(0, 0.981f * massa);
        }

        /** Si la llista de forces es molt llarga, es simplifica*/
        private void ResumeForces() {
            if (Forces.size() > 100) {
                deleteForces();
                addForce(Ftot[0], Ftot[1]);
            }
        }

        /** Elimina les forces de la llista*/
        private void deleteForces() {
            Forces.clear();
        }

        /**
         * Permet redefinir el tamany del JPanel objectiu
         * @param screenWidth nova amplada del JPanel
         * @param screenHeight nova altura del JPanel
         */
        private void setMargins(int screenWidth, int screenHeight) {
            width = screenWidth;
            height = screenHeight;
        }
    }
}