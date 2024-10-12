
package classes.domini;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.AbstractMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import classes.domini.ConjuntCella;
import classes.domini.SolucionadorKenken;
import controladors.Ctrl_Domini;

/**
 * Representacio de un puzzle Kenken.
 * 
 * Un puzzle Kenken es defineix per la seva mida, el nombre de celes collocades, i les celes en si.
 * El puzzle es pot inicialitzar a partir d'un objecte JSON i pot generar una representacio JSON.
 * Tambe proporciona metodes per comprovar la validesa de les files i columnes, i per descartar solucions.
 * 
 */

public class Kenken {
    private Integer id;
    private Integer mida;
    private Integer ncColocades;
    private Ctrl_Domini ctrl_domini;

    private static Integer contador = 0;
    

    private HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> coordToNumCjt;
    private HashMap<Integer, ConjuntCella> cjtCeles;

    /**
     * Construeix un nou objecte Kenken a partir de l'objecte JSON proporcionat.
     *
     * @param KenkenJSONString String JSON object que representa un Kenken
     */
    public Kenken(String KenkenJSONString) {
        JsonObject KenkenJSON = new Gson().fromJson(KenkenJSONString, JsonObject.class);
        ctrl_domini = Ctrl_Domini.getInstancia();
        if(KenkenJSON.has("id")) this.id = KenkenJSON.get("id").getAsInt();
        else {
            this.id = ctrl_domini.getContadorKenken();
            ctrl_domini.incrementarContador();
        }
        this.cjtCeles = new HashMap<>();
        this.coordToNumCjt = new HashMap<>();

        this.mida = KenkenJSON.has("N") ? KenkenJSON.get("N").getAsInt() : 0;
        Integer numCjtCela = KenkenJSON.has("R") ? KenkenJSON.get("R").getAsInt() : 0;
        this.ncColocades = 0;

        JsonArray conjuntsCelesArray = KenkenJSON.getAsJsonArray("regions");
        for (Integer i = 0; i < conjuntsCelesArray.size(); ++i) {
            JsonObject ConjuntCellaObj = conjuntsCelesArray.get(i).getAsJsonObject();
            ConjuntCella cjt = new ConjuntCella(ConjuntCellaObj, this);
            this.ncColocades += cjt.getNcColocades();
            

            // insert en el hashmap cjtCeles amb int i and CjtCela
            cjtCeles.put(i, cjt);
            // agafar les coordenades del cjt
            List<AbstractMap.SimpleEntry<Integer, Integer>> coords = cjt.getCoordenades();
            // afegir totes les coordenades de la llista al hashmap cjtCeles amb les coords i el int i
            for (AbstractMap.SimpleEntry<Integer, Integer> coord : coords) {
                coordToNumCjt.put(coord, i);
            }
        }
    }

    /**
     * Genera un objecte JSON que representa el Kenken.
     *
     * @return Retorna un objecte JSON que representa el Kenken
     */
    public JsonObject generateJSON() {
        JsonObject kenkenObject = new JsonObject();

        kenkenObject.addProperty("id", id);
        kenkenObject.addProperty("N", mida);
        kenkenObject.addProperty("R", cjtCeles.size());

        JsonArray conjuntsCelesArray = new JsonArray();
        for (ConjuntCella cjt : cjtCeles.values()) {
            JsonObject conjuntCellaObj = cjt.generateJSON();
            conjuntsCelesArray.add(conjuntCellaObj);
        }
        kenkenObject.add("regions", conjuntsCelesArray);

        return kenkenObject;
    }

    public JsonObject generateSolucio() {
        JsonObject kenkenObject = new JsonObject();

        kenkenObject.addProperty("id", id);
        kenkenObject.addProperty("N", mida);
        kenkenObject.addProperty("R", cjtCeles.size());

        JsonArray conjuntsCelesArray = new JsonArray();
        for (ConjuntCella cjt : cjtCeles.values()) {
            JsonObject conjuntCellaObj = cjt.generateSolucio();
            conjuntsCelesArray.add(conjuntCellaObj);
        }
        kenkenObject.add("regions", conjuntsCelesArray);

        return kenkenObject;
    }

