package Utils;

import org.jasypt.util.text.BasicTextEncryptor;

public class Seguretat {

    /** Password de la encriptacio. Permet encriptar i desencriptar text pla*/
    private final static String ENCRYPTOR_PASSWORD = "totally secret password";

    //Amb l'us de la llibreria Jasypt, es desencripta un String amb la password guardada en ENCRYPTOR_PASSWORD
    public static Object desencripta(Object text){
        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword(ENCRYPTOR_PASSWORD);
        return bte.decrypt((String)text);
    }

    //Amb l'us de la llibreria Jasypt, s'encripta un text amb la password guardada en ENCRYPTOR_PASSWORD
    public static Object encripta(Object text){
        BasicTextEncryptor bte = new BasicTextEncryptor();
        bte.setPassword(ENCRYPTOR_PASSWORD);
        return bte.encrypt((String)text);
    }

}
