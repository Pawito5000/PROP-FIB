package classes.domini;
/**
 * Factoria d'operacions.
 * 
 * Una factoria d'operacions permet crear una operacio a partir d'un numero identificador d'Operacio.
 */
public class FactoriaOperacions {
    /**
     * Crea una operacio a partir d'un numero identificador d'Operacio.
     * 
     * @param numero Numero identificador de l'operacio.
     * @return Operacio
     */
    static Operacio crearOperacio(int numero) {
        if (numero == 0) {
            return Nada.ObtenirInstancia();
        } else if (numero == 1) {
            return Suma.ObtenirInstancia();
        } else if (numero == 2) {
            return Resta.ObtenirInstancia();
        } else if (numero == 3) {
            return Multiplicacio.ObtenirInstancia();
        } else if (numero == 4) {
            return Divisio.ObtenirInstancia();
        } else if (numero == 5) {
            return Maxim.ObtenirInstancia();
        } else if (numero == 6) {
            return Minim.ObtenirInstancia();
        } else {
            throw new IllegalArgumentException("Numero no valido");
        }
    }
}
