package Vista.GraphicUtils.Animation;

import java.awt.*;

/**
 * Classe que representa un frame d'una animació, és a dir una
 * de les múltiples imatges que la componen.
 *
 * El frame es compon d'una imatge, i de la duració que té dins de l'animació.
 */
public class Frame {
	private Image image;
	private double duration;

	public Frame(Image image, double duration) {
		this.image = image;
		this.duration = duration;
	}

	public double getDuration() {
		return duration;
	}

	public void modDuration(float f) {
	    duration *= f;
    }

	public Image getImage() {
		return image;
	}
}