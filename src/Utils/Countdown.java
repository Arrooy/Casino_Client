package Utils;


/**Classe que permet comptar enrere, en arribar a 0 para de comptar*/
public class Countdown extends Thread {

    /**Valor de comptatge*/
    private long count;

    /**Temps d'inici des de l'ultim increment del comptatge*/
    private long startTime;

    /**indcador de l'estat de comptatge*/
    private boolean isCounting;

    private static final int COUNTRATE = 200;

    public Countdown(){
        this.isCounting = false;
        this.count = 0;
        this.start();
    }


    @Override
    public void run() {
        try {
            while(true){
                if(isCounting) {
                    this.count-=(System.currentTimeMillis() - startTime);
                    startTime = System.currentTimeMillis();
                    if(this.count <= 0){
                        this.count = 0;
                        this.isCounting = false;
                    }
                }
                sleep(COUNTRATE);
            }
        } catch (InterruptedException e) {
        }
    }

    /**
     * @return temps que queda per acabar el comptatge en milisegons
     */
    public long getCount(){
        return this.count;
    }

    /**
     * Es para el comptatge
     */
    public void stopCount(){
        this.isCounting = false;
        this.count = 0;
    }

    /**Metode per reiniciar el comptatge*/
    public void newCount(long count){
        this.count = count;
        this.startTime = System.currentTimeMillis();
        this.isCounting = true;
    }

    /**
     *
     * @return boolea que inidica si s'esta comptant
     */
    public boolean isCounting() {
        return isCounting;
    }
}
