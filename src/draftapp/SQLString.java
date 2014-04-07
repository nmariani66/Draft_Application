/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nmariani
 */
public class SQLString {
    //queryType - 0 = query, 1 = update
    public SQLString(int queryType, String queryCol, String queryMatch, String updateCol, String updateVal) {
        if (queryType == 1) {
            String str = "UPDATE ";
            str = str + updateCol + " = " + updateVal;
            str = str + " WHERE " + queryCol + " = " + queryMatch;
            System.out.println(str);
            
            String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\Users\\nmariani\\Documents\\NetBeansProjects\\DraftApp\\Draft.accdb";
            try {
                Connection con = DriverManager.getConnection(database,"","");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(str);
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
            }   
            DraftPanel.getPlayers();
        }
    }
    
}
