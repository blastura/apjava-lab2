/*
 * @(#)JeedView.java
 * Time-stamp: "2008-12-01 12:58:39 anton"
 */

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.DefaultListModel;
import java.awt.Dimension;

public class JeedView extends JFrame implements Observer {    
    private static Logger logger = Logger.getLogger("jeedreader");
    private Dimension frameDimension = new Dimension(800, 600);
    private static final int PAD_SIZE = 5;
    private static final Color BKGR_COLOR = Color.WHITE;

    private JeedModel jeedModel;
    private Border paddingBorder;
    
    private JList feedList;
    private DefaultListModel feedListModel;
    
    private JList itemList;
    private DefaultListModel itemListModel;
    
    private JEditorPane itemView;
    
    // Buttons
    private JButton addFeedButton = new JButton("Add");
    private JButton updateFeedsButton = new JButton("Refresh");
    
    // MenuItems
    private JMenuItem updateIntervallMenuItem;
    private JMenuItem quitMenuItem;
    
    private JMenuItem updateFeedMenuItem;
    private JMenuItem updateFeedsMenuItem;
    private JMenuItem addFeedMenuItem;
    
    public JeedView(JeedModel jeedModel) {
        super("JeedReader");
        this.jeedModel = jeedModel;
        //FeedBorder borders TODO
        this.paddingBorder
            = BorderFactory.createLineBorder(BKGR_COLOR, PAD_SIZE);

        //Menu
        JMenuBar menubar = new JMenuBar();
        
        // Menu JeadReader
        JMenu fileMenu = new JMenu("JeedReader");
        this.updateIntervallMenuItem = new JMenuItem("Settings");
        fileMenu.add(updateIntervallMenuItem);
        this.quitMenuItem = new JMenuItem("Quit");
        fileMenu.add(quitMenuItem);
        
        // Menu FeedMenu        
        JMenu feedMenu = new JMenu("Feeds");
        
        this.updateFeedMenuItem = new JMenuItem("Update selected feed");
        feedMenu.add(this.updateFeedMenuItem);
        
        this.updateFeedsMenuItem = new JMenuItem("Update all feeds");
        feedMenu.add(this.updateFeedsMenuItem);
        
        this.addFeedMenuItem = new JMenuItem("Add feed");
        feedMenu.add(this.addFeedMenuItem);
        
        // Add menus to menubar
        menubar.add(fileMenu);
        menubar.add(feedMenu);

        // FeedList
        this.feedListModel = new DefaultListModel();
        this.feedList = new JList(feedListModel);
        this.feedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // FeedButtonPanel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addFeedButton);
        buttonPanel.add(updateFeedsButton);
        
        // FeedPanel
        JPanel feedPanel = new JPanel(new BorderLayout());
        feedPanel.add(this.feedList, BorderLayout.CENTER);
        feedPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ItemList
        this.itemListModel = new DefaultListModel();
        this.itemList = new JList(itemListModel);
        this.itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //TODO itemList.setDragEnabled(true);
        JScrollPane itemsScrollPane =
            new JScrollPane(itemList,
                            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // ItemViewer
        this.itemView = new JEditorPane("text/html", "<h1>Nyheter</h1>");
        this.itemView.setEditable(false);
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

    public Feed getSelectedFeed() {
        return (Feed) this.feedList.getSelectedValue();
    }

    //     public void refreshFeeds() {
    //         SwingUtilities.invokeLater(new Runnable() {
    //                 public void run() {
    //                     logger.info(Thread.currentThread().toString());
    //                     feedList.setListData(jeedModel.getFeeds());
    //                 }
    //             });
    //     }
    
    /* Start promt methods ******************************/
    
    public String promtForUpdateInterval(String errorMsg) {
        String message = "Enter interval for feed updates in milliseconds: ";
        String result =
            JOptionPane.showInputDialog(this, errorMsg + message);
        return result;
    }
    
    public String promtForFeedUrlString(String errorMsg) {
        String result =
            JOptionPane.showInputDialog(this, errorMsg + "Enter the URL to a feed: ");
        return result;
    }
    
    /* End promt methods ******************************/
    
    /* Start Setup Listeners methods ******************************/
    
    public void setProgramCloseListeners(ActionListener al) {
        this.quitMenuItem.addActionListener(al);
    }
    
    public void setUpdateIntervalListener(ActionListener al) {
        this.updateIntervallMenuItem.addActionListener(al);
    }

    public void setUpdateFeedsListener(ActionListener al) {
        this.updateFeedsButton.addActionListener(al);
        this.updateFeedsMenuItem.addActionListener(al);
    }

    public void setUpdateFeedListener(ActionListener al) {
        this.updateFeedMenuItem.addActionListener(al);
    }
    
    public void setAddFeedListener(ActionListener al) {
        this.addFeedButton.addActionListener(al);
        this.addFeedMenuItem.addActionListener(al);
    }

    public void setFeedListListener(ListSelectionListener feedListListener) {
        this.feedList.addListSelectionListener(feedListListener);
    }

    public void setItemListListener(ListSelectionListener listSelectionListener) {
        this.itemList.addListSelectionListener(listSelectionListener);
    }
    
    /* End Setup Listeners methods ******************************/

    
    public void showErrorMessage(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "alert", JOptionPane.ERROR_MESSAGE);
    }

    public void showItemsForCurrentFeedSelection() {
        if (feedList.isSelectionEmpty()) {
            return;
        }
        Feed selectedFeed = getSelectedFeed();
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
        // Should this behaviour be in the Controller?
        selectedFeedItem.setIsRead(true);
        String description
            = this.jeedModel.getDescribtionFromFeedItem(selectedFeedItem);
        this.itemView.setText(description);
    }

    public void initView() {
        // Populate FeedList
        Vector<Feed> feeds = jeedModel.getFeeds();
        for (Feed feed : feeds) {
            this.feedListModel.addElement(feed);
        }
        
        // TODO what if there is no selection?
        // this.feedList.setSelectedIndex(0);
        System.out.println("Feeds in gui: " + feeds);

        // Populate ItemList
        showItemsForCurrentFeedSelection();
        this.setPreferredSize(this.frameDimension);
        this.pack();
        this.setVisible(true);
    }

    public void update(Observable obj, Object arg) {
        // TODO
        if (arg instanceof Feed) {
            logger.info("New feed detected");
            this.feedListModel.addElement(arg);
        } else if (arg.equals("newItems")) {
            logger.info("New items detected");
            showItemsForCurrentFeedSelection();
            // TODO Assumtion about generic type, unchecked cast.
        }
    }
}
