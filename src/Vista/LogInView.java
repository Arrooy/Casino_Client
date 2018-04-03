package Vista;

import javax.swing.*;
import java.awt.*;

public class LogInView extends JPanel {
    public LogInView(){
        this.setLayout(new BorderLayout());

        //Panell per col·locar el botó back a la part baixa a l'esquerra
        JPanel jpgblBack = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //Marges
        c.insets = new Insets(20,20,20,0);
        c.fill = GridBagConstraints.BOTH;
        JButton jbBack = new JButton("BACK");
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
        JRadioButton jrbRememberMe = new JRadioButton("Remember user");
        jrbRememberMe.setFocusable(false);

        //S'afegeixen les etiquetes
        c.gridy = 0;
        jpgblInfo.add(jlName, c);

        c.gridy = 1;
        jpgblInfo.add(jlPassword, c);

        c.gridy = 2;
        c.gridx = 1;
        c.insets = new Insets(0,0,0,0);
        jpgblInfo.add(jrbRememberMe, c);

        //S'afegeixen els camps per omplir
        JTextField jtfName = new JTextField();
        JPasswordField jpfPassword = new JPasswordField();

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.gridx = 1;
        c.ipadx = 200;
        c.gridwidth = 2;

        jpgblInfo.add(jtfName, c);

        c.gridy = 1;
        jpgblInfo.add(jpfPassword, c);

        //S'afegeix el botó per acceptar la info introduida
        JButton jbAccept = new JButton("Accept");
        jbAccept.setFocusable(false);

        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(20,0,0,0);

        jpgblInfo.add(jbAccept, c);

        this.add(jpgblInfo, BorderLayout.CENTER);

    }
}
