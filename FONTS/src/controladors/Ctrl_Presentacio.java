package controladors;

import classes.presentacio.PrincipalView;
import java.io.FileNotFoundException;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.io.IOException;
import javax.swing.JOptionPane;
/**
 * Representa el controlador de presentacio.
 */
public class Ctrl_Presentacio {
    private static Ctrl_Presentacio instancia;
    
    private Ctrl_Domini ctrlDomini;
    private final PrincipalView vistaPrincipal; 
    /**
     * Constructor de la classe Ctrl_Persistencia.
     */
    public Ctrl_Presentacio() {
        this.ctrlDomini = Ctrl_Domini.getInstancia();
        ctrlDomini.setCtrlPresentacio(this);
        this.vistaPrincipal = new PrincipalView(this);
        vistaPrincipal.setVisible(true);
    }
    
    /**
     * Genera un Kenken amb les especificacions donades i el retorna en format JSON.
     *
     * @param mida La mida del tauler Kenken.
     * @param nConjunts El nombre de conjunts de celes a generar.
     * @param ncColocades El nombre de celes ja collocades inicialment.
     * @param operacions Un array de booleans que indica quines operacions matematiques son permeses en el Kenken.
     * @return Una cadena que conte la representacio JSON del Kenken generat.
     */
    public Integer generarKenken(Integer mida, Integer nConjunts, Integer ncColocades, Boolean[] operacions) throws Exception{
        Integer kenkenJSON = ctrlDomini.generarkenken(mida, nConjunts, ncColocades, operacions);
        return kenkenJSON;
    }

    /**
     * Retorna la instancia del controlador de persistencia.
     *
     * @return Ctrl_Persistencia.
     */
    public static Ctrl_Presentacio getInstancia() {
        if (instancia == null) instancia = new Ctrl_Presentacio();
        return instancia;
    }
    
    /**
     * Retorna la instancia del ranking kenken identificat per idkenken i nom, en format JSON.
     * @param idKenken
     * @param nom
     * @return String
     */
    public String getRanking(int idKenken,String nom) {    
        return ctrlDomini.ObtenirRanquing(idKenken,nom);
    }
    
    /**
     * Retorna la informacio del ranking identificat per id i nom.
     * 
     * @param id
     * @param nom
     * @return String[]
     */
    public String[] getInfoRanking(Integer id, String nom){    
        return ctrlDomini.getInfoRanking(id,nom);
    }

    /**
     * Retorna la duracio de la partida identificada per id i nom.
     * 
     * @param id
     * @param nom
     * @return Duration
     */
    public Duration getTempsPartida(Integer id,String nom){
        return ctrlDomini.getTemps(id,nom);
    }
    
    public Integer getPistesPartida(Integer id,String nom){
        return ctrlDomini.getPistesPartida(id,nom);
    }

    /**
     * Fixa la duracio de la partida identificada per id i nom.
     * 
     * @param id
     * @param nom
     * @param temps
     */
    public void setTempsPartida(Integer id,String nom, Duration temps){
        ctrlDomini.setTemps(id,nom,temps);
    }
    
    /**
     * Retorna el kenken identificat per idkenken i nom.
     * 
     * @param idkenken
     * @param nom
     * @return String
     */
    public String obtenirKenken(Integer idkenken,String nom){
        if (nom.equals("bd")) {
            return ctrlDomini.ObtenirKenkenBD(idkenken);
        } else {
            return ctrlDomini.ObtenirKenkenUser(idkenken);
        }
        
    }
    
    /**
     * Retorna el kenken de la partida identificada per id i nom.
     * 
     * @param id
     * @param nom
     * @return String
     */
    public String obtenirKenkenPartida(int id,String nom){
        return ctrlDomini.ObtenirPartida(id,nom);
    }
    
