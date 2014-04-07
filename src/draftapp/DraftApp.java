/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

import javax.swing.UIManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 This is the main execution class that builds the GUI for the draft application.
 * @author nmariani
 */


public class DraftApp {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        
        JFrame appWin = new JFrame("Guido's Draft Experience");
        appWin.setSize(1280, 770);
        appWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DraftPanel dp = new DraftPanel();
        dp.setBackground(Color.BLACK);
        appWin.add(dp);
        try {
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception exc) {
            //ignore error
        }
        appWin.setVisible(true);
    }
    
}
