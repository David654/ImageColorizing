package application.neuralnetwork;

import application.image.ComplexImage;

import java.awt.image.BufferedImage;

public class NeuralNetworkThread implements Runnable
{
    private final NeuralNetworkTrainer neuralNetworkTrainer;
    private final String outputDir;
    private final ComplexImage.ChannelType channelType;
    private final int imageSamples;
    private final int epochs;
    private final BufferedImage[] grayImages;
    private final BufferedImage[] coloredImages;

    private final NeuralNetwork neuralNetwork;

    private final Thread thread;

    public NeuralNetworkThread(NeuralNetworkTrainer neuralNetworkTrainer, String outputDir, ComplexImage.ChannelType channelType, int imageSamples, int epochs, BufferedImage[] grayImages, BufferedImage[] coloredImages)
    {
        this.neuralNetworkTrainer = neuralNetworkTrainer;
        this.outputDir = outputDir;
        this.channelType = channelType;
        this.imageSamples = imageSamples;
        this.epochs = epochs;
        this.grayImages = grayImages;
        this.coloredImages = coloredImages;

        neuralNetwork =  new NeuralNetwork(NeuralNetwork.SIGMOID, NeuralNetwork.DERIVATIVE, NeuralNetwork.LEARNING_STEP, 22500, 1875, 1125, 1875, 22500);

        thread = new Thread(this);
    }

    public synchronized void start()
    {
        thread.start();
    }

    public synchronized void stop()
    {
        try
        {
            thread.join();
        }
        catch(InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void run()
    {
        neuralNetworkTrainer.getTrainingWindow().printToConsole("Training for " + channelType.name() + " channel...");
        neuralNetworkTrainer.trainForColorChannel(neuralNetwork, channelType, imageSamples, epochs, grayImages, coloredImages);

        neuralNetworkTrainer.saveResult(outputDir);
    }
}