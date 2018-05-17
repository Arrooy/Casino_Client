package Vista.GameViews.BlackJack;

import Vista.PanelWithBackGround;
import Vista.SwingModifications.IconTextField;

import javax.swing.*;
import java.awt.*;

/** Vista principal del blackJack. Esta directament modificada per el graphicsManager del blackJackController*/
public class BlackJackView extends JPanel{

    /** Inicia la vista del joc BlackJack*/
    public BlackJackView(){
        setFocusable(true);
        requestFocus();
    }

    /**
     * Mostra el dialeg per a introduir l'aposta de la partida
     * @return valor que ha proposat l'usuari per apostar
     */

    public String showInputDialog() {
        Object[] options1 = {"Place Bet", "Return game menu"};

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        panel.add(new JLabel("How much money do you want to bet?"));
        panel.add(Box.createVerticalStrut(10));

        JLabel aux = new JLabel("Remember the minimum bet is 10€");
        aux.setForeground(Color.red);

        panel.add(aux);
        panel.add(Box.createVerticalStrut(10));

        IconTextField iconTextField = new IconTextField("money.png","","The money you're about to bet");
        panel.add(iconTextField);

        boolean succes = JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(this, panel, "BlackJack bet menu",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options1, null);

        //El caracter al inici del return indica l'opcio elegida per l'usuari
        if(succes){
            return "a" + iconTextField.getText();
        }else{
            return "b" + iconTextField.getText();
        }
    }

    /**
     * Mostra un error al usuari
     * @param title titol del error a mostrar
     * @param message missatge del error a mostrar
     */
    public void showDialog(String title,String message) {
        JOptionPane.showMessageDialog(null,message,title,JOptionPane.ERROR_MESSAGE);
    }
}
