/*
 * @(#)JeedView.java
 * Time-stamp: "2008-12-03 12:30:41 anton"
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;


/**
 * JeedView is the GUI of the application. It directly extends JFrame and
 * implements Observer to listen for changes in JeedModel.
 *
 * @author Anton Johansson, dit06ajn@cs.umu.se
 * @version 1.0
 */
public class JeedView extends JFrame implements Observer {    
    private static Logger logger = Logger.getLogger("jeedreader");
    
    private Dimension frameDimension = new Dimension(800, 600);
    private static final int PAD_SIZE = 5;
    private static final Color BKGR_COLOR = Color.WHITE;
    private Border paddingBorder;

    // An instance of the applications JeedModel
    private JeedModel jeedModel;
    
    // Feed- Itemlist
    private JList feedList;
    private DefaultListModel feedListModel;
    private JList itemList;
    
    // A pane to display item content.
    private JEditorPane itemView;
    
    // Buttons
    private JButton addFeedButton = new JButton("Add");
    private JButton updateFeedsButton = new JButton("Refresh");
    
    // JeedReader MenuItems
    private JMenuItem updateIntervallMenuItem;
    private JMenuItem quitMenuItem;
    
    // Feeds MenuItems
    private JMenuItem updateFeedMenuItem;
    private JMenuItem updateFeedsMenuItem;
    private JMenuItem addFeedMenuItem;
    
    /**
     * Sets up every component of the GUI.
     *
     * @param jeedModel An instance of the applications JeedModel used to get
     * feeds and items.
     */
    public JeedView(JeedModel jeedModel) {
        super("JeedReader");
        this.jeedModel = jeedModel;
        
        // A border to get some padding around elements in the GUI.
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
        this.itemList = new JList();
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

    /**
     * A method used by JeedReader to get the currently selected feed.
     *
     * @return the selected Feed.
     */
    public Feed getSelectedFeed() {
        return (Feed) this.feedList.getSelectedValue();
    }

    /* Promt methods ******************************/
    // TODO - could be implemented in one more general method with one extra
    // parameter.
    
    /**
     * Displays an JOptionPane to promt the user for a new update interval.
     *
     * @param errorMsg An error message to be displayed if last call to this
     * method failed.
     * @return The String supplied by the user.
     */
    public String promtForUpdateInterval(String errorMsg) {
        String message = "Enter interval for feed updates in milliseconds: ";
        String result =
            JOptionPane.showInputDialog(this, errorMsg + message);
        return result;
    }
    
    /**
     * Displays an JOptionPane to promt the user for a new feed to add.
     *
     * @param errorMsg An error message to be displayed if last call to this
     * method failed.
     * @return The String supplied by the user.
     */
    public String promtForFeedUrlString(String errorMsg) {
        String result =
            JOptionPane.showInputDialog(this, errorMsg
                                        + "Enter the URL to a feed: ");
        return result;
    }
    
    
    /* Setup Listeners methods ******************************/
    
    /**
     * Attaches an ActionListener to quitMenuItem.
     *
     * @param al The ActionListener to attach.
     */
    public void setProgramCloseListeners(ActionListener al) {
        this.quitMenuItem.addActionListener(al);
    }
    
    /**
     * Attaches an ActionListener to updateIntervallMenuItem.
     *
     * @param al The ActionListener to attach.
     */
    public void setUpdateIntervalListener(ActionListener al) {
        this.updateIntervallMenuItem.addActionListener(al);
    }

    /**
     * Attaches an ActionListener to updateFeedsButton and updateFeedsMenuItem.
     *
     * @param al The ActionListener to attach.
     */
    public void setUpdateFeedsListener(ActionListener al) {
        this.updateFeedsButton.addActionListener(al);
        this.updateFeedsMenuItem.addActionListener(al);
    }

    /**
     * Attaches an ActionListener to updateFeedsMenuItem.
     *
     * @param al The ActionListener to attach.
     */
    public void setUpdateFeedListener(ActionListener al) {
        this.updateFeedMenuItem.addActionListener(al);
    }
    
    /**
     * Attaches an ActionListener to addFeedButton and addFeedMenuItem.
     *
     * @param al The ActionListener to attach.
     */
    public void setAddFeedListener(ActionListener al) {
        this.addFeedButton.addActionListener(al);
        this.addFeedMenuItem.addActionListener(al);
    }

    /**
     * Attaches an ListSelectionListener to feedList.
     *
     * @param feedListListener The ListSelectionListener to attach.
     */
    public void setFeedListListener(ListSelectionListener feedListListener) {
        this.feedList.addListSelectionListener(feedListListener);
    }

    /**
     * Attaches an ListSelectionListener to itemList.
     *
     * @param listSelectionListener The ListSelectionListener to attach.
     */
    public void setItemListListener(ListSelectionListener listSelectionListener) {
        this.itemList.addListSelectionListener(listSelectionListener);
    }
    
    
    /* Methods to display OptionPanes ******************************/
    
    /**
     * Displays an JOptionPane to to display an error message if something has  
     * gone wrong and the user needs to know about it.
     *
     * @param errorMsg An error message to be displayed.
     */
    public void showErrorMessage(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "alert",
                                      JOptionPane.ERROR_MESSAGE);
    }
    
    
    /* Methods to dispay Feeds and FeedItems ******************************/
    
    /**
     * Shows all items for the currently selected feed in the Jlist itemList.
     *
     */
    public void showItemsForCurrentFeedSelection() {
        logger.info("Adding items, thread is: "
                    + Thread.currentThread().toString());
        if (feedList.isSelectionEmpty()) {
            logger.warning("No feed selected");
            return;
        }
        
        Feed selectedFeed = getSelectedFeed();
        System.out.println(selectedFeed);
        FeedItem[] items = this.jeedModel.getItemsForFeed(selectedFeed);

        this.itemList.setListData(items);
    }

    /**
     * Shows the html description from the currently selected item. The
     * description is displayed in the JEditorPane itemView.
     *
     */
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
            = selectedFeedItem.getHtmlDescription();
        this.itemView.setText(description);
    }
    
    /* Init and update methods **************************************/    
    
    /**
     * Fill the Feed JList, the FeedItem JList, and make this JFrame visible.
     */
    public void initView() {
        // Populate FeedList
        Vector<Feed> feeds = jeedModel.getFeeds();
        for (Feed feed : feeds) {
            this.feedListModel.addElement(feed);
        }
        
        // TODO what if there is no selection?
        // this.feedList.setSelectedIndex(0);
        System.out.println("Feeds in GUI: " + feeds);

        // Populate ItemList
        showItemsForCurrentFeedSelection();
        
        // Prepare and show JFrame
        this.setPreferredSize(this.frameDimension);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Is run when JeedModel is changed. Update this Feed- and Item-lists.
     *
     * @param obj A reference to the Observable invoking this call.
     * @param arg An object indicating what is changed, this will either be a
     * Feed or a String "newItems".
     */
    public void update(final Observable obj, final Object arg) {
        // TODO detect and display new items better.
        logger.info("Update(...) JeedView Thread is: "
                    + Thread.currentThread().toString());
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (arg instanceof Feed) {
                        logger.info("New feed detected");
                        feedListModel.addElement(arg);
                        // TODO replace "newItems"" with constant, or change
                        // behavior to use a feed instead.
                    } else if (arg.equals("newItems")) {
                        logger.info("New items detected");
                        showItemsForCurrentFeedSelection();
                    }
                }
            });
    }
}
