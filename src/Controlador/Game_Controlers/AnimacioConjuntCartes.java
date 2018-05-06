package Controlador.Game_Controlers;

import Model.AssetManager;
import Model.Baralla;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimacioConjuntCartes {

    private ArrayList<Carta> boles;
    private int width,height;

    public AnimacioConjuntCartes(int numberBalls, int posX, int posY, int Mass, double PerduaTerra, int ScreenWidth, int ScreenHeight) {
        boles = new ArrayList<>();
        width = ScreenWidth;
        height = ScreenHeight;
        for (int i = 0; i < numberBalls; i++) {
            boles.add(new Carta(posX, posY, Mass, PerduaTerra,ScreenWidth,ScreenHeight));
        }
    }

    public void updateCards( int ScreenWidth, int ScreenHeight) {
        for (int i = boles.size() - 1; i >= 0; i--) {

            if(width != ScreenWidth || height != ScreenHeight){
                boles.get(i).setMargins(ScreenWidth, ScreenHeight);
                width = ScreenWidth;
                height = ScreenHeight;
            }

            colide(i);
            boles.get(i).update();
        }
    }
    public void displayCards(Graphics g) {
        g.drawImage(AssetManager.getImage("BJbackground.png"),0,0,width,height,null);
        for (int i = boles.size() - 1; i >= 0; i--) {
            boles.get(i).display(g);
        }
    }

    public void add(int posX, int posY, int Mass, double PerduaTerra, int ScreenWidth, int ScreenHeight) {
        Carta B =  new Carta(posX, posY, Mass, PerduaTerra,ScreenWidth,ScreenHeight);
        boles.add(B);
    }

    public void colide(int i) {
        double x = boles.get(i).Pos[0];
        double y = boles.get(i).Pos[1];
        double spring = 0.35;
        for (int j = i; j >= 0; j--) {
            double dx = boles.get(j).Pos[0] - x;
            double dy = boles.get(j).Pos[1] - y;
            double distance = Math.sqrt(dx*dx + dy*dy);
            double minDist = (boles.get(i).size)/2  +(boles.get(j).size)/2;
            if (distance < minDist) {
                double angle = Math.atan2(dy, dx);
                double targetX = x + Math.cos(angle) * minDist;
                double targetY = y + Math.sin(angle) * minDist;
                double ax = (targetX - boles.get(j).Pos[0]) * spring;
                double ay = (targetY - boles.get(j).Pos[1]) * spring;
                boles.get(i).Vel[0] -= ax;
                boles.get(i).Vel[1] -= ay;
                boles.get(j).Vel[0] += ax;
                boles.get(j).Vel[1] += ay;
            }
        }
    }

    public class Carta {

        private double[] Acc;
        private double[] Vel;
        private double[] Pos;

        private double[] Ftot;

        ArrayList<double[]> Forces;

        private int massa;

        private int size;

        private double PerduaTerra = 0;

        private int width;
        private int height;

        private int sizeHeight;
        private int sizeWidth;

        private BufferedImage cartaIMG;

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

        private void display(Graphics g) {
            g.drawImage(cartaIMG,(int)Pos[0],(int)Pos[1],sizeWidth,sizeHeight,null);
        }

        private void update() {
            calculateSUMF();
            UpdateAVP();
            checkMargins();
        }

        private void calculateSUMF() {
            Ftot[0] = 0;
            Ftot[1] = 0;
            for (double[] f : Forces) {
                Ftot[0] += f[0];
                Ftot[1] += f[1];
            }
            ResumeForces();
        }

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

        private void UpdateAVP() {
            Acc[0] = Ftot[0] / massa;
            Acc[1] = Ftot[1] / massa;
            Vel[0] += Acc[0];
            Vel[1] += Acc[1];
            Pos[0] += Vel[0];
            Pos[1] += Vel[1];
        }

        private void addForce(double newtonsX, double newtonsY) {
            double[] force = new double[2];
            force[0] = newtonsX;
            force[1] = newtonsY;
            Forces.add(force);
        }

        private void addGravity() {

            addForce(0, 0.981f * massa);


        }

        private void ResumeForces() {
            if (Forces.size() > 100) {
                deleteForces();
                addForce(Ftot[0], Ftot[1]);
            }
        }

        private void deleteForces() {
            for (int i = Forces.size()-1; i >= 0; i--) {
                Forces.remove(0);
            }
        }

        private void setMargins(int screenWidth, int screenHeight) {
            width = screenWidth;
            height = screenHeight;
        }
    }
}