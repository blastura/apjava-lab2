/*
 * @(#)JeedView.java
 * Time-stamp: "2008-11-29 00:03:26 anton"
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
import java.util.Vector;
import javax.swing.JSplitPane;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.ListSelectionModel;

public class JeedView extends JFrame {
    private static final int PAD_SIZE = 2;
    private static final Color BKGR_COLOR = Color.WHITE;
    
    private JeedModel jeedModel;
    private Border paddingBorder;
    private JList feedList;
    private JList itemList;
    private JEditorPane itemViewer;
    private JButton addFeedButton = new JButton("Add feed");
    
    public JeedView(JeedModel jeedModel) {
        super("JeedReader");
        this.jeedModel = jeedModel;
        //FeedBorder borders TODO
        this.paddingBorder
            = BorderFactory.createLineBorder(BKGR_COLOR, PAD_SIZE);
        
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

        this.feedList = new JList();
        // TODO, if there are no feed?
        feedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.feedList.setBorder(paddingBorder);
        
        // FeedPanel
        JPanel feedPanel = new JPanel(new BorderLayout());
        feedPanel.add(this.feedList, BorderLayout.CENTER);
        feedPanel.add(addFeedButton, BorderLayout.SOUTH);

        // ItemList TODO
        this.itemList = new JList();
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setBorder(paddingBorder);
        
        // ItemViewer
        this.itemViewer = new JEditorPane("text/html", "<h1>Nyheter</h1>");
        
        //SplitPanels
        JSplitPane rightVerticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                                   itemList,
                                                   itemViewer);
        
        JSplitPane mainHorisontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                                        feedPanel,
                                                        rightVerticalSplit);

        // Final panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(menubar, BorderLayout.NORTH);
        panel.add(mainHorisontalSplit, BorderLayout.CENTER);
        
        // Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().add(panel);
    }

    public void refreshFeeds() {
        this.feedList.setListData(this.jeedModel.getFeeds());
    }

    public String promtForFeedLink() {
        String result =
            JOptionPane.showInputDialog(this, "Enter the URL to a feed: ");
        return result;
    }
    
    public void setAddFeedListener(ActionListener addFeedListener) {
        this.addFeedButton.addActionListener(addFeedListener);
    }

    public void setFeedListListener(ListSelectionListener feedListListener) {
        this.feedList.addListSelectionListener(feedListListener);
    }

    public void setItemListListener(ListSelectionListener listSelectionListener) {
        this.itemList.addListSelectionListener(listSelectionListener);
    }
    
    public void showItemsForCurrentFeedSelection() {
        Feed selectedFeed = (Feed) this.feedList.getSelectedValue();
        System.out.println(selectedFeed);
        FeedItem[] items = this.jeedModel.getItemsForFeed(selectedFeed);
        this.itemList.setListData(items);
    }

    public void showDescForCurrentItemSelection() {
        FeedItem selectedFeedItem = (FeedItem) this.itemList.getSelectedValue();
        System.out.println(selectedFeedItem);
        String description
            = this.jeedModel.getDescribtionFromFeedItem(selectedFeedItem);
        this.itemViewer.setText(description);
    }
    
    public void showView() {
        // Populate FeedList
        Vector<Feed> feeds = jeedModel.getFeeds();
        this.feedList.setListData(feeds);
        this.feedList.setSelectedIndex(0);
        System.out.println("Feeds in gui: " + feeds);
        
        // Populate ItemList
        showItemsForCurrentFeedSelection();
        this.pack();
        this.setVisible(true);

    }
}