    /**
     * S'ha incrementat el numero de fitxes omplertes del kenken
     *
     */
    private void incrementarColocades() {
        ++ncColocades;
        
    }

    /**
     * S'ha incrementat el numero de fitxes omplertes del kenken
     *
     */
    private void decrementarColocades() {
        --ncColocades;
    }

    /**
     * 
     * @return Retorna l'identificador del Kenken
     */
    public Integer getIdKenken() {
        return id;
    }

    /**
     * Si existeix la coordenada dita retorno el valor de la cela
     * 
     * @param coord Coordenades de la cela de la qual vull saber el valor
     * @return Retorna el valor de la cela
     *
     */
    public Integer getValorCela(AbstractMap.SimpleEntry<Integer, Integer> coord) {
        if (coord.getKey() > mida || coord.getKey() <= 0 || coord.getValue() <= 0 || coord.getValue() > mida)
            throw new IllegalArgumentException("La Cella demanada esta fora dels limits");
            //En contes de retornar null hauriem llencar una excepcio
        Integer numCjt = coordToNumCjt.get(coord);
        ConjuntCella cjt = cjtCeles.get(numCjt);
        return cjt.getValorCella(coord);
    }

    /**
     * 
     * @return Retorna el nombre de celes collocades
     *
     */
    public Integer getNcColocades() {
        return ncColocades;
    }

    /**
     * 
     * @return Retorna la mida del Kenken
     *
     */
    public Integer getMida() {
        return mida;
    }

