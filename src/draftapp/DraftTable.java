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

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.event.ListSelectionListener;

public class DraftTable extends JPanel {
    JTable jt;
    public DraftTable(String[][] data, String[] columns) {
        jt = new JTable(data, columns) 
        {  
            @Override
            public boolean isCellEditable(int row, int col)  
            {  
                return false;  
            }  
        };
        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jt.getColumnModel().getColumn(0).setPreferredWidth(150);
        jt.getColumnModel().getColumn(1).setPreferredWidth(35);
        jt.getColumnModel().getColumn(2).setPreferredWidth(50);
        jt.getColumnModel().getColumn(3).setPreferredWidth(35);
        jt.getColumnModel().getColumn(4).setPreferredWidth(50);
        jt.getColumnModel().getColumn(5).setPreferredWidth(50);
        jt.getColumnModel().getColumn(6).setPreferredWidth(50);
        jt.getColumnModel().getColumn(7).setPreferredWidth(50);
        jt.getColumnModel().getColumn(8).setPreferredWidth(50);
        jt.getColumnModel().getColumn(9).setPreferredWidth(50);
        jt.getColumnModel().getColumn(10).setPreferredWidth(50);
        jt.getColumnModel().getColumn(11).setPreferredWidth(50);
        jt.getColumnModel().getColumn(12).setPreferredWidth(50);
        jt.getColumnModel().getColumn(13).setPreferredWidth(50);
        jt.getColumnModel().getColumn(14).setPreferredWidth(50);
        jt.getColumnModel().getColumn(15).setPreferredWidth(50);
        jt.getColumnModel().getColumn(16).setPreferredWidth(50);
        
        //get player when row is selected  
        ListSelectionModel selectionModel = jt.getSelectionModel();  
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
        selectionModel.addListSelectionListener(new RowListener(this));
        
        JScrollPane sp = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(sp);
        this.setBackground(Color.GREEN);
        
    }
        
    
}
