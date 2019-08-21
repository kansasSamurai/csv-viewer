package org.jwellman.csvviewer;

public interface DataBrowserAware {
    
	/**
	 * Callback from DataBrowser to DocumentManager to let DM know
	 * that a file has been opened (or the focus has changed)
	 * 
	 * DM will update any UI elements with the filename given;
	 * typically the status bar - maybe others.
	 * 
	 * @param filename
	 */
    public void updateFilename(String filename);
    
    /**
     * 
     * @return
     */
    public String getDelimiter();

}
