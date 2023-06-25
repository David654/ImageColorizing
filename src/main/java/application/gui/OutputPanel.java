package application.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class OutputPanel extends JPanel implements GUIComponent
{
    private final int width;
    private final int height;

    private JTabbedPane tabbedPane;
    private Tab redChannelTab;
    private Tab greenChannelTab;
    private Tab blueChannelTab;
    private Tab totalChannelTab;

    private BufferedImage image;

    public OutputPanel(int width, int height)
    {
        this.width = width;
        this.height = height;

        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());

        createAndShowGUI();
    }

    public Tab getRedChannelTab()
    {
        return redChannelTab;
    }

    public Tab getGreenChannelTab()
    {
        return greenChannelTab;
    }

    public Tab getBlueChannelTab()
    {
        return blueChannelTab;
    }

    public Tab getTotalChannelTab()
    {
        return totalChannelTab;
    }

    public void createAndShowGUI()
    {
        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("JTabbedPane.scrollButtonsPlacement", "both");
        UIManager.put("TabbedPane.showTabSeparators", true);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        redChannelTab = new Tab("Red");
        greenChannelTab = new Tab("Green");
        blueChannelTab = new Tab("Blue");
        totalChannelTab = new Tab("Total");

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    public class Tab extends JPanel implements GUIComponent
    {
        private ImageCanvas imageCanvas;

        public Tab(String title)
        {
            this.setLayout(new BorderLayout());

            createAndShowGUI();

            tabbedPane.add(this, title);
        }

        public ImageCanvas getImageCanvas()
        {
            return imageCanvas;
        }

        public void createAndShowGUI()
        {
            imageCanvas = new ImageCanvas(width, height);
            this.add(imageCanvas, BorderLayout.CENTER);
        }
    }
}