package classes.domini;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/**
 * Representa un generador de Kenken.
 * 
 * A partir de les dades proporcionades, el generador de Kenken genera un Kenken amb les caracteristiques proposades.
 * Algunes d'aquestes dades son el tamany del Kenken, el nombre maxim de celles que pot tenir un conjunt, el nombre de celes collocades i les operacions.
 */
public class Generar{
    private static Generar instancia = null;
    private Integer mida = 0;
    private Integer nConjunts = 0;
    private HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean> colocades;
    private HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> solucio;
    private HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer> conjunts;

    private HashMap<Integer, Integer> numCeles;
    private HashMap<Integer, Integer> ops;
    private HashMap<Integer, Integer> resultats;

    private HashMap<Integer, Set<AbstractMap.SimpleEntry<Integer, Integer>>> cjt_celes;



    
    /**
     * Constructor de la classe SolucionadorKenken.
     */
    private Generar(){}

    /**
     * Retorna la instancia del generador de Kenken.
     * 
     * @return Generar
     */
    public static synchronized Generar getInstancia()
    {
        if (instancia == null)
            instancia = new Generar();
        return instancia;
    }

    
    /**
     * Retorna un String que representa un Kenken generat a partir de les dades proporcionades.
     * 
     * @param mida Tamany del Kenken.
     * @param maxConjunts Nombre maxim de celles que pot tenir un conjunt.
     * @param ncColocades Nombre de celes collocades.
     * @param operacions Operacions que poden tenir els conjunts.
     * @return String
     */
    public String generarKenken(Integer mida, Integer maxConjunts, Integer ncColocades, Boolean[] operacions)
    {
        this.mida = mida;
        colocades = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Boolean>();
        cjt_celes = new HashMap<Integer, Set<AbstractMap.SimpleEntry<Integer, Integer>>>();
        conjunts = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        solucio = new HashMap<AbstractMap.SimpleEntry<Integer, Integer>, Integer>();
        for(int i = 1; i <= mida; ++i){
            for(int j = 1; j <= mida; ++j){
                colocades.put(new AbstractMap.SimpleEntry<Integer, Integer>(i, j), false);
                conjunts.put(new AbstractMap.SimpleEntry<Integer, Integer>(i, j), -1);
                solucio.put(new AbstractMap.SimpleEntry<Integer, Integer>(i, j), -1);
            }
        }
        this.omplirCeles(1, 1);
        numCeles = new HashMap<Integer, Integer>();
        this.generarConjunts(1, 1, maxConjunts, 0);
        ops = new HashMap<Integer, Integer>();
        this.generarOperacions(operacions, 0);
        resultats = new HashMap<Integer, Integer>();
        this.generarResultat(1, 1);
        this.triarColocades(ncColocades);
        return construirKenken();

    }

