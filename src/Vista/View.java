package Vista;

import Controlador.Controller;

import javax.swing.*;

/**Classe abstracta creada per tal que totes les vistes hagin d'implementar el metode addController*/
public abstract class View extends JPanel{
    /**
     * Metode que enllaça els diferents elements de la vista que realitzen accions amb el seu Listener
     * Enllaça la vista amb el controlador
     * @param c Controlador del joc
     */
    public abstract void addController(Controller c);
}
