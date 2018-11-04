package org.jwellman.foundation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jwellman.foundation.swing.IWindow;
import org.jwellman.foundation.swing.XFrame;
import org.jwellman.foundation.swing.XInternalFrame;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;

/**
 * The most basic of Swing initialization requirements.
 *
 * @author Rick Wellman
 */
public class Stone {

/** The user's entry point UI in a JPanel */
protected JPanel panel;

/** The JDesktopPane used with Desktop apps */
private JDesktopPane desktop;

/** A user interface context object */
private uContext context;

/** Guards the init() method */
protected boolean isInitialized;

/** Indicates desktop mode */
protected boolean isDesktop;

/** The "controlling" JFrame */
protected XFrame frame;

/** The internal frame used in desktop mode */
protected XInternalFrame internalFrame;





public Foundation init() {
    return init(null);
}

@SuppressWarnings("unused")
public Foundation init(uContext c) {

    if (!isInitialized) {
        isInitialized = true;

        // Log the application classpath for debugging purposes
        System.out.println("----- Application Classpath -----");
        final ClassLoader cl = ClassLoader.getSystemClassLoader();
        final URL[] urls = ((URLClassLoader)cl).getURLs();
        for (URL url: urls){
            System.out.println(url.getFile());
        }

        // Apply anti-aliasing for better rendering (particularly fonts)
        //
        // The following may have some subtle system dependent behavior:
        // http://stackoverflow.com/questions/179955/how-do-you-enable-anti-aliasing-in-arbitrary-java-apps
        // Try System.setProperty("awt.useSystemAAFontSettings", "lcd"); and you should get ClearType
		// https://www.javalobby.org/java/forums/t98492.html
        // Then, there is also this post:
        // https://www.javalobby.org/java/forums/t14179.html by Romain Guy (explodingpixels)
        // ... and also this post (also see Note 1 below): 
        // http://wiki.netbeans.org/FaqFontRendering
        
// also reference: https://docs.oracle.com/javase/7/docs/api/java/awt/doc-files/DesktopProperties.html 
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Map map = (Map)(tk.getDesktopProperty("awt.font.desktophints"));
//        if (map != null) {
//            graphics2D.addRenderingHints(map);
//        }
        
//        System.setProperty("awt.useSystemAAFontSettings","gasp");
        
        // This may be deprecated: https://bugs.openjdk.java.net/browse/JDK-6391267
//        System.setProperty("swing.aatext", "true");

        /*
         *  NetBeans uses the Swing text renderer. 
         *  Since JDK 1.6 this renderer supports sub-pixel rendering in addition to standard anti-aliasing. 
         *  The renderer supports several operating modes. According to

            http://docs.oracle.com/javase/6/docs/technotes/guides/2d/flags.html#aaFonts
            
            If the antialiasing switch awt.useSystemAAFontSettings is not set, 
            then Swing text renderer tries to detect the optimum setting for given system and use that. 
            Since 1.6 the renderer implements the following options:
            
            off | false | default - meaning "do not override what has been auto-detected"
            on - use anti-aliasing without sub-pixel rendering
            gasp - use anti-aliasing wit sub-pixel rendering, intended for use both on CRT and LCD
            lcd - use anti-aliasing wit sub-pixel rendering, optimized for LCD
            lcd_hbgr - same as lcd, but with different distribution of sub pixels (monitor upside down)
            lcd_vrgb - same as lcd, but with different distribution of sub pixels (monitor is vertical)
            lcd_vbgr - same as lcd, but with different distribution of sub pixels (vertical again but on other side)
         */
        
        // true := Make sure our window decorations come from the look and feel.
        JFrame.setDefaultLookAndFeelDecorated(false); // false := I changed my mind... I think the OS frame makes more sense
        // TODO ultimately, need to default to whatever I want but provide override mechanism (uContext)

        // Conditionally apply context settings...
        context = (c != null) ? c : uContext.createContext();
        if (context.getTheme() != null) { context.getTheme().doCustomTheme(); }

        // Prefer Nimbus over default look and feel.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                final String name = info.getName(); System.out.println(name);
                if ("Metal".equals(name)) { // Metal, Nimbus, CDE/Motif, Windows , Windows Classic                    

                	final int MATCHES_SETTING = 1;
                	final int WEB_LAF = 2;
                	final int TBD = 3;
                	final int SYSTEM_LAF = 4;
                	final int NIMROD_LAF = 5;
                	
                    final int version = MATCHES_SETTING; // WEB_LAF; //MATCHES_SETTING;
                    switch (version) {
                        case 1:
                            UIManager.setLookAndFeel(info.getClassName());
                            
                            // http://robertour.com/2016/04/25/quickly-improving-java-metal-look-feel/
                            // https://thebadprogrammer.com/swing-uimanager-keys/
                            if ("aaa".equals(name)) { // Metal, Nimbus, CDE/Motif, Windows , Windows Classic
                                setUIFont( new javax.swing.plaf.FontUIResource("Segoe UI", Font.PLAIN, 14) );
                                UIManager.put("Button.background",  Color.decode("#eeeeee"));
                                UIManager.put("ToggleButton.background",  Color.decode("#eeeeee"));
                                // UIManager.put("Button.border", new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(2, 2, 2, 2)));
                                // UIManager.put("ToggleButton.border", new CompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(2, 2, 2, 2)));
                            }
                            
                            break;
                        case 2:
                            UIManager.setLookAndFeel("com.alee.laf.WebLookAndFeel"); // works but need to upgrade to 1.29 from 1.27
                            break;
                        case 3:
//                            String[] themeNames = NapkinTheme.Manager.themeNames();
//                            String themeToUse = "blueprint"; // napkin | blueprint
//                            NapkinTheme.Manager.setCurrentTheme(themeToUse);
//                            LookAndFeel laf = new NapkinLookAndFeel();
//                            UIManager.setLookAndFeel(laf);
                            break;
                        case 4:
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            break;
                        case 5:
                        	NimRODTheme nt = null;
                        	NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
                        	
                        	int theme = 3;
                        	switch (theme) {
                        	case 1: // this has been testedand works
                            	UIManager.setLookAndFeel( new com.nilo.plaf.nimrod.NimRODLookAndFeel());
                                break;
                        	case 2: // this has been tested and works
                        		nt = new NimRODTheme();
                        		// syracuse
                          		nt.setPrimary1( new Color(0xEB1F00) ); // scroll thumb border
                        		nt.setPrimary2( new Color(0xF52900) ); // jtable.selection, scroll thumb, checkbox bgnd, text focus(highlighter)
                        		nt.setPrimary3( new Color(0xFF3300) );
                        		// firehat
//                        		nt.setPrimary1( new Color(0xB80000) ); // scroll thumb border
//                        		nt.setPrimary2( new Color(0xC20000) ); // jtable.selection, scroll thumb, checkbox bgnd, text focus(highlighter)
//                        		nt.setPrimary3( new Color(0xCC0000) );
                        		nt.setSecondary1( new Color(0x1F1F1F) );
                        		nt.setSecondary2( new Color(0x292929) );
                        		nt.setSecondary3( new Color(0x333333) );
                        		nt.setBlack( new Color(0xCCCCCC) ); // text components (button, jtable, etc.)
                        		nt.setWhite( new Color(0x666666) ); // text bgnd
                        		nt.setFont(new Font("Consolas",Font.PLAIN,16));
                        		
                        		NimRODLookAndFeel.setCurrentTheme(nt);
                        		UIManager.setLookAndFeel(NimRODLF);
                        		break;
                        	case 3:                   
                        	    String[] themes = {
                          	             "themes/nimrod/NimRODThemeFile_rix_mint_segoeui.theme"     // 0
                                        ,"themes/nimrod/NimRODThemeFile_rix_royale_calibri.theme"   // 1
                                        ,"themes/nimrod/NimRODThemeFile_ocean_light_segoe.theme"    // 2
                                        ,"themes/nimrod/NimRODThemeFile_executive_calibri.theme"    // 3
                                        ,"themes/nimrod/NimRODThemeFile_greenonwhite_segoe.theme"   // 4
                        	    };
                        		nt = new NimRODTheme(themes[3]);
                        		NimRODLookAndFeel.setCurrentTheme(nt);
                        		UIManager.setLookAndFeel(NimRODLF);                        		
                        		break;
                        	default:
                            	UIManager.setLookAndFeel( new com.nilo.plaf.nimrod.NimRODLookAndFeel());
                        			
                        	}
                            break;
                        default:
                        	break;
                    }

                    break;
                }
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

    }
    
    return (Foundation) this;
} // end method

/**
 * http://robertour.com/2016/04/25/quickly-improving-java-metal-look-feel/
 * 
 * @param f
 */
private static void setUIFont (javax.swing.plaf.FontUIResource f){
    final Enumeration<?> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get (key);
        if (value != null && value instanceof javax.swing.plaf.FontUIResource) UIManager.put (key, f);
    }
}

