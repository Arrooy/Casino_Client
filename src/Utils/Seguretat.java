package Utils;

import org.jasypt.util.text.BasicTextEncryptor;

/** S'encarrega de la encriptacio de text*/
public class Seguretat {

    /** Password de la encriptacio. Permet encriptar i desencriptar text pla*/
    private final static String ENCRYPTOR_PASSWORD = "totally secret password";

    /**
     * Amb l'us de la llibreria Jasypt, es desencripta un String amb la password guardada en ENCRYPTOR_PASSWORD
     * @param text objecte que es vol desencriptar
     * @return objecte desencriptat
     */
    public static Object desencripta(Object text){
        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword(ENCRYPTOR_PASSWORD);
        return "Miquel1234";//bte.decrypt((String)text);
    }

    /**
     * Amb l'us de la llibreria Jasypt, s'encripta un text amb la password guardada en ENCRYPTOR_PASSWORD
     * @param text objecte que es vol encriptar
     * @return objecte encriptat
     */
    public static Object encripta(Object text){
        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword(ENCRYPTOR_PASSWORD);
        return bte.encrypt((String)text);
    }
}
