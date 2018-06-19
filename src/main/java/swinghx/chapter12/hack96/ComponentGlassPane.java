package swinghx.chapter12.hack96;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A GlassPane that visually indicates the bounds of Components within the JFrame.
 * Note that it was decided to not indicate JPanels with text (outline only).
 * TODO maybe create a boolean property that controls JPanel text visibility
 * 
 * @author rwellman
 *
 */
public class ComponentGlassPane extends JComponent implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Point cursor;

    private int paintCount;

    private final JFrame frame;

    private final Color TEXT_BACKGROUND = new Color(1f, 1f, 1f, 0.9f);

    private final Color BACKGROUND_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.1f);

    public static void main(String[] args) {
        demo();
    }

    /**
     * The original Swing Hacks implementation.
     */
    private static void demo() {

        final JFrame frame = new JFrame("Component Boundary Glasspane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container root = frame.getContentPane();
        root.setLayout(new BoxLayout(root,BoxLayout.Y_AXIS));

        final JButton activate = new JButton("Show component boundaries");
        root.add(activate);
        root.add(new JLabel("Juice Settings"));

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.add(new JLabel("Flavor"));
        panel.add(new JTextField("put some text here..."));
        root.add(panel);

        final ComponentGlassPane glass = new ComponentGlassPane(frame);
        // frame.setGlassPane(glass); // moved to ComponentGlassPane constructor

        activate.addActionListener(glass);
		// Moved to ComponentGlassPane::actionPerformed()
		//                new ActionListener() {
		//            @Override public void actionPerformed(ActionEvent evt) {
		//                glass.setVisible(true);
		//                glass.paintCount = 0;
		//            }
		//        });

        frame.pack();
        frame.show();
    }

    /**
     * Create a new ComponentGlassPane to apply to the given JFrame.
     *
     * @param frame
     */
    public ComponentGlassPane(JFrame frame) {
        this.frame = frame;
        frame.setGlassPane(this);

        cursor = new Point();

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent evt) {
                cursor = new Point(evt.getPoint());
                ComponentGlassPane.this.repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent evt) {
                ComponentGlassPane.this.setVisible(false);
            }
        });
    }

    /**
     * Swing component painter
     *
     * @param g
     */
    @Override public void paint(Graphics g) {
        System.out.print("*"); paintCount++;

        //Container root = frame.getContentPane();
        rPaint(frame.getContentPane(), g, 1);

        System.out.println(" : " + paintCount);
    }

    /**
     * Paint algorithm that recursively navigates the component hierarchy
     *
     * @param comp
     * @param g
     * @param rlevel
     */
    private void rPaint(Component comp, Graphics g, final int rlevel) {

        final boolean isContainer = comp instanceof Container; // so ... everything is a Container in Swing
        final Container cont = isContainer ? (Container)comp : null;
        final boolean hasChildren = isContainer ? (cont.getComponentCount() > 0) : false;

        System.out.print(".");

        int x = comp.getX();
        int y = comp.getY();
        g.translate(x,y);
        cursor.translate(-x,-y);

        int w = comp.getWidth();
        int h = comp.getHeight();

        // draw background
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0,0,w,h);

        g.setColor(Color.red);
        g.drawRect(0,0,w,h);

        // if the mouse is over this component
        if (comp.contains(cursor)) { // && !hasChildren) {

            // draw the text
            final String cls_name = comp.getClass().getName() + " : " + rlevel;
            // Graphics2D g2 = (Graphics2D)g; // TODO IS THIS NECESSARY/BENEFICIAL?
            // Font fnt = g.getFont();
            FontMetrics fm = g.getFontMetrics();
            int text_width = fm.stringWidth(cls_name);
            int text_height = fm.getHeight();
            int text_ascent = fm.getAscent();

            // draw text background
            g.setColor(TEXT_BACKGROUND);
            g.fillRect(0,0,text_width,text_height);
            g.setColor(Color.white);
            g.drawRect(0,0,text_width,text_height);

            // draw text
            g.setColor(Color.black);
            g.drawString(cls_name, 0, 0+text_ascent);
        }

        if (isContainer) {
            for (Component child : cont.getComponents()) {
                rPaint(child, g, rlevel + 1);
            }
//            for (int i=0; i<cont.getComponentCount(); i++) {
//                final Component child = cont.getComponent(i);
//                rPaint(child,g, rlevel + 1);
//            }
        }

        cursor.translate(x,y);
        g.translate(-x,-y);
    }

    /**
     * An ActionListener for buttons to activate this GlassPane
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(true);
        this.paintCount = 0;
    }

}
