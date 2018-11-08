package org.jwellman.csvviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
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
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import jiconfont.swing.IconFontSwing;
import jiconfont.icons.FontAwesome;
import jiconfont.icons.GoogleMaterialDesignIcons;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jwellman.csvviewer.interfaces.TextChooserAware;
import org.jwellman.csvviewer.models.Person;
import org.jwellman.foundation.swing.XButton;
import org.jwellman.foundation.swing.XLabel;
import org.jwellman.foundation.swing.XTextField;
import org.jwellman.foundation.swing.XToggleButton;
import org.jwellman.swing.Utilities;
import org.jwellman.swing.actions.FileActionAware;
import org.jwellman.swing.component.HorizontalGraphitePanel;
import org.jwellman.swing.dnd.FileDropTarget;
import org.jwellman.swing.font.FontFactory;
import org.jwellman.swing.jtable.BetterJTable;
import org.jwellman.swing.jtable.JTablePropertyAction;
import org.jwellman.swing.jtable.TableColumnManager;
import org.jwellman.swing.jtable.XTable;
import org.jwellman.swing.jtable.renderer.NumberCellRenderer;
import org.jwellman.swing.jtable.renderer.StringCellRenderer;
import org.jwellman.swing.layout.SpringUtilities;
import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;
import org.jwellman.utility.Limit;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;

/**
 * 
 * @author rwellman
 *
 */
@SuppressWarnings({"unused", "deprecation"})
public class DataBrowser extends JPanel implements FileActionAware, SwingConstants {

    private static final long serialVersionUID = 1L;
    
    // A reference to a callback object for data browser "events"
    private DataBrowserAware dataBrowserAware;
    
    private JTable tblCsvData;

	private TableModel csvTableModel;
    
    private TableColumnManager csvTableColumnManager;

    private TextChooserAware textChooser;
    
    private DelimiterChooser delimiterChooser;
    
    private JLabel statusFilename;
    
    private JLabel txtFilename;
    
    private JLabel txtFilesize;
    
    private JLabel txtLinecount;
    
	private JButton btnGlassPane;
    
    private JPanel boxpnlColumns;

    private final Font fontSmallData = FontFactory.getFont("Consolas", Font.PLAIN, 12);
    
    private final Font fontSmallLabel = FontFactory.getFont("Segoe UI", Font.PLAIN, 12);

    private final Font fontNormalGrid = FontFactory.getFont("Consolas", Font.PLAIN, 14);

    private final NumberCellRenderer numRenderer = new NumberCellRenderer(fontNormalGrid);
    
    private final StringCellRenderer strRenderer = new StringCellRenderer(fontNormalGrid);
    
