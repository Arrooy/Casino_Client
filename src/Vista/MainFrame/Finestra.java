package Vista.MainFrame;

import Controlador.Controller;
import Controlador.DraggableWindow;
import Utils.AssetManager;
import Model.WalletEvolutionMessage;
import Vista.*;
import Vista.GameViews.BlackJack.BlackJackView;
import Vista.GameViews.Roulette.RouletteView;
import Vista.GameViews.HorseRaceView;
import Vista.SettingsViews.Settings;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;


/** Frame del casino. Apareix despres de que la SplashScreen carregui tots els assets del joc*/
public class Finestra extends JFrame {

    /** Color de la top bar*/
    private final Color COLOR_TOP_BAR = new Color(54, 57, 66);

    /** Amplada minima del joc*/
    private final int MIN_WIDTH = 800;

    /** Altura minima del joc*/
    private final int MIN_HEIGHT = 800;

    /** Cardlayout de la finestra, conte tots els panells del joc*/
    private CardLayout layout;

    /** Vista principal del casino, apareix al obrir la finestra*/
    private MainViewClient mainView;

    private LogInView logInView;
    private GameSelectorView gameSelectorView;
    private SignInView signInView;
    private Settings settings;
    private BlackJackView blackJackView;
    private HorseRaceView horseRaceView;
    private JPanel content;
    private RouletteView rouletteView;

    private JButton jbtexit;
    private JButton jbticonify;
    private JButton jbtmax;
    private JButton jbtUser;

    /** Panell superior*/
    private static JPanel topBar;
    private JButton jbtMute;

    /** Crea el JFrame definint el seu icono, mida, tray i panells que conte al seu interior*/
    public Finestra() {

        //S'inicia la tray
        Tray.init();

        //S'afegeix tot el contingut al interor d'un panell amb background
        PanelWithBackGround MainPane = new PanelWithBackGround("background.png");

        //Es defineix el panell principal no transparent
        MainPane.setOpaque(false);

        content = new JPanel();
        content.setOpaque(false);

        layout = new CardLayout();
        content.setLayout(layout);

        mainView = new MainViewClient();
        logInView = new LogInView();
        gameSelectorView = new GameSelectorView();
        signInView = new SignInView();
        settings = new Settings();
        blackJackView = new BlackJackView();
        rouletteView = new RouletteView();
        this.horseRaceView = new HorseRaceView();

        content.add("main", mainView);
        content.add("logIn", logInView);
        content.add("gameSelectorView", gameSelectorView);
        content.add("signIn", signInView);
        content.add("settings", settings);
        content.add("blackJack", blackJackView);
        content.add("roulette", rouletteView);
        content.add("horseRace", horseRaceView);

        MainPane.add(content,BorderLayout.CENTER);

        //Es genera el panell superior amb els botons de sortir, maximitzar, minimitzar, mute...
        generateTopBar(MainPane);

        //Es configura el JFrame
        setUndecorated(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setSize(MIN_WIDTH, MIN_HEIGHT);

        //Try to fullScreen
        setExtendedState(MAXIMIZED_BOTH);
        checkFullScreen();

        requestFocus();

        setIconImage(AssetManager.getImage("icon.png"));

        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(this);
        cr.setSnapSize(new Dimension(10, 10));
        cr.setMinimumSize(new Dimension(MIN_WIDTH,MIN_HEIGHT));

        MainPane.setBorder(new LineBorder(COLOR_TOP_BAR, 3));

        add(MainPane,BorderLayout.CENTER);
    }

    /*
     * Verifica que el dispositiu ha pogut fer la full screen.
     * La documentacio de JFrame indica que pot no ferse full screen alguns cops
     */
    private void checkFullScreen() {
        if(getExtendedState() != MAXIMIZED_BOTH) {
            System.out.println("Full screen no esta disponible, tirant de minimitzada.");
            centerInScreen();
        }
    }


    /**
     * Afegeix els controladors del JFrame
     * @param c Controlador dels botons del joc, no del topPane
     * @param dw controlador dels botons de la barra superior (topPane) del joc
     */
    public void addController(Controller c, DraggableWindow dw) {
        Tray.addController(c);

        jbtexit.setActionCommand("exitProgram");
        jbtexit.addActionListener(dw);

        jbticonify.setActionCommand("iconify");
        jbticonify.addActionListener(dw);

        jbtmax.setActionCommand("maximize");
        jbtmax.addActionListener(dw);

        jbtMute.setActionCommand("mute");
        jbtMute.addActionListener(dw);

        jbtUser.setActionCommand("settings");
        jbtUser.addActionListener(c);


        mainView.addController(c);
        logInView.addController(c);
        gameSelectorView.addController(c);
        signInView.addController(c);
        settings.addController(c);
        horseRaceView.addController(c);

        c.setMainView(mainView);
        c.setLogInView(logInView);
        c.setSignInView(signInView);
        c.setSettings(settings);
        c.setGameSelectorView(gameSelectorView);
        c.setBlackJackView(blackJackView);
        c.setRouletteView(rouletteView);

        addWindowListener(dw);

        topBar.addMouseListener(dw);
        topBar.addMouseMotionListener(dw);

        addComponentListener(c);
    }

    /** Centra el JFrame al centre de la pantalla*/
    public void centerInScreen() {
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getSize().height / 2);
    }

