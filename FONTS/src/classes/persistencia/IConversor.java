package classes.persistencia;

/**
 * Representa l'interfice d'un conversor.
 */
public interface IConversor {
    /**
     * Guarda l'string dins del fitxer fileName corresponent.
     * 
     * @param id identificador de la part a escriure.
     * @param user nom de l'usuari.
     * @param info dades a escriure en el fitxer.
     */
    public void Escriure(Integer id, String user, String info);

    /**
     * Retorna l'string del fitxer fileName corresponent.
     * 
     * @param id identificador de la part a llegir.
     * @param user nom de l'usuari.
     * @return string
     */
    public String Llegir(Integer id, String user);

    /**
     * Elimina la part identificada pel id del fitxer fileName corresponent.
     * 
     * @param id identificador de la part a eliminar.
     * @param user nom de l'usuari.
     */
    public void Eliminar(Integer id, String user);
}