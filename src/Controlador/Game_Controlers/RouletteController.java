package Controlador.Game_Controlers;

import Controlador.Controller;
import Controlador.CustomGraphics.GraphicsController;
import Controlador.Sounds;
import Model.AssetManager;
import Model.User;
import Network.NetworkManager;
import Network.Transmission;
import Vista.GameViews.Roulette.RouletteView;
import Vista.GraphicUtils.RouletteElements.GRect;
import Vista.GraphicUtils.RouletteElements.RouletteBall;
import Vista.GraphicUtils.RouletteElements.RouletteBetTable;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;

/**
 * Classe que controla tot el conjunt de la ruleta. Aquesta gestiona
 * tots els gràfics que s'han de pintar per pantalla, i actualitza la lògica
 * de l'apartat visual d'aquesta.
 *
 * A més a més s'encarrega de controlar de manera autònoma les accions de
 * l'usuari amb el teclat o el ratolí, per a poder realitzar les diferents
 * accions que permet el joc.
 *
 * El joc rep instruccions des de la classe "RouletteManager", la qual proporciona tota
 * la informació referent a cada tir, i és qui llença la instrucció de generar el tir
 * proporcionant els parametres aleatoris necessaris per a obtenir el guanyador final.
 * A més a més al moment de realitzar l'aposta, també s'indica el temps restant per a iniciar
 * la següent aposta, per així poder representar un temporitzador que indica el temps
 * restant fins la següent aposta.
 *
 * En quant a les apostes, al pèmer sobre una casella de la taula es redirigeix la
 * instrucció a un thread apart que mostra un JinputDialog que demana una quantitat de
 * diners, i la comunica al servidor, on es gestiona l'aposta de manera convenient.
 *
 * El tir de la ruleta consisteix en una animació en la que es desconeix el guanyador d'aquesta,
 * i només es coneix la posició inicial de la ruleta, la velocitat d'aquesta i la velocitat
 * de la bola, i amb aquests parametres s'actualitza la logica de la animació per a arribar al
 * mateix resultat que el servidor. Aquesta només es visualitza quan es rep la instrucció de
 * generar un tir, i desapareix al cap d'uns 12 segons aproximadament.
 *
 * Finalment cal dir que es proporciona un mode de visualització en el que en comptes de
 * visualitzar el tauler i la ruleta, es visualitza un llistat de les apostes realitzades
 * per tots els jugadors connectats.
 */
public class RouletteController implements GraphicsController {

    /** Numero total de cel·les de la ruleta */
    private static final int MAXCELLS = 37;

    /** Angle amb el que s'ha de rotar la imatge de la ruleta per a sincronitzar-la amb la lògica */
    private static final double zeroAng = Math.PI/2 + (Math.PI*2/MAXCELLS)/2;

    /** Amplada / Alçada del tauler de la llista d'apostes totals */
    private static final int LIST_DIM = 700;

    /** Taula de conversio que serveix per transformar el index de la casella de la ruleta
     * per obtenir el valor real d'aquella casella en concret */
    private static final int[] converTable = {0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 26, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26};

    /** Taula de conversió que transforma l'index de les cel·les del tauler d'apostes en el seu valor en String */
    private static final String[] listBetConversion = {"0", "3", "2", "1", "6", "5", "4", "9", "8", "7", "12", "11", "10", "15", "14", "13", "18", "17", "16", "21", "20", "19", "24", "23", "22", "27", "26", "25", "30", "29", "28", "33", "32", "31", "36", "35", "34", "First line", "Second line", "Third line", "First half", "Even", "Red", "Black", "Odd", "Second Half", "First dozen", "Second dozen", "Third dozen", "-----"};

    /** Llistat de barres separadores de les cel·les de la ruleta */
    private LinkedList<GRect> bars;

    /** Bola que es llença a la ruleta */
    private RouletteBall ball;

    /** Velocitat i acceleració del gir de la ruleta */
    private double vel, acc;

