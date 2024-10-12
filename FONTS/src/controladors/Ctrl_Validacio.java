package controladors;

import classes.domini.Validar;


import classes.domini.Kenken;
import classes.domini.Generar;
/**
 * Representa el controlador de validacio.
 * 
 * El controlador de validacio te un conjunt de validacions.
 * Tambe proporciona metodes per validar les dades del kenken.
 */
public class Ctrl_Validacio {
    private static Ctrl_Validacio instancia;

    /**
     * Constructor de la classe Ctrl_Validacio.
     */
    private Ctrl_Validacio() {
        
    }

    /**
     * Retorna la instancia del controlador de validacio.
     * 
     * @return Ctrl_Validacio
     */
    public static Ctrl_Validacio getInstancia() {
        if (instancia == null) instancia = new Ctrl_Validacio();
        return instancia;
    }

    /**
     * Genera un kenken a partir d'un JSON del kenken.
     * 
     * @param mida mida del kenken.
     * @param nConjunts numero maxim de celles que pot tenir un conjunt
     * @param ncColocades numero de fitxes que volem que siguin collocades
     * @param operacions array de Booleans indicant les operacions que volem fer servir
     * @return String JSON amb el Kenken generat
     */
    public String generarKenken(Integer mida, Integer nConjunts, Integer ncColocades, Boolean[] operacions){
        String KenkenJSON = Generar.getInstancia().generarKenken(mida, nConjunts, ncColocades, operacions);
   
        if(this.validar(KenkenJSON)) {
            return KenkenJSON;
        } else {
            //throw new Exception("El Kenken no es pot crear perque no es correcte.");
        }
        return null;
        //Kenken kenken = Generar.getInstancia().generarKenken(KenkenJSON);
        //aqui depen de com es faci l'algoritme faltaria verificar si te solucio
    }

    /**
     * Crear un Kenken a partir d'un JSON.
     * 
     * @param KenkenJSON JSON del Kenken
     * @return Kenken
     * @throws Exception
     */
    public Boolean crearKenken(String KenkenJSON)throws Exception {
        if(this.validar(KenkenJSON)) {
            return true;
        } else {
            throw new Exception("El Kenken no es pot crear perque no es correcte.");
        }
    }

    /**
     * Valida que el Kenken sigui correcte.
     * 
     * @param KenkenJSON JSON del Kenken
     * @return boolean
     * @throws Exception
     */
    public boolean validarKenken(String KenkenJSON) throws Exception {
        if(validar(KenkenJSON)){
            return true;
        } else {
            throw new Exception("El Kenken no es pot crear perque no es correcte.");
        }
    }

    /**
     * Valida que el Kenken sigui correcte.
     * 
     * @param KenkenJSON JSON del Kenken
     * @return boolean
     */
    private boolean validar(String KenkenJSON){
        return Validar.getInstancia().validarKenken(KenkenJSON);
    }

    /**
     * Crea un Kenken a partir d'un JSON.
     * 
     * @param KenkenJSON JSON del Kenken
     * @return Kenken
     */
    public Kenken crear(String KenkenJSON){
        return new Kenken(KenkenJSON);
    }
}