    //Genera el topBar amb els botons de sortir, mute, maximitzar,minimitzar i options
    private void generateTopBar(JPanel MainPanel) {
        topBar = new JPanel(new BorderLayout());

        topBar.setBackground(COLOR_TOP_BAR);

        //Es genera un panell per agrupar els elements de la dreta(exit,maximize,minimize)
        JPanel rightOptions = new JPanel();
        jbtexit = new JButton();
        jbticonify = new JButton();
        jbtmax = new JButton();
        jbtUser = new JButton();
        jbtMute = new JButton();

        //Es configuren els botons
        configButton(jbtexit,"exitOnRest.png","exitOnMouse.png");
        configButton(jbticonify,"minimize.png","minimizeOnMouse.png");
        configButton(jbtmax,"maximize.png","maximizeOnMouse.png");
        configButton(jbtUser,"userConfig.png","userConfigOnMouse.png");
        configButton(jbtMute,"mute.png","muteOnMouse.png");

        //S'amaga el userConfig button
        jbtUser.setVisible(false);
        jbtMute.setVisible(false);

        rightOptions.add(jbticonify);
        rightOptions.add(jbtmax);
        rightOptions.add(jbtexit);

        rightOptions.setBackground(COLOR_TOP_BAR);

        //Es genera un panell per a les opcions de l'esquerra(mute,userConfig)
        JPanel leftOptions = new JPanel();
        leftOptions.add(jbtUser);
        leftOptions.add(jbtMute);

        leftOptions.setBackground(COLOR_TOP_BAR);

        topBar.add(leftOptions, BorderLayout.WEST);
        topBar.add(rightOptions, BorderLayout.EAST);

        MainPanel.add(topBar,BorderLayout.NORTH);
    }

    /**
     * Metode que configura les imatges d'un boto per quan no esta apretat i per quan si que ho esta
     * @param boto JButton al que es volen associal les imatges
     * @param normal Imatge per quan el boto no esta apretat
     * @param onSelection Imatge per quan el boto esta apretat
     */
    private void configButton(JButton boto, String normal, String onSelection){
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

    /** Mostra o amaga l'icono de userConfig en el topBar segons visible*/
    public void showUserconfig(boolean visible){
        jbtUser.setVisible(visible);
    }

    /** Mostra o amaga l'icono de audio en el topBar segons visible*/
    public void showAudio(boolean visible){
        jbtMute.setVisible(visible);
    }

    /** Modifica el icono de muted segons el argument muted*/
    public void changeMuteIcon(boolean muted){
        if(muted){
            configButton(jbtMute,"muted.png","mutedOnMouse.png");
        }else{
            configButton(jbtMute,"mute.png","muteOnMouse.png");
        }
    }

    /***
     * Mostra el dialeg per a introduir l'aposta de la partida
     * @return valor que ha proposat l'usuari per apostar
     */
    public static String showInputDialog(String message, String title) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostra un error al usuari
     * @param title titol del error a mostrar
     * @param message missatge del error a mostrar
     */
    public static void showDialog(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }


    public HorseRaceView getHorseRaceView() {
        return this.horseRaceView;
    }

    public void updateWallet(WalletEvolutionMessage newWallet) {
        settings.updateWallet(newWallet);
    }

    public void requestRouletteFocus() {
        rouletteView.requestFocus();
    }

    public static int getTopBarHeight() {
        return topBar.getHeight();
    }


    //Funcions per a variar la vista dins del card layout

    /** Mostra la vista del logIn*/
    public void setLogInView() {
        layout.show(content, "logIn");
    }

    /** Mostra la vista del signIn*/
    public void setSignInView() {
        layout.show(content, "signIn");
    }

    /** Mostra la vista del blackJack*/
    public void setBlackJackView() {
        layout.show(content, "blackJack");
    }

    /** Mostra la vista de la roulette*/
    public void setRouletteView() {
        layout.show(content, "roulette");
    }

    /** Mostra la vista del horseRace*/
    public void setHorseRaceView(){
        layout.show(content, "horseRace");
    }

    /** Mostra la vista de les settings */
    public void setSettingsView(String s) {
        layout.show(content, "settings");
        settings.showSetting(s);
    }

    /** Mostra la vista del menu principal*/
    public void setMainView() {
        layout.show(content, "main");
        jbtUser.setVisible(false);
        jbtMute.setVisible(false);
    }

    /** Mostra la vista del menu per selecionar els jocs. Si l'usuari que vol anar al gameSelector
     * es user, es mostra la vista modificada.*/
    public void setGameSelector(boolean guest) {
        layout.show(content, "gameSelectorView");
        gameSelectorView.enableButtons(guest);
        showUserconfig(!guest);
        showAudio(true);
    }

}
