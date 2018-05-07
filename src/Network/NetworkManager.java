package Network;

import Controlador.Controller;
import Controlador.Sounds;
import Model.HorseRace_Model.HorseBet;
import Model.HorseRace_Model.HorseMessage;
import Model.HorseRace_Model.HorseSchedule;
import Utils.JsonManager;
import Model.Card;
import Model.Transaction;
import Model.User;
import Controlador.SplashScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

import static Network.Transmission.CONTEXT_BJ_FINISH_USER;

//TODO: FER QUE SERVER RETORNI USER EN EL LOGIN AMB USERNAME COM A USERNAME. PER SI ES DONA EL CAS QUE FAN LOGIN AMB EL MAIL!

/**
 * NetworkManager gestiona totes les comunicacions del client amb el servidor en les dues direccions.
 */

public class NetworkManager extends Thread {

    /** Controlador del sistema*/
    private Controller controller;

    /** Socket que es connectara al servidor*/
    private Socket socket;

    /** IP del servidor*/
    private final String IP;

    /** Port del servidor on el client es connectara*/
    private final int PORT;

    /** Usuari que esta utilitzant el client. Equival a null si aquest no ha fet LogIn*/
    private User user;

    /** Canals d'emissio / recepcio d'objectes amb el serividor*/
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Un cop el client s'ha connectat amb el servidor, aquest no parara de llegir tot el que se li comuniqui.
     * Com totes les commandes del servidor no es poden resoldre alhora, aquestes son guardades en aquesta llista.
     * D'aquesta manera assegurem la lectura i el processament de tota la informacio que ens envia el servidor.
     */
    private ArrayList<Message> lectures;

    /** Indica si s'ha de fer LogIn automatic amb les dades locals*/
    private boolean autoLogin;

    /** Inidica si estem connectats amb el servidor. Es indiferent si el client esta autentificat o no.*/
    private boolean conectatAmbServidor;

    /** Splash screen del programa. En cas de tenir conexio amb el servidor, networkManager sencarrega de tencar-la*/
    private SplashScreen splashScreen;

    /** Indica el nombre de cops que el client ha intentat reconectarse amb el servidor*/
    private int nTryConnect;

    /** Inicialitza el NetworkManager carregant les condicions inicials del JSON. Un cop inicialitzat tot, s'inicia el thread.
     * @param splashScreen
     */
    public NetworkManager(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
        nTryConnect = 0;

        lectures = new ArrayList<>();

        Object[] configuracio = JsonManager.llegirJson("IpServidor", "PortServidor",JsonManager.BOOLEAN_R);

        IP = (String) configuracio[0];
        PORT = (int) configuracio[1];

        autoLogin = configuracio[2] != null && (boolean)configuracio[2];

        start();
    }

    /** Intenta fer logIn a partir de 2 creedencials. Usuari - Password.*/
    public void logIn(Object ... credentials) {

        //Si el client no esta connectat al servidor, es connecta
        if(!conectatAmbServidor){
            connectarAmbServidor();
        }
        if(conectatAmbServidor) {
            //Configurem el logIn i enviem la solicitud al servidor
            User user = new User((String)credentials[0],(String)credentials[1],Transmission.CONTEXT_LOGIN);
            new Transmission( user, this);
        }else{
            System.out.println("No hi ha connexio amb el server");
        }
    }


    /** Solicita al servidor tencar la sessio actual*/
    public void requestLogOut(){
        if(conectatAmbServidor){
            User user = new User("","",Transmission.CONTEXT_LOGOUT);
            user.setOnline(false);
            send(user);
        }else{
            logOut();
        }

    }

    /** Completa el tencament de la sessio actual despres de rebre la confirmacio per part del servidor*/
    private void logOut(){
        conectatAmbServidor = false;
        lectures = new ArrayList<>();
        user = null;
    }

    public void transactionOK(boolean ok){
        controller.transactionOK(ok);
    }

