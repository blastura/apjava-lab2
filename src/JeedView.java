/*
 * @(#)JeedView.java
 * Time-stamp: "2008-11-27 23:43:36 anton"
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.util.List;

public class JeedView extends JFrame {
    private JeedModel jeedModel;
    
    public JeedView(JeedModel jeedModel) {
        super("JeedReader");
        this.jeedModel = jeedModel;
        
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

        // FeedList
        JList feedList = new JList(jeedModel.getFeeds());
        
        JList itemList = new JList(jeedModel.getFeeds());
        
        // Final panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(menubar, BorderLayout.NORTH);
        
        // Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(panel);
        this.pack();
    }

    public void createFeedList(RssFeed[] feeds) {
        // FeedList
        JList feedList = new JList(feeds);
        this.add(feedList);
    }
}
