package org.jwellman.swing.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.List;

/**
 * A single resource to control and optimize use of Font(s) in an application.
 * 
 * @author rwellman
 *
 */
public class FontFactory {

	/**
	 * https://wesbos.com/programming-fonts/
	 * 
	 * https://docs.oracle.com/javase/tutorial/2d/text/fonts.html
	 * https://www.javalobby.org/java/forums/t98492.html
	 * https://docs.oracle.com/javase/7/docs/technotes/guides/intl/font.html
	 * https://wpollock.com/Java/Fonts.htm
	 * http://edn.embarcadero.com/article/29991
	 * https://superuser.com/questions/988379/how-do-i-run-java-apps-upscaled-on-a-high-dpi-display
	 * http://www.pushing-pixels.org/category/java
	 * 
	 * !! http://www.pushing-pixels.org/2018/05/23/hello-radiance.html
	 * !! http://www.pushing-pixels.org/2017/02/23/releases-2017-h1.html
	 * http://www.pushing-pixels.org/2018/05/18/the-art-and-craft-of-screen-graphics-interview-with-krista-lomax.html
	 * 
	 * 
	 * @param name
	 * @param style
	 * @param size
	 * @return
	 */
    public static Font getFont(String name, int style, int size) {
        System.out.println("Requesting font: " + name);
        
    	String fontname = "Dialog";
    	int fontstyle = Font.PLAIN;
    	int fontsize = 36;
    	
    	final GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String[] names = gEnv.getAvailableFontFamilyNames();    
        final List<String> namesList = Arrays.asList(names);
        System.out.println(namesList);
        
        if (namesList.contains(name)) {
        	fontname = name;
        	fontstyle = style;
        	fontsize = size;
        }
        System.out.println("Delivering font: " + fontname);
        
    	final Font font = new Font(fontname, fontstyle, fontsize);
        System.out.println("Verifying font: " + font.getName());
        
        return font;
    }

}
