package application.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class InputPanel extends JPanel implements GUIComponent
{
    private final int width;
    private final int height;

    private ImageCanvas imageCanvas;
    private JButton loadImageButton;

    private BufferedImage image;

    public InputPanel(int width, int height)
    {
        this.width = width;
        this.height = height;

        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());

        createAndShowGUI();
    }

    public ImageCanvas getImageCanvas()
    {
        return imageCanvas;
    }

    public void createAndShowGUI()
    {
        imageCanvas = new ImageCanvas(width, height);
        loadImageButton = new JButton("Load Image");

        this.add(imageCanvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(loadImageButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }
}