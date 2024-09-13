package org.example;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Random;

public class SortApp extends JFrame {
    private static final int MAX_NUM = 1000;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private JButton[] numberButtons;
    private int[] numbers;
    private boolean isDescending = true;
    private final Random random = new Random();
    private int numCount;
    private int[] sortedNumbers;
    private JPanel numbersPanel;
    private static final int SMALL_NUMBER_THRESHOLD = 30;

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

        JPanel introPanel = createIntroPanel();
        JPanel sortPanel = createSortPanel();

        mainPanel.add(introPanel, "Intro");
        mainPanel.add(sortPanel, "Sort");
        add(mainPanel);
    }

    private JPanel createIntroPanel() {
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

        enterButton.addActionListener(e -> {
            try {
                numCount = Integer.parseInt(numberInput.getText());
                if (numCount <= 0 || numCount > MAX_NUM) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a number between 1 and 1000.");
                return;
            }
            generateRandomNumbers();
            cardLayout.show(mainPanel, "Sort");
        });

        return introPanel;
    }

    private JPanel createSortPanel() {
        JPanel sortPanel = new JPanel(new BorderLayout());
        numbersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel buttonPanel = createButtonPanel();
        sortPanel.add(new JScrollPane(numbersPanel), BorderLayout.CENTER);
        sortPanel.add(buttonPanel, BorderLayout.EAST);

        return sortPanel;
    }

    private JPanel createButtonPanel() {
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

        sortButton.addActionListener(e -> quickSortWithVisual());
        resetButton.addActionListener(e -> cardLayout.show(mainPanel, "Intro"));

        return buttonPanel;
    }

    private void generateRandomNumbers() {
        numbers = new int[numCount];
        sortedNumbers = new int[numCount];
        boolean containsSmallNumber = false;

        for (int i = 0; i < numCount; i++) {
            numbers[i] = random.nextInt(MAX_NUM) + 1;
            if (numbers[i] <= SMALL_NUMBER_THRESHOLD) containsSmallNumber = true;
        }
        if (!containsSmallNumber) {
            numbers[random.nextInt(numCount)] = random.nextInt(SMALL_NUMBER_THRESHOLD) + 1;
        }
        if (isDescending) {
            reverseArray(sortedNumbers);
        }
        updateNumberButtons();
    }

    private void updateNumberButtons() {
        SwingUtilities.invokeLater(() -> {
            numbersPanel.removeAll();
            numberButtons = new JButton[numCount];
            int numbersPerColumn = 10;
            int totalColumns = (int) Math.ceil(numCount / (double) numbersPerColumn);

            for (int i = 0; i < totalColumns; i++) {
                JPanel columnPanel = new JPanel(new GridLayout(numbersPerColumn, 1, 5, 5));

                for (int j = i * numbersPerColumn; j < Math.min(numCount, (i + 1) * numbersPerColumn); j++) {
                    numberButtons[j] = new JButton(String.valueOf(numbers[j]));
                    int finalJ = j;
                    numberButtons[j].addActionListener(e -> handleNumberClick(finalJ));
                    columnPanel.add(numberButtons[j]);
                }

                numbersPanel.add(columnPanel);
            }
            numbersPanel.revalidate();
            numbersPanel.repaint();
        });
    }

    private void handleNumberClick(int index) {
        if (numbers[index] <= SMALL_NUMBER_THRESHOLD) {
            generateRandomNumbers();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a value smaller or equal to 30.");
        }
        updateNumberButtons();
    }

    private void quickSortWithVisual() {
        resetAllButtonColors();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                quickSort(numbers, sortedNumbers, 0, numbers.length - 1, isDescending);
                return null;
            }

            @Override
            protected void done() {
                isDescending = !isDescending;
                highlightSortedNumbers();
            }
        }.execute();
    }

    private void resetAllButtonColors() {
        for (JButton button : numberButtons) {
            button.setBackground(Color.WHITE);
        }
    }

    private void quickSort(int[] arr, int[] arrSorted, int low, int high, boolean descending) {
        if (low < high) {
            int pi = partition(arr, low, high, descending);

            arrSorted[pi] = arr[pi];
            highlightSortedNumber(pi);

            checkAndUpdateAlreadySorted(arr, arrSorted, low, pi - 1, descending);
            checkAndUpdateAlreadySorted(arr, arrSorted, pi + 1, high, descending);

            quickSort(arr, arrSorted, low, pi - 1, descending);
            quickSort(arr, arrSorted, pi + 1, high, descending);
        }
    }

    private int partition(int[] arr, int low, int high, boolean descending) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (isButtonGreen(j)) {
                continue;
            }

            if (descending ? arr[j] > pivot : arr[j] < pivot) {
                swap(arr, ++i, j);
                updateNumberButtonsOnSwap(i, j);
                sleep();
            }
        }

        if (!isButtonGreen(high)) {
            swap(arr, i + 1, high);
            updateNumberButtonsOnSwap(i + 1, high);
        }
        sleep();

        return i + 1;
    }

    private boolean isButtonGreen(int index) {
        return numberButtons[index].getBackground().equals(Color.GREEN);
    }

    private void highlightSortedNumber(int index) {
        if (!isButtonGreen(index)) {
            numberButtons[index].setBackground(Color.GREEN);
        }
    }

    private void checkAndUpdateAlreadySorted(int[] arr, int[] arrSorted, int low, int high, boolean descending) {
        for (int i = low; i <= high; i++) {
            if (isInCorrectPosition(arr, i, descending)) {
                arrSorted[i] = arr[i];
                highlightSortedNumber(i);
            }
        }
    }

    private boolean isInCorrectPosition(int[] arr, int index, boolean descending) {
        if (descending) {
            for (int i = 0; i < index; i++) {
                if (arr[i] < arr[index]) return false;
            }
            for (int i = index + 1; i < arr.length; i++) {
                if (arr[i] > arr[index]) return false;
            }
        } else {
            for (int i = 0; i < index; i++) {
                if (arr[i] > arr[index]) return false;
            }
            for (int i = index + 1; i < arr.length; i++) {
                if (arr[i] < arr[index]) return false;
            }
        }
        return true;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void updateNumberButtonsOnSwap(int index1, int index2) {
        updateButtonColors(index1, index2, Color.RED);

        Timer swapTimer = new Timer(500, e -> updateButtonColors(index1, index2, Color.BLUE));
        swapTimer.setRepeats(false);
        swapTimer.start();

        Timer resetTimer = new Timer(1500, e -> resetButtonColors(index1, index2));
        resetTimer.setRepeats(false);
        resetTimer.start();
    }

    private void updateButtonColors(int index1, int index2, Color color) {
        numberButtons[index1].setBackground(color);
        numberButtons[index2].setBackground(color);
        numberButtons[index1].setText(String.valueOf(numbers[index1]));
        numberButtons[index2].setText(String.valueOf(numbers[index2]));
    }

    private void resetButtonColors(int index1, int index2) {
        if (isInCorrectPosition(index1)) {
            numberButtons[index1].setBackground(Color.GREEN);
        } else {
            numberButtons[index1].setBackground(Color.WHITE);
        }
        if (isInCorrectPosition(index2)) {
            numberButtons[index2].setBackground(Color.GREEN);
        } else {
            numberButtons[index2].setBackground(Color.WHITE);
        }
    }

    private boolean isInCorrectPosition(int index) {
        return numbers[index] == sortedNumbers[index];
    }

    private void highlightSortedNumbers() {
        for (int i = 0; i < numCount; i++) {
            if (isInCorrectPosition(i)) {
                numberButtons[i].setBackground(Color.GREEN);
            }
        }
    }

    private void reverseArray(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
