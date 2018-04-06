package Network;

import Controlador.JsonManager;
import Model.User;
import Model.Card;

import static java.lang.Thread.sleep;

/** Transmission gesiona el process de logIn del sistema*/

public class Transmission implements Runnable {

    public static final String CONTEXT_LOGIN = "login";
    public static final String CONTEXT_LOGOUT = "logout";
    public static final String CONTEXT_SIGNUP = "signup";
    public static final String CONTEXT_BLACK_JACK = "blackjack";

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
                if (!updateConnection()) {
                    networkManager.setLoginErrorMessage("⋙ Error, dades incorrectes");
                }
                break;
            case CONTEXT_SIGNUP:
                updateConnection();
                break;
            case CONTEXT_LOGOUT:
                updateConnection();
                break;
            case CONTEXT_BLACK_JACK:
                blackJackUpdate();
                break;

            default:
        }
    }

    private void blackJackUpdate() {
        Card carta = (Card) msg;
        networkManager.send(carta);

        try {
            Card cartaResposta = (Card) waitResponse(carta);
            System.out.println("cartaResposta: " + cartaResposta.getCardName());
            networkManager.newBJCard(cartaResposta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * login o logout
     */
    private boolean updateConnection() {
        try {
            //Enviem l'usuari per intentar accedir al sistema amb les creedencials introduides
            User usuariIntent = (User) msg;

            //Set online indica al servidor que el paquet que rebrá no correspon a una desconexio d'un usuari
            usuariIntent.setOnline(true);
            networkManager.send(usuariIntent);

            //Esperem a una resposta del servidor
            User responseUser = (User) waitResponse(usuariIntent);

            //Si la resposta del servidor indica que tot es correcte. Es completa el logIn
            if(responseUser.areCredentialsOk()){
                finishLogIn(responseUser);

                //TODO: que algu fagi algo amb aixo per a que no quedi com el cul
                //networkManager.displayError("Usuari - Contrasenya correcte!","Ets el puto amo, fesme un fill tete\nNOTA PROGRAMADOR VISTA: cundiria ficar aquesta alerta a la vista!!!!!");

                return true;
            } else {
                //De lo contari, s'indica al usuari que s'ha equivocat
                //networkManager.displayError("Usuari - Contrasenya incorrecte!","Torna a intentar-ho pls\nNOTA PROGRAMADOR VISTA: cundiria ficar aquesta alerta a la vista!!!!!");

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Espera a que el servidor respongui una peticio. Es mira cada 100ms si ha arribat la resposta*/
    private Message waitResponse(Message SolicitudEnviada) throws InterruptedException {
        Message resposta;

        do {
            //Es demana al networkManager la resposta del servidor associada a l'id del Message que s'ha enviat anteriorment.
            resposta = networkManager.read(SolicitudEnviada.getID());
            sleep(100);
        }while(resposta == null);

        return resposta;
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
