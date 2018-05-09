package Model;

import Network.Message;
import Network.Transmission;

import java.util.LinkedList;

/** Missatge orientat a la grafica de l'evolucio de la cartera de l'usuari*/

public class WalletEvolutionMessage extends Message {

    /** Defineix el identificador del missatge. Util per quan s'envia amb el networkManager*/
    private final double ID;

    /** Context del missatge*/
    private String context;

    /** Llista de la transactions del usuari*/
    private LinkedList <Transaction> transactions;

    public WalletEvolutionMessage(){
        //Es defineix un identificador aleatori
        ID = Math.random();

        //Es defineix el contexte del missatge
        context = Transmission.CONTEXT_WALLET_EVOLUTION;

        //S'inicialitza la llista de transaccions
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
