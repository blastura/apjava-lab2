/*
 * @(#)JeedGui.java
 * Time-stamp: "2008-11-20 13:02:29 anton"
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class JeedGui extends JFrame {
    public JeedGui() {
        super("JeedReader");
        
        // Final panel
        JPanel panel = new JPanel(new BorderLayout());
        
        // Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(panel);
        this.pack();
        this.setVisible(true);
    }
}
