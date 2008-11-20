/*
 * @(#)JeedGui.java
 * Time-stamp: "2008-11-20 18:43:48 anton"
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JeedGui extends JFrame {
    public JeedGui() {
        super("JeedReader");
        //Menu
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        menubar.add(file);
        menubar.add(edit);

        JMenuItem fileQuit = new JMenuItem("Quit");
        fileQuit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println(e.paramString());
                }
            });
        file.add(fileQuit);
        
        
        // Final panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(menubar, BorderLayout.NORTH);
        
        // Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(panel);
        this.pack();
        this.setVisible(true);
    }
}