    /**
     * Obte un string amb tots els kenkens de l'usuari o de la base de dades.
     * 
     * @param nom
     * @return String
     */
    public String getAllKenkens(String nom){
        return ctrlDomini.llegirall(nom);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                //new Ctrl_Presentacio();
            }
        });
    }
    
    /**
     * Genera un JSON representant un tauler de joc KenKen a partir d'una llista d'informacio.
     * 
     * @param info
     * @return String
     */
    public String generarJson(ArrayList<String> info) {
        try{
            String ret = vistaPrincipal.generarJson(info);
            return ret;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error al solucionar el Kenken: " + e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }
    
    /**
     * Ens asegurem que el parametre sigui un kenken valid.
     * 
     * @param Kenken
     * @return String
     * @throws Exception
     */
    public String validarKenken(String Kenken) throws Exception{
        return ctrlDomini.validarKenken(Kenken);
        
    }
    
    /**
     * Soluciona un kenken identificat per idKenken.
     * 
     * @param idKenken
     * @return String
     */
    public String solucionarKenken(Integer idKenken) throws Exception{
        return ctrlDomini.solucionarKenken(idKenken);
        
    }
    
    /**
     * Comprova si el kenken identificat per idKenken existeix a persistencia .
     * 
     * @param nom
     * @return Boolean
     */
    public Boolean existsUsuari(String nom){
        return ctrlDomini.existsUsuari(nom);
    }

    /**
     * Comprova si la partida identificada per id i nom existeix a persistencia o a domini.
     * 
     * @param id
     * @param nom
     * @return Boolean
     */
    public Boolean existeixPartida(Integer id,String nom){
        return ctrlDomini.existsPartida(id,nom);
    }
    
    /**
     * Obte l'usuari amb nom igual a nom, a mes carrega els kenkensBD a domini.
     * 
     * @param nom
     * @throws Exception
     */
    public void getUsuari(String nom) throws Exception {
        ctrlDomini.carregaUsuari(nom);
    }

    
    /**
     * Compova si la contrasenya es correcta.
     * 
     * @param contra
     * @return Boolean
     * @throws Exception
     */
    public Boolean verificarContra(String contra) throws Exception {
        return ctrlDomini.verificarContra(contra);
    }
    

    /**
     * Crea un usuari amb nom igual a nom i contrasenya igual a contra, a mes carrega els kenkensBD a domini.
     * @param nom
     * @param contra
     * @throws Exception
     */
    public void crearUsuari(String nom, String contra) throws Exception{
        ctrlDomini.crearUsuari(nom, contra);
    }
    

    /**
     * Comprova si el ranking del kenken identificat per idKenken existeix a persistencia.
     * 
     * @param idKenken
     * @return Boolean
     */
    public Boolean existeixRanking(Integer idKenken) {
        return ctrlDomini.existsRanquing(idKenken);
    }

    /**
     * 
     * @param x
     * @param y
     * @param idKenken
     * @param nom
     * @return Boolean
     */
    public Boolean celaInicial(Integer x, Integer y, Integer idKenken, String nom){
        return ctrlDomini.celaInicial(x, y, idKenken, nom);
    }

    /**
     * Coloca un valor en la cela de coordenades x i y del kenken identificat per idKenken. Si es pot retorna true, altrament false.
     * 
     * @param valor
     * @param x
     * @param y
     * @param idKenken
     * @param nom
     * @return Boolean
     */
    public Boolean colocarNum(String valor, Integer x, Integer y, Integer idKenken, String nom){
        AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<>(x, y);
        int intValue = Integer.parseInt(valor);
        try{
            ctrlDomini.colocarNum(idKenken, coord, intValue, nom);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    /**
     * Esborra el valor de la cela de coordenades x i y del kenken identificat per idKenken. Si es pot retorna true, altrament false.
     * 
     * @param x
     * @param y
     * @param idKenken
     * @param nom
     * @return Boolean
     */
    public Boolean esborrarNum(Integer x, Integer y, Integer idKenken, String nom){
        AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<>(x, y);
        try{
            ctrlDomini.esborrarNum(idKenken, coord, nom);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Elimina un Kenken de la persistencia.
     * 
     * @param id
     * @param nom
     */
    public void eliminarKenken(Integer id, String nom){
        // Codi de esborrar kenken = 1 
        
        ctrlDomini.esborraElemPersistencia(1,id,nom); 
        ctrlDomini.eliminarKenkenUsuariDomini(id);
    }

    /**
     * Elimina una partida de la persistencia.
     * 
     * @param id
     * @param nom
     */
    public void eliminarPartida(Integer id, String nom){
        ctrlDomini.eliminarPartida(id,nom);
    }

    /**
     * Crea una partida amb nom igual a nom i idKenken igual a idKenken.
     * La partida no ha d'existir previament ni a la bd ni a domini.
     * 
     * @param idKenken
     * @param nom
     */
    public void crearPartida(Integer idKenken, String nom){
        ctrlDomini.crearPartida(idKenken,nom);
    }
    
    public void ajuda(Integer x, Integer y, Integer idKenken, String nom) throws Exception{
        AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<>(x, y);
        ctrlDomini.ajuda(idKenken, coord, nom);
    }
    
    public void ajudaExtra(Integer x, Integer y, Integer idKenken, String nom) throws Exception{
        AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<>(x, y);
        ctrlDomini.ajudaExtra(idKenken, coord, nom);
    }
    
    public String getValorCela(Integer x, Integer y, Integer idKenken, String nom){
        AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<>(x, y);
        return ctrlDomini.getValorCela(idKenken, coord, nom).toString();
    }
    
    public void getTempsPartida(){
        vistaPrincipal.getTempsPartida();
    }
    
    public void stopTemps(){
        vistaPrincipal.stopTemps();
    }
    
    public void continueTemps(){
        vistaPrincipal.continueTemps();
    }
    
    public void finalitzarPartida(){
        vistaPrincipal.finalitzarPartida();
    }

    /**
     * Surt de l'aplicacio.
     */
    public void sortir(){
        ctrlDomini.sortir();
    }
    
    public void pausarPartida(Integer idKenken, String nom, Duration temps){
        ctrlDomini.pausar(idKenken, nom, temps);
    }
    
    public Boolean comprovarFila(Integer id, String nom, Integer x, Integer valor) {
        return ctrlDomini.comprovarFila(id, nom, x, valor);
    }
    
    public Boolean comprovarCol(Integer id, String nom, Integer x, Integer valor) {
        return ctrlDomini.comprovarCol(id, nom, x, valor);
    }
}
