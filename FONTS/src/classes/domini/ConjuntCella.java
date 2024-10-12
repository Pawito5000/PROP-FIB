/**
 * Represents a region of the Kenken puzzle, which is a set of cells that must satisfy an operation.
 */
package classes.domini;

import classes.domini.Cella;
import classes.domini.FactoriaOperacions;
import classes.domini.Operacio;
import classes.domini.Kenken;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * Representa una regio del Kenken, que es un conjunt de celles que han de complir una operacio.
 */
public class ConjuntCella {
    private int resultat;
    private int ncColocades;
    private int nOperacio;
    private Kenken kenken;
    private Operacio operacio; 
    

    private LinkedHashMap<AbstractMap.SimpleEntry<Integer, Integer>, Cella> coordToCella;
    private Integer numPossibilitats; 
   

    /**
     * Creadora de la clase ConjuntCella a partir dels paramertres del Json
     * 
     * @param ConjuntCellaObj L'objeto Json que representa el conjunt de celles.
     * @param kenken L'objeto Kenken al qual pertany el conjunt.
     * 
     * @throws IllegalArgumentException Si el nombre de celles no coincideix amb el nombre de celles del conjunt.
     */
    public ConjuntCella(JsonObject ConjuntCellaObj, Kenken kenken) 
    {
        this.resultat = 0;
        this.ncColocades = 0;
        this.coordToCella = new LinkedHashMap<>();
        this.kenken = kenken;
        this.numPossibilitats = null;

        if (ConjuntCellaObj != null) {
            this.nOperacio = ConjuntCellaObj.has("oper") ? ConjuntCellaObj.get("oper").getAsInt() : 0;
            this.resultat = ConjuntCellaObj.has("result") ? ConjuntCellaObj.get("result").getAsInt() : 0;
            int numcaselles = ConjuntCellaObj.has("e") ? ConjuntCellaObj.get("e").getAsInt() : 0;

        this.nOperacio = ConjuntCellaObj.get("oper").getAsInt();
        this.resultat = ConjuntCellaObj.get("result").getAsInt();
        Integer celes = ConjuntCellaObj.get("e").getAsInt();

            operacio = FactoriaOperacions.crearOperacio(nOperacio);

            if (ConjuntCellaObj.has("coordenades")) {
                JsonArray CelesArray = ConjuntCellaObj.getAsJsonArray("coordenades");
                if(numcaselles != CelesArray.size()) throw new IllegalArgumentException("El nombre de celles no coincideix amb el nombre de celles del conjunt");
                Integer minCoordenada_x = -1;
                Integer minCoordenada_y = -1;
                Integer maxCoordenada_x = -1;
                Integer maxCoordenada_y = -1;
                AbstractMap.SimpleEntry<Integer, Integer> coord_aux = null;
                for (Integer i = 0; i < numcaselles; ++i) {
                    JsonObject celaObject = CelesArray.get(i).getAsJsonObject();
                    Integer x = celaObject.has("x") ? celaObject.get("x").getAsInt() : 0;
                    if(x == 0) throw new IllegalArgumentException("La coordenada x no pot ser 0");
                    Integer y = celaObject.has("y") ? celaObject.get("y").getAsInt() : 0;
                    if(y == 0) throw new IllegalArgumentException("La coordenada y no pot ser 0");
                    if(minCoordenada_x == -1 || x < minCoordenada_x) minCoordenada_x = x;
                    if(minCoordenada_y == -1 || y < minCoordenada_y) minCoordenada_y = y;
                    if(maxCoordenada_x == -1 || x > maxCoordenada_x) maxCoordenada_x = x;
                    if(maxCoordenada_y == -1 || y > maxCoordenada_y) maxCoordenada_y = y;
                    if(coord_aux == null) coord_aux = new AbstractMap.SimpleEntry<>(x, y);
                    else if(coord_aux.equals(new AbstractMap.SimpleEntry<>(x, y))) throw new IllegalArgumentException("Hi ha celles repetides");
                    Integer cont = celaObject.has("cont") ? celaObject.get("cont").getAsInt() : 0;
                    Integer valorEnt_aux = celaObject.has("ini") ? celaObject.get("ini").getAsInt() : 0;
                    Boolean ini = (valorEnt_aux != 0);
                    Cella c = new Cella(x, y, cont, ini);
                    coordToCella.put(new AbstractMap.SimpleEntry<>(x, y), c);
                    if (cont != 0) ncColocades++;
                }
                Set<AbstractMap.SimpleEntry<Integer, Integer>> visited = new HashSet<>();
                dfs(coord_aux.getKey(), coord_aux.getValue(), minCoordenada_x, minCoordenada_y, maxCoordenada_x, maxCoordenada_y, visited);
                // Verificar si todas las celdas de la region han sido visitadas
                for (AbstractMap.SimpleEntry<Integer, Integer> cell : coordToCella.keySet()) {
                    if (!visited.contains(cell)) {
                        throw new IllegalArgumentException("No totes les celles de la regio estan connectades" + cell.getKey() + " " + cell.getValue());
                    }
                }
            }
        }
    }

    
    /** 
     * Recorre tot el conjunt de celles de la regio de manera recursiva per comprovar que totes les celles estan connectades

     * @param visited  set de celles visitades
     * @param x coordenada x
     * @param y coordenada y
     * @param minX limit inferior de x
     * @param minY limit inferior de y
     * @param maxX limit superior de x
     * @param maxY limit superior de y
     */
    private void dfs(int x, int y, int minX, int minY, int maxX, int maxY, Set<AbstractMap.SimpleEntry<Integer, Integer>> visited) {
        AbstractMap.SimpleEntry<Integer, Integer> cell = new AbstractMap.SimpleEntry<>(x, y);
        if (x < minX || x > maxX || y < minY || y > maxY || visited.contains(cell) || !coordToCella.containsKey(cell)) {
            return; // Fuera de los limites o celda ya visitada o no pertenece a la region
        }
        visited.add(cell); // Marcar la celda como visitada
        // Explorar las celdas adyacentes
        dfs(x + 1, y, minX, minY, maxX, maxY, visited); // Derecha
        dfs(x - 1, y, minX, minY, maxX, maxY, visited); // Izquierda
        dfs(x, y + 1, minX, minY, maxX, maxY, visited); // Abajo
        dfs(x, y - 1, minX, minY, maxX, maxY, visited); // Arriba
    }

    
    /** 
     * @return JsonObject es genera un Json del objecte ConjuntCella
     */
    public JsonObject generateJSON() {
        JsonObject kenkenObject = new JsonObject();

        kenkenObject.addProperty("oper", nOperacio);
        kenkenObject.addProperty("result", resultat);
        kenkenObject.addProperty("e", coordToCella.size());


        JsonArray CelesArray = new JsonArray();
        for (Cella cela : coordToCella.values()) {
            JsonObject CellaObj = cela.generateJSON();
            CelesArray.add(CellaObj);
        }
        kenkenObject.add("coordenades", CelesArray);

        return kenkenObject;
    }

