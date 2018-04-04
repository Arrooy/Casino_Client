package Vista;

import Controlador.Controller;

import javax.swing.*;
import java.awt.*;

public class LogInView extends View {

    private JButton jbAccept;
    private JTextField jtfUsername;
    private JPasswordField jpfPassword;
    private JCheckBox jcbRememberLogIn;
    private JButton jbBack;

    public LogInView(){
        this.setLayout(new BorderLayout());

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
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 24));
        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els camps d'UserName, Password i l'opció de Remember me centrats al mig de la pantalla
        JPanel jpgblInfo = new JPanel(new GridBagLayout());
        //Marges
        c.insets = new Insets(0,0,20,10);
        JLabel jlName = new JLabel("UserName:");
        JLabel jlPassword = new JLabel("Password:");
        jcbRememberLogIn = new JCheckBox("Remember user");
        jcbRememberLogIn.setFocusable(false);

        //S'afegeixen les etiquetes
        c.gridy = 0;
        jpgblInfo.add(jlName, c);

        c.gridy = 1;
        jpgblInfo.add(jlPassword, c);

        c.gridy = 2;
        c.gridx = 1;
        c.insets = new Insets(0,0,0,0);
        jpgblInfo.add(jcbRememberLogIn, c);

        //S'afegeixen els camps per omplir
        jtfUsername = new JTextField();
        jpfPassword = new JPasswordField();

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.gridx = 1;
        c.ipadx = 200;
        c.gridwidth = 2;

        jpgblInfo.add(jtfUsername, c);

        c.gridy = 1;
        jpgblInfo.add(jpfPassword, c);

        //S'afegeix el botó per acceptar la info introduida
        jbAccept = new JButton("Accept");
        jbAccept.setFocusable(false);

        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(20,0,0,0);

        jpgblInfo.add(jbAccept, c);

        this.add(jpgblInfo, BorderLayout.CENTER);

    }

    @Override
    public void addController(Controller c) {
        //private JButton jbAccept;
        jbAccept.setActionCommand("logIn");
        jbAccept.addActionListener(c);

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
