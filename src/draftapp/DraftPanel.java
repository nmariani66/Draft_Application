/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package draftapp;

import static draftapp.ButtonClass.currPick;
import static draftapp.ButtonClass.player;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;


/**
 *
 * @author nmariani
 */
public class DraftPanel extends JPanel {
    GridBagLayout gridbag = new GridBagLayout();
    Vector<String> playerData;
    Vector<String> columns;
    Vector<Vector<String>> playerEntry;
    JTable jt;
    JPanel playerInfo, draftBut, playerList, teamList, currentTeam, draftOrder, currOrder, teamPos, teamPlay;
    JLabel playerLabel, picLabel;
    JButton draft;
    HashMap playerRow, teamInfo;
    DefaultTableModel tm;
    String db;
    JComboBox teamMenu;
    int currPick, tlWidth;    
    
    public  DraftPanel() {
        super();
        this.setPreferredSize(new Dimension(1280, 770));
        GridBagConstraints constraints;
        setLayout(gridbag);
        db = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=C:\\Users\\nmariani\\Documents\\NetBeansProjects\\DraftApp\\Draft.accdb";
        currPick=1;
        
        //Draft Order Panel
        draftOrder = new JPanel();
        draftOrder.setBackground(Color.BLACK);
        incrementPick(currPick);
        Dimension doMax = new Dimension(1280, 154);
        draftOrder.setPreferredSize(doMax);
        draftOrder.setMaximumSize(doMax);
        addComponent(draftOrder, 0 ,0, 3, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER, gridbag);
        
        //Player Info Panel
        playerInfo = new JPanel();
        playerInfo.setBackground(Color.BLACK);
        playerInfo.setLayout(new BoxLayout(playerInfo, BoxLayout.X_AXIS));
        Dimension piMax = new Dimension(975, 75);
        playerInfo.setPreferredSize(piMax);
        playerInfo.setMaximumSize(piMax);
        playerInfo.setMinimumSize(new Dimension(900, 75));
        BufferedImage blankPlayer = null;
        try {
           blankPlayer = ImageIO.read(new File("BlankHead.jpg"));
        } catch (IOException e) {
           Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, e);
        }
        Image sblankPlayer = blankPlayer.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        JLabel playerPic = new JLabel(new ImageIcon(sblankPlayer));
        Dimension bpMax = new Dimension(75,75);
        playerPic.setPreferredSize(bpMax);
        playerPic.setMaximumSize(bpMax);
        playerInfo.add(playerPic);
        playerLabel = new JLabel("No Player Selected");
        Dimension playLabelMax = new Dimension(900, 75);
        playerLabel.setPreferredSize(playLabelMax);
        playerLabel.setMaximumSize(playLabelMax);
        playerLabel.setFont(new Font("Sherif", Font.PLAIN, 12));
        playerLabel.setForeground(Color.WHITE);
        playerInfo.add(playerLabel);
        addComponent(playerInfo, 0 ,1, 2, 1, 1, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER, gridbag);
        
        //Draft Button Panel
        draftBut = new JPanel();
        Dimension dbMax = new Dimension(281,154);
        draftBut.setPreferredSize(dbMax);
        draftBut.setMaximumSize(dbMax);
        draftBut.setBackground(Color.BLACK);
        draft = new JButton("Draft");
        draft.setBackground(Color.BLUE);
        draft.setForeground(Color.YELLOW);
        draft.setPreferredSize(new Dimension(100, 50));
        ButtonClass buttonClick = new ButtonClass(this);
        draft.addActionListener(buttonClick);
        draftBut.add(draft);
        addComponent(draftBut, 2, 1, 1, 1, 0, 0, GridBagConstraints.BOTH, GridBagConstraints.CENTER, gridbag);
        
        //Player List Tabel Panel
        columns = new Vector(17);  
        playerRow = new HashMap();
        //Build Player table, seperate function for organizational purposes
        getPlayers();
        
        //Draft Queue Panel, STILL NEED TO ADD FUNCTIONALITY
        JPanel draftQueue = new JPanel();
        Dimension dqMax = new Dimension(225,462);
        draftQueue.setPreferredSize(dqMax);
        draftQueue.setMaximumSize(dqMax);
        draftQueue.setBackground(Color.ORANGE);
        String label = "          Auto Draft Queue          ";
        JLabel queue = new JLabel(label);
        queue.setMinimumSize(new Dimension(225,462));
        draftQueue.add(queue);
        addComponent(draftQueue, 1, 2, 1, 1, 0, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, gridbag);
        