    public JsonObject generateSolucio() {
        JsonObject kenkenObject = new JsonObject();

        kenkenObject.addProperty("oper", nOperacio);
        kenkenObject.addProperty("result", resultat);
        kenkenObject.addProperty("e", coordToCella.size());


        JsonArray CelesArray = new JsonArray();
        for (Cella cela : coordToCella.values()) {
            JsonObject CellaObj = cela.generateSolucio();
            CelesArray.add(CellaObj);
        }
        kenkenObject.add("coordenades", CelesArray);

        return kenkenObject;
    }


    
    /** 
     * Si la cela no es inicial l'atribut valor de la cella s'actualitza al valor corresponent
     * 
     * @param coord el valor de les coordenades es correcte i pertanyen al conjunt.
     * @param valor enter entre 1 i mida del kenken.
     * @return boolean
     */
    public boolean colocarNum(AbstractMap.SimpleEntry<Integer, Integer> coord, int valor)
    {
        Cella c = coordToCella.get(coord);
                
        if (c.getInicial() || c.getValue() != 0) return false;
        else {
            c.colocarNum(valor);
            incrementarColocades();
            //marcar en vermell les celelles de la mateixa fila/columna que tinguin el mateix valor
            return true;
        }
        
    }


    
    /** 
     * si la cela no es inicial i esta ocupada, el contingut de l'atribut valor de la cella s'esborra
     * 
     * @param coord el valor de les coordenades es correcte i pertanyen al conjunt.
     * @return boolean
     */
    public boolean esborrarNum(AbstractMap.SimpleEntry<Integer, Integer> coord)
    {
        Cella c = coordToCella.get(coord);
        if (c.getInicial() || c.getValue() == 0) return false;
        else {
            c.esborrarNum();
            decrementarColocades();
            return true;
        }
        
    }


    
    /** 
     * Modifica el parametre inicial de la cella en la posicio x,y.
     * @param coord el valor de les coordenades es correcte i pertanyen al conjunt.
     */
    public void setInicial(AbstractMap.SimpleEntry<Integer, Integer> coord)
    {
        Cella c = coordToCella.get(coord);
        c.setInicial();
    }

