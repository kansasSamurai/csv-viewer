package org.jwellman.swing.jtabbedpane;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jwellman.swing.icon.CloseIcon;

/**
 * A component that adds the feature to close a tab of a JTabbedPane.
 * 
 * @author rwellman
 *
 */
public class CloseTabPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedpane;

    private JLabel jlabel;

    private static final Cursor HAND = new Cursor(Cursor.HAND_CURSOR);
    
    // TODO make this injectable which removes dependency on CloseIcon class
    private Icon closeIcon = new CloseIcon();
    
    public CloseTabPanel(JTabbedPane pane, int index) {
        this.setOpaque(false);
        this.tabbedpane = pane;        

        this.add(jlabel = new JLabel(pane.getTitleAt(index), pane.getIconAt(index), JLabel.LEFT));
        
        JButton btClose = new JButton(closeIcon);
        btClose.setFocusable(false); btClose.setBorderPainted(false); btClose.setContentAreaFilled(false); btClose.setCursor(HAND);
        btClose.setPreferredSize(new Dimension(closeIcon.getIconWidth(), closeIcon.getIconHeight()));
        btClose.addActionListener(this);
        this.add(btClose);
        
        this.tabbedpane.setTabComponentAt(index, this);
    }

    public void actionPerformed(ActionEvent e) {
        if (tabbedpane.getTabCount() < 2) {
            JOptionPane.showMessageDialog(
                    null, 
                    "You Cannot Close The Last Tab",
                    "Not Allowed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            
            int i = tabbedpane.indexOfTabComponent(this);
            if (i != -1) {
                tabbedpane.remove(i);
            }
            
        }
    }
    
    public void setTitle(String title) {
        this.jlabel.setText(title);        
    }
    
}
