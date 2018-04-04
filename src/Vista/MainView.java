package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * @deprecated
 */
public class MainView extends JFrame {

    private JButton logInButton;
    private JButton logOutButton;
    private JTextField jtfUsername;
    private JPasswordField jpfPassword;
    private JCheckBox jcbRememberLogIn;

    /**
     *  Crea la vista del client amb una amplada i una alçada determinades per width i height
     * @param width indica l'amplada de la vista
     * @param height indica l'altura de la vista
     */

    public MainView(int width,int height){

        Tray.init();

        JPanel aux = new JPanel();
        aux.setLayout(new BoxLayout(aux,BoxLayout.Y_AXIS));

        jtfUsername = new JTextField();
        jpfPassword = new JPasswordField();
        jpfPassword.setEchoChar('*');

        jcbRememberLogIn = new JCheckBox("Remember the account in this pc");
        jcbRememberLogIn.setSelected(false);

        logInButton = new JButton("LogIn");
        logOutButton = new JButton("LogOut");

        aux.add(jtfUsername);
        aux.add(jpfPassword);
        aux.add(jcbRememberLogIn);
        aux.add(logInButton);
        aux.add(logOutButton);

        getContentPane().add(aux);
        //Es determinen les dimensions de la finestra
        setSize(width,height);

        //Es centra la finestra en el centre de la pantalla
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //FULL SCREEN
        setTitle("Casino");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /** Afegeix el controlador del programa a la vista*/
    public void addController(Controller c){
        Tray.addController(c);

        //Tenen el mateix actionCommand perque les dues accions resulten en el mateix
        logInButton.setActionCommand("logIn");
        jpfPassword.setActionCommand("logIn");
        logInButton.addActionListener(c);
        jpfPassword.addActionListener(c);

        logOutButton.setActionCommand("logOut");
        logOutButton.addActionListener(c);

        jcbRememberLogIn.setActionCommand("rememberLogIn");
        jcbRememberLogIn.addActionListener(c);

        addWindowListener(c);
    }

    /** Obra una finestra indicant un error*/
    public void displayError(String title,String errorText) {
        JOptionPane.showMessageDialog(this,title,errorText,JOptionPane.ERROR_MESSAGE);
    }

    public boolean displayQuestion(String message) {
        //Retorna true si
        //Retorn false no
        return JOptionPane.showConfirmDialog(this,message,"Are you sure?",JOptionPane.YES_NO_OPTION) == 0;
    }

    public String getUsername() {
        return jtfUsername.getText();
    }

    public String getPassword() {
        return String.valueOf(jpfPassword.getPassword());
    }

    public boolean getRememberLogIn() {
        return jcbRememberLogIn.isSelected();
    }
}
