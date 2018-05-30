package org.jwellman.csvviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;
import org.jwellman.csvviewer.models.Person;
import org.jwellman.swing.component.HorizontalGraphitePanel;
import org.jwellman.swing.jtable.JTablePropertyAction;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;

public class IssuesBrowser extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private JTable table; 
    
    private static final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    private static final Border BORDER_ETCHED = BorderFactory.createEtchedBorder();
    
    public IssuesBrowser() {

     // this.setLayout(new GridBagLayout()); 
		this.setLayout(new BorderLayout());		
		
		this.add(this.createFileDropTarget(), BorderLayout.CENTER);
		
		this.add(this.createEasternPanel(), BorderLayout.EAST);
    }
    
    private Component createFileDropTarget() {
    	
    	JPanel panel = new JPanel(new GridBagLayout()); // (new GridBagLayout());
    	panel.setBorder( BorderFactory.createCompoundBorder(BORDER_EMPTY, BORDER_ETCHED) ); //(BORDER_EMPTY);
    	
    	JLabel label = new JLabel("Open a file by dragging it here.");
    	// label.setBorder(BORDER_ETCHED);
    	label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setForeground(new Color(0x92b0b3));
        label.setFont(new Font("Roboto", Font.PLAIN, calcPointSize(20)));

        final DropTarget target = new DropTarget(panel, new FileDropTarget(panel));
    	panel.add(label); // BorderLayout.CENTER
    	
		return panel;
	}

    private Component createFileDropTargetOriginal() {

    	JPanel panel = new JPanel(new BorderLayout()); // (new GridBagLayout());
    	panel.setBorder(BORDER_EMPTY);
    	
    	JLabel label = new JLabel("Choose a file or drag it here.");
    	label.setBorder(BORDER_ETCHED);
    	label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setForeground(new Color(0x92b0b3));
        label.setFont(new Font("Roboto", Font.PLAIN, calcPointSize(20)));

        final DropTarget target = new DropTarget(label, new FileDropTarget(label));
    	panel.add(label, BorderLayout.CENTER); // BorderLayout.CENTER
    	
		return panel;
	}

    /**
     * https://stackoverflow.com/questions/5829703/java-getting-a-font-with-a-specific-height-in-pixels
     * 
     * @param pixelSize
     * @return
     */
    private int calcPointSize(int pixelSize) {
    	final double fontSize= pixelSize * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;
		return (int) fontSize;
	}

	private Component createCsvTable() {
		
		final EventList<Person> personList = new BasicEventList<>();
		personList.add(new Person("Anthony Hopkins", 74));
		personList.add(new Person("Barack Obama", 50));
		personList.add(new Person("American McGee", 39));

		//		EventList people = new BasicEventList();
		//		// Add all the elements
		//		for (Person p : getPeople()) {
		//			personList.add(p);
		//		}
		
		TableFormat personTableFormat = GlazedLists.tableFormat(
				Person.class,
				new String[] { "name", "age" }, // Names of the properties to fetch
				new String[] { "Name", "Age" }); // Names for the columns
		
		EventTableModel tableModel = new EventTableModel(personList, personTableFormat);
		table = new JXTable(tableModel); // new JXTable(tableModel); // JTable(tableModel);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Any modifications to the ‘people’ list is automatically reflected in the table
		
		final NumberCellRenderer cellRenderer = new NumberCellRenderer("Consolas"); 
		// order of preference: Consolas , Lucida Console , Bitstream Vera Sans Mono 
		// Courier New , Lucida Sans Typewriter
		// following are interesting (* = not monospaced):
		// OCR A Extended , HP Simplified* , LuzSans-Book* , Rockwell* , Roboto*
		// Sitka Text* , X-Files*
		for (int i=0; i<table.getColumnCount(); i++) {
			final String cname = table.getColumnName(i);
			table.getColumn(cname).setCellRenderer(cellRenderer);
		}
		//table.getTableHeader().setFont(cellRenderer.getFont());
		//table.getTableHeader().setDefaultRenderer(cellRenderer);
		//table.setColumnSelectionAllowed(true);

		((JXTable)table).packAll(); 
		//this.resizeColumnWidth(table);

		JScrollPane pane = new JScrollPane(table);
		//pane.setMinimumSize(new Dimension(500,250));
		// scrollPane.add(table);
		return pane;
	}

	private Component createEasternPanel() {
		JPanel actions = new JPanel();
		actions.setLayout(new BorderLayout()); //(new BoxLayout(actions, BoxLayout.PAGE_AXIS));
		actions.setBorder( BorderFactory.createCompoundBorder(BORDER_EMPTY, BORDER_ETCHED) );
		
		JToggleButton jtbButton = new JToggleButton("ToggleButton Press Me");
		actions.add(jtbButton, BorderLayout.CENTER);

		JButton packButton = HorizontalGraphitePanel.createButton("PACK", null, null);
		JButton unpackButton = HorizontalGraphitePanel.createButton("UNPACK", null, null);
		final HorizontalGraphitePanel graphitepanel = HorizontalGraphitePanel
				.createDefault(Arrays.asList(packButton, unpackButton));
		actions.add(graphitepanel, BorderLayout.NORTH);

		JPanel south = new JPanel(new GridLayout(0,1));
		//JButton btnClearSelectionButton = new JButton(new JTablePropertyAction("CLEAR", table));		
		south.add(new JButton(new JTablePropertyAction("CLEAR", table)));
		south.add(new JCheckBox(new JTablePropertyAction("CLEAR", table)));
		south.add(new JCheckBox(new JTablePropertyAction("CLEAR XYZ", table)));
		
		JToggleButton a = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton("CLEAR"), null, null);
		south.add(HorizontalGraphitePanel
				.createDefault(Arrays.asList(a)));
		
		actions.add(south, BorderLayout.SOUTH);

		return actions;
	}

	private Component createEasternPanelOriginal() {
		JPanel actions = new JPanel();
		actions.setLayout(new BorderLayout()); //(new BoxLayout(actions, BoxLayout.PAGE_AXIS));
		actions.setBorder( BorderFactory.createCompoundBorder(BORDER_EMPTY, BORDER_ETCHED) );
		
		JToggleButton jtbButton = new JToggleButton("ToggleButton Press Me");
		actions.add(jtbButton, BorderLayout.CENTER);

		JButton packButton = HorizontalGraphitePanel.createButton("PACK", null, null);
		JButton unpackButton = HorizontalGraphitePanel.createButton("UNPACK", null, null);
		final HorizontalGraphitePanel graphitepanel = HorizontalGraphitePanel
				.createDefault(Arrays.asList(packButton, unpackButton));
		actions.add(graphitepanel, BorderLayout.NORTH);

		JPanel south = new JPanel(new GridLayout(0,1));
		//JButton btnClearSelectionButton = new JButton(new JTablePropertyAction("CLEAR", table));		
		south.add(new JButton(new JTablePropertyAction("CLEAR", table)));
		south.add(new JCheckBox(new JTablePropertyAction("CLEAR", table)));
		south.add(new JCheckBox(new JTablePropertyAction("CLEAR XYZ", table)));
		
		JToggleButton a = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton("CLEAR"), null, null);
		south.add(HorizontalGraphitePanel
				.createDefault(Arrays.asList(a)));
		
		actions.add(south, BorderLayout.SOUTH);

		return actions;
	}

	@SuppressWarnings("serial")
	private class NumberCellRenderer extends DefaultTableCellRenderer  {
    	private Font customFont;
    	
    	public NumberCellRenderer(String fontname) {
	    	this.setFont(this.customFont = this.getFont(fontname, Font.PLAIN, 12));
	    	setHorizontalAlignment(SwingConstants.RIGHT);
    	}
    	
    	@Override
    	public Font getFont() {
    		return customFont;
    	}
    	
    	/**
    	 * https://wesbos.com/programming-fonts/
    	 * 
    	 * https://docs.oracle.com/javase/tutorial/2d/text/fonts.html
    	 * https://www.javalobby.org/java/forums/t98492.html
    	 * https://docs.oracle.com/javase/7/docs/technotes/guides/intl/font.html
    	 * https://wpollock.com/Java/Fonts.htm
    	 * http://edn.embarcadero.com/article/29991
    	 * https://superuser.com/questions/988379/how-do-i-run-java-apps-upscaled-on-a-high-dpi-display
    	 * http://www.pushing-pixels.org/category/java
    	 * 
    	 * !! http://www.pushing-pixels.org/2018/05/23/hello-radiance.html
    	 * !! http://www.pushing-pixels.org/2017/02/23/releases-2017-h1.html
    	 * http://www.pushing-pixels.org/2018/05/18/the-art-and-craft-of-screen-graphics-interview-with-krista-lomax.html
    	 * 
    	 * 
    	 * @param name
    	 * @param style
    	 * @param size
    	 * @return
    	 */
        private Font getFont(String name, int style, int size) {
        	String fontname = "Dialog";
        	int fontstyle = Font.PLAIN;
        	int fontsize = 36;
        	
        	final GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final String[] names = gEnv.getAvailableFontFamilyNames();    
            final List<String> namesList = Arrays.asList(names);
            System.out.println(namesList);
            
            if (namesList.contains(name)) {
            	fontname = name;
            	fontstyle = style;
            	fontsize = size;
            }
            System.out.println("" + fontname);
            
        	final Font font = new Font(fontname, fontstyle, fontsize);
            return font;
        }

    }
    
    /**
     * As taken from https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
     * 
     * @param table
     */
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300) width=300;
            if(width < 50) width=50;
            
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    
    private class IssueTableFormat implements TableFormat<Person> {

        public int getColumnCount() {
            return 2;
        }
        
        public String getColumnName(int column) {
            if (column == 0)      return "Name";
            else if (column == 1) return "Age";

            throw new IllegalStateException();
        }
        
        public Object getColumnValue(Person issue, int column) {
            if(column == 0)      return issue.getName();
            else if(column == 1) return issue.getAge();

            throw new IllegalStateException();
        }
    }

}
