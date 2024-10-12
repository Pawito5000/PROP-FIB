package classes.domini;

/**
 * Representa una operacio.
 * 
 * Una operacio te un resultat i un nombre de solucions possibles.
 * Tambe proporciona metodes per calcular el resultat de l'operacio i les possibilitats del resultat.
 */
public class Operacio {

    /**
     * Constructor de la classe Operacio.
     */
    public Operacio() {

    }   

    /**
     * Calcula el resultat de l'operacio.
     * 
     * @param vector Vector amb els operands.
     * @return double
     */
    public double Calcula(double[] vector) { 
        return vector[0]; 
    }

    /**
     * Calcula les possibilitats del resultat de l'operacio.
     * 
     * @param resultat Resultat de l'operacio.
     * @param solucions Nombre de solucions possibles.
     * @param cella Cella a la que pertany l'operacio.
     * @return Integer
     */
    public Integer calcularPossibilitats(Integer resultat, Integer solucions, ConjuntCella cella){
        Integer possibilitats = 0;
        return possibilitats;
    }
    
}
