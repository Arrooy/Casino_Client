package Network;

import Controlador.JsonManager;
import Model.User;

import static java.lang.Thread.sleep;

/** Transmission gesiona el process de logIn del sistema*/

public class Transmission implements Runnable {

    public static final String CONTEXT_LOGIN = "login";
    public static final String CONTEXT_LOGOUT = "logout";
    public static final String CONTEXT_SIGNUP = "signup";

    private Message msg;
    private String context;

    /** Referencia al networkManager per a poder enviar dades al servidor*/
    private NetworkManager networkManager;

    public Transmission(Message msg, NetworkManager networkManager){
        this.networkManager = networkManager;
        this.msg = msg;
        this.context = msg.getContext();

        (new Thread(this)).start();
    }

    @Override
    public void run() {

        switch (context) {
            case CONTEXT_LOGIN:
                updateConnection();
                break;
            case CONTEXT_LOGOUT:
                updateConnection();
                break;
            default:
        }

    }

    /**
     * login o logout
     */
    private void updateConnection() {
        try {
            //Enviem l'usuari per intentar accedir al sistema amb les creedencials introduides
            User usuariIntent = (User) msg;

            //Set online indica al servidor que el paquet que rebrá no correspon a una desconexio d'un usuari
            usuariIntent.setOnline(true);
            networkManager.send(usuariIntent);

            //Esperem a una resposta del servidor
            User responseUser = waitLogInResponse(usuariIntent);

            //Si la resposta del servidor indica que tot es correcte. Es completa el logIn
            if(responseUser.areCredentialsOk()){
                finishLogIn(responseUser);

                //TODO: que algu fagi algo amb aixo per a que no quedi com el cul
                networkManager.displayError("Usuari - Contrasenya correcte!","Ets el puto amo, fesme un fill tete\nNOTA PROGRAMADOR VISTA: cundiria ficar aquesta alerta a la vista!!!!!");
            }else {
                //De lo contari, s'indica al usuari que s'ha equivocat
                networkManager.displayError("Usuari - Contrasenya incorrecte!","Torna a intentar-ho pls\nNOTA PROGRAMADOR VISTA: cundiria ficar aquesta alerta a la vista!!!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Espera a que el servidor respongui la peticio de LogIn. Es mira cada 100ms si ha arribat la resposta*/
    private User waitLogInResponse(User UsuariIntent) throws InterruptedException {
        User responseUser;

        do {
            //Es demana al networkManager la resposta del servidor associada a l'id del usuari que hem enviat anteriorment.
            responseUser = (User) networkManager.read(UsuariIntent.getID());
            sleep(100);
        }while(responseUser == null);

        return responseUser;
    }

    /** Finalitza el logIn, actualitzant les dades de l'usuari. Tambe es gestiona la copia local del logIn*/
    private void finishLogIn(User userVerificat){
        //S'actualitza el user
        networkManager.setUser(userVerificat);

        //En el cas de haver marcat la casella de recordar usuari
        if(networkManager.rememberLogIn()){

            //L'afegim al json
            JsonManager.addRemember(userVerificat.getUsername(),userVerificat.getPassword());
        }else{

            //El borrem del json si existeix
            JsonManager.removeRemember();
        }

        networkManager.enterToGames();
    }
}