    /** Velocitat inicial que pren el gir de la ruleta */
    private static double initVel = 40;

    /** Indica si s'ha trobat un guanyador o no en una tirada */
    private boolean winnerE;

    /** Guanyador d'una tirada concreta */
    private int winner;

    /** Offset inicial de caselles amb el que s'inicia l'animació de la ruleta */
    private int shotOff;

    /** Moment en el que es realitzarà la següent tirada */
    private static long nextTime = Timestamp.from(Instant.now()).getTime();

    /** Taula d'apostes */
    private RouletteBetTable table;

    /** Imatge de fons (tauler d'apostes) */
    private Image background;

    /** Imatge de la ruleta */
    private Image rouletteImage;

    /** Imatge del panell sobre el que apareix la ruleta */
    private Image boardImage;

    /** Font que s'utilitza pels textos del joc */
    private Font font;

    /** Fons del temporitzador */
    private Image wood;

    /** Boto per visualitzar la llista d'apostes */
    private Image viewList;

    /** Mode seleccionat del boto de visualització d'apostes */
    private Image viewListSelected;

    /** Boto EXIT */
    private Image returnButton;

    /** Boto EXIT seleccionat */
    private Image returnButtonSelected;

    /** Fons de pantalla de la llista d'apostes */
    private Image listBackground;

    /** Boto per desplaçar ascendentment el llistat d'apostes */
    private Image upButton;

    /** Boto per desplaçar descendentment el llistat d'apostes */
    private Image downButton;

    /** Imatge del tauler del llistat d'apostes */
    private Image listTable;

    /** Color dels diferents textos a printar */
    private static final Color TEXT_COLOR = new Color(216, 204, 163);

    /** Coordenades del boto de View List */
    private int vlx, vly;

    /** Coordenades del boto de View List */
    private int ebx, eby;

    /** Indica si el botó View List es troba seleccionat */
    private boolean viewListPressed;

    /** Indica si el botó EXIT es troba seleccionat */
    private boolean returnPressed;

    /** Angle actual amb el que es representa la ruleta */
    private double rang, roffTimer;

    /** Indiquen si la ruleta s'ha d'amagar o bé iniciar l'animació per desapareixer */
    private boolean backAnim, hideRoulette;

    /** Accés al NetworkManager per a enviar Messages al Server */
    private NetworkManager networkManager;

    /** Wallet de l'usuari i aposta total realitzada */
    private long wallet, bet;//lastWallet

    /** Mode de visualització de la ruleta */
    private static final int GAME_MODE = 0;

    /** Mode de visualització del llistat d'apostes */
    private static final int LIST_MODE = 1;

    /** Mode de visualització */
    private int mode;

    /** Informació que es pinta al llistat d'apostes */
    private String[][] info;

    /** Offset amb el que es mostra el llistat */
    private int listOff;

    /** Vista en la que es representa tot el que es genera en aquesta classe */
    private RouletteView view;

    /**
     * Constructor de la classe
     * @param view Vista a la que es pinta la funcio render
     * @param networkManager Comunicador amb el Servidor
     */
    public RouletteController(RouletteView view, NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.view = view;

        rang = zeroAng;
        roffTimer = 0;
        backAnim = false;
        hideRoulette = true;

        shotOff = 0;
        mode = GAME_MODE;
        listOff = 0;

        wallet = 0;
        //lastWallet = 0;
        bet = 0;
    }

    /**
     * Mètode per a reinicialitzar el controlador
     */
    public void initRoulette() {
        rang = zeroAng;
        roffTimer = 0;
        backAnim = false;
        hideRoulette = true;

        shotOff = 0;
        mode = GAME_MODE;
        listOff = 0;

        wallet = 0;
        bet = 0;
    }

