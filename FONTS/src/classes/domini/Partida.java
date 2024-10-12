package classes.domini;

import java.time.*;

import java.util.AbstractMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Representa una partida del Kenken.
 * 
 * Una partida te un estat, un temps, un nombre de pistes, un nombre de celles collocades, un Kenken i un usuari.
 * Tambe proporciona metodes per collocar i esborrar valors de les celles, demanar pistes i finalitzar la partida.
 */
public class Partida {
    private Instant startTime;
    private String estat;
    private Duration temps;
    private Kenken kenken;
    private Usuari usuari;
    private int nPistes;
    private int ncColocades;
    private int mida;

    /**
     * Retorna l'estat de la partida.
     * 
     * @return String
     */
    public String getEstat() {
        return estat;
    }

    /**
     * Estableix l'estat de la partida.
     * 
     * @param estat Estat de la partida.
     */
    public void setEstat(String estat) {
        this.estat = estat;
    }

    /**
     * Retorna el temps de la partida.
     * 
     * @return Duration
     */
    public Duration getTemps() {
        return temps;
    }
    
    /**
     * Estableix el temps de la partida.
     * 
     * @param temps Temps de la partida.
     */
    public void setTemps(Duration temps) {
        this.temps = temps;
    }

    /**
     * Retorna el nombre de pistes disponibles.
     * 
     * @return Integer
     */
    public int getnPistes() {
        return nPistes;
    }

    /**
     * Estableix el nombre de pistes disponibles.
     * 
     * @param nPistes Nombre de pistes disponibles.
     */
    public void setnPistes(int nPistes) {
        this.nPistes = nPistes;
    }
    
    /**
     * Retorna el nom de l'usuari.
     * 
     * @return String
     */
    public String getUsuari() {
        return usuari.getNom();
    }

    /**
     * Retorna l'identificador del Kenken.
     * 
     * @return Integer
     */
    public int getIdKenken() {
        return kenken.getIdKenken();
    }

    /**
     * Retorna la mida del Kenken.
     * 
     * @return Integer
     */
    public Integer getMida() {
        return this.mida;
    }

    /**
     * Retorna el nombre de celles collocades.
     * 
     * @return Integer
     */
    public Integer getNcColocades() {
        return this.ncColocades;
    }

    /**
     * Retorna el Kenken de la partida.
     * 
     * @return Kenken
     */
    public String getKenkenJSON() {
        return kenken.generateJSON().toString();
    }

    /**
     * Retorna el Kenken de la partida.
     * 
     * @return Kenken
     */
    public Kenken getKenken() {
        return kenken;
    }
    
    public Integer getValorCela(AbstractMap.SimpleEntry<Integer, Integer> pos){
        return kenken.getValorCela(pos);
    }

    /**
     * Constructor de la classe Partida.
     * 
     * @param KenkenJSON JSON amb la informacio del Kenken.
     * @param usr Usuari que juga la partida.
     */
    public Partida(Kenken kenkenP, Usuari usr) {
        this.estat = " ";
        this.temps = Duration.ZERO;
        this.nPistes = 3;
        usuari = usr;
        String kenkenJSON = kenkenP.generateJSON().toString();
        kenken = new Kenken(kenkenJSON);
        mida = kenken.getMida();
        ncColocades = kenken.getNcColocades();
        reanudar();
    }

    public Partida(String partidaJSON, Usuari user){
        this.usuari = user;

        Gson gson = new Gson();
        JsonObject partidaObject = gson.fromJson(partidaJSON, JsonObject.class);
        this.estat = partidaObject.get("estat").getAsString();
        this.temps = Duration.parse(partidaObject.get("temps").getAsString());
        
        this.nPistes = partidaObject.get("nPistes").getAsInt();
        this.ncColocades = partidaObject.get("ncColocades").getAsInt();
        this.mida = partidaObject.get("mida").getAsInt();

       
        JsonObject kenkenObject = partidaObject.getAsJsonObject("kenken");
        
        kenken = new Kenken(kenkenObject.toString());
        reanudar();

    }

