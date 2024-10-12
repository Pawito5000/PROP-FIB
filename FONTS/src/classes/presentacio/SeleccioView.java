/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package classes.presentacio;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.awt.Image;
import javax.swing.ImageIcon;
import com.google.gson.*;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.*;
import java.awt.*;


/**
 * Classe SeleccioView que representa una vista de seleccio entre diferents Kenkens.
 * Aquesta classe es responsable de mostrar les opcions de generar i diferents Kenkens jugables.
 * 
 * @author marti
 */
public class SeleccioView extends javax.swing.JPanel {
    private final PrincipalView vistaPrincipal; 
    private static final int MAX_COMPONENTS_PER_ROW = 8;
    /**
     * Creates new form Seleccio
     */
    public SeleccioView(PrincipalView vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        initComponents();
        
        ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/images/flecha.png"));
        Image image = imageIcon.getImage(); // Convertir ImageIcon a Image
        Image newImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Escalar la imagen
        ImageIcon scaledImageIcon = new ImageIcon(newImage);
        bEnrere4.setIcon(scaledImageIcon);
    }
   
    
    public void crearpanels(JPanel cjt,String info,Boolean bd){
        cjt.removeAll();
        
        
        JsonArray jsonArray = JsonParser.parseString(info).getAsJsonArray();

        cjt.setLayout(new FlowLayout(FlowLayout.LEFT));

        
        for (JsonElement element : jsonArray) {
            
            Boolean[] op = new Boolean[]{false,false,false,false,false,false,false};
            JsonObject kenkenObject = element.getAsJsonObject();
            javax.swing.JPanel kenken = new javax.swing.JPanel();
            javax.swing.JLabel mida = new javax.swing.JLabel();
            javax.swing.JPanel operacions = new javax.swing.JPanel();


            kenken.setMaximumSize(new java.awt.Dimension(50,50));
            kenken.setMinimumSize(new java.awt.Dimension(1, 1));
            kenken.setPreferredSize(new java.awt.Dimension(50, 50));
            kenken.setLayout(new java.awt.GridLayout(2, 1));
            kenken.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

            mida.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            mida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            
            int N = kenkenObject.has("N") ? kenkenObject.get("N").getAsInt() : 0;

            
            JsonArray conjuntsCelesArray = kenkenObject.getAsJsonArray("regions");
            for (Integer i = 0; i < conjuntsCelesArray.size(); ++i) {

                JsonObject ConjuntCellaObj = conjuntsCelesArray.get(i).getAsJsonObject();
                int index; 
                index = ConjuntCellaObj.has("oper") ? ConjuntCellaObj.get("oper").getAsInt() : 0;
                op[index] = true;
                //cortarlo cuando todos sean true
                
                
            }
            op[0] = false;
            mida.setText(N+"x"+N);
            mida.setAlignmentX(0.5F);
            mida.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            kenken.add(mida);
            
            kenken.putClientProperty("id", kenkenObject.has("id") ? kenkenObject.get("id").getAsInt() : -1);
            kenken.putClientProperty("bd", bd);
            
            operacions.setLayout(new java.awt.GridLayout());
            for(int i= 0; i <= 6; ++i){
                if(op[i]){
                    JLabel la_op = new JLabel();
                    la_op.setText(vistaPrincipal.calcularSimbol(i));
                    la_op.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                    la_op.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                    operacions.add(la_op);
                }
            }
            kenken.add(operacions);
            kenken.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            kenken.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int id = (Integer) kenken.getClientProperty("id");
                    Boolean bd = (Boolean) kenken.getClientProperty("bd");
                    vistaPrincipal.setIdKenken(id);
                    vistaPrincipal.setBd(bd);
                    vistaPrincipal.showPanel("Seleccio2View");
                  
                }
            });
            
            cjt.add(kenken);
            

            
        }
        

        
    }
    
    
    void ini(String nom){
        
        String cjtBdJSON = vistaPrincipal.getAllKenkens("bd");
        String cjtUserJSON = vistaPrincipal.getAllKenkens(nom);
        if(!cjtBdJSON.equals("")) crearpanels(cjtBd,cjtBdJSON,true);

        if(!cjtUserJSON.equals(""))crearpanels(cjtUser,cjtUserJSON,false);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGenerarKenken = new javax.swing.JButton();
        bEnrere4 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        tNom4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cjtBd = new javax.swing.JPanel();
        jSeparator7 = new javax.swing.JSeparator();
        tNom5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cjtUser = new javax.swing.JPanel();

        setBackground(new java.awt.Color(204, 204, 255));
        setMaximumSize(new java.awt.Dimension(700, 700));
        setMinimumSize(new java.awt.Dimension(700, 700));
        setPreferredSize(new java.awt.Dimension(700, 700));

        bGenerarKenken.setBackground(new java.awt.Color(0, 102, 102));
        bGenerarKenken.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bGenerarKenken.setForeground(new java.awt.Color(255, 255, 255));
        bGenerarKenken.setText("Generar Kenken");
        bGenerarKenken.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bGenerarKenken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGenerarKenkenActionPerformed(evt);
            }
        });

        bEnrere4.setBorderPainted(false);
        bEnrere4.setContentAreaFilled(false);
        bEnrere4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bEnrere4.setMaximumSize(new java.awt.Dimension(50, 50));
        bEnrere4.setMinimumSize(new java.awt.Dimension(50, 50));
        bEnrere4.setPreferredSize(new java.awt.Dimension(50, 50));
        bEnrere4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEnrere4ActionPerformed(evt);
            }
        });

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        tNom4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        tNom4.setText("Kenkens de la BD:");

        cjtBd.setBackground(new java.awt.Color(204, 204, 255));
        cjtBd.setLayout(new java.awt.GridLayout(0, 1, 0, 1));
        jScrollPane1.setViewportView(cjtBd);

        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));

        tNom5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        tNom5.setText("Kenkens propis:");

        cjtUser.setBackground(new java.awt.Color(204, 204, 255));
        cjtUser.setLayout(new java.awt.GridLayout(0, 1, 0, 1));
        jScrollPane2.setViewportView(cjtUser);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(152, 152, 152)
                .addComponent(bGenerarKenken, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(152, 152, 152))
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(bEnrere4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tNom4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(287, 287, 287))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(43, 43, 43))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(70, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tNom5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(287, 287, 287))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(43, 43, 43))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(bEnrere4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(tNom5, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addGap(36, 36, 36)
                .addComponent(tNom4, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(bGenerarKenken, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addGap(49, 49, 49))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bGenerarKenkenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGenerarKenkenActionPerformed

        vistaPrincipal.showPanel("GenerarView");
    }//GEN-LAST:event_bGenerarKenkenActionPerformed

    private void bEnrere4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEnrere4ActionPerformed
        vistaPrincipal.showPanel("JocView");
    }//GEN-LAST:event_bEnrere4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bEnrere4;
    private javax.swing.JButton bGenerarKenken;
    private javax.swing.JPanel cjtBd;
    private javax.swing.JPanel cjtUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel tNom4;
    private javax.swing.JLabel tNom5;
    // End of variables declaration//GEN-END:variables
}
