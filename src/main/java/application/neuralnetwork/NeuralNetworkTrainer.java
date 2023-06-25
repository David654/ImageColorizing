package application.neuralnetwork;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import application.gui.*;
import application.image.ComplexImage;

public class NeuralNetworkTrainer
{
    private final NeuralNetwork neuralNetwork;
    private final TrainingWindow trainingWindow;

    public NeuralNetworkTrainer(NeuralNetwork neuralNetwork)
    {
        this.neuralNetwork = neuralNetwork;
        trainingWindow = new TrainingWindow(400, 600, "Neural Network Training", neuralNetwork);
    }

    public TrainingWindow getTrainingWindow()
    {
        return trainingWindow;
    }

    public void train(String grayScaleImageDir, String coloredImageDir, int imageSamples, int epochs)
    {
        trainingWindow.setEpochsTotal(epochs);
        trainingWindow.setImageSamples(imageSamples);
        trainingWindow.open();

        trainingWindow.startTimer();
        trainingWindow.printToConsole("LEARNING STARTED.");
        trainingWindow.printToConsole("READING DATA...");

        File[] grayScaleFiles = new File(grayScaleImageDir).listFiles();
        File[] coloredFiles = new File(coloredImageDir).listFiles();
        //double[] digits = new double[imageSamples];
        BufferedImage[] images = new BufferedImage[imageSamples];
        BufferedImage[] coloredImages = new BufferedImage[imageSamples];
        for(int i = 0; i < imageSamples; i++)
        {
            //digits[i] = Integer.parseInt(files[i].getName().charAt(10) + "");
            try
            {
                images[i] = ImageIO.read(grayScaleFiles[i]);
                coloredImages[i] = ImageIO.read(coloredFiles[i]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        trainingWindow.printToConsole("Training for RED channel...");
        trainForColorChannel(neuralNetwork, ComplexImage.ChannelType.RED, imageSamples, epochs, images, coloredImages);

        /*trainingWindow.printToConsole("\n\nTraining for GREEN channel...");
        trainForColorChannel(neuralNetwork, ComplexImage.ChannelType.GREEN, imageSamples, epochs, images, coloredImages);

        saveResult("src\\main\\resources\\output green.dat");

        trainingWindow.printToConsole("\n\nTraining for BLUE channel...");
        trainForColorChannel(neuralNetwork, ComplexImage.ChannelType.BLUE, imageSamples, epochs, images, coloredImages);

        saveResult("src\\main\\resources\\output blue.dat");**/

        NeuralNetworkThread redThread = new NeuralNetworkThread(this, "src\\main\\resources\\output red.dat", ComplexImage.ChannelType.RED, imageSamples, epochs, images, coloredImages);
        NeuralNetworkThread greenThread = new NeuralNetworkThread(this, "src\\main\\resources\\output green.dat", ComplexImage.ChannelType.GREEN, imageSamples, epochs, images, coloredImages);
        NeuralNetworkThread blueThread = new NeuralNetworkThread(this, "src\\main\\resources\\output blue.dat", ComplexImage.ChannelType.BLUE, imageSamples, epochs, images, coloredImages);

        //redThread.start();
        //greenThread.start();
       // blueThread.start();

        trainingWindow.printToConsole("LEARNING FINISHED SUCCESSFULLY.");
        trainingWindow.stopTimer();
    }

    public void trainForColorChannel(NeuralNetwork neuralNetwork, ComplexImage.ChannelType channelType, int imageSamples, int epochs, BufferedImage[] grayScaleImages, BufferedImage[] coloredImages)
    {
        trainingWindow.setEpochs(0);

        int firstLayerNeuronsNum = neuralNetwork.layers[0].size;
        double[][] inputNeurons = new double[imageSamples][firstLayerNeuronsNum];

        for(int i = 0; i < inputNeurons.length; i++)
        {
            for(int x = 0; x < 150; x++)
            {
                for(int y = 0; y < 150; y++)
                {
                    int rgb = grayScaleImages[i].getRGB(x, y);
                    int color = 0;

                    switch(channelType)
                    {
                        case RED -> color = rgb >> 16 & 0xff;
                        case GREEN -> color = rgb >> 8 & 0xff;
                        case BLUE -> color = rgb & 0xff;
                    }

                    inputNeurons[i][x + y * 150] = color / 255.0;
                }
            }
        }

        for(int i = 1; i <= epochs; i++)
        {
            int correct = 0;
            double errorSum = 0;
            int batchSize = 10;

            for(int j = 0; j < batchSize; j++)
            {
                int imageIndex = (int) (Math.random() * imageSamples);
                int lastLayerNeuronsNum = neuralNetwork.layers[neuralNetwork.layers.length - 1].size;
                double[] targets = new double[lastLayerNeuronsNum];
                //int digit = (int) digits[imageIndex];
                //targets[digit] = 1.0;
                BufferedImage grayScaleImage = grayScaleImages[imageIndex];
                BufferedImage coloredImage = coloredImages[imageIndex];

                int[] colors = new int[coloredImage.getWidth() * coloredImage.getHeight()];

                for(int x = 0; x < coloredImage.getWidth(); x++)
                {
                    for(int y = 0; y < coloredImage.getHeight(); y++)
                    {
                        int rgb = coloredImage.getRGB(x, y);
                        int color = 0;

                        switch(channelType)
                        {
                            case RED -> color = rgb >> 16 & 0xff;
                            case GREEN -> color = rgb >> 8 & 0xff;
                            case BLUE -> color = rgb & 0xff;
                        }
                        colors[x + y * coloredImage.getHeight()] = color;
                    }
                }

                //trainingWindow.printToConsole("Loaded colors (" + (j + 1) + " / " + epochs * batchSize + ")");

                for(int a = 0; a < colors.length; a++)
                {
                    int color = colors[a]; //(int) (Math.random() * colors.length)
                    targets[a] = color / 255.0;
                }

                double[] out = neuralNetwork.feedForward(inputNeurons[imageIndex]);
                int maxDigit = 0;
                double maxDigitWeight = -1;

               /* for(int k = 0; k < colors.length; k++)
                {
                    if(out[k] > maxDigitWeight)
                    {
                        maxDigitWeight = out[k];
                        int rgb = coloredImage.getRGB(k % coloredImage.getWidth(), k / coloredImage.getWidth());
                        int maxColor = 0;

                        switch(channelType)
                        {
                            case RED -> maxColor = rgb >> 16 & 0xff;
                            case GREEN -> maxColor = rgb >> 8 & 0xff;
                            case BLUE -> maxColor = rgb & 0xff;
                        }
                        maxDigit = maxColor;
                    }
                }**/

                for(int a = 0; a < colors.length; a++)
                {
                    if(targets[a] == out[a])
                    {
                        correct++;
                    }
                }

                for(int l = 0; l < lastLayerNeuronsNum; l++)
                {
                    errorSum += Math.pow(targets[l] - out[l], 2);
                }

                neuralNetwork.backPropagation(targets);

                //trainingWindow.printToConsole("Back propagation performed (" + (j + 1) + " / " + epochs * batchSize + ")");
            }
            //System.out.println("Epoch: " + i + ", correct: " + correct + ", error: " + errorSum);
            trainingWindow.setEpochs(i);
            trainingWindow.printToConsole(("Epoch: " + i + ", correct: " + correct + ", error: " + trainingWindow.format(errorSum, "#.###")));

            if(i > 0 && i % 20 == 0)
            {
                saveResult("src\\main\\resources\\output red.dat");
            }
        }
    }

    public void saveResult(String filename)
    {
        try
        {
            File myObj = new File(filename);
            if(myObj.createNewFile()) System.out.println("File created: " + myObj.getName());
            else System.out.println("File already exists.");

            FileWriter writer = new FileWriter(filename);

            for(int i = 0; i < neuralNetwork.layers.length - 1; i++)
            {
                //double[] neurons = neuralNetwork.layers[i].getNeurons();
                double[][] weights = neuralNetwork.layers[i].getWeights();
                double[] biases = neuralNetwork.layers[i].getBiases();

                //writer.write("n" + Arrays.toString(neurons));
                //writer.write("\n\n");
                writer.write("w" + Arrays.deepToString(weights));
                writer.write("\n\n");
                writer.write("b" + Arrays.toString(biases));
                writer.write("\n\n----------------------\n\n");
            }

            writer.close();
            System.out.println("Successfully wrote to the file.");
        }
        catch(IOException e)
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void read(String filename)
    {
        ArrayList<String> lines = new ArrayList<>();
        try
        {
            Path p = Paths.get(filename);
            BufferedReader reader = Files.newBufferedReader(p);
            while(true)
            {
                String line = reader.readLine();
                if(line == null)
                {
                    break;
                }

                if(!line.equals("") && !line.equals("\n"))
                {
                    lines.add(line);
                }
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        int layerNum = 0;
        for(String line : lines)
        {
            double[][] weights = new double[neuralNetwork.layers[layerNum].getSize()][neuralNetwork.layers[layerNum].getNextSize()];
            double[] biases = new double[neuralNetwork.layers[layerNum].getSize()];

            if(line.charAt(0) == 'w')
            {
                line = line.substring(3, line.length() - 2);
                String[] values = line.split(", \\[");
                for(int i = 0; i < values.length; i++)
                {
                    String s = values[i];
                    if(s.endsWith("]"))
                    {
                        values[i] = values[i].replace("]", "");
                    }

                    String[] value = values[i].split(", ");
                    for(int j = 0; j < value.length; j++)
                    {
                        weights[i][j] = Double.parseDouble(value[j]);
                    }
                }
                neuralNetwork.layers[layerNum].setWeights(weights);
            }

            if(line.charAt(0) == 'b')
            {
                line = line.substring(2, line.length() - 1);
                String[] values = line.split(", ");
                for(int i = 0; i < values.length; i++)
                {
                    biases[i] = Double.parseDouble(values[i]);
                }
                neuralNetwork.layers[layerNum].setBiases(biases);
            }

            if(line.contains("---"))
            {
                layerNum++;
            }
        }
    }
}