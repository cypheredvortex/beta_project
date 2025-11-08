package presentation.proprietaire.view;

import presentation.proprietaire.model.ReparationTableModel;

import javax.swing.*;
import java.awt.*;

public class ListeReparationsPanel extends JPanel {
    private final JTable table;
    private final JButton btnRafraichir;
    private final JButton btnAjouter;
    private final JButton btnChangerEtat;
    private final JButton btnVoirCode;
    private final JButton btnRegenererCode;

    public ListeReparationsPanel(ReparationTableModel model) {
        setLayout(new BorderLayout());

        // Buttons
        btnRafraichir = new JButton("Rafraîchir");
        btnAjouter = new JButton("Ajouter");
        btnChangerEtat = new JButton("Changer état...");
        btnVoirCode = new JButton("Voir code");
        btnRegenererCode = new JButton("Regénérer code");

        // Toolbar
        JToolBar tb = new JToolBar();
        tb.add(btnRafraichir);
        tb.add(btnAjouter);
        tb.add(btnChangerEtat);
        tb.add(btnVoirCode);
        tb.add(btnRegenererCode);

        // Table
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);

        // Section panel with title
        JPanel section = new JPanel(new BorderLayout());
        section.add(tb, BorderLayout.NORTH);
        section.add(scroll, BorderLayout.CENTER);

        // Title label
        JLabel title = new JLabel("Liste des réparations");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(title, BorderLayout.NORTH);
        add(section, BorderLayout.CENTER);
    }

    // Getters
    public JTable getTable() { return table; }
    public JButton getBtnRafraichir() { return btnRafraichir; }
    public JButton getBtnAjouter() { return btnAjouter; }
    public JButton getBtnChangerEtat() { return btnChangerEtat; }
    public JButton getBtnVoirCode() { return btnVoirCode; }
    public JButton getBtnRegenererCode() { return btnRegenererCode; }
}
