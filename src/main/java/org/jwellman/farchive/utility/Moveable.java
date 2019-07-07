package org.jwellman.farchive.utility;

import java.awt.Image;

/**
 *
 * @author Rick
 */
public interface Moveable {

    public int getX();

    public int getY();

    public int getWidth();

    public int getHeight();

    public Image getImage();

    public void setX(int x);

    public void setY(int y);

    /**
     * Indicates whether the piece should be allowed to move (via the mouse).
     * i.e. A game such as chess might display captured pieces but it would
     * be assumed that they cannot be moved after capture.
     *
     * @return true, if the piece can be moved. Otherwise, return false.
     */
    public boolean isMovable();

}