    private static final Border BORDER_EMPTY = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    private static final Border BORDER_MATTE = BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(249,249,249));
    
    private static final Border BORDER_ETCHED = BorderFactory.createEtchedBorder();
    
    private static final Border BORDER_LINE = BorderFactory.createLineBorder(Color.black, 1);
    
    private static final Border BORDER_DEBUG = BorderFactory.createLineBorder(Color.red, 1);    
    
    private static final Border BORDER_DASHED = BorderFactory.createDashedBorder(null, 3.0f, 2.0f);
    
    private static final Border BORDER_COMPOUND = BorderFactory.createCompoundBorder(BORDER_EMPTY, BORDER_ETCHED);
    
    private static final Border BORDER_FIX = BorderFactory.createEmptyBorder(6, 0, 0, 0);
    
    private static final Border BORDER_DEBUG_INNER = BorderFactory
    		.createCompoundBorder(
    				BORDER_DASHED, 
    				BorderFactory.createEmptyBorder(12, 2, 2, 2)
    				);
    
    private static final Border BORDER_DEBUG_OUTER = BorderFactory
    		.createCompoundBorder(
    				BORDER_DEBUG_INNER, 
    				BORDER_DEBUG
    				);

    private static final Font FONT_SEGOE_UI = new Font("Segoe UI", Font.PLAIN, 12);
    
    private static final Font FONT_SEGOE_UI_BOLD = new Font("Segoe UI", Font.BOLD, 12);
    
    private static final Font FONT_CALIBRI_BOLD = new Font("Calibri", Font.BOLD, 14);
 
    private static final Font FONT_CALIBRI = new Font("Calibri", Font.PLAIN, 12);
   
    private static final Font FONT_VERDANA = new Font("Verdana", Font.PLAIN, 14);
   
    private static final Color COLOR_GREY_MED = new Color(136,136,136);
   
    private static final Color COLOR_GREY_DARKEST = new Color(64,64,64);
   
    private static final Color COLOR_EAST_TEXT = new Color(0xcdcdcd);

    private boolean printedCellSize = false;
    
    public DataBrowser(DataBrowserAware aware) {

		this.setLayout(new BorderLayout());		
		
		this.dataBrowserAware = aware;
		
		this.initJTable();
		
		this.add(this.createFileDropTarget(), BorderLayout.CENTER);
		
		this.add(this.createEasternPanel(), BorderLayout.EAST);
		
		this.add(this.createStatusBar(), BorderLayout.SOUTH);
		
    }
    
    private void initJTable() {
        final int tabletype = 1;
        switch (tabletype) {
        case 1:
            tblCsvData = new JTable();
            tblCsvData.setAutoCreateRowSorter(true);
            break;
        case 2:
            tblCsvData = new BetterJTable();
            break;
        case 3:
            JXTable xtable = (JXTable) (tblCsvData = new JXTable()); // new JXTable(tableModel); // JTable(tableModel); // BetterJTable           
            xtable.addHighlighter( new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, new Color(0x3A87AD), new Color(0xD9EDF7)) );
            xtable.setColumnControlVisible(true);
            xtable.setFillsViewportHeight(false);
            break;
        }
    }
 
    private Component createStatusBar() {
        Border b = BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0,5,1,5),
            BORDER_ETCHED
        );
        
        final JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.setBorder(b);
        
        this.statusFilename = new JLabel(" X:/.../........");
        statusBar.add(this.statusFilename);
        
        return statusBar;
    }

    private Component createFileDropTarget() {
    	
    	final JPanel panel = new JPanel(new GridBagLayout()); // (new GridBagLayout());
    	panel.setBorder( BORDER_COMPOUND ); // BORDER_EMPTY ;
        // final DropTarget target = 
        new DropTarget(panel, new FileDropTarget(panel, this));
    	
        final String calltoaction = "Drop a file here to open it...";
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
        System.out.println("Given pixel size of " + pixelSize + ", font size = " + fontSize);
        
		return (int) fontSize;
	}

    private JComponent createCsvTable(File file) {
        return createCsvTableV2(file);
    }
    
	private JComponent createCsvTableV1(File file) {
        final JScrollPane pane = (tblCsvData instanceof BetterJTable) 
                ? BetterJTable.createStripedJScrollPane(tblCsvData) 
                : new JScrollPane(tblCsvData);
        if (tblCsvData instanceof BetterJTable) {
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
		tblCsvData.setModel(tableModel);		
        // Any modifications to the ‘people’ list is automatically reflected in the csvTable
		
		boolean fillpanel = false;
		if (fillpanel) {
		    // do nothing; autoresize is on by default
		    // This is generally undesirable as most delimited files will be wider
		    // than the default size of the panel and overflow/scrolling is desirable.
		} else {
	        tblCsvData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		final NumberCellRenderer cellRenderer = new NumberCellRenderer("Consolas", "dummy"); 
		// order of preference: Consolas , Lucida Console , Bitstream Vera Sans Mono 
		// Courier New , Lucida Sans Typewriter
		// following are interesting (* = not monospaced):
		// OCR A Extended , HP Simplified* , LuzSans-Book* , Rockwell* , Roboto*
		// Sitka Text* , X-Files*
		for (int i=0; i<tblCsvData.getColumnCount(); i++) {
			final String cname = tblCsvData.getColumnName(i);
			tblCsvData.getColumn(cname).setCellRenderer(cellRenderer);
		}

		tblCsvData.getTableHeader().setFont(cellRenderer.getFont());
		//csvTable.getTableHeader().setDefaultRenderer(cellRenderer);
		//csvTable.setColumnSelectionAllowed(true);

		if (tblCsvData instanceof JTable) {
	        this.resizeColumnWidth(tblCsvData);
		    
		} else if (tblCsvData instanceof JXTable) {
		    final JXTable t = (JXTable) tblCsvData;
	        t.packAll();
	        t.setFillsViewportHeight(false);
	        t.setColumnControlVisible(true);
		    
		}

		return pane;
	}

    private JComponent createCsvTableV2(File file) {
        
        final JScrollPane pane = (tblCsvData instanceof BetterJTable)
                ? BetterJTable.createStripedJScrollPane(tblCsvData)
                : new JScrollPane(tblCsvData);       

        pane.setBorder(BorderFactory.createCompoundBorder(
                BORDER_EMPTY,
                BorderFactory.createLineBorder(COLOR_GREY_MED) ) // BORDER_LINE
                ); // (BORDER_EMPTY); //( BORDER_COMPOUND );
        
        if (tblCsvData instanceof BetterJTable) {
            // do nothing (yet?)
        } else {
        }

        tblCsvData.setModel(csvTableModel = new DelimitedFileTableModel(file, this.textChooser.getText()));
        tblCsvData.setShowVerticalLines(false);
        tblCsvData.setFont(FONT_CALIBRI); // (fontSmallData);
        tblCsvData.setRowMargin(1); tblCsvData.getColumnModel().setColumnMargin(0);
        tblCsvData.setFillsViewportHeight(true);           

        boolean solarized = false;
        if (solarized) {           
            boolean lightScheme = false;
           
            tblCsvData.setForeground(lightScheme ? new Color(0x657b83) : new Color(0x839496)); // (COLOR_GREY_MED); // 0x073642 is solarized base02
            tblCsvData.setBackground(lightScheme ? new Color(0xfdf6e3) : new Color(0x002b36));
           
            tblCsvData.setSelectionForeground(lightScheme ? new Color(0x839496) : new Color(0x657b83));
            tblCsvData.setSelectionBackground(lightScheme ? new Color(0x002b36) : new Color(0xfdf6e3));
           
        } else {
            tblCsvData.setForeground(COLOR_GREY_DARKEST);
            // tblCsvData.setBackground(lightScheme ? new Color(0xfdf6e3) : new Color(0x002b36));
            
            tblCsvData.setSelectionForeground( new Color(0x3A87AD) );
            tblCsvData.setSelectionBackground( new Color(0xD9EDF7) );
        }

        csvTableColumnManager = new TableColumnManager(tblCsvData);
        
        boolean fillpanel = false;
        if (fillpanel) {
            // do nothing; autoresize is on by default
            // This is generally undesirable as most delimited files will be wider
            // than the default size of the panel and overflow/scrolling is desirable.
        } else {
            tblCsvData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        
        // order of preference: Consolas , Lucida Console , Bitstream Vera Sans Mono 
        // Courier New , Lucida Sans Typewriter
        // following are interesting (* = not monospaced):
        // OCR A Extended , HP Simplified* , LuzSans-Book* , Rockwell* , Roboto*
        // Sitka Text* , X-Files*
        final List<DataHint> hints = ((DataHintAware) csvTableModel).getDataHints();
        for (int i=0; i<tblCsvData.getColumnCount(); i++) {
            final String cname = tblCsvData.getColumnName(i);
        	if (hints.get(i).equals(DataHint.NUMERIC)) {
	            tblCsvData.getColumn(cname).setCellRenderer(numRenderer);
        	} else {
	            tblCsvData.getColumn(cname).setCellRenderer(strRenderer);        		
        	}
        }

        boolean customizeHeader = true;
        if (customizeHeader) {
            final NumberCellRenderer hdrRenderer = new NumberCellRenderer("Consolas", "dummy");
            hdrRenderer.setForeground(new Color(249,249,249));
            hdrRenderer.setBackground(COLOR_GREY_DARKEST);        
            // hdrRenderer.setFont(FONT_SEGOE_UI);
            // hdrRenderer.setBorder(BORDER_ETCHED); // DefaultTableCellRenderer does not honor a user Border :(
            
            hdrRenderer.setCellBorder(BORDER_MATTE); // This works but I don't like the look
            
            hdrRenderer.setHorizontalAlignment(CENTER);
            hdrRenderer.setVerticalAlignment(BOTTOM);
            
            tblCsvData.getTableHeader().setDefaultRenderer(hdrRenderer);
            //tblCsvData.getTableHeader().setBorder(BORDER_ETCHED);
        	//csvTable.setBorder(BORDER_ETCHED);
        } else {
            tblCsvData.getTableHeader().setFont(FONT_CALIBRI_BOLD);
            tblCsvData.getTableHeader().setForeground(COLOR_GREY_DARKEST);
            tblCsvData.getTableHeader().setBackground(new Color(249,249,249));
        }

        if (tblCsvData instanceof JTable) {
            this.resizeColumnWidth(tblCsvData);            
        } else if (tblCsvData instanceof JXTable) {
            final JXTable t = (JXTable) tblCsvData;
            t.packAll();
        }

        return pane;
    }
    
    private JPanel createEasternPanel() {
        return createEasternPanel_new();
    }
    
    private JPanel createEasternPanel_new() {
        
        JPanel actions = new JPanel();
        actions.setLayout(new BorderLayout());
        actions.setBorder( BORDER_EMPTY );

        Font font = null;
        try {
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/fontawesome/Font Awesome 5 Free-Solid-900.otf");
            font = Font.createFont(Font.TRUETYPE_FONT, stream);
            font = font.deriveFont(28f);
            // font = font.deriveFont(Font.BOLD, 40);
            stream.close();
        } catch (FontFormatException | IOException e1) {
            e1.printStackTrace();
        }
        
        // TODO move this to global initialization
        IconFontSwing.register(FontAwesome.getIconFont());
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
        
        JPanel center = new JPanel(new GridLayout(0,1));
        center.setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 1, 2, 3)
                ,BORDER_LINE
                )
            ); // fixes a small glitch caused by HorizontalGraphitePanel
        
        int designoption = 2;
        switch (designoption) {
        case 1:
            JButton b = HorizontalGraphitePanel.decorateButton(new JButton("BIG BUTTON"), null, null);
            //JToggleButton jtbButton = new JToggleButton("ToggleButton Press Me");
            center.add(HorizontalGraphitePanel.createDefault(Arrays.asList(b)));
            break;
        case 2:
            boxpnlColumns = new JPanel();
            boxpnlColumns.setLayout(new BoxLayout(boxpnlColumns, BoxLayout.Y_AXIS));
            
            JScrollPane pane = new JScrollPane(boxpnlColumns);
            pane.setBorder(null); // This may only be necessary when using Nimbus L&F?
            center.add(pane);
            break;
        }
        
        ///// ===== NORTH ===== /////
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS)); // (new GridLayout(4,1));
        north.setBorder(BorderFactory.createEmptyBorder(1, 1, 0, 3)); // fixes a small glitch caused by HorizontalGraphitePanel
        
        JLabel lblFilename = (JLabel) XLabel.create().setFont(fontSmallLabel).setText("FILENAME:").get(); 
        txtFilename = (JLabel) XLabel.create().setFont(fontSmallData).setText("............").get(); 
