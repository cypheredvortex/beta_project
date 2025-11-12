package presentation;

import javax.swing.*;
import java.io.File;

public class TestImage {
	public static void main(String[] args) {
        String path = "C:\\Users\\Dell\\OneDrive\\Pictures\\client2.webp";
        File f = new File(path);
        System.out.println("Exists: " + f.exists());
        ImageIcon icon = new ImageIcon(path);
        JLabel label = new JLabel(icon);
        JOptionPane.showMessageDialog(null, label);
    }
}
