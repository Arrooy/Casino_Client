package Network;

import java.io.Serializable;

/**
 * Pare de totes les comunicacions entre client - servidor
 * Obliga a que tots els elements que s'envien tinguin un ID
 */

public abstract class Message implements Serializable {
    
    public abstract String getContext();
    public abstract double getID();

}
