package Utils;

import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * JsonManager gestiona tota interaccio amb arxius json.
 * Pot escriure i llegir fitxers json.
 * Tambe permet escriure, borrar i llegir camps especifics d'un json determinat.
 * Al utilitzar les funcions escriure i llegir, si al nom del camp que volem llegir li afegim al inici un #,
 * el escriptor/lector ho interpretara com que en aquell camp hi ha o ha d'haver-hi text encriptat.
 */

public class JsonManager {

    /** Nom del fitxer objectiu de la llibreria*/
    private final static String CONFIG_FILENAME = "config.json";

    /** Nom del camp del Json que guarda si l'usuari ha selecionat rememberLogIn*/
    public final static String BOOLEAN_R = "IsLoggedIn";

    /** Nom del camp del Json que guarda el username del usuari*/
    public final static String USERNAME_R = "LogUsername";

    /**
     * Nom del camp del Json que guarda la password del usuari
     * El camp conte un asterisc perque el camp s'ha de treballar amb encriptacio
     */

    public final static String PASSWORD_R = "#LogPassword";

    /**
     * LlegirJson rep un conjunt d'identificadors i
     * retorna la informació que contenen aquests identificadors del json objectiu.
     * En el cas de trobar un camp que contingui un #, significa que aquest camp ha de desencriptar-se.
     * @param campsJson Camps que es volen llegir d'un json
     * @return Informació dels camps del json sol·licitats
     */

    public static Object[] llegirJson(String ... campsJson){

        Object [] informacioExtreta = new Object[campsJson.length];

        //Es llegeix l'arxiu json
        JSONObject jsonObject;

        try {
            //S'obte el json del fitxer CONFIG_FILENAME
            jsonObject = JsonManager.getJSONObject();

            int index = 0;

            //Per a tots els camps especificats s'extreu la informacio del json
            for(String camp : campsJson){
                //Si la informacio esta encriptada, la llegim i la desencriptem
                if(camp.contains("#")){
                    camp = camp.substring(camp.indexOf("#"));
                    informacioExtreta[index++] = jsonObject.has(camp) ? Seguretat.desencripta(jsonObject.get(camp)) : null;
                }else{
                    //Si la informacio no esta encriptada, es guarda de manera standard
                    informacioExtreta[index++] = jsonObject.has(camp) ? jsonObject.get(camp) : null;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Fitxer de configuracio no trobat");
        }

        return informacioExtreta;
    }

    /**
     * Afegeix un nou camp al JSON "CONFIG_FILENAME".
     * @param nomCamp camp per afegir al json. Si el camp conte un #, aquest s'afegira encriptadament.
     * @param contingut contingut d'aquest camp. El tipus d'aquest esta restringit per les possibilitats
     * de la llibreria json.
     * @throws FileNotFoundException En cas de no trobar-se l'arxiu CONFIG_FILENAME es llença aquesta excepcio
     */

    public static void afegeixCamp(String nomCamp, Object contingut) throws FileNotFoundException {

        //Es guarda una copia actual de JSON i se li afegeix el nom camp

        //Aquest s'encripta si es necesari
        if(nomCamp.contains("#")) {

            //S'elimina el # del camp, per a guardar nomes el text
            nomCamp = nomCamp.substring(nomCamp.indexOf("#"));
            contingut = Seguretat.encripta(contingut);
        }

        //S'obte el json del fitxer CONFIG_FILENAME i se li afegeix el camp
        JSONObject nouJson = JsonManager.getJSONObject().put(nomCamp,contingut);

        //Try - catch - resources per a crear el fitxer
        try (FileWriter file = new FileWriter("data/" + CONFIG_FILENAME)) {

            //S'afegeix el contingut guardat en l'auxiliar nouJson
            file.write(nouJson.toString(1));
        }catch (IOException e){
            System.out.println("[Error]: Impossible sobreescriure el fitxer " + CONFIG_FILENAME);
        }
    }

    /**
     * Elimina un camp del JSON "CONFIG_FILENAME"
     * @param camps camps que es volen eliminar del json
     * @throws FileNotFoundException En cas de no trobar-se l'arxiu CONFIG_FILENAME es llença aquesta excepcio
     */

    public static void eliminaCamps(String ... camps) throws FileNotFoundException {

        //Es guarda una copia actual de JSON i se li elimina el camp desitjat si aquest existeix en el json
        JSONObject nouJson = JsonManager.getJSONObject();
        for(String campPerBorrar : camps) {
            if(nouJson.has(campPerBorrar))
                nouJson.remove(campPerBorrar);
        }

        //Try - catch - resources per a crear el fitxer
        try (FileWriter file = new FileWriter("data/" + CONFIG_FILENAME)) {

            //S'afegeix el contingut guardat en l'auxiliar nouJson
            file.write(nouJson.toString(1));
        }catch (IOException e){
            System.out.println("[Error]: Impossible sobreescriure el fitxer " + CONFIG_FILENAME);
        }
    }

    private static JSONObject getJSONObject() throws FileNotFoundException {
        //Es llegeix l'arxiu json sencer i es retorna en forma de JSONObject
        return  new JSONObject((new Scanner(new File("data/" + CONFIG_FILENAME)).useDelimiter("}").next()) + "}");
    }

    /**
     * Afegeix els 3 camps per a guardar el logIn d'un usuari. D'aquesta forma, la proxima vegada que s'obri el
     * client, el login sera automatic.
     * @param username username que es vol guardar al json
     * @param password la passwords que es vol guardar al json
     */

    public static void addRemember(String username,String password) {
        try {
            afegeixCamp(BOOLEAN_R, true);
            afegeixCamp(USERNAME_R,username);
            afegeixCamp(PASSWORD_R,password);
        }catch (IOException e){
            //No sha trobat \ no es pot escriure en el fitxer de configuracio
        }
    }

    /** Elimina els 3 camps per a guardar el logIn d'un usuari*/
    public static void removeRemember() {
        try{
            eliminaCamps(BOOLEAN_R,USERNAME_R,PASSWORD_R);
        }catch (IOException e){
            //No sha trobat \ no es pot escriure en el fitxer de configuracio
        }
    }
}