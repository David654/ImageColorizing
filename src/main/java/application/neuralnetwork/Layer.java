package application.neuralnetwork;

public class Layer
{
    public final int size;
    public final int nextSize;
    public double[] neurons;
    public double[][] weights;
    public double[] biases;

    public Layer(int size, int nextSize)
    {
        this.size = size;
        this.nextSize = nextSize;
        neurons = new double[size];
        weights = new double[size][nextSize];
        biases = new double[size];
    }

    public int getSize()
    {
        return size;
    }

    public int getNextSize()
    {
        return nextSize;
    }

    public double[] getNeurons()
    {
        return neurons;
    }

    public void setNeurons(double[] neurons)
    {
        System.arraycopy(neurons, 0, this.neurons, 0, neurons.length);
    }

    public double[][] getWeights()
    {
        return weights;
    }

    public void setWeights(double[][] weights)
    {
        this.weights = weights;
    }

    public double[] getBiases()
    {
        return biases;
    }

    public void setBiases(double[] biases)
    {
        this.biases = biases;
    }

    public double getNeuron(int i)
    {
        return neurons[i];
    }

    public double getWeight(int i, int j)
    {
        return weights[i][j];
    }

    public double getBias(int i)
    {
        return biases[i];
    }

    public void setNeuron(int i, double value)
    {
        neurons[i] = value;
    }

    public void setWeight(int i, int j, double value)
    {
        weights[i][j] = value;
    }

    public void setBias(int i, double value)
    {
        biases[i] = value;
    }
}