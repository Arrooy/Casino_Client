package Vista;

import javax.swing.*;
import java.awt.*;

public class SingInView extends JPanel {
    public SingInView(){
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
        JLabel jlTitle = new JLabel("Sing In");
        jlTitle.setFont(new Font("ArialBlack", Font.BOLD, 24));
        //Marges
        c.insets = new Insets(20,0,0,0);
        jpgblTitle.add(jlTitle, c);
        jpTitle.add(jpgblTitle);
        this.add(jpTitle, BorderLayout.NORTH);

        //Panell amb els camps d'UserName, email, Password i confirmar Password centrats al mig de la pantalla
        JPanel jpgblInfo = new JPanel(new GridBagLayout());
        c.insets = new Insets(0,0,20,10);
        JLabel jlName = new JLabel("UserName:");
        JLabel jlEmail = new JLabel("e-mail:");
        JLabel jlPassword = new JLabel("Password:");
        JLabel jlConfirmPassword = new JLabel("Confirm Password:");

        //S'afegeixen les etiquetes
        c.gridy = 0;
        jpgblInfo.add(jlName, c);

        c.gridy = 1;
        jpgblInfo.add(jlEmail, c);

        c.gridy = 2;
        jpgblInfo.add(jlPassword, c);

        c.gridy = 3;
        c.insets = new Insets(0,0,0,10);
        jpgblInfo.add(jlConfirmPassword, c);

        //S'afegeixen els camps per omplir
        JTextField jtfName = new JTextField();
        JTextField jtfEmail = new JTextField();
        JPasswordField jpfPassword = new JPasswordField();
        JPasswordField jpfConfirmPassword = new JPasswordField();

        c.insets = new Insets(0,0,20,0);
        c.gridy = 0;
        c.gridx = 1;
        c.ipadx = 200;
        c.gridwidth = 2;

        jpgblInfo.add(jtfName, c);

        c.gridy = 1;
        jpgblInfo.add(jtfEmail, c);

        c.gridy = 2;
        jpgblInfo.add(jpfPassword, c);

        c.gridy = 3;
        c.insets = new Insets(0,0,0,0);
        jpgblInfo.add(jpfConfirmPassword, c);

        //S'afegeix el botó per acceptar la info introduida
        JButton jbAccept = new JButton("Accept");
        jbAccept.setFocusable(false);

        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = 3;
        c.insets = new Insets(20,0,0,0);

        jpgblInfo.add(jbAccept, c);

        this.add(jpgblInfo, BorderLayout.CENTER);
    }
}
