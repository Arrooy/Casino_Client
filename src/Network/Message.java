package Network;

import java.io.Serializable;

/**
 * Pare de totes les comunicacions entre client - servidor
 * Obliga a que tots els elements que s'envien tinguin un ID i
 * un context.
 *
 * L'identifiador serveix per a facilitar el efecte ping-pong de la comunicació
 * ja que al enviar un missatge al servidor, aquest en la majoria de casos ha de
 * respondre, i per identificar la resposta en mig d'altres possibles missatges,
 * s'utilita un identificador aleatori que es comparteix entre missatge enviat i resposta.
 *
 * Per altre banda, el context serveix per a identificar la finalitat del missatge,
 * ja que tots els missatges s'envien per un mateix canal, el context és qui s'encarrega
 * d'informar sobre si el missatge tracta sobre un intent de registrar-se l'usuari, o
 * bé una aposta del joc dels cavalls, per exemple.
 */
public abstract class Message implements Serializable {
    /** Getter del context del missatge */
    public abstract String getContext();

    /** Getter de l'identificador del missatge */
    public abstract double getID();
}