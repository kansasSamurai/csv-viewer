package org.jwellman.utility;

import javax.swing.*;
import javax.swing.table.*;

import org.jwellman.foundation.Foundation;
import org.jwellman.foundation.extend.SimpleMain;

import java.awt.*;

public class SCCE extends SimpleMain {
	
  JTable table;
  
  public static void main(String[] args) {
	  boolean useOriginal = true;
	  if (useOriginal)
		  new SCCE("original");
	  else {
		  new SCCE();
	  }
  }

  public SCCE() {
	  
    // Step 1 - Initialize Swing
	final Foundation f = Foundation.get().init();
	
    // Step 2 - Create your UIs in JPanel(s)
    mainui = f.registerUI("viewer", new ThisPanel());

    // Step 3 - Use Foundation to create your "window"; give it your UI.
    window = f.useWindow(mainui);
    // Step 3a (optional) - Customize your window
    window.setTitle("Setting the margin in JTable Component!"); 
    window.setResizable(true);
    
    // Step 5 - Display your User Interface
    f.showGUI();
    
  }
    
  /**
   * This is the original constructor for reference (with very small mods):
   * https://www.roseindia.net/java/example/java/swing/CellGap.shtml 
   * 
   * @param original
   */
  public SCCE(String original) {
	  JFrame frame = new JFrame("Setting the margin in JTable Component!");
	  JPanel panel = new JPanel();
	  panel.setLayout(new BorderLayout()); // new 
	  
	  String data[][] = {
		{"Vinod","Computer","3"},
	    {"Rahul","History","2"},
	    {"Manoj","Biology","4"},
		{"Vinod","Computer","3"},
	    {"Rahul","History","2"},
	    {"Manoj","Biology","4"},
		{"Vinod","Computer","3"},
	    {"Rahul","History","2"},
	    {"Manoj","Biology","4"},
	    {"Sanjay","PSD","5"}
	    };
	  String col [] = {"Name","Course","Year"};
	  DefaultTableModel model = new DefaultTableModel(data,col);
	  table = new JTable(model);
	  Dimension dim = new Dimension(200,10); // new Dimension(20,1);
	  table.setIntercellSpacing(new Dimension(dim));
	  //this.SetRowHight(table);
	  table.setColumnSelectionAllowed(true);
	  JTableHeader header = table.getTableHeader();
	  header.setBackground(Color.yellow);
	  JScrollPane pane = new JScrollPane(table);
	  panel.add(pane, BorderLayout.CENTER); // modified
	  frame.add(panel);
	  //frame.setUndecorated(true); // removed
	  //frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG); // removed
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  frame.pack(); // new 
	  frame.setSize(500,150);
	  frame.setVisible(true);
  }
  
  public void SetRowHight(JTable table){
    int height = table.getRowHeight();
    table.setRowHeight(height+10);
  }
  
  private void updateRowHeights_fast() {
      for (int row = 0; row < table.getRowCount(); row++)
      {
          int rowHeight = table.getRowHeight(); 
          System.out.print("row height before: " + rowHeight);

          //assume first column is sufficient to adjust the entire row
          for (int column = 0; column < 1; column++) {
              Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
              rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
          }

          System.out.println(", after: " + rowHeight);
          table.setRowHeight(row, rowHeight);
      }
  }

  @SuppressWarnings("serial")
	private class ThisPanel extends JPanel {

		ThisPanel() {
			this.setLayout(new BorderLayout());
			
			final String data[][] = { 
					{ "Vinod", "Computer", "3" },
					{ "Rahul", "History", "2" }, 
					{ "Manoj", "Biology", "4" },
					{ "Vinod", "Computer", "3" },
					{ "Rahul", "History", "2" }, 
					{ "Manoj", "Biology", "4" },
					{ "Vinod", "Computer", "3" },
					{ "Rahul", "History", "2" }, 
					{ "Manoj", "Biology", "4" },
					{ "Sanjay", "PSD", "5" } 
					};
			final String col[] = { "Name", "Course", "Year" };
			final DefaultTableModel model = new DefaultTableModel(data, col);
			table = new JTable(model);
			table.setColumnSelectionAllowed(true);
			
			final Dimension dim = new Dimension(20, 20);
			table.setIntercellSpacing(new Dimension(dim));
		    SetRowHight(table);			
			updateRowHeights_fast();

			final JTableHeader header = table.getTableHeader();
			header.setBackground(Color.yellow);
			
			final JScrollPane pane = new JScrollPane(table);
			this.add(pane, BorderLayout.CENTER);

		}
		
	}
  
}