    @Override
    public void run() {
        while(true) {

            //Si el client no esta connectat al servidor, esperem a que ho estigui
            if(!conectatAmbServidor) {
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                //Quan el client estigui connectat es llegeixen les commandes del servidor i
                //es guarden en la llista de lectures.
                try {
                    Message missatge = (Message) ois.readObject();

                    //Si el servidor vol desconnectar aquest client, no guardem el missatge a lectures i acabem el logOut
                    if(ServidorVolDesconnectarAquestClient(missatge))
                        continue;

                    lectures.add(missatge);
                } catch (IOException | ClassNotFoundException e) {
                    //e.printStackTrace();
                }
            }
        }
    }


    /** Reconeix el missatge rebut del servidor i indica si el servidor vol la desconnexio d'aquest client*/
    private boolean ServidorVolDesconnectarAquestClient(Message missatge){
        //Si el missatge es un usuari amb el flag online a false significa que el servidor
        //desitja/permet la desconnexio d'aquest client
        if(missatge instanceof User){
            if(!((User)missatge).isOnline()){
                logOut();
                return true;
            }
        }
        return false;
    }


    /**
     * Connecta el client amb el servidor. Tambe enllaça el NetworkManager amb el controlador del sistema
     * @param controller controlador que es vol associar amb el NetworkManager
     */
    public void connectarAmbServidor(Controller controller){
        this.controller = controller;
        user = null;

        try {
            //S'intenta realitzar la connexio al servidor
            socket = new Socket(IP, PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            //En el cas d'haver arribat aquest punt del codi, significa que ens hem connectat correctament
            conectatAmbServidor = true;

            //Si en el json de configuracio inicial apareix l'indicador
            //d'autoLogin, s'executa el login de forma automatica.
            if (autoLogin){
                splashScreen.infoMessage("Logging in...");
                logIn(JsonManager.llegirJson(JsonManager.USERNAME_R, JsonManager.PASSWORD_R));
           }else{
                //Com ja estem conectats al servidor, ja podem obrir la vista i tencar la SplashScreen
                exitLoadingScreen();
            }

        }catch (IOException e){
            if(nTryConnect == 0) {
                splashScreen.showError("Server connection failed");
                nTryConnect++;
            }else{
                splashScreen.showError("Attempting to reconnect ["+nTryConnect+"]");
                nTryConnect++;
            }
            connectarAmbServidor(controller);
            try {
                sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void exitLoadingScreen() {
        //Com ja estem conectats al servidor, ja podem obrir la vista i tencar la SplashScreen
        splashScreen.exit();
        controller.showFinestra();
    }

    /**
     * Connecta el client amb el servidor sense fer l'enllaç amb el controlador. Tampoc revisa el autoLogin.
     * Es molt util quan es vol fer un logIn despres d'haver fet un logOut
     */

    private void connectarAmbServidor(){
        try {
            socket = new Socket(IP,PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            conectatAmbServidor = true;
        }catch (IOException e){
            System.out.println("Impossible connectarse amb el servidor");
        }
    }

    /***
     * Envia un objecte al servidor.
     * @param objectToSend objecte que es vol enviar al servidor
     */
    public synchronized void send(Object objectToSend){
        try {
            oos.writeObject(objectToSend);
        } catch (IOException e) {
            System.out.println("IMPOSSIBLE ENVIAR MISSATGE!\n\n");
            e.printStackTrace();
        }
    }

    /** TODO: no se escriure nois, algu majuda?
     * Com la lectura del sevidor es fa constantment de manera paral·lela, el funcionament de read varia bastant.
     * Read es limita a buscar un missatge identificat amb l'ID rebut per parametres dins del conjunt de missatges
     * rebuts pel client, guardats a la llista lectures.
     * @param ID Identificador del missatge que es vol buscar.
     * @return Si read no troba el missatge que es desitja, retorna null. De lo contrari retorna el missatge.
     */
    public Message read(double ID){
        //Es miren tots els missatges registrats fins el moment
        for(int index = 0; index < lectures.size(); index++){
            Message message = lectures.get(index);
            //Si el missatge de l'iteracio conte l'id que es buscava, es retorna l'objecte.
            if(message != null && message.getID() == ID) {
                lectures.remove(message);
                return message;
            }
        }
        //Si no s'ha trobat l'ID, es retorna null;
        return null;
    }

    /** TODO: no se escriure nois, algu majuda?
     * Com la lectura del sevidor es fa constantment de manera paral·lela, el funcionament de read varia bastant.
     * Read es limita a buscar un missatge identificat amb el contexte rebut per parametres dins del conjunt de missatges
     * rebuts pel client, guardats a la llista lectures.
     * @param context Context del missatge
     * @return Si read no troba el missatge que es desitja, retorna null. De lo contrari retorna el missatge.
     */
    public Message readContext(String context){
        //Es miren tots els missatges registrats fins el moment
        for(int index = 0; index < lectures.size(); index++){
            Message message = lectures.get(index);
            //Si el missatge de l'iteracio conte l'id que es buscava, es retorna l'objecte.
            if(message != null && message.getContext().equals(context)) {
                lectures.remove(message);
                System.out.println(message.getContext());
                return message;
            }
        }
        //Si no s'ha trobat l'ID, es retorna null;
        return null;
    }

    /** Inicialitza l'usuari un cop aquest s'ha autentificat*/
    public void setUser(User user) {
        this.user = user;
        controller.setUser(user);
        System.out.println("[NETWORK MANAGER]: Logged In");
    }

    /** Inidica si es vol recordar el logIn*/
    public boolean rememberLogIn() {
        if(autoLogin)
            return true;
        return controller.rememberLogIn();
    }

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        controller.displayError(title,errorText);
    }

    public void enterToGames() {
        controller.showGamesView();
        exitLoadingScreen();
    }

    /**
     * Envia al servidor una petició de SignUp per a un usuari concret
     * @param user
     */
    public void
    requestSignUp(User user) {
        new Transmission(user, this);
    }

    public void setLoginErrorMessage(String errorMessage) { controller.showErrorLogIn(errorMessage); }

    //TODO mirar transaction OK MERI
    public void doTransaction(long money){
        Transaction transaction = new Transaction(Transmission.CONTEXT_TRANSACTION, user.getUsername(), money, 0);
        new Transmission(transaction, this);
    }
    //TODO revisat MERI
    public void getWallet(){
        User userAux = new User(user.getUsername(), user.getPassword(), user.getMail(), Transmission.CONTEXT_GET_COINS);
        new Transmission(userAux, this);
    }

    public void initBlackJack(Stack<String> nomCartes,long bet) {
        if(bet != 0){
            Card card = new Card("",bet,Transmission.CONTEXT_BJ_INIT,nomCartes,false);
            new Transmission(card,this);
        }
    }

    public void newBlackJackCard(boolean forIa) {
        new Transmission(new Card("",Transmission.CONTEXT_BJ,forIa),this);
    }


    /** Pont transmitter - controlador - Model BlackJack*/
    public void newBJCard(Card cartaResposta) {

        if(cartaResposta.getContext().equals(Transmission.CONTEXT_BJ_INIT)){
            new Transmission(new Card("",Transmission.CONTEXT_BJ,false),this);
            new Transmission(new Card("",Transmission.CONTEXT_BJ,true),this);
            new Transmission(new Card("",Transmission.CONTEXT_BJ,true),this);
            controller.initBlackJack();
        }
           controller.newBJCard(cartaResposta);
    }

    public void enterAsGuest() {
        if(!conectatAmbServidor){
            connectarAmbServidor();
        }

        if(conectatAmbServidor) {
            //Configurem el logIn i enviem la solicitud al servidor
            new Transmission( new User(), this);
        }else{
            System.out.println("No hi ha connexio amb el server");
        }
    }

    public void newCardForIaTurn() {
        Sounds.play("cardPlace1.wav");
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Transmission(new Card("",CONTEXT_BJ_FINISH_USER,true),this);
    }


    /**Metode per indicar al servidor de que volem jugar als cavalls*/
    public void sendHorseRaceRequest() {
        HorseMessage horseMessage = new HorseMessage((HorseSchedule) null, "Connect");
        horseMessage.setID(user.getID());
        new Transmission(horseMessage, this);
    }

    /**Metode que indica al servidor que ens desconectem de la cursa de cavalls*/
    public void exitHorses() {
        HorseMessage horseMessage = new HorseMessage((HorseSchedule) null, "Disconnect");
        horseMessage.setID(user.getID());
        new Transmission(horseMessage, this);
    }

    public void showGamesView() {
        controller.showGamesView();
    }

}
