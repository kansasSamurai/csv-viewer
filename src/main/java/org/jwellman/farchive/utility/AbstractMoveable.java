package org.jwellman.farchive.utility;

import java.awt.Image;

/**
 *
 * @author Rick
 */
public class AbstractMoveable implements Moveable {

    private int x;
    private int y;
    private final Image image;

    public AbstractMoveable(Image img, int x, int y) {
        this.image = img;
        this.x = x;
        this.y = y;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
            return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return image.getWidth(null);
    }

    @Override
    public int getHeight() {
        return image.getHeight(null);
    }

    @Override
    public boolean isMovable() {
        return true;
    }

}
