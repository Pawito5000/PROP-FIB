/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package classes.presentacio;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Classe Seleccio2View que representa una vista de seleccio en l'aplicacio Kenken.
 * Aquesta classe es responsable de mostrar les opcions de seleccio de les diferents opcionalitats del Kenken.
 *
 * @author marti
 */
public class Seleccio2View extends javax.swing.JPanel {
    private final PrincipalView vistaPrincipal; 
    private String nomJugador;
    private int idKenken;
    /**
     * Aquesta funcio inicialitza els components de la vista.
     */
    public Seleccio2View(PrincipalView vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        initComponents();
        
        ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/images/flecha.png"));
        Image image = imageIcon.getImage(); // Convertir ImageIcon a Image
        Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Escalar la imagen
        ImageIcon scaledImageIcon = new ImageIcon(newImage);
        bEnrere7.setIcon(scaledImageIcon);
    }
    
    public void ini(String nom,Integer id){
        //canviar el idKenken del principalview
        bJugar2.setVisible(true);
        nomJugador = nom;
        idKenken = id;
        if(nom.equals("bd")){//el kenken es de la bd
            bMostrarRanking.setVisible(true);
            bEliminar.setVisible(false);
        }else{
            bMostrarRanking.setVisible(false);
            bEliminar.setVisible(true);
        }
        String info = vistaPrincipal.obtenirKenken(id,nom);
        JsonObject KenkenJSON = new Gson().fromJson(info,JsonObject.class);
        
        int N = KenkenJSON.has("N") ? KenkenJSON.get("N").getAsInt() : 0;
        tPantalla2.setText(N+"x"+N);
        
        Boolean[] op = new Boolean[]{false,false,false,false,false,false,false};

        JsonArray conjuntsCelesArray = KenkenJSON.getAsJsonArray("regions");
        for (Integer i = 0; i < conjuntsCelesArray.size(); ++i) {

            JsonObject ConjuntCellaObj = conjuntsCelesArray.get(i).getAsJsonObject();
            int index; 
            index = ConjuntCellaObj.has("oper") ? ConjuntCellaObj.get("oper").getAsInt() : 0;
            op[index] = true;
            //cortarlo cuando todos sean true

        }
        op[0] = false;
        pOperacions.removeAll();
        for(int i= 0; i <= 6; ++i){
            if(op[i]){
                JLabel la_op = new JLabel();
                la_op.setText(vistaPrincipal.calcularSimbol(i));
                la_op.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                la_op.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                pOperacions.add(la_op);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tPantalla2 = new javax.swing.JLabel();
        bEnrere7 = new javax.swing.JButton();
        bJugar2 = new javax.swing.JButton();
        bRenaudar = new javax.swing.JButton();
        bMostrarRanking = new javax.swing.JButton();
        bEliminar = new javax.swing.JButton();
        pOperacions = new javax.swing.JPanel();

        setBackground(new java.awt.Color(204, 204, 255));
        setMaximumSize(new java.awt.Dimension(700, 700));
        setMinimumSize(new java.awt.Dimension(700, 700));
        setPreferredSize(new java.awt.Dimension(700, 700));

        tPantalla2.setFont(new java.awt.Font("Arial", 1, 70)); // NOI18N
        tPantalla2.setForeground(new java.awt.Color(0, 102, 102));
        tPantalla2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tPantalla2.setText("6x6");
        tPantalla2.setAlignmentX(0.5F);
        tPantalla2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tPantalla2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        bEnrere7.setBorderPainted(false);
        bEnrere7.setContentAreaFilled(false);
        bEnrere7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bEnrere7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEnrere7ActionPerformed(evt);
            }
        });

