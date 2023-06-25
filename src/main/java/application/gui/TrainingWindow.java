package application.gui;

import application.neuralnetwork.NeuralNetwork;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class TrainingWindow extends AbstractWindow
{
    private final JFrame frame;
    private JLabel imageSamplesLabel;
    private JLabel epochsTotalLabel;
    private JProgressBar progressBar;
    private JLabel timeLabel;
    private JTextArea console;
    private final Timer timer;

    private final NeuralNetwork neuralNetwork;
    private int epochs = 0;
    private int epochsTotal = 0;
    private int imageSamples = 0;
    private int time = 0;

    public TrainingWindow(int width, int height, String title, NeuralNetwork neuralNetwork)
    {
        super(width, height, title);
        this.neuralNetwork = neuralNetwork;
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timer = new Timer(1000, e ->
        {
            time++;
            int hours = time / 3600;
            int minutes = time / 60;
            int seconds = time % 60;
            timeLabel.setText(hours + ":" + (minutes > 9 ? minutes : "0" + minutes) + ":" + (seconds > 9 ? seconds : "0" + seconds));
        });

        createAndShowGUI();
    }

    public void setEpochs(int epochs)
    {
        this.epochs = epochs;
        progressBar.setString(epochs + " / " + epochsTotal + " (" + format((double) epochs / epochsTotal * 100, "#.##") + "%)");
        progressBar.setValue(epochs);
    }

    public void setEpochsTotal(int epochsTotal)
    {
        this.epochsTotal = epochsTotal;
        progressBar.setMinimum(0);
        progressBar.setMaximum(epochsTotal);
        progressBar.setString(epochs + " / " + epochsTotal + " (" + (epochs / epochsTotal * 100) + "%)");
        epochsTotalLabel.setText(String.valueOf(epochsTotal));
    }

    public void setImageSamples(int imageSamples)
    {
        this.imageSamples = imageSamples;
        imageSamplesLabel.setText(String.valueOf(imageSamples));
    }

    public void printToConsole(String text)
    {
        console.append("\n" + text);
    }

    public String format(double value, String pattern)
    {
        DecimalFormat df = new DecimalFormat(pattern);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    public void open()
    {
        frame.setVisible(true);
    }

    public void startTimer()
    {
        timer.start();
    }

    public void stopTimer()
    {
        timer.stop();
       // frame.dispose();
    }

    protected void createAndShowGUI()
    {
        initNeuralNetworkPanel();
        initAlgorithmsPanel();
        initProgressPanel();
        initButtonPanel();
    }

    private void initNeuralNetworkPanel()
    {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Neural Network"));
        panel.setLayout(new SpringLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) panel.getPreferredSize().getHeight()));

        String[] nameLabels = {"Layers total: ", "Neurons: "};
        String[] valueLabels = {String.valueOf(neuralNetwork.layers.length), neuralNetwork.toString()};

        for (int i = 0; i < nameLabels.length; i++)
        {
            JLabel l1 = new JLabel(nameLabels[i]);
            panel.add(l1);
            JLabel l2 = new JLabel(valueLabels[i]);
            panel.add(l2);
        }

        SpringUtilities.makeCompactGrid(panel, nameLabels.length, 2, 5, 5, 5, 5);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        frame.add(panel);
    }

    private void initAlgorithmsPanel()
    {
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        panel.setBorder(new TitledBorder("Algorithms"));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) panel.getPreferredSize().getHeight()));

        String[] nameLabels = {"Training algorithm: ", "Activation function: ", "Derivative: ", "Learning step: "};
        String[] valueLabels = {"Back propagation", "Sigmoid", "Sigmoid derivative", String.valueOf(neuralNetwork.getLearningStep())};

        for (int i = 0; i < nameLabels.length; i++)
        {
            JLabel l1 = new JLabel(nameLabels[i]);
            panel.add(l1);
            JLabel l2 = new JLabel(valueLabels[i]);
            panel.add(l2);
        }

        SpringUtilities.makeCompactGrid(panel, nameLabels.length, 2, 5, 5, 5, 5);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        frame.add(panel);
    }

    private void initProgressPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new TitledBorder("Progress"));

        JPanel p1 = new JPanel();
        SpringLayout layout = new SpringLayout();
        p1.setLayout(layout);

        JLabel l1 = new JLabel("Image samples: ");
        JLabel l2 = new JLabel("Epochs total: ");
        JLabel l3 = new JLabel("Epoch: ");
        JLabel l4 = new JLabel("Time: ");
        imageSamplesLabel = new JLabel(String.valueOf(imageSamples));
        epochsTotalLabel = new JLabel(String.valueOf(epochsTotal));
        progressBar = new JProgressBar(0, epochsTotal);
        progressBar.setString(epochs + " / " + epochsTotal + " (0%)");
        progressBar.setStringPainted(true);
        timeLabel = new JLabel("0:00:00");

        p1.add(l1);
        p1.add(imageSamplesLabel);
        p1.add(l2);
        p1.add(epochsTotalLabel);
        p1.add(l3);
        p1.add(progressBar);
        p1.add(l4);
        p1.add(timeLabel);

        SpringUtilities.makeCompactGrid(p1, 4, 2, 5, 5, 5, 5);

        panel.add(p1, BorderLayout.NORTH);

        console = new JTextArea();
        console.setEditable(false);
        console.setFocusable(false);
        console.setFont(console.getFont().deriveFont(12f));
        DefaultCaret caret = (DefaultCaret) console.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(console);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        frame.add(panel);
    }

    private void initButtonPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) panel.getPreferredSize().getHeight()));

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> System.exit(0));
        cancel.setFocusable(true);
        cancel.setFocusPainted(false);

        panel.add(cancel);

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        frame.add(panel);
    }

    private static final class SpringUtilities
    {
        private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols)
        {
            SpringLayout layout = (SpringLayout) parent.getLayout();
            Component c = parent.getComponent(row * cols + col);
            return layout.getConstraints(c);
        }

        public static void makeCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad, int yPad)
        {
            SpringLayout layout;
            try
            {
                layout = (SpringLayout)parent.getLayout();
            }
            catch(ClassCastException exc)
            {
                System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
                return;
            }

            //Align all cells in each column and make them the same width.
            Spring x = Spring.constant(initialX);
            for (int c = 0; c < cols; c++) {
                Spring width = Spring.constant(0);
                for (int r = 0; r < rows; r++) {
                    width = Spring.max(width,
                            getConstraintsForCell(r, c, parent, cols).
                                    getWidth());
                }
                for (int r = 0; r < rows; r++) {
                    SpringLayout.Constraints constraints =
                            getConstraintsForCell(r, c, parent, cols);
                    constraints.setX(x);
                    constraints.setWidth(width);
                }
                x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
            }

            //Align all cells in each row and make them the same height.
            Spring y = Spring.constant(initialY);
            for (int r = 0; r < rows; r++) {
                Spring height = Spring.constant(0);
                for (int c = 0; c < cols; c++) {
                    height = Spring.max(height,
                            getConstraintsForCell(r, c, parent, cols).
                                    getHeight());
                }
                for (int c = 0; c < cols; c++) {
                    SpringLayout.Constraints constraints =
                            getConstraintsForCell(r, c, parent, cols);
                    constraints.setY(y);
                    constraints.setHeight(height);
                }
                y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
            }

            //Set the parent's size.
            SpringLayout.Constraints pCons = layout.getConstraints(parent);
            pCons.setConstraint(SpringLayout.SOUTH, y);
            pCons.setConstraint(SpringLayout.EAST, x);
        }
    }
}