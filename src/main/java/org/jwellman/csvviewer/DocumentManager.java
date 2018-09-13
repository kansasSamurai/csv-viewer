package org.jwellman.csvviewer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jwellman.swing.jtabbedpane.CloseTabPanel;

/**
 * Manages creation, view, and deletion of document views (i.e. DataBrowsers)
 * 
 * @author rwellman
 *
 */
public class DocumentManager extends JPanel implements DataBrowserAware {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;
    
    public DocumentManager() {
        super(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("CSV1", new DataBrowser(this));
        new CloseTabPanel(tabbedPane, 0);
        
        tabbedPane.addTab("CSV2", new DataBrowser(this));
        new CloseTabPanel(tabbedPane, 1);
        
        this.add(tabbedPane, BorderLayout.CENTER);
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
    
}
