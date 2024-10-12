package classes.domini;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.util.AbstractMap;
import java.util.HashMap;
/**
 * Representa un validador de Kenken.
 * 
 * El validador de Kenken te un conjunt de validacions.
 * Tambe proporciona metodes per validar les dades del kenken.
 */
public class Validar {
    private static Validar instancia = null;

    private Integer minMida = 2;
    private Integer maxMida = 12;
    private Integer minCjt = 2;
    private Integer minOp = 0;
    private Integer maxOp = 6;

    private HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean> matriu1;
    private HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean> matriu2;


    /**
     * Constructor de la classe Validar.
     */
    private Validar() {
        
    }

    /**
     * Retorna la instancia del validador de Kenken.
     * 
     * @return Validar
     */
    public static synchronized Validar getInstancia() {
        if (instancia == null)
            instancia = new Validar();
        return instancia;
    }

    /**
     *Retorna true si el Kenken es valid, false altrament.
     * 
     * @param KenkenJSON JSON del Kenken.
     * @return boolean
     */
    public boolean validarKenken(String KenkenJSON){
        //assumeixo que rebo un string tipo aquest: String jsonString = "{\"N\":4,\"R\":3,\"regions\":[{\"oper\":1,\"result\":10,\"e\":3,\"coordenades\":[{\"x\":1,\"y\":1},{\"x\":1,\"y\":2,\"cont\":3},{\"x\":2,\"y\":1},{\"x\":2,\"y\":2}]},{\"oper\":2,\"result\":2,\"e\":2,\"coordenades\":[{\"x\":3,\"y\":1},{\"x\":3,\"y\":2}]},{\"oper\":4,\"result\":2,\"e\":2,\"coordenades\":[{\"x\":4,\"y\":3},{\"x\":4,\"y\":4}]}]}";
        //Gson gson = new Gson();
        //convertir la cadena json a objecte json
        //JsonObject kenkenObject = gson.fromJson(KenkenJSON, JsonObject.class);

        JsonObject kenkenObject = new Gson().fromJson(KenkenJSON, JsonObject.class);
        matriu1 = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean>();
        
        //Primer valor es la mida. aquesta s'ha de trobar entre 2-9
        Integer mida = kenkenObject.get("N").getAsInt();
        if(mida == null || mida < minMida || mida > maxMida) throw new IllegalArgumentException("Error: la mida del Kenken ha de ser entre 2 i 13");

        //Segon valor es el numero total de conjunts de cella, minim 1 maxim mida*mida
        Integer numCjtCela = kenkenObject.get("R").getAsInt();
        Integer totalCeles = mida * mida;
        if(numCjtCela == null || numCjtCela < minCjt || numCjtCela > totalCeles) throw new IllegalArgumentException("Error: el nombre de conjunts de cella ha de ser entre 2 i " + totalCeles);

        //aqui falta crear la "matriu" buida per anar omplint despres amb quines celes ja hem fet servir
        matriu1 = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean>();

        //ara agafem el Json de les regions per iterar sobre ell
        JsonArray conjuntsCelesArray = kenkenObject.getAsJsonArray("regions");
        if (conjuntsCelesArray.size() != numCjtCela)
            throw new IllegalArgumentException("Error: el nombre de conjunts de cella no coincideix amb el nombre de regions" + conjuntsCelesArray.size() + " " + numCjtCela);

        //comprovem que realment ens hagin passat la informacio de totes les regions
        //if (conjuntsCelesArray.size() != numCjtCela) return false;  
        for(int i = 0; i < conjuntsCelesArray.size(); i++){
            JsonObject conjuntCelaObj = conjuntsCelesArray.get(i).getAsJsonObject();

            //Vale la estructura de dades de operacio no es correcta del tot pero de moment per comprovar nos sirve xd
            //comprovar que estigui entre 0 i 6 
            Integer operacio = conjuntCelaObj.get("oper").getAsInt();
            if (operacio < minOp || operacio > maxOp)
                throw new IllegalArgumentException("Error: l'operacio ha de ser entre 0 i 6");

            //Agafem el resultat i apart de comprovar que no sigui null no se si falta algo mes xd
            //Integer result = null;
            //Integer result = conjuntCelaObj.get("result").getAsInt();
            if (conjuntCelaObj.get("result").isJsonNull()) 
                throw new IllegalArgumentException("Error: el resultat no pot ser null");

            Integer elements = conjuntCelaObj.get("e").getAsInt();
            //si ja he omplert totes les celes i tinc encara mes conjunts... malament
           
            if(conjuntCelaObj.get("e").isJsonNull() || totalCeles - elements < 0) 
                throw new IllegalArgumentException("Error: el nombre d'elements del conjunt de cella ha de ser entre 1 i " + totalCeles);
            // la divisio i resta nomes de 2 element. la nada nomes 1 elment. suma i multi no tenen limit
            switch (operacio) {
                case 0:
                    if (elements != 1)
                        throw new IllegalArgumentException("Error: l'operacio de divisio ha de tenir 1 element");
                    break;
                case 2:
                    if (elements != 2) 
                        throw new IllegalArgumentException("Error: l'operacio de divisio ha de tenir 2 element");
                    break;
                case 4:
                    if (elements != 2)
                        throw new IllegalArgumentException("Error: l'operacio de divisio ha de tenir 2 element");
                    break;
                default:
                    if (elements < 1 || elements > totalCeles)
                        throw new IllegalArgumentException("Error: l'operacio ha de tenir entre 1 i " + totalCeles + " elements");
            }
            totalCeles = totalCeles - elements;

            //aqui crear una altre "matriu"(num 2) buida de les mateixes dimensions que utilitzarem per saber si les celes es "toquen" o no
            matriu2 = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean>();
            //vale ara agafem el json de les coordenades
            JsonArray celesArray = conjuntCelaObj.getAsJsonArray("coordenades");
            //comprobem que rebem la info de tots els elements o sigui totes les celes que participen en el cjt de celes
            if(celesArray.size() != elements) 
                throw new IllegalArgumentException("Error: el nombre d'elements del conjunt de cella no coincideix amb el nombre de celes" + elements + " " + celesArray.size());
            for (Integer j = 0; j < celesArray.size(); j++) {
                //agafem cada parella de coordenades
                JsonObject celaObject = celesArray.get(j).getAsJsonObject();

                Integer x = celaObject.get("x").getAsInt();
                Integer y = celaObject.get("y").getAsInt();

                //si sortim de la "matriu" error
                if (x == null || y == null || x <= 0 || x > mida || y <= 0 || y > mida) 
                    throw new IllegalArgumentException("Error: la cela " + x + " " + y + " esta fora de la matriu");
                //que no haguem ja utilitzat aquesta cela en un altre conjunt
                AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<Integer, Integer>(x, y);
                if (matriu1.get(coord) != null) 
                    throw new IllegalArgumentException("Error: la cela " + coord + " ja ha estat utilitzada en un altre conjunt");
                else {
                    matriu1.put(coord, true);
                    matriu2.put(coord, true);
                }

                if (celaObject.get("ini") != null && celaObject.get("ini").getAsInt() == 1 && celaObject.get("cont").isJsonNull())
                    throw new IllegalArgumentException("Error: la cela " + coord + " no pot ser inicial i no tenir valor");
            }
        }

        return true;
    }
}