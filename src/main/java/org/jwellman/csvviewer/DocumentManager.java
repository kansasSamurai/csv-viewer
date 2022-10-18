package org.jwellman.csvviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jwellman.foundation.swing.XButton;
import org.jwellman.swing.component.HorizontalGraphitePanel;
import org.jwellman.swing.jtabbedpane.CloseTabPanel;
import org.jwellman.swing.uimgmt.PaletteManager;

/**
 * Manages creation, view, and deletion of document views (i.e. DataBrowsers)
 * 
 * @author rwellman
 *
 */
public class DocumentManager extends JPanel implements DataBrowserAware, ActionListener {

    private static final long serialVersionUID = 1L;
    
    private JFrame palette;

    private JTabbedPane tabbedPane;

	private DelimiterChooser delimiterChooser;
	
	private static final String CMD_NEW = "New";
	
	private static final String CMD_SETTINGS = "Settings";
    
    public DocumentManager() {

        super(new BorderLayout());

        this.createToolbar(BorderLayout.NORTH);

        this.createTabbedPane(BorderLayout.CENTER);       

    }

    private void createTabbedPane(String where) {

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("CSV", new DataBrowser(this));
        new CloseTabPanel(tabbedPane, 0);

        tabbedPane.addTab("CSV", new DataBrowser(this));
        new CloseTabPanel(tabbedPane, 1);
       
        this.add(tabbedPane, where);
   }

    private void createToolbar(String where) {
    	AbstractButton s = null; // reusable
    	
        this.delimiterChooser = new DelimiterChooser();
        
        this.delimiterChooser.getUI().add( Box.createHorizontalGlue() );
        
        final Dimension d = new Dimension(22, 1);       
        s = HorizontalGraphitePanel.decorateButton((AbstractButton)XButton.create().setText(CMD_NEW).get(), null, d);
        s.addActionListener(this);
        this.delimiterChooser.getUI().add( s );
       
        if ( ! Settings.global().isUserMode() ) {
            s = HorizontalGraphitePanel.decorateButton((AbstractButton)XButton.create().setText(CMD_SETTINGS).get(), null, d);
            s.addActionListener(this);
            this.delimiterChooser.getUI().add( s );
        }

        this.add(this.delimiterChooser.getUI(), where);
    }

    @Override
    public void updateFilename(String filename) {
        int idx = this.tabbedPane.getSelectedIndex();
        
        Object c = this.tabbedPane.getTabComponentAt(idx);
        if (c instanceof CloseTabPanel) {
            CloseTabPanel b = (CloseTabPanel)c;
            b.setTitle(filename);
        } else {
            this.tabbedPane.setTitleAt(idx, filename);            
        }
        
    }

	@Override
	public String getDelimiter() {
		return this.delimiterChooser.getText();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
		case CMD_NEW:
			this.tabbedPane.addTab("CSV", new DataBrowser(this)); {
				final int idx = this.tabbedPane.getTabCount() - 1;
				new CloseTabPanel(tabbedPane, idx);        
				this.tabbedPane.setSelectedIndex(idx);			
			}
			break;
		case CMD_SETTINGS:
			//JOptionPane.showMessageDialog(this, "Under Construction...");
			// framework.showWindow("");
			if (palette == null) {
				palette = new JFrame("Palette Manager");
				palette.setLayout(new BorderLayout());
				palette.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				palette.add(PaletteManager.get(), BorderLayout.CENTER);
				palette.pack();				
			}
			palette.setVisible(true);
			break;
		}

	}

}
