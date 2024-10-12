package classes.domini;
import java.util.HashMap;
import java.util.AbstractMap;

/**
 * Representa una operacio de multiplicacio.
 * 
 * La multiplicacio te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat de la multiplicacio i les possibilitats del resultat.
 */
public class Multiplicacio extends Operacio {
    private static Multiplicacio instance;
    private Integer solucions;

    /**
     * Constructor de la classe Multiplicacio.
     */
    private Multiplicacio () {
    } 

    /**
     * Retorna la instancia de la multiplicacio.
     * 
     * @return Multiplicacio
     */
    public static Multiplicacio ObtenirInstancia() {
        if (instance == null) {
            instance = new Multiplicacio();
        }
        return instance;
    }
    
    /**
     * Calcula el resultat de la multiplicacio.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) {
        if(vector.length == 0) return -1;
        double multiplicacio = vector[0];
        for (int i = 1; i < vector.length; i++) {
            multiplicacio *= vector[i];
        }
        return multiplicacio;
    }

    /**
     * Calcula les possibilitats del resultat de la multiplicacio.
     * 
     * @param resultat Resultat de la multiplicacio.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        Integer restant = resultat;
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
                    restant = restant / valor;
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
        if(celesBuides == 0 && restant != 1) return 0;


        Integer possibilitats = calculRecursiu(celesBuides, valorsIni, restant, cjtCella, array, 0);
        
        return possibilitats;
    }
    
    /**
     * Calcula les possibilitats del resultat de la multiplicacio de forma recursiva.
     * 
     * @param celesBuides Nombre de celles buides.
     * @param valorsIni Valors inicials de les celles.
     * @param restant Restant de la multiplicacio.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @param array Array amb les coordenades de les celles.
     * @param it index de l'array.
     * @return Integer
     */
    private Integer calculRecursiu(Integer celesBuides, HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni, Integer restant, ConjuntCella cjtCella, HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array, Integer it){
        if (celesBuides == 0 && restant == 1) {
            solucions = solucions + 1;
            for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
            }
            return 1;
        } else if (celesBuides == 0 && restant != 1) {
            return 0;
        } else {
            Integer possibilitats = 0;
            if(valorsIni.get(array.get(it)) != 0) {
                possibilitats += calculRecursiu(celesBuides, valorsIni, restant, cjtCella, array, it + 1);
            } else{
                for(int i = cjtCella.getTamany(); i > 0; i--){
                    if(restant % i == 0 && cjtCella.setSolucio(array.get(it), i)){
                        valorsIni.put(array.get(it), i);
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(it).getKey(), array.get(it).getValue(), i)) {
                            possibilitats += calculRecursiu(celesBuides - 1, valorsIni, restant / i, cjtCella, array, it + 1);
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