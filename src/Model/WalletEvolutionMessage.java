package Model;

import Network.Message;
import Network.Transmission;

import java.util.LinkedList;

public class WalletEvolutionMessage extends Message {

    /** Defineix el identificador del missatge. Util per quan s'envia amb el networkManager*/
    private final double ID;

    /** Context del missatge*/
    private String context;

    private LinkedList <Transaction> transactions;

    public WalletEvolutionMessage(){
        ID = Math.random();
        context = Transmission.CONTEXT_WALLET_EVOLUTION;

        transactions = new LinkedList<>();
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public double getID() {
        return ID;
    }

    public LinkedList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(LinkedList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
