package Vista.GameViews.BlackJack;
import Model.AssetManager;

import javax.swing.*;

public class BlackJackView extends JPanel{

    public BlackJackView(){
        setFocusable(true);
        requestFocus();
    }

    public String showInputDialog() {
       return JOptionPane.showInputDialog(this,"How much money do you want to bet?\nThe minimum bet is 10€","Bet menu",JOptionPane.INFORMATION_MESSAGE);
    }

    public void showDialog(String title,String message) {
        JOptionPane.showMessageDialog(this,message,title,JOptionPane.ERROR_MESSAGE);
    }
}
