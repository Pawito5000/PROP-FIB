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
 * Representa un gestor d'Usuari.
 */
public class Gestor_Usuari implements IConversor {
    private static Gestor_Usuari instancia;

    /**
     * Constructor de la classe Gestor_Usuari.
     */
    private Gestor_Usuari() {
    }

    /**
     * Retorna la instancia del gestor d'usuari.
     *
     * @return Gestor_Usuari.
     */
    static public Gestor_Usuari getInstancia() {
        if (instancia == null) instancia = new Gestor_Usuari();
        return instancia;
    }

    private static int valAdjs(char c) {
        if (c >= 'A' && c <= 'Z') return c - 'A';
        else if (c >= 'a' && c <= 'z') return c - 'a';
        return c;
    }

    /**
     * Guarda l'string dins del fitxer fileName corresponent.
     * 
     * @param idUsuari identificador del usuari a escriure.
     * @param user nom de l'usuari.
     * @param string string a escriure.
     */
    public void Escriure(Integer idUsuari, String user, String string) {
        try {
            int fileNumber = valAdjs(user.charAt(0)) % 3;
            // Get the string of the corresponding ranking file
            String fileName = "../../EXE/recursos/usuaris" + fileNumber + ".json";
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
    * @param idusuari identificador de l'usuari a llegir.
    * @param user nom de l'usuari.
    */
   public String Llegir(Integer idusuari, String user)
   {
       BufferedReader fileReader = null;
        try {
            int fileNumber = valAdjs(user.charAt(0)) % 3;
            // Obtener el string del ranking del archivo correspondiente
            String fileName = "../../EXE/recursos/usuaris" + fileNumber + ".json";
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
                    JsonElement username = jsonObject.get("nom");
                    if (username != null && user.equals(username.getAsString())) {
                        return jsonObject.toString();
                    }
                }
            }
            fileReader.close();
            System.out.println("El elemento no se encuentra en el archivo.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo.");
            e.printStackTrace();
        }
        return null;
   }

    /**
     * Elimina la part identificada pel idkenken del fitxer fileName corresponent.
     * 
     * @param idusuari identificador de l'usuari a llegir.
     * @param user nom de l'usuari.
     */
    public void Eliminar(Integer idusuari, String user) {
        try {
            int fileNumber = valAdjs(user.charAt(0)) % 3;
            // Get the string of the corresponding ranking file
            String fileName = "../../EXE/recursos/usuaris" + fileNumber + ".json";
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(file.toPath()));
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(content).getAsJsonArray();
    
            // Find the object with the given idkenken and remove it
            for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext();) {
                JsonObject jsonObject = iterator.next().getAsJsonObject();
                if (jsonObject.get("nom").getAsString().equals(user)) {
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