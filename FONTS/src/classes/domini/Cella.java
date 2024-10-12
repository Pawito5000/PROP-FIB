package classes.domini;

import java.util.AbstractMap;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Representa una cella del Kenken.
 * 
 * Una cella pot tenir un valor inicial, un valor, una solucio i possibles solucions.
 * Tambe proporciona metodes per establir i obtenir els valors de la cella.
 */
public class Cella {
    private AbstractMap.SimpleEntry<Integer, Integer> coord;
    private boolean inicial;
    private Integer value = 0;
    private Integer solucio = 0;
     

    private  HashMap<Integer, AbstractMap.SimpleEntry<Integer, Boolean>> idSoltoValor; //id valor bool

    
    
    /**
     * Constructor de la classe Cella.
     * 
     * @param PosX La coordenada x de la cella. Ha de ser un enter mes gran o igual a 0.
     * @param PosY La coordenada y de la cella. Ha de ser un enter mes gran o igual a 0.
     * @param value El valor inicial de la cella. Si es null, el valor de la cella es posa a 0.
     */
    public Cella(int PosX, int PosY, Integer value) 
    {
        this.coord = new AbstractMap.SimpleEntry<>(PosX, PosY);
        if (value == null) this.value = 0;
        else this.value = value;
        this.inicial = false;
        idSoltoValor = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Boolean>>();
    }

    /**
     * Constructor de la classe Cella.
     * 
     * @param PosX La coordenada x de la cella. Ha de ser un enter mes gran o igual a 0.
     * @param PosY La coordenada y de la cella. Ha de ser un enter mes gran o igual a 0.
     * @param value El valor inicial de la cella. Si es null, el valor de la cella es posa a 0.
     * @param inicial Indica si la cella es inicial o no.
     */
    public Cella(int PosX, int PosY, Integer value, boolean inicial) 
    {
        this.coord = new AbstractMap.SimpleEntry<>(PosX, PosY);
        if (value == null) this.value = 0;
        else this.value = value;
        this.inicial = inicial;
        if(inicial) this.solucio = value;
        idSoltoValor = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Boolean>>();

    }

    /**
     * @return JsonObject es genera un Json del objecte Cella
     */
    public JsonObject generateJSON() {
        JsonObject kenkenObject = new JsonObject();
        kenkenObject.addProperty("x", coord.getKey());
        kenkenObject.addProperty("y", coord.getValue());
        if(value != null){
            kenkenObject.addProperty("cont", value);
            kenkenObject.addProperty("ini", inicial ? 1 : 0);
        } 
        return kenkenObject;
    }
    public JsonObject generateSolucio() {
        JsonObject kenkenObject = new JsonObject();
        kenkenObject.addProperty("x", coord.getKey());
        kenkenObject.addProperty("y", coord.getValue());
        if(value != null){
            kenkenObject.addProperty("cont", solucio);
            kenkenObject.addProperty("ini", 1);
        } 
        return kenkenObject;
    }

    /** 
     * Retorna el valor de la cella.
     * 
     * @return Integer
     */
    public Integer getValue()
    {
        return value;
    }

    /**
     * Retorna la solucio de la cella.
     * 
     * @return Integer
     */
    public Integer getSolucio()
    {
        return solucio;
    }

    /**
     * Retorna les coordenades de la cella.
     * 
     * @return {@code AbstractMap.SimpleEntry<Integer, Integer>}
     */
    public AbstractMap.SimpleEntry<Integer, Integer> getCoord()
    {
        return coord;
    }

    /**
     * Retorna si la cella es inicial o no.
     * 
     * @return boolean
     */
    public boolean getInicial()
    {
        return inicial;
    }

    /**
     * Estableix si la cella es inicial o no.
     */
    public void setInicial() 
    {
        this.inicial = true;
        this.solucio = value;
    }
    
    public void setInicial(Integer valueS) 
    {
        if(valueS != 0){
        this.inicial = true;
        this.solucio = valueS;
        }
    }

