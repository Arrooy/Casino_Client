package Model;

import java.util.LinkedList;

public class BlackJack {

    private Baralla baralla;
    private User jugador;
    //private LinkedList<Card>


    public BlackJack(User jugador){
        this.jugador = jugador;
        baralla = new Baralla();
    }

    public LinkedList<String> getNomCartes(){
        return baralla.getNomCartes();
    }

    public void addCard(Card cartaResposta) {
        if(cartaResposta.isForIA()){
            System.out.println("Carta for IA RECIEVED ON MODEL");
        }else{
            System.out.println("Carta for USER RECIEVED ON MODEL");
        }
    }
}
