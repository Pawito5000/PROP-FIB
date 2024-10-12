/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package classes.presentacio;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.border.Border;

/**
 * Panell per a la vista de joc.
 * Aquest panell proporciona una interficie d'usuari per al joc en l'aplicacio.
 * es utilitzat per mostrar el taulell de joc i interactuar amb les operacions i els valors.
 
 * @author marti
 */
public class JocView extends javax.swing.JPanel {
    private final PrincipalView vistaPrincipal; 
    Integer N,R;
    public JLabel[][] operaciones;
    public JButton[][] valores;
    public JButton seleccionado;
    /**
     * Assigna una nova instancia de la vista del joc i inicialitza els components..
     *
     * @param vistaPrincipal La vista principal de l'aplicacio.
     */
    public JocView(PrincipalView vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tPantalla4 = new javax.swing.JLabel();
        bJugar = new javax.swing.JButton();
        bCarregarKenken = new javax.swing.JButton();

        setBackground(new java.awt.Color(204, 204, 255));
        setMaximumSize(new java.awt.Dimension(700, 700));
        setMinimumSize(new java.awt.Dimension(700, 700));
        setPreferredSize(new java.awt.Dimension(700, 700));

        tPantalla4.setFont(new java.awt.Font("Segoe UI Black", 1, 90)); // NOI18N
        tPantalla4.setForeground(new java.awt.Color(0, 102, 102));
        tPantalla4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tPantalla4.setText("KenKen");
        tPantalla4.setAlignmentX(0.5F);
        tPantalla4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tPantalla4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        bJugar.setBackground(new java.awt.Color(0, 102, 102));
        bJugar.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bJugar.setForeground(new java.awt.Color(255, 255, 255));
        bJugar.setText("Jugar");
        bJugar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bJugarActionPerformed(evt);
            }
        });

        bCarregarKenken.setBackground(new java.awt.Color(0, 102, 102));
        bCarregarKenken.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 24)); // NOI18N
        bCarregarKenken.setForeground(new java.awt.Color(255, 255, 255));
        bCarregarKenken.setText("Carregar Kenken");
        bCarregarKenken.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bCarregarKenken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCarregarKenkenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tPantalla4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bCarregarKenken, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addComponent(bJugar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(100, 100, 100))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(tPantalla4, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bJugar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(bCarregarKenken, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bJugarActionPerformed
        vistaPrincipal.showPanel("SeleccioView");
    }//GEN-LAST:event_bJugarActionPerformed

    private void bCarregarKenkenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCarregarKenkenActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        fc.setFileFilter(filter);
        
        Integer response =  fc.showOpenDialog(null);
        if(response == JFileChooser.APPROVE_OPTION){
            
            File selectedFile = fc.getSelectedFile();
            
            ArrayList<String> info = readFile(selectedFile);
            //definir n i r
            String[] parts = info.get(0).split(" "); // Divide la linea en partes separadas por espacio
            
            //cridar a transformar JSON
            
            try{
                String json = vistaPrincipal.generarJson(info);
                String result;
                try {
                    result = vistaPrincipal.validarKenken(json);
                    N = Integer.parseInt(parts[0]); // Devuelve la primera parte como un entero
                    R = Integer.parseInt(parts[1]);
                } catch (Exception e) {
                    // Handle the exception here
                    JOptionPane.showMessageDialog(null, "El Kenken proporcionat NO es valid: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
                    result = null; // or any default value you want to return in case of an error
                }
                if(result != null) {
                    vistaPrincipal.crearKenken2(result,"CarregarKenkenView","mostrar");
                    vistaPrincipal.setIdKenken(result);
                    vistaPrincipal.showPanel("CarregarKenkenView");

                }
            } catch (Exception e2){
                JOptionPane.showMessageDialog(null, "El Kenken proporcionat NO es valid: " + e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);                
            }
            
            //cridar a validar de controlador domini
            
            
        }
    }//GEN-LAST:event_bCarregarKenkenActionPerformed
    
    

        
    private ArrayList<String> readFile(File file) {
        ArrayList<String> lines = new ArrayList<>() ;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Integer i= 0;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                ++i;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lines;
    }
    
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCarregarKenken;
    private javax.swing.JButton bJugar;
    private javax.swing.JLabel tPantalla4;
    // End of variables declaration//GEN-END:variables
}
