package Model.RouletteModel;

import Network.Message;

public class RouletteBetMessage extends Message {

    private double ID;
    private String context;

    private long bet;
    private int cellID;

    private boolean successful;

    public RouletteBetMessage(long bet, int cellID) {
        this.bet = bet;
        this.cellID = cellID;

        context = "rouletteBet";
        ID = Math.random();

        successful = false;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getCellID() {
        return cellID;
    }

    public long getBet() {
        return bet;
    }

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public double getID() {
        return ID;
    }
}
