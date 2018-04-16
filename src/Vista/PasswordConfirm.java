package Vista;


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
    /** retorna la contrasenya que l'usuari ha introduit en el camp New Password*/
    String getNewPassword();
    /** Controla el funcionament del boto que et permet sotmetrela nova contrasenya*/
    void canConfirm(boolean ok);




}
