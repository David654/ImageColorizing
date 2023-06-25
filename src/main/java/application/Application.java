package application;

import application.gui.MainWindow;
import application.image.ComplexImage;
import application.neuralnetwork.NeuralNetwork;
import application.neuralnetwork.NeuralNetworkTrainer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Application
{
    public static final Dimension DIMENSIONS = Toolkit.getDefaultToolkit().getScreenSize();
    public static int WIDTH = DIMENSIONS.width / 2;
    public static int HEIGHT = DIMENSIONS.height / 2;

    public static final String TITLE = "Image Colorizing";

    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color FOREGROUND_COLOR = Color.WHITE;
    public static final Font FONT = new Font("Calibri", Font.PLAIN, 20);

    public static NeuralNetwork NEURAL_NETWORK;
    public static BufferedImage INPUT_IMAGE;

    public void launch()
    {
        NEURAL_NETWORK = new NeuralNetwork(NeuralNetwork.SIGMOID, NeuralNetwork.DERIVATIVE, NeuralNetwork.LEARNING_STEP, 22500, 1875, 1125, 1875, 22500);

        NeuralNetworkTrainer neuralNetworkTrainer = new NeuralNetworkTrainer(NEURAL_NETWORK);
        //neuralNetworkTrainer.read("src\\main\\resources\\output.dat");

        neuralNetworkTrainer.train("src\\main\\resources\\images\\gray", "src\\main\\resources\\images\\color", 100, 100);
        //neuralNetworkTrainer.saveResult("src\\main\\resources\\output.dat");

        MainWindow window = new MainWindow(WIDTH, HEIGHT, TITLE);

        ComplexImage image = new ComplexImage("src\\main\\resources\\images\\color\\0.jpg");

        double[] colors = NEURAL_NETWORK.feedForward(image.getColors());

        BufferedImage redChannel = image.getChannel(ComplexImage.ChannelType.RED);
        BufferedImage greenChannel = image.getChannel(ComplexImage.ChannelType.GREEN);
        BufferedImage blueChannel = image.getChannel(ComplexImage.ChannelType.BLUE);
        ComplexImage total = new ComplexImage(colors, 150, 150);

        window.getInputPanel().getImageCanvas().setImage(image.getImage());
        window.getOutputPanel().getRedChannelTab().getImageCanvas().setImage(redChannel);
        window.getOutputPanel().getGreenChannelTab().getImageCanvas().setImage(greenChannel);
        window.getOutputPanel().getBlueChannelTab().getImageCanvas().setImage(blueChannel);
        window.getOutputPanel().getTotalChannelTab().getImageCanvas().setImage(total.getImage());

        window.show();
    }
}