    /**
     * Comprova si es repeteixen valor en una fila en l'atribut valor
     * 
     * @param x Fila de la cela a comprovar
     * @param valor Valor a comprovar
     * @return Retorna true si existeix1 i tan sols 1 cela amb aquest valor en la fila
     *
     */
    public boolean comprovarFila(Integer x, Integer valor) {
        if (x > mida || x <= 0)
            throw new IllegalArgumentException("La fila demanada esta fora dels limits");
        AbstractMap.SimpleEntry<Integer, Integer> coord;
        int count = 0;
        for (Integer y = 1; y <= mida; y++) {
            coord = new AbstractMap.SimpleEntry<>(x, y);
            Integer numCjt = coordToNumCjt.get(coord); // Obtenir el id del conjunt de celes corresponent a les coordenades
            ConjuntCella cjt = cjtCeles.get(numCjt); // Obtenir el conjunt de celes corresponent al id
            Integer valCela = cjt.getValorCella(coord);
            if (valCela == valor) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return count == 1; 
    }

    /**
     * Comprova si es repeteixen valor en una fila en l'atribut Solucio
     * 
     * @param x Fila de la cela a comprovar
     * @param valor Valor a comprovar
     * @return InRetorna true si existeix 1 i tan sols 1 cela amb aquest valor en la fila
     *
     */
    public boolean comprovarFilaPossibilitat(Integer x, Integer valor) {
        if (x > mida || x <= 0)
            throw new IllegalArgumentException("La fila demanada esta fora dels limits");
        AbstractMap.SimpleEntry<Integer, Integer> coord;
        int count = 0;
        for (Integer y = 1; y <= mida; y++) {
            coord = new AbstractMap.SimpleEntry<>(x, y);
            Integer numCjt = coordToNumCjt.get(coord);
            ConjuntCella cjt = cjtCeles.get(numCjt);

            Integer solCela = cjt.getSolCella(coord);
            if (solCela == valor) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return count == 1; 
    }

    /**
     * Consulta el valor de la solucio de la cela
     * 
     * @param coord coordenades de la cela a comprovar
     * @return Retorna la solucio de la cela
     *
     */
    public Integer getSolucioCela(AbstractMap.SimpleEntry<Integer, Integer> coord) {
        if (coord.getKey() > mida || coord.getKey() <= 0 || coord.getValue() <= 0 || coord.getValue() > mida)
            throw new IllegalArgumentException("La Cella demanada esta fora dels limits");
        Integer numCjt = coordToNumCjt.get(coord);
        ConjuntCella cjt = cjtCeles.get(numCjt);
        return cjt.getSolCella(coord);
    }

    /**
     * Comprova si es repeteixen valor en una columna en l'atribut Valor
     * 
     * @param y Columna de la cela a comprovar
     * @param valor Valor a comprovar
     * @return Retorna true si existeix 1 i tan sols 1 cela amb aquest valor en la columna
     *
     */
    public boolean comprovarCol(Integer y, Integer valor) {
        if (y > mida || y <= 0)
            throw new IllegalArgumentException("La columna demanada esta fora dels limits");
        AbstractMap.SimpleEntry<Integer, Integer> coord;
        int count = 0;
        for (Integer x = 1; x <= mida; x++) {
            coord = new AbstractMap.SimpleEntry<>(x, y);
            Integer numCjt = coordToNumCjt.get(coord);
            //
            ConjuntCella cjt = cjtCeles.get(numCjt);
            Integer valCela = cjt.getValorCella(coord);
            if (valCela == valor) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return count == 1; 
    }

    /**
     * Comprova si es repeteixen valor en una columna en l'atribut Solucio
     * 
     * @param y Columna de la cela a comprovar
     * @param valor Valor a comprovar
     * @return Retorna true si existeix 1 i tan sols 1 cela amb aquest valor en la columna
     *
     */
    public boolean comprovarColPossibilitat(Integer y, Integer valor) {
        if (y > mida || y <= 0)
            throw new IllegalArgumentException("La columna demanada esta fora dels limits");
        AbstractMap.SimpleEntry<Integer, Integer> coord;
        int count = 0;
        for (Integer x = 1; x <= mida; x++) {
            coord = new AbstractMap.SimpleEntry<>(x, y);
            Integer numCjt = coordToNumCjt.get(coord);
            ConjuntCella cjt = cjtCeles.get(numCjt);
            Integer solCela = cjt.getSolCella(coord);
            if (solCela == valor) {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        return count == 1; 
    }


    /**
     * Descarto les solucions de una fila que coincideixen en coordenada i amb el valor passat
     * 
     * @param x Fila a comprovar
     * @param valor Valor a comprovar
     * @return Retorna els ids de les posiblitats descartades
     *
     */
    public HashSet<Integer> descartarSolucionsFila(Integer x, Integer valor, Integer id_cjt) {
        AbstractMap.SimpleEntry<Integer, Integer> coord;
        HashSet<Integer> ids_eliminats = new HashSet<>();

        for (Integer y = 1; y <= mida; y++) { //recorro el kenken de dalt a baix
            coord = new AbstractMap.SimpleEntry<>(x, y);
            Integer numCjt = coordToNumCjt.get(coord);
            ConjuntCella cjt = cjtCeles.get(numCjt);
            if(numCjt != id_cjt){ //si el conjunt es el mateix que el que estic com
                //un cop tinc el conjunt comprobo si esta el valor a alguna posiblitat i l'elimino
                HashSet<Integer> ids = cjt.eliminarSolucionsFila(x,valor);
                if(ids.size() > 0){ //si hi ha alguna posiblitat amb el valor
                    ids_eliminats.addAll(ids); //afegeixo els ids a la llista de ids eliminats
                }
            }
        }
        
        return ids_eliminats;
    }

    /**
     * Descarto les solucions de una columna que coincideixen en coordenada i amb el valor passat
     * 
     * @param y Columna a comprovar
     * @param valor Valor a comprovar
     * @return Retorna els ids de les posiblitats descartades
     *
     */
    public HashSet<Integer> descartarSolucionsColumna(Integer y, Integer valor, Integer id_cjt) {
        AbstractMap.SimpleEntry<Integer, Integer> coord;
        HashSet<Integer> ids_eliminats = new HashSet<>();

        for (Integer x = 1; x <= mida; x++) { //recorro el kenken de dalt a baix
            coord = new AbstractMap.SimpleEntry<>(x, y);
            Integer numCjt = coordToNumCjt.get(coord);
            ConjuntCella cjt = cjtCeles.get(numCjt);
            if(numCjt != id_cjt){
                //un cop tinc el conjunt comprobo si esta el valor a alguna posiblitat i l'elimino
                HashSet<Integer> ids = cjt.eliminarSolucionsColumna(y,valor);
                if(ids.size() > 0){ //si hi ha alguna posiblitat amb el valor
                    ids_eliminats.addAll(ids); //afegeixo els ids a la llista de ids eliminats
                }
            }
        }
        return ids_eliminats;
    }

    /**
     * Modifico l'atribut valor de la cella corresponent
     * 
     * @param coord Coordenades de la cela
     * @param valor Valor a colocar
     * @return Retorna true si aconsegueix colocar el numero.
     *
     */
    public boolean colocarNum(AbstractMap.SimpleEntry<Integer, Integer> coord, Integer valor) {
        // Primer comprobar que les coordenades son les correctes, que no passen de la mida
        if (coord.getKey() > mida || coord.getKey() <= 0 || coord.getValue() <= 0 || coord.getValue() > mida)
            return false;
        // Segon que el valor no passa de la mida ni es mes petit que 1
        if (valor > mida || valor <= 0)
            return false;

        // Agafar el conjunt de celes corresponent a les coordenades
        Integer numCjt = coordToNumCjt.get(coord);
        ConjuntCella cjt = cjtCeles.get(numCjt);
        if (cjt.colocarNum(coord, valor)) {
            this.incrementarColocades();
            return true;
        } else
            return false;
    }

    /**
     * Esborro el contingut de l'atribut valor de la cella corresponent
     * 
     * @param coord Coordenades de la cela 
     * @return Retorna true si aconsegueix esborrar el numero.
     *
     */
    public boolean esborrarNum(AbstractMap.SimpleEntry<Integer, Integer> coord) {
        // Primer comprobar que les coordenades son les correctes, que no passen de la mida
        if (coord.getKey() > mida || coord.getKey() <= 0 || coord.getValue() <= 0 || coord.getValue() > mida)
            return false;

        // Agafar el conjunt de celes corresponent a les coordenades
        Integer numCjt = coordToNumCjt.get(coord);
        ConjuntCella cjt = cjtCeles.get(numCjt);
        if (cjt.esborrarNum(coord)) {
            this.decrementarColocades();
            return true;
        } else
            return false;
    }

    public String KenkenSolucionatJSON() {
        SolucionadorKenken solKenken = new SolucionadorKenken();

        Kenken sol = solKenken.SolucionarKenKenIni(this);
        return sol.generateSolucio().toString();
    }

    /**
     * Calculo una possible solucio del kenken
     * 
     * @param coord Coordenades de la cela
     * @return Retorna true si aconsegueix resoldre el kenken
     *
     */
    public Boolean ajuda(AbstractMap.SimpleEntry<Integer, Integer> coord, Integer idKenken, String nom) {
        // comprobar que les coordenades son correctes
        if (coord.getKey() <= mida || coord.getKey() > 0 || coord.getValue() > 0 || coord.getValue() <= mida) {
            Integer numCjt = coordToNumCjt.get(coord);
            ConjuntCella cjt = cjtCeles.get(numCjt);
            Integer valCela = cjt.getValorCella(coord);
            if (valCela == 0) {
                try{
                    Kenken sol = new Kenken(this.generateJSON().toString());
                    sol.setValuesSolucio();
                    SolucionadorKenken solKenken = new SolucionadorKenken();

                    Kenken solucio = solKenken.SolucionarKenKenIni(sol);

                    Integer valSol = solucio.getSolucioCela(coord);
                    if (valSol != 0) {
                        if (cjt.colocarNum(coord, valSol)) {

                            cjt.setInicial(coord);
                            return true;
                        } else
                            return false;
                    } else {
                        return false;
                    }
                } catch (Exception e){
                    return false;
                }
            } else {
                return false;
            }
        } else
            throw new IllegalArgumentException("La Cella demanada esta fora dels limits");
    }
    
    public Boolean ajudaExtra(AbstractMap.SimpleEntry<Integer, Integer> coord, Integer idKenken, String nom) {
        // comprobar que les coordenades son correctes
        if (coord.getKey() <= mida || coord.getKey() > 0 || coord.getValue() > 0 || coord.getValue() <= mida) {
            Integer numCjt = coordToNumCjt.get(coord);
            ConjuntCella cjt = cjtCeles.get(numCjt);
            Integer valCela = cjt.getValorCella(coord);
            if (valCela == 0) {
                try{
                    Kenken sol = new Kenken(ctrl_domini.getKenken(idKenken, nom));
                    SolucionadorKenken solKenken = new SolucionadorKenken();
                    Kenken solucio = solKenken.SolucionarKenKenIni(sol);

                    Integer valSol = solucio.getSolucioCela(coord);
                    if (valSol != 0) {
                        if (cjt.colocarNum(coord, valSol)) {

                            cjt.setInicial(coord);
                            return true;
                        } else
                            return false;
                    } else {
                        throw new Exception("Error en el solucionador Kenken");
                    }
                } catch (Exception e){
                    return false;
                }
            } else {
                return false;
            }
        } else
            throw new IllegalArgumentException("La Cella demanada esta fora dels limits");
    }

    /**
     * Consulto si ja he colocat totes les celes i si estan correctament collocades
     * 
     * @return Retorna true si el kenken esta finalitzat
     */
    public boolean finalitzar() {
        // que tinguem totes les casselles colocades
        
        // primer, recorrer totes les columnes i files fent el comprovarFila i comprovarCol
        for (Integer i = 1; i <= mida; i++) {
          
            for (Integer x = 1; x < mida; x++) {
                if (!comprovarFila(x, i)){
                    return false;
                }
            }
            for (Integer y = 1; y < mida; y++) {
                if (!comprovarCol(y, i))
                    return false;
            }
        }
        // Despres anar a cada Cjt de celes i revisar que el resultat sigui el correcte
        for (HashMap.Entry<Integer, ConjuntCella> entry : cjtCeles.entrySet()) {
            ConjuntCella cjt = entry.getValue();
            if (!cjt.comprobarRes())
                return false;
        }
        // si tot es correcte, finalitzar la partida
        return true;
    }

    /**
     * Consulto el numero de possibilitats d'un conjunt de celes
     * 
     * @param id Cjt de celes
     * @return Retorna el numero de possibilitats del conjunt
     *
     */
    public Integer getNumPossibilitats(Integer id) {
        return cjtCeles.get(id).getNumPossibilitats();
    }

    /**
     * Consulto el numero de conjunts del kenken
     * 
     * @return Retorna el numero de conjunts del kenken
     *
     */
    public Set<Integer> getNumCjts() {
        return cjtCeles.keySet();
    }

    /**
     * Calculo les possibilitats de un conjunt de celles.
     * 
     * @param id_cjt identificador del conjunt de celles
     * @param id_solucio ultima solucio collocada
     * @return Retorna un cop fet el calcul l'ultim id_solucio colocat
     *
     */
    public Integer calcularPossibilitats(Integer id_cjt, Integer id_solucio) {
        return cjtCeles.get(id_cjt).calcularPossibilitats(id_solucio);
    }

    /**
     * Coloco la solucio en el conjunt i descarto les possibilitats que no son correctes
     * 
     * @param id_cjt identificador del conjunt on es troba la possibilitat a colocar
     * @param id_solucio identificador de la solucio a colocar
     * @return Retorna els identidicadors de les possibilitats que s'han de descartar
     *
     */
    public HashSet<Integer>  colocarSolucio(Integer id_cjt, Integer id_solucio) {

        HashSet<Integer> ids_totals;

        ids_totals = cjtCeles.get(id_cjt).colocarSolucio(id_cjt, id_solucio);

        return ids_totals;

    }

    /**
     * Desfaig la solucio colocada en el conjunt de celles
     * 
     * @param id_cjt identificador del conjunt de celles
     *
     */
    public void desferSolucioColocada(Integer id_cjt) {
        cjtCeles.get(id_cjt).desferSolucioColocada();
    }

    /**
     * Desfaig l'eliminacio de la solucio en el conjunt de celles
     * 
     * @param id_cjt identificador del conjunt de celles
     * @param id_solucio identificador de la solucio a desfer
     *
     */
    public void desferEliminarSolucio(Integer id_cjt, Integer id_solucio) {
        cjtCeles.get(id_cjt).desferEliminarSolucio(id_solucio);
    }

    /**
     * Consulto les possibilitats d'un conjunt de celles
     * 
     * @param id_cjt identificador del conjunt de celles
     * @return Retorna les possibilitats del conjunt
     *
     */
    public Set<Integer> getPossibilitats(Integer id_cjt) {
        return cjtCeles.get(id_cjt).getPossibilitats();
    }

    /**
     * Consulto si el kenken esta completament omplert
     * 
     * @return Retorna true si el kenken esta completament omplert
     *
     */
    public boolean esComplet(){
        return ncColocades == mida*mida;
    }

    /**
     * Consulto si el kenken esta completament omplert amb la solucio
     * 
     * @return Retorna true si el kenken esta completament omplert amb la solucio
     *
     */
    public boolean esCompletSolucio(){
        for (ConjuntCella cjt : cjtCeles.values()) {
            for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjt.getCoordenades()) {
                if(cjt.getCella(coord).getSolucio() == 0) return false;
            }
        }

        return true;
    }

    /**
     * Esborro el valor de la solucio de totes les celes
     * 
     */
    public void deleteValueSolucio(){
        for (ConjuntCella cjt : cjtCeles.values()) {
            cjt.deleteValueSolucio();
        }
    }

    /**
     * Coloco el valor de la solucio de totes les celes
     * 
     */
    public void setValuesSolucio(){
        for (ConjuntCella cjt : cjtCeles.values()) {
            cjt.setValuesSolucio();
        }
    }

    /**
     * Retorna el conjunt de celles identificat per l'id
     * 
     * @param id identificador del conjunt de celles
     * @return Retorna el conjunt de celles
     *
     */
    public ConjuntCella getConjuntCella(Integer id){
        return cjtCeles.get(id);
    }

    /**
     * Consultora de les coordenades de les celes
     * 
     * @return un HashMap de cada coordenada a quin conjunt pertany
     *
     */
    public HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> getcoordToNumCjt(){
        return coordToNumCjt;
    }
    
    /**
     * Consultora dels conjunts de Celes del Kenken
     * 
     * @return el HashMap de conjunts de celles
     *
     */
    public HashMap<Integer, ConjuntCella> getcjtCeles(){
        return cjtCeles;
    }

    public Boolean celaInicial(AbstractMap.SimpleEntry<Integer, Integer> coord) {
        Integer numCjt = coordToNumCjt.get(coord);
        ConjuntCella cjt = cjtCeles.get(numCjt);
        return cjt.celaInicial(coord);
    }
    
    public void esborrarValuesGenerats(){
        for(ConjuntCella cjt : cjtCeles.values()){
            cjt.esborrarValuesGenerats();
        }
    }

}
