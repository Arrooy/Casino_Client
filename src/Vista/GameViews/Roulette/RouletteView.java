package Vista.GameViews.Roulette;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsManager;
import Controlador.Game_Controlers.RouletteController;
import Vista.View;

import java.awt.*;

public class RouletteView extends View {

    private RouletteController rouletteController;
    private GraphicsManager graphicsManager;
    private Controller controller;

    public RouletteView() {
        setPreferredSize(new Dimension(600, 600));
    }

    @Override
    public void addController(Controller c) {
        addComponentListener(c);
        controller = c;
    }
}
