package org.jwellman.csvviewer;

import jiconfont.icons.FontAwesome;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.swing.IconFontSwing;

import org.jwellman.foundation.Foundation;
import org.jwellman.foundation.uContext;
import org.jwellman.foundation.extend.AbstractSimpleMain;
import org.jwellman.foundation.interfaces.uiThemeProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
public class CsvViewerApplication extends AbstractSimpleMain implements uiThemeProvider {

	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(CsvViewerApplication.class);
		app.setWebEnvironment(false);

		new CsvViewerApplication().startup(false, args);
	}

	/**
	 * This is the "simple"/simplest version of startup which must always work.
	 * Particularly in desktop mode, the initial display may not be packed and "nice"
	 * but that just means it is using defaults and does not mean that there is a logic error.
	 * This was tested successfully on 12/1/2020 using Foundation snapshot of the same date.
	 * 
	 * @param useDesktop
	 * @param args
	 * @return
	 */
    public CsvViewerApplication startup_simple(boolean useDesktop, String[] args) {

        // Global Initialization(s) [ Frameworks, etc. ]
        IconFontSwing.register(FontAwesome.getIconFont());
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());

        // Prepare - User Interface Context

        // Step 1 - Initialize Swing
        final Foundation f = Foundation.init(null); // context
        f.showGUI( useDesktop 
                ? f.useDesktop(new DocumentManager()) 
                : f.useWindow(new DocumentManager()));

        return this;
    }

    /**
     * As of 12/1/2020, this version works with Foundation API of the same date.
     * 
     * @param useDesktop
     * @param args
     * @return
     */
    public CsvViewerApplication startup(boolean useDesktop, String[] args) {

        // Global Initialization(s) [ Frameworks, etc. ]
        IconFontSwing.register(FontAwesome.getIconFont());
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont());

        // Prepare - User Interface Context
        final uContext context = uContext.createContext();
        context.setThemeProvider(this);
        context.setDimension(160); //(85);
        context.setDesktopMode(useDesktop);

        // Step 1 - Initialize Swing
        final Foundation f = Foundation.init(context); // context

        // Step 2 - Create your UIs in JPanel(s)
        mainui = f.registerUI("viewer", new DocumentManager()); // new DataBrowser());

        // Step 3 - Use Foundation to create your "window"; give it your UI.
        window = useDesktop ? f.useDesktop(mainui) : f.useWindow(mainui);
        // Step 3a (optional) - Customize your window
        window.setTitle("Data Viewer");
        window.setResizable(true);
        window.setMaximizable(true);

//      final ComponentGlassPane gp = new ComponentGlassPane((JFrame)this.window);      
//      final DataBrowser b = (DataBrowser)this.mainui.getChild();
//      b.getGlassPaneButton().addActionListener(gp);

        // Step 4a - Create data models, controllers, and other non-UI objects
        // n/a
        
        // Step 4b (optional)- Associate models with views
        // n/a

        // Step 5 - Display your User Interface
        if (useDesktop) {} else f.registerDesktopWindow(window); 
        f.showGUI(window);

        return this;
    }

	@Override
	public void doTheme() {
		// TODO Auto-generated method stub
		
	}
	
}
