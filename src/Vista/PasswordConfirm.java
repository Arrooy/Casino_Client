package Vista;

/**Interficie que s'ha d'implementar quan l'usuari vol tenir una contrasenya nova
 * per tal que es puguin fer les pertinents comprovacions sobre aquesta*/
public interface PasswordConfirm {

    /** Mostra un missatge d'error*/
    void passwordKO(String message);

    /** Retorna la contrasenya que es vol escollir i la de confirmació*/
    boolean getPasswordChangeRequest();

    /** Fixa el valor del JProgress bar
     * Depenent del "strength" actualitza color i text de l'indicador de seguretat*/
    void setStrength(int strength);

    /** Controla la visibilitat del missatge d'error*/
    void manageError(boolean error);

    /** Retorna la contrasenya que l'usuari ha introduit en el camp New Password*/
    String getNewPassword();

    /** Controla el funcionament del boto que et permet sotmetre la nova contrasenya*/
    void canConfirm(boolean ok);
}