    public Integer getnOperacio() {
        return nOperacio;
    }

    
    /**
     * Retorna el valor de la cella en la posicio x,y.
     *
     * @param coord el valor de les coordenades es correcte i pertanyen al conjunt.
     * @return valor de la cella 
     */
    public Integer getValorCella(AbstractMap.SimpleEntry<Integer, Integer> coord)
    {
        Cella c = coordToCella.get(coord);
        return c.getValue();
    }

    //pre: e
    //post: es 
    /**
     * Retorna l'atribut solucio de la cella en la posicio x,y.
     *
     * @param coord el valor de les coordenades es correcte i pertanyen al conjunt.
     * @return l'atribut solucio de la cella
     */
    public Integer getSolCella(AbstractMap.SimpleEntry<Integer, Integer> coord)
    {
        Cella c = coordToCella.get(coord);
        return c.getSolucio();
    }


    /**
     * Retorna el numero de celles del conjunt
     *
     * @return el numero de celles del conjunt
     */
    public int getnCeles(){
        return coordToCella.size();
    }

    
    public Integer getNcColocades() {
        return ncColocades;
    }
    
    //pre: -
    //post: es retorna el numero de possibilitats del conjunt.
    /**
     * Retorna el numero de possibilitats del conjunt.
     *
     * @return possibilitats del conjunt.
     */
    public Integer getNumPossibilitats() {
        return numPossibilitats;
    }  
    
    
    /**
     * Comproba el resultat del conjunt
     *
     * @return retorna true si el conjunt de celles dona el resultat correcte, false altrament
     */
    public boolean comprobarRes(){
        if(ncColocades == coordToCella.size()){

            double[] vector = new double[ncColocades];
            int i = 0;

            //recorrer el Linkedhashmap i omplir el vector amb els valors de les celles
            for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {
                Cella cella = coordToCella.get(coord);
                vector[i] = cella.getValue();
                if(vector[i] == 0) return false;
                i++;

            }
            return operacio.Calcula(vector) == resultat;
        }
        else return false;

    }

    
    /**
     * Retorna les coordenades de les celles del conjunt
     *
     * @return coordenades de les celles del conjunt
     */
    public  List<AbstractMap.SimpleEntry<Integer, Integer>> getCoordenades(){
        List<AbstractMap.SimpleEntry<Integer, Integer>> res = new ArrayList<>(); 
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {
            res.add(coord);
        }
        return res;
    }

    
    /**
     * Incrementa en 1 el nombre de celles collocades al conjunt
     *
     */
    public void incrementarColocades()
    {
        this.ncColocades++;
    }

    /**
     * Decrementa en 1 el nombre de celles collocades al conjunt
     *
     */
    public void decrementarColocades()
    {
        this.ncColocades--;
    }
  

    /**
     * Calcula les possibilitats del conjunt segons la operacio a realitzar i les celles que ja tenen una solucio colocada
     *
     * @param id_solucio l'ultim id_solucio colocat per l'algoritme solucionar kenken
     * @return suma de l'ultim id_solucio i les possibilitats trobades
     */
    public Integer calcularPossibilitats(Integer id_solucio) {
        //Calculo el numero de possibilitats del conjunt segons la operacio a fer
        this.numPossibilitats = operacio.calcularPossibilitats(resultat, id_solucio, this);
        //retorno el nombre de possibilitats sumades a les solucions per tal de tenir l'ultim id de solucio.
        return id_solucio + numPossibilitats;
    }

