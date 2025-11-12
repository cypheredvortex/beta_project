package presentation.proprietaire.view;

import presentation.proprietaire.model.ReparationTableModel;

import javax.swing.*;
import javax.swing.table.*;
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
        setBackground(Color.WHITE);

        // Buttons
        btnRafraichir = new JButton("Rafraîchir");
        btnVoirCode = new JButton("Voir code");
        btnVoirAppareils = new JButton("Voir appareils");
        btnVoirClient = new JButton("Voir client");
        btnVoirReparateur = new JButton("Voir réparateur");

        // Toolbar
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(btnRafraichir);
        tb.add(btnVoirCode);
        tb.add(btnVoirAppareils);
        tb.add(btnVoirClient);
        tb.add(btnVoirReparateur);

        // Table
        table = new JTable(model) {
            // Allow multi-line cells
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent jc) {
                    jc.setToolTipText(getValueAt(row, column) == null ? "" : getValueAt(row, column).toString());
                }

                // Alternate row colors for clarity
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 247, 250) : Color.WHITE);
                } else {
                    c.setBackground(new Color(220, 230, 250));
                }
                return c;
            }
        };

        table.setRowHeight(60); // more vertical space for multi-line text
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // allow scroll horizontally
        table.setAutoCreateRowSorter(true);

        // Multi-line cell renderer
        TableCellRenderer multiLineRenderer = new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setText(value == null ? "" : value.toString());
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea area = new JTextArea(value == null ? "" : value.toString());
                area.setWrapStyleWord(true);
                area.setLineWrap(true);
                area.setFont(table.getFont());
                area.setOpaque(true);
                area.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

                if (isSelected) {
                    area.setBackground(new Color(220, 230, 250));
                } else {
                    area.setBackground(row % 2 == 0 ? new Color(245, 247, 250) : Color.WHITE);
                }

                return area;
            }
        };

        // Apply the multi-line renderer to all columns
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(multiLineRenderer);
        }

        // Dynamically adjust column width based on content
        autoResizeColumns(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);

        // Section panel with toolbar
        JPanel section = new JPanel(new BorderLayout());
        section.add(tb, BorderLayout.NORTH);
        section.add(scroll, BorderLayout.CENTER);

        // Title
        JLabel title = new JLabel("Liste des réparations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(title, BorderLayout.NORTH);
        add(section, BorderLayout.CENTER);
    }

    // Automatically adjust column widths based on preferred sizes
    private void autoResizeColumns(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 100; // min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 10, width);
            }
            if (width > 400) width = 400; // prevent too large columns
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    // Getters
    public JTable getTable() { return table; }
    public JButton getBtnRafraichir() { return btnRafraichir; }
    public JButton getBtnVoirCode() { return btnVoirCode; }
    public JButton getBtnVoirAppareils() { return btnVoirAppareils; }
    public JButton getBtnVoirClient() { return btnVoirClient; }
    public JButton getBtnVoirReparateur() { return btnVoirReparateur; }
}
