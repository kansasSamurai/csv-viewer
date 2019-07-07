package org.jwellman.foarchive.beans;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Rick
 */
public interface PropertyChangeBroadcaster {

    public void addPropertyChangeListener(PropertyChangeListener l);
    public void removePropertyChangeListener(PropertyChangeListener l);

}
