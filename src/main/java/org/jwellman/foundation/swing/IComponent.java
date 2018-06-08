package org.jwellman.foundation.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

public interface IComponent {

    public JComponent get();
    
    public XComponent addTo(ButtonGroup bg);
    
    public XComponent setFont(Font f);
    
    public XComponent setText(String string);
    
    public XComponent setAction(AbstractAction action);
    
    public XComponent setForeground(Color color);
    
    public XComponent setBackground(Color color);
    
}
