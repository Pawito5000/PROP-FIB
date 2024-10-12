package classes.domini;
import java.util.HashMap;
import java.util.AbstractMap;
import java.lang.Math;

/**
 * Representa una operacio de resta.
 * 
 * La resta te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat de la resta i les possibilitats del resultat.
 */
public class Resta extends Operacio {
    private static Resta instance;
    private Integer solucions;

    /**
     * Constructor de la classe Resta.
     */
    private Resta () {
    } 

    /**
     * Retorna la instancia de la resta.
     * 
     * @return Resta
     */
    public static Resta ObtenirInstancia() {
        if (instance == null) {
            instance = new Resta();
        }
        return instance;
    }
    
    /**
     * Calcula el resultat de la resta.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) {
        if(vector.length != 2) return -1;
        double a = vector[0];
        double b = vector[1];

        if(a > b) {
            return a - b;
        } else {
            return b - a;
        }
    }

    /**
     * Calcula les possibilitats del resultat de la resta.
     * 
     * @param resultat Resultat de la resta.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        Integer celesBuides = 0;
        this.solucions = solucions;
        int it = 0;
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjtCella.getCoordenades()) {
            Integer valor = cjtCella.getCella(coord).getSolucio();
            array.put(it, coord);
            if (valor ==  0) {
                valorsIni.put(coord, 0);
                ++celesBuides;
            } else {
                if(cjtCella.comprovarFilaColumnaPossibilitat(coord.getKey(), coord.getValue(), valor)) {
                    valorsIni.put(coord, valor);
                }
                else {
                    return 0;
                }
            }
            ++it;
        }


        Integer possibilitats = calculRecursiu(celesBuides, valorsIni, resultat, cjtCella, array, 0);
        
        return possibilitats;
    }
 
    /**
     * Calcula les possibilitats del resultat de la resta de forma recursiva.
     * 
     * @param celesBuides Nombre de celles buides.
     * @param valorsIni Valors inicials de les celles.
     * @param resultat Resultat de la resta.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @param array Array amb les coordenades de les celles.
     * @param it index de l'array.
     * @return Integer
     */
    private Integer calculRecursiu(Integer celesBuides, HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni, Integer resultat, ConjuntCella cjtCella, HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array, Integer it){
        if (celesBuides == 0){
            AbstractMap.SimpleEntry<Integer, Integer> coord1 = array.get(0);
            AbstractMap.SimpleEntry<Integer, Integer> coord2 = array.get(1);

            Integer restant = Math.abs(valorsIni.get(coord1) - valorsIni.get(coord2));
            if(restant == resultat) {
                solucions = solucions + 1;
                //possibilitats.put(solucions, valorsIni.values());
                for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                    cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
                }
                return 1;
            } else {
                return 0;
            }
        } else {
            Integer possibilitats = 0;
            if(valorsIni.get(array.get(it)) != 0) {
                possibilitats += calculRecursiu(celesBuides, valorsIni, resultat, cjtCella, array, it + 1);
            } else{
                for(int i = 1; i <= cjtCella.getTamany(); i++){
                    if(cjtCella.setSolucio(array.get(it), i)){
                        valorsIni.put(array.get(it), i);
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(it).getKey(), array.get(it).getValue(), i)) {
                            possibilitats += calculRecursiu(celesBuides - 1, valorsIni, resultat, cjtCella, array, it + 1);
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