package Controlador;

import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Utils{

    public final static String CONFIG_FILENAME = "config.json";

    /**
     * LlegirConfiguracio rep un conjunt d'identificadors i
     * retorna la informació que contenen aquests identificadors del json objectiu
     * @param campsJson Camps que es volen llegir d'un json
     * @return Informació dels camps del json sol·licitats
     * @throws FileNotFoundException En cas de no trobar el json especificat saltarà aquesta excepció
     */

    public String[] llegirConfiguracio(String ... campsJson) throws FileNotFoundException {

        String [] informacioExtreta = new String[campsJson.length];

        //Es llegeix l'arxiu json fins el caracter '}'.
        //Al acabar la lectura, s'afegeix al final del string resultant un '}'
        JSONObject jsonObject =  new JSONObject((new Scanner(new File("data/" + CONFIG_FILENAME)).useDelimiter("}").next()) + "}");

        int index = 0;

        //Per a tots els camps especificats s'extreu la informacio del json
        for(String camp : campsJson){
            informacioExtreta[index++] = jsonObject.has(camp) ? jsonObject.getString(camp) : null;
        }

        return informacioExtreta;
    }
}