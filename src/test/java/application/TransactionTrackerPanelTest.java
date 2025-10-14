package application;

import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Initialize TransactionTrackerPanelTest")
class TransactionTrackerPanelTest {
    private TransactionTrackerPanel transactionTrackerPanel;

    @BeforeEach
    public void setUp() {
        transactionTrackerPanel = new TransactionTrackerPanel();
    }

    @Test
    @DisplayName("Should get financial entry panel")
    public void shouldGetFinancialEntryPanel() {
        assertNotNull(transactionTrackerPanel.generateFinancialEntryPanel());
    }

    @Test
    @DisplayName("Should get list panel")
    public void shouldGetListPanel() {
        assertNotNull(transactionTrackerPanel.generateListPanel());
    }

    @Test
    @DisplayName("Should load data properly")
    public void shouldLoadDataProperly() {
        transactionTrackerPanel.addRecord("Eggs", "0", "100");
        transactionTrackerPanel.addRecord("Milk", "0", "200");
        transactionTrackerPanel.addRecord("Earnings", "1500", "0");

        TransactionTrackerPanel newTransactionTrackerPanel = new TransactionTrackerPanel();

        assertFalse(newTransactionTrackerPanel.getRecords().isEmpty());
    }

    @Test
    @DisplayName("Should add a record properly")
    public void shouldAddRecordProperly() {
        transactionTrackerPanel.addRecord("Earnings", "1500", "0");
        assertFalse(transactionTrackerPanel.getRecords().isEmpty());
    }

    @Test
    @DisplayName("Should clear input properly")
    public void shouldClearInputProperly() {
        JTextField detailTextField = transactionTrackerPanel.getDetailTextField();
        JTextField incomeTextField = transactionTrackerPanel.getIncomeTextField();
        JTextField expenseTextField = transactionTrackerPanel.getExpenseTextField();

        detailTextField.setText("Cookie");
        incomeTextField.setText("150");

        assertEquals("Cookie", detailTextField.getText());
        assertEquals("150", incomeTextField.getText());

        transactionTrackerPanel.clearInput();

        assertEquals("", detailTextField.getText());
        assertEquals("", incomeTextField.getText());
    }

    @Test
    @DisplayName("Should delete selected record properly")
    public void shouldDeleteSelectedRecordProperly() {
        RecordComponent newRecord = transactionTrackerPanel.addRecord("Earnings", "1500", "0");

        transactionTrackerPanel.setSelectedRecord(newRecord);

        transactionTrackerPanel.deleteSelectedRecord();

        System.out.println(transactionTrackerPanel.getRecords().size());

        assertTrue(transactionTrackerPanel.getRecords().isEmpty());
    }

    @Test
    @DisplayName("Should delete all records properly")
    public void shouldDeleteAllRecordsProperly() {
        transactionTrackerPanel.addRecord("Earnings", "1500", "0");
        transactionTrackerPanel.addRecord("Chickens", "0", "100");
        transactionTrackerPanel.addRecord("Eggs", "0", "100");

        assertEquals(3, transactionTrackerPanel.getRecords().size());

        transactionTrackerPanel.deleteAllRecords();

        assertTrue(transactionTrackerPanel.getRecords().isEmpty());
    }

    @AfterEach
    public void cleanupTransactionRecords() {
        transactionTrackerPanel.deleteAllRecords();
    }
}