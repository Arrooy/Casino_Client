package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public abstract class View extends JPanel{
    public abstract void addController(Controller c);
}
