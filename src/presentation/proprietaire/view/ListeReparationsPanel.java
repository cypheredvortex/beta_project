package presentation.proprietaire.view;

import presentation.proprietaire.model.ReparationTableModel;

import javax.swing.*;
import java.awt.*;

public class ListeReparationsPanel extends JPanel {
    private final JTable table;
    private final JButton btnRafraichir;
    private final JButton btnVoirCode;
    private final JButton btnVoirAppareils;
    private final JButton btnVoirClient;      
    private final JButton btnVoirReparateur;

    public ListeReparationsPanel(ReparationTableModel model) {
        setLayout(new BorderLayout());

        // Buttons
        btnRafraichir = new JButton("Rafraîchir");
        btnVoirCode = new JButton("Voir code");
        btnVoirAppareils = new JButton("Voir appareils");
        btnVoirClient = new JButton("Voir client");
        btnVoirReparateur = new JButton("Voir réparateur");

        // Toolbar
        JToolBar tb = new JToolBar();
        tb.add(btnRafraichir);
        tb.add(btnVoirCode);
        tb.add(btnVoirAppareils);
        tb.add(btnVoirClient);
        tb.add(btnVoirReparateur);

        // Table
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);

        // Section panel with toolbar
        JPanel section = new JPanel(new BorderLayout());
        section.add(tb, BorderLayout.NORTH);
        section.add(scroll, BorderLayout.CENTER);

        // Title
        JLabel title = new JLabel("Liste des réparations");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(title, BorderLayout.NORTH);
        add(section, BorderLayout.CENTER);
    }

    // Getters
    public JTable getTable() { return table; }
    public JButton getBtnRafraichir() { return btnRafraichir; }
    public JButton getBtnVoirCode() { return btnVoirCode; }
    public JButton getBtnVoirAppareils() { return btnVoirAppareils; }
    public JButton getBtnVoirClient() { return btnVoirClient; }
    public JButton getBtnVoirReparateur() { return btnVoirReparateur; }
}
