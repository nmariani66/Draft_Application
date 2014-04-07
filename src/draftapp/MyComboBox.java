/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

/**
 This class listens for when a new team is selected from the drop down menu and 
 carries out the actions to rebuild the panel with the new team's player info.
 * @author nmariani
 */

import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;  
import javax.swing.event.*;  

public class MyComboBox implements ActionListener {
    DraftPanel dp;
    Boolean counter;
    
    public MyComboBox(DraftPanel panel) {
        dp=panel;
    }
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String selTeam = (String)cb.getSelectedItem();
        System.out.println(selTeam);
        if (counter!=null) {
            dp.getTeamPlayers(selTeam);
        } else {
            counter = true;
        }
    }
}
