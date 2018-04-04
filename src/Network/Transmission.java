package Network;

import Controlador.JsonManager;
import Model.User;

import static java.lang.Thread.sleep;

/** Transmission gesiona el process de logIn del sistema*/

public class Transmission implements Runnable {

    /** Nom del usuari que vol fer logIn*/
//    private String username;

    /** Password del usuari que vol fer logIn*/
//    private String password;

    /** Referencia al networkManager per a poder enviar dades al servidor*/
    private NetworkManager networkManager;

    public Transmission(NetworkManager networkManager, String context){
        this.networkManager = networkManager;

    }

    @Override
    public void run() {
        try {

            //Enviem l'usuari per intentar accedir al sistema amb les creedencials introduides
            User usuariIntent = new User(username,password, "");
            //Set online indica al servidor que el paquet que rebrá no correspon a una desconexio d'un usuari
            usuariIntent.setOnline(true);
            networkManager.send(usuariIntent);
            System.out.println("Evniant solicituds");

            //Esperem a una resposta del servidor
            User responseUser = waitLogInResponse(usuariIntent);

            //Si la resposta del servidor indica que tot es correcte. Es completa el logIn
            if(responseUser.areCredentialsOk()){
                finishLogIn(responseUser);
                System.out.println("perf");
                networkManager.displayError("Usuari - Contrasenya correcte!","Ets el puto amo, fesme un fill tete\nNOTA PROGRAMADOR VISTA: cundiria ficar aquesta alerta a la vista!!!!!");
            }else {
                System.out.println("mal rollo tete");
                //De lo contari, s'indica al usuari que s'ha equivocat
                networkManager.displayError("Usuari - Contrasenya incorrecte!","Torna a intentar-ho pls\nNOTA PROGRAMADOR VISTA: cundiria ficar aquesta alerta a la vista!!!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Inicialitza el nom i la password del usuari que es vol fer LogIn*/
    public void config(String username, String password, NetworkManager networkManager) {
        this.username = username;
        this.password = password;
        this.networkManager = networkManager;
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
    }
}
