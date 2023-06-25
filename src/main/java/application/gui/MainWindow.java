package application.gui;

import application.Application;
import application.neuralnetwork.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.UnaryOperator;

public class MainWindow extends AbstractWindow
{
    private InputPanel inputPanel;
    private OutputPanel outputPanel;

    private NeuralNetwork neuralNetwork;

    public MainWindow(int width, int height, String title)
    {
        super(width, height, title);
        frame.setLayout(new BorderLayout());

        initNeuralNetwork();
        init();
        createAndShowGUI();
    }

    public void show()
    {
        frame.setVisible(true);
    }

    public InputPanel getInputPanel()
    {
        return inputPanel;
    }

    public OutputPanel getOutputPanel()
    {
        return outputPanel;
    }

    private void initNeuralNetwork()
    {
       // NeuralNetworkTrainer neuralNetworkTrainer = new NeuralNetworkTrainer(neuralNetwork);
        //neuralNetworkTrainer.read("resources\\output.dat");

        //neuralNetworkTrainer.learn("resources\\test", 10000, 1000);
        //neuralNetworkTrainer.saveResult("resources\\output.dat");

        /*long start = System.currentTimeMillis();
        neuralNetworkTrainer.learn("resources\\train", 60000, 4000);
        long end = System.currentTimeMillis();
        System.out.println("Training 1: " + (end - start) / 60 / 1000 + " min");

        neuralNetworkTrainer.saveResult("resources\\output.dat");

        neuralNetworkTrainer.read("resources\\output.dat");
        start = System.currentTimeMillis();
        neuralNetworkTrainer.learn("resources\\test", 10000, 1000);
        end = System.currentTimeMillis();
        System.out.println("Training 2: " + (end - start) / 60 / 1000 + " min");
        neuralNetworkTrainer.saveResult("resources\\output.dat");**/
    }

    private void init()
    {
        inputPanel = new InputPanel(Application.WIDTH / 2 - 50, Application.HEIGHT);
        outputPanel = new OutputPanel(Application.WIDTH / 2 - 50, Application.HEIGHT);
       // mouse = new Mouse(handler);
    }

    protected void createAndShowGUI()
    {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        frame.add(splitPane, BorderLayout.CENTER);
    }
}