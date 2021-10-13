package org.jwellman.csvviewer;

/**
 * Application Settings
 * 
 * @author Rick Wellman
 *
 */
public class Settings {

    private static Settings singleton = new Settings();

    private boolean userMode = true;

    /**
     * Use this to return the singleton.
     * 
     * @return
     */
    public static Settings global() {
        return singleton;
    }
    
    private Settings() {
        // Private constructor to enforce singleton        
    }
    
    /**
     * userMode controls some subtle differences in behavior
     * (as opposed to its opposite: developerMode).
     * 
     * i.e. in userMode, the grid control buttons actually set the grid
     * margins (and the grid margin buttons are not shown).  Whereas in
     * developer mode, the grid control buttons simply toggle the grid lines
     * and the grid margin buttons allow you to fine tune the margins manually.
     * 
     * There may be other uses, just search for isUserMode().
     * 
     * @return the userMode
     */
    public boolean isUserMode() {
        return userMode;
    }

    /**
     * @param userMode the userMode to set
     */
    public void setUserMode(boolean userMode) {
        this.userMode = userMode;
    }

}
