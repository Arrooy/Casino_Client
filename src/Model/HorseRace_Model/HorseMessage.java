package Model.HorseRace_Model;
import Network.Message;

/**Missatge respecte al joc dels cavalls
 * /*
 * HorseMessage getContext()-
 *
 *     TASK           |STRING                |DIRECTION  |DESCRIPTION
 * -------------------------------------------------------------------------------------------------------------------------------------
 *     Join            HORSES-Connection       C->S        Client wants to join game
 *     Leave           HORSES-Disconnection    C->S        Client wants to leave game
 *     Bet             HORSES-Bet              C->S        Client wants to place a bet
 *     FinishMessage   HORSES-Finished         C->S        Client's race has finished
 *     Result          HORSES-Result           S->C        Server is sending a client their prize, and informing about the winning horse
 *     Countdown       HORSES-Countdown        S->C        Server informs a client about the time remaining to begin a race
 *     Schedule        HORSES-Schedule         S->C        Server sends the race schedule for reproducing the race
 *     BetConfirm      HORSES-BetConfirm       S->C        Server sends bet confirmation
 *     WalletRequest   HORSES-WalletRequest    C->S->C     Client asks for the user's wallet, server returns the wallet
 *
 *  */
public class HorseMessage extends Message {
    private String option;
    private HorseBet horseBet;
    private HorseSchedule horseSchedule;
    private long timeForRace;
    private HorseResult horseResult;
    private long wallet;
    private double ID;

    public void setID(double ID) {
        this.ID = ID;
    }

    public HorseMessage(long wallet){
        option = "WalletRequest";
        this.wallet = wallet;
    }

    /**
     * Constructor utilitzat per enviar una aposta o nformar sobre l'estat d'ella
     * @param horseBet aposta que es vol realitzar
     * @param option String que permet identificar el tipus de missatge
     */
    public HorseMessage(HorseBet horseBet, String option){
        this.horseBet = horseBet;
        this.option = option;
        this.horseSchedule = null;
        this.horseResult = null;
    }
    /**
     * Constructor utilitzat per enviar la carrera als clients
     * @param horseSchedule objecte que conte l'informacio per reproduir una carrera
     * @param option String que permet identificar el tipus de missatge
     */
    public HorseMessage(HorseSchedule horseSchedule, String option){
        this.horseBet = null;
        this.option = option;
        this.horseSchedule = horseSchedule;
        this.horseResult = null;

    }

    /**
     * Constructor utilitzat per informar als clients del temps que queda per començar una carrera
     * @param timeForRace temps per començar la carrera en mlisegons
     * @param option String que permet identificar el tipus de missatge
     */
    public HorseMessage(long timeForRace, String option){
        this.timeForRace = timeForRace;
        this.option = option;
        this.horseSchedule = null;
        this.horseBet = null;
        this.horseResult = null;
    }

    /**
     * Constructor utilitzat per enviar el resultat d'una carrera i una aposta
     * @param result Conte el cavall guanyador i el premi de l'aposta
     * @param option String que permet identificar el tipus de missatge
     */
    public HorseMessage(HorseResult result, String option){
        this.horseBet = null;
        this.option = option;
        this.horseSchedule = null;
        this.horseResult = result;
    }

    /**Retorna el temps que queda per començar a reproduir la carrera*/
    public long getTimeForRace(){
        return timeForRace;
    }

    public long getWallet(){
        return wallet;
    }

    @Override
    public String getContext() {
        return "HORSES-" + option;
    }

    @Override
    public double getID() {
        return this.ID;
    }

    public HorseResult getHorseResult(){
        return this.horseResult;
    }

    public void setHorseSchedule(HorseSchedule horseSchedule) {
        this.horseSchedule = horseSchedule;
    }

    public HorseBet getHorseBet(){
        return this.horseBet;
    }

    public HorseSchedule getHorseSchedule(){
        return this.horseSchedule;
    }
}
