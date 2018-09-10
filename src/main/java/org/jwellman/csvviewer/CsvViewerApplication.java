package org.jwellman.csvviewer;

import javax.swing.JFrame;

import org.jwellman.foundation.Foundation;
import org.jwellman.foundation.uContext;
import org.jwellman.foundation.extend.SimpleMain;
import org.jwellman.foundation.interfaces.uiCustomTheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;

import swinghx.chapter12.hack96.ComponentGlassPane;

@SpringBootApplication
@EnableAutoConfiguration(exclude = WebMvcAutoConfiguration.class)
public class CsvViewerApplication extends SimpleMain implements uiCustomTheme {

	public static void main(String[] args) {
		
		SpringApplication app = new SpringApplication(CsvViewerApplication.class);
		app.setWebEnvironment(false);
		// app.run(args);
		
		new CsvViewerApplication().startup(true, args);
	}

	private CsvViewerApplication startup(boolean asMainFrame, String[] args) {

        // Prepare - User Interface Context
        final uContext context = uContext.createContext();
        context.setTheme(this);
        context.setDimension(85);

        // Step 1 - Initialize Swing
        final Foundation f = Foundation.get();
        f.init(context); // context

        // Step 2 - Create your UIs in JPanel(s)
        mainui = f.registerUI("viewer", new DocumentManager()); // new DataBrowser());

        // Step 3 - Use Foundation to create your "window"; give it your UI.
        window = f.useWindow(mainui);
        // Step 3a (optional) - Customize your window
        window.setTitle("CSV Viewer"); 
        window.setResizable(true);

//		final ComponentGlassPane gp = new ComponentGlassPane((JFrame)this.window);		
//		final DataBrowser b = (DataBrowser)this.mainui.getChild();
//		b.getGlassPaneButton().addActionListener(gp);

        // Step 4a - Create data models, controllers, and other non-UI objects
        // n/a
        
        // Step 4b (optional)- Associate models with views
        // n/a

        // Step 5 - Display your User Interface
        f.showGUI();

        return this;
    }

	@Override
	public void doCustomTheme() {
		
	}
}
