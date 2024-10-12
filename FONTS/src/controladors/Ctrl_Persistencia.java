package controladors;
import java.io.FileNotFoundException;
import java.io.IOException;

import classes.persistencia.Gestor_Kenken;
import classes.persistencia.Gestor_Ranquing;
import classes.persistencia.Gestor_Usuari;
import classes.persistencia.IConversor;
import classes.persistencia.Gestor_Partida;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
/**
 * Representa el controlador de persistencia.
 */
public class Ctrl_Persistencia {
    private static Ctrl_Persistencia instancia;

    /**
     * Constructor de la classe Ctrl_Persistencia.
     */
    private Ctrl_Persistencia() {
    }

    /**
     * Retorna la instancia del controlador de persistencia.
     *
     * @return Ctrl_Persistencia.
     */
    public static Ctrl_Persistencia getInstancia() {
        if (instancia == null) instancia = new Ctrl_Persistencia();
        return instancia;
    }

    /**
     * Retorna la instancia d'un Kenken en format JSON.
     *
     * @param idkenken identificador del kenken.
     * @param user nom de l'usuari.
     * @return string
     */
    public String ObtenirKenken(Integer idkenken, String user) {
        IConversor conv = Gestor_Kenken.getInstancia();
        return conv.Llegir(idkenken, user);
    }

