package classes.domini;

import java.util.HashMap;
import java.util.AbstractMap;

/**
 * Representa una operacio de minim.
 *
 * El minim te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat del minim i les possibilitats del resultat.
 */
public class Minim extends Operacio {
    private static Minim instance;
    private Integer solucions;

    /**
     * Constructor de la classe Minim.
     */
    private Minim () {
    } 

    /**
     * Retorna la instancia del minim.
     * 
     * @return Minim
     */
    public static Minim ObtenirInstancia() {
        if (instance == null) {
            instance = new Minim();
        }
        return instance;
    }
    
    /**
     * Calcula el resultat del minim.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) {
        if(vector.length == 0) return -1;
        double minim = vector[0];
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] < minim) {
                minim = vector[i];
            }
        }
        return minim;
    }

    /**
     * Calcula les possibilitats del resultat del minim.
     * 
     * @param resultat Resultat del minim.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        Integer celesBuides = 0;
        this.solucions = solucions;
        Integer minim = resultat;
        int it = 0;
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        AbstractMap.SimpleEntry<Integer, Integer> coordMinim = null;
        HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjtCella.getCoordenades()) {
            Integer valor = cjtCella.getCella(coord).getSolucio();
            array.put(it, coord);
            if(valor != 0){
                if(cjtCella.comprovarFilaColumnaPossibilitat(coord.getKey(), coord.getValue(), valor)) {
                    valorsIni.put(coord, valor);
                    if(valor == minim) coordMinim = coord;
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
        if(celesBuides == 0 && coordMinim == null) return 0;
        Integer aux = -1;
        Integer possibilitats = calculRecursiu(celesBuides, valorsIni, minim, coordMinim, cjtCella, array, 0, aux);
        
        return possibilitats;
    }
    
    /**
     * Calcula les possibilitats del resultat del minim de forma recursiva.
     * 
     * @param celesBuides Nombre de celles buides.
     * @param valorsIni Valors inicials de les celles.
     * @param minim Resultat del minim.
     * @param coordMinim Coordenades del minim.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @param array Array amb les coordenades de les celles.
     * @param it Iterador.
     * @param aux Auxiliar.
     * @return Integer
     */
    private Integer calculRecursiu(Integer celesBuides, HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni, Integer minim, AbstractMap.SimpleEntry<Integer, Integer> coordMinim, ConjuntCella cjtCella, HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array, Integer it, Integer aux){
        if (celesBuides == 0 && coordMinim != null) {
            solucions = solucions + 1;
            for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
            }
            //possibilitats.put(solucions, valorsIni.values());
            return 1;
        } else if (celesBuides == 0 && coordMinim == null) {
            return 0;
        } else {
            Integer possibilitats = 0;
            if(valorsIni.get(array.get(it)) != 0) {
                possibilitats += calculRecursiu(celesBuides, valorsIni, minim, coordMinim, cjtCella, array, it + 1, aux);
            } else{
                for(int i = minim; i <= cjtCella.getTamany(); i++){
                    if(cjtCella.setSolucio(array.get(it), i)){
                        valorsIni.put(array.get(it), i);
                        if(i == minim && coordMinim == null) {
                            aux = it;
                            coordMinim = array.get(it);
                        }
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(it).getKey(), array.get(it).getValue(), i)) {
                            possibilitats += calculRecursiu(celesBuides - 1, valorsIni, minim, coordMinim, cjtCella, array, it + 1, aux);
                        }
                        if(cjtCella.deleteSolucio(array.get(it))){
                            valorsIni.put(array.get(it), 0);
                            if(i == minim && aux == it) {
                                coordMinim = null;
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