    /**
     * Elimina el valor de la cella pos_cela.
     * 
     * @param pos_cela Coordenades de la cella.
     * @return Boolean
     */
    public Boolean esborrarNum(AbstractMap.SimpleEntry<Integer, Integer> pos_cela) {
        if (estat.equals("enCurs")) {
            if(kenken.esborrarNum(pos_cela)) {
                --ncColocades;
                return true;
            } else return false;
        } else return false;
    }

    public Boolean celaInicial(AbstractMap.SimpleEntry<Integer, Integer> pos_cela) {
        return kenken.celaInicial(pos_cela);
    }

    /**
     * Colloca el valor num a la cella pos_cela.
     * 
     * @param pos_cela Coordenades de la cella.
     * @param num Valor a collocar.
     * @return Boolean
     */
    public Boolean colocarNum(AbstractMap.SimpleEntry<Integer, Integer> pos_cela, Integer num) {
        if (estat.equals("enCurs")) {
            if(kenken.colocarNum(pos_cela, num)) {
                ++ncColocades;
                return true;
            } else return false;
        } else return false;
    }

    /**
     * Demana una pista per la cella pos_cela.
     * 
     * @param pos_cela Coordenades de la cella.
     */
    public void ajuda(AbstractMap.SimpleEntry<Integer, Integer> pos_cela, Integer idKenken, String nom) throws Exception{
        if (estat.equals("enCurs")) {
            if(kenken.getValorCela(pos_cela) != 0) {
                throw new Exception("Aquesta cella ja te un valor. Esborra'l abans de demanar pista.");
            }else if (kenken.ajuda(pos_cela, idKenken, nom)) {
                ++ncColocades;
                --nPistes;
            } else throw new Exception("No hi ha cap pista disponible per aquesta cella.");
        }
    }
    
    public void ajudaExtra(AbstractMap.SimpleEntry<Integer, Integer> pos_cela, Integer idKenken, String nom) throws Exception{
        if (estat.equals("enCurs")) {
            if (kenken.ajudaExtra(pos_cela, idKenken, nom)) {
                ++ncColocades;
                --nPistes;
            } else throw new Exception("No hi ha cap pista disponible per aquesta cella.");
        }
    }

    /**
     * Reanuda la partida.
     */
    public void reanudar() {
        if (!estat.equals("Finalitzada")) {
            this.estat = "enCurs";  
        }
    }

    /**
     * Pausa la partida.
     * 
     * @param seconds temps de la partida.
     */
    public void pausar(Duration seconds) {
        if (this.estat.equals("enCurs")) {
            this.estat = "Pausada";
            this.temps = seconds;
        }
    }


    /**
     * Finalitza la partida.
     * 
     * @return Boolean
     */
    public Boolean finalitzarPartida() {
        if(estat.equals("enCurs")) {
            this.estat = "Pausada";
         
            if(kenken.finalitzar()) {
                this.estat = "Finalitzada";
                return true;
            } else {
                this.estat = "enCurs";
                return false;
            }
        } else return false;
    }
 
    /**
     * Guarda la partida en un JSON.
     * 
     * @return JsonObject
     */
    public JsonObject toJSON() {
        if(!estat.equals("Finalitzada")) {
            if(estat.equals("enCurs")) {
                this.estat = "Pausada";
            } 
            //guardar la partida
            //generar un JSON amb la partida        
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject partidaJson = new JsonObject();
            
            partidaJson.addProperty("estat", estat);
            partidaJson.addProperty("temps", temps.toString());
            partidaJson.addProperty("nPistes", nPistes);
            partidaJson.addProperty("ncColocades", ncColocades);
            partidaJson.addProperty("mida", mida);
            
            JsonObject kenkenJson = kenken.generateJSON();
            partidaJson.add("kenken", kenkenJson);
            
            try (FileWriter file = new FileWriter("partida.json")) {
                file.write(gson.toJson(partidaJson));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return partidaJson;
        } else return null;
        
    }
    
    public Boolean comprovarFila(Integer pos_cela, Integer num) {
        if (estat.equals("enCurs")) {
           return (kenken.comprovarFila(pos_cela, num));
        } else return false;
    }
    
    public Boolean comprovarCol(Integer pos_cela, Integer num) {
        if (estat.equals("enCurs")) {
           return (kenken.comprovarCol(pos_cela, num));
        } else return false;
    }
}
