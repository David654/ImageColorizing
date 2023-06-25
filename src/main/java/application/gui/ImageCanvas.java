package application.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageCanvas extends JPanel
{
    private int width;
    private int height;

    private BufferedImage image;

    public ImageCanvas(int width, int height)
    {
        this.width = width;
        this.height = height;

        this.setPreferredSize(new Dimension(width, height));

        Timer timer = new Timer(1, e -> repaint());
        timer.start();
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(image != null)
        {
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            double imgAspect = (double) imgHeight / imgWidth;

            int canvasWidth = width;
            int canvasHeight = height;

            double canvasAspect = (double) canvasHeight / canvasWidth;

            int x1 = 0; // top left X position
            int y1 = 0; // top left Y position
            int x2; // bottom right X position
            int y2; // bottom right Y position

            if(imgWidth < canvasWidth && imgHeight < canvasHeight)
            {
                // the image is smaller than the canvas
                x1 = (canvasWidth - imgWidth)  / 2;
                y1 = (canvasHeight - imgHeight) / 2;
                x2 = imgWidth + x1;
                y2 = imgHeight + y1;
            }
            else
            {
                if(canvasAspect > imgAspect)
                {
                    y1 = canvasHeight;
                    // keep image aspect ratio
                    canvasHeight = (int) (canvasWidth * imgAspect);
                    y1 = (y1 - canvasHeight) / 2;
                }
                else
                {
                    x1 = canvasWidth;
                    // keep image aspect ratio
                    canvasWidth = (int) (canvasHeight / imgAspect);
                    x1 = (x1 - canvasWidth) / 2;
                }
                x2 = canvasWidth + x1;
                y2 = canvasHeight + y1;
            }

            g2d.drawImage(image, x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, null);
        }
    }
}