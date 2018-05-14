package Utils;


/**Classe que permet comptar enrere, en arribar a 0 para de comptar*/
public class Countdown extends Thread {
    private long count;
    private long startTime;
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
            System.out.println("Countdown error");
        }
    }

    public long getCount(){
        return this.count;
    }

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

    public boolean isCounting() {
        return isCounting;
    }
}