//            north.add(fileinfoPanel(lblFilename,txtFilename));
        JLabel lblFilesize = (JLabel) XLabel.create().setFont(fontSmallLabel).setText("FILESIZE:").get(); 
        txtFilesize = (JLabel) XLabel.create().setFont(fontSmallData).setText("......").get(); 
//            north.add(fileinfoPanel(lblFilesize,txtFilesize));
        JLabel lblLinecount = (JLabel) XLabel.create().setFont(fontSmallLabel).setText("LINES:").get();
        txtLinecount = (JLabel) XLabel.create().setFont(fontSmallData).setText("......").get();
//            north.add(fileinfoPanel(lblLinecount,txtLinecount));

        JLabel titlebar = new JLabel("DATA COLUMNS");
//            titlebar.setOpaque(true);
              titlebar.setForeground(new Color(0xcdcdcd));
              titlebar.setFont(FONT_VERDANA);
//            titlebar.setBackground(Color.black);
            titlebar.setHorizontalAlignment(SwingConstants.CENTER);
            
        HorizontalGraphitePanel titlepanel = new HorizontalGraphitePanel();  
            titlepanel.setLayout(new BorderLayout());
            titlepanel.add(titlebar, BorderLayout.CENTER); //(Utilities.allowMaxWidth(titlebar, north));            
            north.add(titlepanel);
            
        JButton packButton = HorizontalGraphitePanel.createButton("PACK", null, null);
        JButton unpackButton = HorizontalGraphitePanel.createButton("UNPACK", null, null);
        final HorizontalGraphitePanel graphitepanel = HorizontalGraphitePanel
                .createDefault(Arrays.asList(packButton, unpackButton));
