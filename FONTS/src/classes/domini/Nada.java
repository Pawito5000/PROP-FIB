package classes.domini;
import java.util.HashMap;
import java.util.AbstractMap;
/**
 * Representa una operacio de no fer res.
 * 
 * La operacio de no fer res te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat de no fer res i les possibilitats del resultat.
 */
public class Nada extends Operacio {
    private static Nada instance;

    /**
     * Constructor de la classe Nada.
     */
    private Nada () {
    } 

    /**
     * Retorna la instancia de no fer res.
     * 
     * @return Nada
     */
    public static Nada ObtenirInstancia() {
        if (instance == null) {
            instance = new Nada();
        }
        return instance;
    }
    
    /**
     * Calcula el resultat de no fer res.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) {
        if(vector.length != 1) return -1;
        return vector[0];
    }

    /**
     * Calcula les possibilitats del resultat de no fer res.
     * 
     * @param resultat Resultat de no fer res.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        if(cjtCella.getCoordenades().size() == 1){
            for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjtCella.getCoordenades()) {
                if( cjtCella.getCella(coord).getSolucio() == 0){
                    if (cjtCella.setSolucio(coord, resultat)){
                        if(cjtCella.comprovarFilaColumnaPossibilitat(coord.getKey(), coord.getValue(), resultat)) {
                            valorsIni.put(coord, resultat);
                            solucions = solucions + 1;
                            cjtCella.afegirPossibilitatTrue(coord, resultat, solucions);
                            return 1;
                        }
                        if(cjtCella.deleteSolucio(coord)){
                            valorsIni.put(coord, 0);
                            return 0;
                        }
                    }                
                }
                else if(cjtCella.getCella(coord).getSolucio() == resultat){
                    solucions = solucions + 1;
                    cjtCella.afegirPossibilitatTrue(coord, resultat, solucions);
                    return 1;
                }
                return 0;
            }
        }
        return 0;
    }
}