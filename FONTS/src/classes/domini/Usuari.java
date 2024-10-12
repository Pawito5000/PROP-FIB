package classes.domini;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Representa un usuari del sistema.
 * 
 * Un usuari te un identificador, un nom i una contrasenya.
 * Tambe proporciona metodes per obtenir i comprovar la contrasenya de l'usuari.
 */
public class Usuari {
    private static Usuari instance;
    private int id;
    private String nom;
    private String contrasenya;
    private static int contador = 0;
    private Integer contKenken;

    /**
     * Constructor de la classe Usuari.
     * 
     * @param nom Nom de l'usuari.
     * @param contrasenya Contrasenya de l'usuari.
     */
    public Usuari(String nom, String contrasenya) {
      
        this.id = contador;
        ++contador;
        this.nom = nom;
        this.contrasenya = contrasenya;
        this.contKenken = 0;
    }

    /**
     * Constructor de la classe Usuari a partir d'un JsonObject.
     * 
     * @param UsuariJSON String en format JSON amb la informacio de l'usuari.
     */
    public Usuari(String UsuariJSON) {
        JsonObject UsuariObject = new Gson().fromJson(UsuariJSON, JsonObject.class);
        this.id = UsuariObject.get("id").getAsInt();
        this.nom = UsuariObject.get("nom").getAsString();
        this.contrasenya = UsuariObject.get("contrasenya").getAsString();
        this.contKenken = UsuariObject.get("contador").getAsInt();
    }

    /**
     * @return JsonObject es genera un Json del objecte Usuari
     */
    public JsonObject toJson() {
        JsonObject UsuariJSON = new JsonObject();
        UsuariJSON.addProperty("id", id);
        UsuariJSON.addProperty("nom", nom);
        UsuariJSON.addProperty("contrasenya", contrasenya);
        UsuariJSON.addProperty("contador", contKenken);
        return UsuariJSON;
    }

    /**
     * Retorna la instancia de l'usuari.
     * 
     * @param nom Nom de l'usuari.
     * @param contrasenya Contrasenya de l'usuari.
     * @return Usuari
     */
    public static Usuari getInstancia(String nom, String contrasenya) {
        if (instance == null) {
            instance = new Usuari(nom, contrasenya);
        }
        return instance;
    }

    /**
     * Retorna l'indentificador de l'usuari.
     * 
     * @return Integer
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna el nom de l'usuari.
     * 
     * @return String
     */
    public String getNom() {
        return nom;
    }

    public void incrementarContador() {

        ++contKenken;
    }

    public Integer getContadorKenken() {
        return contKenken;
    }

    /**
     * Retorna la contrasenya de l'usuari.
     * 
     * @param contrasenya Contrasenya de l'usuari.
     * @return boolean
     */
    public boolean comprobarContrasenya(String contrasenya) {
        return this.contrasenya.equals(contrasenya);
    }
}
