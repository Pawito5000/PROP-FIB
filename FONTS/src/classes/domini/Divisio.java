package classes.domini;
import java.util.HashMap;
import java.util.AbstractMap;
/**
 * Representa una operacio de divisio.
 * 
 * La divisio te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat de la divisio i les possibilitats del resultat.
 */
public class Divisio extends Operacio {
    private static Divisio instance;
    private Integer solucions;

    /**
     * Constructor de la classe Divisio.
     */
    private Divisio () {
    } 

    /**
     * Retorna la instancia de la divisio.
     * 
     * @return Divisio
     */
    public static Divisio ObtenirInstancia() {
        if (instance == null) {
            instance = new Divisio();
        }
        return instance;
    }
    
    /**
     * Calcula el resultat de la divisio.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) {
        if(vector.length != 2) return -1;
        double a = vector[0];
        double b = vector[1];
        if(a == 0 || b == 0) {
            return 0;
        }
        if(a > b) {
            return (a / b);
        } else {
            return (b / a);
        }
    }

    /**
     * Calcula les possibilitats del resultat de la divisio.
     * 
     * @param resultat Resultat de la divisio.
     * @param solucions Nombre de solucions possibles.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cjtCella){
        Integer celesBuides = 0;
        this.solucions = solucions;
        int it = 0;
        Integer divisor = null;
        HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array = new HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>>();
        for (AbstractMap.SimpleEntry<Integer, Integer> coord : cjtCella.getCoordenades()) {
            Integer valor = cjtCella.getCella(coord).getSolucio();
            array.put(it, coord);
            if(valor != 0){
                if(cjtCella.comprovarFilaColumnaPossibilitat(coord.getKey(), coord.getValue(), valor)) {
                    valorsIni.put(coord, valor);
                    if(valor < resultat) divisor = valor;                }
                else {
                    return 0;
                }
            } else {
                valorsIni.put(coord, 0);
                celesBuides++;
            }
            ++it;
        }
        Integer possibilitats = 0;
        possibilitats += calculRecursiu(celesBuides, valorsIni, resultat, cjtCella, array, 0, divisor);

        return possibilitats;
    }
 
    /**
     * Calcula les possibilitats del resultat de la divisio de forma recursiva.
     * 
     * @param celesBuides Nombre de celles buides.
     * @param valorsIni Valors inicials de les celles.
     * @param resultat Resultat de la divisio.
     * @param cjtCella Conjunt de celles a les que pertany l'operacio.
     * @param array Array amb les coordenades de les celles.
     * @param it index de l'array.
     * @param divisor Divisor de la divisio.
     * @return Integer
     */
    private Integer calculRecursiu(Integer celesBuides, HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> valorsIni, Integer resultat, ConjuntCella cjtCella, HashMap<Integer, AbstractMap.SimpleEntry<Integer, Integer>> array, Integer it, Integer divisor){
        if (celesBuides == 0){
            Integer div1 = valorsIni.get(array.get(0));
            Integer div2 = valorsIni.get(array.get(1));
            if(div1/div2 == resultat || div2/div1 == resultat) {
                solucions = solucions + 1;
                for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                    cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
                }
                return 1;
            } else {
                return 0;
            }
        } else if(celesBuides == 1){
            Integer buida = 0;
            Integer div1 = valorsIni.get(array.get(0));
            if(valorsIni.get(array.get(0)) == 0) {
                buida = 0;
                div1 = valorsIni.get(array.get(1));
            }
            else {
                buida = 1;
                div1 = valorsIni.get(array.get(0));
            }
            Integer div2 = div1 * resultat;
            Integer possibilitats = 0;
            if(div2 <= cjtCella.getTamany()){
                if(cjtCella.setSolucio(array.get(buida), div2)){
                    if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(buida).getKey(), array.get(buida).getValue(), div2)) {
                        valorsIni.put(array.get(buida), div2);
                        possibilitats += 1;
                        solucions = solucions + 1;
                        for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                            cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
                        }
                    }
                    if(cjtCella.deleteSolucio(array.get(buida))){
                        valorsIni.put(array.get(buida), 0);
                    }
                }
            }
            if(divisor == null && div1 % resultat == 0) {
                div2 = div1 / resultat;
                if(cjtCella.setSolucio(array.get(buida), div2)){
                    if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(buida).getKey(), array.get(buida).getValue(), div2)) {
                        valorsIni.put(array.get(buida), div2);
                        possibilitats += 1;
                        solucions = solucions + 1;
                        for(AbstractMap.SimpleEntry<Integer, Integer> coord : valorsIni.keySet()){
                            cjtCella.afegirPossibilitatTrue(coord, valorsIni.get(coord), solucions);
                        }
                    }
                    if(cjtCella.deleteSolucio(array.get(buida))){
                        valorsIni.put(array.get(buida), 0);
                    }
                }
            }
            return possibilitats;  
        } else {
            Integer possibilitats = 0;
            for(int i = 1; i <= cjtCella.getTamany(); i++){
                Integer div = resultat * i;
                if(div <= cjtCella.getTamany()){
                    if(cjtCella.setSolucio(array.get(0), div)){
                        valorsIni.put(array.get(0), div);
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(0).getKey(), array.get(0).getValue(), div)){
                            if(cjtCella.setSolucio(array.get(1), i)){
                                valorsIni.put(array.get(1), i);
                                if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(1).getKey(), array.get(1).getValue(), i)){
                                    celesBuides = 0;
                                    possibilitats += calculRecursiu(celesBuides, valorsIni, resultat, cjtCella, array, it, divisor);
                                }
                                if(cjtCella.deleteSolucio(array.get(1))){
                                    valorsIni.put(array.get(1), 0);
                                }
                            }
                        }
                        if(cjtCella.deleteSolucio(array.get(0))){
                            valorsIni.put(array.get(0), 0);
                        }
                    }
                }
                if(i % resultat == 0) {
                    div = i / resultat;
                    if(cjtCella.setSolucio(array.get(0), div)){
                        valorsIni.put(array.get(0), div);
                        if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(0).getKey(), array.get(0).getValue(), div)){
                            if(cjtCella.setSolucio(array.get(1), i)){
                                valorsIni.put(array.get(1), i);
                                if(cjtCella.comprovarFilaColumnaPossibilitat(array.get(1).getKey(), array.get(1).getValue(), i)){
                                    celesBuides = 0;
                                    possibilitats += calculRecursiu(celesBuides, valorsIni, resultat, cjtCella, array, it, divisor);
                                }
                                if(cjtCella.deleteSolucio(array.get(1))){
                                    valorsIni.put(array.get(1), 0);
                                }
                            }
                        }
                        if(cjtCella.deleteSolucio(array.get(0))){
                            valorsIni.put(array.get(0), 0);
                        }
                    }
                }
            }
            return possibilitats;
        }        
    }
}