    /**
     * Defineix quines celles estan collocades inicialment en el Kenken.
     * 
     * @param ncColocades Nombre de celes collocades que volem aconseguir.
     * @return void
     */
    private void triarColocades(Integer ncColocades){
        while(ncColocades != 0){
            Integer min = 1; 
            Integer max = mida; 

            Random rand = new Random();
            Integer firstRandomNumber =  rand.nextInt(max - min + 1) + min;
            Integer x = firstRandomNumber;
            Integer secondRandomNumber =  rand.nextInt(max - min + 1) + min;
            Integer y = secondRandomNumber;
            if(colocades.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)) == false){
                colocades.put(new AbstractMap.SimpleEntry<Integer, Integer>(x, y), true);
                ncColocades = ncColocades - 1;
            }
        }
    }

    /**
     * Omple recursivament, fent un backtracking, les celles del Kenken amb un valor aleatori satisfent les normes de no repetir el mateix valor per fila i columna.
     * 
     * @param x Coordenada x de la cella.
     * @param y Coordenada y de la cella.
     * @return Boolean
     * 
     */
    private Boolean omplirCeles(Integer x, Integer y){
        if(y > mida) return true;
        else{
            if(x > mida){
                return omplirCeles(1, y+1);
            } else {
                int min = 1; 
                int max = mida; 

                Random rand = new Random();
                int firstRandomNumber =  rand.nextInt(max - min + 1) + min;
                int randomNumber =  firstRandomNumber;
                boolean[] used = new boolean[max+1];
                
                for(int i = 0; i < mida; i++){
                    while(!comprobarCol(x, randomNumber) || !comprobarFil(y, randomNumber) || used[randomNumber]){
                        randomNumber = randomNumber + 1;
                        if(randomNumber > max) randomNumber = min;
                        if(randomNumber == firstRandomNumber) return false;
                    }
                    solucio.put(new AbstractMap.SimpleEntry<Integer, Integer>(x, y), randomNumber);
                    used[randomNumber] = true;
                    if(omplirCeles(x+1, y)) return true;
                    solucio.put(new AbstractMap.SimpleEntry<Integer, Integer>(x, y), -1);
                }
                return false;
            }
        }
    }

    /**
     * Comprova si un nombre n es pot collocar a la fila y.
     * 
     * @param y Fila on es vol collocar el nombre.
     * @param n Nombre que es vol collocar.
     * @return Boolean
     */
    private Boolean comprobarFil(Integer y, Integer n){
        for(int i = 1; i <= mida; ++i){
            if(solucio.get(new AbstractMap.SimpleEntry<Integer, Integer>(i, y)) == n) return false;
        }
        return true;
    }

    /**
     * Comprova si un nombre n es pot collocar a la columna x.
     * 
     * @param x Columna on es vol collocar el nombre.
     * @param n Nombre que es vol collocar.
     * @return Boolean
     */
    private Boolean comprobarCol(Integer x, Integer n){
        for(int i = 1; i <= mida; ++i){
            if(solucio.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, i)) == n) return false;
        }
        return true;
    }

    /**
     * Genera els conjunts del Kenken recursivament tenint en compte no sobrepassar el maxim de celles per conjunt.
     * 
     * @param x Coordenada x de la cella.
     * @param y Coordenada y de la cella.
     * @param maxConjunts Nombre maxim de celles que pot tenir un conjunt.
     * @param n Identificador del conjunt.
     * @return void
     */
    private void generarConjunts(Integer x, Integer y, Integer maxConjunts, Integer n){
        if(y > mida) {
            nConjunts = n;
            return;
        }
        else if(x > mida) generarConjunts(1, y+1, maxConjunts, n);
        else if(conjunts.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)) != -1){
            if(x > mida && y > mida){
                nConjunts = n;
                return;
            } else {
                if(x > mida){
                    generarConjunts(1, y+1, maxConjunts, n);
                } else {
                    generarConjunts(x+1, y, maxConjunts, n);
                }
            }
        } else {
            Integer min = 1; 
            Integer max = maxConjunts; 

            Random rand = new Random();
            Integer firstRandomNumber =  rand.nextInt(max - min + 1) + min;

            Integer randomNumber = dfs(firstRandomNumber, x, y, n, 0);
            numCeles.put(n, randomNumber);
            generarConjunts(x+1, y, maxConjunts, n+1);
            
        }
        
        
    }

    /**
     * Afegeix una coordenada a un conjunt.
     * 
     * @param number Identificador del conjunt.
     * @param coordinate Coordenada a afegir.
     * @return void
     */
    private void addCoordinate(Integer number, AbstractMap.SimpleEntry<Integer, Integer> coordinate) {
        // Get the set of coordinates associated with the number
        Set<AbstractMap.SimpleEntry<Integer, Integer>> coordinates = cjt_celes.get(number);
        if (coordinates == null) {
            // If there's no set of coordinates associated with the number, create a new one
            coordinates = new HashSet<>();
        }
        // Add the new coordinate to the set
        coordinates.add(coordinate);
        // Update the map with the modified set of coordinates
        cjt_celes.put(number, coordinates);
    }


    
    /**
     * Realitza un recorregut en profunditat per a generar els conjunts del Kenken.
     * 
     * @param numC Nombre de celles que ha de tenir el conjunt.
     * @param x Coordenada x de la cella.
     * @param y Coordenada y de la cella.
     * @param n Identificador del conjunt.
     * @param aux Nombre de celles que ja s'han afegit al conjunt.
     * @return Integer
     */
    private Integer dfs(Integer numC, Integer x, Integer y, Integer n, Integer aux){
        if(numC == aux) return aux;
        else if(x < 1 || x > mida || y < 1 || y > mida || conjunts.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y)) != -1){
            return aux;
        } else {
            conjunts.put(new AbstractMap.SimpleEntry<Integer, Integer>(x, y), n);
            addCoordinate(n, new AbstractMap.SimpleEntry<Integer, Integer>(x, y));
            aux = aux + 1;
            if(aux == numC) return numC;
            Integer min = 1; 
            Integer max = 4; 

            Random rand = new Random();
            Integer firstRandomNumber =  rand.nextInt(max - min + 1) + min;

            Integer first = dfs(numC, dirx(firstRandomNumber, x), diry(firstRandomNumber, y), n, aux);
            if(first != numC){
                Integer second = dfs(numC, dirx(firstRandomNumber+1, x), diry(firstRandomNumber+1, y), n, first);
                if(second != numC){
                    Integer third = dfs(numC, dirx(firstRandomNumber+2, x), diry(firstRandomNumber+2, y), n, second);
                    if(third != numC){
                        Integer fourth = dfs(numC, dirx(firstRandomNumber+3, x), diry(firstRandomNumber+3, y), n, third);
                        if(fourth != numC){
                            return fourth;
                        } else return numC;
                    } else return numC;
                } else return numC;
            } else return numC;
             
        }

    }
    
    /**
     * Retorna la coordenada x de la cella seguent en funcio de la direccio.
     * 
     * @param dir Direccio de la cella.
     * @param x Coordenada x de la cella.
     * @return Integer
     */
    private Integer dirx(Integer dir, Integer x){
        if(dir == 1) return x+1;
        else if(dir == 2) return x-1;
        else return x;
    }   

    /**
     * Retorna la coordenada y de la cella seguent en funcio de la direccio.
     * 
     * @param dir Direccio de la cella.
     * @param y Coordenada y de la cella.
     * @return Integer
     */
    private Integer diry(Integer dir, Integer y){
        if(dir == 3) return y+1;
        else if(dir == 4) return y-1;
        else return y;
    }

    /**
     * Genera les operacions dels conjunts del Kenken de forma recursiva, tenint en compte quines operacions podem utilitzar y el numero de celles dels conjunts.
     * 
     * @param operacions Operacions que poden tenir els conjunts.
     * @param n Identificador del conjunt.
     * @return void
     */
    private void generarOperacions(Boolean[] operacions, Integer n){
        if(n == nConjunts) return;
        else{
            Integer min = 1;
            Integer max = 6;

            Random rand = new Random();
            Integer randomNumber =  rand.nextInt(max - min + 1) + min;

            Integer nC = numCeles.get(n);
            switch (nC){
                case 1:
                    ops.put(n, 0);
                    break;
                case 2:
                    while(!operacions[randomNumber]){
                        randomNumber += 1;
                        if(randomNumber > max) randomNumber = min;
                    }
                    ops.put(n, randomNumber);
                    break;
                default:
                    if(randomNumber.equals(2) || randomNumber.equals(4)){
                        randomNumber += 1;
                    }
                    while(!operacions[randomNumber]){
                        randomNumber += 1;
                        if(randomNumber.equals(2) || randomNumber.equals(4)){
                            randomNumber += 1;
                        }
                        if(randomNumber > max) randomNumber = min;
                    }
                    ops.put(n, randomNumber);
                    break;
            }

            generarOperacions(operacions, n+1);
        }

    }

    /**
     * Genera el resultat de les operacions dels conjunts del Kenken de forma recursiva, mirant la operacio del conjunt i els valors de les celles que formen part del conjunt.
     * 
     * @param x Coordenada x de la cella.
     * @param y Coordenada y de la cella.
     * @return void
     */
    private void generarResultat(Integer x, Integer y){
        if(y > mida) return;
        else{
            if(x > mida){
                generarResultat(1, y+1);
            } else {
                Integer res;
                Integer idConj = conjunts.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y));
                Integer num = solucio.get(new AbstractMap.SimpleEntry<Integer, Integer>(x, y));
                Integer op = ops.get(idConj);
                switch(op){
                    case 0:
                        resultats.put(idConj, num);
                        break;
                    case 1:
                        res = resultats.get(idConj) != null ? resultats.get(idConj) : 0;
                        resultats.put(idConj, res + num);
                        break;
                    case 2:
                        res = resultats.get(idConj) != null ? resultats.get(idConj) : 0;
                        if(res == 0){
                            resultats.put(idConj, num);
                        } else {
                            Integer aux = Math.abs(res - num);
                            resultats.put(idConj, aux);
                        }
                        break;
                    case 3:
                        res = resultats.get(idConj) != null ? resultats.get(idConj) : 1;
                        resultats.put(idConj, res * num);
                        break;
                    case 4:
                        res = resultats.get(idConj) != null ? resultats.get(idConj) : 0;
                        if(res == 0){
                            resultats.put(idConj, num);
                        } else {
                            if(num%res == 0) resultats.put(idConj, num/res);
                            else if(res%num == 0) resultats.put(idConj, res/num);
                            else{
                                ops.put(idConj, 1);
                                resultats.put(idConj, num+res);
                            }
                        }
                        break;
                    case 5:
                        res = resultats.get(idConj) != null ? resultats.get(idConj) : 0;
                        if(res == 0){
                            resultats.put(idConj, num);
                        } else {
                            if(res < num) resultats.put(idConj, num);
                        }
                        break;
                    case 6:
                        res = resultats.get(idConj) != null ? resultats.get(idConj) : 0;
                        if(res == 0){
                            resultats.put(idConj, num);
                        } else {
                            if(res > num) resultats.put(idConj, num);
                        }
                        break;
                }
                generarResultat(x+1, y);
            }
        }
    }

    /**
     * Construeix un String en format JSON que representa un Kenken.
     * 
     * @return String
     */
    private String construirKenken(){
        JsonObject kenken = new JsonObject();
        kenken.addProperty("N", mida);
        kenken.addProperty("R", nConjunts);
        JsonArray regions = new JsonArray();
        for(int i = 0; i < nConjunts; ++i){
            JsonObject regio = new JsonObject();
            regio.addProperty("oper", ops.get(i));
            regio.addProperty("result", resultats.get(i));
            regio.addProperty("e", numCeles.get(i));
            JsonArray coordenades = new JsonArray();
            Set<AbstractMap.SimpleEntry<Integer, Integer>> cjt = cjt_celes.get(i);
            for(AbstractMap.SimpleEntry<Integer, Integer> coord : cjt){
                JsonObject crd = new JsonObject();
                crd.addProperty("x", coord.getKey());
                crd.addProperty("y", coord.getValue());
                crd.addProperty("cont", solucio.get(coord));
                if(colocades.get(coord)) crd.addProperty("ini", 1);
                else crd.addProperty("ini", 0);
                coordenades.add(crd);
            }
            regio.add("coordenades", coordenades);
            regions.add(regio);
        }
        kenken.add("regions", regions);
        return kenken.toString();
    }
}