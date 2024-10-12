package classes.persistencia;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;

/**
 * Representa un gestor de Ranquing
 */
public class Gestor_Ranquing implements IConversor {
    private static Gestor_Ranquing instancia;

    /**
     * Constructor de la classe Gestor_Ranquing.
     */
    private Gestor_Ranquing() {
    }

    /**
     * Retorna la instancia del gestor de ranquing.
     *
     * @return Gestor_Ranquing.
     */
    static public Gestor_Ranquing getInstancia() {
        if (instancia == null) instancia = new Gestor_Ranquing();
        return instancia;
    }

    /**
     * Guarda l'string dins del fitxer fileName corresponent.
     * 
     * @param idkenken identificador del kenken del ranquing a escriure.
     * @param user nom de l'usuari.
     * @param string string a escriure.
     */
    public void Escriure(Integer idkenken, String user, String string) {
        try {
            // Calculate the file where the ranking is located
            int fileNumber = idkenken / 10;
            // Get the string of the corresponding ranking file
            // Esto es lo que habia antes String fileName = "subgrup-prop42.1/ENTREGA III/EXE/recursos/ranquings" + fileNumber + ".json";
            String fileName = "../../EXE/recursos/ranquings" + fileNumber + ".json";
            File file = new File(fileName);
            JsonArray jsonArray;
        
            // If the file exists, read the existing JSON array from it
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                jsonArray = JsonParser.parseString(content).getAsJsonArray();
            } else {
                // If the file doesn't exist, create a new JSON array
                jsonArray = new JsonArray();
            }
        
            // Parse the string into a JsonObject
            JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
        
            // Add the JsonObject to the JSON array
            jsonArray.add(jsonObject);
        
            // Write the JSON array to the file
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            fileWriter.write(gson.toJson(jsonArray));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error al escriure el fitxer.");
            e.printStackTrace();
        }
    }
    
    /**
     * Retorna l'string del fitxer fileName corresponent.
     * 
     * @param idkenken identificador del kenken del ranquing a llegir.
     * @param user nom de l'usuari.
     */
    public String Llegir(Integer idkenken, String user) {
            

            // Calcular el archivo en el que se encuentra el ranking
            int fileNumber = idkenken / 10;
            // Obtener el string del ranking del archivo correspondiente
            String fileName = "../../EXE/recursos/ranquings" + fileNumber + ".json";
            File file = new File(fileName);
            
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
                String linea;
                StringBuilder contentBuilder = new StringBuilder();

                while ((linea = fileReader.readLine()) != null) {
                    contentBuilder.append(linea);
                }
                JsonReader reader = new JsonReader(new StringReader(contentBuilder.toString()));
                JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        JsonElement idElement = jsonObject.get("idkenken");
                        if (idElement != null && idkenken.equals(idElement.getAsInt())) {
                            return jsonObject.toString();
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer el archivo: " + e.getMessage());
            }
  
        return null;
    }

    /**
     * Elimina la part identificada pel idkenken del fitxer fileName corresponent.
     * 
     * @param idkenken identificador del ranquing a eliminar.
     * @param user nom de l'usuari.
     */
    public void Eliminar(Integer idkenken, String user) {
        try {
            int fileNumber = idkenken / 10;
            // Get the string of the corresponding ranking file
            String fileName = "../../EXE/recursos/ranquings" + fileNumber + ".json";
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(file.toPath()));
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(content).getAsJsonArray();
    
            // Find the object with the given idkenken and remove it
            for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext();) {
                JsonObject jsonObject = iterator.next().getAsJsonObject();
                if (jsonObject.get("idkenken").getAsInt() == idkenken) {
                    iterator.remove();
                    break;
                }
            }
    
            // Write the modified JSON back to the file
            Files.write(file.toPath(), jsonArray.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error when removing the line.");
        }
    } 
    
    public String[] getInfoRanking(Integer id, String user) throws NumberFormatException{
        
        try {
            String rank = Llegir(id, user);
            JsonObject jsonObject = JsonParser.parseString(rank).getAsJsonObject();
        
            JsonArray rankingsArray = jsonObject.getAsJsonArray("rankings");
            String[] result = null;
            for (int i = 0; i < rankingsArray.size(); i++) {
                JsonObject rankingObject = rankingsArray.get(i).getAsJsonObject();
                String username = rankingObject.get("username").getAsString();
                String tiempo = rankingObject.get("tiempo").getAsString();

                if (username.equals(user)) {
                    result = new String[2];
                    result[0] = Integer.toString(i + 1);
                    result[1] = tiempo;
                    break; 
                }
            }
            return result;
        }catch(NumberFormatException e){
            System.out.println("no esta el jugador al ranquing");
            return null;
        }
        
    }
}