    /**
     * Per totes les celles del conjunt es posa en l'atribut solucio de la cella el valor de l'atribut valor de la cella
     *
     */
    public void setValuesSolucio(){
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {
            Cella cella = coordToCella.get(coord);
            Integer valor = cella.getValue();
            //Coloco el valor de la cela a la solucio
            cella.setInicial(valor);
        }
    }

    
    /**
     * Es modifica l'atribut solucio de la cella corresponent a les coordenades.
     *
     * @param coord les coordenades es correcte i pertanyen al conjunt
     * @param valor es un enter entre 1 i mida del kenken
     * @return true si s'ha colocat el valor, false altrament.
     */
    public Boolean setSolucio(AbstractMap.SimpleEntry<Integer, Integer> coord, Integer valor){
            Cella cella = coordToCella.get(coord);
            return cella.setSolucio(valor);
        
    }

    
    /**
     * Esborra l'atribut solucio de la cella corresponent a les coordenades.
     *
     * @param coord les coordenades es correcte i pertanyen al conjunt
     * @return true si s'ha esborrat el valor, false altrament.
     */
    public Boolean deleteSolucio(AbstractMap.SimpleEntry<Integer, Integer> coord){
        Cella cella = coordToCella.get(coord);
        return cella.deleteSolucio();
    }
    
    /**
     * Per totes les celles del conjunt, sempre que no siguin inicials, s'esborrara el contingut del atribut solucio.
     *
     */
    public void deleteValueSolucio(){
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {
            Cella cella = coordToCella.get(coord);
            Boolean ini = cella.getInicial();
            if(!ini) cella.deleteSolucio();
        }
    }

    
    /**
     * Retorna la mida total del kenken
     */
    public Integer getTamany(){
        return kenken.getMida();
    }

    /**
     * Comprova si existeix en aquella fila o columna mes de 1 cela amb l'atribut solucio igual a valor
     *
     * @param x representa una coordenada x
     * @param y representa una coordenada y
     * @param valor un enter entre 1 i la mida del kenken
     * @return true si hi ha en la columna o la fila mes de 1 cella amb l'atribut solucio igual a valor
     */
    public Boolean comprovarFilaColumnaPossibilitat(Integer x, Integer y, Integer valor){
        return (kenken.comprovarFilaPossibilitat(x, valor) && kenken.comprovarColPossibilitat(y, valor));
    }

    /**
     * Coloca la solucio amb id_solucio a cada una de les celes del conjunt
     *
     * @param id_cjt l'identificador de un conjunt
     * @param id_solucio l'identificador de la solucio
     * @return els id_solucio eliminats al colocar la solucio.
     */
    public HashSet<Integer> colocarSolucio(Integer id_cjt, Integer id_solucio){
        
        HashSet<Integer> ids_fila = new HashSet<>();
        HashSet<Integer> ids_columna = new HashSet<>();
        HashSet<Integer> ids_totals = new HashSet<>();
        
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {
            Cella cella = coordToCella.get(coord);
            cella.colocarSolucio(id_solucio);

            Integer sol = cella.getSolucio();
            ids_fila = kenken.descartarSolucionsFila(coord.getKey(),sol, id_cjt); 
            ids_columna = kenken.descartarSolucionsColumna(coord.getValue(),sol, id_cjt);
            ids_totals.addAll(ids_columna);
            ids_totals.addAll(ids_fila);

        }

        return ids_totals;
    }

    /**
     * Esborra la solucio a cada una de les celes del conjunt
     */
    public void desferSolucioColocada(){
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {//itera per totes les celes del conjunt
            Cella cella = coordToCella.get(coord);
            cella.deleteSolucio();
        }
    }

    /**
     * Per cada cela del conjunt es comproba si coincideix la seva coordenada x amb x, en cas afirmatiu s'eliminen aquelles possibilitats que coincideixin en valor. Es retornen els id_solucio eliminats. 
     *
     * @param x un numero entre 1 i mida que representa la coordenada x
     * @param valor un numero entre 1 i mida
     * @return els id_solucio eliminats al colocar la solucio.
     */
    public HashSet<Integer> eliminarSolucionsFila(Integer x, Integer valor){
        //recorrer el hash map de posiblitats
        HashSet<Integer> ids_eliminats = new HashSet<>();
        Set<Integer> ids = new HashSet<>();

        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {//recorro las celas del conjunt
            if(coord.getKey() == x){ //cela en la misma fila //COMPROBAR X I Y
                Cella cella = coordToCella.get(coord);
                ids = cella.eliminaSolucio(valor);
                for(Integer id : ids) setPossibilitatFalse(id);
                ids_eliminats.addAll(ids);
            }
        }
        return ids_eliminats;
    }


