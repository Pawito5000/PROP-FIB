package classes.domini;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Representa el ranquing d'un Kenken.
 * 
 * Un ranquing te un identificador de Kenken i una llista de parelles de temps i noms d'usuari.
 * Tambe proporciona metodes per obtenir i establir les dades del ranquing.
 */
public class Ranquing {
    private int idKenken;
    private List<AbstractMap.SimpleEntry<Duration, String>> rank;

    /**
     * Constructor de la classe Ranquing.
     */
    public Ranquing() {
        this.idKenken = 0;
        this.rank = new ArrayList<>();
    }
    
    /**
     * Constructor de la classe Ranquing.
     * 
     * @param idKenken Identificador del Kenken.
     * @param rank Llista de parelles de temps i noms d'usuari.
     */
    public Ranquing(int idKenken, List<AbstractMap.SimpleEntry<Duration, String>> rank) {
        this.idKenken = idKenken;
        if (rank.equals(null)) this.rank = new ArrayList<>();
        else this.rank = rank;
    }

    /**
     * Retorna l'identificador del Kenken.
     * 
     * @return Integer
     */
    public int getIdKenken() {
        return idKenken;
    }

    /**
     * Estableix l'identificador del Kenken.
     * 
     * @param idKenken Identificador del Kenken.
     */
    public void setIdKenken(int idKenken) {
        this.idKenken = idKenken;
    }

    /**
     * Retorna el ranquing del Kenken.
     * 
     * @return {@code List<AbstractMap.SimpleEntry<Duration, String>>}
     */
    public List<AbstractMap.SimpleEntry<Duration, String>> getRank() {
        return rank;
    }

    /**
     * Estableix el ranquing del Kenken.
     * 
     * @param rank Llista de parelles de temps i noms d'usuari.
     */
    public void setRank(List<AbstractMap.SimpleEntry<Duration, String>> rank) {
        this.rank = rank;
    }

    /**
     * Constructor de la classe Ranquing a partir d'un objecte JSON.
     * 
     * @param RanquingJSONString String en format JSON amb les dades del ranquing.
     */
    public Ranquing(String RanquingJSONString) {
        JsonObject RanquingJSON = new Gson().fromJson(RanquingJSONString, JsonObject.class);
        this.idKenken = RanquingJSON.get("idkenken").getAsInt();
        JsonArray rankArray = RanquingJSON.getAsJsonArray("rankings");
        this.rank = new ArrayList<>();
        for (int i = 0; i < rankArray.size(); ++i) {
            JsonObject rankObject = rankArray.get(i).getAsJsonObject();
            String username = rankObject.get("username").getAsString();
            String timeString = rankObject.get("tiempo").getAsString();
            String[] parts = timeString.split(":");
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            Duration duration = Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
            AbstractMap.SimpleEntry<Duration, String> newEntry = new AbstractMap.SimpleEntry<>(duration, username);
            this.rank.add(newEntry);
        }
    }

    /**
     * Retorna l'objecte Ranquing en format JSON.
     * 
     * @return JsonObject
     */
    public JsonObject toJson() {
        JsonObject RanquingJSON = new JsonObject();
        RanquingJSON.addProperty("idkenken", idKenken);
        JsonArray rankArray = new JsonArray();
        for (AbstractMap.SimpleEntry<Duration, String> entry : rank) {
            JsonObject entryJSON = new JsonObject();
            long hours = entry.getKey().toHours();
            long minutes = entry.getKey().minusHours(hours).toMinutes();
            long seconds = entry.getKey().minusHours(hours).minusMinutes(minutes).getSeconds();
            entryJSON.addProperty("tiempo", String.format("%02d:%02d:%02d", hours, minutes, seconds));
            entryJSON.addProperty("username", entry.getValue());
            rankArray.add(entryJSON);
        }
        RanquingJSON.add("rankings", rankArray);
        return RanquingJSON;
    }

    /**
     * Actualitza el ranquing amb un nou temps i nom d'usuari.
     * 
     * @param time Temps de la partida.
     * @param username Nom d'usuari.
     */
    public void ActualitzarRanquing(Duration time, String username) {
        if (time != null && username != null) {
            AbstractMap.SimpleEntry<Duration, String> newEntry = new AbstractMap.SimpleEntry<>(time, username);
            int iUsuariTrobat = -1;
            int iUsuariNoTrobat = -1;
            int i = 0;
            int menystemps = 0;
            int trobat = 0;

            //hem de recorrer tot el rank per asegurar si l'usuari ja esta al ranquing
            while (i < rank.size()) {
                //si l'usuari ja esta al ranquing, mirem si el nou temps es millor
                if (rank.get(i).getValue().equals(username)) {
                    iUsuariTrobat = i;
                    //si el temps del rank es menor al temps que volem insertar, no guardem l'index
                    if (rank.get(i).getKey().compareTo(time) < 0) {
                        menystemps = 1;
                    }
                }
                //guardem l'index del primer temps del rank mes gran que el temps que volem inserir
                if (rank.get(i).getKey().compareTo(time) > 0 && trobat == 0) {
                    iUsuariNoTrobat = i;
                    trobat = 1;
                }

                i++;
            }
            if (iUsuariTrobat != -1 && menystemps == 0) {
                rank.remove(iUsuariTrobat);
                if (iUsuariNoTrobat > iUsuariTrobat) {
                    iUsuariNoTrobat--;
                }
            }
            if (iUsuariTrobat == -1 || menystemps != 1) {
                if (iUsuariNoTrobat >= 0 && iUsuariNoTrobat <= rank.size())rank.add(iUsuariNoTrobat, newEntry);
                else if (iUsuariNoTrobat == -1) rank.add(0, newEntry);
            }
        }
    }
}