        //Team List Panel      
        teamList = new JPanel();
        teamList.setLayout(new BoxLayout(teamList, BoxLayout.Y_AXIS));
        tlWidth = 281;
        Dimension tlMax = new Dimension(tlWidth,462);
        teamList.setPreferredSize(tlMax);
        teamList.setMaximumSize(tlMax);
        teamList.setBackground(Color.BLACK);
        teamInfo = new HashMap();
        //Get the team names and build Panel, Method for organizational purposes
        getTeams();       
        addComponent(teamList, 2, 2, 1, 1, 0, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, gridbag);
        }

    private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, int fill, int anchor, GridBagLayout gridbag){
        /*
        This method adds components to the main panel
        */
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx=gridx;
        constraints.gridy=gridy;
        constraints.gridwidth=gridwidth;
        constraints.gridheight=gridheight;
        constraints.weightx=weightx;
        constraints.weighty=weighty;
        constraints.fill=fill;
        constraints.anchor = anchor;
        gridbag.setConstraints(component, constraints);
        add(component);
    }

    public void getPlayers() {        
        /*
        This method finds which players are available to be drafted and builds a 
        table with them. It is a seperate method for organizational purposes.
        */
        playerEntry = new Vector<Vector<String>>();

        String format = "%1$25s %2$4s %3$5s %4$4s %5$5s %6$5s %7$5s %8$6s %9$4s %10$4s %11$6s %12$4s %13$5s %14$6s %15$4s %16$3s %17$3s"; 
        String rsStr="";
        int i=0;

        try {
           Connection con = DriverManager.getConnection(db,"","");
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT * FROM Offense_Proj WHERE Available='y' ORDER BY ESPN");
           ResultSetMetaData rsmd = rs.getMetaData();
           int columnsNumber = rsmd.getColumnCount();
      
           for (int x=1; x<columnsNumber; ++x) {
                //columnNames[x-1]=rsmd.getColumnLabel(x);
                columns.add(x-1,rsmd.getColumnLabel(x));               
           }

           String rsStr1="";
           while (rs.next()) {
               rsStr=""; 
               playerData = new Vector<String>(17);
               for (int x=1; x<columnsNumber; ++x) {
                    rsStr1=rs.getString(x);
                    playerData.add(x-1,rsStr1);
                    rsStr=rsStr+"   "+rsStr1;
                    if (x==1) {
                        playerRow.put(rsStr1, i);
                    }
                }
               System.out.println(i + " "+rsStr);
               playerEntry.add(i,playerData); 
               ++i;               
           }
           con.close();
           //Build Table
           tm = new DefaultTableModel(playerEntry, columns);
           if (jt==null) {
             jt = new JTable(tm)
             {  
             @Override
             public boolean isCellEditable(int row, int col)  
             {  
                return false;  
             }  
             };
             jt.setFont(new Font("Serif", Font.BOLD, 9));
             jt.setBackground(Color.BLACK);
             jt.setForeground(Color.WHITE);
             jt.getTableHeader().setFont(new Font("Serif", Font.BOLD,9));
             jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
             jt.getColumnModel().getColumn(0).setPreferredWidth(95);
             jt.getColumnModel().getColumn(1).setPreferredWidth(30);
             jt.getColumnModel().getColumn(2).setPreferredWidth(30);
             jt.getColumnModel().getColumn(3).setPreferredWidth(25);
             jt.getColumnModel().getColumn(4).setPreferredWidth(25);
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
        
           }              
        } catch (SQLException ex) {
            Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        //get player when row is selected  
        ListSelectionModel selectionModel = jt.getSelectionModel();  
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        selectionModel.addListSelectionListener(new RowListener(this));
        
        JScrollPane sp = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        Dimension spMax = new Dimension(768,462);
        sp.setPreferredSize(spMax);
        sp.setMaximumSize(spMax);
        addComponent(sp, 0 , 2, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, gridbag);
    }
    
    public void getPlayerRows(){
        /*
        This method keeps track of which player is in which row sos they can 
        be removed from the table when drafted.
        */
            String tableName="Offense_Proj";
            int queryType = 1;
            String[] playerCol = new String[] {"*"};
            String[] playerConVal = new String[] {"y"};
            String[] playerConCol = new String[] {"Available="};
            String orderVal = "ESPN";
            int[] playerConType = new int[] {1};
            String str = sqlString(queryType, tableName, null, playerCol, null, playerConVal, playerConCol, playerConType, orderVal);
            
        try {
           Connection con = DriverManager.getConnection(db,"","");
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery(str);
           //ResultSetMetaData rsmd = rs.getMetaData();
           int i=0;
           while (rs.next()) {
                         
                String rsStr1=rs.getString(1);
                playerRow.put(rsStr1, i);
                ++i;
           }
           con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public String sqlString(int queryType, String tableName, String[] queryVal, String [] queryCol, int[] colType, String[] queryConVal, String[] queryConCol, int[] colConType, String orderVal) {
        /*
        This method returns a string for SQL execution
        queryType: 0 update, 1 single condition query, 2 multiple condition query
        colType: 0 is int, 1 is string 
        */
        
        int i = 0;
        if (queryVal!=null) {
            for (String val:queryVal) {
                if (colType[i] == 1) {
                    queryVal[i] = "'" + val + "'";
                }
                ++i;
            }
        }
        
        i = 0;
        if (queryConVal!=null) {
            for (String val:queryConVal) {
                if (colConType[i] == 1) {
                    queryConVal[i] = "'" + val + "'";
                }
                ++i;
            }
        }
        
        String sqlStr = "";
        if (queryType == 0) {
            sqlStr = "UPDATE " + tableName + " SET ";
            for (int x=0; x<=(queryVal.length-1); x++) {
                if (x==0) {
                    sqlStr = sqlStr + queryCol[x] + "=" + queryVal[x];
                } else {
                    sqlStr = sqlStr + " AND " + queryCol[x] + "=" + queryVal[x];
                }
            }
            sqlStr = sqlStr + " WHERE ";
            for (int x=0; x<=(queryConVal.length-1); x++) {
                if (x==0) {
                    sqlStr = sqlStr + queryConCol[x] + "=" + queryConVal[x];
                } else {
                    sqlStr = sqlStr + " AND " + queryCol[x] + "=" + queryVal[x];
                }
            }
        } else if (queryType==1 || queryType==2) {
            sqlStr = "SELECT ";
            for (int x=0; x<=(queryCol.length-1); x++) {
                if (x==0) {
                    sqlStr = sqlStr + queryCol[x];
                } else {
                    sqlStr = sqlStr + " AND " + queryCol[x];
                }
            }
            sqlStr = sqlStr + " FROM " + tableName + " WHERE ";
            for (int x=0; x<=(queryConVal.length-1); x++) {
                sqlStr = sqlStr + queryConCol[x] + queryConVal[x];           
            }
        }
        if (queryType==2){
            sqlStr+=")";
        }
        if (orderVal!=null) {
            sqlStr = sqlStr + " ORDER BY " + orderVal;
        }
        System.out.println(sqlStr);
        return sqlStr;
}
    
    public void getTeams() {
        /*
        This method grabs the teams for the draft and builds the panels to display
        the players on each team. It is a seperate method for organizational purposes.
        */
        Vector<String> teams = new Vector();
        String sqlstr = "Select * FROM Teams ORDER BY TeamName";
        
        try {
           Connection con = DriverManager.getConnection(db,"","");
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery(sqlstr);
           //ResultSetMetaData rsmd = rs.getMetaData();
           while (rs.next()) {            
                String rsStr1=rs.getString(1);
                String rsStr2=rs.getString(2);
                teamInfo.put(rsStr1, rsStr2);   
           }
           con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map<String, String> treemap = new TreeMap(teamInfo);
        int myTeamIndex = 0;
        String myTeam = "Team2";
        int counter =0;
        for(Map.Entry<String, String> entry: treemap.entrySet()) {
            teams.add(entry.getKey());
            if (entry.getKey().equals(myTeam)) {
                myTeamIndex=counter;
            }
            ++counter;
        }
        //Build Panels
        teamMenu = new JComboBox(teams);
        Dimension tmMax = new Dimension(tlWidth,30);
        teamMenu.setPreferredSize(tmMax);
        teamMenu.setMaximumSize(tmMax);
        teamMenu.setFont(new Font("Sherif", Font.BOLD, 18));
        teamMenu.addActionListener(new MyComboBox(this));
        teamMenu.setSelectedIndex(myTeamIndex);
        teamList.add(teamMenu);
        currentTeam= new JPanel();
        Dimension ctMax = new Dimension(tlWidth,310);
        currentTeam.setBackground(Color.BLACK);
        currentTeam.setPreferredSize(ctMax);
        currentTeam.setMinimumSize(ctMax);
        getTeamPlayers(myTeam);
        teamList.add(currentTeam);
        BufferedImage wvuimg = null;
        try {
           wvuimg = ImageIO.read(new File("wvulogo.jpg"));
        } catch (IOException e) {
           Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, e);
       }
       Image swvuimg = wvuimg.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
       picLabel = new JLabel(new ImageIcon(swvuimg));
       Dimension plMax = new Dimension(tlWidth,200);
       picLabel.setPreferredSize(plMax);
       picLabel.setMaximumSize(plMax);
       teamList.add(picLabel);  
    }
    
    public void getTeamPlayers(String teamName) {
        /*
        This method gets the players on each team and builds the panel with this
        info. It updates it with the players as they are drafted.
        */
        String teamDB = (String)teamInfo.get(teamName);
        System.out.println(teamDB + "!!");
        if (teamPos != null) {
            currentTeam.remove(teamPos);
        }
        if (teamPlay != null){
            currentTeam.remove(teamPlay);
        }
        currentTeam.revalidate();

        String sqlstr = "Select * FROM " + teamDB + " ORDER BY PosID";

        try {
           Connection con = DriverManager.getConnection(db,"","");
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery(sqlstr);
           //ResultSetMetaData rsmd = rs.getMetaData();
           teamPos = new JPanel();
           teamPos.setBackground(Color.BLACK);
           Dimension tpMax = new Dimension(60,310);
           teamPos.setPreferredSize(tpMax);
           teamPos.setMaximumSize(tpMax);
           teamPos.setLayout(new BoxLayout(teamPos, BoxLayout.Y_AXIS));
           teamPlay = new JPanel();
           Dimension tplMax = new Dimension(220,310);
           teamPlay.setPreferredSize(tplMax);
           teamPlay.setMaximumSize(tplMax);
           teamPlay.setBackground(Color.BLACK);
           teamPlay.setLayout(new BoxLayout(teamPlay, BoxLayout.Y_AXIS));
           while (rs.next()) {
                String pos = rs.getString(2);
                String play = rs.getString(3);
                if (play==null) {
                    play=" ";
                }

                JLabel posInfo = new JLabel(pos, JLabel.LEFT);
                posInfo.setFont(new Font("Sherif", Font.BOLD, 19));                
                posInfo.setForeground(Color.WHITE);
                
                JLabel posPlayer = new JLabel(play);
                posPlayer.setFont(new Font("Sherif", Font.PLAIN, 19));
                posPlayer.setForeground(Color.WHITE);
                
                posInfo.setPreferredSize(new Dimension(60, 25));
                posPlayer.setPreferredSize(new Dimension(220, 25));
                //posInfo.setMinimumSize(new Dimension(100, 15));
                //posPlayer.setMinimumSize(new Dimension(156, 15));
                posInfo.setMaximumSize(new Dimension(60, 25));
                posPlayer.setMaximumSize(new Dimension(220, 25));              
                teamPos.add(posInfo);
                teamPlay.add(posPlayer);
           }
           currentTeam.add(teamPos);
           currentTeam.add(teamPlay);
           con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void incrementPick(int pick) {
        /*
        This method builds and increments the draft order panel at the top of the
        GUI whenever a player is drafted.
        */
        int pickLast = pick + 9;
        ButtonClass.currPick = pick;
        
        if (currOrder != null) {
            draftOrder.remove(currOrder);
            draftOrder.revalidate();
        }
        
        String str = "Select * FROM DraftOrder WHERE Pick >= " + pick + " AND Pick <= " + pickLast +" ORDER BY Pick";
        
        try {          
           Connection con = DriverManager.getConnection(db,"","");
           Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery(str);
           //ResultSetMetaData rsmd = rs.getMetaData();
           //int i=0;
           currOrder = new JPanel();
           currOrder.setBackground(Color.BLACK);
           currOrder.setPreferredSize(new Dimension(1280, 154));
           while (rs.next()) {
                int ovPick = rs.getInt(1);
                int round = rs.getInt(2);
                int roundPick = rs.getInt(3);
                String teamPick = rs.getString(4);
                String teamPlayer = rs.getString(5);
                
                if (roundPick == 1) {
                    String roundHeader = "Round " + round;
                    JLabel newRound = new JLabel(roundHeader);
                    newRound.setFont(new Font("Sherif", Font.BOLD, 14));
                    newRound.setForeground(Color.BLUE);
                    currOrder.add(newRound);
                }
                JLabel teamHeader = new JLabel(teamPick);
                if (ovPick == pick) {
                    teamHeader.setFont(new Font("Sherif", Font.BOLD, 42));
                    teamHeader.setForeground(Color.YELLOW);
                    teamHeader.setBorder(BorderFactory.createRaisedBevelBorder());
                    ButtonClass.currTeam = teamPick;
                } else {
                    teamHeader.setFont(new Font("Sherif", Font.BOLD, 25));
                    teamHeader.setForeground(Color.WHITE);    
                }
                currOrder.add(teamHeader);
           }
           draftOrder.add(currOrder);
           con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DraftPanel.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }

}
