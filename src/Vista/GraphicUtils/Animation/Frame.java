package Vista.GraphicUtils.Animation;

import java.awt.*;

/**
 * Classe que representa un frame d'una animació, és a dir una
 * de les múltiples imatges que la componen.
 *
 * El frame es compon d'una imatge, i de la duració que té dins de l'animació.
 */
public class Frame {

	/** Imatge del frame */
	private Image image;

	/** Duració en l'animació */
	private double duration;

    /**
     * Constructor de la classe
     * @param image Imatge del frame
     * @param duration Duració en l'animació
     */
	public Frame(Image image, double duration) {
		this.image = image;
		this.duration = duration;
	}

	/** Getter de la duració */
	public double getDuration() {
		return duration;
	}

	/** En cas de volguer escalar la velocitat, aquest mètode
     * retorna la duració escalada */
	public void modDuration(float f) {
	    duration *= f;
    }

    /** Getter de la Imatge */
	public Image getImage() {
		return image;
	}
}