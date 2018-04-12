package Model;

import Network.Message;

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

    public Transaction(String context, String username, long gain, int type) {
        this.context = context;
        this.gain = gain;
        this.type = type;
        this.username = username;

        ID = Math.random();
    }

    public Transaction(long gain, Timestamp time, int type) {
        this.gain = gain;
        this.time = time;
        this.type = type;
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
        return 0;
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
}