    /**
     * Retorna la instancia d'un Ranquing en format JSON.
     *
     * @param idkenken identificador del kenken del ranquing.
     * @param user nom de l'usuari.
     * @return string
     */
    public String ObtenirRanquing(Integer idkenken, String user)  {
        IConversor conv = Gestor_Ranquing.getInstancia();
        String rankjson = conv.Llegir(idkenken, user);
        try{
        
            JsonObject jsonObject = JsonParser.parseString(rankjson).getAsJsonObject();
            Integer idk = jsonObject.get("idkenken").getAsInt();

            // Obtener el array de rankings
            JsonArray rankingsArray = jsonObject.getAsJsonArray("rankings");

            // Crear un nuevo JsonArray para almacenar las primeras 10 entradas
            JsonArray firstTenEntries = new JsonArray();

            // Iterar sobre las primeras 10 entradas y agregarlas al nuevo JsonArray
            for (Integer i = 0; i < 10 && i < rankingsArray.size(); i++) {
                firstTenEntries.add(rankingsArray.get(i));
            }

            // Crear un nuevo JsonObject para el resultado
            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("idkenken", idk);
            resultObject.add("rankings", firstTenEntries);

            return resultObject.toString();
        }
        catch(NullPointerException e){
            System.out.println("Error al escriure el fitxer.");
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * Retorna la instancia d'un Usuari en format JSON.
     *
     * @param iduser identificador del l'usuari.
     * @param user nom de l'usuari.
     * @return string
     */
    public String ObtenirUsuari(Integer iduser, String user) {
        IConversor conv = Gestor_Usuari.getInstancia();
        return conv.Llegir(iduser, user);
    }

    /**
     * Retorna la instancia d'una Partida en format JSON.
     *
     * @param idkenken identificador del la partida.
     * @param user nom de l'usuari.
     * @return string
     */
    public String ObtenirPartida(Integer idkenken, String user) {
        IConversor conv = Gestor_Partida.getInstancia();
        return conv.Llegir(idkenken, user);
    }

    /**
     * Retorna la instancia dels kenkens de la base de dades en format JSON.
     * 
     * @param nom nom de la base de dades
     * @return string
     */
    public String llegirall(String nom) {
        Gestor_Kenken gk = Gestor_Kenken.getInstancia();
        return gk.llegirall(nom);
    }

    /**
     * Retorna la instancia d'un Kenken en format JSON.
     * 
     * @param nom nom de l'usuari.
     * @return string
     */
    public String llegirkenkenuser(String nom) {
        Gestor_Kenken gk = Gestor_Kenken.getInstancia();
        return gk.llegirkenkenuser(nom);
    }

     /**
     * Guarda la instancia d'un kenken en format JSON.
     *
     * @param idkenken identificador del kenken a guardar.
     * @param user nom de l'usuari.
     * @param kenken string en format json que representa un kenken.
     */
    public void GuardarKenken(Integer idkenken, String user, String kenken) {
        IConversor conv = Gestor_Kenken.getInstancia();
        conv.Escriure(idkenken, user, kenken);
    }

    /**
     * Guarda la instancia d'un ranquing en format JSON.
     *
     * @param idkenken identificador del kenken del ranquing a guardar.
     * @param user nom de l'usuari.
     * @param ranquing string en format json que representa un ranquing.
     */
    public void GuardarRanquing(Integer idkenken, String user, String ranquing) {
        IConversor conv = Gestor_Ranquing.getInstancia();
        conv.Escriure(idkenken, user, ranquing);
    }

    /**
     * Guarda la instancia d'un Usuari en format JSON.
     *
     * @param iduser Integer identificador de l'usuari
     * @param user nom de l'usuari.
     * @param stringuser string en format json que representa un usuari.
     */
    public void GuardarUsuari(Integer iduser, String user, String stringuser) {
        IConversor conv = Gestor_Usuari.getInstancia();
        conv.Escriure(iduser, user, stringuser);
    }

    /**
     * Guarda la instancia d'una Partida en format JSON.
     *
     * @param idpartida identificador de la partida a guardar.
     * @param user nom de l'usuari.
     * @param partida string en format json que representa una partida.
     */
    public void GuardarPartida(Integer idpartida, String user, String partida) {
        IConversor conv = Gestor_Partida.getInstancia();
        conv.Escriure(idpartida, user, partida);
    }

    /**
     * Elimina la instancia d'un kenken.
     * 
     * @param idkenken identificador del kenken a eliminar.
     * @param user nom de l'usuari.
     */
    public void EliminarKenken(Integer idkenken, String user) {
        IConversor conv = Gestor_Kenken.getInstancia();
        conv.Eliminar(idkenken, user);
    }

    /**
     * Elimina la instancia d'un ranquing.
     * 
     * @param idkenken identificador del kenken del ranquing a eliminar.
     * @param user nom de l'usuari.
     */
    public void EliminarRanquing(Integer idkenken, String user) {
        if(ObtenirRanquing(idkenken, user) != null){
            IConversor conv = Gestor_Ranquing.getInstancia();
            conv.Eliminar(idkenken, user);
        }
    }

    /**
     * Elimina la instancia d'un usuari.
     * 
     * @param iduser identificador de l'usuari a eliminar.
     * @param user nom de l'usuari.
     */
    public void EliminarUsuari(Integer iduser, String user) {
        IConversor conv = Gestor_Usuari.getInstancia();
        conv.Eliminar(iduser, user);
    }

    /**
     * Elimina la instancia d'una partida.
     * 
     * @param idpartida identificador de la partida a eliminar.
     * @param user nom de l'usuari.
     */
    public void EliminarPartida(Integer idpartida, String user) {
        IConversor conv = Gestor_Partida.getInstancia();
        conv.Eliminar(idpartida, user);
    }

    /**
     * Comprova si existeix un usuari.
     * 
     * @param nom nom de l'usuari.
     * @return boolean
     */
    public Boolean existsUsuari(String nom) {
        if (ObtenirUsuari(null, nom) == null) return false; 
        else return true;
    }

    /**
     *  Obte informacio clau dels ranquings.
     * 
     * @param id
     * @param user
     * @return String[]
     */
    public String[] getInfoRanking(Integer id, String user) {
        Gestor_Ranquing gr = Gestor_Ranquing.getInstancia();
        return gr.getInfoRanking(id, user);
    }

    /**
     * Comprova si existeix una partida.
     * 
     * @param id id del kenken.
     * @param nom nom de l'usuari.
     * @return boolean
     */
    public Boolean existsPartida(Integer id, String nom) throws FileNotFoundException {
        if (ObtenirPartida(id, nom) == null) {
            throw new FileNotFoundException("El fichero no existe.");
        }
        return true;
    }

    /**
     * Comprova si existeix un ranquing indetificat per idkenken.
     * 
     * @param idkenken
     * @return Boolean
     * @throws FileNotFoundException
     */
    public Boolean existsRanquing(Integer idkenken) throws FileNotFoundException {
        if (ObtenirRanquing(idkenken, null) == null) {
            throw new FileNotFoundException("El fichero no existe.");
        }
        return true;
    }
}