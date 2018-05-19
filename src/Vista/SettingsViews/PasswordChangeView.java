package Vista.SettingsViews;

import Controlador.Controller;
import Model.AssetManager;
import Vista.SwingModifications.IconPasswordField;
import Vista.PasswordConfirm;
import Vista.View;



import javax.swing.*;
import java.awt.*;

/**Classe que crea el panell que implementa la funcio dels settings de canviar la cotrasenya*/
public class PasswordChangeView extends View implements PasswordConfirm{
    /**String constant per indicar que el camp s'ha d'omplir amb la nova contrasenya*/
    private static final String TOOL_TIP_NEW_PASSWORD = "Your new password";

    /**String constant per indicar que el camp s'ha de repetir la nova contrasenya*/
    private static final String TOOL_TIP_CONFIRM_PASSWORD = "Repeat the same password";

    /**String constant per indicar que el camp s'ha d'omplir amb la nova contrasenya*/
    private static final String NEW_PASSWORD_HINT = "New password";

    /**String constant per indicar que el camp s'ha de repetir la nova contrasenya*/
    private static final String CONFIRM_PASSWORD_HINT = "Confirm password";

    /**Caracter constant que es mostra quan s'escriu la contrasenya enlloc dels caracters que l'usuari esta escribint*/
    private final static char PASSWORD_CHAR = '*';

    /**Color Grana*/
    private final static Color GRANA = new Color(125, 28, 37);

    /**Color Verd*/
    private final static Color VERD = new Color(40, 73, 7);

    /**Color Groc*/
    private final static Color GROC = new Color(237, 175, 67);

    /**Color Transparent*/
    private final static Color TRANSPARENT = new Color(0,0,0,0);

    /**Nova contrasenya*/
    private IconPasswordField jpfNewPassword;

    /**Repetir contrasenya*/
    private IconPasswordField jpfConfirmPassword;

    /**Boto que confirma el canvi de contrasenya*/
    private JButton jbConfirmPassword;

    /**Missatge que indica els errors dels que consta la contrasenya*/
    private JLabel  jlCheckPassword;

    /**Mostra la fortalesa de la contrasenya*/
    private JLabel jlStrength;

    /**Mostra de forma visual la fortalesa de la contrasenya*/
    private JProgressBar jpbStrength;

    /**Constructor del panell que defineix on i com es col·loquen els elements*/
    public  PasswordChangeView(){
        //Es defineix el Layout
        this.setLayout(new BorderLayout());

        JPanel jpGeneric = new JPanel(new GridBagLayout());
        jpGeneric.setOpaque(false);

        jlCheckPassword = new JLabel();

        //S'associa als camps una icona per tal d'identificar-los millor
        jpfConfirmPassword = new IconPasswordField("padlock.png",CONFIRM_PASSWORD_HINT,20,TOOL_TIP_CONFIRM_PASSWORD);
        jpfNewPassword = new IconPasswordField("padlock.png",NEW_PASSWORD_HINT,20,TOOL_TIP_NEW_PASSWORD);
        jpfConfirmPassword.setMaximumSize(jpfConfirmPassword.getSize());
        jpfNewPassword.setMaximumSize(jpfNewPassword.getSize());

        //Es canvia el caracter que es mostra quan s'escriu la contrasenya enlloc dels caracters que l'usuari esta escribint
        jpfNewPassword.setEchoChar(PASSWORD_CHAR);
        jpfConfirmPassword.setEchoChar(PASSWORD_CHAR);

        jbConfirmPassword = new JButton();

        //Es defineixen les imatges corresponents a aquest boto
        configButton(jbConfirmPassword, "UP.png", "UPS.png", "UPD.png");
        jbConfirmPassword.setEnabled(false);

        //Panell per posicionar correctament els camps i botons
        JPanel jpPasswordChange = new JPanel();
        jpPasswordChange.setOpaque(false);
        jpPasswordChange.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        //Posicio nova contrasenya
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        c.insets = new Insets(0,0,20,0);
        jpfNewPassword.setText("");
        jpPasswordChange.add(this.jpfNewPassword, c);

        //Posicio repetir contrasenya
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0,0,20,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        jpPasswordChange.add(this.jpfConfirmPassword, c);

        c = new GridBagConstraints();

        //Posicio missatge error contrasenya
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,0,20,0);
        this.jlCheckPassword.setText("Check");
        this.jlCheckPassword.setPreferredSize(new Dimension(300,15));
        this.jlCheckPassword.setForeground(TRANSPARENT);
        jpGeneric.add(this.jlCheckPassword, c);

        //Posicio fortalesa contrasenya
        jlStrength = new JLabel("Strength");
        jlStrength.setForeground(TRANSPARENT);
        jlStrength.setPreferredSize(new Dimension(150,15));
        c = new GridBagConstraints();
        c.insets = new Insets(0,10,20,95);
        c.gridy = 2;
        c.gridx = 1;
        c.gridwidth = 1;
        jpPasswordChange.add(jlStrength, c);

        //Posicio fortalesa visual contrasenya
        c = new GridBagConstraints();
        c.insets = new Insets(0,0,20,0);
        c.gridy = 2;
        c.gridx = 0;
        c.ipadx = 100 ;
        c.fill = GridBagConstraints.HORIZONTAL;
        jpbStrength = new JProgressBar(0, 40);