//        north.add(graphitepanel);       

        ///// ===== SOUTH ===== /////
        JPanel south = new JPanel(); // 1,1,2,2
        south.setBorder( BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 1, 1, 3) // fixes a small glitch caused by HorizontalGraphitePanel
                ,BORDER_LINE
                )
            ); 
        // (BorderFactory.createEmptyBorder(0, 1, 0, 1)); // fixes a small glitch caused by HorizontalGraphitePanel
        
        LayoutManager southlayout = new BoxLayout(south, BoxLayout.Y_AXIS); // new GridLayout(0,1)
        south.setLayout(southlayout);

        // Use this reference to control where the following components are placed
        JPanel panelptr = south;
        
        this.delimiterChooser = (DelimiterChooser) (this.textChooser = new DelimiterChooser());
        //this.delimiterChooser.getUI().setBorder(BORDER_DEBUG);
        panelptr.add(this.delimiterChooser.getUI());
                
        JToggleButton b = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton(), null, null);
        b.setAction(new JTablePropertyAction("RESIZE MODE",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_AUTORESIZEMODE, null));
        panelptr.add(HorizontalGraphitePanel.createDefault(Arrays.asList(b)));

        JToggleButton c = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton(), null, null);
        c.setAction(new JTablePropertyAction("SELECTION MODE",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_COLUMNSELECTION, null));
        //panelptr.add(HorizontalGraphitePanel.createDefault(Arrays.asList(c)));

        JButton a = (JButton) HorizontalGraphitePanel.decorateButton(new JButton(), null, null);
        a.setAction(new JTablePropertyAction("CLEAR",  tblCsvData, JTablePropertyAction.ACTION_CLEAR_SELECTION, null));
        panelptr.add(HorizontalGraphitePanel.createDefault(Arrays.asList(c,a)));

        JToggleButton d = (JToggleButton) HorizontalGraphitePanel.createToggleButton(null, null, null);
        //d.setFont(font); 
        //d.setText("\uf0ab\uf039\uf038\uf13d"); d.setForeground(Color.white);
        d.setAction(new JTablePropertyAction("GRID",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_GRID, null));
        d.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BORDER_ALL, 14, COLOR_EAST_TEXT));
        // GRID_ON / OFF
        JToggleButton e = HorizontalGraphitePanel.createToggleButton(null, null, null);
        e.setAction(new JTablePropertyAction("HORZ",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_HORIZONTAL_LINES, null));
        e.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BORDER_BOTTOM, 14, COLOR_EAST_TEXT));
        e.getModel().setSelected(true);
        JToggleButton f = HorizontalGraphitePanel.createToggleButton(null, null, null);
        f.setAction(new JTablePropertyAction("VERT",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_VERTICAL_LINES, null));
        f.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BORDER_VERTICAL, 14, COLOR_EAST_TEXT));
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(d, e, f)));

        final Dimension dimicon = new Dimension(30,1);
        JButton g = HorizontalGraphitePanel.createButton(null, null, dimicon);
        g.setAction(new JTablePropertyAction("CMARGIN+",  tblCsvData, JTablePropertyAction.ACTION_INCREASE_COLUMN_MARGIN, null));
        g.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_INDENT_INCREASE, 14, COLOR_EAST_TEXT));
        g.setText("");
        
        JButton h = HorizontalGraphitePanel.createButton(null, null, dimicon);
        h.setAction(new JTablePropertyAction("CMARGIN-",  tblCsvData, JTablePropertyAction.ACTION_DECREASE_COLUMN_MARGIN, null));
        h.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_INDENT_DECREASE, 14, COLOR_EAST_TEXT));
        h.setText("");
        
        JButton i = HorizontalGraphitePanel.createButton(null, null, dimicon);
        i.setAction(new JTablePropertyAction(" +",  tblCsvData, JTablePropertyAction.ACTION_INCREASE_ROW_MARGIN, null));
        i.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_LINE_SPACING, 14, COLOR_EAST_TEXT));
        
        JButton j = HorizontalGraphitePanel.createButton(null, null, dimicon);
        j.setAction(new JTablePropertyAction(" -",  tblCsvData, JTablePropertyAction.ACTION_DECREASE_ROW_MARGIN, null));
        j.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_LINE_SPACING, 14, COLOR_EAST_TEXT));

        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(h, g, j, i)));        
                
        this.btnGlassPane = HorizontalGraphitePanel.decorateButton(new JButton("GLASS PANE"), null, null);
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(this.btnGlassPane)));

        actions.add(north, BorderLayout.NORTH);
        actions.add(south, BorderLayout.SOUTH);
        actions.add(center, BorderLayout.CENTER);

        return actions;        
    }
    
    private JPanel fileinfoPanel(JComponent label, JComponent text) {
        final JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(label);
        p.add(Box.createHorizontalGlue());
        p.add(text);
        
        boolean debug = false;
        if (debug) {
            p.setBorder(BORDER_DEBUG);
            label.setBorder(BORDER_DEBUG);
        }
        
        return p;
    }
    
	private JPanel createEasternPanel_works() {
		
		Font font = null;
		try {
			InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("com/fontawesome/Font Awesome 5 Free-Solid-900.otf");
			font = Font.createFont(Font.TRUETYPE_FONT, stream);
			font = font.deriveFont(28f);
			// font = font.deriveFont(Font.BOLD, 40);
			stream.close();
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
		
		// TODO move this to global initialization
		IconFontSwing.register(FontAwesome.getIconFont());
		IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());
		
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
            boxpnlColumns = new JPanel();
            boxpnlColumns.setLayout(new BoxLayout(boxpnlColumns, BoxLayout.Y_AXIS));
            
            // Actually creating these now
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Name",  csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Age",   csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Month", csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Day",   csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());
//            boxlayout.add(XCheckBox.create().setFont(FONT_SEGOE_UI).setAction(new JTablePropertyAction("Year",  csvTable, JTablePropertyAction.ACTION_CLEAR_SELECTION, null)).get());

            JScrollPane pane = new JScrollPane(boxpnlColumns);
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

		JPanel south = new JPanel(); // 1,1,2,2
		south.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1)); // fixes a small glitch caused by HorizontalGraphitePanel
		LayoutManager southlayout = new BoxLayout(south, BoxLayout.Y_AXIS); // new GridLayout(0,1)
		south.setLayout(southlayout);

		//JPanel delimiter = this.createDelimiter();
		//south.add(delimiter);
		this.delimiterChooser = (DelimiterChooser) (this.textChooser = new DelimiterChooser());
		south.add(this.delimiterChooser.getUI());
				
        JToggleButton b = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton(), null, null);
        b.setAction(new JTablePropertyAction("RESIZE MODE",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_AUTORESIZEMODE, null));
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(b)));

		JButton a = (JButton) HorizontalGraphitePanel.decorateButton(new JButton(), null, null);
		a.setAction(new JTablePropertyAction("CLEAR SELECTION",  tblCsvData, JTablePropertyAction.ACTION_CLEAR_SELECTION, null));
		south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(a)));

        JToggleButton c = (JToggleButton) HorizontalGraphitePanel.decorateButton(new JToggleButton(), null, null);
        c.setAction(new JTablePropertyAction("COLUMN SELECTION",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_COLUMNSELECTION, null));
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(c)));

        JToggleButton d = (JToggleButton) HorizontalGraphitePanel.createToggleButton(null, null, null);
        //d.setFont(font); 
        //d.setText("\uf0ab\uf039\uf038\uf13d"); d.setForeground(Color.white);
        d.setAction(new JTablePropertyAction("GRID",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_GRID, null));
        d.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BORDER_ALL, 18, COLOR_EAST_TEXT));
        // GRID_ON / OFF
        JToggleButton e = HorizontalGraphitePanel.createToggleButton(null, null, null);
        e.setAction(new JTablePropertyAction("HORZ",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_HORIZONTAL_LINES, null));
        e.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BORDER_BOTTOM, 18, COLOR_EAST_TEXT));
        e.getModel().setSelected(true);
        JToggleButton f = HorizontalGraphitePanel.createToggleButton(null, null, null);
        f.setAction(new JTablePropertyAction("VERT",  tblCsvData, JTablePropertyAction.ACTION_TOGGLE_VERTICAL_LINES, null));
        f.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.BORDER_VERTICAL, 18, COLOR_EAST_TEXT));
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(d, e, f)));

        final Dimension dimicon = new Dimension(30,1);
        JButton g = HorizontalGraphitePanel.createButton(null, null, dimicon);
        g.setAction(new JTablePropertyAction("CMARGIN+",  tblCsvData, JTablePropertyAction.ACTION_INCREASE_COLUMN_MARGIN, null));
        g.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_INDENT_INCREASE, 18, COLOR_EAST_TEXT));
        g.setText("");
        
        JButton h = HorizontalGraphitePanel.createButton(null, null, dimicon);
        h.setAction(new JTablePropertyAction("CMARGIN-",  tblCsvData, JTablePropertyAction.ACTION_DECREASE_COLUMN_MARGIN, null));
        h.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_INDENT_DECREASE, 18, COLOR_EAST_TEXT));
        h.setText("");
        
        JButton i = HorizontalGraphitePanel.createButton(null, null, dimicon);
        i.setAction(new JTablePropertyAction(" +",  tblCsvData, JTablePropertyAction.ACTION_INCREASE_ROW_MARGIN, null));
        i.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_LINE_SPACING, 18, COLOR_EAST_TEXT));
        
        JButton j = HorizontalGraphitePanel.createButton(null, null, dimicon);
        j.setAction(new JTablePropertyAction(" -",  tblCsvData, JTablePropertyAction.ACTION_DECREASE_ROW_MARGIN, null));
        j.setIcon(IconFontSwing.buildIcon(GoogleMaterialDesignIcons.FORMAT_LINE_SPACING, 18, COLOR_EAST_TEXT));

        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(h, g, j, i)));        
                
        this.btnGlassPane = HorizontalGraphitePanel.decorateButton(new JButton("GLASS PANE"), null, null);
        south.add(HorizontalGraphitePanel.createDefault(Arrays.asList(this.btnGlassPane)));

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

    /**
     * Thanks to:
     * https://stackoverflow.com/questions/17627431/auto-resizing-the-jtable-column-widths
     * 
     * @param tblCsvData
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
            width = Limit.rangeOf(width).toRange(50,300); 
            // if(width > 300) width=300;
            // if(width < 50) width=50;
            
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    @Override
    public void doSingleFileAction(File file) {
        
        // Remove the existing center component
        final BorderLayout layout = (BorderLayout) this.getLayout();
        this.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        
        // Create/Add the JTable
        this.add(this.createCsvTable(file), BorderLayout.CENTER);        
        
        // Put the checkboxes in our user interface
        for (JCheckBox checkbox : csvTableColumnManager.getListOfJCheckBox()) {
            // We want them to span the entire width of the container for usability
            Utilities.allowMaxWidth(checkbox);
//        	Dimension d = checkbox.getPreferredSize(); d.width = Short.MAX_VALUE;
//        	checkbox.setMaximumSize(d);
        	
            boxpnlColumns.add(checkbox);
        }

        if (tblCsvData instanceof JTable) {
            //
        } else if (tblCsvData instanceof JXTable) {
            final JXTable t = (JXTable) tblCsvData;
            t.setColumnControlVisible(false);
        }

        // Set file properties on UI components
        this.txtFilename.setText(file.getName());
        this.txtFilesize.setText("" + file.length() / 1024 + " KB");
        this.txtLinecount.setText("" + this.csvTableModel.getRowCount());
        this.statusFilename.setText(" " + file.getAbsolutePath());

        this.dataBrowserAware.updateFilename(file.getName());
        
        // this.updateRowHeights_fast();

        this.validate();
    }

    @Override
    public void doListOfFilesAction(List<File> listoffiles) {
        throw new RuntimeException("Method not implemented");
    }

    private void updateRowHeights() {
    	boolean fastest = true;
    	boolean enable = false;
    	if (enable) {
    		if (fastest) 
    			updateRowHeights_fast();
    		else
    			updateRowHeights_slower();
    	}
    }
    
    private void updateRowHeights_slower() {
        for (int row = 0; row < tblCsvData.getRowCount(); row++) {
        	
            int rowHeight = tblCsvData.getRowHeight(); 
            System.out.print("row height before: " + rowHeight);

            for (int column = 0; column < tblCsvData.getColumnCount(); column++) {
                Component comp = tblCsvData.prepareRenderer(tblCsvData.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            System.out.println(", after: " + rowHeight);
            tblCsvData.setRowHeight(row, rowHeight);
        }
    }
    
    private void updateRowHeights_fast() {
        for (int row = 0; row < tblCsvData.getRowCount(); row++)
        {
            int rowHeight = tblCsvData.getRowHeight(); 
            if (!printedCellSize)
                System.out.print("row height before: " + rowHeight);

            //assume first column is sufficient to adjust the entire row
            for (int column = 0; column < 1; column++) {
                Component comp = tblCsvData.prepareRenderer(tblCsvData.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            if (!printedCellSize)
                System.out.println(", after: " + rowHeight);
            
            tblCsvData.setRowHeight(row, rowHeight);

            printedCellSize = true;
        }
    }
    
	public JButton getGlassPaneButton() {
		return this.btnGlassPane;
	}

}
