package Network;

import Controlador.Controller;
import Controlador.JsonManager;
import Model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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

    /** logInManager per a l'inici de sesio*/
    //private Transmission logInManager;

    /** Indica si s'ha de fer LogIn automatic amb les dades locals*/
    private boolean autoLogin;

    /** Inidica si estem connectats amb el servidor. Es indiferent si el client esta autentificat o no.*/
    private boolean conectatAmbServidor;

    /** Inicialitza el NetworkManager carregant les condicions inicials del JSON. Un cop inicialitzat tot, s'inicia el thread.*/
    public NetworkManager() {
       // logInManager = new Transmission(null, this);
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

        //Configurem el logInManager i l'iniciem
        Transmission logInManager = new Transmission( this);
        logInManager.config((String)credentials[0],(String)credentials[1], Transmission.CONTEXT_LOGIN,this);
        (new Thread(logInManager)).start();
    }

    /** Solicita al servidor tencar la sessio actual*/
    public void requestLogOut(){
        if(user != null) {
            user.setOnline(false);
            user.setContext(Transmission.CONTEXT_LOGOUT);
            send(user);
        }else
            logOut();

    }

    /** Completa el tencament de la sessio actual despres de rebre la confirmacio per part del servidor*/
    private void logOut(){
        conectatAmbServidor = false;
        lectures = new ArrayList<>();
        user = null;
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
                    e.printStackTrace();
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
            socket = new Socket(IP,PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            //En el cas d'haver arribat aquest punt del codi, significa que ens hem connectat correctament
            conectatAmbServidor = true;
            //Si en el json de configuracio inicial apareix l'indicador
            //d'autoLogin, s'executa el login de forma automatica.
            if(autoLogin)
                logIn(JsonManager.llegirJson(JsonManager.USERNAME_R, JsonManager.PASSWORD_R));
        }catch (IOException e){
            System.out.println("Impossible connectarse amb el servidor");
        }
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
    public void send(Object objectToSend){
        try {
            oos.writeObject(objectToSend);
            //User u = (User) objectToSend;
            //System.out.println(u);
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
    public Object read(double ID){
        //Es miren tots els missatges registrats fins el moment
        for(Message message: lectures){
            //Si el missatge de l'iteracio conte l'id que es buscava, es retorna l'objecte.
            if(message.getID() == ID) {
                lectures.remove(message);
                return message;
            }
        }
        //Si no s'ha trobat l'ID, es retorna null;
        return null;
    }

    /** Inicialitza l'usuari un cop aquest s'ha autentificat*/
    public void setUser(User user) {
        this.user = user;

        System.out.println("Log in correcte");
    }

    /** Inidica si es vol recordar el logIn*/
    public boolean rememberLogIn() {
        return controller.rememberLogIn();
    }

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        controller.displayError(title,errorText);
    }

    public void enterToGames() {
        controller.showGamesView();
    }

    public Controller getController() {
        return controller;
    }
}
