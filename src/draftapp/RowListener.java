/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;  
import javax.swing.event.*;  
import javax.swing.table.*;

/**
 This class is used to identify the player row selected in the table, set
 public variables, and display info on the JLabel
 * @author nmariani
 */
  
class RowListener implements ListSelectionListener  
{  
    DraftPanel readRow;  
    JTable jt;  
    
    public String[] playerHeader;
   
    public RowListener(DraftPanel rar)  
    {  
        readRow = rar;  
        jt = readRow.jt; 
    }  
   
    @Override
    public void valueChanged(ListSelectionEvent e)  
    {  
        if(!e.getValueIsAdjusting())  
        {  
            ListSelectionModel model = jt.getSelectionModel();  
            int lead = model.getLeadSelectionIndex();  
            displayRowValues(lead);  
        }  
    }  
   
    private void displayRowValues(int rowIndex)  
    {  
        int columns = jt.getColumnCount();  
        String s = "";  
        String msg = "";
        playerHeader = new String[columns];
        Object d = jt.getValueAt(rowIndex,0);
        playerHeader[0] = d.toString();
        ButtonClass.player = playerHeader[0];
        d = jt.getValueAt(rowIndex,1);
        playerHeader[1] = d.toString();
        System.out.println("Position: " + playerHeader[1]);
        ButtonClass.position = playerHeader[1];
        String[] colHeader = new String[] {"Comp: ", "Inc: ", "PassYds: ", "PassTD: ", "Int: ", "RushYds: ", "RushTD: ", "Rec: ", "RecYds: ", "RecTD: ", "RetTD: ", "Fum: "};
        for(int col = 0; col < columns; col++)  
        {  
            Object o = jt.getValueAt(rowIndex, col);  
            s = o.toString();  
            if (col==0) {
                msg="<html> " + s;
            }else if(col==1) {
                msg += ", " + s;
            }  else if(col==2) {
                msg += ", " + s + "<BR>";
            } else if(col==5) {
                msg += colHeader[col-5] + s;
            } else if(col>5) {
                msg += "  " + colHeader[col-5] + s;
            }
                  
        }  
        System.out.println(msg); 
        readRow.playerLabel.setText(msg);
     }  
}  


