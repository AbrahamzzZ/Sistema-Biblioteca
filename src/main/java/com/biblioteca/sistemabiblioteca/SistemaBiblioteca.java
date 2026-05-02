/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.biblioteca.sistemabiblioteca;

import ui.LoginFrame;

/**
 *
 * @author herma
 */
public class SistemaBiblioteca {

    public static void main(String[] args) {
        try{
            for(javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()){
                if("Nimbus".equals(info.getName())){
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;  
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        } 
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
