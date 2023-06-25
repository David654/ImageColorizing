package application.neuralnetwork;

import java.util.function.UnaryOperator;

public class NeuralNetwork
{
    public static final UnaryOperator<Double> SIGMOID = x -> 1 / (1 + Math.exp(-x));
    public static final UnaryOperator<Double> DERIVATIVE = y -> y * (1 - y);
    public static final double LEARNING_STEP = 0.001;

    private final UnaryOperator<Double> activation;
    private final UnaryOperator<Double> derivative;
    private final double learningStep;
    public final Layer[] layers;

    public NeuralNetwork(UnaryOperator<Double> activation, UnaryOperator<Double> derivative, double learningStep, int... layerSizes)
    {
        this.activation = activation;
        this.derivative = derivative;
        this.learningStep = learningStep;
        layers = new Layer[layerSizes.length];

        for(int i = 0; i < layers.length; i++)
        {
            int nextLayerSize = 0;
            if(i < layers.length - 1)
            {
                nextLayerSize = layerSizes[i + 1];
            }
            layers[i] = new Layer(layerSizes[i], nextLayerSize);

            for (int j = 0; j < layerSizes[i]; j++)
            {
                layers[i].setBias(j, Math.random() * 2.0 - 1.0);
                for (int k = 0; k < nextLayerSize; k++)
                {
                    layers[i].setWeight(j, k, Math.random() * 2.0 - 1.0);
                }
            }
        }
    }

    public UnaryOperator<Double> getActivation()
    {
        return activation;
    }

    public UnaryOperator<Double> getDerivative()
    {
        return derivative;
    }

    public double getLearningStep()
    {
        return learningStep;
    }

    public double[] feedForward(double[] inputNeurons)
    {
        layers[0].setNeurons(inputNeurons);

        for(int i = 1; i < layers.length; i++)
        {
            Layer l = layers[i - 1];
            Layer l1 = layers[i];

            for(int j = 0; j < l1.getSize(); j++)
            {
                l1.setNeuron(j, 0);
                for(int k = 0; k < l.getSize(); k++)
                {
                    l1.setNeuron(j, l1.getNeuron(j) + l.getNeuron(k) * l.getWeight(k, j));
                }
                l1.setNeuron(j, l1.getNeuron(j) + l1.getBias(j));
                l1.setNeuron(j, activation.apply(l1.getNeuron(j)));
            }
        }

        return layers[layers.length - 1].getNeurons();
    }

    public void backPropagation(double[] targets)
    {
        double[] errors = new double[layers[layers.length - 1].getSize()];

        for(int i = 0; i < errors.length; i++)
        {
            errors[i] = targets[i] - layers[layers.length - 1].getNeuron(i);
        }

        for(int k = layers.length - 2; k >= 0; k--)
        {
            Layer l = layers[k];
            Layer l1 = layers[k + 1];

            double[] gradients = new double[l1.getSize()];
            double[][] deltas = new double[l1.getSize()][l.getSize()];
            double[] errorsNext = new double[l.getSize()];
            double[][] weightsNew = new double[l.getSize()][l.getNextSize()];

            for(int i = 0; i < l1.getSize(); i++)
            {
                double out = l1.getNeuron(i);
                double gradient = learningStep * errors[i] * derivative.apply(out);
                gradients[i] = gradient;
            }

            for(int i = 0; i < l1.getSize(); i++)
            {
                for(int j = 0; j < l.getSize(); j++)
                {
                    deltas[i][j] = gradients[i] * l.getNeuron(j);
                }
            }

            for(int i = 0; i < l.getSize(); i++)
            {
                errorsNext[i] = 0;
                for(int j = 0; j < l1.getSize(); j++)
                {
                    errorsNext[i] += errors[j] * l.getWeight(i, j);
                }
            }

            errors = new double[l.getSize()];
            System.arraycopy(errorsNext, 0, errors, 0, l.getSize());
            for(int i = 0; i < l1.getSize(); i++)
            {
                for(int j = 0; j < l.getSize(); j++)
                {
                    weightsNew[j][i] = l.getWeight(j, i) + deltas[i][j];
                }
            }

            l.setWeights(weightsNew);

            for(int i = 0; i < l1.getSize(); i++)
            {
                l1.setBias(i, l1.getBias(i) + gradients[i]);
            }
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < layers.length; i++)
        {
            sb.append(layers[i].size);
            if(i < layers.length - 1) sb.append(" → ");
        }
        return sb.toString();
    }
}