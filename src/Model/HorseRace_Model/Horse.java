package Model.HorseRace_Model;

import Model.AssetManager;
import Vista.GraphicUtils.Animation.Animation;
import Vista.GraphicUtils.Animation.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Horse {
    private Point position;
    private BufferedImage[] images;
    private int horse_id;
    private boolean paint;
    private Animation animation;
    private static int horseWidth = 0;
    private static int horseHeigth = 0;


    private static final double BASE_SPEED = 0.1;



    public Horse (int id, Point position, int horseWidth, int horseHeigth, boolean paint){
        this.horse_id = id;
        this.horseWidth = horseWidth;
        this.horseHeigth = horseHeigth;
        this.position = position;
        this.paint = paint;

        Frame[] frames = new Frame[7];

        for(int i = 1; i < 7; i++){
            frames[i - 1] = new Frame(AssetManager.getImage("horses" + id%12 + "_" + i),BASE_SPEED );
        }
        animation = new Animation(frames);
    }


    public void horseStart(boolean paint, Point position){
        this.paint = paint;
        this.position = position;
    }






}