        bJugar2.setBackground(new java.awt.Color(0, 102, 102));
        bJugar2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bJugar2.setForeground(new java.awt.Color(255, 255, 255));
        bJugar2.setText("Jugar");
        bJugar2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bJugar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bJugar2ActionPerformed(evt);
            }
        });

        bRenaudar.setBackground(new java.awt.Color(0, 102, 102));
        bRenaudar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bRenaudar.setForeground(new java.awt.Color(255, 255, 255));
        bRenaudar.setText("Renaudar");
        bRenaudar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bRenaudar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRenaudarActionPerformed(evt);
            }
        });

        bMostrarRanking.setBackground(new java.awt.Color(0, 102, 102));
        bMostrarRanking.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bMostrarRanking.setForeground(new java.awt.Color(255, 255, 255));
        bMostrarRanking.setText("Mostrar Ranking");
        bMostrarRanking.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bMostrarRanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMostrarRankingActionPerformed(evt);
            }
        });

        bEliminar.setBackground(new java.awt.Color(0, 102, 102));
        bEliminar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bEliminar.setForeground(new java.awt.Color(255, 255, 255));
        bEliminar.setText("Eliminar");
        bEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEliminarActionPerformed(evt);
            }
        });

        pOperacions.setLayout(new java.awt.GridLayout(1, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tPantalla2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(bEnrere7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(444, 444, 444))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(pOperacions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50))
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bMostrarRanking, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addComponent(bRenaudar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bJugar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(100, 100, 100))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(bEnrere7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tPantalla2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pOperacions, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bJugar2, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bRenaudar, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bMostrarRanking, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addGap(64, 64, 64))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bEnrere7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEnrere7ActionPerformed
        // TODO add your handling code here:
        vistaPrincipal.showPanel("SeleccioView");
    }//GEN-LAST:event_bEnrere7ActionPerformed

    private void bJugar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bJugar2ActionPerformed
        // TODO add your handling code here:
        String nom = nomJugador;
        if(vistaPrincipal.getBd()) nom = "bd";
        
        
        if(vistaPrincipal.existeixPartida(idKenken,nom)){
            //existeix
            // Define las opciones de los botones
            Object[] options = {"SI", "NO"};

            // Muestra el JOptionPane
            Integer option = JOptionPane.showOptionDialog(
                null,
                "Estas segur que vols comencar la partida de nou, es perdra el progres anterior:",
                "Confirmacio",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            

            // Verifica que opcion fue seleccionada
            if (option == JOptionPane.YES_OPTION) {
                vistaPrincipal.eliminarPartida(idKenken, nom);
                vistaPrincipal.crearPartida(idKenken, nom);
                String kenkenJSON = vistaPrincipal.obtenirKenkenPartida(idKenken,nom);
                vistaPrincipal.crearKenken2(kenkenJSON,"JugantView","jugable");
                vistaPrincipal.showPanel("JugantView");
            } else if (option == JOptionPane.NO_OPTION) {

            } else {
                
            }
        }else{
            //no existeix
            vistaPrincipal.crearPartida(idKenken, nom);
            String kenkenJSON = vistaPrincipal.obtenirKenkenPartida(idKenken,nom);
          
            vistaPrincipal.crearKenken2(kenkenJSON,"JugantView","jugable");
            vistaPrincipal.showPanel("JugantView");
            //JOptionPane.showMessageDialog(null, "No tienes ninguna parida iniciada con este kenken", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bJugar2ActionPerformed

    private void bRenaudarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRenaudarActionPerformed
        String nom = nomJugador;
        
        if(vistaPrincipal.existeixPartida(idKenken,nom)){
            String kenkenJSON = vistaPrincipal.obtenirKenkenPartida(idKenken,nom);
            vistaPrincipal.crearKenken2(kenkenJSON,"JugantView","jugable");
            vistaPrincipal.showPanel("JugantView");
            
        }else{
           //no existeix
            JOptionPane.showMessageDialog(null, "No tienes ninguna parida iniciada con este kenken", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bRenaudarActionPerformed

    private void bMostrarRankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMostrarRankingActionPerformed
        // TODO add your handling code here:
        if(vistaPrincipal.existeixRanking()){
           
            vistaPrincipal.showPanel("RankingView");
        }else{
            JOptionPane.showMessageDialog(null, "Ningu ha jugat aquest kenken encara, sigues el primer en fer-ho!", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_bMostrarRankingActionPerformed

    private void bEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEliminarActionPerformed
        // TODO add your handling code here:
        // Define las opciones de los botones
        String nom = nomJugador;
        
        Object[] options = {"SI", "NO"};

        // Muestra el JOptionPane
        Integer option = JOptionPane.showOptionDialog(
            null,
            "Estas seguro que quieres eliminar este kenken:",
            "Confirmacion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        // Verifica que opcion fue seleccionada
        if (option == JOptionPane.YES_OPTION) {
            vistaPrincipal.eliminarKenken(idKenken,nom);
            vistaPrincipal.setIdKenken(-1);
            vistaPrincipal.showPanel("SeleccioView");
        } else if (option == JOptionPane.NO_OPTION) {

        } else {

        }
    }//GEN-LAST:event_bEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEliminar;
    private javax.swing.JButton bEnrere7;
    private javax.swing.JButton bJugar2;
    private javax.swing.JButton bMostrarRanking;
    private javax.swing.JButton bRenaudar;
    private javax.swing.JPanel pOperacions;
    private javax.swing.JLabel tPantalla2;
    // End of variables declaration//GEN-END:variables
}
