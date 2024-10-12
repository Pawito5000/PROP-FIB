package classes.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.nio.file.DirectoryStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

/**
 * Representa un gestor de Kenken.
 */
public class Gestor_Kenken implements IConversor{
    private static Gestor_Kenken instancia;

    /**
     * Constructor de la classe Gestor_Kenken.
     */
    private Gestor_Kenken() {
    }

    /**
     * Retorna la instancia del gestor de kenken.
     *
     * @return Gestor_Kenken.
     */
    static public Gestor_Kenken getInstancia() {
        if (instancia == null) instancia = new Gestor_Kenken();
        return instancia;
    }

    /**
     * Guarda l'string dins del fitxer fileName corresponent.
     * 
     * @param idKenken identificador del kenken a escriure.
     * @param user nom de l'usuari.
     * @param string string a escriure.
     */
    public void Escriure(Integer idkenken, String user, String string) {
        try {
            //obtenir el nom del fitxer corresponent
            String fileName = "../../EXE/recursos/kenkensusers/kenkens" + user + ".json";
            File file = new File(fileName);
            JsonArray jsonArray;
        
            //Si el fitxer existeix, llegir-lo
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                jsonArray = JsonParser.parseString(content).getAsJsonArray();
            } else {
                //Si no existeix, crear-lo
                jsonArray = new JsonArray();
            }
        
            //Convertir l'string a un JsonObject
            JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();
        
            //Afegir l'objecte en un array
            jsonArray.add(jsonObject);
        
            //Escriure l'array al fitxer
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
     * @param idkenken identificador del kenken a llegir.
     * @param user nom de l'usuari.
     */
    public String Llegir(Integer idkenken, String user) {
            int fileNumber = idkenken / 10;
            String fileName;
            if (user.equals("bd"))fileName  = "../../EXE/recursos/kenkensbd/kenkens" + fileNumber + ".json";
            else fileName = "../../EXE/recursos/kenkensusers/kenkens" + user + ".json";
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
                        JsonElement idElement = jsonObject.get("id");
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
     * @param idkenken identificador del kenken a eliminar.
     * @param user nom de l'usuari.
     */
    public void Eliminar(Integer idkenken, String user) {
        try {
            //Obtenir el fitxer corresponent
            String fileName = "../../EXE/recursos/kenkensuser/kenkens" + user + ".json";
            File file = new File(fileName);
            String content = new String(Files.readAllBytes(file.toPath()));
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parseString(content).getAsJsonArray();
    
            //Trobar l'objecte amb l'idkenken corresponent i eliminar-lo
            for (Iterator<JsonElement> iterator = jsonArray.iterator(); iterator.hasNext();) {
                JsonObject jsonObject = iterator.next().getAsJsonObject();
                if (jsonObject.get("id").getAsInt() == idkenken) {
                    iterator.remove();
                    break;
                }
            }
    
            //Escriure l'array al fitxer
            Files.write(file.toPath(), jsonArray.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Error when removing the line.");
            e.printStackTrace();
        }
    } 
    
    public String llegirall(String nom) {
        String rutaCarpeta = "../../EXE/recursos/kenkensbd";
        JsonArray jsonArray = new JsonArray();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(rutaCarpeta), "*.json")) {
            for (Path entry : stream) {
                String contenido = new String(Files.readAllBytes(entry), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(contenido);
                if (jsonElement.isJsonObject()) {
                    jsonArray.add(jsonElement.getAsJsonObject());
                } else if (jsonElement.isJsonArray()) {
                    for (JsonElement elem : jsonElement.getAsJsonArray()) {
                        jsonArray.add(elem.getAsJsonObject());
                    }
                }
            }
        } catch (IOException e) {
            return "";
        } catch (JsonIOException | JsonSyntaxException e) {
            return "";
        }
        return jsonArray.toString();
    }

    public String llegirkenkenuser(String nom) {
        
        String rutafile = "../../EXE/recursos/kenkensusers/kenkens" + nom + ".json";
        
        try {
            // Verificar si el archivo existe
            Path archivo = Paths.get(rutafile);
            if (!Files.exists(archivo)) {
                return "";
            }
            // Leer el contenido del archivo
            String content = Files.readString(archivo);
            // Analizar el contenido como JSON y devolverlo como una cadena
            return content;
        } catch (IOException e) {
            return "";
        }
    }
}