package Controlador;

import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class JsonManager {

    private final static String CONFIG_FILENAME = "config.json";

    /**
     * LlegirJson rep un conjunt d'identificadors i
     * retorna la informació que contenen aquests identificadors del json objectiu
     * @param campsJson Camps que es volen llegir d'un json
     * @return Informació dels camps del json sol·licitats
     * @throws FileNotFoundException En cas de no trobar el json especificat saltarà aquesta excepció
     */

    public static String[] llegirJson(String ... campsJson) throws FileNotFoundException {

        String [] informacioExtreta = new String[campsJson.length];

        //Es llegeix l'arxiu json fins el caracter '}'.
        //Al acabar la lectura, s'afegeix al final del string resultant un '}'
        JSONObject jsonObject =  JsonManager.getJSONObject();

        int index = 0;

        //Per a tots els camps especificats s'extreu la informacio del json
        for(String camp : campsJson){
            informacioExtreta[index++] = jsonObject.has(camp) ? jsonObject.getString(camp) : null;
        }

        return informacioExtreta;
    }

    /**
     * Afegeix un nou camp al JSON "CONFIG_FILENAME"
     * @param nomCamp camp per afegir al json
     * @param contingut contingut d'aquest camp. El tipus d'aquest esta restringit per les possibilitats
     * @throws FileNotFoundException En cas de no trobar-se l'arxiu CONFIG_FILENAME es llença aquesta excepcio
     */

    public static void afegeixCamp(String nomCamp, Object contingut) throws FileNotFoundException {

        //Es guarda una copia actual de JSON i se li afegeix el nom camp
        JSONObject nouJson = JsonManager.getJSONObject().put(nomCamp,contingut);

        //Try - catch - resources per a crear el fitxer
        try (FileWriter file = new FileWriter("data/" + CONFIG_FILENAME)) {

            //S'afegeix el contingut guardat en l'auxiliar nouJson
            file.write(nouJson.toString(1));
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("[Error]: Impossible sobreescriure el fitxer " + CONFIG_FILENAME);
        }
    }

    private static JSONObject getJSONObject() throws FileNotFoundException {

        //Es llegeix l'arxiu json sencer i es retorna en forma de JSONObject
        return  new JSONObject((new Scanner(new File("data/" + CONFIG_FILENAME)).useDelimiter("}").next()) + "}");
    }
}