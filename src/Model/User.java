package Model;

import Network.Message;

import java.io.Serializable;
import java.util.ArrayList;

/** Usuari basic del casino*/

public class User extends Message implements Serializable {

    /** Defineix el identificador del missatge. Util per quan s'envia amb el networkManager*/
    private final double ID;

    /** Nom de l'usuari*/
    private String username;

    /** Password del usuari*/
    private String password;

    /** Indica si el servidor accepta el logIn de l'usuari*/
    private boolean credentialsOk;

    /** Indica si l'usuari desitja desconnectar-se del servidor*/
    private boolean online;

    /** Email de l'usuari*/
    private String mail;

    /** Diners del usuari*/
    private long wallet;

    /** Evolucio dels diners del usuari*/
    private ArrayList<Long> coinEvolution;


    /**
     * Crea un usuari amb un nom i una password.
     * Aquest usuari se li adjudica un IdentificadorAleatori per a una millor comunicacio client - servidor.
     * @param name Username del usuari que es vol crear
     * @param password Password del usuari que es vol crear
     */

    public User(String name, String password) {

        coinEvolution = new ArrayList<>();
        ID = Math.random();

        this.username = name;
        this.password = password;
        this.credentialsOk = false;
    }

    /** GETTERS I SETTERS */

    public boolean areCredentialsOk() {
        return credentialsOk;
    }

    public void setCredentialsOk(boolean credentialsOk) {
        this.credentialsOk = credentialsOk;
    }

    public double getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getWallet() {
        return wallet;
    }

    public void setWallet(long wallet) {
        this.wallet = wallet;
    }

    public ArrayList<Long> getCoinEvolution() {
        return coinEvolution;
    }

    public void setCoinEvolution(ArrayList<Long> coinEvolution) {
        this.coinEvolution = coinEvolution;
    }


    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
