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

/**
 * Representa un gestor de Partida
 */
public class Gestor_Partida implements IConversor {
    private static Gestor_Partida instancia;

    /**
     * Constructor de la classe Gestor_Partida.
     */
    private Gestor_Partida() {
    }

    /**
     * Retorna la instancia del gestor de partida.
     *
     * @return Gestor_Partida.
     */
    static public Gestor_Partida getInstancia() {
        if (instancia == null) instancia = new Gestor_Partida();
        return instancia;
    }

    /**
     * Guarda l'string dins del fitxer fileName corresponent.
     * 
     * @param idpartida identificador de la partida a escriure.
     * @param user nom de l'usuari.
     * @param string string a escriure.
     */
    public void Escriure(Integer idpartida, String user, String string) {
        try {
      
            // Get the string of the corresponding ranking file
            String fileName = "../../EXE/recursos/partides" + user + ".json";
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
     * @param idkenken identificador de la partida a llegir.
     * @param user nom de l'usuari.
     */
    public String Llegir(Integer idkenken, String user)
    {
        BufferedReader fileReader = null;
        try {
            // Obtener el string del ranking del archivo correspondiente
            String fileName = "../../EXE/recursos/partides" + user + ".json";
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("El archivo no existe.");
                throw new IOException();
            }
            fileReader = new BufferedReader(new FileReader(file));
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
                    JsonObject kenkenObject = jsonObject.getAsJsonObject("kenken");
                    JsonElement idElement = kenkenObject.get("id");
                    if (idElement != null && idkenken.equals(idElement.getAsInt())) {
                        return jsonObject.toString();
                    }
                }
            }
            fileReader.close();
            System.out.println("El elemento no se encuentra en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * Elimina la part identificada per l'idpartida del fitxer fileName corresponent.
     * 
     * @param idpartida identificador de la partida a eliminar.
     * @param user nom de l'usuari.
     */
    public void Eliminar(Integer idkenken, String user) {
        try {
            // Get the string of the corresponding ranking file
            String fileName = "../../EXE/recursos/partides" + user + ".json";
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(file.toPath()));
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(content).getAsJsonArray();
    
            // Find the object with the given idkenken and remove it
            for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext();) {
     
                JsonObject jsonObject = iterator.next().getAsJsonObject();
                JsonObject kenkenObject = jsonObject.getAsJsonObject("kenken");
                JsonElement idElement = kenkenObject.get("id");
                if (idElement != null && idkenken.equals(idElement.getAsInt())) {
           
                    iterator.remove();
                    break;
                }
            }
    
            // Write the modified JSON back to the file
            Files.write(file.toPath(), jsonArray.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error when removing the line.");
            e.printStackTrace();
        }
    }
}