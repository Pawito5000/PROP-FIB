package classes.domini;
import java.util.HashMap;
import java.util.AbstractMap;
/**
 * Representa una operacio de suma.
 * 
 * La suma te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat de la suma i les possibilitats del resultat.
 */
public class Suma extends Operacio {
    private static Suma instance;
    private Integer solucions;

    /**
     * Constructor de la classe Suma.
     */
    private Suma () {
    } 

    /**
     * Retorna la instancia de la suma.
     * 
     * @return Suma
     */
    public static Suma ObtenirInstancia() {
        if (instance == null) {
            instance = new Suma();
        }
        return instance;
    }
    
    /**
     * Calcula el resultat de la suma.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula (double[] vector) {
        if(vector.length == 0) return -1;
        double suma = 0;
        for (int i = 0; i < vector.length; i++) {
            suma += vector[i];
        }
        return suma;
    }

    /**
     * Calcula les possibilitats del resultat de la suma.
     * 
     * @param resultat Resultat de la suma.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        Integer suma = 0;
        Integer celesBuides = 0;
        this.solucions = solucions;
        int it = 0;
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjtCella.getCoordenades()) {
            Integer valor = cjtCella.getCella(coord).getSolucio();
            array.put(it, coord);
            if(valor != 0){
                if(cjtCella.comprovarFilaColumnaPossibilitat(coord.getKey(), coord.getValue(), valor)) {
                    valorsIni.put(coord, valor);
                    suma += valor;
                }
                else {
                    return 0;
                }
            } else {
                valorsIni.put(coord, 0);
                celesBuides++;
            }
            ++it;
        }
        Integer restant = resultat - suma;

        if(restant < 0 || (restant != 0 && celesBuides == 0) || (restant == 0 && celesBuides != 0)) return 0;

            
        Integer possibilitats = calculRecursiu(celesBuides, valorsIni, restant, cjtCella, array, 0);
            
        return possibilitats;
    }
    
     
    /**
     * Calcula les possibilitats del resultat de la suma de forma recursiva.
     * 
     * @param celesBuides Nombre de celles buides.
     * @param valorsIni Valors inicials de les celles.
     * @param restant Restant de la suma.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @param array Array amb les coordenades de les celles.
     * @param it Iterador de l'array.
     * @return Integer
     */
    private Integer calculRecursiu(Integer celesBuides, HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni, Integer restant, ConjuntCella cjtCella, HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array, Integer it){
        if (celesBuides == 0 && restant == 0) {
            solucions = solucions + 1;
            for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
            }
            return 1;
        } else if (celesBuides > 0 && restant == 0 || celesBuides == 0 && restant != 0 || restant < 0) {
            return 0;
        } else {
            Integer possibilitats = 0;
            if(valorsIni.get(array.get(it)) != 0) {
                possibilitats += calculRecursiu(celesBuides, valorsIni, restant, cjtCella, array, it + 1);
            } else{
                for(int i = 1; i <= cjtCella.getTamany(); i++){
                    if(restant - i >= 0 && cjtCella.setSolucio(array.get(it), i)){
                        valorsIni.put(array.get(it), i);
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(it).getKey(), array.get(it).getValue(), i)) {
                            possibilitats += calculRecursiu(celesBuides - 1, valorsIni, restant - i, cjtCella, array, it + 1);
                        }
                        if(cjtCella.deleteSolucio(array.get(it))){
                            valorsIni.put(array.get(it), 0);
                        }
                    }
                }
            }
            return possibilitats;
        }
    }
}


