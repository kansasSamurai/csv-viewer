package org.jwellman.csvviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;
import org.jwellman.csvviewer.models.Person;
import org.jwellman.foundation.swing.XButton;
import org.jwellman.foundation.swing.XCheckBox;
import org.jwellman.foundation.swing.XLabel;
import org.jwellman.foundation.swing.XTextField;
import org.jwellman.foundation.swing.XToggleButton;
import org.jwellman.swing.actions.FileActionAware;
import org.jwellman.swing.component.HorizontalGraphitePanel;
import org.jwellman.swing.jtable.BetterJTable;
import org.jwellman.swing.jtable.JTablePropertyAction;
import org.jwellman.swing.jtable.TableColumnManager;
import org.jwellman.swing.layout.SpringUtilities;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;

@SuppressWarnings("deprecation")
public class IssuesBrowser extends JPanel implements FileActionAware {

    private static final long serialVersionUID = 1L;
    
    private JTable csvTable = new JXTable(); // new JXTable(tableModel); // JTable(tableModel); // BetterJTable
    
    private TableModel csvTableModel;
    
    private JPanel boxlayout;
    
    private static final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    private static final Border BORDER_ETCHED = BorderFactory.createEtchedBorder();
    
    private static final Border BORDER_COMPOUND = BorderFactory.createCompoundBorder(BORDER_EMPTY, BORDER_ETCHED);
    
    private static final Font FONT_SEGOE_UI = new Font("Segoe UI", Font.PLAIN, 10);
    
    public IssuesBrowser() {

		this.setLayout(new BorderLayout());		
		
		this.add(this.createFileDropTarget(), BorderLayout.CENTER);
		
		this.add(this.createEasternPanel(), BorderLayout.EAST);
    }
    
    private Component createFileDropTarget() {
    	
    	final JPanel panel = new JPanel(new GridBagLayout()); // (new GridBagLayout());
    	panel.setBorder( BORDER_COMPOUND ); // BORDER_EMPTY ;
        // final DropTarget target = 
        new DropTarget(panel, new FileDropTarget(panel, this));
    	
        final String calltoaction = "Open a file by dragging it here.";
        final Font font = new Font("Roboto", Font.PLAIN, calcPointSize(20));
        final Color foreground = new Color(0x92b0b3);
    	boolean uselabel = true;
    	if (uselabel) {
            final JLabel label = new JLabel(calltoaction);
            label.setBorder(BORDER_EMPTY);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setForeground(foreground);
            label.setFont((font));    	    
            panel.add(label); // BorderLayout.CENTER
    	} else {
    	    /* Using the textarea is inferior to using the label for this purpose because:
    	     * - The textarea background is opaque 
    	     *   (yes, a workaround exits to set the background color, but...)
    	     * - The textarea overrides the dragging operation whereas the label does not seem to
    	     *   (I do not know if a workaround exists)
    	     */
    	    final JTextArea text = new JTextArea();
    	    text.setBorder(BORDER_EMPTY);
    	    // text.setLineWrap(true); text.setWrapStyleWord(true); // this did not do what I expected/wanted
    	    text.setEditable(false);
    	    text.setText(calltoaction);    	
    	    text.setFont(font);
    	    text.setForeground(foreground);
    	    
    	    boolean trytoaddbutton = true;
    	    if (trytoaddbutton) {
    	        JButton b = new JButton("T");
    	        text.add(b);
    	    }
    	    
    	    panel.add(text);
    	}
    	
		return panel;
	}

    /**
     * TODO Add to another class/project specific to Swing Font utilities.
     * https://stackoverflow.com/questions/5829703/java-getting-a-font-with-a-specific-height-in-pixels
     * 
     * @param pixelSize
     * @return
     */
    private int calcPointSize(int pixelSize) {
    	final double fontSize= pixelSize * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;
		return (int) fontSize;
	}

    private JComponent createCsvTable(File file) {
        return createCsvTableV2(file);
    }
    
