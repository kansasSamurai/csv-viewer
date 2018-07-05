package org.jwellman.foundation;

/**
 * A micro-framework for Swing applications.
 *
 * Usage:
 * (1) init() - initializes the Swing framework
 * (2) useWindow()/useDesktop() - supply your user interface within a JPanel
 *     and instantiate the supporting Swing containers.
 * (3) showGUI() - make your user interface/JPanel visible;
 *     most applications will have initialized all data models
 *     and this will usually be the last method called in your startup() code.
 *
 * @author Rick Wellman
 */
public class Foundation extends Platinum {

    private Foundation() {} // private constructor

    private static Foundation f; // singleton

    /**
     * Use static method to get singleton
     * 
     * @deprecated Use static init() instead
     * @return
     */
    public static Foundation get() {
        if (f == null) f = new Foundation();
        return f;
    }

}
