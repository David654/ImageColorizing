package application.gui;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel implements GUIComponent
{
    private int width;
    private int height;

    private JButton loadImageButton;

    public ButtonPanel(int width, int height)
    {
        this.width = width;
        this.height = height;

        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        init();
        createAndShowGUI();
    }

    public void setWidth(int width)
    {
        this.width = width;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void setHeight(int height)
    {
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
    }

    private void init()
    {
        loadImageButton = new JButton("Load Image");
       // clear.addActionListener(e -> handler.clear());
    }

    public void createAndShowGUI()
    {
        this.add(loadImageButton);
    }
}