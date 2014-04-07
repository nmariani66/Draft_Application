/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

import javax.swing.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nmariani
 */
public class PlayerTable extends JTable {
    public PlayerTable() {
        super();
        String[] columnNames = {"Rank"};
        String[] data = null;
        int i=0;
        try (FileReader file = new FileReader("Player.txt");
                BufferedReader buff = new BufferedReader(file)) {
            String line;
            while ((line = buff.readLine()) != null) {
                data[i]=line;
                ++i;
            }
            
            buff.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayerTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            Logger.getLogger(PlayerTable.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        
       
        
        
    }
    
}
