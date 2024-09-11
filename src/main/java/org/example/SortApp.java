package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SortApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JButton[] numberButtons;
    private int[] numbers;
    private boolean isDescending = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortApp app = new SortApp();
            app.setVisible(true);
        });
    }

    public SortApp() {
        setTitle("Sort Application");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel introPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        introPanel.add(new JLabel("How many numbers to display?"), gbc);

        JTextField numberInput = new JTextField(10);
        gbc.gridy = 1;
        introPanel.add(numberInput, gbc);

        JButton enterButton = new JButton("Enter");
        gbc.gridy = 2;
        introPanel.add(enterButton, gbc);

        JPanel sortPanel = new JPanel(new BorderLayout());
        JPanel numbersPanel = new JPanel();
        numbersPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(100, getHeight()));

        JButton sortButton = new JButton("Sort");
        JButton resetButton = new JButton("Reset");
        sortButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(sortButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createVerticalGlue());

        sortPanel.add(new JScrollPane(numbersPanel), BorderLayout.CENTER);
        sortPanel.add(buttonPanel, BorderLayout.EAST);

        mainPanel.add(introPanel, "Intro");
        mainPanel.add(sortPanel, "Sort");
        add(mainPanel);

        enterButton.addActionListener(e -> {
            int numCount;
            try {
                numCount = Integer.parseInt(numberInput.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                return;
            }
            if (numCount <= 0 || numCount > 1000) {
                JOptionPane.showMessageDialog(this, "Please enter a number between 1 and 1000.");
                return;
            }
            generateRandomNumbers(numbersPanel, numCount);
            cardLayout.show(mainPanel, "Sort");
        });

        sortButton.addActionListener(e -> {
            quickSort(numbers, 0, numbers.length - 1, isDescending);
            isDescending = !isDescending;
            updateNumberButtons(numbersPanel);
        });

        resetButton.addActionListener(e -> cardLayout.show(mainPanel, "Intro"));
    }

    private void generateRandomNumbers(JPanel numbersPanel, int numCount) {
        Random random = new Random();
        numbers = new int[numCount];
        boolean containsSmallNumber = false;

        for (int i = 0; i < numCount; i++) {
            numbers[i] = random.nextInt(1000) + 1;
            if (numbers[i] <= 30) {
                containsSmallNumber = true;
            }
        }

        if (!containsSmallNumber) {
            numbers[random.nextInt(numCount)] = random.nextInt(30) + 1;
        }

        updateNumberButtons(numbersPanel);
    }

    private void updateNumberButtons(JPanel numbersPanel) {
        numbersPanel.removeAll();
        numberButtons = new JButton[numbers.length];
        int numbersPerColumn = 10;
        int totalColumns = (int) Math.ceil(numbers.length / (double) numbersPerColumn);

        for (int i = 0; i < totalColumns; i++) {
            JPanel columnPanel = new JPanel();
            columnPanel.setLayout(new GridLayout(numbersPerColumn, 1, 5, 5));

            for (int j = i * numbersPerColumn; j < Math.min(numbers.length, (i + 1) * numbersPerColumn); j++) {
                numberButtons[j] = new JButton(String.valueOf(numbers[j]));
                columnPanel.add(numberButtons[j]);

                int finalJ = j;
                numberButtons[j].addActionListener(e -> {
                    if (numbers[finalJ] <= 30) {
                        generateRandomNumbers(numbersPanel, numbers.length);
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a value smaller or equal to 30.");
                    }
                    updateNumberButtons(numbersPanel);
                });
            }

            numbersPanel.add(columnPanel);
        }

        numbersPanel.revalidate();
        numbersPanel.repaint();
    }

    private void quickSort(int[] arr, int low, int high, boolean descending) {
        if (low < high) {
            int pi = partition(arr, low, high, descending);
            quickSort(arr, low, pi - 1, descending);
            quickSort(arr, pi + 1, high, descending);
        }
    }

    private int partition(int[] arr, int low, int high, boolean descending) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (descending ? arr[j] > pivot : arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
