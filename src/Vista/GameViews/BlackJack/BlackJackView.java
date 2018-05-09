package Vista.GameViews.BlackJack;

import javax.swing.*;

/** Vista principal del blackJack. Esta directament modificada per el graphicsManager del blackJackController*/
public class BlackJackView extends JPanel{

    /** Inicia la vista del joc BlackJack*/
    public BlackJackView(){
        setFocusable(true);
        requestFocus();
    }

    /***
     * Mostra el dialeg per a introduir l'aposta de la partida
     * @return valor que ha proposat l'usuari per apostar
     */
    public String showInputDialog() {
       return JOptionPane.showInputDialog(null,"How much money do you want to bet?\nThe minimum bet is 10€","Bet menu",JOptionPane.INFORMATION_MESSAGE);
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
