package Vista;

import Controlador.Controller;
import Vista.SwingModifications.IconPasswordField;
import Vista.SwingModifications.IconTextField;

import javax.swing.*;
import java.awt.*;

public class LogInView extends View {

    //TODO: FINISH THIS
    private final static String TOOLTIP_USERNAME_LOGIN = "Username field";
    private final static String TOOLTIP_PASSWORD_LOGIN = "Password field";

    private static final String USERNAME_HINT = "Username";
    private static final String PASSWORD_HINT = "Password";

    private final static char PASSWORD_CHAR = '*';

    private final static Color GRANA = new Color(125, 28, 37);
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    private JButton jbAccept;
    private IconTextField jtfUsername;
    private IconPasswordField jpfPassword;
    private JCheckBox jcbRememberLogIn;
    private JButton jbBack;

    private JLabel jlErrorMessage;

    /**
     * TODO: acabar de colocar el missatge d'error
     */
    public LogInView(){
        this.setLayout(new BorderLayout());

        //Label missatge error
        JPanel jpGeneric = new JPanel(new GridBagLayout());

        jlErrorMessage = new JLabel("Error");
        jlErrorMessage.setHorizontalAlignment(JLabel.CENTER);

        //Panell per col·locar el botó back a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        jbBack = new JButton("BACK");
        jbBack.setFocusable(false);
        jpgblBack.add(jbBack, c);
        //Flow Layout per a que el botó quedi a l'esquerra
        JPanel jpBack = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jpBack.add(jpgblBack);
        this.add(jpBack, BorderLayout.SOUTH);

        //Panell que té el títol de la pantalla a dalt a la dreta al mig
        JPanel jpTitle = new JPanel();
        JPanel jpgblTitle = new JPanel(new GridBagLayout());
        JLabel jlTitle = new JLabel("Log In");
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 100));

        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els camps d'UserName, Password i l'opció de Remember me centrats al mig de la pantalla
        JPanel jpgblInfo = new JPanel(new GridBagLayout());
        jcbRememberLogIn = new JCheckBox("Remember user");
        jcbRememberLogIn.setFocusable(false);

        //S'afegeixen les etiquetes
        c.gridy = 3;
        c.gridx = 0;
        c.insets = new Insets(0,0,0,0);
        jpgblInfo.add(jcbRememberLogIn, c);

        //S'afegeixen els camps per omplir
        jtfUsername = new IconTextField("user.png",USERNAME_HINT,TOOLTIP_USERNAME_LOGIN);
        jpfPassword = new IconPasswordField("padlock.png",PASSWORD_HINT,TOOLTIP_PASSWORD_LOGIN);
        jpfPassword.setEchoChar(PASSWORD_CHAR);

        c.gridy = 1;
        c.gridx = 0;
        c.insets = new Insets(0,0,20,0);
        c.ipadx = 200;

        jpgblInfo.add(jtfUsername, c);

        c.gridy = 2;
        jpgblInfo.add(jpfPassword, c);

        //S'afegeix el botó per acceptar la info introduida
        jbAccept = new JButton("Accept");
        jbAccept.setFocusable(false);

        c.gridy = 4;
        c.gridx = 0;
        c.insets = new Insets(20,0,0,0);

        jpgblInfo.add(jbAccept, c);

        c.gridy = 1;
        c.gridx = 0;
        c.insets = new Insets(0,0,0,0);
        jpGeneric.add(jpgblInfo, c);

        //S'afegeix el missatge d'error
        c.insets = new Insets(0,0,20,0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 0;
        c.gridx = 0;
        jlErrorMessage.setForeground(TRANSPARENT);
        jpGeneric.add(jlErrorMessage, c);

        add(jpGeneric, BorderLayout.CENTER);
    }

    public void setError(String error) {
        jlErrorMessage.setText(error);
        jlErrorMessage.setForeground(GRANA);
    }

    @Override
    public void addController(Controller c) {
        jbAccept.setActionCommand("logIn");
        jbAccept.addActionListener(c);


        jpfPassword.setActionCommand("logIn");
        jpfPassword.addActionListener(c);
        jpfPassword.addFocusListener(c);

        jtfUsername.addFocusListener(c);

        jbBack.setActionCommand("backToMain");
        jbBack.addActionListener(c);
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

    public void clearFields() {
        jpfPassword.setText("");
        jtfUsername.setText("");
        jcbRememberLogIn.setSelected(false);
        jlErrorMessage.setText("");
    }

    public String getUsername() {
        return jtfUsername.getText();
    }

    public String getPassword() {
        return String.valueOf(jpfPassword.getPassword());
    }

    public boolean getRememberLogIn() {
        System.out.println("jcbRemember" + jcbRememberLogIn.isSelected());
        return jcbRememberLogIn.isSelected();
    }

}
