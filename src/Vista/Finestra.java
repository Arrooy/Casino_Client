package Vista;

import Controlador.Controller;
import Controlador.DraggableWindow;
import Controlador.Game_Controlers.BlackJackController;
import Model.AssetManager;
import Model.User;
import Network.Transmission;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.SettingsViews.Settings;
import Vista.SettingsViews.SettingsView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Finestra extends JFrame {

    private CardLayout layout;
    private MainViewClient mainView;
    private LogInView logInView;
    private GameSelectorView gameSelectorView;
    private SignInView signInView;
    private Settings settings;
    private BlackJackView blackJackView;
    private JPanel content;
    
    private JButton jbtexit;
    private JButton jbticonify;
    private JButton jbtmax;
    private JButton jbtUser;

    public Finestra() {

        Tray.init();

        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(AssetManager.getImage("arroba.png"))));

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(640, 480);

        //Try to fullScreen
        setExtendedState(MAXIMIZED_BOTH);
        checkFullScreen();

        setMinimumSize(new Dimension(BlackJackView.MIN_SCREEN_WIDTH, BlackJackView.MIN_SCREEN_HEIGHT));
        System.out.println("[FRAME]: "+ getMinimumSize().width + " - " + getMinimumSize().height);

        requestFocus();

        setIconImage(AssetManager.getImage("icon.png"));

        getContentPane().setLayout(new BorderLayout());

        content = new JPanel();
        layout = new CardLayout();
        content.setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();
        gameSelectorView = new GameSelectorView();
        signInView = new SignInView();
        settings = new Settings();
        blackJackView = new BlackJackView();

        content.add("main", mainView);
        content.add("logIn", logInView);
        content.add("gameSelectorView", gameSelectorView);
        content.add("signIn", signInView);
        content.add("settings", settings);
        content.add("blackJack", blackJackView);

        getContentPane().add(content,BorderLayout.CENTER);
        generateTopBar();

       //useCustomCursor();
        setUndecorated(true);
    }


    private void useCustomCursor() {
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(AssetManager.getImage("mouse.png"),new Point(15,0),null);
        setCursor(cursor);
    }

    /*
     * Verifica que el dispositiu ha pogut fer la full screen.
     * La documentacio de JFrame indica que pot no ferse full screen alguns cops
     */
    private void checkFullScreen() {
        if(getExtendedState() != MAXIMIZED_BOTH) {
            System.out.println("Full screen no esta disponible, tirant de minimitzada.");
            goCenter();
        }
    }

    public void addController(Controller c, DraggableWindow dw) {
        Tray.addController(c);

        jbtexit.setActionCommand("exitProgram");
        jbtexit.addActionListener(dw);
        jbticonify.setActionCommand("iconify");
        jbticonify.addActionListener(dw);
        jbtmax.setActionCommand("maximize");
        jbtmax.addActionListener(dw);

        jbtUser.setActionCommand("settings");
        jbtUser.addActionListener(c);

        mainView.addController(c);
        logInView.addController(c);
        gameSelectorView.addController(c);
        signInView.addController(c);
        settings.addController(c);


        c.setMainView(mainView);
        c.setLogInView(logInView);
        c.setSignInView(signInView);
        c.setSettings(settings);
        c.setGameSelectorView(gameSelectorView);
        c.setBlackJackView(blackJackView);


        addWindowListener(dw);
        addMouseMotionListener(dw);

        addComponentListener(c);
    }

    public void setMainView() {
        layout.show(content, "main");
        showUserconfig(false);
    }

    public void setLogInView() {
        layout.show(content, "logIn");
    }

    public void setGameSelector(boolean guest) {
        layout.show(content, "gameSelectorView");
        gameSelectorView.enableButtons(guest);
        showUserconfig(!guest);
    }

    public void setSignInView() {
        layout.show(content, "signIn");
    }


    public User getSignUpUser() {
        return new User(signInView.getUsername(), signInView.getPassword(), signInView.getMail(), Transmission.CONTEXT_SIGNUP);
    }

    public void setBlackJackView() {
        layout.show(content, "blackJack");
    }

    public LogInView getLogInView() {
        return logInView;
    }

    public SignInView getSignInView() {
        return signInView;
    }

    /** Escollim la vista dels settings*/
    public void setSettingsView(String s) {
        layout.show(content, "settings");
        settings.showSetting(s);
    }

    public void goCenter() {
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
    }

    private void generateTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());

        JPanel rightOptions = new JPanel();
        jbtexit = new JButton();
        jbticonify = new JButton();
        jbtmax = new JButton();
        jbtUser = new JButton();

        addButtonTop(jbtexit,"exitOnRest.png","exitOnMouse.png");
        addButtonTop(jbticonify,"minimize.png","minimizeOnMouse.png");
        addButtonTop(jbtmax,"maximize.png","maximizeOnMouse.png");
        addButtonTop(jbtUser,"userConfig.png","userConfigOnMouse.png");
        jbtUser.setVisible(false);

        rightOptions.add(jbticonify);
        rightOptions.add(jbtmax);
        rightOptions.add(jbtexit);

        topBar.add(jbtUser, BorderLayout.WEST);
        topBar.add(rightOptions, BorderLayout.EAST);

        getContentPane().add(topBar,BorderLayout.NORTH);
    }

    private void addButtonTop(JButton boto, String normal,String onSelection){
        boto.setBorderPainted(false);
        boto.setBorder(null);
        boto.setFocusable(false);
        boto.setMargin(new Insets(0, 0, 0, 0));
        boto.setContentAreaFilled(false);
        boto.setIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setDisabledIcon(new ImageIcon(AssetManager.getImage(normal)));
        boto.setRolloverIcon(new ImageIcon(AssetManager.getImage(onSelection)));
        boto.setPressedIcon(new ImageIcon(AssetManager.getImage(normal)));
    }
    public void showUserconfig(boolean visible){
        jbtUser.setVisible(visible);
    }
}
