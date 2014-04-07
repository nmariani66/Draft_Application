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
public class PlayerArray {
    //public String[] playerEntry;
    DraftPanel p;
    
    
    public void setEntry (int x, String entry, DraftPanel y) {
    
        y.playerEntry[x-1] = entry;
            
}
    
}