    /**
     * Mètode en el que s'inicialitza tot el contingut gràfic i lògic
     * de la classe.
     */
    @Override
    public void init() {
        final int teoricWidth = 600;
        final int teoricHeight = 600;

        ball = new RouletteBall(teoricWidth / 2 - 20, teoricHeight / 2 - 50, teoricWidth / 2, teoricHeight / 2, this, 100, 80);

        table = new RouletteBetTable();
        bars = new LinkedList<>();

        vel = 0;
        acc = 0.1;
        winnerE = false;

        rouletteImage = AssetManager.getImage("Rulet.png");
        background = AssetManager.getImage("casino ruleta sin marco.png");
        boardImage = AssetManager.getImage("rouletteBackgroundSmall.png");
        wood = AssetManager.getImage("Num.png");

        viewList = AssetManager.getImage("VG.png");
        viewListSelected = AssetManager.getImage("VGS.png");
        returnButton = AssetManager.getImage("EXIT_NO_SOMBRA.png");
        returnButtonSelected = AssetManager.getImage("EXIT_SOMBRA.png");

        viewListPressed = false;
        returnPressed = false;

        listBackground = AssetManager.getImage("background.png");
        upButton = AssetManager.getImage("SUB.png");
        //upButtonSelected = AssetManager.getImage("SUPS.png");
        downButton = AssetManager.getImage("BAJ.png");
        //getDownButtonSelected = AssetManager.getImage("BAJS.png");
        listTable = AssetManager.getImage("POnline.png");
        wood = AssetManager.getImage("Num.png");

        vlx = 20;
        vly = Controller.getWinHeight() - viewList.getHeight(null) - 20;
        ebx = Controller.getWinWidth() - returnButton.getHeight(null) - 20;
        eby = Controller.getWinHeight() - returnButton.getHeight(null) - 20;

        font = AssetManager.getEFont(50);

        info = new String[0][0];

        hideRoulette = true;
        backAnim = false;

        initRoulette();
    }

    /**
     * Mètode per a actualitzar el temps del proper tir
     * @param newTime Nou temps
     */
    public static void updateNextTime(long newTime) {
        nextTime = newTime;
    }

    /**
     * Funció que obté en un format especial els segons que queden per al
     * següent tir de la ruleta
     * @return String amb el temps
     */
    private static String nextTimeToString() {
        long diff = nextTime - Timestamp.from(Instant.now()).getTime();
        if (diff < -10) diff += 78000;
        else if (diff < 0) diff = 0;
        return String.format("%02d", (int) (diff / 1000));
    }

