package classes.domini;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Stack;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Representa un solucionador de Kenken.
 * 
 * El solucionador de Kenken te un Kenken a solucionar i una cua de conjunts ordenats per possibilitats.
 * Tambe proporciona metodes per solucionar el Kenken.
 */
public class SolucionadorKenken {
    private static SolucionadorKenken instance;
    private Kenken kenken;
    private Stack<Integer> colocades;
    private Stack<Set<Integer>>descartades;
    Integer nCjts; //nombre de conjunts
    //Cua de id_conjunts ordenats per nombre de possibilitats de menor a major
    private PriorityQueue<Integer> conjuntsOrdenatsPossiblitats = new PriorityQueue<>(Comparator.comparingInt(id -> { //id de conjunts ordenats per nombre de possibilitats
        return kenken.getNumPossibilitats(id);
    }));

    private HashMap<Integer, Integer> idSoltoidCjt; // relaciona el id de la solucio amb el id del conjunt de celes al que pertany
    private Integer id_solucio;
    
    Integer idantic = 0; 

    /**
     * Constructor de la classe SolucionadorKenken.
     */
    public SolucionadorKenken() {
        

    }

    /**
     * Retorna la instancia del solucionador de Kenken.
     * 
     * @return SolucionadorKenken
     */
    /*public static SolucionadorKenken ObtenirInstancia() {
        if (instance == null) {
            instance = new SolucionadorKenken();
        }
        return instance;
    }*/

    /**
     * Estableix el Kenken a solucionar.
     * 
     * @param kenken Kenken a solucionar.
     */
    public void setKenken(Kenken kenken) {
        this.kenken = kenken;
    }

    /**
     * Retorna el Kenken solucionat.
     * 
     * @param kenkeini Kenken a solucionar.
     * @return Kenken
     */
    public Kenken SolucionarKenKen(Kenken kenkenPartida) {
        idSoltoidCjt = new HashMap<>();
        colocades = new Stack<>();
        descartades = new Stack<>();
        kenken = null;
        nCjts = 0;
        id_solucio = 0;
        
        this.kenken = new Kenken(kenkenPartida.generateJSON().toString());
        nCjts = kenken.getcjtCeles().size();
        idSoltoidCjt = new HashMap<>();
        construirSolucions();
        solucionarkenkenRecursivo();
        //kenken.deleteValueSolucio();
        //throw new IllegalArgumentException("solucionsss"+ colocades);
        return kenken;
    }

    /**
     * Retorna el Kenken solucionat.
     * 
     * @param kenkeini Kenken a solucionar.
     * @return Kenken
     */
    public Kenken SolucionarKenKenIni(Kenken kenkeini) {
        idSoltoidCjt = new HashMap<>();
        colocades = new Stack<>();
        descartades = new Stack<>();
        kenken = null;
        nCjts = 0;
        id_solucio = 0;

        this.kenken = new Kenken(kenkeini.generateJSON().toString());
        nCjts = kenken.getcjtCeles().size();
        idSoltoidCjt = new HashMap<>();
        construirSolucionsInicial();
        solucionarkenkenRecursivo();
        //throw new IllegalArgumentException("solucionsss"+ colocades);
        return kenken;
    }

    /**
     * Retorna el Kenken solucionat.
     * 
     * @return Kenken
     */
    public Boolean solucionarkenkenRecursivo() {
        if (colocades.size() == nCjts) { //si ja s'han collocat totes les solucions
            return true;
        }
        if (conjuntsOrdenatsPossiblitats.isEmpty()) { //si no hi ha mes conjunts per provar || si hi ha algun conjunt amnb 0 possibilitats
            return false;
        }
        Integer id_cjt = conjuntsOrdenatsPossiblitats.poll();

        if(kenken.getNumPossibilitats(id_cjt) == 0) { //si el conjunt no te possibilitats
            conjuntsOrdenatsPossiblitats.add(id_cjt);
            return false;
        }
        for (Integer id_sol : kenken.getPossibilitats(id_cjt)) { //itero per todas les posiblitats de un conjunt
            colocarSolucio(id_sol); //
            Boolean aux = solucionarkenkenRecursivo();
            if (aux) return true;
            desferSolucio();
            
        }
        if(conjuntsOrdenatsPossiblitats.size() != nCjts-1){
            conjuntsOrdenatsPossiblitats.add(id_cjt);
        }
        
        return false;
    }