/**
 * Not using this but I wanted to capture it in case it is ever useful.
 * https://stackoverflow.com/questions/179955/how-do-you-enable-anti-aliasing-in-arbitrary-java-apps
 */
@SuppressWarnings("unused")
private static void olderAntiAliasHint() {
//    if (desktopHints == null) { 
//        Toolkit tk = Toolkit.getDefaultToolkit(); 
//        desktopHints = (Map) (tk.getDesktopProperty("awt.font.desktophints")); 
//    }
//    if (desktopHints != null) { 
//        g2d.addRenderingHints(desktopHints); 
//    } 
}

public IWindow useDesktop(JPanel ui) {

    isDesktop = true; // store the mode

    if (internalFrame == null) {
        panel = ui; // Store a reference to the JPanel

        desktop = new JDesktopPane(); // a specialized layered pane
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE); // Make dragging a little faster but perhaps uglier.

        // Create the JFrame
        frame = new XFrame("Foundation Desktop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(context.getDimension());
        frame.setContentPane(desktop);

        showGUI(); // in desktop mode, this should only show the jframe

        internalFrame = new XInternalFrame("Your UI", true, true, true, true);
        internalFrame.setBounds(10, 10, 225, 125);
        // internalFrame.setVisible(true); // [B]
        internalFrame.add(ui);
        internalFrame.setMaximizable(false);
        internalFrame.setClosable(false);
        desktop.add(internalFrame);

        this.initializeOtherWindows();

    }

    return internalFrame; // frame;
} // end method

