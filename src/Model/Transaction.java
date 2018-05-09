package Model;

import Network.Message;
import Utils.Seguretat;

import java.sql.Timestamp;

/** Missatge per a indicar una transaccio monetaria en el casino*/
public class Transaction extends Message {

    /** Diferents tipus de transaccions*/
    public static final int TRANSACTION_DEPOSIT = 0;
    public static final int TRANSACTION_HORSES = 1;
    public static final int TRANSACTION_ROULETTE = 2;
    public static final int TRANSACTION_BLACKJACK = 3;

    /** Indica el motiu de la transaccio*/
    private int type;

    /** Context del missatge*/
    private String context;

    /** Identificador del missatge*/
    private double ID;

    /** Increment monetari de la transaccio, pot ser positiu o negatiu*/
    private long gain;

    /** Instant que s'ha produit la transaccio*/
    private Timestamp time;

    /** Usuari que reb la transaccio*/
    private String username;

    /** Password de l'usuari que reb la transaccio. Aquest camp no es estrictament necesari*/
    private String password;

    /** Indica si la transaccio s'ha efectuat satisfactoriament*/
    private boolean transactionOk;

    /**
     * Crea una nova transaccio per a un usuari
     * @param context contexte de la transaccio
     * @param username nom de l'usuari al que va dirigida la transaccio
     * @param gain increment o decrement monetari que reb l'usuari
     * @param type motiu de la transaccio.
     */
    public Transaction(String context, String username, long gain, int type) {
        this.context = context;
        this.gain = gain;
        this.type = type;
        this.username = username;
        this.transactionOk = false;
        ID = Math.random();
    }

    /**
     * Crea una nova transaccio anònima
     * @param gain increment o decrement monetari que reb l'usuari
     * @param time instant de temps quan es produeix la transaccio
     * @param type motiu de la transaccio.
     */

    public Transaction(long gain, Timestamp time, int type) {
        this.gain = gain;
        this.time = time;
        this.type = type;
        this.transactionOk = false;
        ID = Math.random();
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public double getID() {
        return ID;
    }

    public long getGain() {
        return gain;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public int getType() {
        return type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = (String) Seguretat.encripta(password);
    }
    public boolean isTransactionOk() {
        return transactionOk;
    }

    public void setTransactionOk(boolean transactionOk) {
        this.transactionOk = transactionOk;
    }
    public void setType(int type) {
        this.type = type;
    }
}
