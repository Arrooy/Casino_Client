package Network;

import Controlador.Controller;
import Controlador.JsonManager;
import Model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkManager extends Thread {

    private Controller controller;


    private Socket socket;

    private final String IP;
    private final int PORT;

    private User user;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private ArrayList<Message> lectures;

    private LogInManager logInManager;
    private boolean autoLogin;
    private boolean conectatAmbServidor;

    public NetworkManager() {
        logInManager = new LogInManager(this);
        lectures = new ArrayList<>();

        Object[] configuracio = JsonManager.llegirJson("IpServidor", "PortServidor",JsonManager.BOOLEAN_R);

        IP = (String) configuracio[0];
        PORT = (int) configuracio[1];

        autoLogin = configuracio[2] != null && (boolean)configuracio[2];

        start();
    }

    public void logIn(Object ... credentials) {
        if(!conectatAmbServidor){
            connectarAmbServidor();
        }
        logInManager.init((String)credentials[0],(String)credentials[1],this);
        (new Thread(logInManager)).start();
    }

    public void requestLogOut(){
        System.out.println("requestLogOut");
        user.setOnline(false);
        send(user);
    }

    private void logOut(){
        conectatAmbServidor = false;
        lectures = new ArrayList<>();
        user = null;
    }

    @Override
    public void run() {
        while(true) {
            if(!conectatAmbServidor) {
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Message missatge = (Message) ois.readObject();
                    if(missatge instanceof User){
                        if(!((User)missatge).isOnline()){
                            System.out.println("LOGING OUT");
                            logOut();
                            continue;
                        }
                    }
                    lectures.add(missatge);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void connectarAmbServidor(Controller controller){
        this.controller = controller;
        user = null;

        try {
            socket = new Socket(IP,PORT);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            conectatAmbServidor = true;
            if(autoLogin)
                logIn(JsonManager.llegirJson(JsonManager.USERNAME_R, JsonManager.PASSWORD_R));
        }catch (IOException e){
            System.out.println("Impossible connectarse amb el servidor");
        }
    }
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

    public void send(Object objectToSend){
        try {
            oos.writeObject(objectToSend);
        } catch (IOException e) {
            System.out.println("IMPOSSIBLE ENVIAR MISSATGE!\n\n");
            e.printStackTrace();
        }
    }

    public Object read(double ID){
        for(Message message : lectures){
            System.out.println("message Sin leer:" + message.getID());
            if(message.getID() == ID) {
                lectures.remove(message);
                return message;
            }
        }
        return null;
    }

    public void setUser(User user) {
        this.user = user;
        System.out.println("Log in correcte");
    }

    public boolean rememberLogIn() {
        return controller.rememberLogIn();
    }

    /** Mostra un error amb una alerta al centre de la finestra grafica*/
    public void displayError(String title, String errorText){
        controller.displayError(title,errorText);
    }

}
