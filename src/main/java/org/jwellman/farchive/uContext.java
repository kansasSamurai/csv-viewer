package org.jwellman.farchive;

import java.awt.Dimension;
import org.jwellman.foundation.interfaces.uiCustomTheme;

/**
 *
 * @author Rick
 */
public class uContext {

    public static uContext createContext() {
        return new uContext();
    }

    private uContext() {
        // Empty/private constructor to enforce factory pattern
    }

    /** An object that implements the theme-ing interface */
    private uiCustomTheme theme;

    public void setTheme(uiCustomTheme x) { theme = x; }

    public uiCustomTheme getTheme() { return theme; }


    /** A dimension object for the window (w/ default value) */
    private Dimension dimension = new Dimension(450,250);

    public void setDimension(Dimension x) { dimension = x; }

    public void setDimension(int w, int h) { dimension = new Dimension(w,h); }

    public void setDimension(int base) { this.setDimension(base, 9, 5); }
    
    public void setDimension(int base, int w, int h) { this.setDimension(base * w, base * h); }

    public Dimension getDimension() { return dimension; }

}