    /**
     * Construeix les possibilitats de cada conjunt de celles.
     */
    public void construirSolucionsInicial() {
        id_solucio = 0;
        idantic = 0;

        kenken.getNumCjts().forEach(id_cjt -> {
            //calcula les possibilitats de cada conjunt, se li passa id_solucio per tenir un id unic per cada solucio
            id_solucio = kenken.calcularPossibilitats(id_cjt, id_solucio);
            for (int i = idantic; i <= id_solucio; i++) { //assigna a cada solucio el conjunt al que pertany
                idSoltoidCjt.put(i+1, id_cjt);
            }
            idantic = id_solucio;
            conjuntsOrdenatsPossiblitats.add(id_cjt);
        });
        kenken.deleteValueSolucio();
    }

    /**
     * Construeix les possibilitats de cada conjunt de celles.
     */
    public void construirSolucions() {
        id_solucio = 0;
        idantic = 0;
        kenken.setValuesSolucio();
        kenken.getNumCjts().forEach(id_cjt -> {
            //calcula les possibilitats de cada conjunt, se li passa id_solucio per tenir un id unic per cada solucio
            id_solucio = kenken.calcularPossibilitats(id_cjt, id_solucio);
            for (int i = idantic; i <= id_solucio; i++) { //assigna a cada solucio el conjunt al que pertany
                idSoltoidCjt.put(i, id_cjt);
            }
            idantic = id_solucio;
            conjuntsOrdenatsPossiblitats.add(id_cjt);
        });
        kenken.deleteValueSolucio();
    }

    
    /**
     * Ordena les possibilitats d'un conjunt de celles.
     * 
     * @param id Identificador del conjunt de celles.
     */
    public void ordenarSolucions(Integer id) {
        conjuntsOrdenatsPossiblitats.remove(id);
        conjuntsOrdenatsPossiblitats.add(id);
    }

    /**
     * Colloca una solucio en el Kenken.
     * 
     * @param id_sol Identificador de la solucio.
     */
    public void colocarSolucio(Integer id_sol){    

        Set<Integer> ids_descartats = new HashSet<>();

        //colocar la solucio i descarta les solucions de la fila i columna
        colocades.push(id_sol);
        ids_descartats = kenken.colocarSolucio(idSoltoidCjt.get(id_sol), id_sol);
        
        //posar els ids descartats a descartades
        if(ids_descartats.size() == 0) ids_descartats.add(-1);
        else{
            for(Integer id : ids_descartats){
                if(conjuntsOrdenatsPossiblitats.contains(idSoltoidCjt.get(id))){
                    kenken.getConjuntCella(idSoltoidCjt.get(id)).decrementarNposiblitats();
                    ordenarSolucions(idSoltoidCjt.get(id));
                }
            }
        }
        descartades.push(ids_descartats);
    }

    /**
     * Desfa la solucio collocada.
     */
    public void desferSolucio(){   
        Integer id_sol = colocades.pop();
        Set<Integer> ids_descartades = descartades.pop();
        //treu la colocada i coloca les descartades
        kenken.desferSolucioColocada(idSoltoidCjt.get(id_sol));

        for(Integer id : ids_descartades){
            if(id != -1) {
                kenken.desferEliminarSolucio(idSoltoidCjt.get(id), id);

                if(conjuntsOrdenatsPossiblitats.contains(idSoltoidCjt.get(id))){
                    ordenarSolucions(idSoltoidCjt.get(id));
                }
            }
        }
        
    }

    /**
     * Retorna la cua de conjunts ordenats per possibilitats.
     * 
     * @return {@code PriorityQueue<Integer>}
     */
    public PriorityQueue<Integer> getConjuntsOrdenatsPossiblitats() {
        return conjuntsOrdenatsPossiblitats;
    }

    /**
     * Retorna el primer identificador de conjunt de celles de la cua.
     * 
     * @return Integer
     */
    public Integer getFirstIdCjt() {
        return conjuntsOrdenatsPossiblitats.peek();
    }

    /**
     * Elimina el primer element de la cua.
     */
    public void getFirstPop() {
        conjuntsOrdenatsPossiblitats.poll();
    }

    /**
     * Retorna les celles collocades.
     * 
     * @return {@code Stack<Integer>}
     */
    public Stack<Integer> getcolocades() {
        return colocades;
    }

    /**
     * Retorna les celles descartades.
     * 
     * @return {@code Stack<Set<Integer>>}
     */
    public Stack<Set<Integer>> getdescartades() {
        return descartades;
    }
}