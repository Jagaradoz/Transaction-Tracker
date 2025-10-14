package application;

import config.LayoutConfig;
import config.StyleConfig;
import util.StyleUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class TransactionTrackerPanel extends JPanel {
    public static RecordComponent SELECTED_RECORD;

    private final ArrayList<RecordComponent> records;

    private JLabel incomeHeader;
    private JLabel expenseHeader;

    private JPanel panelForScrollPane;

    private JTextField detailTextField;
    private JTextField incomeTextField;
    private JTextField expenseTextField;

    private JButton deleteButton;

    public int sumIncome;
    public int sumExpense;

    private final Font mainFont;
    private final Font font14f;

    public TransactionTrackerPanel() {
        // Initialize units and fonts.
        records = new ArrayList<>();

        mainFont = StyleUtils.getFont();
        font14f = mainFont.deriveFont(14f);

        setLayout(new BorderLayout());
        setBackground(StyleConfig.BG);

        add(generateFinancialEntryPanel(), BorderLayout.NORTH);
        add(generateListPanel(), BorderLayout.CENTER);

        // Load saved data.
        loadData();
    }

    public void loadData() {
        // Find data folder.
        File dataFolder = new File("data");
        if (!dataFolder.exists()) return;

        // Read .ser fiel and sotre in record.
        for (File file : Objects.requireNonNull(dataFolder.listFiles())) {
            if (file.isFile()) {
                try (ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(file))) {
                    RecordComponent record = (RecordComponent) objectIn.readObject();
                    record.setTransactionTrackerPanel(this);

                    // Calculate total income, expense.
                    sumIncome += record.getIncome();
                    sumExpense += record.getExpense();

                    records.add(record);
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Error reading object from file: " + ex.getMessage());
                }
            }
        }

        // Update UI.
        updatePanelForScrollPane();
        updateHeaderListPanel();
    }

    public void clearInput() {
        detailTextField.setText("");
        incomeTextField.setText("");
        expenseTextField.setText("");
    }

    public void deleteAllRecords() {
        File dataFolder = new File("data");
        if (!dataFolder.exists()) return;

        // Delete all .ser files.
        for (File file : Objects.requireNonNull(dataFolder.listFiles())) {
            @SuppressWarnings("unused")
            boolean deleted = file.delete();
        }

        records.clear();
        updatePanelForScrollPane();
        updateHeaderListPanel();
    }

    public void deleteSelectedRecord() {
        // Select record to delete.
        if (SELECTED_RECORD != null) {
            File dataFolder = new File("data");
            if (!dataFolder.exists()) return;

            File targetFile = null;

            // Loop through data folder to find selected record.
            for (File file : Objects.requireNonNull(dataFolder.listFiles())) {
                if (file.getName().equals(SELECTED_RECORD.getId() + ".ser")) {
                    targetFile = file;
                    break;
                }
            }

            // Delete record.
            if (targetFile != null && targetFile.exists()) {
                @SuppressWarnings("unused")
                boolean deleted = targetFile.delete();
            }

            // Remove selected record from records.
            records.remove(SELECTED_RECORD);
            deleteButton.setEnabled(false);
            SELECTED_RECORD = null;

            // Update UI.
            updatePanelForScrollPane();
            updateHeaderListPanel();
        }
    }

    public void updateHeaderListPanel() {
        incomeHeader.setText("Income : (" + sumIncome + ")");
        expenseHeader.setText("Expense : (" + sumExpense + ")");
    }

    public void updatePanelForScrollPane() {
        sumIncome = 0;
        sumExpense = 0;

        // Add records to scroll pane panels.
        panelForScrollPane.removeAll();
        for (RecordComponent recordComponent : records) {
            sumIncome += recordComponent.getIncome();
            sumExpense += recordComponent.getExpense();

            panelForScrollPane.add(recordComponent);
        }

        panelForScrollPane.revalidate();
        panelForScrollPane.repaint();
    }

    public void setSelectedRecord(RecordComponent record) {
        // Set selected.
        SELECTED_RECORD = record;
        if (SELECTED_RECORD != null) {
            SELECTED_RECORD.setSelected(true);
        }

        deleteButton.setEnabled(SELECTED_RECORD != null);
    }

    public JPanel generateListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(StyleConfig.BG);
        panel.setBorder(new EmptyBorder(0, 8, 0, 8));

        JPanel headerPanel = generateHeaderListPanel();

        // Panel for storing records.
        panelForScrollPane = new JPanel();
        panelForScrollPane.setBackground(StyleConfig.BG);
        panelForScrollPane.setLayout(new BoxLayout(panelForScrollPane, BoxLayout.Y_AXIS));

        JScrollPane listScrollPane = new JScrollPane(panelForScrollPane);
        listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        listScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(listScrollPane, BorderLayout.CENTER);

        return panel;
    }

    public JPanel generateHeaderListPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(StyleConfig.HEADER_BG);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, StyleConfig.LINE_COLOR));
        panel.setLayout(new GridLayout(1, 4));
        panel.setPreferredSize(new Dimension(500, 30));

        JLabel dateHeader = new JLabel("Date:");
        dateHeader.setFont(font14f);

        JLabel detailHeader = new JLabel("Detail:");
        detailHeader.setFont(font14f);

        incomeHeader = new JLabel("Income:");
        incomeHeader.setFont(font14f);

        expenseHeader = new JLabel("Expense:");
        expenseHeader.setFont(font14f);

        panel.add(dateHeader);
        panel.add(detailHeader);
        panel.add(incomeHeader);
        panel.add(expenseHeader);

        return panel;
    }

    public JPanel generateFinancialEntryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(StyleConfig.BG);
        panel.setPreferredSize(new Dimension(LayoutConfig.SCREEN_WIDTH, 150));

        JLabel detailLabel = generateFinancialEntryLabel("Header :", 25, 10, 100, 20);
        detailTextField = generateFinancialEntryTextField(25, 40, 300, 30);

        JLabel incomeLabel = generateFinancialEntryLabel("Income :", 25, 80, 100, 20);
        incomeTextField = generateFinancialEntryTextField(25, 100, 145, 30);
        incomeTextField.addKeyListener(new NumberListener());

        JLabel expenseLabel = generateFinancialEntryLabel("Expense :", 180, 80, 100, 20);
        expenseTextField = generateFinancialEntryTextField(180, 100, 145, 30);
        expenseTextField.addKeyListener(new NumberListener());

        JButton addButton = generateFinancialEntryButton("ADD", new Color(46, 204, 113), 345, 100);
        addButton.addActionListener(_ -> addRecord(detailTextField.getText(), incomeTextField.getText(), expenseTextField.getText()));

        JButton clearButton = generateFinancialEntryButton("CLEAR", new Color(46, 204, 113), 345, 40);
        clearButton.addActionListener(_ -> clearInput());

        deleteButton = generateFinancialEntryButton("DELETE", new Color(231, 76, 60), 480, 40);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(_ -> deleteSelectedRecord());

        JButton deleteAllButton = generateFinancialEntryButton("DELETE ALL", new Color(231, 76, 60), 480, 100);
        deleteAllButton.addActionListener(_ -> deleteAllRecords());

        panel.add(detailLabel);
        panel.add(incomeLabel);
        panel.add(expenseLabel);

        panel.add(detailTextField);
        panel.add(incomeTextField);
        panel.add(expenseTextField);

        panel.add(addButton);
        panel.add(clearButton);
        panel.add(deleteButton);
        panel.add(deleteAllButton);

        return panel;
    }

    public JLabel generateFinancialEntryLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font14f);

        return label;
    }

    public JButton generateFinancialEntryButton(String text, Color color, int x, int y) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBounds(x, y, 120, 30);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(mainFont);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        return button;
    }

    public JTextField generateFinancialEntryTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setFont(font14f);
        textField.setBounds(x, y, width, height);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    public JTextField getDetailTextField() {
        return detailTextField;
    }

    public JTextField getIncomeTextField() {
        return incomeTextField;
    }

    public JTextField getExpenseTextField() {
        return expenseTextField;
    }

    public RecordComponent addRecord(String detailText, String incomeText, String expenseText) {
        // If detail is blank, return it.
        if (detailText.isBlank() || detailText.isEmpty()) return null;

        int income = 0;
        int expense = 0;

        // Either incomeor expense can input.
        if (!incomeText.isBlank() && !incomeText.isEmpty()) income = Integer.parseInt(incomeText);
        else if (!expenseText.isBlank() && !expenseText.isEmpty()) expense = Integer.parseInt(expenseText);
        else return null;

        // Random Id
        String id = UUID.randomUUID().toString();

        RecordComponent newRecord = new RecordComponent(this, id, detailText, income, expense);

        records.add(newRecord);

        // Update UI
        updatePanelForScrollPane();
        updateHeaderListPanel();

        String pathFileName = String.format("%s.ser", id);

        // Write record into .ser file.
        try (ObjectOutputStream objectOut = new ObjectOutputStream(
                new FileOutputStream("data/" + pathFileName))) {
            objectOut.writeObject(newRecord);

            // Clear Input.
            clearInput();

            // Return new record for testing.
            return newRecord;
        } catch (IOException ex) {
            System.out.println("Error writing object to file: " + ex.getMessage());
        }

        return null;
    }

    public ArrayList<RecordComponent> getRecords() {
        return records;
    }

    public class NumberListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();

            // Only input 0-9 numbers
            if (c < '0' || c > '9') {
                e.consume();
            }

            // Either incomeor expense can input.
            if (e.getSource() == incomeTextField) {
                expenseTextField.setText("");
            } else if (e.getSource() == expenseTextField) {
                incomeTextField.setText("");
            }
        }
    }
}
