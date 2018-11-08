package org.jwellman.csvviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jwellman.foundation.swing.XButton;
import org.jwellman.swing.component.HorizontalGraphitePanel;
import org.jwellman.swing.jtabbedpane.CloseTabPanel;

/**
 * Manages creation, view, and deletion of document views (i.e. DataBrowsers)
 * 
 * @author rwellman
 *
 */
public class DocumentManager extends JPanel implements DataBrowserAware, ActionListener {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

	private DelimiterChooser delimiterChooser;
    
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

        final Dimension d = new Dimension(22, 1);       
        final AbstractButton s = HorizontalGraphitePanel.decorateButton((AbstractButton)XButton.create().setText("New").get(), null, d);
        s.addActionListener(this);

        this.delimiterChooser = new DelimiterChooser();       
        this.delimiterChooser.getUI().add( s );
       
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
	public void actionPerformed(ActionEvent e) {

		this.tabbedPane.addTab("CSV", new DataBrowser(this));
        
		final int idx = this.tabbedPane.getTabCount() - 1;
        new CloseTabPanel(tabbedPane, idx);        
        this.tabbedPane.setSelectedIndex(idx);
	}
    
}
