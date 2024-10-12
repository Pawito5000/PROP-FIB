package classes.domini;

import java.util.HashMap;
import java.util.AbstractMap;

/**
 * Representa una operacio de maxim.
 * 
 * La divisio te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat del maxim i les possibilitats del resultat.
 */
public class Maxim extends Operacio {
    private static Maxim instance;
    private Integer solucions;

    /**
     * Constructor de la classe Maxim.
     */
    private Maxim () {
    } 

    /**
     * Retorna la instancia del maxim.
     * 
     * @return Maxim
     */
    public static Maxim ObtenirInstancia() {
        if (instance == null) {
            instance = new Maxim();
        }
        return instance;
    }

    /**
     * Calcula el resultat del maxim.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) {
        if(vector.length == 0) return -1;
        double maxim = vector[0];
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] > maxim) {
                maxim = vector[i];
            }
        }
        return maxim;
    }
    
    /**
     * Calcula les possibilitats del resultat del maxim.
     * 
     * @param resultat Resultat del maxim.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        Integer celesBuides = 0;
        this.solucions = solucions;
        Integer maxim = resultat;
        int it = 0;
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        AbstractMap.SimpleEntry<Integer, Integer> coordMaxim = null;
        HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjtCella.getCoordenades()) {
            Integer valor = cjtCella.getCella(coord).getSolucio();
            array.put(it, coord);
            if(valor != 0){
                if(cjtCella.comprovarFilaColumnaPossibilitat(coord.getKey(), coord.getValue(), valor)) {
                    valorsIni.put(coord, valor);
                    if(valor == resultat) coordMaxim = coord;
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

        if(celesBuides == 0 && coordMaxim == null) return 0;
        Integer aux = -1;
        Integer possibilitats = calculRecursiu(celesBuides, valorsIni, maxim, coordMaxim, cjtCella, array, 0, aux);
        
        return possibilitats;
    }
 
    /**
     * Calcula les possibilitats del resultat del maxim de forma recursiva.
     * 
     * @param celesBuides Nombre de celles buides.
     * @param valorsIni Valors inicials de les celles.
     * @param maxim Resultat del maxim.
     * @param coordMaxim Coordenades del maxim.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @param array Array amb les coordenades de les celles.
     * @param it Iterador de l'array.
     * @param aux Auxiliar per a la cerca del maxim.
     * @return Integer
     */
    private Integer calculRecursiu(Integer celesBuides, HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni, Integer maxim, AbstractMap.SimpleEntry<Integer, Integer> coordMaxim, ConjuntCella cjtCella, HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array, Integer it, Integer aux){
        if (celesBuides == 0 && coordMaxim != null) {
            solucions = solucions + 1;
            for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
            }
            return 1;
        } else if (celesBuides == 0 && coordMaxim == null) {
            return 0;
        } else {
            
            Integer possibilitats = 0;
            if(valorsIni.get(array.get(it)) != 0) {
                possibilitats += calculRecursiu(celesBuides, valorsIni, maxim, coordMaxim, cjtCella, array, it + 1, aux);
            } else{
                for(int i = maxim; i > 0; i--){
                    if(cjtCella.setSolucio(array.get(it), i)){
                        valorsIni.put(array.get(it), i);
                        if(i == maxim && coordMaxim == null) {
                            aux = it;
                            coordMaxim = array.get(it);
                        }
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(it).getKey(), array.get(it).getValue(), i)) {
                            possibilitats += calculRecursiu(celesBuides - 1, valorsIni, maxim, coordMaxim, cjtCella, array, it + 1, aux);
                        }
                        if(cjtCella.deleteSolucio(array.get(it))){
                            valorsIni.put(array.get(it), 0);
                            if(i == maxim && it == aux) {
                                coordMaxim = null;
                                aux = -1;
                            }
                        }
                    }
                }
            }
            return possibilitats;
        }
    }




}