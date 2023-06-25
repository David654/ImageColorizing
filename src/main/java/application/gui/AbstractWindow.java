package application.gui;

import javax.swing.*;

public abstract class AbstractWindow
{
    protected final JFrame frame;

    public AbstractWindow(int width, int height, String title)
    {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    protected abstract void createAndShowGUI();
}