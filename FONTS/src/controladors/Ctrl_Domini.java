package controladors;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.gson.*;
import java.util.ArrayList;

import classes.domini.Usuari;
import controladors.Ctrl_Persistencia;
import controladors.Ctrl_Presentacio;
import classes.domini.Ranquing;
import classes.domini.Kenken;
import classes.domini.Partida;

import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Representa el controlador de domini.
 * 
 * El controlador de domini te un conjunt de partides i ranquings.
 * Tambe proporciona metodes per gestionar les partides i ranquings.
 */
public class Ctrl_Domini {
    private static Ctrl_Domini instancia;
    private Ctrl_Persistencia ctrlPersistencia;
    private Ctrl_Presentacio ctrlPresentacio;
    private HashMap<Integer, Partida> partidesBD;
    private HashMap<Integer, Partida> partidesUser;

    private HashMap<Integer, Ranquing> ranquings;
    private HashMap<Integer, Kenken> kenkensUser;
    private HashMap<Integer, Kenken> kenkensBD;
    private Integer hello = 0;
    private Integer id = 1;
    private Usuari usuari = null;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;
    
    
    
    /**
     * Constructor de la classe Ctrl_Domini.
     */
    private Ctrl_Domini() {
        partidesBD = new HashMap<>();
        partidesUser = new HashMap<>();
        ranquings = new HashMap<>();
        kenkensUser = new HashMap<>();
        kenkensBD = new HashMap<>();
        ctrlPersistencia = Ctrl_Persistencia.getInstancia();
        //this.ctrlPresentacio = ctrlPresentacio;
    } 
    
    public void setCtrlPresentacio(Ctrl_Presentacio ctrlPresentacio){
        this.ctrlPresentacio = ctrlPresentacio;
    }

        
    /**
     * Retorna la instancia del controlador de domini.
     * 
     * @return Ctrl_Domini
     */
    public static Ctrl_Domini getInstancia() {
        if (instancia == null) instancia = new Ctrl_Domini();
        return instancia;
    }

    /**
     * Retorna les partides.
     * 
     * @param idrank identificador del ranquing
     * @param ranks ranquing
     */
    public void setranquings(Integer idrank, Ranquing ranks) {
        this.ranquings.put(idrank, ranks); 
    }

    /**
     * Crea un usuari.
     * 
     * @param nom Nom de l'usuari.
     * @param contrasenya Contrasenya de l'usuari.
     */
    public void crearUsuari(String nom, String contrasenya) throws Exception{
        usuari = new Usuari(nom, contrasenya);
        ctrlPersistencia.GuardarUsuari(null, nom, usuari.toJson().toString());
        carregaKenkensBD();
        getAllKenkens(usuari.getNom());
    }

    /**
     * Retorna el string si el kenken KenkenJSON es valid, null altrament.
     * Si es valid, el guarda a domini.
     * 
     * @return String
     */
    public String validarKenken(String KenkenJSON) throws Exception {
        Ctrl_Validacio validador = Ctrl_Validacio.getInstancia(); // Obtener la instancia unica del singleton
        if(validador.crearKenken(KenkenJSON)){
            Kenken k = validador.crear(KenkenJSON);
            if(k == null) return null;
            Integer id = k.getIdKenken();
            kenkensUser.put(id, k);
            hello = 9;
            return k.generateJSON().toString();
        } // Llamar al metodo generarKenken en la instancia
        return null;
    }

    /**
     * Soluciona el kenken KenkenJSON.
     * 
     * @param idKenken id del kenken a solucionar  
     * @return String
     */
    public String solucionarKenken(Integer idKenken) throws Exception{
        Kenken k;
        try{
            k = kenkensUser.get(idKenken);
        } catch (Exception e) {
            throw new Exception("No existeix el kenken amb aquest id.");
        }
        return k.KenkenSolucionatJSON();

    }

