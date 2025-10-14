package application;

import config.LayoutConfig;
import config.StyleConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class MainFrame extends JFrame {
    public MainFrame() {
        add(new TransactionTrackerPanel());

        setVisible(true);
        setTitle("Transaction Tracker");
        setResizable(false);
        setSize(LayoutConfig.SCREEN_WIDTH, LayoutConfig.SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png"))).getImage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }


}