	private JComponent createCsvTableV1(File file) {
        final JScrollPane pane = (csvTable instanceof BetterJTable) 
                ? BetterJTable.createStripedJScrollPane(csvTable) 
                : new JScrollPane(csvTable);
        if (csvTable instanceof BetterJTable) {
            // do nothing (yet?)
        } else {
        }
        pane.setBorder( BORDER_COMPOUND );
        // pane.setMinimumSize(new Dimension(500,250));
        // scrollPane.add(csvTable);            

		final EventList<Person> personList = new BasicEventList<>();
		personList.add(new Person("Anthony Hopkins", 74));
		personList.add(new Person("Barack Obama", 50));
		personList.add(new Person("American McGee", 39));

		//		EventList people = new BasicEventList();
		//		// Add all the elements
		//		for (Person p : getPeople()) {
		//			personList.add(p);
		//		}
		
		TableFormat<Person> personTableFormat = GlazedLists.tableFormat(
				Person.class,
				new String[] { "name", "age" }, // Names of the properties to fetch
				new String[] { "Name", "Age" }); // Names for the columns
		
		/*
		 * Use {@link DefaultEventTableModel} instead. This class will be removed in the GL
         *     2.0 release. The wrapping of the source list with an EDT safe list has been
         *     determined to be undesirable (it is better for the user to provide their own EDT
         *     safe list).
		 */
		EventTableModel<Person> tableModel = new EventTableModel<>(personList, personTableFormat);
		// csvTable = new JTable(tableModel); // new JXTable(tableModel); // JTable(tableModel);
		// ^^^ this line was moved to initializer and replaced with following line
		csvTable.setModel(tableModel);		
        // Any modifications to the ‘people’ list is automatically reflected in the csvTable
		
		boolean fillpanel = false;
		if (fillpanel) {
		    // do nothing; autoresize is on by default
		    // This is generally undesirable as most delimited files will be wider
		    // than the default size of the panel and overflow/scrolling is desirable.
		} else {
	        csvTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		final NumberCellRenderer cellRenderer = new NumberCellRenderer("Consolas"); 
		// order of preference: Consolas , Lucida Console , Bitstream Vera Sans Mono 
		// Courier New , Lucida Sans Typewriter
		// following are interesting (* = not monospaced):
		// OCR A Extended , HP Simplified* , LuzSans-Book* , Rockwell* , Roboto*
		// Sitka Text* , X-Files*
		for (int i=0; i<csvTable.getColumnCount(); i++) {
			final String cname = csvTable.getColumnName(i);
			csvTable.getColumn(cname).setCellRenderer(cellRenderer);
		}

		csvTable.getTableHeader().setFont(cellRenderer.getFont());
		//csvTable.getTableHeader().setDefaultRenderer(cellRenderer);
		//csvTable.setColumnSelectionAllowed(true);

		if (csvTable instanceof JTable) {
	        this.resizeColumnWidth(csvTable);
		    
		} else if (csvTable instanceof JXTable) {
		    final JXTable t = (JXTable) csvTable;
	        t.packAll();
	        t.setFillsViewportHeight(false);
	        t.setColumnControlVisible(true);
		    
		}

		return pane;
	}

    private JComponent createCsvTableV2(File file) {
        final JScrollPane pane = (csvTable instanceof BetterJTable) 
                ? BetterJTable.createStripedJScrollPane(csvTable) 
                : new JScrollPane(csvTable);
        if (csvTable instanceof BetterJTable) {
            // do nothing (yet?)
        } else {
        }
        pane.setBorder( BORDER_COMPOUND );

        csvTable.setModel(csvTableModel = new DelimitedFileTableModel(file, ","));
        TableColumnManager tcm = new TableColumnManager(csvTable);
        
        boolean fillpanel = false;
        if (fillpanel) {
            // do nothing; autoresize is on by default
            // This is generally undesirable as most delimited files will be wider
            // than the default size of the panel and overflow/scrolling is desirable.
        } else {
            csvTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        
        final NumberCellRenderer cellRenderer = new NumberCellRenderer("Consolas"); 
        // order of preference: Consolas , Lucida Console , Bitstream Vera Sans Mono 
        // Courier New , Lucida Sans Typewriter
        // following are interesting (* = not monospaced):
        // OCR A Extended , HP Simplified* , LuzSans-Book* , Rockwell* , Roboto*
        // Sitka Text* , X-Files*
        for (int i=0; i<csvTable.getColumnCount(); i++) {
            final String cname = csvTable.getColumnName(i);
            csvTable.getColumn(cname).setCellRenderer(cellRenderer);
        }

        csvTable.getTableHeader().setFont(cellRenderer.getFont());
        //csvTable.getTableHeader().setDefaultRenderer(cellRenderer);
        //csvTable.setColumnSelectionAllowed(true);

        if (csvTable instanceof JTable) {
            this.resizeColumnWidth(csvTable);
            
        } else if (csvTable instanceof JXTable) {
            final JXTable t = (JXTable) csvTable;
            t.packAll();
            t.setFillsViewportHeight(false);
            t.setColumnControlVisible(true);
            
        }

        return pane;
    }
    
	private JPanel createEasternPanel() {
		JPanel actions = new JPanel();
		actions.setLayout(new BorderLayout()); //(new BoxLayout(actions, BoxLayout.PAGE_AXIS));
		actions.setBorder( BorderFactory.createCompoundBorder(BORDER_EMPTY, BORDER_ETCHED) );

        JPanel center = new JPanel(new GridLayout(0,1));
        center.setBorder(BorderFactory.createEmptyBorder(1, 1, 2, 2)); // fixes a small glitch caused by HorizontalGraphitePanel
        
        int designoption = 2;
        switch (designoption) {
        case 1:
            JButton b = HorizontalGraphitePanel.decorateButton(new JButton("BIG BUTTON"), null, null);
            //JToggleButton jtbButton = new JToggleButton("ToggleButton Press Me");
            center.add(HorizontalGraphitePanel.createDefault(Arrays.asList(b)));
            break;
        case 2:
            boxlayout = new JPanel();
            boxlayout.setLayout(new BoxLayout(boxlayout, BoxLayout.Y_AXIS));
            
            // Actually creating these now
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Name",  csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Age",   csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Month", csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Day",   csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Year",  csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());

            JScrollPane pane = new JScrollPane(boxlayout);
            pane.setBorder(null); // This may only be necessary when using Nimbus L&F?
            center.add(pane);
            break;
        }
		
        JPanel north = new JPanel(new GridLayout(0,1));
        north.setBorder(BorderFactory.createEmptyBorder(1, 1, 2, 2)); // fixes a small glitch caused by HorizontalGraphitePanel
        JButton packButton = HorizontalGraphitePanel.createButton("PACK", null, null);
        JButton unpackButton = HorizontalGraphitePanel.createButton("UNPACK", null, null);
        final HorizontalGraphitePanel graphitepanel = HorizontalGraphitePanel
                .createDefault(Arrays.asList(packButton, unpackButton));
        north.add(graphitepanel);       

		JPanel south = new JPanel();
		LayoutManager southlayout = new BoxLayout(south, BoxLayout.Y_AXIS); // new GridLayout(0,1)
		south.setLayout(southlayout);
		south.setBorder(BorderFactory.createEmptyBorder(1, 1, 2, 2)); // fixes a small glitch caused by HorizontalGraphitePanel

		JPanel delimiter = this.createDelimiter();
		south.add(delimiter);
				
		JToggleButton a = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton(), null, null);
		a.setAction(new JTablePropertyAction("CLEAR SELECTION",  csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null));
		south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(a)));

