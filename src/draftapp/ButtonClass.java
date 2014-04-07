/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

/**
 This class is used to listen for when the draft button is pushed and carry out 
 the required actions. 
 * @author nmariani
 */

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class ButtonClass implements ActionListener {
    public static String position;
    JButton draft;
    DraftPanel dp;
    public static String player;
    public static int currPick;
    public static String currTeam;
    
    public ButtonClass(DraftPanel draftPanel) {
        dp = draftPanel;
        draft=dp.draft;
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == draft)
        {
            System.out.println("Draft " + player + "!");
            
            //Update Player to unavailable
            String tableName = "Offense_Proj";
            int queryType = 0;
            String[] playerVal = new String[] {"n"};
            String[] playerCol = new String[] {"Available"};
            int[] playerType = new int[] {1};
            String[] playerConVal = new String[] {player};
            String[] playerConCol = new String[] {"Player"};
            int[] playerConType = new int[] {1};
            String playerSQL = dp.sqlString(queryType, tableName, playerVal, playerCol, playerType, playerConVal, playerConCol, playerConType, null);
            
            //Update DraftOrder Table
            tableName="DraftOrder";
            String[] draftVal = new String[] {player};
            String[] draftCol = new String[] {"Player"};
            int[] draftType = new int[] {1};
            String[] draftConVal = new String[] {""+currPick};
            String[] draftConCol = new String[] {"Pick"};
            int[] draftConType = new int[] {0};
            String draftSQL = dp.sqlString(queryType, tableName, draftVal, draftCol, draftType, draftConVal, draftConCol, draftConType, null);
            
            //Find which position to assign player to
            tableName=""+currTeam;
            queryType = 2;
            String[] posCol = new String[] {"PosID"};
            String[] posConVal;
            int[] posConType;
            String[] posConCol;
            if (position.equals("RB") || position.equals("WR") || position.equals("TE")) {
                posConVal = new String[] {"", position, "Flex", "Bench"};
                posConType = new int[] {0, 1, 1,1};
                posConCol = new String[] {"Player IS NULL", " AND Position IN (", ", ", ", "};
            } else {
                posConVal = new String[] {"", position, "Bench"};
                posConType = new int[] {0, 1,1};
                posConCol = new String[] {"Player IS NULL", " AND Position IN (", ", "};
            }          
            String posSQL = dp.sqlString(queryType, tableName, null, posCol, null, posConVal, posConCol, posConType, "PosID");
            try {
                Connection con = DriverManager.getConnection(dp.db,"","");
                Statement stmt = con.createStatement();
                stmt.executeUpdate(playerSQL);
                stmt.executeUpdate(draftSQL);
                String teamPos = "";
                ResultSet rs = stmt.executeQuery(posSQL);
                while (rs.next()) {
                    teamPos = rs.getString(1);
                    break;
                }
                queryType = 0;
                String[] teamVal = new String[] {player};
                String[] teamCol = new String[] {"Player"};
                int[] teamType = new int[] {1};
                String[] teamConVal = new String[] {teamPos};
                String[] teamConCol = new String[] {"PosID"};
                int[] teamConType = new int[] {1};
                String teamSQL = dp.sqlString(queryType, tableName, teamVal, teamCol, teamType, teamConVal, teamConCol, teamConType, null);
                stmt.executeUpdate(teamSQL);
                
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
            }   
            
            dp.tm.removeRow((int)dp.playerRow.get(player));
            dp.getPlayerRows();
            if (currTeam.equals((String)dp.teamMenu.getSelectedItem())) {
                dp.getTeamPlayers(currTeam);
            }
            ++currPick;
            dp.incrementPick(currPick);
        }
    }

    
}
