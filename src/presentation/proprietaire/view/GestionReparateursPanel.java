package presentation.proprietaire.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import presentation.proprietaire.model.ReparateurTableModel;

public class GestionReparateursPanel extends JPanel {

    private final JTable table;
    private final JButton btnAjouter;
    private final JButton btnModifier;
    private final JButton btnSupprimer;
    private final JButton btnRafraichir;
    private final JButton btnEnvoyerIdentifiants;
    private final JTextField txtRecherche;
    private final JButton btnRechercher;

    public GestionReparateursPanel(ReparateurTableModel model) {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Table ---
        table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);

        // --- Buttons ---
        btnRafraichir = new JButton("Rafraîchir");
        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");
        btnEnvoyerIdentifiants = new JButton("Envoyer identifiants");

        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(btnRafraichir);
        tb.add(btnAjouter);
        tb.add(btnModifier);
        tb.add(btnSupprimer);
        tb.add(btnEnvoyerIdentifiants);

        // --- Search ---
        txtRecherche = new JTextField(15);
        btnRechercher = new JButton("Rechercher");

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(txtRecherche);
        searchPanel.add(btnRechercher);

        // --- Section panel ---
        JPanel section = new JPanel(new BorderLayout(5, 5));
        JLabel title = new JLabel("Gestion des réparateurs");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        section.add(title, BorderLayout.NORTH);
        section.add(searchPanel, BorderLayout.CENTER);
        section.add(tb, BorderLayout.SOUTH);

        add(section, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // Simple table styling
    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    // --- Getters ---
    public JTable getTable() { return table; }
    public JButton getBtnAjouter() { return btnAjouter; }
    public JButton getBtnModifier() { return btnModifier; }
    public JButton getBtnSupprimer() { return btnSupprimer; }
    public JButton getBtnRafraichir() { return btnRafraichir; }
    public JButton getBtnEnvoyerIdentifiants() { return btnEnvoyerIdentifiants; }
    public JTextField getTxtRecherche() { return txtRecherche; }
    public JButton getBtnRechercher() { return btnRechercher; }
}