        JToggleButton b = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton(), null, null);
        b.setAction(new JTablePropertyAction("RESIZE MODE",  csvTable, JTablePropertyAction.ACTION_TOGGLE_AUTORESIZEMODE, null));
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(b)));

        // TODO toggle column selection
        
        actions.add(north, BorderLayout.NORTH);
		actions.add(south, BorderLayout.SOUTH);
        actions.add(center, BorderLayout.CENTER);

		return actions;
	}


    private JPanel createDelimiter() {
        return this.createDelimiterV3();
    }
    
	private JPanel createDelimiterV1() {
        final JPanel p = new JPanel(new SpringLayout());
        p.add(new JLabel("Delimiter"));
        p.add(new JTextField());
        p.add(new JLabel(""));
        
        final ButtonGroup bg = new ButtonGroup(); JButton b = null;
        final JPanel buttons = new JPanel(new GridLayout(1,0)); // FlowLayout() by default
        b = new JButton(","); buttons.add(b); bg.add(b);
        b = new JButton("~"); buttons.add(b); bg.add(b);
        b = new JButton("|"); buttons.add(b); bg.add(b);
        b = new JButton("\\t"); buttons.add(b); bg.add(b);
        p.add(buttons);
        
        SpringUtilities.makeCompactGrid(p, 2, 2, 1, 1, 1, 1);
        return p;
    }

    private JPanel createDelimiterV2() {
        final JPanel p = new JPanel(new SpringLayout());
        // Before: p.add(new JLabel("Delimiter").setFont(FONT_SEGOE_UI));
        // After: (next line)
        p.add(XLabel.create().setText("DELIMITER").setFont(FONT_SEGOE_UI).get());
        p.add(new JTextField());
        //p.add(new JLabel(""));
        
        final ButtonGroup bg = new ButtonGroup(); 
        final JPanel buttons = new JPanel(new GridLayout(1,0)); // FlowLayout() by default
        buttons.add(XButton.create().setText(",").setFont(FONT_SEGOE_UI).addTo(bg).get());
        buttons.add(XButton.create().setText("~").setFont(FONT_SEGOE_UI).addTo(bg).get());
        buttons.add(XButton.create().setText("|").setFont(FONT_SEGOE_UI).addTo(bg).get());
        buttons.add(XButton.create().setText("\\t").setFont(FONT_SEGOE_UI).addTo(bg).get());
        p.add(buttons);
        
        SpringUtilities.makeCompactGrid(p, 1, 3, 1, 1, 1, 1);
        return p;
    }

    private JPanel createDelimiterV3() {
        final JTextField tfield = (JTextField) XTextField.create().setFont(FONT_SEGOE_UI).get();
        tfield.setEditable(false);
        
        final JPanel p = new JPanel(new SpringLayout());
        p.setBorder(null);
        // p.add(XLabel.create().setText("DELIMITER").setFont(FONT_SEGOE_UI).get());

        final Dimension d = new Dimension(22, 1);
        final ButtonGroup bg = new ButtonGroup(); 
        p.add(HorizontalGraphitePanel.createDefault(Arrays.asList(
                 HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector(",", tfield)).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("~", tfield)).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("|", tfield)).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector("\\t", tfield)).addTo(bg).get(), null, d)
                ,HorizontalGraphitePanel.decorateButton((AbstractButton)XToggleButton.create().setAction(new DelimiterSelector(">", tfield)).addTo(bg).get(), null, d)
            )));
        
        p.add(tfield);
        
        SpringUtilities.makeCompactGrid(p, 1, 2, 0, 0, 0, 0);
        return p;
    }
    
    @SuppressWarnings("serial")
    private class DelimiterSelector extends AbstractAction {
        
        private JTextField textfield;
        
        public DelimiterSelector(String s, JTextField tf) {
            super(s);
            this.textfield = tf;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof AbstractButton) {
                AbstractButton b = (AbstractButton)e.getSource();
                final String command = b.getActionCommand();
                if (command.equals(">")) {
                    this.textfield.setEditable(true);
                    this.textfield.setText(null);
                } else {
                    this.textfield.setEditable(false);
                    this.textfield.setText(b.getActionCommand());
                }
                                
            }
        }
        
    }

    @SuppressWarnings("serial")
	private class NumberCellRenderer extends DefaultTableCellRenderer  {
    	private Font customFont;
    	
    	public NumberCellRenderer(String fontname) {
	    	this.setFont(this.customFont = this.getFont(fontname, Font.PLAIN, 12));
	    	setHorizontalAlignment(SwingConstants.LEFT);
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
            System.out.println("Requesting font: " + name);
            
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
            System.out.println("Delivering font: " + fontname);
            
        	final Font font = new Font(fontname, fontstyle, fontsize);
            System.out.println("Verifying font: " + font.getName());
            
            return font;
        }

    }
    
    /**
     * As taken from https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
     * 
     * @param csvTable
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
    
    @SuppressWarnings("unused")
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

    @Override
    public void doSingleFileAction(File file) {
        //jtextarea.read(new FileReader(file),null);
        
        
        final BorderLayout layout = (BorderLayout) this.getLayout();
        this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        this.add(this.createCsvTable(file), BorderLayout.CENTER);
        
//        final URL csv = file.toURI().toURL(); // new URL("http://myapp/employees.csv");
//        DefaultTableModelExt data = new DefaultTableModelExt(url);
//        TableModelExtTextLoader loader = new TableModelExtTextLoader(",", false, 75);
//        data.setLoader(loader);
//        data.startLoading();
        
        if (csvTable instanceof JTable) {
            //
        } else if (csvTable instanceof JXTable) {
            final JXTable t = (JXTable) csvTable;
            t.setColumnControlVisible(false);
        }

        this.validate();
    }

    @Override
    public void doListOfFilesAction(List<File> listoffiles) {
        throw new RuntimeException("Method not implemented");
    }

}