    /**
     * Estableix el valor de la cella.
     * 
     * @param valor El valor a establir.
     * @return boolean
     */
    public boolean colocarNum(int valor)
    {
        if (!this.inicial || this.value == 0){
            this.value = valor;
            return true;
        } else return false;
    }

    /**
     * Esborra el valor de la cella.
     * 
     * @return boolean
     */
    public boolean esborrarNum()
    {
        if (!this.inicial) {
            this.value = 0;
            return true;
        } else return false;
    }

    /**
     * Estableix la solucio de la cella.
     * 
     * @param valor El valor a establir.
     * @return boolean
     */
    public Boolean setSolucio(int valor)
    {
        
            this.solucio = valor;
            return true;
    
    }

    /**
     * Esborra la solucio de la cella.
     * 
     * @return boolean
     */
    public Boolean deleteSolucio()
    {
        if (!this.inicial) {
            this.solucio = 0;
            return true;
        } else return false;
    }

    /**
     * Colloca la solucio de la cella.
     * 
     * @param id_solucio L'identificador de la solucio.
     */
    public void colocarSolucio(Integer id_solucio)
    {
        if (!this.inicial && idSoltoValor.containsKey(id_solucio)){
            this.solucio = idSoltoValor.get(id_solucio).getKey();
        }
    }

    /**
     * Afegeix el valor d'una possible solucio de la cella.
     * 
     * @param id_solucio L'identificador de la solucio.
     * @param valor El valor a comprovar.
     */
    public void afegirPossibilitatTrue(Integer id_solucio, Integer valor)
    {
        AbstractMap.SimpleEntry<Integer, Boolean> valor_bool = new AbstractMap.SimpleEntry<Integer, Boolean>(valor, true);
        idSoltoValor.put(id_solucio, valor_bool);
    }

    /**
     * Elimina una possible solucio de la cella.
     * 
     * @param valor El valor a eliminar.
     * @return {@code Set<Integer>}
     */
    public Set<Integer> eliminaSolucio(Integer valor)
    {
        Set<Integer> ids_eliminats = new HashSet<>();

        for (Integer id : idSoltoValor.keySet()) {
            if (idSoltoValor.get(id).getKey()==valor && idSoltoValor.get(id).getValue() ) {//conte el id
                ids_eliminats.add(id);
                AbstractMap.SimpleEntry<Integer, Boolean> valor2 = new AbstractMap.SimpleEntry<>(valor, false);
                idSoltoValor.put(id, valor2);
            }
        }

        return ids_eliminats;
    }

    /**
     * Estableix una possible solucio de la cella a false.
     * 
     * @param id_solucio L'identificador de la solucio.
     */
    public void setPossibilitatFalse(Integer id_solucio){
        if(idSoltoValor.containsKey(id_solucio)){
            idSoltoValor.get(id_solucio).setValue(false);
        }
    }


    /**
     * Desfer l'eliminacio d'una possible solucio de la cella.
     */
    public void desferEliminarSolucio(Integer id_solucio){
        if(idSoltoValor.containsKey(id_solucio)){
            idSoltoValor.get(id_solucio).setValue(true);
        }
    }

    /**
     * Retorna les possibles solucions de la cella.
     * 
     * @return {@code Set<Integer>}
     */
    public Set<Integer> getPosiblitats(){
        Set<Integer> posiblitats = new HashSet<>();
        for (Integer id : idSoltoValor.keySet()) {
            if (idSoltoValor.get(id).getValue()) posiblitats.add(id); //nomes les qeu siguin true
        }
        return posiblitats;
    }

    /**
     * Retorna les possibles solucions de la cella.
     * 
     * @return {@code HashMap<Integer, AbstractMap.SimpleEntry<Integer, Boolean>>}
     */
    public HashMap<Integer, AbstractMap.SimpleEntry<Integer, Boolean>> getIdSoltoValor(){
        return idSoltoValor;
    }
}  



