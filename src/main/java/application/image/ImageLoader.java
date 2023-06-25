package application.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader
{
    public static BufferedImage load(String path)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(new File(path));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    public static void save(String path, BufferedImage image)
    {
        try
        {
            ImageIO.write(resize(image, 100, 100), "png", new File(path));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static BufferedImage resize(BufferedImage img, int newW, int newH)
    {
        Image tmp = img.getScaledInstance(newW, newH, BufferedImage.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}