    /**
     * Genera un kenken a partir del JSON.
     * 
     * @param mida mida del kenken.
     * @param nConjunts numero maxim de celles que pot tenir un conjunt
     * @param ncColocades numero de fitxes que volem que siguin collocades
     * @param operacions array de Booleans indicant les operacions que volem fer servir
     * @return String
     */
    public Integer generarkenken(Integer mida, Integer nConjunts, Integer ncColocades, Boolean[] operacions) throws Exception{
        Ctrl_Validacio validador = Ctrl_Validacio.getInstancia(); // Obtener la instancia unica del singleton
        String kenkenJSON = validador.generarKenken(mida, nConjunts, ncColocades, operacions); // Llamar al metodo generarKenken en la instancia
        if(validador.crearKenken(kenkenJSON)){
            Kenken k = validador.crear(kenkenJSON);
            k.esborrarValuesGenerats();
            if(k == null) return null;
            Integer id = k.getIdKenken();
            kenkensUser.put(id, k);
            
            return id;
        } 
        return null;
    }

    /**
     * Retorna el temps de la partida amb identificador idkenken.
     * 
     * @param idkenken
     * @param nom
     * @return Duration
     */
    public Duration getTemps(Integer idkenken, String nom) {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idkenken);
        else p = partidesUser.get(idkenken);
        return p.getTemps();

    }
    
    public Integer getPistesPartida(Integer idkenken, String nom) {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idkenken);
        else p = partidesUser.get(idkenken);
        return p.getnPistes();

    }

    /**
     * Actualitza el temps de la partida amb identificador idkenken.
     * 
     * @param idkenken
     * @param nom
     * @param temps
     */
    public void setTemps(Integer idkenken, String nom, Duration temps) {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idkenken);
        else p = partidesUser.get(idkenken);
        p.setTemps(temps);
    }

    /**
     * Retorna el kenken amb idkenken emmagatzemat a la BD.
     * 
     * @param idKenken
     * @return String
     */
    public String ObtenirKenkenBD(Integer idKenken) {
        String k = kenkensBD.get(idKenken).generateJSON().toString();
        return k;
    }

    /**
     * Retorna el kenken amb idkenken emmagatzemat a l'usuari.
     * 
     * @param idKenken
     * @return String
     */
    public String ObtenirKenkenUser(Integer idKenken) {
        Kenken k = kenkensUser.get(idKenken);
        return k.generateJSON().toString();
    }
    
    /**
     * Retorna un string amb tots els kenekens emmagatmes a persistencia, ja siguin de l'usuari o de la BD.
     * 
     * @param nom
     * @return String
     */
    public String getAllKenkens(String nom) {
        if(nom.equals("bd")){
            return ctrlPersistencia.llegirall(nom);
        }else{
            return ctrlPersistencia.llegirkenkenuser(nom);
        }
    }

    /**
     * Colloca un numero en la cella coord.
     * 
     * @param idpartida Identificador de la partida.
     * @param coord Coordenades de la cella.
     * @param valor Valor a collocar.
     * @throws Exception
     */
    public void colocarNum(Integer idpartida, AbstractMap.SimpleEntry<Integer, Integer> coord, Integer valor, String nom) throws Exception {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idpartida);
        else p = partidesUser.get(idpartida);
        if(!p.colocarNum(coord, valor)){
            //la cella es incial o ja te numero
            throw new Exception("No es pot col.locar el numero en aquesta cel.la perque no compleix les restriccions.");
        } else {
            //fer visualitzacio
            Integer ncColocades = p.getNcColocades();
            Integer mida = p.getMida();
            if(ncColocades == mida * mida) {
                ctrlPresentacio.stopTemps();
                if(p.finalitzarPartida()){
                    ctrlPresentacio.getTempsPartida();
                    if(nom.equals("bd")) ActualitzarRanquing(idpartida);
                    if(nom.equals("bd")) {
                        if (existsPartida(p.getIdKenken(),"bd")) ctrlPersistencia.EliminarPartida(p.getIdKenken(),"BD"+usuari.getNom());      
                        p = partidesBD.remove(idpartida);
                        
                    }
                    else {
                        if (existsPartida(p.getIdKenken(),usuari.getNom()))ctrlPersistencia.EliminarPartida(p.getIdKenken(),usuari.getNom());
                        p = partidesUser.remove(idpartida);
                    }
                    
                    ctrlPresentacio.finalitzarPartida();
                    
                }
                else ctrlPresentacio.continueTemps();
            }
        }
    }

    /**
     * Esborra el numero de la cella coord.
     * 
     * @param idpartida Identificador de la partida.
     * @param coord Coordenades de la cella.
     * @throws Exception
     */
    public void esborrarNum(Integer idpartida, AbstractMap.SimpleEntry<Integer, Integer> coord, String nom) throws Exception {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idpartida);
        else p = partidesUser.get(idpartida);
        if(!p.esborrarNum(coord)){
            //la cella es incial o no te numero
            throw new Exception("No es pot col.locar el numero en aquesta cel.la perque no compleix les restriccions.");
        } else {
            //fer visualitzacio
        }
    }

    /**
     * Crea una partida amb el kenken identificat per KenkenID.
     * La partida no ha d'existir previament ni a la bd ni a domini.
     * 
     * @param KenkenJSON JSON amb el kenken.
     */
    public void crearPartida(Integer KenkenID, String user) 
    {
        Kenken k;
        if(user == "bd") {
            k = kenkensBD.get(KenkenID);
            
            Partida p = new Partida(k, usuari);
            partidesBD.put(KenkenID,p);
        }
        else {
            k = kenkensUser.get(KenkenID);
            partidesUser.put(KenkenID,new Partida(k, usuari));
        }
        
    }
    

    /**
     * Retorna si la cela en la posicio x,y de la partida amb identificador idKenken es inicial.
     * 
     * @param x
     * @param y
     * @param idKenken
     * @param nom
     * @return Boolean
     */
    public Boolean celaInicial(Integer x, Integer y, Integer idKenken, String nom){
        Partida p;
        if(nom == "bd") p = partidesBD.get(idKenken);
        else p = partidesUser.get(idKenken);
        AbstractMap.SimpleEntry<Integer, Integer> coord = new AbstractMap.SimpleEntry<>(x, y);
        return p.celaInicial(coord);
    }
    
    public Integer getValorCela(Integer idKenken, AbstractMap.SimpleEntry<Integer, Integer> coord, String nom){
        Partida p;
        if(nom == "bd") p = partidesBD.get(idKenken);
        else p = partidesUser.get(idKenken);
        return p.getValorCela(coord);
    }

    /**
     * Retorna una pista del Kenken de la partida amb identificador id.
     * 
     * @param id Identificador de la partida.
     * @param coord Coordenades de la cella.
     * @throws Exception
     */
    public void ajuda(Integer id, AbstractMap.SimpleEntry<Integer, Integer> coord, String nom) throws Exception {
        if (nom.equals("bd")){
            if (partidesBD.get(id).getnPistes() == 0) {
                throw new Exception("No queden pistes disponibles."); 
            } else {
                Partida p = partidesBD.get(id);
                p.ajuda(coord, id, nom);
                Integer ncColocades = p.getNcColocades();
                Integer mida = p.getMida();
                if(ncColocades == mida * mida) {
                    ctrlPresentacio.stopTemps();
                    if(p.finalitzarPartida()){
                        ctrlPresentacio.getTempsPartida();
                        if(nom.equals("bd")) ActualitzarRanquing(id);
                        if(nom.equals("bd")) {
                            if (existsPartida(p.getIdKenken(),"bd")) ctrlPersistencia.EliminarPartida(p.getIdKenken(),"BD"+usuari.getNom());      
                            p = partidesBD.remove(id);

                        }
                        else {
                            if (existsPartida(p.getIdKenken(),usuari.getNom()))ctrlPersistencia.EliminarPartida(p.getIdKenken(),usuari.getNom());
                            p = partidesUser.remove(id);
                        }
                        ctrlPresentacio.finalitzarPartida();
                    }
                    else ctrlPresentacio.continueTemps();
                }
            }
        } else {
            if (partidesUser.get(id).getnPistes() == 0) {
                throw new Exception("No queden pistes disponibles."); 
            } else {
                Partida p = partidesUser.get(id);
                p.ajuda(coord, id, nom);
                Integer ncColocades = p.getNcColocades();
                Integer mida = p.getMida();
                if(ncColocades == mida * mida) {
                    ctrlPresentacio.stopTemps();
                    if(p.finalitzarPartida()){
                        ctrlPresentacio.getTempsPartida();
                        if(nom.equals("bd")) ActualitzarRanquing(id);
                        if(nom.equals("bd")) {
                            if (existsPartida(p.getIdKenken(),"bd")) ctrlPersistencia.EliminarPartida(p.getIdKenken(),"BD"+usuari.getNom());      
                            p = partidesBD.remove(id);

                        }
                        else {
                            if (existsPartida(p.getIdKenken(),usuari.getNom()))ctrlPersistencia.EliminarPartida(p.getIdKenken(),usuari.getNom());
                            p = partidesUser.remove(id);
                        }
                        ctrlPresentacio.finalitzarPartida();
                    }
                    else ctrlPresentacio.continueTemps();
                }
            }
        }
    }
    
    public void ajudaExtra(Integer id, AbstractMap.SimpleEntry<Integer, Integer> coord, String nom) throws Exception {
        if (nom.equals("bd")){
            if (partidesBD.get(id).getnPistes() == 0) {
                throw new Exception("No queden pistes disponibles."); 
            } else {
                partidesBD.get(id).ajudaExtra(coord, id, nom);
            }
        } else {
            if (partidesUser.get(id).getnPistes() == 0) {
                throw new Exception("No queden pistes disponibles."); 
            } else {
                partidesUser.get(id).ajudaExtra(coord, id, nom);
            }
        }
    }

    /**
     * Pausa la partida amb identificador id.
     * 
     * @param id Identificador de la partida.
     */
    public void pausar(Integer id, String nom, Duration temps)
    {
        if(nom.equals("bd")){
            if(partidesBD.containsKey(id)) partidesBD.get(id).pausar(temps);
        } else {
            if(partidesUser.containsKey(id)) partidesUser.get(id).pausar(temps);
        }
    }


    /**
     * Retorna en format JSON la partida amb identificador idKenken.
     * 
     * @param idKenken
     * @param nom
     * @return String
     */
    public String obtenirKenkenPartida(Integer idKenken, String nom){
        if(nom.equals("bd")) {
            return partidesBD.get(idKenken).getKenkenJSON();
        }else {
            return partidesUser.get(idKenken).getKenkenJSON();
        }
    }
    
    public String getKenken(Integer idKenken, String nom){
        if(nom.equals("bd")) {
            return ObtenirKenkenBD(idKenken);
        }
        else {
            return ObtenirKenkenUser(idKenken);
                
        }
    }

    /**
     * Reanuda la partida amb identificador id.
     * 
     * @param id Identificador de la partida.
     */
    public void reanudar(Integer id, String nom) 
    {
        if(nom.equals("bd")){
            partidesBD.get(id).reanudar();
        } else {
            partidesUser.get(id).reanudar();
        }
    }

    /**
     * Actualitza el ranquing de la partida amb identificador idpartida.
     * 
     * @param idpartida Identificador de la partida.
     */
    public void ActualitzarRanquing(Integer idpartida) {
        Partida partida = partidesBD.get(idpartida);
        Duration time = partida.getTemps();
        String username = partida.getUsuari();
        Integer idKenken = partida.getIdKenken();

        Ranquing r = ranquings.get(idKenken);
        if (r == null) {
            //Llamar funcion en persistencia que te devuelva el json 
            Ctrl_Persistencia pers = Ctrl_Persistencia.getInstancia();
            String RanquingJSON = pers.ObtenirRanquing(idKenken,null);
            r = CrearRanquing(RanquingJSON);
            //r = new Ranquing(idKenken, new ArrayList<>());
            ranquings.put(idKenken, r);
        }
        r.ActualitzarRanquing(time, username);
    }

    /**
     * Crea un ranquing a partir del JSON.
     * 
     * @param RanquingJSON String JSON amb el ranquing.
     * @return Ranquing
     */
    public Ranquing CrearRanquing(String RanquingJSON) {
        Ranquing ranquing = new Ranquing(RanquingJSON);
        return ranquing;
    }

    /**
     * Surt de l'aplicacio.
     */
    public void sortir() 
    {
        scheduler.schedule(() -> System.exit(0), 5, TimeUnit.SECONDS); //Controla el temps de guardar les dades
        guardaPersistencia();
    }

    //FUNCIONS DE PERSISTENCIA -----------------------------------------------------------------------------

    /**
     * Carrega un usuari
     * 
     * @param nom
     * @throws Exception
     */
    public void carregaUsuari(String nom) throws Exception {
        String usuariJSON = ctrlPersistencia.ObtenirUsuari(null,nom);
        usuari = new Usuari(usuariJSON);
        carregaKenkensBD();
        carregaKenkensUser();
    }


    /**
     * Carrega els kenkens de la BD a domini.
     */
    public void carregaKenkensBD() {
        /* TODO: el problema esta si a la hora de cargar lo metemos en dominio, que para eso es esta funcion, que llamaria a llegir all  */        
        String listaKen = ctrlPersistencia.llegirall("kenken");
        JsonArray jsonArray = JsonParser.parseString(listaKen).getAsJsonArray();
        for (JsonElement element : jsonArray) {
            
            Kenken k = new Kenken(element.toString());
            Integer idKenken = k.getIdKenken();
            
            kenkensBD.put(idKenken, k);
        }
    }
    
    public void carregaKenkensUser() {
        /* TODO: el problema esta si a la hora de cargar lo metemos en dominio, que para eso es esta funcion, que llamaria a llegir all  */        
        String listaKen = ctrlPersistencia.llegirkenkenuser(usuari.getNom());
        JsonArray jsonArray = JsonParser.parseString(listaKen).getAsJsonArray();
        for (JsonElement element : jsonArray) {
            Kenken k = new Kenken(element.toString());
            Integer idKenken = k.getIdKenken();
            kenkensUser.put(idKenken, k);
        }
    }

    /**
     * Obte el kenken de persistencia, identificat per idKenken i nom. L'introdueix a domini.
     * 
     * @param idKenken
     * @param nom
     * @return String
     */
    public String ObtenirKenken(Integer idKenken, String nom) {
        String kenken = ctrlPersistencia.ObtenirKenken(idKenken, nom);
        kenkensUser.put(idKenken, new Kenken(kenken));

        return kenken;
    }

    /**
     * Obte la partida de persistencia, identificada per idkenken i nom. L'introdueix a domini.
     * Previament s'ha comprovat que la partida no existeix a domini i existeix a la BD
     * 
     * @param idkenken
     * @param nom
     * @return String
     */
    public String ObtenirPartida(Integer idkenken, String nom){
        Partida p;
        if(nom.equals("bd")) {
            if(partidesBD.containsKey(idkenken)) return partidesBD.get(idkenken).getKenkenJSON();
            
            String partida = ctrlPersistencia.ObtenirPartida(idkenken, "BD"+usuari.getNom());
            p = new Partida(partida, usuari);
            partidesBD.put(idkenken, p);
            Kenken k = p.getKenken();
            kenkensBD.put(idkenken, k);
            
        }
        else {
            if(partidesUser.containsKey(idkenken)) return partidesUser.get(idkenken).getKenkenJSON();
            String partida = ctrlPersistencia.ObtenirPartida(idkenken, usuari.getNom());
            p = new Partida(partida, usuari);
            partidesUser.put(idkenken, p);
            Kenken k = p.getKenken();
            kenkensUser.put(idkenken, k);
        }
        return p.getKenkenJSON();
    }

    /**
     * Guarda l'informacio de domini a persistencia.
     */
    public void guardaPersistencia() {
        if(usuari != null){
            if (existsUsuari(usuari.getNom()))ctrlPersistencia.EliminarUsuari(null,usuari.getNom());
            ctrlPersistencia.GuardarUsuari(null,usuari.getNom(),usuari.toJson().toString());
            //Bucle per totes les instancies de kenkens usuari, partides i ranquings

            for (Kenken k : kenkensUser.values()) {
                ctrlPersistencia.GuardarKenken(k.getIdKenken(),usuari.getNom(),k.generateJSON().toString());
            }
            for (Partida p : partidesBD.values()) {
               if (existsPartida(p.getIdKenken(),"bd")) ctrlPersistencia.EliminarPartida(p.getIdKenken(),"BD"+usuari.getNom());
               ctrlPersistencia.GuardarPartida(p.getIdKenken(),"BD"+usuari.getNom(),p.toJSON().toString());
            }
            for (Partida p : partidesUser.values()) {
                if (existsPartida(p.getIdKenken(),usuari.getNom()))ctrlPersistencia.EliminarPartida(p.getIdKenken(),usuari.getNom());
               ctrlPersistencia.GuardarPartida(p.getIdKenken(),usuari.getNom(),p.toJSON().toString());
            }
            for (Ranquing r : ranquings.values()) {
                ctrlPersistencia.EliminarRanquing(r.getIdKenken(),usuari.getNom());
                ctrlPersistencia.GuardarRanquing(r.getIdKenken(),usuari.getNom(),r.toJson().toString());
            }
        }
    }

    /**
     * Esborra un element de la persistencia.
     * 
     * @param tipus
     * @param id
     */
    public void esborraElemPersistencia(Integer tipus, Integer id, String nom) {
        if(usuari.getNom() == null) throw new IllegalArgumentException("No hi ha cap usuari loguejat.");
        else {
            String username = usuari.getNom();
            switch (tipus) {
                case 0: //usuari
                    ctrlPersistencia.EliminarUsuari(id,username);
                    break;
                case 1: //kenken
                    ctrlPersistencia.EliminarKenken(id,username);
                    break;
                case 2: //partida
                    ctrlPersistencia.EliminarPartida(id,username);
                    break;
                case 3: //ranquing
                    ctrlPersistencia.EliminarRanquing(id,username);
                    break;
                default:
                    break;
            }
        }
    }
    
    public void eliminarKenkenUsuariDomini(Integer idKenken){
        if(usuari != null && kenkensUser.containsKey(idKenken)) kenkensUser.remove(idKenken);
    }

    /**
     * Retorna true si l'usuari amb nom existeix a la persistencia, false altrament.
     * 
     * @param nom
     * @return Boolean
     */
    public Boolean existsUsuari(String nom) {
        return ctrlPersistencia.existsUsuari(nom);
    }
 
    /**
     * Retorna true si el ranquing del kenkan amb idKenken existeix a la persistencia, false altrament.
     * 
     * @param idKenken
     * @return Boolean
     */
    public Boolean existsRanquing(Integer idkenken) {
        try {
            return ctrlPersistencia.existsRanquing(idkenken);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Retorna true si la partida amb idKenken i nom existeix a persistencia i a domini, false altrament.
     * 
     * @param idKenken
     * @param nom
     * @return Boolean
     */
    public Boolean existsPartida(Integer idKenken,String nom) {
        if(nom == "bd") {
            try {
                if(partidesBD.containsKey(idKenken)) return true;
                else return ctrlPersistencia.existsPartida(idKenken,"BD"+usuari.getNom());
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                return false;
            }
        } else {
            try {
                if(partidesUser.containsKey(idKenken)) return true;
                else return ctrlPersistencia.existsPartida(idKenken,usuari.getNom());
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
    }

    /**
     * Esborra la partida amb identificador idKenken i nom, de persistencia i domini.
     * 
     * @param idKenken
     * @param nom
     */
    public void eliminarPartida(Integer idKenken, String nom){
        if(nom == "bd" && partidesBD.containsKey(idKenken)) {
            partidesBD.remove(idKenken);
        } else if(nom != "bd" && partidesUser.containsKey(idKenken)){
            partidesUser.remove(idKenken);
        }
        try {
            if(ctrlPersistencia.existsPartida(idKenken, nom)){
                String nom2 = nom;
                if(nom == "bd") nom2 = "BD"+usuari.getNom();

                ctrlPersistencia.EliminarPartida(idKenken, nom2);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Retorna true si la contrasenya es correcta, false altrament.
     * 
     * @param contrasenya
     * @return Boolean
     * @throws Exception
     */
    public Boolean verificarContra(String contrasenya) throws Exception {
        if(!usuari.comprobarContrasenya(contrasenya)) throw new Exception("Contrasenya incorrecta.");
        return usuari.comprobarContrasenya(contrasenya);
    }
    
    public String ObtenirRanquing(Integer idKenken,String nom) {
      
        if(ranquings.get(idKenken) != null){
            JsonObject r = ranquings.get(idKenken).toJson();
            String aux = r.toString();
              try{
                JsonObject jsonObject = JsonParser.parseString(aux).getAsJsonObject();
                Integer idk = jsonObject.get("idkenken").getAsInt();
                // Obtener el array de rankings
                JsonArray rankingsArray = jsonObject.getAsJsonArray("rankings");
                // Crear un nuevo JsonArray para almacenar las primeras 10 entradas
                JsonArray firstTenEntries = new JsonArray();
                // Iterar sobre las primeras 10 entradas y agregarlas al nuevo JsonArray
                for (Integer i = 0; i < 10 && i < rankingsArray.size(); i++) {
                    firstTenEntries.add(rankingsArray.get(i));
                }
                // Crear un nuevo JsonObject para el resultado
                JsonObject resultObject = new JsonObject();
                resultObject.addProperty("idkenken", idk);
                resultObject.add("rankings", firstTenEntries);
                return resultObject.toString();
            }
            catch(NullPointerException e){
                System.out.println("Error al escriure el fitxer.");
                //e.printStackTrace();
            }
            
        }
        else{
            
            return ctrlPersistencia.ObtenirRanquing(idKenken, nom);
        }
        return null;
        //String aux = "{\"idkenken\":4,\"rankings\":[{\"tiempo\":\"00:04:50\",\"id\":\"10\",\"username\":\"user10\"},{\"tiempo\":\"00:05:10\",\"id\":\"11\",\"username\":\"user11\"},{\"tiempo\":\"00:06:25\",\"id\":\"12\",\"username\":\"user12\"}]}";
        //return aux;
    }
    
    /**
     * Retorna un string amb tots els kenkens emmagatzemats a persistencia.
     * 
     * @param nom
     * @return String
     */
    public String llegirall(String nom) {String aux = new String("[");
        int it = 0;
        if(nom.equals("bd")){
            for (HashMap.Entry<Integer, Kenken> entry : kenkensBD.entrySet()) {
                Kenken value = entry.getValue();
                if(it!= 0) aux+= ",";
                aux += value.generateJSON().toString();
                ++it;
            }
        }else{
            for (HashMap.Entry<Integer, Kenken> entry : kenkensUser.entrySet()) {
                Kenken value = entry.getValue();
                if(it!= 0) aux+= ",";
                aux += value.generateJSON().toString();
                ++it;
                
            }
        }
        aux += "]";
        return aux;
    }

    /**
     * Retorna el contador dels kenkens de l'usuari.
     * 
     * @return Integer
     */
    public Integer getContadorKenken(){
        return usuari.getContadorKenken();
    }

    /**
     * Incrementa el contador dels kenkens de l'usuari.
     * 
     */
    public void incrementarContador(){
        usuari.incrementarContador();
    }

    /**
     * Obte informacio clau dels ranquings.
     * 
     * @param id
     * @param nom
     * @return String[]
     */
    public String[] getInfoRanking(Integer id,String nom){
         if(ranquings.get(id) != null){
             
             try {
                String aux =  ranquings.get(id).toJson().toString();
                JsonObject jsonObject = JsonParser.parseString(aux).getAsJsonObject();

                JsonArray rankingsArray = jsonObject.getAsJsonArray("rankings");
                String[] result = null;
                for (int i = 0; i < rankingsArray.size(); i++) {
                    JsonObject rankingObject = rankingsArray.get(i).getAsJsonObject();
                    String username = rankingObject.get("username").getAsString();
                    String tiempo = rankingObject.get("tiempo").getAsString();

                    if (username.equals(nom)) {
                        result = new String[2];
                        result[0] = Integer.toString(i + 1);
                        result[1] = tiempo;
                        break; 
                    }
                }
                return result;
            }catch(NumberFormatException e){
                System.out.println("no esta el jugador al ranquing");
                return null;
            }
        
             
             
             
         }else{
            return ctrlPersistencia.getInfoRanking(id, nom);
         }

    }
   
    
    public Boolean comprovarFila(Integer idpartida, String nom, Integer x, Integer valor) {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idpartida);
        else p = partidesUser.get(idpartida);
        return p.comprovarFila(x, valor);
    }
    
    public Boolean comprovarCol(Integer idpartida, String nom, Integer x, Integer valor) {
        Partida p;
        if(nom.equals("bd")) p = partidesBD.get(idpartida);
        else p = partidesUser.get(idpartida);
        return p.comprovarCol(x, valor);
    }
}