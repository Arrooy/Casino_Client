package Vista.GraphicUtils.Animation;

import java.awt.*;

/**
 * Classe per generar una animació. Laanimacióes composa de frames,
 * cada frame es compon d'una imatge que es pot extreure de la classe
 * AssetManager.
 * L'ús de la classe es basa en:
 * 1- Generar la animació: Inicialitzar prèviament els Frames i seguidament
 *      cridar el constructor col·locant tots els frames a usar com a paràmetres
 *      i en ordre. Cal indicar la duracio del frame en la seva construcció. (Veure Nota)
 * 2- Update: la funció update va incrementant el comptador intern de la classe.
 *      La funcio s'encarrega d'anar avançant i cambiant les imatges de manera automàtica.
 * 3- Render: Simplement renderitza la imatge actual que pertoca de la animació,
 *      a les coordenades especificades.
 *
 * Nota: s'ha de tenir en compte que la duració indicada al Frame és relativa. Tot depén del
 *      increment indicat a la funció update. En cas de indicar que increment és igual a delta
 *      (0.017 normalment), la duració indicada en el frame correspondrà directament al temps
 *      real de durada del frame.
 */
public class Animation {
	private Frame[] frames;
	private double[] frameEndTimes;
	private int currentFrameIndex = 0;

	private double totalDuration = 0;
	private double currentTime = 0;
	
	private boolean finished;
	private int reps;

    /**
     * Constructor de la classe que genera l'estructura de l'animació
     * @param frames Conjunt de Frames que componen la animació
     */
	public Animation(Frame... frames) {
		this.frames = frames;
		frameEndTimes = new double[frames.length];
		
		finished = false;
		reps = 0;

		for (int i = 0; i < frames.length; i++) {
			Frame f = frames[i];
			totalDuration += f.getDuration();
			frameEndTimes[i] = totalDuration;
		}
	}

    /**
     * Mètode que actualitza l'estat de l'animació
     * @param increment Valor pel que incrementa el temporitzador de la animació
     */
	public synchronized void update(float increment) {
		currentTime += increment;

		if (currentTime > totalDuration) {
			wrapAnimation();
			reps++;
		}

		while (currentTime > frameEndTimes[currentFrameIndex]) {
			currentFrameIndex++;
		}
		
		if (reps >= 1) finished = true;
	}

	private synchronized void wrapAnimation() {
		currentFrameIndex = 0;
		currentTime %= totalDuration; // equal to cT = cT % tD
	}

    /**
     * Mètode que renderitza la imatge corresponent de la animació.
     * @param g Component de gràfics en el que es pinta la imatge
     * @param x Coordenada x on es pinta la imatge
     * @param y Coordenada y on es pinta la imatge
     */
	public synchronized void render(Graphics g, int x, int y) {
		g.drawImage(frames[currentFrameIndex].getImage(), x, y, null);
	}

    /**
     * Mètode que renderitza la imatge corresponent de la animació.
     * @param g Component de gràfics en el que es pinta la imatge
     * @param x Coordenada x on es pinta la imatge
     * @param y Coordenada y on es pinta la imatge
     * @param width Amplada de la imatge (Resize)
     * @param height Alçada de la imatge (Resize)
     */
	public synchronized void render(Graphics g, int x, int y, int width, int height) {
		g.drawImage(frames[currentFrameIndex].getImage(), x, y, width, height, null);
	}

    /**
     * Mètode per modificar la duració de la animació.
     * Simplement multiplica la duració actual de cada frame pel
     * factor indicat.
     * @param f Factor de increment o decrement de l'animació
     */
	public synchronized void modSpeedBy(float f) {
	    totalDuration = 0;
	    for (int i = 0; i < frames.length; i++) {
	        frames[i].modDuration(f);
            totalDuration += frames[i].getDuration();
            frameEndTimes[i] = totalDuration;
        }
    }

    /**@deprecated no dona bones vibracions*/
	public void restart(){
		reps = 0;
		finished = false;
		currentFrameIndex = 0;
		totalDuration = 0;
		currentTime = 0;
	}
	
	public boolean finished() {return finished;}

}