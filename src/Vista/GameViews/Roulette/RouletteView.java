package Vista.GameViews.Roulette;

import Controlador.Controller;
import Vista.View;

/**
 * Vista de la ruleta. Ja que aquesta no conté cap element
 * de Swing, practicament es troba buida, ja que a més a més,
 * en aquest panell, Graphics Manager i el Controlador general
 * són els qui s'encarreguen de vincular els graphics generats per
 * RouletteController en aquest JPanel.
 */
public class RouletteView extends View {

    /**
     * Mètode per a registrar el controlador de la vista
     * @param c Controlador a registrar
     */
    @Override
    public void addController(Controller c) {
        addComponentListener(c);
    }
}