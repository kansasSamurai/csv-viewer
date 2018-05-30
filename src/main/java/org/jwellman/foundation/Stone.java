package org.jwellman.foundation;

import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
//import net.sourceforge.napkinlaf.NapkinLookAndFeel;
//import net.sourceforge.napkinlaf.NapkinTheme;
import org.jwellman.foundation.swing.IWindow;
import org.jwellman.foundation.swing.XFrame;
import org.jwellman.foundation.swing.XInternalFrame;

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





public void init() {
    init(null);
}

public void init(uContext c) {

    if (!isInitialized) {
        isInitialized = true;

        // Log the application classpath for debugging purposes
        System.out.println("----- Application Classpath -----");
        final ClassLoader cl = ClassLoader.getSystemClassLoader();
        final URL[] urls = ((URLClassLoader)cl).getURLs();
        for (URL url: urls){
            System.out.println(url.getFile());
        }

        // Apply anti-aliasing for better rendering (particulary fonts)
        // The following may have some subtle system dependent behavior:
        // http://stackoverflow.com/questions/179955/how-do-you-enable-anti-aliasing-in-arbitrary-java-apps
        // Try System.setProperty("awt.useSystemAAFontSettings", "lcd"); and you should get ClearType
// https://www.javalobby.org/java/forums/t98492.html
//        System.setProperty("awt.useSystemAAFontSettings","on");
//        System.setProperty("swing.aatext", "true");

        // Make sure our window decorations come from the look and feel.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Conditionally apply context settings...
        context = (c != null) ? c : uContext.createContext();
        if (context.getTheme() != null) { context.getTheme().doCustomTheme(); }

        // Prefer Nimbus over default look and feel.
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                final String name = info.getName(); System.out.println(name);
                if ("Nimbus".equals(name)) {
                    final int version = 1;
                    switch (version) {
                        case 1:
                            UIManager.setLookAndFeel(info.getClassName());
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
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            break;
                        default:
                        	break;
                    }

                    // break;
                }
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

    }
} // end method

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
    }
    else if (frame != null) {
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
    }
    else {
        throw new RuntimeException("You have not chosen a window type: useWindow() or useDesktop() ");
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
