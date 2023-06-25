package application.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ComplexImage
{
    private BufferedImage image;

    public enum ChannelType
    {
        RED,
        GREEN,
        BLUE
    }

    public ComplexImage(String fileName)
    {
        loadImage(fileName);
    }

    public ComplexImage(BufferedImage redChannel, BufferedImage greenChannel, BufferedImage blueChannel)
    {
        image = new BufferedImage(redChannel.getWidth(), redChannel.getHeight(), redChannel.getType());

        for(int x = 0; x < redChannel.getWidth(); x++)
        {
            for(int y = 0; y < redChannel.getHeight(); y++)
            {
                int red = redChannel.getRGB(x, y) >> 16 & 0xff;
                int green = greenChannel.getRGB(x, y) >> 8 & 0xff;
                int blue = blueChannel.getRGB(x, y) & 0xff;
                image.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
    }

    public ComplexImage(double[] colors, int width, int height)
    {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                int color = (int) (colors[x + y * image.getHeight()] * 255);
                image.setRGB(x, y, new Color(color, color, color).getRGB());
            }
        }
    }

    public double[] getColors()
    {
        double[] colors = new double[image.getWidth() * image.getHeight()];

        for(int x = 0; x < 150; x++)
        {
            for(int y = 0; y < 150; y++)
            {
                int rgb = image.getRGB(x, y);
                int red = rgb >> 16 & 0xff;
                int green = rgb >> 8 & 0xff;
                int blue = rgb & 0xff;
                int avg = (red + green + blue) / 3;
                colors[x + y * 150] = avg / 255.0;
            }
        }

        return colors;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public void loadImage(String fileName)
    {
        try
        {
            image = ImageIO.read(new File(fileName));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void resize(int newW, int newH)
    {
        Image tmp = image.getScaledInstance(newW, newH, BufferedImage.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        image = dimg;
    }

    public BufferedImage getChannel(ChannelType channelType)
    {
        BufferedImage channel = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for(int x = 0; x < channel.getWidth(); x++)
        {
            for(int y = 0; y < channel.getHeight(); y++)
            {
                int rgb = image.getRGB(x, y);

                switch(channelType)
                {
                    case RED -> channel.setRGB(x, y, new Color(rgb >> 16 & 0xff, 0, 0).getRGB());
                    case GREEN -> channel.setRGB(x, y, new Color(0, rgb >> 8 & 0xff, 0).getRGB());
                    case BLUE -> channel.setRGB(x, y, new Color(0, 0, rgb & 0xff).getRGB());
                }
            }
        }
        return channel;
    }
}