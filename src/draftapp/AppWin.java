/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

/**
 *
 * @author nmariani
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AppWin extends JFrame {
    public AppWin() {
        super("Guido's Draft Experience");
        setSize(1280, 770);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setLookAndFeel();
        DraftPanel dp = new DraftPanel();
        dp.setBackground(Color.WHITE);
        add(dp);
        setVisible(true);
        
    }
    
    private static void setLookAndFeel() {
        try {
             UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception exc) {
            //ignore error
        }
    }
    
}


   