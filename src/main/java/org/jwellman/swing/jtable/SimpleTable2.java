package org.jwellman.swing.jtable;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * https://www.safaribooksonline.com/library/view/java-swing/156592455X/ch16.html
 * 
 * This example works VERY well... but there is also a neat idea at:
 * https://stackoverflow.com/questions/8002445/trying-to-create-jtable-with-proper-row-header
 * 
 * Also, just wanted to capture this for table header renderers:
 * https://tips4java.wordpress.com/2009/02/27/default-table-header-cell-renderer/
 * https://tips4java.wordpress.com/2009/03/06/vertical-table-header-cell-renderer/
 * http://www.informit.com/articles/article.aspx?p=24142
 * http://www.informit.com/articles/article.aspx?p=24121&seqNum=4
 * https://stackoverflow.com/questions/7137786/how-can-i-put-a-control-in-the-jtableheader-of-a-jtable
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("serial")
public class SimpleTable2 extends JFrame {

  public SimpleTable2() {
    super("Simple JTable Test");
    
    this.setSize(300, 200);
    //addWindowListener(new BasicWindowMonitor());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    final TableModel tablemodel = new AbstractTableModel() {
      String data[] = {"", "a", "b", "c", "d", "e"};
      String headers[] = {"", "Column 1", "Column 2", "Column 3", "Column 4", "Column 5"};

      public int getRowCount() { return 1000; }
      public int getColumnCount() { return data.length; }
      
      public String getColumnName(int col) { return headers[col]; }

      // Synthesize some entries using the data values & the row #
      public Object getValueAt(int row, int col) { 
        return data[col] + (row+1); 
      }
      
    };

    // Create a column model for the main table. 
    // This model ignores the first column added, 
    // and sets a minimum width of 150 pixels for all others.
    final TableColumnModel tcmMain = new DefaultTableColumnModel() {
      boolean first = true;
      public void addColumn(TableColumn tc) {
        // Drop the first column . . . that'll be the row header
        if (first) { first = false; return; }
        tc.setMinWidth(150);
        super.addColumn(tc);
      }
    };

    // Create a column model that will serve as our row header table. 
    // This model picks a maximum width and only stores the first column.
    final TableColumnModel tcmRows = new DefaultTableColumnModel() {
      boolean first = true;
      public void addColumn(TableColumn tc) {
        if (first) {
          tc.setMaxWidth(35);
          super.addColumn(tc);
          first = false;
        }
        // Drop the rest of the columns . . . this is the header column only
      }
    };

    final JTable tblMain = new JTable(tablemodel, tcmMain);
    tblMain.createDefaultColumnsFromModel();

    // Set up the header column and get it hooked up to everything
    final JTable tblRows = new JTable(tablemodel, tcmRows);
    tblRows.createDefaultColumnsFromModel();

    // Make the header column look pretty
    // headerColumn.setMaximumSize(new Dimension(40, 10000));
    tblRows.setBackground(Color.lightGray);
    // If you want to make the header selection invisible, uncomment this next line:
    // headerColumn.setSelectionBackground(Color.lightGray);
    tblRows.setColumnSelectionAllowed(false);
    //headerColumn.setCellSelectionEnabled(false);

 // ====================================================================
    
    // Without shutting off autoResizeMode, our tables won't scroll correctly (horizontally, anyway)
    tblMain.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //(JTable.AUTO_RESIZE_ALL_COLUMNS);
    tblRows.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    // Make sure that selections between the main table 
    // and the header stay in sync (by sharing the same model)
    tblMain.setSelectionModel(tblRows.getSelectionModel());

// ====================================================================
    
    // Put it in a viewport that we can control a bit
    final JViewport jv = new JViewport();
    jv.setView(tblRows);
    jv.setPreferredSize(tblRows.getMaximumSize());

    // We have to manually attach the row headers, 
    // but after that, the scroll pane keeps them in sync
    final JScrollPane jsp = new JScrollPane(tblMain);
    jsp.setRowHeader(jv);

    this.add(jsp, BorderLayout.CENTER); //getContentPane().add(jsp, BorderLayout.CENTER);
    
  }

  public static void main(String args[]) {
    SimpleTable2 st = new SimpleTable2();
    st.pack();
    st.setVisible(true);
  }
  
}
