package presentation;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestion de réparation");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Create a panel for the top controls
        JPanel panel = new JPanel();
        panel.add(new JLabel("Input:"));
        panel.add(new JTextField(10));
        panel.add(new JButton("Click here"));

        // Create a scrollable text area
        JTextArea textarea = new JTextArea(10, 30);
        JScrollPane scroll = new JScrollPane(textarea);

        // Add components to the frame using BorderLayout
        frame.add(panel, BorderLayout.NORTH);  // top of window
        frame.add(scroll, BorderLayout.CENTER); // center area
        

        frame.setVisible(true);
    }
}
