package Model;

import Network.Message;
import Utils.Seguretat;

import java.sql.Timestamp;

public class Transaction extends Message {

    public static final int TRANSACTION_DEPOSIT = 0;
    public static final int TRANSACTION_HORSES = 1;
    public static final int TRANSACTION_ROULETTE = 2;
    public static final int TRANSACTION_BLACKJACK = 3;

    private String context;
    private double ID;
    private long gain;
    private Timestamp time;
    private int type;
    private String username;
    private String password;

    private boolean transactionOk;

    public Transaction(String context, String username, long gain, int type) {
        this.context = context;
        this.gain = gain;
        this.type = type;
        this.username = username;
        this.transactionOk = false;
        ID = Math.random();
    }

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

    public void saveToDatabase() {
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
