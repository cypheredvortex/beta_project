package presentation.proprietaire.view;

import javax.swing.*;
import java.awt.*;
import presentation.proprietaire.model.CaisseTableModel;

public class CaisseGlobalePanel extends JPanel {
    private final JTable table;
    private final JButton btnAjouterEntree;
    private final JButton btnAjouterSortie;
    private final JButton btnSupprimer;
    private final JButton btnRafraichir;
    private final JLabel lblSolde;

    public CaisseGlobalePanel(CaisseTableModel model) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // === TABLE ===
        table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === BUTTONS ===
        btnRafraichir = new JButton("Rafraîchir");
        btnAjouterEntree = new JButton("+ Entrée");
        btnAjouterSortie = new JButton("- Sortie");
        btnSupprimer = new JButton("Supprimer");

        styleButton(btnRafraichir, new Color(52, 152, 219));
        styleButton(btnAjouterEntree, new Color(46, 204, 113));
        styleButton(btnAjouterSortie, new Color(231, 76, 60));
        styleButton(btnSupprimer, new Color(155, 89, 182));

        // Toolbar
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setBackground(Color.WHITE);
        tb.add(btnRafraichir);
        tb.addSeparator();
        tb.add(btnAjouterEntree);
        tb.add(btnAjouterSortie);
        tb.addSeparator();
        tb.add(btnSupprimer);

        // === HEADER ===
        lblSolde = new JLabel("Solde: 0.00");
        lblSolde.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSolde.setForeground(new Color(44, 62, 80));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(lblSolde, BorderLayout.WEST);
        header.add(tb, BorderLayout.EAST);

        // === WRAPPER ===
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createTitledBorder("Caisse Globale"));
        content.setBackground(Color.WHITE);
        content.add(header, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    // ===== UI HELPERS =====
    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ===== GETTERS =====
    public JTable getTable() { return table; }
    public JButton getBtnAjouterEntree() { return btnAjouterEntree; }
    public JButton getBtnAjouterSortie() { return btnAjouterSortie; }
    public JButton getBtnSupprimer() { return btnSupprimer; }
    public JButton getBtnRafraichir() { return btnRafraichir; }
    public JLabel getLblSolde() { return lblSolde; }
}
