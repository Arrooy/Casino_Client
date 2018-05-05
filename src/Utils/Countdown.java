package Utils;


/**Classe que permet comptar enrere, en arribar a 0 para de comptar*/
public class Countdown extends Thread {
    private long count;
    private boolean isCounting;

    private static final int COUNTRATE = 50;

    public Countdown(){
        this.isCounting = false;
        this.count = 0;
    }


    @Override
    public void run() {
        try {
            while (true) {
                if (isCounting) {
                   sleep(50);
                   this.count-=50;
                   if(this.count <= 0){
                       this.count = 0;
                       this.isCounting = false;
                   }
                }
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
    }

    /**Metode per reiniciar el comptatge*/
    public void newCount(long count){
        this.count = count;
        this.isCounting = true;
    }

    public boolean isCounting() {
        return isCounting;
    }
}