    /**
     * Per cada cela del conjunt es comproba si coincideix la seva coordenada y amb y, en cas afirmatiu s'eliminen aquelles possibilitats que coincideixin en valor. Es retornen els id_solucio eliminats. 
     *
     * @param y un numero entre 1 i mida que representa la coordenada y
     * @param valor un numero entre 1 i mida
     * @return els id_solucio eliminats al colocar la solucio.
     */public HashSet<Integer> eliminarSolucionsColumna(Integer y, Integer valor){
        //recorrer el hash map de posiblitats
        HashSet<Integer> ids_eliminats = new HashSet<>();
        Set<Integer> ids = new HashSet<>();

        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {//recorro las celas del conjunt
            if(coord.getValue() == y){ //cela en la misma fila //COMPROBAR X I Y
                Cella cella = coordToCella.get(coord);
                ids = cella.eliminaSolucio(valor);
                for(Integer id : ids) setPossibilitatFalse(id);
                ids_eliminats.addAll(ids);
            }
        }
        return ids_eliminats;
    }

    /**
     * torna a posar a true l'atribut de la solucio amb id_solucio a cada una de les celes del conjunt
     *
     * @param id_solucio un enter identificador de solucio
     */
    public void desferEliminarSolucio(Integer id_solucio){
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {//itera per totes les celes del conjunt
            Cella cella = coordToCella.get(coord);
            cella.desferEliminarSolucio(id_solucio);//canvair
        }
        numPossibilitats += 1;
    }

    /**
     * Afegeix la possibilitat de que la cela de coordenades coord tingui el valor valor a la solucio amb id_solucio
     *
     * @param coord les coordenades es correcte i pertanyen al conjunt
     * @param valor un numero entre 1 i mida del kenken
     * @param id_solucio un enter identificador de solucio
     */
    public void afegirPossibilitatTrue(AbstractMap.SimpleEntry<Integer, Integer> coord, Integer valor, Integer id_solucio){
        Cella cella = coordToCella.get(coord);
        cella.afegirPossibilitatTrue(id_solucio, valor);
    }

    /**
     * Per cada cella es coloca la possibilitat amb id_solucio com a false.
     *
     * @param id_solucio un enter valid que existeix com a possibilitat en el conjunt.
     */
    public void setPossibilitatFalse(Integer id_solucio){
        for(AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()){
            Cella cella = coordToCella.get(coord);
            cella.setPossibilitatFalse(id_solucio);
        }
    }

    /**
     * Troba totes les possibilitats disponibles del conjunt.
     *
     * @return set amb totes les id_solucions que estiguin a true.
     */
    public Set<Integer> getPossibilitats(){
        Set<Integer> posiblitats = new HashSet<>();
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) { //NOMES NECESITO ELS ID DE UNA CELA
            Cella cella = coordToCella.get(coord);
            posiblitats.addAll(cella.getPosiblitats());
            return posiblitats;
        }
        return posiblitats;
        
    }

    /**
     * Decrementa el num de possibilitats.
     */
    public void decrementarNposiblitats(){
        numPossibilitats--;
    }

    /**
     * Retorna un objecte cella correspon a les coordenades
     *
     * @param coord les coordenades son correctes i pertanyen al conjunt
     */    
    public Cella getCella(AbstractMap.SimpleEntry<Integer, Integer> coord){
        return coordToCella.get(coord);
    }

    public Boolean celaInicial(AbstractMap.SimpleEntry<Integer, Integer> coord){
        Cella c = coordToCella.get(coord);
        return c.getInicial();
    }

    public void esborrarValuesGenerats(){
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : coordToCella.keySet()) {
            Cella cella = coordToCella.get(coord);
            cella.esborrarNum();
        }
    }
}