        jpbStrength.setValue(0);
        jpbStrength.setForeground(VERD);
        jpPasswordChange.add(jpbStrength, c);

        c = new GridBagConstraints();

        //Posicio boto acceptar canvi contrasenya
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0,0,0,0);
        jpPasswordChange.add(jbConfirmPassword, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0,0,0,0);
        jpGeneric.add(jpPasswordChange, c);

        //Els elements es col·loquen al centre del panell
        this.add(jpGeneric, BorderLayout.CENTER);
        setOpaque(false);
    }

    /**
     * Metode override que relaciona cada element amb el seu Action Listener
     * @param c Controlador
     * */
    @Override
    public void addController(Controller c) {
        jpfConfirmPassword.addKeyListener(c);
        jpfNewPassword.addKeyListener(c);

        jbConfirmPassword.addActionListener(c);

        jpfConfirmPassword.addFocusListener(c);
        jpfNewPassword.addFocusListener(c);

        jpfNewPassword.setName("PASSWORD FIELD CHANGE - NEW PASSWORD");
        jpfConfirmPassword.setName("PASSWORD FIELD CHANGE - CONFIRM PASSWORD");
        jbConfirmPassword.setActionCommand("PASSWORD CHANGE  - CONFIRM PASSWORD");
    }

    /** Metode que mostra un missatge d'error
     * @param message missatge d'error que es mostra quan la contrasenya es incorrecta o te algun error*/
    @Override
    public void passwordKO(String message){
        jlCheckPassword.setText(message);
        jlCheckPassword.setForeground(GRANA);
    }

    /**
     * Metode que indica si la contrasenya i la confirmacio de la contrasenya son iguals
     * @return boolean que indica si son iguals (true) o no (false)
     * */
    @Override
    public boolean getPasswordChangeRequest(){
        String confirmPassword = new String(jpfConfirmPassword.getPassword());
        System.out.println("Confirm: " + confirmPassword);
        String newPassword  = new String(jpfNewPassword.getPassword());
        return confirmPassword.equals(newPassword);
    }

    /** Metode que fixa el valor del JProgress bar
     * Depenent del "strength" actualitza color i text de l'indicador de seguretat
     * @param strength enter qie indica la fortalesa de la contrasenya*/
    @Override
    public void setStrength(int strength){
        if(strength < 15){
            jlStrength.setForeground(GRANA);
            jlStrength.setText("Weak Password");
            jpbStrength.setForeground(GRANA);
            jpbStrength.setValue(17);
        }else if(strength < 25){
            jlStrength.setForeground(GROC);
            jlStrength.setText("Average Password");
            jpbStrength.setForeground(GROC);
            jpbStrength.setValue(27);
        }else{
            jlStrength.setForeground(VERD);
            jlStrength.setText("Strong Password");
            jpbStrength.setForeground(VERD);
            jpbStrength.setValue(40);
        }

    }

    /** Controla la visibilitat del missatge d'error
    * @param error boolean que indica si hi ha hagut algun error o no*/
    @Override
    public void manageError(boolean error){
        if(error){
            this.jlCheckPassword.setForeground(GRANA);
        }else{
            this.jlCheckPassword.setForeground(TRANSPARENT);
        }
    }

    /**@return la contrasenya que l'usuari ha introduit en el camp New Password*/
    @Override
    public String getNewPassword() {
        return new String(jpfNewPassword.getPassword());
    }

    /** Controla el funcionament del boto que et permet sotmetre la nova contrasenya
     * @param ok indica si la contrasenya es correcta*/
    @Override
    public void canConfirm(boolean ok) {
       jbConfirmPassword.setEnabled(ok);
    }

    /**Metode que s'encarrega de netejar tots els camps*/
    public void clearFields(){
       jpfNewPassword.setText("");
       jpfNewPassword.setHint(true);
       jpfConfirmPassword.setText("");
       jpfConfirmPassword.setHint(true);
       jlCheckPassword.setText("");
       jlStrength.setText("");
       jpbStrength.setValue(0);
    }

    /**Metode que configura les imatges d'un boto per quan no esta apretat i per quan si que ho esta
     * @param boto JButton al que es volen associal les imatges
     * @param normal Imatge per quan el boto no esta apretat
     * @param onSelection Imatge per quan el boto esta apretat*/
    private void configButton(JButton boto, String normal,String onSelection){
        boto.setBorderPainted(false);
        boto.setBorder(null);
        boto.setFocusable(false);
        boto.setMargin(new Insets(0, 0, 0, 0));
        boto.setContentAreaFilled(false);
        boto.setIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setRolloverIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setPressedIcon(new ImageIcon(AssetManager.getImage(onSelection)));
    }

    /**Ampliacio del configButton per a poder concretar l'imatge del boto disabled
     * @param boto JButton al que es volen associal les imatges
     * @param normal Imatge per quan el boto no esta apretat
     * @param onSelection Imatge per quan el boto esta apretat
     * @param disabled Imatge per quan el boto esta desactivat*/
    private void configButton(JButton boto, String normal,String onSelection, String disabled){
        configButton(boto,normal,onSelection);
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(disabled)));
    }
}
