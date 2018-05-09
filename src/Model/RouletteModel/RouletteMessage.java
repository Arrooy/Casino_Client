package Model.RouletteModel;

import Network.Message;

public class RouletteMessage extends Message {

    private final String context;
    private final double ID;

    private double rouletteVel;
    private double ballVel;
    private int winner;
    private double timeTillNext;
    private int shotOff;

    public RouletteMessage(double rouletteVel, double ballVel, int winner, int shotOff) {
        this.rouletteVel = rouletteVel;
        this.ballVel = ballVel;
        this.winner = winner;
        this.shotOff = shotOff;

        context = "roulette";

        ID = Math.random();
    }

    public RouletteMessage(int type) {
        if (type == 0) context = "rouletteConnection";
        else context = "rouletteDisconnection";
        ID = Math.random();
        rouletteVel = 0;
        ballVel = 0;
        winner = 100;
    }

    public int getShotOff() {
        return shotOff;
    }

    public double getTimeTillNext() {
        return timeTillNext;
    }

    public void setTimeTillNext(double timeTillNext) {
        this.timeTillNext = timeTillNext;
    }

    public double getRouletteVel() {
        return rouletteVel;
    }

    public double getBallVel() {
        return ballVel;
    }

    public int getWinner() {
        return winner;
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