    /**
     * Funció que actualitza la lògica del joc. Actualitza la ruleta seguint
     * el mateix procediment que el servidor per a arribar al mateix resultat.
     * A més a més també actualitza el moviment de les animacions restants i
     * actualitza el contingut de la llista d'apostes.
     * @param delta Periode d'actualitzacio de la pantalla (0.017s)
     */
    @Override
    public void update(float delta) {

        //////////////////////////////////////////////////////////////////////////////////////////////////////

        /* --- UNTOUCHABLE --- */

        final int teoricWidth = 600;
        final int teoricHeight = 600;

        acc = vel > 100 ? vel > 300 ? 2 : 0.5 : 0.1;

        vel += acc;
        vel = Math.min(100000, vel);

        for (GRect r: bars) r.updateRotation( (float) vel, 0.017f, teoricWidth/2, teoricHeight/2);


        ball.update(0.017f, vel, teoricWidth / 2, teoricHeight / 2);

        boolean bool = true;
        for (int i = bars.size() - 1; i >= 0 && bool; i--) if (ball.rectCollision(bars.get(i))) bool = false;

        //////////////////////////////////////////////////////////////////////////////////////////////////////

        rang += Math.PI / vel;
        if ((System.nanoTime() - roffTimer)/1000000000 > 10) {
            if (!backAnim) roffTimer = System.nanoTime();
            backAnim = true;
        }
        if (backAnim && ((20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 - 2, 4)) > 1000) || (System.nanoTime() - roffTimer)/1000000000 > 3)) hideRoulette = true;

        vlx = 20;
        vly = Controller.getWinHeight() - viewList.getHeight(null) - 20;
        ebx = Controller.getWinWidth() - returnButton.getWidth(null) - 20;
        eby = Controller.getWinHeight() - returnButton.getHeight(null) - 20;

        table.update();

        String[][] aux = networkManager.updateRouletteList();
        info = aux == null ? info : aux;
    }

    /**
     * Mètode que pinta o bé el joc de la ruleta o bé el llistat d'apostes realitzades
     * @param g Element en el que pintar el contingut
     */
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (mode) {
            case GAME_MODE:
                renderRoulette(g);
                break;
            case LIST_MODE:
                renderList(g);
                break;
        }

        g.drawImage(returnPressed ? returnButtonSelected : returnButton, ebx, eby, null);
    }

    /**
     * Mètode que genera la imatge a mostrar per pantalla del llistat d'apostes.
     * La imatge es composa d'un botó per a retornar al joc i un tauler central que
     * mostra el llistat d'apostes realitzades, en el que es pot veure el nom de
     * l'usuari que ha realitzat l'aposta, la cel·la en la que ha apostat i la quantitat
     * de diners que ha apostat. A més a més també es renderitzen dos botons a la part
     * central-dreta de la taula, amb els que l'usuari pot navegar pel llistat.
     * @param g Component en el que pintar el llistat
     */
    private void renderList(Graphics g) {
        g.drawImage(listBackground, 0, 0, Controller.getWinWidth(), Controller.getWinHeight(), null);

        g.drawImage(listTable, Controller.getWinWidth()/2 - LIST_DIM/2, Controller.getWinHeight()/2 - LIST_DIM/2, null);

        g.drawImage(upButton, Controller.getWinWidth()/2 + LIST_DIM/2 - 10, Controller.getWinHeight()/2 - 20, 20, 20, null);
        g.drawImage(downButton, Controller.getWinWidth()/2 + LIST_DIM/2 - 10, Controller.getWinHeight()/2, 20, 20, null);

        int zx = Controller.getWinWidth()/2 - LIST_DIM/2;
        int zy = Controller.getWinHeight()/2 - LIST_DIM/2 + 112;

        int[] cx = {zx + LIST_DIM/6, zx + LIST_DIM*3/6, zx + LIST_DIM*5/6};

        if (info.length != 0) {
            g.setColor(TEXT_COLOR);
            g.setFont(font.deriveFont(17f));

            for (int i = 0; i < Math.min((info.length > 0 ? info[0].length : 0), 33); i++) {
                String cell = "-----";
                try {
                    //int cellID = info[1][i + listOff].equals("-----") ? 49 : Integer.parseInt(info[1][i + listOff]);
                    int cellID = info[1][i + listOff].equals("-----") ? 49 : Integer.parseInt(info[1][i + listOff]);
                    cell = (cellID < 37 ? cellID + "" : listBetConversion[cellID]);
                } catch (Exception e) {}

                for (int j = 0; j < 3; j++) {
                    String s = j == 1 ? cell : info[j][i + listOff];
                    int width = g.getFontMetrics().getStringBounds(s, g).getBounds().width / 2;

                    g.drawString(s, cx[j] - width, zy + 18 * i);
                }
            }
        }
    }

    /**
     * Mètode que nateja el llistat d'apostes
     */
    private void resetBetList() {
        info = new String[0][0];
    }

    /**
     * Mètode que genera la imatge a mostrar per pantalla en la que es visualitza
     * el tauler d'apostes i la ruleta en si, juntament amb la UI que l'acompanya,
     * que consisteix en un botó per a visualitzar el llistat d'apostes, un botó per
     * retornar al menu principal dels jocs, el wallet actual de l'usuari, i el
     * temps restant per a una nota tirada de la ruleta.
     * @param g Component en el que pintar els grafics
     */
    private void renderRoulette(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(background, 0, 0, Controller.getWinWidth(), Controller.getWinHeight(), null);

        double offset = backAnim ? 20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 - 2, 4)) : 20/(Math.pow((System.nanoTime() - roffTimer)/1000000000 +0.4, 4));
        int cx = Controller.getWinWidth()/2;
        int cy = 370 - (int)offset;

        g.setFont(font.deriveFont(15f));
        g.setColor(TEXT_COLOR);
        table.render(g);

        g.setFont(font.deriveFont(50f));

        if (!hideRoulette) g.drawImage(boardImage, cx - 300, -(int)offset, 600, 600, null);

        g2d.rotate(rang, cx, cy);
        if (!hideRoulette) g2d.drawImage(rouletteImage, cx - 200, cy - 200, 400, 400, null);
        g2d.rotate(-rang, cx, cy);

        if (!hideRoulette) ball.render(g, cx, cy, 200);

        g.drawImage(wood, 30, 20, null);
        g.setColor(TEXT_COLOR);
        int timwid = g.getFontMetrics().getStringBounds(nextTimeToString(), g).getBounds().width;
        g.drawString(nextTimeToString(), 30 + (wood.getWidth(null)/2 - timwid/2), 70);

        if (winnerE && !hideRoulette) g.drawString("" + getWinner(), cx - 250, cy + 200);

        int walwid = g.getFontMetrics().getStringBounds("" + (wallet - bet), g).getBounds().width;
        g.setFont(font.deriveFont(20f));

        String mtitle = hideRoulette ? "Wallet" : "Result";

        g.drawString(mtitle, Controller.getWinWidth()/2 - g.getFontMetrics().getStringBounds(mtitle, g).getBounds().width/2, Controller.getWinHeight() - 100);
        g.setFont(font.deriveFont(50f));

        long moneyToShow = (wallet - bet);
        g.drawString(moneyToShow + "", Controller.getWinWidth()/2 - walwid/2, Controller.getWinHeight() - 40);

        g.drawImage(viewListPressed ? viewListSelected : viewList, vlx, vly, null);
    }

    /**
     * Mètode per a indicar que la bola ha caigut a una cel·la de la ruleta
     * @param winer Cel·la de la ruleta
     */
    public void setWinner(int winer) {
        if (!winnerE) requestWallet();
        winnerE = true;
        this.winner = winer;
    }

    /** Getter de les barres separadores de la ruleta */
    public LinkedList<GRect> getBars() {
        return bars;
    }

    /**
     * Mètode per a establir els paràmetres aleatoris de cada tirada generats
     * pel servidor.
     * @param vel Velocitat inicial de la ruleta
     * @param ballVel Velocitat inicial de la bola
     * @param shotOff Deplaçament inicial de cel·les
     */
    public void setParams(double vel, double ballVel, int shotOff) {
        RouletteController.initVel = vel;
        ball.setDefaultVelY(ballVel);
        this.shotOff = shotOff;
    }

    /**
     * @return Retorna el numero guanyador de la tirada
     */
    public int getWinner() {
        return converTable[(shotOff + winner) % MAXCELLS];
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * Codi que s'executa cada cop que es deixa de prèmer un botó
     */
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("KEY PRESSED");
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.out.println("ESCAPE PRESSED");
            networkManager.exitRoulette();
        }
    }

    /**
     * Mètode que serveix per a iniciar una tirada a partir dels
     * paràmetres establerts
     */
    public void shoot() {
        bars = new LinkedList<>();

        int width = 600;
        int height = 600;

        for (int i = 0; i < 37; i++) {
            bars.add(new GRect(width / 2 - 100, height / 2 - 1, 20, 2, i * 2*Math.PI/37, width /2, height /2));
        }
        ball = new RouletteBall(width / 2 - 20, height / 2 - 50, width / 2, height / 2, this, 100, 80);
        vel = initVel;
        winnerE = false;

        table.cleanTable();
        //resetBetList();
        String[][] aux = networkManager.updateRouletteList();
        info = aux == null ? info : aux;

        rang = zeroAng - (Math.PI*2/MAXCELLS) * shotOff;
        roffTimer = System.nanoTime();
        backAnim = false;
        hideRoulette = false;
        //lastWallet = wallet;

        //S'inicia el fx de la bola
        Sounds.play("RRun.wav");
        //requestWallet();
    }

    /**
     * Mètode que realitza una petició al servidor per a que
     * aquest retorni la quantitat de diners actual de l'usuari
     */
    public void requestWallet() {
        new Transmission(new User("", "", "walletRequest"), networkManager);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Mètode que s'executa cada cop que l'usuari prem el ratolí en una part de la
     * pantalla. S'encarrega d'activar els botons en cas de prèmer en ells, i s'encarrega
     * d'activar el JInputDialog en el que es demana a l'usuari que introdueixi la quantitat
     * de diners que vol apostar en aquella cel·la per a fer la petició d'aposta al servidor
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (mode == GAME_MODE) {
            int cellID = table.getCellID(e.getX(), e.getY());
            if (cellID >= 0) view.bet(cellID, networkManager, "How much do you want to bet?", "Bet on " + (cellID < 37 ? cellID + "" : listBetConversion[cellID]));

            viewListPressed = e.getX() > vlx && e.getY() > vly && e.getY() < vly + viewList.getHeight(null) && e.getX() < vlx + viewList.getWidth(null);
        }
        returnPressed = e.getX() > ebx && e.getX() < ebx + returnButton.getWidth(null) && e.getY() > eby && e.getY() < eby + returnButton.getHeight(null);
    }

    /**
     * Mètode que s'executa al deixar de prèmer e ratolí. S'encarrega de realitzar l'acció
     * pertinent de cada botó en cas necessari.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Sounds.play("RClick.wav");

        if (mode == LIST_MODE) {
            if (e.getX() > Controller.getWinWidth()/2 + LIST_DIM/2 - 10 && e.getX() < Controller.getWinWidth()/2 + LIST_DIM/2 + 10
                    && e.getY() > Controller.getWinHeight()/2 - 20 && e.getY() < Controller.getWinHeight()/2)
                if (listOff > 0) listOff--;
            if (e.getX() > Controller.getWinWidth()/2 + LIST_DIM/2 - 10 && e.getX() < Controller.getWinWidth()/2 + LIST_DIM/2 + 10
                    && e.getY() > Controller.getWinHeight()/2 && e.getY() < Controller.getWinHeight()/2 + 20)
                if (info.length > 0 && info[0].length - listOff > 33) listOff++;

        } else {
            if (e.getX() > vlx && e.getY() > vly && e.getY() < vly + viewList.getHeight(null) && e.getX() < vlx + viewList.getWidth(null))
                mode = LIST_MODE;
        }

        //System.out.println(Controller.getWinWidth() + " X "+ Controller.getWinHeight());

        if (e.getX() > ebx && e.getX() < ebx + returnButton.getWidth(null) && e.getY() > eby && e.getY() < eby + returnButton.getHeight(null)) {
            if (mode == GAME_MODE) networkManager.exitRoulette();
            else mode = GAME_MODE;
        }

        returnPressed = false;
        viewListPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    /**
     * Mètode per a afegir una aposta al tauler després d'haver-la confirmat
     * al servidor.
     * @param bet Quantitat apostada
     * @param cellID Cel·la en la que s'hi aposta
     */
    public void bet(long bet, int cellID) {
        table.bet(bet, cellID);
    }

    /** Setter del moneder */
    public void setWallet(long wallet) {
        this.wallet = wallet;
    }

    /** Mètode per eliminar la aposta realitzada */
    public void resetBet() {
        bet = 0;
    }

    /** Mètode per a incrementar la aposta realitzada */
    public void increaseBet(long amount) {
        bet += amount;
    }
}