public IWindow useWindow(JPanel ui) {

    // Store a reference to the JPanel
    panel = ui;

    // Create the JFrame
    frame = new XFrame("Your UI in Foundation Window");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setSize(450, 250);
    if (panel != null) frame.add(panel);

    return frame;
}

public void showGUI() {

    if (internalFrame != null) {
        // Start the GUI on the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(
            new Runnable() { @Override public void run() {
                    internalFrame.pack();
                    internalFrame.setVisible(true);
                }
            });
    } else if (frame != null) {
        // Start the GUI on the Event Dispatch Thread (EDT)
        javax.swing.SwingUtilities.invokeLater(
            new Runnable() { @Override public void run() {

                // Display the window.
                if (!isDesktop) frame.pack(); // [A]
                frame.setSize(context.getDimension());
                frame.setLocationRelativeTo(null); // [C]
                frame.setVisible(true);

            } }
        );
    } else {
        throw new RuntimeException("You have not chosen a window type: useWindow() or useDesktop() ");
    }

    // Dump all properties now that system and app have been fully initialized.
    System.out.println("======= System Initialized : JVM Property Listing =======");
    Properties systemProperties = System.getProperties();
    
    List<String> keys = new ArrayList (systemProperties.keySet());
    Collections.sort(keys);
    for (String key : keys) {
        System.out.println(key + ": " + systemProperties.getProperty(key));        
    }
    
    Toolkit tk = Toolkit.getDefaultToolkit();
    Map map = (Map)(tk.getDesktopProperty("awt.font.desktophints"));
    if (map != null) {
        System.out.println("------- Desktop Property : awt.font.desktophints -------");
        keys = new ArrayList (map.keySet());
        System.out.println(map);
// For some reason, the keys are not Strings :( but the println prints the map nicely
//        for (String key : keys) {
//            System.out.println(key + ": " + map.get(key));        
//        }
    }

} // end method

public JDesktopPane getDesktop() {
    return desktop;
}

/* ========== Footnotes =====================================================
[A] The swing documentation says that pack() makes the frame "displayable"
    I originally thought that meant "visible" but it doesn't (or at least
    it doesn't work that way).  A:  "displayable" means that the component,
    or its root container, have a native-peer.
[B] We intentionally defer showing the internal frame until the user/designer
    calls showGUI() from his code.
[C] Centers the frame/window on the native desktop.
/* ========================================================================== */

    /**
     * This is basically a noop in Stone since it only supports a single
     * application window.  Other levels will definitely override this.
     */
    protected void initializeOtherWindows() {}

} // end class
