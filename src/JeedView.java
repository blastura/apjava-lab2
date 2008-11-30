/*
 * @(#)JeedView.java
 * Time-stamp: "2008-11-30 02:04:37 anton"
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import java.util.Observable;
import java.awt.FlowLayout;

public class JeedView extends JFrame implements Observer {    
    private static Logger logger = Logger.getLogger("jeedreader");
    private static final int PAD_SIZE = 5;
    private static final Color BKGR_COLOR = Color.WHITE;

    private JeedModel jeedModel;
    private Border paddingBorder;
    private JList feedList;
    private JList itemList;
    private JEditorPane itemView;
    private JButton addFeedButton = new JButton("Add");
    private JButton updateFeedsButton = new JButton("Refresh");

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
        
        // FeedButtonPanel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addFeedButton);
        buttonPanel.add(updateFeedsButton);
        
        // FeedPanel
        JPanel feedPanel = new JPanel(new BorderLayout());
        feedPanel.add(this.feedList, BorderLayout.CENTER);
        feedPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ItemList
        this.itemList = new JList();
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //TODO itemList.setDragEnabled(true);
        JScrollPane itemsScrollPane =
            new JScrollPane(itemList,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // ItemViewer
        this.itemView = new JEditorPane("text/html", "<h1>Nyheter</h1>");
        this.itemView.setBorder(paddingBorder);
        JScrollPane itemViewScrollPane =
            new JScrollPane(itemView,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //SplitPanels
        JSplitPane rightVerticalSplit
            = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                             itemsScrollPane,
                             itemViewScrollPane);

        JSplitPane mainHorisontalSplit =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
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

    public String promtForFeedUrlString() {
        String result =
            JOptionPane.showInputDialog(this, "Enter the URL to a feed: ");
        return result;
    }

    public void setUpdateFeedListener(ActionListener al) {
        this.updateFeedsButton.addActionListener(al);
    }
    
    public void setAddFeedListener(ActionListener al) {
        this.addFeedButton.addActionListener(al);
    }

    public void setFeedListListener(ListSelectionListener feedListListener) {
        this.feedList.addListSelectionListener(feedListListener);
    }

    public void setItemListListener(ListSelectionListener listSelectionListener) {
        this.itemList.addListSelectionListener(listSelectionListener);
    }

    public void showItemsForCurrentFeedSelection() {
        if (feedList.isSelectionEmpty()) {
            return;
        }
        Feed selectedFeed = (Feed) this.feedList.getSelectedValue();
        System.out.println(selectedFeed);
        FeedItem[] items = this.jeedModel.getItemsForFeed(selectedFeed);
        this.itemList.setListData(items);
    }

    public void showDescForCurrentItemSelection() {
        if (itemList.isSelectionEmpty()) {
            return;
        }
        FeedItem selectedFeedItem = (FeedItem) this.itemList.getSelectedValue();
        logger.info("Selected Feed: " + selectedFeedItem);
        System.out.println(selectedFeedItem);
        String description
            = this.jeedModel.getDescribtionFromFeedItem(selectedFeedItem);
        this.itemView.setText(description);
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

    public void update(Observable obj, Object arg) {
        // TODO
        logger.info("Change detected");
        refreshFeeds();
    }
}
