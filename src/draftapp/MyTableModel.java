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
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Arrays.*;
import java.util.*;

public class MyTableModel extends AbstractTableModel{
    Vector<Vector<String>> data;
    Vector<String> colName;
    
    public MyTableModel(Vector<Vector<String>> data, Vector<String> colName) {
        super();
        this.data=data;
        this.colName = colName;
    }

    
    //public int getColumnCount() {
      //  return columnNames.length;
    //}

   // public int getRowCount() {
     //   return data.length;
    //}
    @Override
    public String getColumnName(int col) {
        return colName.get(col);
    }
    @Override
    public Object getValueAt(int row, int col) {
        //System.out.println(((String[]) data.get(row))[col]);
        return ((Vector<String>) data.get(row)).get(col);
    } 
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    @Override
    public int getColumnCount() {
        return colName.size();
    }
    @Override
    public int getRowCount() {
        System.out.println("rows "+data.size());
        return data.size();
    }

    public void removeRow(int row){
        
    }
    
    
}
